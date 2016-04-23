package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import players.Human;

/** The listener for the Skip action button
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class SkipButtonListener implements ActionListener
{
	private Human human;
	private GameFrame frame;
	
	/** The constructor
	 * 
	 * @param frame The game frame
	 * @param human The human bound to this button
	 */
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
