

package org.gjt.sp.jedit.gui;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicButtonUI;
import org.gjt.sp.jedit.OperatingSystem;



public class RolloverButton extends JButton
{
	
	
	public RolloverButton()
	{
		setContentAreaFilled(false);

		addMouseListener(new MouseOverHandler());
	} 

	
	
	public RolloverButton(Icon icon)
	{
		this();

		setIcon(icon);
	} 

	
	public void updateUI()
	{
		if(OperatingSystem.isWindows())
		{
			
			setUI(new BasicButtonUI());
		}
		else
			super.updateUI();

		setBorder(new EtchedBorder());
		setBorderPainted(false);
		setMargin(new Insets(1,1,1,1));

		setRequestFocusEnabled(false);
	} 

	
	public boolean isOpaque()
	{
		return false;
	} 

	
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		setBorderPainted(false);
		repaint();
	} 

	
	public void setBorderPainted(boolean b)
	{
		try
		{
			revalidateBlocked = true;
			super.setBorderPainted(b);
		}
		finally
		{
			revalidateBlocked = false;
		}
	} 

	
	
	public void revalidate()
	{
		if(!revalidateBlocked)
			super.revalidate();
	} 

	
	public void paint(Graphics g)
	{
		if(isEnabled())
			super.paint(g);
		else
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setComposite(c);
			super.paint(g2);
		}
	} 

	
	private static final AlphaComposite c = AlphaComposite.getInstance(
		AlphaComposite.SRC_OVER, 0.5f);

	private boolean revalidateBlocked;

	
	
	class MouseOverHandler extends MouseAdapter
	{
		public void mouseEntered(MouseEvent e)
		{
			if (isEnabled())
				setBorderPainted(true);
		}

		public void mouseExited(MouseEvent e)
		{
			setBorderPainted(false);
		}
	} 
}
