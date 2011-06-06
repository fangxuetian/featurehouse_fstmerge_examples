

package org.gjt.sp.util;


public class IntegerArray
{
	
	public IntegerArray()
	{
		array = new int[2000];
	} 

	
	public void add(int num)
	{
		if(len >= array.length)
		{
			int[] arrayN = new int[len * 2];
			System.arraycopy(array,0,arrayN,0,len);
			array = arrayN;
		}

		array[len++] = num;
	} 

	
	public final int get(int index)
	{
		return array[index];
	} 

	
	public final int getSize()
	{
		return len;
	} 

	
	public final void setSize(int len)
	{
		this.len = len;
	} 

	
	public final void clear()
	{
		len = 0;
	} 

	
	public int[] getArray()
	{
		return array;
	} 

	
	private int[] array;
	private int len;
	
}
