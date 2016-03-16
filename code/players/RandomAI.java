package players;

import java.util.*;
import board.*;

/** This AI decides randomly if it moves or sets a wall.
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */
public final class RandomAI extends APlayer
{
	private Random randgen = new Random();
	
	/** Constructor	 */
	public RandomAI(int num, int wallsCounter)
	{
		super(num, wallsCounter);
	}
	
	@Override
	public void play(Board board)
	{
		System.out.println("Player " + (num+1) + " (RandomAI) processing...");
		boolean choice = randgen.nextBoolean();
		if (choice)
			move(board);
		else
			// If it can't manage to set a wall, the bot will move instead
			if (!setAWall(board))
				move(board);
	}
	
	/** The bot moves to a random possible destination*/
	private void move(Board board)
	{
		Coordinates[] possibleMoves = possibleMoves(board).toArray(new Coordinates[0]);
		int choice = randgen.nextInt(possibleMoves.length);
		board.move(num, possibleMoves[choice]);
	}
	
	/** The bot sets a wall if possible, trying fifty times at a random location
	 * 
	 * @return true if the bot can set a wall
	 */
	private boolean setAWall(Board board)
	{
		if (wallsCounter <= 0)
			return false;
		
		int x = randgen.nextInt(board.getXSize());
		int y = randgen.nextInt(board.getYSize());
		int tries = 50;
		
		while (!board.setWall(new Coordinates(x,y)) && tries > 0)
		{
			x = randgen.nextInt(board.getXSize());
			y = randgen.nextInt(board.getYSize());
			tries--;
		}
		if (tries > 0)
			wallsCounter--;
		return true;
	}
}
