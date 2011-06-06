

package org.gjt.sp.jedit.textarea;


import java.util.*;
import org.gjt.sp.jedit.buffer.JEditBuffer;
import org.gjt.sp.jedit.Debug;
import org.gjt.sp.jedit.syntax.*;
import org.gjt.sp.util.Log;



class ChunkCache
{
	
	ChunkCache(TextArea textArea)
	{
		this.textArea = textArea;
		out = new ArrayList<Chunk>();
		tokenHandler = new DisplayTokenHandler();
	} 

	
	
	int getMaxHorizontalScrollWidth()
	{
		int max = 0;
		for(int i = 0; i < firstInvalidLine; i++)
		{
			LineInfo info = lineInfo[i];
			if(info.width > max)
				max = info.width;
		}
		return max;
	} 

	
	
	int getScreenLineOfOffset(int line, int offset)
	{
		if(lineInfo.length == 0)
			return -1;
		if(line < textArea.getFirstPhysicalLine())
			return -1;
		if(line == textArea.getFirstPhysicalLine()
			&& offset < getLineInfo(0).offset)
			return -1;
		if(line > textArea.getLastPhysicalLine())
			return -1;
		
		if(line == lastScreenLineP)
		{
			LineInfo last = getLineInfo(lastScreenLine);

			if(offset >= last.offset
				&& offset < last.offset + last.length) {
				return lastScreenLine;
			}
		}

		int screenLine = -1;

		
		for(int i = 0; i < textArea.getVisibleLines(); i++)
		{
			LineInfo info = getLineInfo(i);
			if(info.physicalLine > line)
			{
				
				return i - 1;
				
			}
			if(info.physicalLine == line)
			{
				if(offset >= info.offset
					&& offset < info.offset + info.length)
				{
					screenLine = i;
					break;
				}
			}
		}

		if(screenLine == -1)
			return -1;


		lastScreenLineP = line;
		lastScreenLine = screenLine;

		return screenLine;
	} 

	
	
	void recalculateVisibleLines()
	{
		LineInfo[] newLineInfo = new LineInfo[textArea.getVisibleLines()];

		int start;
		if(lineInfo == null)
			start = 0;
		else
		{
			start = Math.min(lineInfo.length,newLineInfo.length);
			System.arraycopy(lineInfo,0,newLineInfo,0,start);
		}

		for(int i = start; i < newLineInfo.length; i++)
			newLineInfo[i] = new LineInfo();

		lineInfo = newLineInfo;

		lastScreenLine = lastScreenLineP = -1;
	} 

	
	void setBuffer(JEditBuffer buffer)
	{
		this.buffer = buffer;
		lastScreenLine = lastScreenLineP = -1;
	} 

	
	void scrollDown(int amount)
	{
		int visibleLines = textArea.getVisibleLines();

		System.arraycopy(lineInfo,amount,lineInfo,0,visibleLines - amount);

		for(int i = visibleLines - amount; i < visibleLines; i++)
		{
			lineInfo[i] = new LineInfo();
		}

		firstInvalidLine -= amount;
		if(firstInvalidLine < 0)
			firstInvalidLine = 0;

		if(Debug.CHUNK_CACHE_DEBUG)
		{
			System.err.println("f > t.f: only " + amount
				+ " need updates");
		}

		lastScreenLine = lastScreenLineP = -1;
	} 

	
	void scrollUp(int amount)
	{
		System.arraycopy(lineInfo,0,lineInfo,amount,
			textArea.getVisibleLines() - amount);

		for(int i = 0; i < amount; i++)
		{
			lineInfo[i] = new LineInfo();
		}

		
		int oldFirstInvalidLine = firstInvalidLine;
		firstInvalidLine = 0;
		updateChunksUpTo(amount);
		firstInvalidLine = oldFirstInvalidLine + amount;
		if(firstInvalidLine > textArea.getVisibleLines())
			firstInvalidLine = textArea.getVisibleLines();

		if(Debug.CHUNK_CACHE_DEBUG)
		{
			Log.log(Log.DEBUG,this,"f > t.f: only " + amount
				+ " need updates");
		}

		lastScreenLine = lastScreenLineP = -1;
	} 

	
	void invalidateAll()
	{
		firstInvalidLine = 0;
		lastScreenLine = lastScreenLineP = -1;
	} 

	
	void invalidateChunksFrom(int screenLine)
	{
		if(Debug.CHUNK_CACHE_DEBUG)
			Log.log(Log.DEBUG,this,"Invalidate from " + screenLine);
		firstInvalidLine = Math.min(screenLine,firstInvalidLine);

		if(screenLine <= lastScreenLine)
			lastScreenLine = lastScreenLineP = -1;
	} 

	
	void invalidateChunksFromPhys(int physicalLine)
	{
		for(int i = 0; i < firstInvalidLine; i++)
		{
			LineInfo info = lineInfo[i];
			if(info.physicalLine == -1 || info.physicalLine >= physicalLine)
			{
				firstInvalidLine = i;
				if(i <= lastScreenLine)
					lastScreenLine = lastScreenLineP = -1;
				break;
			}
		}
	} 

	
	LineInfo getLineInfo(int screenLine)
	{
		updateChunksUpTo(screenLine);
		return lineInfo[screenLine];
	} 

	
	int getLineSubregionCount(int physicalLine)
	{
		if(!textArea.softWrap)
			return 1;

		out.clear();
		lineToChunkList(physicalLine,out);

		int size = out.size();
		if(size == 0)
			return 1;
		else
			return size;
	} 

	
	
