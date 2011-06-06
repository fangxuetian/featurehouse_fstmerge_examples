

package org.gjt.sp.jedit;



import org.gjt.sp.jedit.msg.BufferUpdate;
import org.gjt.sp.jedit.msg.DynamicMenuChanged;
import org.gjt.sp.util.Log;
import org.gjt.sp.util.StandardUtilities;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.Reader;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;



public class Macros
{
	
	
	public static void showRunScriptDialog(View view)
	{
		String[] paths = GUIUtilities.showVFSFileDialog(view,
			null,JFileChooser.OPEN_DIALOG,true);
		if(paths != null)
		{
			Buffer buffer = view.getBuffer();
			try
			{
				buffer.beginCompoundEdit();

file_loop:			for(int i = 0; i < paths.length; i++)
					runScript(view,paths[i],false);
			}
			finally
			{
				buffer.endCompoundEdit();
			}
		}
	} 

	
	
	public static void runScript(View view, String path, boolean ignoreUnknown)
	{
		Handler handler = getHandlerForPathName(path);
		if(handler != null)
		{
			try
			{
				Macro newMacro = handler.createMacro(
					MiscUtilities.getFileName(path), path);
				newMacro.invoke(view);
			}
			catch (Exception e)
			{
				Log.log(Log.ERROR, Macros.class, e);
				return;
			}
			return;
		}

		
		
		
		if(ignoreUnknown)
		{
			Log.log(Log.NOTICE,Macros.class,path +
				": Cannot find a suitable macro handler");
		}
		else
		{
			Log.log(Log.ERROR,Macros.class,path +
				": Cannot find a suitable macro handler, "
				+ "assuming BeanShell");
			getHandler("beanshell").createMacro(
				path,path).invoke(view);
		}
	} 

	
	
	public static void message(Component comp, String message)
	{
		GUIUtilities.hideSplashScreen();

		JOptionPane.showMessageDialog(comp,message,
			jEdit.getProperty("macro-message.title"),
			JOptionPane.INFORMATION_MESSAGE);
	} 

	
	
	public static void error(Component comp, String message)
	{
		GUIUtilities.hideSplashScreen();

		JOptionPane.showMessageDialog(comp,message,
			jEdit.getProperty("macro-message.title"),
			JOptionPane.ERROR_MESSAGE);
	} 

	
	
	public static String input(Component comp, String prompt)
	{
		GUIUtilities.hideSplashScreen();

		return input(comp,prompt,null);
	} 

	
	
	public static String input(Component comp, String prompt, String defaultValue)
	{
		GUIUtilities.hideSplashScreen();

		return (String)JOptionPane.showInputDialog(comp,prompt,
			jEdit.getProperty("macro-input.title"),
			JOptionPane.QUESTION_MESSAGE,null,null,defaultValue);
	} 

	
	
	public static int confirm(Component comp, String prompt, int buttons)
	{
		GUIUtilities.hideSplashScreen();

		return JOptionPane.showConfirmDialog(comp,prompt,
			jEdit.getProperty("macro-confirm.title"),buttons,
			JOptionPane.QUESTION_MESSAGE);
	} 

	
	
	public static int confirm(Component comp, String prompt, int buttons, int type)
	{
		GUIUtilities.hideSplashScreen();

		return JOptionPane.showConfirmDialog(comp,prompt,
			jEdit.getProperty("macro-confirm.title"),buttons,type);
	} 

	
	
	public static void loadMacros()
	{
		macroActionSet.removeAllActions();
		macroHierarchy.removeAllElements();
		macroHash.clear();

		
		
		
		String settings = jEdit.getSettingsDirectory();

		if(settings != null)
		{
			userMacroPath = MiscUtilities.constructPath(
				settings,"macros");
			loadMacros(macroHierarchy,"",new File(userMacroPath));
		}

		if(jEdit.getJEditHome() != null)
		{
			systemMacroPath = MiscUtilities.constructPath(
				jEdit.getJEditHome(),"macros");
			loadMacros(macroHierarchy,"",new File(systemMacroPath));
		}

		EditBus.send(new DynamicMenuChanged("macros"));
	} 

	
	
	public static void registerHandler(Handler handler)
	{
		if (getHandler(handler.getName()) != null)
		{
			Log.log(Log.ERROR, Macros.class, "Cannot register more than one macro handler with the same name");
			return;
		}

		Log.log(Log.DEBUG,Macros.class,"Registered " + handler.getName()
			+ " macro handler");
		macroHandlers.add(handler);
	} 

	
	
