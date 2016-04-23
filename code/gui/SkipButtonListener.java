package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import players.Human;

public class SkipButtonListener implements ActionListener
{
	private Human human;
	private GameFrame frame;
	
	public SkipButtonListener(GameFrame frame, Human human)
	{
		this.human = human;
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (human.isActive())
		{
			frame.setActionButtonBorder(null);
			Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			((JButton) e.getSource()).setBorder(border);
			human.skip();
		}
	}

}
