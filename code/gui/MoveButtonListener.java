package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import board.Board;
import players.Human;

/** The listener for the move button (used by human players to activate the cases them can reach)
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class MoveButtonListener implements ActionListener
{
	private GameFrame frame;
	private Human human;
	private Board board;
	
	/** Constructor
	 * 
	 * @param frame The game frame
	 * @param human A human
	 * @param board The board
	 */
	public MoveButtonListener(GameFrame frame, Human human, Board board)
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
			human.move(board);
		}
	}
}
