using Microsoft.DirectX;
using Microsoft.DirectX.Direct3D;
using WorldWind.Terrain;
using WorldWind.Configuration;
using System;
using System.IO;
using WorldWind.Net;
namespace WorldWind.Renderable
{
 public class QuadTile : IDisposable
 {
  public enum ChildLocation
  {
   NorthWest,
   SouthWest,
   NorthEast,
   SouthEast
  }
  public QuadTileSet QuadTileSet;
  public double West;
  public double East;
  public double North;
  public double South;
  public Angle CenterLatitude;
  public Angle CenterLongitude;
  public double LatitudeSpan;
  public double LongitudeSpan;
  public int Level;
  public int Row;
  public int Col;
  public bool isInitialized;
  public BoundingBox BoundingBox;
  public GeoSpatialDownloadRequest DownloadRequest;
  protected Texture texture;
  protected static int vertexCount = 40;
  protected static int vertexCountElevated = 40;
  protected QuadTile northWestChild;
  protected QuadTile southWestChild;
  protected QuadTile northEastChild;
  protected QuadTile southEastChild;
  protected CustomVertex.PositionNormalTextured[] northWestVertices;
  protected CustomVertex.PositionNormalTextured[] southWestVertices;
  protected CustomVertex.PositionNormalTextured[] northEastVertices;
  protected CustomVertex.PositionNormalTextured[] southEastVertices;
  protected short[] vertexIndexes;
  Point3d localOrigin;
  bool m_isResetingCache;
  float verticalExaggeration;
  bool isDownloadingTerrain;
  public QuadTile(double south, double north, double west, double east, int level, QuadTileSet quadTileSet)
  {
   this.South = south;
   this.North = north;
   this.West = west;
   this.East = east;
   CenterLatitude = Angle.FromDegrees(0.5f * (North + South));
   CenterLongitude = Angle.FromDegrees(0.5f * (West + East));
   LatitudeSpan = Math.Abs(North - South);
   LongitudeSpan = Math.Abs(East - West);
   this.Level = level;
   this.QuadTileSet = quadTileSet;
   BoundingBox = new BoundingBox((float)south, (float)north, (float)west, (float)east,
        (float)quadTileSet.LayerRadius, (float)quadTileSet.LayerRadius + 300000f);
   localOrigin = MathEngine.SphericalToCartesianD(CenterLatitude, CenterLongitude, quadTileSet.LayerRadius);
   localOrigin.X = (float)(Math.Round(localOrigin.X / 10000) * 10000);
   localOrigin.Y = (float)(Math.Round(localOrigin.Y / 10000) * 10000);
   localOrigin.Z = (float)(Math.Round(localOrigin.Z / 10000) * 10000);
   Row = MathEngine.GetRowFromLatitude(South, North - South);
   Col = MathEngine.GetColFromLongitude(West, North - South);
  }
  public virtual void ResetCache()
  {
   try
   {
    m_isResetingCache = true;
    this.isInitialized = false;
    if (northEastChild != null)
    {
     northEastChild.ResetCache();
    }
    if (northWestChild != null)
    {
     northWestChild.ResetCache();
    }
    if (southEastChild != null)
    {
     southEastChild.ResetCache();
    }
    if (southWestChild != null)
    {
     southWestChild.ResetCache();
    }
    this.Dispose();
    QuadTileSet.ImageStore.DeleteLocalCopy(this);
    m_isResetingCache = false;
   }
   catch
   {
   }
  }
  private QuadTile ComputeChild(double childSouth, double childNorth, double childWest, double childEast)
  {
   QuadTile child = new QuadTile(
    childSouth,
    childNorth,
    childWest,
    childEast,
    this.Level + 1,
    QuadTileSet);
   return child;
  }
  public virtual void ComputeChildren(DrawArgs drawArgs)
  {
   if (Level + 1 >= QuadTileSet.ImageStore.LevelCount)
    return;
   double CenterLat = 0.5f * (South + North);
   double CenterLon = 0.5f * (East + West);
   if (northWestChild == null)
    northWestChild = ComputeChild(CenterLat, North, West, CenterLon);
   if (northEastChild == null)
    northEastChild = ComputeChild(CenterLat, North, CenterLon, East);
   if (southWestChild == null)
    southWestChild = ComputeChild(South, CenterLat, West, CenterLon);
   if (southEastChild == null)
    southEastChild = ComputeChild(South, CenterLat, CenterLon, East);
  }
  public virtual void Dispose()
  {
   try
   {
    isInitialized = false;
    if (texture != null && !texture.Disposed)
    {
     texture.Dispose();
     texture = null;
    }
    if (northWestChild != null)
    {
     northWestChild.Dispose();
     northWestChild = null;
    }
    if (southWestChild != null)
    {
     southWestChild.Dispose();
     southWestChild = null;
    }
    if (northEastChild != null)
    {
     northEastChild.Dispose();
     northEastChild = null;
    }
    if (southEastChild != null)
    {
     southEastChild.Dispose();
     southEastChild = null;
    }
    if(DownloadRequest != null)
    {
     QuadTileSet.RemoveFromDownloadQueue(DownloadRequest);
     DownloadRequest.Dispose();
     DownloadRequest = null;
    }
   }
   catch
   {
   }
  }
  public bool WaitingForDownload = false;
  public bool IsDownloadingImage = false;
  public virtual void Initialize()
  {
   if (m_isResetingCache)
    return;
   try
   {
    if (DownloadRequest != null)
    {
     return;
    }
    Texture newTexture = QuadTileSet.ImageStore.LoadFile(this);
    if (newTexture == null)
    {
     return;
    }
    if (texture != null)
     texture.Dispose();
    texture = newTexture;
    WaitingForDownload = false;
    IsDownloadingImage = false;
    CreateTileMesh();
   }
   catch(Exception)
   {
   }
   finally
   {
    isInitialized = true;
   }
  }
  public virtual void Update(DrawArgs drawArgs)
  {
   if (m_isResetingCache)
    return;
   try
   {
    double tileSize = North - South;
    if (!isInitialized)
    {
     if (DrawArgs.Camera.ViewRange * 0.5f < Angle.FromDegrees(QuadTileSet.TileDrawDistance * tileSize)
 && MathEngine.SphericalDistance(CenterLatitude, CenterLongitude,
       DrawArgs.Camera.Latitude, DrawArgs.Camera.Longitude) < Angle.FromDegrees(QuadTileSet.TileDrawSpread * tileSize * 1.25f)
 && DrawArgs.Camera.ViewFrustum.Intersects(BoundingBox)
      )
      Initialize();
    }
    if (isInitialized && World.Settings.VerticalExaggeration != verticalExaggeration || m_CurrentOpacity != QuadTileSet.Opacity ||
     QuadTileSet.RenderStruts != renderStruts)
    {
     CreateTileMesh();
    }
    if (isInitialized)
    {
     if (DrawArgs.Camera.ViewRange < Angle.FromDegrees(QuadTileSet.TileDrawDistance * tileSize)
 && MathEngine.SphericalDistance(CenterLatitude, CenterLongitude,
       DrawArgs.Camera.Latitude, DrawArgs.Camera.Longitude) < Angle.FromDegrees(QuadTileSet.TileDrawSpread * tileSize)
 && DrawArgs.Camera.ViewFrustum.Intersects(BoundingBox)
      )
     {
      if (northEastChild == null || northWestChild == null || southEastChild == null || southWestChild == null)
      {
       ComputeChildren(drawArgs);
      }
      if (northEastChild != null)
      {
       northEastChild.Update(drawArgs);
      }
      if (northWestChild != null)
      {
       northWestChild.Update(drawArgs);
      }
      if (southEastChild != null)
      {
       southEastChild.Update(drawArgs);
      }
      if (southWestChild != null)
      {
       southWestChild.Update(drawArgs);
      }
     }
     else
     {
      if (northWestChild != null)
      {
       northWestChild.Dispose();
       northWestChild = null;
      }
      if (northEastChild != null)
      {
       northEastChild.Dispose();
       northEastChild = null;
      }
      if (southEastChild != null)
      {
       southEastChild.Dispose();
       southEastChild = null;
      }
      if (southWestChild != null)
      {
       southWestChild.Dispose();
       southWestChild = null;
      }
     }
    }
    if (isInitialized)
    {
     if (DrawArgs.Camera.ViewRange / 2 > Angle.FromDegrees(QuadTileSet.TileDrawDistance * tileSize * 1.5f)
       || MathEngine.SphericalDistance(CenterLatitude, CenterLongitude, DrawArgs.Camera.Latitude, DrawArgs.Camera.Longitude) > Angle.FromDegrees(QuadTileSet.TileDrawSpread * tileSize * 1.5f))
     {
      if (Level != 0 || (Level == 0 && !QuadTileSet.AlwaysRenderBaseTiles))
       this.Dispose();
     }
    }
   }
   catch
   {
   }
  }
  bool renderStruts = true;
  public virtual void CreateTileMesh()
  {
   verticalExaggeration = World.Settings.VerticalExaggeration;
   m_CurrentOpacity = QuadTileSet.Opacity;
   renderStruts = QuadTileSet.RenderStruts;
   if (QuadTileSet.TerrainMapped && Math.Abs(verticalExaggeration) > 1e-3)
    CreateElevatedMesh();
   else
    CreateFlatMesh();
  }
  protected virtual void CreateFlatMesh()
  {
   double layerRadius = (double)QuadTileSet.LayerRadius;
   double scaleFactor = 1.0 / (double)vertexCount;
   int thisVertexCount = vertexCount / 2 + (vertexCount % 2);
   int thisVertexCountPlus1 = thisVertexCount + 1;
   int totalVertexCount = thisVertexCountPlus1 * thisVertexCountPlus1;
   northWestVertices = new CustomVertex.PositionNormalTextured[totalVertexCount];
   southWestVertices = new CustomVertex.PositionNormalTextured[totalVertexCount];
   northEastVertices = new CustomVertex.PositionNormalTextured[totalVertexCount];
   southEastVertices = new CustomVertex.PositionNormalTextured[totalVertexCount];
   const double Degrees2Radians = System.Math.PI / 180.0;
   double[] sinLon = new double[thisVertexCountPlus1];
   double[] cosLon = new double[thisVertexCountPlus1];
   int baseIndex;
   double angle = West * Degrees2Radians;
   double deltaAngle = scaleFactor * LongitudeSpan * Degrees2Radians;
   for (int i = 0; i < thisVertexCountPlus1; i++)
   {
    angle = West * Degrees2Radians + i * deltaAngle;
    sinLon[i] = Math.Sin(angle);
    cosLon[i] = Math.Cos(angle);
   }
   baseIndex = 0;
   angle = North * Degrees2Radians;
   deltaAngle = -scaleFactor * LatitudeSpan * Degrees2Radians;
   for (int i = 0; i < thisVertexCountPlus1; i++)
   {
    angle = North * Degrees2Radians + i * deltaAngle;
    double sinLat = Math.Sin(angle);
    double radCosLat = Math.Cos(angle) * layerRadius;
    for (int j = 0; j < thisVertexCountPlus1; j++)
    {
     northWestVertices[baseIndex].X = (float)(radCosLat * cosLon[j] - localOrigin.X);
     northWestVertices[baseIndex].Y = (float)(radCosLat * sinLon[j] - localOrigin.Y);
     northWestVertices[baseIndex].Z = (float)(layerRadius * sinLat - localOrigin.Z);
     northWestVertices[baseIndex].Tu = (float)(j * scaleFactor);
     northWestVertices[baseIndex].Tv = (float)(i * scaleFactor);
     baseIndex += 1;
    }
   }
   baseIndex = 0;
   angle = 0.5 * (North + South) * Degrees2Radians;
   for (int i = 0; i < thisVertexCountPlus1; i++)
   {
    angle = 0.5 * (North + South) * Degrees2Radians + i * deltaAngle;
    double sinLat = Math.Sin(angle);
    double radCosLat = Math.Cos(angle) * layerRadius;
    for (int j = 0; j < thisVertexCountPlus1; j++)
    {
     southWestVertices[baseIndex].X = (float)(radCosLat * cosLon[j] - localOrigin.X);
     southWestVertices[baseIndex].Y = (float)(radCosLat * sinLon[j] - localOrigin.Y);
     southWestVertices[baseIndex].Z = (float)(layerRadius * sinLat - localOrigin.Z);
     southWestVertices[baseIndex].Tu = (float)(j * scaleFactor);
     southWestVertices[baseIndex].Tv = (float)((i + thisVertexCount) * scaleFactor);
     baseIndex += 1;
    }
   }
   angle = 0.5 * (West + East) * Degrees2Radians;
   deltaAngle = scaleFactor * LongitudeSpan * Degrees2Radians;
   for (int i = 0; i < thisVertexCountPlus1; i++)
   {
    angle = 0.5 * (West + East) * Degrees2Radians + i * deltaAngle;
    sinLon[i] = Math.Sin(angle);
    cosLon[i] = Math.Cos(angle);
   }
   baseIndex = 0;
   angle = North * Degrees2Radians;
   deltaAngle = -scaleFactor * LatitudeSpan * Degrees2Radians;
   for (int i = 0; i < thisVertexCountPlus1; i++)
   {
    angle = North * Degrees2Radians + i * deltaAngle;
    double sinLat = Math.Sin(angle);
    double radCosLat = Math.Cos(angle) * layerRadius;
    for (int j = 0; j < thisVertexCountPlus1; j++)
    {
     northEastVertices[baseIndex].X = (float)(radCosLat * cosLon[j] - localOrigin.X);
     northEastVertices[baseIndex].Y = (float)(radCosLat * sinLon[j] - localOrigin.Y);
     northEastVertices[baseIndex].Z = (float)(layerRadius * sinLat - localOrigin.Z);
     northEastVertices[baseIndex].Tu = (float)((j + thisVertexCount) * scaleFactor);
     northEastVertices[baseIndex].Tv = (float)(i * scaleFactor);
     baseIndex += 1;
    }
   }
   baseIndex = 0;
   angle = 0.5f * (North + South) * Degrees2Radians;
   for (int i = 0; i < thisVertexCountPlus1; i++)
   {
    angle = 0.5 * (North + South) * Degrees2Radians + i * deltaAngle;
    double sinLat = Math.Sin(angle);
    double radCosLat = Math.Cos(angle) * layerRadius;
    for (int j = 0; j < thisVertexCountPlus1; j++)
    {
     southEastVertices[baseIndex].X = (float)(radCosLat * cosLon[j] - localOrigin.X);
     southEastVertices[baseIndex].Y = (float)(radCosLat * sinLon[j] - localOrigin.Y);
     southEastVertices[baseIndex].Z = (float)(layerRadius * sinLat - localOrigin.Z);
     southEastVertices[baseIndex].Tu = (float)((j + thisVertexCount) * scaleFactor);
     southEastVertices[baseIndex].Tv = (float)((i + thisVertexCount) * scaleFactor);
     baseIndex += 1;
    }
   }
   vertexIndexes = new short[2 * thisVertexCount * thisVertexCount * 3];
   for (int i = 0; i < thisVertexCount; i++)
   {
    baseIndex = (2 * 3 * i * thisVertexCount);
    for (int j = 0; j < thisVertexCount; j++)
    {
     vertexIndexes[baseIndex] = (short)(i * thisVertexCountPlus1 + j);
     vertexIndexes[baseIndex + 1] = (short)((i + 1) * thisVertexCountPlus1 + j);
     vertexIndexes[baseIndex + 2] = (short)(i * thisVertexCountPlus1 + j + 1);
     vertexIndexes[baseIndex + 3] = (short)(i * thisVertexCountPlus1 + j + 1);
     vertexIndexes[baseIndex + 4] = (short)((i + 1) * thisVertexCountPlus1 + j);
     vertexIndexes[baseIndex + 5] = (short)((i + 1) * thisVertexCountPlus1 + j + 1);
     baseIndex += 6;
    }
   }
  }
  double meshBaseRadius = 0;
  protected virtual void CreateElevatedMesh()
  {
   isDownloadingTerrain = true;
   TerrainTile tile = QuadTileSet.World.TerrainAccessor.GetElevationArray(North, South, West, East, vertexCountElevated + 1);
   float[,] heightData = tile.ElevationData;
   int vertexCountElevatedPlus3 = vertexCountElevated / 2 + 3;
   int totalVertexCount = vertexCountElevatedPlus3 * vertexCountElevatedPlus3;
   northWestVertices = new CustomVertex.PositionNormalTextured[totalVertexCount];
   southWestVertices = new CustomVertex.PositionNormalTextured[totalVertexCount];
   northEastVertices = new CustomVertex.PositionNormalTextured[totalVertexCount];
   southEastVertices = new CustomVertex.PositionNormalTextured[totalVertexCount];
   double layerRadius = (double)QuadTileSet.LayerRadius;
   float minimumElevation = float.MaxValue;
   float maximumElevation = float.MinValue;
   foreach (float height in heightData)
   {
    if (height < minimumElevation)
     minimumElevation = height;
    if (height > maximumElevation)
     maximumElevation = height;
   }
   minimumElevation *= verticalExaggeration;
   maximumElevation *= verticalExaggeration;
   if (minimumElevation > maximumElevation)
   {
    minimumElevation = maximumElevation;
    maximumElevation = minimumElevation;
   }
   double overlap = 500 * verticalExaggeration;
   meshBaseRadius = layerRadius + minimumElevation - overlap;
   CreateElevatedMesh(ChildLocation.NorthWest, northWestVertices, meshBaseRadius, heightData);
   CreateElevatedMesh(ChildLocation.SouthWest, southWestVertices, meshBaseRadius, heightData);
   CreateElevatedMesh(ChildLocation.NorthEast, northEastVertices, meshBaseRadius, heightData);
   CreateElevatedMesh(ChildLocation.SouthEast, southEastVertices, meshBaseRadius, heightData);
   BoundingBox = new BoundingBox((float)South, (float)North, (float)West, (float)East,
    (float)layerRadius, (float)layerRadius + 10000 * this.verticalExaggeration);
   QuadTileSet.IsDownloadingElevation = false;
   int vertexCountElevatedPlus2 = vertexCountElevated / 2 + 2;
   vertexIndexes = new short[2 * vertexCountElevatedPlus2 * vertexCountElevatedPlus2 * 3];
   int elevated_idx = 0;
   for (int i = 0; i < vertexCountElevatedPlus2; i++)
   {
    for (int j = 0; j < vertexCountElevatedPlus2; j++)
    {
     vertexIndexes[elevated_idx++] = (short)(i * vertexCountElevatedPlus3 + j);
     vertexIndexes[elevated_idx++] = (short)((i + 1) * vertexCountElevatedPlus3 + j);
     vertexIndexes[elevated_idx++] = (short)(i * vertexCountElevatedPlus3 + j + 1);
     vertexIndexes[elevated_idx++] = (short)(i * vertexCountElevatedPlus3 + j + 1);
     vertexIndexes[elevated_idx++] = (short)((i + 1) * vertexCountElevatedPlus3 + j);
     vertexIndexes[elevated_idx++] = (short)((i + 1) * vertexCountElevatedPlus3 + j + 1);
    }
   }
   calculate_normals(ref northWestVertices, vertexIndexes);
   calculate_normals(ref southWestVertices, vertexIndexes);
   calculate_normals(ref northEastVertices, vertexIndexes);
   calculate_normals(ref southEastVertices, vertexIndexes);
   isDownloadingTerrain = false;
  }
  byte m_CurrentOpacity = 255;
  protected void CreateElevatedMesh(ChildLocation corner, CustomVertex.PositionNormalTextured[] vertices,
   double meshBaseRadius, float[,] heightData)
  {
   double north = MathEngine.DegreesToRadians(North);
   double west = MathEngine.DegreesToRadians(West);
   float TuOffset = 0;
   float TvOffset = 0;
   switch (corner)
   {
    case ChildLocation.NorthWest:
     break;
    case ChildLocation.NorthEast:
     west = MathEngine.DegreesToRadians(0.5 * (West + East));
     TuOffset = 0.5f;
     break;
    case ChildLocation.SouthWest:
     north = MathEngine.DegreesToRadians(0.5 * (North + South));
     TvOffset = 0.5f;
     break;
    case ChildLocation.SouthEast:
     north = MathEngine.DegreesToRadians(0.5 * (North + South));
     west = MathEngine.DegreesToRadians(0.5 * (West + East));
     TuOffset = 0.5f;
     TvOffset = 0.5f;
     break;
   }
   double latitudeRadianSpan = MathEngine.DegreesToRadians(LatitudeSpan);
   double longitudeRadianSpan = MathEngine.DegreesToRadians(LongitudeSpan);
   double layerRadius = (double)QuadTileSet.LayerRadius;
   double scaleFactor = 1.0 / vertexCountElevated;
   int terrainLongitudeIndex = (int)(TuOffset * vertexCountElevated);
   int terrainLatitudeIndex = (int)(TvOffset * vertexCountElevated);
   int vertexCountElevatedPlus1 = vertexCountElevated / 2 + 1;
   double radius = 0;
   int vertexIndex = 0;
   for (int latitudeIndex = -1; latitudeIndex <= vertexCountElevatedPlus1; latitudeIndex++)
   {
    int latitudePoint = latitudeIndex;
    if (latitudePoint < 0)
     latitudePoint = 0;
    else if (latitudePoint >= vertexCountElevatedPlus1)
     latitudePoint = vertexCountElevatedPlus1 - 1;
    double latitudeFactor = latitudePoint * scaleFactor;
    double latitude = north - latitudeFactor * latitudeRadianSpan;
    double cosLat = Math.Cos(latitude);
    double sinLat = Math.Sin(latitude);
    for (int longitudeIndex = -1; longitudeIndex <= vertexCountElevatedPlus1; longitudeIndex++)
    {
     int longitudePoint = longitudeIndex;
     if (longitudePoint < 0)
      longitudePoint = 0;
     else if (longitudePoint >= vertexCountElevatedPlus1)
      longitudePoint = vertexCountElevatedPlus1 - 1;
     if (longitudeIndex != longitudePoint || latitudeIndex != latitudePoint)
     {
      if(heightData != null)
      {
       radius = layerRadius +
        heightData[terrainLatitudeIndex + latitudePoint, terrainLongitudeIndex + longitudePoint]
        * verticalExaggeration;
      }
      else
      {
       radius = meshBaseRadius;
      }
     }
     else
     {
      radius = layerRadius +
       heightData[terrainLatitudeIndex + latitudeIndex, terrainLongitudeIndex + longitudeIndex]
       * verticalExaggeration;
     }
     double longitudeFactor = longitudePoint * scaleFactor;
     vertices[vertexIndex].Tu = TuOffset + (float)longitudeFactor;
     vertices[vertexIndex].Tv = TvOffset + (float)latitudeFactor;
     double longitude = west + longitudeFactor * longitudeRadianSpan;
     double radCosLat = radius * cosLat;
     vertices[vertexIndex].X = (float)(radCosLat * Math.Cos(longitude) - localOrigin.X);
     vertices[vertexIndex].Y = (float)(radCosLat * Math.Sin(longitude) - localOrigin.Y);
     vertices[vertexIndex].Z = (float)(radius * sinLat - localOrigin.Z);
     vertexIndex++;
    }
   }
  }
  private void calculate_normals(ref CustomVertex.PositionNormalTextured[] vertices, short[] indices)
  {
   System.Collections.ArrayList[] normal_buffer = new System.Collections.ArrayList[vertices.Length];
   for(int i = 0; i < vertices.Length; i++)
   {
    normal_buffer[i] = new System.Collections.ArrayList();
   }
   for(int i = 0; i < indices.Length; i += 3)
   {
    Vector3 p1 = vertices[indices[i+0]].Position;
    Vector3 p2 = vertices[indices[i+1]].Position;
    Vector3 p3 = vertices[indices[i+2]].Position;
    Vector3 v1 = p2 - p1;
    Vector3 v2 = p3 - p1;
    Vector3 normal = Vector3.Cross(v1, v2);
    normal.Normalize();
    normal_buffer[indices[i+0]].Add( normal );
    normal_buffer[indices[i+1]].Add( normal );
    normal_buffer[indices[i+2]].Add( normal );
   }
   for( int i = 0; i < vertices.Length; ++i )
   {
    for( int j = 0; j < normal_buffer[i].Count; ++j )
    {
     Vector3 curNormal = (Vector3)normal_buffer[i][j];
     if(vertices[i].Normal == Vector3.Empty)
      vertices[i].Normal = curNormal;
     else
      vertices[i].Normal += curNormal;
    }
    vertices[i].Normal.Multiply(1.0f / normal_buffer[i].Count);
   }
   if(renderStruts && m_CurrentOpacity == 255)
   {
    short vertexDensity = (short)Math.Sqrt(vertices.Length);
    for(int i = 0; i < vertexDensity; i++)
    {
     if(i == 0 || i == vertexDensity - 1)
     {
      for(int j = 0; j < vertexDensity; j++)
      {
       Point3d p = new Point3d(vertices[i * vertexDensity + j].Position.X, vertices[i * vertexDensity + j].Position.Y, vertices[i * vertexDensity + j].Position.Z);
       p = p + this.localOrigin;
       p = p.normalize();
       p = p * meshBaseRadius - this.localOrigin;
       vertices[i * vertexDensity + j].Position = new Vector3((float)p.X, (float)p.Y, (float)p.Z);
      }
     }
     else
     {
      Point3d p = new Point3d(vertices[i * vertexDensity].Position.X, vertices[i * vertexDensity].Position.Y, vertices[i * vertexDensity].Position.Z);
      p = p + this.localOrigin;
      p = p.normalize();
      p = p * meshBaseRadius - this.localOrigin;
      vertices[i * vertexDensity].Position = new Vector3((float)p.X, (float)p.Y, (float)p.Z);
      p = new Point3d(vertices[i * vertexDensity + vertexDensity - 1].Position.X, vertices[i * vertexDensity + vertexDensity - 1].Position.Y, vertices[i * vertexDensity + vertexDensity - 1].Position.Z);
      p = p + this.localOrigin;
      p = p.normalize();
      p = p * meshBaseRadius - this.localOrigin;
      vertices[i * vertexDensity + vertexDensity - 1].Position = new Vector3((float)p.X, (float)p.Y, (float)p.Z);
     }
    }
   }
  }
  public string ImageFilePath = null;
  public virtual bool Render(DrawArgs drawArgs)
  {
   try
   {
    if (!isInitialized ||
     this.northWestVertices == null ||
     this.northEastVertices == null ||
     this.southEastVertices == null ||
     this.southWestVertices == null)
     return false;
    if (!DrawArgs.Camera.ViewFrustum.Intersects(BoundingBox))
     return false;
    bool northWestChildRendered = false;
    bool northEastChildRendered = false;
    bool southWestChildRendered = false;
    bool southEastChildRendered = false;
    if (northWestChild != null)
     if (northWestChild.Render(drawArgs))
     northWestChildRendered = true;
    if (southWestChild != null)
     if (southWestChild.Render(drawArgs))
     southWestChildRendered = true;
    if (northEastChild != null)
     if (northEastChild.Render(drawArgs))
     northEastChildRendered = true;
    if (southEastChild != null)
     if (southEastChild.Render(drawArgs))
     southEastChildRendered = true;
    if(QuadTileSet.RenderFileNames &&
     (!northWestChildRendered || !northEastChildRendered || !southWestChildRendered || !southEastChildRendered))
    {
     Vector3 referenceCenter = new Vector3(
      (float)drawArgs.WorldCamera.ReferenceCenter.X,
      (float)drawArgs.WorldCamera.ReferenceCenter.Y,
      (float)drawArgs.WorldCamera.ReferenceCenter.Z);
     RenderDownloadRectangle(drawArgs, System.Drawing.Color.FromArgb( 255, 0, 0).ToArgb(), referenceCenter);
     Vector3 cartesianPoint = MathEngine.SphericalToCartesian(
      CenterLatitude.Degrees,
      CenterLongitude.Degrees,
      drawArgs.WorldCamera.WorldRadius + drawArgs.WorldCamera.TerrainElevation);
     if(ImageFilePath != null && drawArgs.WorldCamera.ViewFrustum.ContainsPoint(cartesianPoint))
     {
      Vector3 projectedPoint = drawArgs.WorldCamera.Project(cartesianPoint - referenceCenter);
      System.Drawing.Rectangle rect = new System.Drawing.Rectangle(
       (int)projectedPoint.X - 100,
       (int)projectedPoint.Y,
       200,
       200);
      drawArgs.defaultDrawingFont.DrawText(
       null,
       ImageFilePath,
       rect,
       DrawTextFormat.WordBreak,
       System.Drawing.Color.Red);
     }
    }
    if(northWestChildRendered && northEastChildRendered && southWestChildRendered && southEastChildRendered)
    {
     return true;
    }
    if (texture == null || texture.Disposed)
     return false;
    Device device = DrawArgs.Device;
    device.SetTexture(0, texture);
    drawArgs.numberTilesDrawn++;
    DrawArgs.Device.Transform.World = Matrix.Translation(
     (float)(localOrigin.X - drawArgs.WorldCamera.ReferenceCenter.X),
     (float)(localOrigin.Y - drawArgs.WorldCamera.ReferenceCenter.Y),
     (float)(localOrigin.Z - drawArgs.WorldCamera.ReferenceCenter.Z)
     );
    if(!northWestChildRendered)
     Render(device, northWestVertices, northWestChild);
    if(!southWestChildRendered)
     Render(device, southWestVertices, southWestChild);
    if(!northEastChildRendered)
     Render(device, northEastVertices, northEastChild);
    if(!southEastChildRendered)
     Render(device, southEastVertices, southEastChild);
    DrawArgs.Device.Transform.World = DrawArgs.Camera.WorldMatrix;
    return true;
   }
   catch (DirectXException)
   {
   }
   return false;
  }
  CustomVertex.PositionColored[] downloadRectangle = new CustomVertex.PositionColored[5];
  public void RenderDownloadRectangle(DrawArgs drawArgs, int color, Vector3 referenceCenter)
  {
   Vector3 northWestV = MathEngine.SphericalToCartesian((float)North, (float)West, QuadTileSet.LayerRadius) - referenceCenter;
   Vector3 southWestV = MathEngine.SphericalToCartesian((float)South, (float)West, QuadTileSet.LayerRadius) - referenceCenter;
   Vector3 northEastV = MathEngine.SphericalToCartesian((float)North, (float)East, QuadTileSet.LayerRadius) - referenceCenter;
   Vector3 southEastV = MathEngine.SphericalToCartesian((float)South, (float)East, QuadTileSet.LayerRadius) - referenceCenter;
   downloadRectangle[0].X = northWestV.X;
   downloadRectangle[0].Y = northWestV.Y;
   downloadRectangle[0].Z = northWestV.Z;
   downloadRectangle[0].Color = color;
   downloadRectangle[1].X = southWestV.X;
   downloadRectangle[1].Y = southWestV.Y;
   downloadRectangle[1].Z = southWestV.Z;
   downloadRectangle[1].Color = color;
   downloadRectangle[2].X = southEastV.X;
   downloadRectangle[2].Y = southEastV.Y;
   downloadRectangle[2].Z = southEastV.Z;
   downloadRectangle[2].Color = color;
   downloadRectangle[3].X = northEastV.X;
   downloadRectangle[3].Y = northEastV.Y;
   downloadRectangle[3].Z = northEastV.Z;
   downloadRectangle[3].Color = color;
   downloadRectangle[4].X = downloadRectangle[0].X;
   downloadRectangle[4].Y = downloadRectangle[0].Y;
   downloadRectangle[4].Z = downloadRectangle[0].Z;
   downloadRectangle[4].Color = color;
   drawArgs.device.RenderState.ZBufferEnable = false;
   drawArgs.device.VertexFormat = CustomVertex.PositionColored.Format;
   drawArgs.device.TextureState[0].ColorOperation = TextureOperation.Disable;
   drawArgs.device.DrawUserPrimitives(PrimitiveType.LineStrip, 4, downloadRectangle);
   drawArgs.device.TextureState[0].ColorOperation = TextureOperation.SelectArg1;
   drawArgs.device.VertexFormat = CustomVertex.PositionNormalTextured.Format;
   drawArgs.device.RenderState.ZBufferEnable = true;
  }
  void Render(Device device, CustomVertex.PositionNormalTextured[] verts, QuadTile child)
  {
   bool isMultitexturing = false;
   if(!World.Settings.EnableSunShading)
   {
    if (World.Settings.ShowDownloadIndicator && child != null)
    {
     GeoSpatialDownloadRequest request = child.DownloadRequest;
     if (child.isDownloadingTerrain)
     {
      device.SetTexture(1, QuadTileSet.DownloadTerrainTexture);
      isMultitexturing = true;
     }
     else if(child.WaitingForDownload)
     {
      if (child.IsDownloadingImage)
       device.SetTexture(1, QuadTileSet.DownloadInProgressTexture);
      else
       device.SetTexture(1, QuadTileSet.DownloadQueuedTexture);
      isMultitexturing = true;
     }
    }
   }
   if (isMultitexturing)
    device.SetTextureStageState(1, TextureStageStates.ColorOperation, (int)TextureOperation.BlendTextureAlpha);
   if(verts != null && vertexIndexes != null)
   {
    if(World.Settings.EnableSunShading)
    {
     Point3d sunPosition = SunCalculator.GetGeocentricPosition(TimeKeeper.CurrentTimeUtc);
     Vector3 sunVector = new Vector3(
      (float)sunPosition.X,
      (float)sunPosition.Y,
      (float)sunPosition.Z);
     device.RenderState.Lighting = true;
     Material material = new Material();
     material.Diffuse = System.Drawing.Color.White;
     material.Ambient = System.Drawing.Color.White;
     device.Material = material;
     device.RenderState.AmbientColor = World.Settings.ShadingAmbientColor.ToArgb();
     device.RenderState.NormalizeNormals = true;
     device.RenderState.AlphaBlendEnable = true;
     device.Lights[0].Enabled = true;
     device.Lights[0].Type = LightType.Directional;
     device.Lights[0].Diffuse = System.Drawing.Color.White;
     device.Lights[0].Direction = sunVector;
     device.TextureState[0].ColorOperation = TextureOperation.Modulate;
     device.TextureState[0].ColorArgument1 = TextureArgument.Diffuse;
     device.TextureState[0].ColorArgument2 = TextureArgument.TextureColor;
     device.TextureState[0].AlphaOperation = TextureOperation.SelectArg1;
     device.TextureState[0].AlphaArgument1 = TextureArgument.TextureColor;
    }
    else
    {
     device.RenderState.Lighting = false;
     device.RenderState.Ambient = World.Settings.StandardAmbientColor;
    }
    device.DrawIndexedUserPrimitives(PrimitiveType.TriangleList, 0,
     verts.Length, vertexIndexes.Length / 3, vertexIndexes, true, verts);
   }
   if (isMultitexturing)
    device.SetTextureStageState(1, TextureStageStates.ColorOperation, (int)TextureOperation.Disable);
  }
 }
}