	static int getSubregionOfOffset(int offset, LineInfo[] lineInfos)
	{
		for(int i = 0; i < lineInfos.length; i++)
		{
			LineInfo info = lineInfos[i];
			if(offset >= info.offset && offset < info.offset + info.length)
				return i;
		}

		return -1;
	} 

	
	
	int xToSubregionOffset(int physicalLine, int subregion, int x,
		boolean round)
	{
		LineInfo[] infos = getLineInfosForPhysicalLine(physicalLine);
		if(subregion == -1)
			subregion += infos.length;
		return xToSubregionOffset(infos[subregion],x,round);
	} 

	
	
	static int xToSubregionOffset(LineInfo info, int x,
		boolean round)
	{
		int offset = Chunk.xToOffset(info.chunks,x,round);
		if(offset == -1 || offset == info.offset + info.length)
			offset = info.offset + info.length - 1;

		return offset;
	} 

	
	
	int subregionOffsetToX(int physicalLine, int offset)
	{
		LineInfo[] infos = getLineInfosForPhysicalLine(physicalLine);
		LineInfo info = infos[getSubregionOfOffset(offset,infos)];
		return subregionOffsetToX(info,offset);
	} 

	
	
	static int subregionOffsetToX(LineInfo info, int offset)
	{
		return (int)Chunk.offsetToX(info.chunks,offset);
	} 

	
	
	int getSubregionStartOffset(int line, int offset)
	{
		LineInfo[] lineInfos = getLineInfosForPhysicalLine(line);
		LineInfo info = lineInfos[getSubregionOfOffset(offset,lineInfos)];
		return textArea.getLineStartOffset(info.physicalLine)
			+ info.offset;
	} 

	
	
	int getSubregionEndOffset(int line, int offset)
	{
		LineInfo[] lineInfos = getLineInfosForPhysicalLine(line);
		LineInfo info = lineInfos[getSubregionOfOffset(offset,lineInfos)];
		return textArea.getLineStartOffset(info.physicalLine)
			+ info.offset + info.length;
	} 

	
	
	int getBelowPosition(int physicalLine, int offset, int x,
		boolean ignoreWrap)
	{
		LineInfo[] lineInfos = getLineInfosForPhysicalLine(physicalLine);

		int subregion = getSubregionOfOffset(offset,lineInfos);

		if(subregion != lineInfos.length - 1 && !ignoreWrap)
		{
			return textArea.getLineStartOffset(physicalLine)
				+ xToSubregionOffset(lineInfos[subregion + 1],
				x,true);
		}
		else
		{
			int nextLine = textArea.displayManager
				.getNextVisibleLine(physicalLine);

			if(nextLine == -1)
				return -1;
			else
			{
				return textArea.getLineStartOffset(nextLine)
					+ xToSubregionOffset(nextLine,0,
					x,true);
			}
		}
	} 

	
	
