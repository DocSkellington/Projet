package players;

import java.util.*;
import board.*;

/**
 * 
 * @author Thibaut De Cooman & Gaetan Staquet
 *
 */
public class RandomAI extends APlayer
{
	private Random randgen = new Random();
	
	/** Constructor	 */
	public RandomAI(Board board, int num, int wallsCounter)
	{
		super(board, num, wallsCounter);
	}
	
	/** Plays randomly */
	public void play()
	{
		System.out.println("Player " + num + " (RandomAI) processing...");
		boolean choice = randgen.nextBoolean();
		if (choice)
			move();
		else
			// If it can't manage to set a wall, the bot will move instead
			if (!setAWall())
				move();
	}
	
	/** The bot moves to a random possible destination*/
	public void move()
	{
		Coordinates[] possibleMoves = possibleMoves().toArray(new Coordinates[0]);
		int choice = randgen.nextInt(possibleMoves.length);
		board.move(num, possibleMoves[choice]);
		coord = possibleMoves[choice];
	}
	
	/** The bot sets a wall if possible, trying fifty times at a random location
	 * 
	 * @return true if the bot can set a wall
	 */
	public boolean setAWall()
	{
		if (wallsCounter <= 0)
			return false;
		
		int x = randgen.nextInt(board.getXSize());
		int y = randgen.nextInt(board.getYSize());
		int tries = 50;
		
		while (!board.setWall(num, new Coordinates(x,y), randgen.nextBoolean()) && tries > 0)
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
