

package org.gjt.sp.jedit.gui.statusbar;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.CompletionPopup;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.textarea.JEditTextArea;



public class MultiSelectWidgetFactory implements StatusWidgetFactory
{
	
	public Widget getWidget(View view)
	{
		Widget multiSelect = new MultiSelectWidget(view);
		return multiSelect;
	} 

	
	private static class MultiSelectWidget implements Widget
	{
		private final JLabel multiSelect;
		private final View view;
		MultiSelectWidget(final View view)
		{
			multiSelect = new ToolTipLabel();
			multiSelect.setHorizontalAlignment(SwingConstants.CENTER);
			multiSelect.setToolTipText(jEdit.getProperty("view.status.multi-tooltip"));
			this.view = view;
			multiSelect.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					JEditTextArea textArea = view.getTextArea();
					if (textArea != null)
						textArea.toggleMultipleSelectionEnabled();
				}
			});
		}

		public JComponent getComponent()
		{
			return multiSelect;
		}

		public void update()
		{
			JEditTextArea textArea = view.getTextArea();
			if (textArea != null)
			{
				if (textArea.isMultipleSelectionEnabled())
				{
					multiSelect.setText("M");
					multiSelect.setEnabled(true);
				}
				else
				{
					multiSelect.setText("m");
					multiSelect.setEnabled(false);
				}
			}
		}

		public void propertiesChanged()
		{
			
			Font font = new JLabel().getFont();
			
			FontMetrics fm = multiSelect.getFontMetrics(font);
			Dimension dim = new Dimension(
						      Math.max(fm.charWidth('m'),fm.charWidth('M')) + 1,
						      fm.getHeight());
			multiSelect.setPreferredSize(dim);
			multiSelect.setMaximumSize(dim);
		}
	} 

}