	public static Handler[] getHandlers()
	{
		Handler[] handlers = new Handler[macroHandlers.size()];
		return macroHandlers.toArray(handlers);
	} 

	
	
	public static Handler getHandlerForPathName(String pathName)
	{
		for (int i = 0; i < macroHandlers.size(); i++)
		{
			Handler handler = macroHandlers.get(i);
			if (handler.accept(pathName))
				return handler;
		}

		return null;
	} 

	
	
	public static Handler getHandler(String name)
	{
		for (int i = 0; i < macroHandlers.size(); i++)
		{
			Handler handler = macroHandlers.get(i);
			if (handler.getName().equals(name))
				return handler;
		}

		return null;
	}
	

	
	
	public static Vector getMacroHierarchy()
	{
		return macroHierarchy;
	} 

	
	
	public static ActionSet getMacroActionSet()
	{
		return macroActionSet;
	} 

	
	
	public static Macro getMacro(String macro)
	{
		return macroHash.get(macro);
	} 

	
	
	public static Macro getLastMacro()
	{
		return lastMacro;
	} 

	
	
	public static void setLastMacro(Macro macro)
	{
		lastMacro = macro;
	} 

	
	
	public static class Macro extends EditAction
	{
		
		public Macro(Handler handler, String name, String label, String path)
		{
			super(name);
			this.handler = handler;
			this.label = label;
			this.path = path;
		} 

		
		public Handler getHandler()
		{
			return handler;
		}
		

		
		public String getPath()
		{
			return path;
		} 

		
		@Override
		public void invoke(View view)
		{
			setLastMacro(this);

			if(view == null)
				handler.runMacro(null,this);
			else
			{
				try
				{
					view.getBuffer().beginCompoundEdit();
					handler.runMacro(view,this);
				}
				finally
				{
					view.getBuffer().endCompoundEdit();
				}
			}
		} 

		
		@Override
		public String getCode()
		{
			return "Macros.getMacro(\"" + getName() + "\").invoke(view);";
		} 

		
		public static String macroNameToLabel(String macroName)
		{
			int index = macroName.lastIndexOf('/');
			return macroName.substring(index + 1).replace('_', ' ');
		}
		

		
		private Handler handler;
		private String path;
		String label;
		
	} 

	
	
	public static void recordTemporaryMacro(View view)
	{
		String settings = jEdit.getSettingsDirectory();

		if(settings == null)
		{
			GUIUtilities.error(view,"no-settings",new String[0]);
			return;
		}
		if(view.getMacroRecorder() != null)
		{
			GUIUtilities.error(view,"already-recording",new String[0]);
			return;
		}

		Buffer buffer = jEdit.openFile((View)null,settings + File.separator
			+ "macros","Temporary_Macro.bsh",true,null);

		if(buffer == null)
			return;

		buffer.remove(0,buffer.getLength());
		buffer.insert(0,jEdit.getProperty("macro.temp.header"));

		recordMacro(view,buffer,true);
	} 

	
	
	public static void recordMacro(View view)
	{
		String settings = jEdit.getSettingsDirectory();

		if(settings == null)
		{
			GUIUtilities.error(view,"no-settings",new String[0]);
			return;
		}

		if(view.getMacroRecorder() != null)
		{
			GUIUtilities.error(view,"already-recording",new String[0]);
			return;
		}

		String name = GUIUtilities.input(view,"record",null);
		if(name == null)
			return;

		name = name.replace(' ','_');

		Buffer buffer = jEdit.openFile((View) null,null,
			MiscUtilities.constructPath(settings,"macros",
			name + ".bsh"),true,null);

		if(buffer == null)
			return;

		buffer.remove(0,buffer.getLength());
		buffer.insert(0,jEdit.getProperty("macro.header"));

		recordMacro(view,buffer,false);
	} 

	
	
	public static void stopRecording(View view)
	{
		Recorder recorder = view.getMacroRecorder();

		if(recorder == null)
			GUIUtilities.error(view,"macro-not-recording",null);
		else
		{
			view.setMacroRecorder(null);
			if(!recorder.temporary)
				view.setBuffer(recorder.buffer);
			recorder.dispose();
		}
	} 

	
	
