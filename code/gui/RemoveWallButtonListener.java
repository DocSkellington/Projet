package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import board.ABoard;
import players.Human;

/** The listener for the remove wall button
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class RemoveWallButtonListener implements ActionListener
{
	private GameFrame frame;
	private Human human;
	private ABoard board;

	/** Constructor
	 * 
	 * @param frame The game frame
	 * @param human A human
	 * @param board The board
	 */
	public RemoveWallButtonListener(GameFrame frame, Human human, ABoard board)
	{
		this.frame = frame;
		this.human = human;
		this.board = board;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (human.isActive())
		{
			frame.setActionButtonBorder(null);
			Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			((JButton) e.getSource()).setBorder(border);
			human.removeWalls(board);
		}
	}

}
