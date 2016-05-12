package players;

import board.*;
import players.Round.Type;

import java.text.ParseException;
import java.util.TreeSet;

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
	protected String name;
	
	/** The default constructor */
	public APlayer()
	{
		wallCounter = 10;
		waitingTurns = 0;
		name = 0 + "";
	}
	
	/** The constructor that must be used
	 * 
	 * @param num The number of the Player
	 * @param wallCounter The number of walls this player can set
	 * @param name The name of this player
	 */
	public APlayer(int num, int wallCounter, String name)
	{
		this.num = num;
		this.wallCounter = wallCounter;
		waitingTurns = 0;
		this.name = name;
	}
	
	/** Gets the name of this player
	 * 
	 * @return The name of this player
	 */
	public String getName()
	{
		return name;
	}
	
	/** This function handles the turn of a player.
	 * 
	 * @param board The board
	 * @return The played round
	 */
	public Round play(ABoard board)
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
	
	/** Skip/Stops this turn
	 * 
	 * @return A Round of type NONE
	 */
	public Round skip()
	{
		return new Round(Type.NONE, new Coordinates(-1, -1));
	}
	
	/** Plays the given round (this is used to load/replay)
	 * 
	 * @param board The board
	 * @param round The round to do
	 */
	public void play(ABoard board, Round round)
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
	
	/** Effectively plays
	 * 
	 * @param board The board
	 * @return The round done
	 */
	protected abstract Round doPlay(ABoard board);
	
	/** Parse a string and create a new player with the given number (and 10 walls)
	 * 
	 * @param num The number of the player
	 * @param string The string to parse
	 * @return A reference to the new player (Human/StrategyAI)
	 * @throws ParseException If the string is incorrect, a ParseException is thrown
	 */
	public static APlayer parse(int num, String string) throws ParseException
	{
		String[] words = string.split("\t");
		switch(words[1])
		{
		case "human":
			return new Human(num, 10, words[0]);
		case "shiller":
			return new StrategyAI(num, 10, words[0], new ShillerStrategy());
		case "random":
			return new StrategyAI(num, 10, words[0], new RandomStrategy());
		case "straight":
			return new StrategyAI(num, 10, words[0], new StraightStrategy());
		default:
			throw new ParseException("Invalid player", 0);
		}
	}
}
