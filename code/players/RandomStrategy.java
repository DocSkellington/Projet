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
	public Round strategy(Board board, int numPlayer, int wallCounter, Coordinates[] possibleMoves, int numRounds)
	{
		boolean choice = randgen.nextBoolean();
		if (choice)
			return move(board, possibleMoves);
		else
		{
			Round round = setAWall(board, wallCounter);
			// If it can't manage to set a wall, the bot will move instead
			if (round.getType() == Type.NONE)
				round = move(board, possibleMoves);
			while(round.getType() == Type.NONE && wallCounter > 0)
				round = setAWall(board, wallCounter);
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
	private Round setAWall(Board board, int wallCounter)
	{
		if (wallCounter <= 0)
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
