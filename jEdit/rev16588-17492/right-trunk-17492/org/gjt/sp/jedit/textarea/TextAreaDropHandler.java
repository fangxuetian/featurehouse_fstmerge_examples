

package org.gjt.sp.jedit.textarea;


import javax.swing.*;
import java.awt.dnd.*;
import java.awt.*;
import org.gjt.sp.jedit.buffer.JEditBuffer;
import org.gjt.sp.util.Log;



class TextAreaDropHandler extends DropTargetAdapter
{
	private final TextArea textArea;
	private JEditBuffer savedBuffer;
	private int savedCaret;

	
	TextAreaDropHandler(TextArea textArea)
	{
		this.textArea = textArea;
	} 

	
	@Override
	public void dragEnter(DropTargetDragEvent dtde)
	{
		Log.log(Log.DEBUG,this,"Drag enter");
		savedBuffer = textArea.getBuffer();
		
		savedCaret = textArea.getCaretPosition();
	} 

	
	@Override
	public void dragOver(DropTargetDragEvent dtde)
	{
		Point p = dtde.getLocation();
		p = SwingUtilities.convertPoint(textArea,p,
			textArea.getPainter());
		int pos = textArea.xyToOffset(p.x,p.y,
			!(textArea.getPainter().isBlockCaretEnabled()
			|| textArea.isOverwriteEnabled()));
		if(pos != -1)
		{
			textArea.moveCaretPosition(pos,
				TextArea.ELECTRIC_SCROLL);
		}
	} 

	
	@Override
	public void dragExit(DropTargetEvent dtde)
	{
		Log.log(Log.DEBUG,this,"Drag exit");
		
		if(textArea.getBuffer() == savedBuffer)
		{
			textArea.moveCaretPosition(savedCaret,
				TextArea.ELECTRIC_SCROLL);
		}
		savedBuffer = null;
	} 

	
	public void drop(DropTargetDropEvent dtde)
	{
		Log.log(Log.DEBUG,this,"Drop");
		
		savedBuffer = null;
	} 
}
