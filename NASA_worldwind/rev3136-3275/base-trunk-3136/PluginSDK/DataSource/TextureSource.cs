using System;
using System.Drawing;
using System.Net;
using System.IO;
using System.Collections.Generic;
using System.Text;
using Microsoft.DirectX;
using Microsoft.DirectX.Direct3D;
using Utility;
using System.Reflection;
namespace WorldWind.DataSource
{
    public class TextureSource : IDisposable
    {
        private Texture m_texture = null;
        private DataRequest m_request = null;
        private DataRequestDescriptor m_drd;
        private ImageStore m_imageStore = null;
        private static Texture s_inQueueTexture = null;
        private static Texture s_inProgressTexture = null;
        private static Texture s_delayedTexture = null;
        private static Texture s_errorTexture = null;
        protected static WorldWindSettings m_settings = null;
        private static Object m_cacheLock = new Object();
        public int BasePriority
        {
            get { if (m_request != null) return m_drd.BasePriority; else return 0; }
            set { if (m_request != null) m_drd.BasePriority = value; }
        }
        public string Description
        {
            get { return m_drd.Description; }
            set { m_drd.Description = value; }
        }
        private static void InitStaticTextures()
        {
            Log.Write(Log.Levels.Debug, "initializing static textures...");
            Bitmap bmp = new Bitmap(512, 512, System.Drawing.Imaging.PixelFormat.Format24bppRgb);
            using (Graphics g = Graphics.FromImage(bmp))
            {
                g.Clear(Color.Blue);
            }
            s_inQueueTexture = new Texture(DrawArgs.Device, bmp, Usage.None, Pool.Managed);
            using (Graphics g = Graphics.FromImage(bmp))
            {
                g.Clear(Color.Green);
            }
            s_inProgressTexture = new Texture(DrawArgs.Device, bmp, Usage.None, Pool.Managed);
            using (Graphics g = Graphics.FromImage(bmp))
            {
                g.Clear(Color.Orange);
            }
            s_delayedTexture = new Texture(DrawArgs.Device, bmp, Usage.None, Pool.Managed);
            using (Graphics g = Graphics.FromImage(bmp))
            {
                g.Clear(Color.Red);
            }
            s_errorTexture = new Texture(DrawArgs.Device, bmp, Usage.None, Pool.Managed);
            Log.Write(Log.Levels.Debug, "done.");
        }
        public TextureSource(string url, string cacheLocation, ImageStore imageStore, PriorityCallback priorityCallback)
        {
            m_drd = new DataRequestDescriptor(url, cacheLocation, new CacheCallback(CacheCallback));
            m_drd.CompletionCallback = new CompletionCallback(CompletionCallback);
            m_drd.PriorityCallback = priorityCallback;
            m_imageStore = imageStore;
            if (m_settings == null)
            {
                Assembly a = Assembly.GetEntryAssembly();
                Type appType = a.GetType("WorldWind.MainApplication");
                System.Reflection.FieldInfo finfo = appType.GetField("Settings", BindingFlags.Static | BindingFlags.Public | BindingFlags.GetField);
                m_settings = finfo.GetValue(null) as WorldWindSettings;
            }
        }
        static private Stream CacheCallback(DataRequestDescriptor drd)
        {
            lock (m_cacheLock)
            {
                if (drd.CacheLocation == null)
                    return null;
                string cacheFullPath = drd.CacheLocation;
                if (File.Exists(cacheFullPath))
                    return new FileStream(cacheFullPath, FileMode.Open);
                const string ValidExtensions = ".bmp.dds.dib.hdr.jpg.jpeg.pfm.png.ppm.tga.gif.tif";
                {
                    string cacheSearchPath = Path.GetDirectoryName(cacheFullPath);
                    if (Directory.Exists(cacheSearchPath))
                    {
                        foreach (string imageFile in Directory.GetFiles(
                            cacheSearchPath,
                            Path.GetFileNameWithoutExtension(cacheFullPath) + ".*"))
                        {
                            string extension = Path.GetExtension(imageFile).ToLower();
                            if (ValidExtensions.IndexOf(extension) > 0)
                                return new FileStream(imageFile, FileMode.Open);
                        }
                    }
                }
                return null;
            }
        }
        private void CompletionCallback(DataRequest dr)
        {
            Texture texture = null;
            try
            {
                if (dr.Stream.CanSeek)
                    dr.Stream.Seek(0, SeekOrigin.Begin);
                texture = TextureLoader.FromStream(DrawArgs.Device, dr.Stream, 0, 0, 1, Usage.None, Format.A8R8G8B8, Pool.Managed, Filter.Box, Filter.Box, 0);
                if (texture == null)
                {
                    Log.Write(Log.Levels.Error, "TextureSource: conversion to texture failed!");
                }
            }
            catch (Microsoft.DirectX.Direct3D.InvalidDataException ex)
            {
                Log.Write(Log.Levels.Error, "TextureSource: conversion to texture failed - data not valid!");
                Log.Write(Log.Levels.Error, ex.ErrorString);
                Log.Write(Log.Levels.Error, ex.Message);
                Log.Write(ex);
                return;
            }
            if (!dr.CacheHit && (dr.CacheLocation != null))
            {
                lock (m_cacheLock)
                {
                    try
                    {
                        Directory.CreateDirectory(Path.GetDirectoryName(Path.GetFullPath(dr.CacheLocation)));
                        File.Delete(dr.CacheLocation);
                        if (World.Settings.ConvertDownloadedImagesToDds)
                        {
                            string ddsLocation = Path.Combine(
                                Path.GetDirectoryName(dr.CacheLocation),
                                Path.GetFileNameWithoutExtension(dr.CacheLocation) + ".dds");
                            TextureLoader.Save(ddsLocation, ImageFileFormat.Dds, texture);
                        }
                        else if (dr.Stream.CanSeek)
                        {
                            dr.Stream.Seek(0, SeekOrigin.Begin);
                            FileStream cacheFile = new FileStream(dr.CacheLocation, FileMode.Create);
                            int Length = 256;
                            Byte[] buffer = new Byte[Length];
                            int bytesRead = dr.Stream.Read(buffer, 0, Length);
                            while (bytesRead > 0)
                            {
                                cacheFile.Write(buffer, 0, bytesRead);
                                bytesRead = dr.Stream.Read(buffer, 0, Length);
                            }
                            cacheFile.Close();
                        }
                        else
                        {
                            Log.Write(Log.Levels.Error, "TextureSource: seeks not supported in stream - cannot save texture in cache!");
                        }
                    }
                    catch (Exception ex)
                    {
                        Log.Write(Log.Levels.Error, "could not save texture to cache in " + dr.CacheLocation);
                        Log.Write(ex);
                    }
                }
            }
            m_request = null;
            m_texture = PostProcessTexture(texture);
        }
        static public implicit operator Texture(TextureSource source)
        {
            if (source.m_texture == null)
            {
                if (source.m_request == null)
                {
                    source.m_request = DataStore.Request(source.m_drd);
                }
            }
            return source.m_texture;
        }
        private struct ColorType
        {
            public byte Blue;
            public byte Green;
            public byte Red;
            public byte Alpha;
            ColorType(byte r, byte g, byte b, byte a)
            {
                Red = r;
                Green = g;
                Blue = b;
                Alpha = a;
            }
            ColorType(ColorType c)
            {
                Red = c.Red;
                Green = c.Green;
                Blue = c.Blue;
                Alpha = c.Alpha;
            }
        };
        private Texture PostProcessTexture(Texture texture)
        {
            Log.Write(Log.Levels.Debug, "post processing texture for " + m_drd.Description);
            Surface surface = texture.GetSurfaceLevel(0);
            SurfaceDescription sd = texture.GetLevelDescription(0);
            ColorType[] cvs = (ColorType[])surface.LockRectangle(typeof(ColorType), LockFlags.None, sd.Width*sd.Height);
            if (m_imageStore.ColorKeyEnabled)
            {
                System.Drawing.Color c = Color.FromArgb(m_imageStore.ColorKey);
                for (int y = 0; y < sd.Height; y++)
                {
                    for (int x = 0; x < sd.Width; x++)
                    {
                        ColorType cv = cvs[x + y * sd.Width];
                        if (cv.Red == c.R && cv.Green == c.G && cv.Blue == c.B)
                            cv.Alpha = 0;
                        cvs[x + y * sd.Width] = cv;
                    }
                }
            }
            if (m_imageStore.AlphaKeyEnabled)
            {
                int blendFrom = m_imageStore.AlphaKeyMin * 3;
                int blendTo = m_imageStore.AlphaKeyMax * 3;
                for (int y = 0; y < sd.Height; y++)
                {
                    for (int x = 0; x < sd.Width; x++)
                    {
                        ColorType cv = cvs[x + y * sd.Width];
                        int grey = cv.Red + cv.Green + cv.Blue;
                        float scale = (grey - blendFrom) / (blendTo - blendFrom);
                        if (scale < 0) scale = 0;
                        if (scale > 1) scale = 1;
                        cv.Alpha = (byte)(255 * scale);
                        cvs[x + y * sd.Width] = cv;
                    }
                }
            }
            surface.UnlockRectangle();
            Log.Write(Log.Levels.Debug, "post processing finished for " + m_drd.Description);
            return texture;
        }
        public void Dispose()
        {
            if (m_request != null)
                m_request.Cancel();
            if(m_texture != null)
                m_texture.Dispose();
        }
    }
}
