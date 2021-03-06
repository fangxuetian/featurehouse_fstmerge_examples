package net.sourceforge.squirrel_sql.fw.util;


import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.*;

import net.sourceforge.squirrel_sql.fw.gui.action.BaseAction;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

public abstract class Resources
{
	public static final String ACCELERATOR_STRING = "SQuirreLAcceleratorString";

	private interface ActionProperties
	{
		String DISABLED_IMAGE = "disabledimage";

		String IMAGE = "image";

		String NAME = "name";

		String ROLLOVER_IMAGE = "rolloverimage";

		String TOOLTIP = "tooltip";
	}

	private interface MenuProperties
	{
		String TITLE = "title";

		String MNEMONIC = "mnemonic";
	}

	private interface MenuItemProperties extends MenuProperties
	{
		String ACCELERATOR = "accelerator";
	}

	private interface Keys
	{
		String ACTION = "action";

		String MENU = "menu";

		String MENU_ITEM = "menuitem";
	}

	
	private static final ILogger s_log = LoggerController.createLogger(Resources.class);

	
	private final ResourceBundle _bundle;

	
	private ClassLoader _classLoader;

	
	private final String _imagePath;

	protected Resources(String rsrcBundleBaseName, ClassLoader cl) {
		super();
		if (rsrcBundleBaseName == null || rsrcBundleBaseName.trim().length() == 0)
		{
			throw new IllegalArgumentException("Null or empty rsrcBundleBaseName passed");
		}

		_classLoader = cl;
		_bundle = ResourceBundle.getBundle(rsrcBundleBaseName, Locale.getDefault(), cl);
		_imagePath = _bundle.getString("path.images");
	}

	public KeyStroke getKeyStroke(Action action)
	{
		Utilities.checkNull("getKeyStroke", "action", action);
		final String fullKey = Keys.MENU_ITEM + "." + action.getClass().getName();

		String accel = getResourceString(fullKey, MenuItemProperties.ACCELERATOR);
		if (accel.length() > 0)
		{
			return KeyStroke.getKeyStroke(accel);
		}
		return null;
	}

	private String getAcceleratorString(Action action)
	{
		try
		{
			final String fullKey = Keys.MENU_ITEM + "." + action.getClass().getName();
			return getResourceString(fullKey, MenuItemProperties.ACCELERATOR);
		} catch (MissingResourceException e)
		{
			return null;
		}
	}

	public JMenuItem addToPopupMenu(Action action, javax.swing.JPopupMenu menu)
	      throws MissingResourceException
	{
		Utilities.checkNull("addToPopupMenu", "action", action, "menu", menu);
		final String fullKey = Keys.MENU_ITEM + "." + action.getClass().getName();
		final JMenuItem item = menu.add(action);

		if (action.getValue(Action.MNEMONIC_KEY) == null)
		{
			String mn = getResourceString(fullKey, MenuItemProperties.MNEMONIC);
			if (mn.length() > 0)
			{
				item.setMnemonic(mn.charAt(0));
			}
		}

		if (action.getValue(Action.ACCELERATOR_KEY) == null)
		{
			String accel = getResourceString(fullKey, MenuItemProperties.ACCELERATOR);
			if (accel.length() > 0)
			{
				item.setAccelerator(KeyStroke.getKeyStroke(accel));
			}
		}

		String toolTipText = getToolTipTextWithAccelerator(action, fullKey);

		item.setToolTipText(toolTipText);

		return item;
	}

	public JCheckBoxMenuItem addToMenuAsCheckBoxMenuItem(Action action, JMenu menu)
	      throws MissingResourceException
	{
		Utilities.checkNull("addToMenuAsCheckBoxMenuItem", "action", action, "menu", menu);
		final JCheckBoxMenuItem item = new JCheckBoxMenuItem(action);
		menu.add(item);
		configureMenuItem(action, item);
		return item;
	}

	public JCheckBoxMenuItem addToMenuAsCheckBoxMenuItem(Action action, JPopupMenu popupMenu)
	{
		final JCheckBoxMenuItem item = new JCheckBoxMenuItem(action);
		popupMenu.add(item);
		configureMenuItem(action, item);
		return item;
	}

	public JMenuItem addToMenu(Action action, JMenu menu) throws MissingResourceException
	{
		final JMenuItem item = menu.add(action);
		configureMenuItem(action, item);
		return item;
	}

