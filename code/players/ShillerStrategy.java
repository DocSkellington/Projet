package players;

import java.util.Random;
import pathFinder.Path;
import players.Round.Type;
import board.*;

/** Describes the Shiller opening, then uses the following strategy : if the player has the shortest path to reach their target, they move, otherwise they
 * block the opponent with the shortest path.
 *
 * @author De Cooman Thibaut
 * @author Staquet Gaetan
 *
 */
public final class ShillerStrategy implements IStrategy
{
	@Override
	public Round strategy(Board board, int numPlayer, int wallsCounter, Coordinates[] possibleMoves, int numRounds)
	{
		System.out.println("Shiller strategy in process...");
		if(numRounds < 3)
		{
			return move(board, numPlayer, possibleMoves);
		}
		else if(numRounds == 3)
		{
			if(numPlayer == 0)
				return shiller(board, numPlayer);
			else
			{
				if(checkShiller(board, numPlayer))
				{
					Round round = shiller(board, numPlayer);
					if (round.getType() == Type.NONE)
					{
						return move(board, numPlayer, possibleMoves);
					}
					return round;
				}
				return move(board, numPlayer, possibleMoves);
			}
		}
		else
		{
			Path bestPath = new Path();
			Path ownPath = board.findPath(numPlayer, true);
			if(ownPath == null)
				ownPath = board.findPath(numPlayer, false);
			
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
			
			if (ownPath.getLength() > bestPath.getLength())
			{
				Round round = wall(board, wallsCounter, bestPath);
				if (round.getType() == Type.NONE)
					return move(board, numPlayer, possibleMoves);
				return round;
			}
			else
			{
				return move(board, numPlayer, possibleMoves);
			}
		}
	}

	private Round move(Board board, int numPlayer, Coordinates[] possibleMoves)
	{
		Path bestPath = board.findPath(numPlayer, true);
		Coordinates coord = null;
		// If there isn't any available path right now, we take the first possible move
		if (bestPath == null)
		{
			coord = possibleMoves[0];
		}
		else
			coord = new Coordinates(bestPath.getX(1), bestPath.getY(1));
		return new Round(Type.MOVE, coord);
	}
	
	private Round shiller(Board board, int numPlayer)
	{
		Coordinates coord = board.startingPos(numPlayer);
		Random rand = new Random();
		// Player 1
		if (coord.getY() == 16)
		{
			if(rand.nextBoolean())
			{
				coord = new Coordinates(coord.getX()+1, 14);
			}
			else
				coord = new Coordinates(coord.getX()-1, 14);
		}
		// Player 2
		else if (coord.getY() == 0)
		{
			if(rand.nextBoolean())
			{
				coord = new Coordinates(coord.getX()+1, 0);
			}
			else
				coord = new Coordinates(coord.getX()-1, 0);
		}
		// Player 3
		else if (coord.getX() == 0)
		{
			if(rand.nextBoolean())
			{
				coord = new Coordinates(coord.getX(), coord.getY() - 1);
			}
			else
				coord = new Coordinates(coord.getX(), coord.getY() - 1);
		}
		// Player 4
		else if (coord.getX() == 16)
		{
			if(rand.nextBoolean())
			{
				coord = new Coordinates(14, coord.getY() - 1);
			}
			else
				coord = new Coordinates(14, coord.getY() - 1);
		}
		
		if (board.filled(coord) == 1)
		{
			return new Round(Type.NONE, coord);
		}
		return new Round(Type.WALL, coord);
	}
	
	private boolean checkShiller(Board board, int numPlayer)
	{
		Coordinates coord = null;
		if (numPlayer == 1)
		{
			coord = board.startingPos(0);
		}
		else if (numPlayer == 2)
			coord = board.startingPos(3);
		else if (numPlayer == 3)
			coord = board.startingPos(2);
		// TODO : 6 players
		if (coord.getY() == 0)
		{
			if (board.filled(new Coordinates(coord.getX() - 1, coord.getY())) == 1)
				return true;
			else if (board.filled(new Coordinates(coord.getX() + 1, coord.getY())) == 1)
				return true;
			return false;
		}
		else if (coord.getY() == 16)
		{
			if (board.filled(new Coordinates(coord.getX() - 1, coord.getY())) == 1)
			{
				return true;
			}
			else if (board.filled(new Coordinates(coord.getX() + 1, coord.getY())) == 1)
			{
				return true;
			}
			return false;
		}
		else if (coord.getX() == 0)
		{
			if (board.filled(new Coordinates(coord.getX(), coord.getY() - 1)) == 1)
				return true;
			else if (board.filled(new Coordinates(coord.getX(), coord.getY() + 1)) == 1)
				return true;
			return false;
		}
		else
		{
			if (board.filled(new Coordinates(coord.getX(), coord.getY() - 1)) == 1)
				return true;
			else if (board.filled(new Coordinates(coord.getX(), coord.getY() + 1)) == 1)
				return true;
			return false;
		}
	}

	private Round wall(Board board, int wallsCounter, Path path)
	{
		if (wallsCounter <= 0)
			return new Round(Type.NONE, new Coordinates(-1, -1));
		for (int i = 0 ; i < path.getLength()-1 ; i++)
		{
			int dx = (path.getX(i+1)-path.getX(i));
			int dy = (path.getY(i+1)-path.getY(i));
			
			if(board.canSetWall(new Coordinates(path.getX(i)+dx/2, path.getY(i)+dy/2)))
			{
				return new Round(Type.WALL, new Coordinates(path.getX(i)+dx/2, path.getY(i)+dy/2));
			}
			
			// Vertical
			if (dx != 0)
			{
				if (board.canSetWall(new Coordinates(path.getX(i), path.getY(i)+dy/2-2)))
				{
					return new Round(Type.WALL, new Coordinates(path.getX(i), path.getY(i)+dy/2-2));
				}
			}
			// Horizontal
			if (dy != 0)
			{
				if (board.canSetWall(new Coordinates(path.getX(i)+dx/2-2, path.getY(i))))
				{
					return new Round(Type.WALL, new Coordinates(path.getX(i)+dx/2-2, path.getY(i)));
				}
			}
		}
		return new Round(Type.NONE, new Coordinates(-1, -1));
	}
}