	int getAbovePosition(int physicalLine, int offset, int x,
		boolean ignoreWrap)
	{
		LineInfo[] lineInfos = getLineInfosForPhysicalLine(physicalLine);

		int subregion = getSubregionOfOffset(offset,lineInfos);

		if(subregion != 0 && !ignoreWrap)
		{
			return textArea.getLineStartOffset(physicalLine)
				+ xToSubregionOffset(lineInfos[subregion - 1],
				x,true);
		}
		else
		{
			int prevLine = textArea.displayManager
				.getPrevVisibleLine(physicalLine);

			if(prevLine == -1)
				return -1;
			else
			{
				return textArea.getLineStartOffset(prevLine)
					+ xToSubregionOffset(prevLine,-1,
					x,true);
			}
		}
	} 

	
	
	boolean needFullRepaint()
	{
		boolean retVal = needFullRepaint;
		needFullRepaint = false;
		return retVal;
	} 

	
	LineInfo[] getLineInfosForPhysicalLine(int physicalLine)
	{
		out.clear();

		if(!buffer.isLoading())
			lineToChunkList(physicalLine,out);

		if(out.isEmpty())
			out.add(null);

		List<LineInfo> returnValue = new ArrayList<LineInfo>(out.size());
		getLineInfosForPhysicalLine(physicalLine,returnValue);
		return returnValue.toArray(new LineInfo[out.size()]);
	} 

	

	
	private final TextArea textArea;
	private JEditBuffer buffer;
	
	private LineInfo[] lineInfo;
	private final List<Chunk> out;

	
	private int firstInvalidLine;
	private int lastScreenLineP;
	private int lastScreenLine;

	private boolean needFullRepaint;

	private final DisplayTokenHandler tokenHandler;
	

	
	private void getLineInfosForPhysicalLine(int physicalLine, List<LineInfo> list)
	{
		for(int i = 0; i < out.size(); i++)
		{
			Chunk chunks = out.get(i);
			LineInfo info = new LineInfo();
			info.physicalLine = physicalLine;
			if(i == 0)
			{
				info.firstSubregion = true;
				info.offset = 0;
			}
			else
				info.offset = chunks.offset;

			if(i == out.size() - 1)
			{
				info.lastSubregion = true;
				info.length = textArea.getLineLength(physicalLine)
					- info.offset + 1;
			}
			else
			{
				info.length = out.get(i + 1).offset
					- info.offset;
			}

			info.chunks = chunks;

			list.add(info);
		}
	} 

	
	
	private int getFirstScreenLine()
	{
		for(int i = firstInvalidLine - 1; i >= 0; i--)
		{
			if(lineInfo[i].lastSubregion)
				return i + 1;
		}

		return 0;
	} 

	
	
