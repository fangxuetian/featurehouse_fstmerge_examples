

package org.gjt.sp.jedit.buffer;


import org.gjt.sp.util.Log;



public class UndoManager
{
	
	public UndoManager(JEditBuffer buffer)
	{
		this.buffer = buffer;
	} 

	
	public void setLimit(int limit)
	{
		this.limit = limit;
	} 

	
	public void clear()
	{
		undosFirst = undosLast = redosFirst = null;
		undoCount = 0;
	} 

	
	public boolean canUndo()
	{
		return (undosLast != null);
	} 

	
	public int undo()
	{
		if(insideCompoundEdit())
			throw new InternalError("Unbalanced begin/endCompoundEdit()");

		if(undosLast == null)
			return -1;
		else
		{
			reviseUndoId();
			undoCount--;

			int caret = undosLast.undo();
			redosFirst = undosLast;
			undosLast = undosLast.prev;
			if(undosLast == null)
				undosFirst = null;
			return caret;
		}
	} 

	
	public boolean canRedo()
	{
		return (redosFirst != null);
	} 

	
	public int redo()
	{
		if(insideCompoundEdit())
			throw new InternalError("Unbalanced begin/endCompoundEdit()");

		if(redosFirst == null)
			return -1;
		else
		{
			reviseUndoId();
			undoCount++;

			int caret = redosFirst.redo();
			undosLast = redosFirst;
			if(undosFirst == null)
				undosFirst = undosLast;
			redosFirst = redosFirst.next;
			return caret;
		}
	} 

	
	public void beginCompoundEdit()
	{
		if(compoundEditCount == 0)
		{
			compoundEdit = new CompoundEdit();
			reviseUndoId();
		}

		compoundEditCount++;
	} 

	
	public void endCompoundEdit()
	{
		if(compoundEditCount == 0)
		{
			Log.log(Log.WARNING,this,new Exception("Unbalanced begin/endCompoundEdit()"));
			return;
		}
		else if(compoundEditCount == 1)
		{
			if(compoundEdit.first == null)
				;
			else if(compoundEdit.first == compoundEdit.last)
				addEdit(compoundEdit.first);
			else
				addEdit(compoundEdit);

			compoundEdit = null;
		}

		compoundEditCount--;
	} 

	
	public boolean insideCompoundEdit()
	{
		return compoundEditCount != 0;
	} 

	
	public Object getUndoId()
	{
		return undoId;
	} 

	
	public void contentInserted(int offset, int length, String text, boolean clearDirty)
	{
		Edit last = getLastEdit();
		Edit toMerge = getMergeEdit();

		if(!clearDirty && toMerge instanceof Insert
			&& redosFirst == null)
		{
			Insert ins = (Insert)toMerge;
			if(ins.offset == offset)
			{
				ins.str = text.concat(ins.str);
				ins.length += length;
				return;
			}
			else if(ins.offset + ins.length == offset)
			{
				ins.str = ins.str.concat(text);
				ins.length += length;
				return;
			}
		}

		Insert ins = new Insert(this,offset,length,text);

		if(clearDirty)
		{
			redoClearDirty = last;
			undoClearDirty = ins;
		}

		if(compoundEdit != null)
			compoundEdit.add(ins);
		else
		{
			reviseUndoId();
			addEdit(ins);
		}
	} 

	
	public void contentRemoved(int offset, int length, String text, boolean clearDirty)
	{
		Edit last = getLastEdit();
		Edit toMerge = getMergeEdit();

		if(!clearDirty && toMerge instanceof Remove
			&& redosFirst == null)
		{
			Remove rem = (Remove)toMerge;
			if(rem.offset == offset)
			{
				rem.content.str = rem.content.str.concat(text);
				rem.content.hashcode = rem.content.str.hashCode();
				rem.length += length;
				KillRing.getInstance().changed(rem.content);
				return;
			}
			else if(offset + length == rem.offset)
			{
				rem.content.str = text.concat(rem.content.str);
				rem.content.hashcode = rem.content.str.hashCode();
				rem.length += length;
				rem.offset = offset;
				KillRing.getInstance().changed(rem.content);
				return;
			}
		}

		Remove rem = new Remove(this,offset,length,text);
		if(clearDirty)
		{
			redoClearDirty = last;
			undoClearDirty = rem;
		}

		if(compoundEdit != null)
			compoundEdit.add(rem);
		else
		{
			reviseUndoId();
			addEdit(rem);
		}

		KillRing.getInstance().add(rem.content);
	} 

	
	public void resetClearDirty()
	{
		redoClearDirty = getLastEdit();
		if(redosFirst instanceof CompoundEdit)
			undoClearDirty = ((CompoundEdit)redosFirst).first;
		else
			undoClearDirty = redosFirst;
	} 

	

	
	private JEditBuffer buffer;

	
	private Edit undosFirst;
	private Edit undosLast;

	
	private Edit redosFirst;

