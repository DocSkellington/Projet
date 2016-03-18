package players;

import java.util.Random;
import pathFinder.*;
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
	 * @param wallsCounter The number of available walls
	 */
	public StrategyAI(int num, int wallsCounter)
	{
		super(num, wallsCounter);
		numRounds = 0;
		strat = new ShillerStrategy();
	}

	/** A constructor
	 * 
	 * @param num The number of this player
	 * @param wallsCounter The number of available walls
	 * @param strat A Strategy to use
	 */
	public StrategyAI(int num, int wallsCounter, IStrategy strat)
	{
		super(num, wallsCounter);
		numRounds = 0;
		this.strat = strat;
	}
	
	@Override
	public void play(Board board)
	{
		System.out.println("Player " + (num+1) + " (HardAI) processing...");
		
		Round round = strat.strategy(board, num, possibleMoves(board, true).toArray(new Coordinates[0]), numRounds);
		if (round.getType() == Type.MOVE)
		{
			board.move(num, round.getCoord());
		}
		else if (round.getType() == Type.WALL)
		{
			System.out.println("Trying to set a wall");
			board.setWall(round.getCoord());
		}
		numRounds++;
	}
}
