package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import board.Board;
import players.Human;

/** The listener for the remove wall button
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class RemoveWallButtonListener implements ActionListener
{
	private ActionButton wall;
	private Human human;
	private Board board;

	/** Constructor
	 * 
	 * @param wall The wall button
	 * @param human A human
	 * @param board The board
	 */
	public RemoveWallButtonListener(ActionButton wall, Human human, Board board)
	{
		this.wall = wall;
		this.human = human;
		this.board = board;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (human.isActive())
		{
			Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			((JButton) e.getSource()).setBorder(border);
			wall.setBorder(null);
			human.removeWalls(board);
		}
	}

}
