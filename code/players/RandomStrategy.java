package players;

import java.util.*;
import board.*;
import players.Round.Type;

/** This AI decides randomly if it moves or sets a wall.
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */
public final class RandomStrategy implements IStrategy
{
	private Random randgen;
	
	/** Constructor	 */
	public RandomStrategy()
	{
		randgen = new Random();
	}
	
	@Override
	public Round strategy(Board board, int numPlayer, int wallsCounter, Coordinates[] possibleMoves, int numRounds)
	{
		System.out.println("Random strategy in process...");
		boolean choice = randgen.nextBoolean();
		if (choice)
			return move(board, possibleMoves);
		else
		{
			Round round = setAWall(board, wallsCounter);
			// If it can't manage to set a wall, the bot will move instead
			if (round.getType() == Type.NONE)
				return move(board, possibleMoves);
			return round;
		}
	}
	
	/** The bot moves to a random possible destination*/
	private Round move(Board board, Coordinates[] possibleMoves)
	{
		int choice = randgen.nextInt(possibleMoves.length);
		//board.move(num, possibleMoves[choice]);
		return new Round(Type.MOVE, possibleMoves[choice]);
	}
	
	/** The bot sets a wall if possible, trying fifty times at a random location */
	private Round setAWall(Board board, int wallsCounter)
	{
		if (wallsCounter <= 0)
			return new Round(Type.NONE, new Coordinates(-1, -1));
		
		int x = randgen.nextInt(board.getXSize());
		int y = randgen.nextInt(board.getYSize());
		int tries = 50;
		
		while (!board.canSetWall(new Coordinates(x,y)) && tries > 0)
		{
			x = randgen.nextInt(board.getXSize());
			y = randgen.nextInt(board.getYSize());
			tries--;
		}
		if (tries > 0)
			return new Round(Type.WALL, new Coordinates(x,y));
		return new Round(Type.NONE, new Coordinates(-1, -1));
	}
}