	private int limit;
	private int undoCount;
	private int compoundEditCount;
	private CompoundEdit compoundEdit;
	private Edit undoClearDirty, redoClearDirty;
	private Object undoId;
	

	
	private void addEdit(Edit edit)
	{
		if(undosFirst == null)
			undosFirst = undosLast = edit;
		else
		{
			undosLast.next = edit;
			edit.prev = undosLast;
			undosLast = edit;
		}

		redosFirst = null;

		undoCount++;

		while(undoCount > limit)
		{
			undoCount--;

			if(undosFirst == undosLast)
				undosFirst = undosLast = null;
			else
			{
				undosFirst.next.prev = null;
				undosFirst = undosFirst.next;
			}
		}
	} 

	
	private Edit getMergeEdit()
	{
		Edit last = getLastEdit();
		return (compoundEdit != null ? compoundEdit.last : last);
	} 

	
	private Edit getLastEdit()
	{
		if(undosLast instanceof CompoundEdit)
			return ((CompoundEdit)undosLast).last;
		else
			return undosLast;
	} 

	
	
	private void reviseUndoId()
	{
		undoId = new Object();
	} 

	

	

	
	abstract static class Edit
	{
		Edit prev, next;

		
		abstract int undo();
		

		
		abstract int redo();
		
	} 

	
	static class Insert extends Edit
	{
		
		Insert(UndoManager mgr, int offset, int length, String str)
		{
			this.mgr = mgr;
			this.offset = offset;
			this.length = length;
			this.str = str;
		} 

		
		int undo()
		{
			mgr.buffer.remove(offset,length);
			if(mgr.undoClearDirty == this)
				mgr.buffer.setDirty(false);
			return offset;
		} 

		
		int redo()
		{
			mgr.buffer.insert(offset,str);
			if(mgr.redoClearDirty == this)
				mgr.buffer.setDirty(false);
			return offset + length;
		} 

		UndoManager mgr;
		int offset;
		int length;
		String str;
	} 

	
	
	public static class RemovedContent
	{
		String str;
		int hashcode;
		boolean inKillRing;

		public RemovedContent(String str)
		{
			this.str = str;
			this.hashcode = str.hashCode();
		}

		public String toString()
		{
			return str;
		}
	}

	
	static class Remove extends Edit
	{
		
		Remove(UndoManager mgr, int offset, int length, String str)
		{
			this.mgr = mgr;
			this.offset = offset;
			this.length = length;
			this.content = new RemovedContent(str);
		} 

		
		int undo()
		{
			mgr.buffer.insert(offset,content.str);
			if(mgr.undoClearDirty == this)
				mgr.buffer.setDirty(false);
			return offset + length;
		} 

		
		int redo()
		{
			mgr.buffer.remove(offset,length);
			if(mgr.redoClearDirty == this)
				mgr.buffer.setDirty(false);
			return offset;
		} 

		UndoManager mgr;
		int offset;
		int length;
		final RemovedContent content;
	} 

	
	static class CompoundEdit extends Edit
	{
		
		public int undo()
		{
			int retVal = -1;
			Edit edit = last;
			while(edit != null)
			{
				retVal = edit.undo();
				edit = edit.prev;
			}
			return retVal;
		} 

		
		public int redo()
		{
			int retVal = -1;
			Edit edit = first;
			while(edit != null)
			{
				retVal = edit.redo();
				edit = edit.next;
			}
			return retVal;
		} 

		
		public void add(Edit edit)
		{
			if(first == null)
				first = last = edit;
			else
			{
				edit.prev = last;
				last.next = edit;
				last = edit;
			}
		} 

		Edit first, last;
	} 

	
}
