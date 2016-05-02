package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.JButton;

import board.Board;
import players.Human;

/** The listener of the wall action button (used for human players)
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class WallButtonListener implements ActionListener
{
	private GameFrame frame;
	private Human human;
	private Board board;
	
	/** The constructor
	 * 
	 * @param frame The main frame
	 * @param human The human bound to this listener
	 * @param board The board
	 */
	public WallButtonListener(GameFrame frame, Human human, Board board)
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
			human.walls(board);
		}
	}
}
