package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import board.ABoard;
import board.Coordinates;
import board.Wall;
import players.Human;

/** The listener for the walls (used for human players)
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class WallListener implements ActionListener
{
	private Human human;
	private ABoard board;
	
	/** The constructor
	 * 
	 * @param human The human bound to this player
	 * @param board The board
	 */
	public WallListener(Human human, ABoard board)
	{
		this.human = human;
		this.board = board;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (human.isActive())
		{
			String message = e.getActionCommand();
			boolean destroy = ((Wall)e.getSource()).getDestroy();
			
			// Light on
			if (message.charAt(0) == 'l')
			{
				try
				{
					Coordinates coord = Coordinates.parse(message.substring(2));
					if (destroy)
						board.colorAdjacentWallsDestroy(coord, true);
					else
						board.colorAdjacentWalls(coord, true);
				}
				catch(ParseException ex)
				{
					throw new RuntimeException("Can not parse action command" + ex);
				}
			}
			// Light off
			else if (message.charAt(0) == 'u')
			{
				try
				{
					Coordinates coord = Coordinates.parse(message.substring(2));
					if (destroy)
						board.colorAdjacentWallsDestroy(coord, false);
					else
						board.colorAdjacentWalls(coord, false);
				}
				catch(ParseException ex)
				{
					throw new RuntimeException("Can not parse action command" + ex);
				}
			}
			// Click
			else
			{

				try
				{
					Coordinates coord = Coordinates.parse(message);
					if (destroy)
					{
						human.destroyWall(board, coord);
					}
					else
						human.setWall(board, coord);
				}
				catch(ParseException ex)
				{
					throw new RuntimeException("Can not parse action command" + ex);
				}
			}
		}
	}
}
