package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;

import board.ABoard;
import board.Coordinates;
import players.Human;

/** The listener for the case (used for human players)
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class CaseListener implements ActionListener
{
	private Human human;
	private ABoard board;
	
	/** The constructor
	 * 
	 * @param human The human bound to the listener
	 * @param board The board
	 */
	public CaseListener(Human human, ABoard board)
	{
		this.human = human;
		this.board = board;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (human.isActive())
		{
			try
			{
				Coordinates coord = Coordinates.parse(((JButton) e.getSource()).getActionCommand());
				human.doMove(board, coord);
			}
			catch (ParseException ex)
			{
				throw new RuntimeException("Can not parse action command" + ex);
			}
		}
	}
}
