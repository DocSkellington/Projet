package players;

import board.*;
import players.Round.Type;

import java.text.ParseException;
import java.util.HashSet;

/** Abstract class to handle a player.
 * This can't be used because play() must have a specific behaviour depending on the type of the player (Human/AI).
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public abstract class APlayer
{
	protected int num, wallCounter, waitingTurns;
	
	/** The default constructor */
	public APlayer()
	{
		wallCounter = 10;
		waitingTurns = 0;
	}
	
	/** The constructor that must be used
	 * 
	 * @param num The number of the Player
	 * @param wallCounter The number of walls this player can set
	 */
	public APlayer(int num, int wallCounter)
	{
		this.num = num;
		this.wallCounter = wallCounter;
		waitingTurns = 0;
	}
	
	/** This function handles the turn of a player.
	 * 
	 * @param board The board
	 * @return The played round
	 */
	public synchronized Round play(Board board)
	{
		if (waitingTurns == 0)
		{
			return doPlay(board);
		}
		else
		{
			waitingTurns--;
			return skip();
		}
	}
	
	/** Skip/Stops this turn */
	public Round skip()
	{
		return new Round(Type.NONE, new Coordinates(-1, -1));
	}
	
	/** Plays the given round (this is used to load/replay)
	 * 
	 * @param board The board
	 * @param round The round to do
	 */
	public void play(Board board, Round round)
	{
		switch(round.getType())
		{
		case MOVE:
			board.move(num, round.getCoord());
			break;
		case WALL:
			board.setWall(round.getCoord());
			wallCounter--;
			break;
		case DEST:
			board.destroyWall(round.getCoord());
			waitingTurns++;
			break;
		case NONE:
			if (waitingTurns > 0)
				waitingTurns--;
			break;
		}
	}
	
	/** Gets the number of the Player
	 * 
	 * @return The number of the Player
	 */
	public int getNum()
	{
		return num;
	}
	
	/** Gives the possible moves of this player for their current position
	 * 
	 * @param board The board
	 * @param withPlayer If we consider other player(s) or not
	 * @return An HashSet of the possible coordinates
	 */
	public HashSet<Coordinates> possibleMoves(Board board, boolean withPlayer)
	{
		return possibleMoves(board, withPlayer, board.getCoordinates(num));
	}
	
	/** Gets the number of available walls
	 * 
	 * @return The number of available walls 
	 */
	public int getWallCounter()
	{
		return wallCounter;
	}
	
	/** Reset the main fields of this player and set wallCounter to the given value
	 * 
	 * @param wallCounter The new wallCounter
	 */
	public void reset(int wallCounter)
	{
		this.wallCounter = wallCounter;
	}
	
	/** Gives the possible moves of this player for a precise position
	 * 
	 * @param board The board
	 * @param withPlayer If we check with other players or not
	 * @param pos The position we look at
	 * @return The (relative) coordinates of the accessible cases
	 */
	public HashSet<Coordinates> possibleMoves(Board board, boolean withPlayer, Coordinates pos)
	{
		HashSet<Coordinates> coord = new HashSet<Coordinates>();
		
		for (int i = -2 ; i <= 2 ; i += 2)
		{
			for (int j = -2 ; j <= 2 ; j += 2)
			{
				if (i == 0 && j == 0)
					continue;
				if (Math.abs(i) == 2 && Math.abs(j) == 2)
					continue;
				// Checking diagonals or jumping
				// First : x coordinate
				if(board.filled(pos.getX()+i, pos.getY()) != 0 && i != 0 && withPlayer)
				{
				    // If there isn't a wall on the way
				    if (board.filled(pos.getX()+i/2, pos.getY()) == 0)
					{
				        int x = pos.getX() + i, y = pos.getY();
					    // Can we jump over the other player ?
					    if(!board.blocked(x, y, x + i, y, withPlayer))
					    {
						    // Yes
						    coord.add(new Coordinates(x+i, y));
					    }
					    else
					    {
						    // No
						    // Up
						    if(!board.blocked(x, y, x, y-2, withPlayer))
						    {
							    coord.add(new Coordinates(x, y-2));
						    }
						    // Down
						    if(!board.blocked(x, y, x, y+2, withPlayer))
						    {
							    coord.add(new Coordinates(x, y+2));
						    }
					    }
				    }
				}
				else if (pos.getX() + i != pos.getX())
				{
					if(!board.blocked(pos.getX(), pos.getY(), pos.getX()+i, pos.getY(), withPlayer))
						coord.add(new Coordinates(pos.getX() + i, pos.getY()));
				}
				// y coordinate
				if(board.filled(pos.getX(), pos.getY()+j) != 0 && j != 0 && withPlayer)
				{
				    // If there isn't any wall on the way
				    if (board.filled(pos.getX(), pos.getY()+j/2) == 0)
					{
					    int x = pos.getX(), y = pos.getY() + j;
					    // Can we jump over it ?
					    if(!board.blocked(x, y, x, y + j, withPlayer))
					    {
						    // Yes
						    coord.add(new Coordinates(x, y + j));
					    }
					    else
					    {
						    // No
						    // Left
						    if(!board.blocked(x, y, x - 2, y, withPlayer))
						    {
							    coord.add(new Coordinates(x - 2, y));
						    }
						    // Right
						    if(!board.blocked(x, y, x + 2, y, withPlayer))
						    {
							    coord.add(new Coordinates(x + 2, y));
						    }
					    }
				    }
			    }
			    else if (pos.getY() + j != pos.getY())
			    {
				    if (!board.blocked(pos.getX(), pos.getY(), pos.getX(), pos.getY() + j, withPlayer))
					    coord.add(new Coordinates(pos.getX(), pos.getY() + j));
			    }
		    }
		}
		
		return coord;
	}
	
	// Effectively plays
	protected abstract Round doPlay(Board board);
	
	/** Parse a string and create a new player with the given number (and 10 walls)
	 * 
	 * @param num The number of the player
	 * @param string The string to parse
	 * @return A reference to the new player (Human/StrategyAI)
	 * @throws ParseException If the string is incorrect, a ParseException is thrown
	 */
	public static APlayer parse(int num, String string) throws ParseException
	{
		switch(string)
		{
		case "human":
			return new Human(num, 10);
		case "shiller":
			return new StrategyAI(num, 10, new ShillerStrategy());
		case "random":
			return new StrategyAI(num, 10, new RandomStrategy());
		case "straight":
			return new StrategyAI(num, 10, new StraightStrategy());
		default:
			throw new ParseException("Invalid player", 0);
		}
	}
}
