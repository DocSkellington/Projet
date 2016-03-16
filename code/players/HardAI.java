package players;

import pathFinder.*;
import board.*;

/** This AI uses two different opening strategies (depending on the other player), then moves if it can reach his goal faster than his opponent(s)
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */

public final class HardAI extends APlayer
{
	private int numRounds;
	public HardAI(int num, int wallsCounter)
	{
		super(num, wallsCounter);
		numRounds = 0;
	}
	
	
	/** Decides whether the HardAI moves or sets a wall */
	public void play(Board board)
	{
		Coordinates coord = board.getCoordinates(num);
		if (numRounds < 2)
		{
			if(!move(board))
			{
				
			}
		}
		else
		{
			
		}
		numRounds++;
	}
	
	
	/** Follows the shortest path */
	private boolean move(Board board)
	{
		Path bestPath = board.findPath(this);
		return board.move(num, new Coordinates(bestPath.getX(0), bestPath.getY(0)));
	}
	
	private void blockLongestPath(Board board)
	{
		
	}
	
	/** Sets a wall to block the shortest path of an opponent */
	private void setAWall(Board board)
	{
		
	}
}