	public static void runTemporaryMacro(View view)
	{
		String settings = jEdit.getSettingsDirectory();

		if(settings == null)
		{
			GUIUtilities.error(view,"no-settings",null);
			return;
		}

		String path = MiscUtilities.constructPath(
			jEdit.getSettingsDirectory(),"macros",
			"Temporary_Macro.bsh");

		if(jEdit.getBuffer(path) == null)
		{
			GUIUtilities.error(view,"no-temp-macro",null);
			return;
		}

		Handler handler = getHandler("beanshell");
		Macro temp = handler.createMacro(path,path);

		Buffer buffer = view.getBuffer();

		try
		{
			buffer.beginCompoundEdit();
			temp.invoke(view);
		}
		finally
		{
			
			if(buffer.insideCompoundEdit())
				buffer.endCompoundEdit();
		}
	} 

	

	
	private static String systemMacroPath;
	private static String userMacroPath;

	private static List<Handler> macroHandlers;

	private static ActionSet macroActionSet;
	private static Vector macroHierarchy;
	private static Map<String, Macro> macroHash;

	private static Macro lastMacro;
	

	
	static
	{
		macroHandlers = new ArrayList<Handler>();
		registerHandler(new BeanShellHandler());
		macroActionSet = new ActionSet(jEdit.getProperty("action-set.macros"));
		jEdit.addActionSet(macroActionSet);
		macroHierarchy = new Vector();
		macroHash = new Hashtable<String, Macro>();
	} 

	
	private static void loadMacros(List vector, String path, File directory)
	{
		lastMacro = null;

		File[] macroFiles = directory.listFiles();
		if(macroFiles == null || macroFiles.length == 0)
			return;

		for(int i = 0; i < macroFiles.length; i++)
		{
			File file = macroFiles[i];
			String fileName = file.getName();
			if(file.isHidden())
			{
				
			}
			else if(file.isDirectory())
			{
				String submenuName = fileName.replace('_',' ');
				List submenu = null;
				
				for(int j = 0; j < vector.size(); j++)
				{
					Object obj = vector.get(j);
					if(obj instanceof List)
					{
						List vec = (List)obj;
						if(submenuName.equals(vec.get(0)))
						{
							submenu = vec;
							break;
						}
					}
				} 
				if(submenu == null)
				{
					submenu = new Vector();
					submenu.add(submenuName);
					vector.add(submenu);
				}

				loadMacros(submenu,path + fileName + '/',file);
			}
			else
			{
				addMacro(file,path,vector);
			}
		}
	} 

	
	private static void addMacro(File file, String path, List vector)
	{
		String fileName = file.getName();
		Handler handler = getHandlerForPathName(file.getPath());

		if(handler == null)
			return;

		try
		{
			
			
			
			String macroName = (path + fileName).replace(' ','_');
			Macro newMacro = handler.createMacro(macroName,
				file.getPath());
			
			
			if(macroHash.get(newMacro.getName()) != null)
				return;

			vector.add(newMacro.getName());
			jEdit.setTemporaryProperty(newMacro.getName()
				+ ".label",
				newMacro.label);
			jEdit.setTemporaryProperty(newMacro.getName()
				+ ".mouse-over",
				handler.getLabel() + " - " + file.getPath());
			macroActionSet.addAction(newMacro);
			macroHash.put(newMacro.getName(),newMacro);
		}
		catch (Exception e)
		{
			Log.log(Log.ERROR, Macros.class, e);
			macroHandlers.remove(handler);
		}
	} 

	
	
	private static void recordMacro(View view, Buffer buffer, boolean temporary)
	{
		view.setMacroRecorder(new Recorder(view,buffer,temporary));

		
		
		view.getStatus().setMessage(null);
	} 

	

	
	
	public static class Recorder implements EBComponent
	{
		View view;
		Buffer buffer;
		boolean temporary;

		boolean lastWasInput;
		boolean lastWasOverwrite;
		int overwriteCount;

		
		public Recorder(View view, Buffer buffer, boolean temporary)
		{
			this.view = view;
			this.buffer = buffer;
			this.temporary = temporary;
			EditBus.addToBus(this);
		} 

		
		public void record(String code)
		{
			if (BeanShell.isScriptRunning())
				return;
			flushInput();

			append("\n");
			append(code);
		} 

		
		public void record(int repeat, String code)
		{
			if(repeat == 1)
				record(code);
			else
			{
				record("for(int i = 1; i <= " + repeat + "; i++)\n"
					+ "{\n"
					+ code + '\n'
					+ '}');
			}
		} 

		
		
