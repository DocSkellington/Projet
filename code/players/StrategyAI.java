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
	 */
	public StrategyAI(int num, int wallCounter)
	{
		super(num, wallCounter);
		numRounds = 0;
		strat = new ShillerStrategy();
	}
	
	@Override
	public void reset(int wallCounter)
	{
		super.reset(wallCounter);
		numRounds = 0;
	}

	/** A constructor
	 * 
	 * @param num The number of this player
	 * @param wallCounter The number of available walls
	 * @param strat A Strategy to use
	 */
	public StrategyAI(int num, int wallCounter, IStrategy strat)
	{
		super(num, wallCounter);
		numRounds = 0;
		this.strat = strat;
	}
	
	@Override
	public String toString()
	{
		return strat.toString();
	}
	
	@Override
	protected Round doPlay(Board board)
	{
		Round round = strat.strategy(board, num, wallCounter, possibleMoves(board, true).toArray(new Coordinates[0]), numRounds++);
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
