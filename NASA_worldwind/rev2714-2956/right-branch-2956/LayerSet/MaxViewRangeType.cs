using Altova.Types;
namespace LayerSet
{
 public class MaxViewRangeType : SchemaDecimal
 {
  public MaxViewRangeType() : base()
  {
  }
  public MaxViewRangeType(string newValue) : base(newValue)
  {
   Validate();
  }
  public MaxViewRangeType(SchemaDecimal newValue) : base(newValue)
  {
   Validate();
  }
  public void Validate()
  {
   if (CompareTo(GetMinInclusive()) < 0)
    throw new System.Exception("Out of range");
   if (CompareTo(GetMaxInclusive()) > 0)
    throw new System.Exception("Out of range");
  }
  public SchemaDecimal GetMinInclusive()
  {
   return new SchemaDecimal("0");
  }
  public SchemaDecimal GetMaxInclusive()
  {
   return new SchemaDecimal("180");
  }
 }
}
