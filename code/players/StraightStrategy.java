package players;

import board.ABoard;
import board.Coordinates;
import players.Round.Type;
import pathFinder.Path;


/** This AI goes straight for its goal. It will move toward its objective; if it can't, it will set a wall to block the opponent with the shortest path.
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */
public final class StraightStrategy implements IStrategy
{
	@Override
	public Round strategy(ABoard board, int numPlayer, int wallCounter, Coordinates[] possibleMoves, int numRounds)
	{
		if (possibleMoves.length != 0)
		{
			Round round = move(board, possibleMoves, numPlayer, numRounds);
			if (round.getType() == Type.NONE)
				return wall(board, numPlayer, wallCounter);
			else
				return round;
		}
		return wall(board, numPlayer, wallCounter);
	}
	
	@Override
	public String toString()
	{
		return "straight";
	}
	
	private Round move(ABoard board, Coordinates[] possibleMoves, int numPlayer, int numRounds)
	{
		Path bestPath = null;
		if (numRounds == 0)
			bestPath = board.findPath(numPlayer, false);
		else
			bestPath = board.findPath(numPlayer, true);
		
		if (bestPath == null)
		{
			return new Round(Type.NONE, new Coordinates(-1, -1));
		}
		else
			return new Round(Type.MOVE, new Coordinates(bestPath.getX(1), bestPath.getY(1)));
		
	}
	
	private Round wall(ABoard board, int numPlayer, int wallCounter)
	{
		if (wallCounter <= 0)
		{
			return new Round(Type.NONE, new Coordinates(-1, -1));
		}
		Path bestPath = new Path();
		for (int i = 0 ; i < board.getPlayerNumber() ; i++)
		{
			if (i != numPlayer)
			{
				Path tempPath = board.findPath(i, true);
				if(tempPath == null)
					tempPath = board.findPath(numPlayer, false);
				if (tempPath.getLength() < bestPath.getLength() || bestPath.getLength() == 0)
				{
					bestPath = tempPath;
				}
			}
		}
		for (int i = 0 ; i < bestPath.getLength()-1 ; i++)
		{
			int dx = (bestPath.getX(i+1)-bestPath.getX(i));
			int dy = (bestPath.getY(i+1)-bestPath.getY(i));
			
			if(board.canSetWall(new Coordinates(bestPath.getX(i)+dx/2, bestPath.getY(i)+dy/2)))
			{
				return new Round(Type.WALL, new Coordinates(bestPath.getX(i)+dx/2, bestPath.getY(i)+dy/2));
			}
			
			// Vertical
			if (dx != 0)
			{
				if (board.canSetWall(new Coordinates(bestPath.getX(i), bestPath.getY(i)+dy/2-2)))
				{
					return new Round(Type.WALL, new Coordinates(bestPath.getX(i), bestPath.getY(i)+dy/2-2));
				}
			}
			// Horizontal
			if (dy != 0)
			{
				if (board.canSetWall(new Coordinates(bestPath.getX(i)+dx/2-2, bestPath.getY(i))))
				{
					return new Round(Type.WALL, new Coordinates(bestPath.getX(i)+dx/2-2, bestPath.getY(i)));
				}
			}
		}
		return new Round(Type.NONE, new Coordinates(-1, -1));
	}
}
