package players;

import players.Round.Type;
import board.*;


/** This AI uses a strategy
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */

public final class StrategyAI extends APlayer
{
	private int numRounds;
	private IStrategy strat;
	
	/** A constructor
	 * 
	 * @param num The number of this player
	 * @param wallCounter The number of available walls
	 * @param name The name of this player
	 */
	public StrategyAI(int num, int wallCounter, String name)
	{
		super(num, wallCounter, name);
		numRounds = 0;
		strat = new ShillerStrategy();
	}
	
	/** A constructor
	 * 
	 * @param num The number of this player
	 * @param wallCounter The number of available walls
	 * @param name The name of this player
	 * @param strat A Strategy to use
	 */
	public StrategyAI(int num, int wallCounter, String name, IStrategy strat)
	{
		super(num, wallCounter, name);
		numRounds = 0;
		this.strat = strat;
	}
	
	@Override
	public void reset(int wallCounter)
	{
		super.reset(wallCounter);
		numRounds = 0;
	}
	
	@Override
	public String toString()
	{
		return this.name + "\t" + strat.toString();
	}
	
	@Override
	protected Round doPlay(ABoard board)
	{
		Round round = strat.strategy(board, num, wallCounter, possibleMoves(board, true), numRounds++);
		if (round.getType() == Type.MOVE)
		{
			board.move(num, round.getCoord());
			return round;
		}
		else if (round.getType() == Type.WALL)
		{
			board.setWall(round.getCoord());
			wallCounter--;
			return round;
		}
		else if (round.getType() == Type.DEST)
		{
			board.destroyWall(round.getCoord());
			waitingTurns++;
			return round;
		}
		// Can only occur in 3-4 players mode
		else
			return round;
	}
}