	private int getUpdateStartLine(int firstScreenLine)
	{
		
		
		if(firstScreenLine == 0)
		{
			return textArea.getFirstPhysicalLine();
		}
		
		else
		{
			int prevPhysLine = lineInfo[
				firstScreenLine - 1]
				.physicalLine;
			
			
			if(prevPhysLine == -1)
				return -1;
			else
			{
				return textArea.displayManager
					.getNextVisibleLine(prevPhysLine);
			}
		}
	} 

	
	private void updateChunksUpTo(int lastScreenLine)
	{
		
		if(lastScreenLine >= lineInfo.length)
			throw new ArrayIndexOutOfBoundsException(lastScreenLine);

		
		
		if(lastScreenLine < firstInvalidLine)
			return;

		int firstScreenLine = getFirstScreenLine();
		int physicalLine = getUpdateStartLine(firstScreenLine);

		if(Debug.CHUNK_CACHE_DEBUG)
		{
			Log.log(Log.DEBUG,this,"Updating chunks from " + firstScreenLine
				+ " to " + lastScreenLine);
		}

		
		
		
		

		out.clear();

		int offset = 0;
		int length = 0;

		for(int i = firstScreenLine; i <= lastScreenLine; i++)
		{
			LineInfo info = lineInfo[i];

			Chunk chunks;

			
			if(out.isEmpty())
			{
				
				
				if(physicalLine != -1 && i != firstScreenLine)
				{
					physicalLine = textArea.displayManager
						.getNextVisibleLine(physicalLine);
				}

				
				if(physicalLine == -1)
				{
					info.chunks = null;
					info.physicalLine = -1;
					
					
					
					info.width = 0;
					continue;
				}

				
				lineToChunkList(physicalLine,out);

				info.firstSubregion = true;

				int screenLines;

				
				if(out.isEmpty())
				{
					screenLines = 1;

					if(i == 0)
					{
						if(textArea.displayManager.firstLine.skew > 0)
						{
							Log.log(Log.ERROR,this,"BUG: skew=" + textArea.displayManager.firstLine.skew + ",out.size()=" + out.size());
							textArea.displayManager.firstLine.skew = 0;
							needFullRepaint = true;
							lastScreenLine = lineInfo.length - 1;
						}
					}
					chunks = null;
					offset = 0;
					length = 1;
				}
				
				else
				{
					screenLines = out.size();

					if(i == 0)
					{
						int skew = textArea.displayManager.firstLine.skew;
						if(skew >= out.size())
						{
							Log.log(Log.ERROR,this,"BUG: skew=" + skew + ",out.size()=" + out.size());
							skew = 0;
							needFullRepaint = true;
							lastScreenLine = lineInfo.length - 1;
						}
						else if(skew > 0)
						{
							info.firstSubregion = false;
							for(int j = 0; j < skew; j++)
								out.remove(0);
						}
					}
					chunks = out.remove(0);
					offset = chunks.offset;
					if (!out.isEmpty())
						length = out.get(0).offset - offset;
					else
						length = textArea.getLineLength(physicalLine) - offset + 1;
				}
			}
			else
			{
				info.firstSubregion = false;

				chunks = out.remove(0);
				offset = chunks.offset;
				if (!out.isEmpty())
					length = out.get(0).offset - offset;
				else
					length = textArea.getLineLength(physicalLine) - offset + 1;
			}

			boolean lastSubregion = out.isEmpty();

			if(i == lastScreenLine
				&& lastScreenLine != lineInfo.length - 1)
			{
				
				if(tokenHandler.getLineContext() !=
					info.lineContext)
				{
					lastScreenLine++;
					needFullRepaint = true;
				}
				
				else if(info.physicalLine != physicalLine
					|| info.lastSubregion != lastSubregion)
				{
					lastScreenLine++;
					needFullRepaint = true;
				}
				
				else if (!out.isEmpty())
					lastScreenLine++;
			}

			info.physicalLine = physicalLine;
			info.lastSubregion = lastSubregion;
			info.offset = offset;
			info.length = length;
			info.chunks = chunks;
			info.lineContext = tokenHandler.getLineContext();
		}

		firstInvalidLine = Math.max(lastScreenLine + 1,firstInvalidLine);
	} 

	
	private void lineToChunkList(int physicalLine, List<Chunk> out)
	{
		TextAreaPainter painter = textArea.getPainter();

		tokenHandler.init(painter.getStyles(),
			painter.getFontRenderContext(),
			painter,out,
			textArea.softWrap
			? textArea.wrapMargin : 0.0f);
		buffer.markTokens(physicalLine,tokenHandler);
	} 

	

	
	
	static class LineInfo
	{
		int physicalLine;
		int offset;
		int length;
		boolean firstSubregion;
		boolean lastSubregion;
		Chunk chunks;
		
		int width;
		TokenMarker.LineContext lineContext;
	} 
}
