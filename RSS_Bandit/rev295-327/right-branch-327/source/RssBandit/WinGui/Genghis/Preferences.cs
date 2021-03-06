using System;
using System.Configuration;
using System.IO;
using System.IO.IsolatedStorage;
using System.Xml;
using System.Diagnostics;
using Timer = System.Threading.Timer;
using TimerCallback = System.Threading.TimerCallback;
using StringDictionary = System.Collections.Specialized.StringDictionary;
namespace Genghis
{
    public abstract class Preferences : IDisposable
    {
        static Type backingStore = null;
        string path;
        protected Preferences()
        {
            path = "";
        }
        protected Preferences(string path)
        {
            this.path = ValidatePath(path, "path");
        }
        public virtual void Dispose()
        {
        }
        public void Close()
        {
            Dispose();
        }
        private string ValidatePath(string path, string argumentName)
        {
            if (path.Length > 0 && path[path.Length - 1] != '/')
            {
                path = path + '/';
            }
            return path;
        }
        public string Path
        {
            get { return path; }
        }
        public abstract object GetProperty(string path, object defaultValue, Type returnType);
        public object GetProperty(string name, object defaultValue)
        {
            if (defaultValue == null)
            {
                throw new ArgumentNullException("defaultValue");
            }
            return GetProperty(name, defaultValue, defaultValue.GetType());
        }
        public string GetString(string name, string defaultValue)
        {
            return (string)GetProperty(name, defaultValue, typeof(string));
        }
        public bool GetBoolean(string name, bool defaultValue)
        {
            return (bool)GetProperty(name, defaultValue, typeof(bool));
        }
        public int GetInt32(string name, int defaultValue)
        {
            return (int)GetProperty(name, defaultValue, typeof(int));
        }
        public double GetInt64(string name, long defaultValue)
        {
            return (long)GetProperty(name, defaultValue, typeof(long));
        }
        public float GetSingle(string name, float defaultValue)
        {
            return (float)GetProperty(name, defaultValue, typeof(float));
        }
        public double GetDouble(string name, double defaultValue)
        {
            return (double)GetProperty(name, defaultValue, typeof(double));
        }
        public abstract void SetProperty(string name, object value);
        public abstract void Flush();
        public virtual Preferences GetSubnode(string subpath)
        {
            return (Preferences)Activator.CreateInstance(GetType(), new object[] { path + ValidatePath(subpath, "subpath") });
        }
        public static Preferences GetUserRoot()
        {
            return GetUserNode("");
        }
        public static Preferences GetUserNode(Type type)
        {
            string path = type.FullName;
            path = path.Replace('.', '/');
            return GetUserNode(path);
        }
        public static Preferences GetUserNode(string path)
        {
            if (path == null)
            {
                throw new ArgumentNullException("path");
            }
            if (backingStore == null)
            {
                try
                {
                    string backingStoreName = null;
                    try
                    {
                        AppSettingsReader appSettings = new AppSettingsReader();
                        backingStoreName = (string)appSettings.GetValue("CustomPreferencesStore", typeof(string));
                    }
                    catch
                    {
                        Trace.WriteLine("No custom data store specified (in application settings file).  Using default.");
                        throw;
                    }
                    try
                    {
                        backingStore = Type.GetType(backingStoreName, true);
                    }
                    catch
                    {
                        Trace.WriteLine("Could not load custom data store " + backingStoreName + ".  Using default.");
                        throw;
                    }
                }
                catch
                {
                    backingStore = typeof(IsolatedStorageUserPreferencesStore);
                }
            }
            return (Preferences)Activator.CreateInstance(backingStore, new object[] { path });
        }
    }
    class IsolatedStorageUserPreferencesStore : Preferences
    {
        static StringDictionary userStore;
        static bool userStoreModified;
        public IsolatedStorageUserPreferencesStore(string path) : base(path)
        {
            if (userStore == null)
            {
                userStore = new StringDictionary();
                Deserialize();
                userStoreModified = false;
                System.Windows.Forms.Application.ApplicationExit += new EventHandler(OnApplicationExit);
            }
        }
        public override object GetProperty(string name, object defaultValue, Type returnType)
        {
            string value = userStore[Path + name];
            if (value == null)
            {
                return defaultValue;
            }
            try
            {
                return Convert.ChangeType(value, returnType);
            }
            catch (Exception e)
            {
                Trace.WriteLine("Genghis.Preferences: The property " + name + " could not be converted to the intended type (" + returnType + ").  Using defaults.");
                Trace.WriteLine("Genghis.Preferences: The exception was: " + e.Message);
                return defaultValue;
            }
        }
        public override void SetProperty(string name, object value)
        {
            userStore[Path + name] = Convert.ToString(value);
            userStoreModified = true;
        }
        public override void Flush()
        {
            Serialize();
        }
        private static void OnApplicationExit(object sender, EventArgs e)
        {
            Serialize();
        }
        private static IsolatedStorageFileStream CreateSettingsStream()
        {
            IsolatedStorageFile store =
                IsolatedStorageFile.GetStore(
                    IsolatedStorageScope.User |
                    IsolatedStorageScope.Assembly |
                    IsolatedStorageScope.Domain |
                    IsolatedStorageScope.Roaming,
                    null, null);
            return new IsolatedStorageFileStream("preferences.xml",
                FileMode.Create, store);
        }
        private static IsolatedStorageFileStream OpenSettingsStream()
        {
            IsolatedStorageFile store =
                IsolatedStorageFile.GetStore(
                    IsolatedStorageScope.User |
                    IsolatedStorageScope.Assembly |
                    IsolatedStorageScope.Domain |
                    IsolatedStorageScope.Roaming,
                    null, null);
            return new IsolatedStorageFileStream("preferences.xml",
                FileMode.Open, store);
        }
        private static void Deserialize()
        {
            XmlTextReader reader = null;
            try
            {
                reader = new XmlTextReader(OpenSettingsStream());
                while (reader.Read())
                {
                    if (reader.NodeType == XmlNodeType.Element && reader.Name == "property")
                    {
                        string name = reader.GetAttribute("name");
                        string value = reader.ReadString();
                        userStore[name] = value;
                    }
                }
                reader.Close();
            }
            catch (Exception e)
            {
                if (reader != null)
                    reader.Close();
                Trace.WriteLine("Genghis.Preferences: There was an error while deserializing from Isolated Storage.  Ignoring.");
                Trace.WriteLine("Genghis.Preferences: The exception was: " + e.Message);
                Trace.WriteLine(e.StackTrace);
            }
        }
        private static void Serialize()
        {
            if (userStoreModified == false)
            {
                return;
            }
            XmlTextWriter writer = null;
            try
            {
                writer = new XmlTextWriter(CreateSettingsStream(), null);
                writer.Formatting = Formatting.Indented;
                writer.Indentation = 4;
                writer.WriteStartDocument(true);
                writer.WriteStartElement("preferences");
                foreach (System.Collections.DictionaryEntry entry in userStore)
                {
                    writer.WriteStartElement("property");
                    writer.WriteAttributeString("name", (string)entry.Key);
                    writer.WriteString((string)entry.Value);
                    writer.WriteEndElement();
                }
                writer.WriteEndElement();
                writer.WriteEndDocument();
                writer.Close();
                userStoreModified = false;
            }
            catch (Exception e)
            {
                if (writer != null)
                {
                    writer.Close();
                }
                Trace.WriteLine("Genghis.Preferences: There was an error while serializing to Isolated Storage.  Ignoring.");
                Trace.WriteLine("Genghis.Preferences: The exception was: " + e.Message);
                Trace.WriteLine(e.StackTrace);
            }
        }
    }
}