	public JMenu createMenu(String menuKey) throws MissingResourceException
	{
		JMenu menu = new JMenu();
		final String fullKey = Keys.MENU + "." + menuKey;
		menu.setText(getResourceString(fullKey, MenuProperties.TITLE));
		String mn = getResourceString(fullKey, MenuProperties.MNEMONIC);
		if (mn.length() >= 1)
		{
			menu.setMnemonic(mn.charAt(0));
		}
		return menu;
	}

	
	public void setupAction(Action action, boolean showColoricons)
	{
		if (action == null)
		{
			throw new IllegalArgumentException("Action == null");
		}

		final String actionClassName = action.getClass().getName();
		final String key = Keys.ACTION + "." + actionClassName;
		action.putValue(Action.NAME, getResourceString(key, ActionProperties.NAME));

		String shortDescription = getResourceString(key, ActionProperties.TOOLTIP);

		String acceleratorString = getAcceleratorString(action);
		if (null != acceleratorString && 0 < acceleratorString.trim().length())
		{
			shortDescription += "  (" + acceleratorString + ")";

		}

		action.putValue(Action.SHORT_DESCRIPTION, shortDescription);

		String accelerator = getAcceleratorString(action);
		if (null != accelerator)
		{
			action.putValue(ACCELERATOR_STRING, accelerator);
		}

		Icon icon = null;
		try
		{
			if (showColoricons == true)
			{
				icon = getIcon(key, ActionProperties.ROLLOVER_IMAGE);
				action.putValue(Action.SMALL_ICON, icon);
			} else
			{
				icon = getIcon(key, ActionProperties.IMAGE);
				action.putValue(Action.SMALL_ICON, icon);
			}
		} catch (MissingResourceException ex)
		{
			try
			{
				icon = getIcon(key, ActionProperties.IMAGE);
				action.putValue(Action.SMALL_ICON, icon);
			} catch (MissingResourceException ignore)
			{
				
			}
		}

		try
		{
			icon = getIcon(key, ActionProperties.ROLLOVER_IMAGE);
			action.putValue(BaseAction.IBaseActionPropertyNames.ROLLOVER_ICON, icon);
		} catch (MissingResourceException ignore)
		{
			
		}

		try
		{
			icon = getIcon(key, ActionProperties.DISABLED_IMAGE);
			action.putValue(BaseAction.IBaseActionPropertyNames.DISABLED_ICON, icon);
		} catch (MissingResourceException ignore)
		{
			
		}
	}

	public ImageIcon getIcon(String keyName)
	{
		return getIcon(keyName, "image");
	}

	public ImageIcon getIcon(Class<?> objClass, String propName)
	{
		return getIcon(objClass.getName(), propName);
	}

	public ImageIcon getIcon(String keyName, String propName)
	{
		if (keyName == null)
		{
			throw new IllegalArgumentException("keyName == null");
		}
		if (propName == null)
		{
			throw new IllegalArgumentException("propName == null");
		}

		ImageIcon icon = null;

		String rsrcName = getResourceString(keyName, propName);

		if (rsrcName != null && rsrcName.length() > 0)
		{
			icon = privateGetIcon(rsrcName);
			if (icon == null)
			{
				s_log.error("can't load image: " + rsrcName);
			}
		} else
		{
			if (s_log.isDebugEnabled())
			{
				s_log.debug("No resource found for " + keyName + " : " + propName);
			}
		}

		return icon;
	}

	public String getString(String key)
	{
		Utilities.checkNull("getString", "key", key);
		return _bundle.getString(key);
	}

	public void configureMenuItem(Action action, JMenuItem item) throws MissingResourceException
	{
		Utilities.checkNull("configureMenuItem", "action", action, "item", item);
		final String fullKey = Keys.MENU_ITEM + "." + action.getClass().getName();

		if (action.getValue(Action.MNEMONIC_KEY) == null)
		{
			String mn = getResourceString(fullKey, MenuItemProperties.MNEMONIC);
			if (mn.length() > 0)
			{
				item.setMnemonic(mn.charAt(0));
			}
		}

		if (action.getValue(Action.ACCELERATOR_KEY) == null)
		{
			String accel = getResourceString(fullKey, MenuItemProperties.ACCELERATOR);
			if (accel.length() > 0)
			{
				item.setAccelerator(KeyStroke.getKeyStroke(accel));
			}
		}

		String toolTipText = getToolTipTextWithAccelerator(action, fullKey);

		item.setToolTipText(toolTipText);

		
	}

	private String getToolTipTextWithAccelerator(Action action, String fullKey)
	{
		String toolTipText = (String) action.getValue(Action.SHORT_DESCRIPTION);

		if (null == toolTipText)
		{
			toolTipText = "";
		}
		try
		{
			String accel = getResourceString(fullKey, MenuItemProperties.ACCELERATOR);
			if (null != accel && accel.length() > 0)
			{
				toolTipText += "  (" + accel + ")";
			}
		} catch (MissingResourceException e)
		{
			
		}
		return toolTipText;
	}

	protected ResourceBundle getBundle()
	{
		return _bundle;
	}

	private ImageIcon privateGetIcon(String iconName)
	{
		if (iconName != null && iconName.length() > 0)
		{
			URL url;
			String imagePathName = getImagePathName(iconName);

			if (null == _classLoader)
			{
				url = getClass().getResource(imagePathName);

				
				if (null == url && imagePathName.startsWith("/"))
				{
					url = getClass().getResource(imagePathName.substring(1));
				} else if (null == url && false == imagePathName.startsWith("/"))
				{
					url = getClass().getResource("/" + imagePathName);
				}

			} else
			{
				url = _classLoader.getResource(imagePathName);

				if (null == url && imagePathName.startsWith("/"))
				{
					url = _classLoader.getResource(imagePathName.substring(1));
				} else if (null == url && false == imagePathName.startsWith("/"))
				{
					url = _classLoader.getResource("/" + imagePathName);
				}
			}

			if (url != null)
			{
				return new ImageIcon(url);
			}
		}
		return null;
	}

	private String getResourceString(String keyName, String propName) throws MissingResourceException
	{
		return _bundle.getString(keyName + "." + propName);
	}

	private String getImagePathName(String iconName)
	{
		return _imagePath + iconName;
	}
}