		public void recordInput(int repeat, char ch, boolean overwrite)
		{
			
			
			if(ch == '\n')
				record(repeat,"textArea.userInput(\'\\n\');");
			else if(ch == '\t')
				record(repeat,"textArea.userInput(\'\\t\');");
			else
			{
				StringBuilder buf = new StringBuilder(repeat);
				for(int i = 0; i < repeat; i++)
					buf.append(ch);
				recordInput(buf.toString(),overwrite);
			}
		} 

		
		
		public void recordInput(String str, boolean overwrite)
		{
			String charStr = StandardUtilities.charsToEscapes(str);

			if(overwrite)
			{
				if(lastWasOverwrite)
				{
					overwriteCount++;
					append(charStr);
				}
				else
				{
					flushInput();
					overwriteCount = 1;
					lastWasOverwrite = true;
					append("\ntextArea.setSelectedText(\"" + charStr);
				}
			}
			else
			{
				if(lastWasInput)
					append(charStr);
				else
				{
					flushInput();
					lastWasInput = true;
					append("\ntextArea.setSelectedText(\"" + charStr);
				}
			}
		} 

		
		public void handleMessage(EBMessage msg)
		{
			if(msg instanceof BufferUpdate)
			{
				BufferUpdate bmsg = (BufferUpdate)msg;
				if(bmsg.getWhat() == BufferUpdate.CLOSED)
				{
					if(bmsg.getBuffer() == buffer)
						stopRecording(view);
				}
			}
		} 

		
		private void append(String str)
		{
			buffer.insert(buffer.getLength(),str);
		} 

		
		private void dispose()
		{
			flushInput();

			for(int i = 0; i < buffer.getLineCount(); i++)
			{
				buffer.indentLine(i,true);
			}

			EditBus.removeFromBus(this);

			
			
			view.getStatus().setMessage(null);
		} 

		
		
		private void flushInput()
		{
			if(lastWasInput)
			{
				lastWasInput = false;
				append("\");");
			}

			if(lastWasOverwrite)
			{
				lastWasOverwrite = false;
				append("\");\n");
				append("offset = buffer.getLineEndOffset("
					+ "textArea.getCaretLine()) - 1;\n");
				append("buffer.remove(textArea.getCaretPosition(),"
					+ "Math.min(" + overwriteCount
					+ ",offset - "
					+ "textArea.getCaretPosition()));");
			}
		} 
	} 

	
	
	public abstract static class Handler
	{
		
		public String getName()
		{
			return name;
		} 

		
		public String getLabel()
		{
			return label;
		} 

		
		public boolean accept(String path)
		{
			return filter.matcher(MiscUtilities.getFileName(path)).matches();
		} 

		
		public abstract Macro createMacro(String macroName, String path);
		

		
		
		public abstract void runMacro(View view, Macro macro);
		

		
		
		public void runMacro(View view, Macro macro, boolean ownNamespace)
		{
			runMacro(view,macro);
		} 

		
		protected Handler(String name)
		{
			this.name = name;
			label = jEdit.getProperty("macro-handler."
				+ name + ".label", name);
			try
			{
				filter = Pattern.compile(StandardUtilities.globToRE(
					jEdit.getProperty(
					"macro-handler." + name + ".glob")));
			}
			catch (Exception e)
			{
				throw new InternalError("Missing or invalid glob for handler " + name);
			}
		} 

		
		private String name;
		private String label;
		private Pattern filter;
		
	} 

	
	private static class BeanShellHandler extends Handler
	{
		
		BeanShellHandler()
		{
			super("beanshell");
		} 

		
		@Override
		public Macro createMacro(String macroName, String path)
		{
			
			macroName = macroName.substring(0, macroName.length() - 4);

			return new Macro(this, macroName,
				Macro.macroNameToLabel(macroName), path);
		} 

		
		@Override
		public void runMacro(View view, Macro macro)
		{
			BeanShell.runScript(view,macro.getPath(),null,true);
		} 

		
		@Override
		public void runMacro(View view, Macro macro, boolean ownNamespace)
		{
			BeanShell.runScript(view,macro.getPath(),null,ownNamespace);
		} 
	} 
}
