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
	public Round strategy(ABoard board, int numPlayer, int wallCounter, Coordinates[] possibleMoves, int numRounds)
	{
		if(numRounds < 3)
		{
			// The bot always moves (except if the bot is blocked)
			Round round = move(board, numPlayer, possibleMoves, numRounds);
			if (round.getType() == Type.NONE)
				return wall(board, wallCounter, board.findPath((1+numPlayer)%board.getPlayerNumber(), true));
			return round;
		}
		else if(numRounds == 3)
		{
			// Shiller strategy
			if(numPlayer == 0 || numPlayer == 2)
			{
				Round round = shiller(board, numPlayer);
				return decide(round, numPlayer, board, wallCounter, possibleMoves);
			}
			else
			{
				if(checkShiller(board, numPlayer))
				{
					Round round = shiller(board, numPlayer);
					round = decide(round, numPlayer, board, wallCounter, possibleMoves);
					return round;
				}
				Round round = move(board, numPlayer, possibleMoves, numRounds);
				return decide(round, numPlayer, board, wallCounter, possibleMoves);
			}
		}
		else
		{
			// Moves if the path of this player is shorter than the paths for the other players
			// Blocks the shortest path otherwise
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
						tempPath = board.findPath(i, false);
					if (tempPath.getLength() < bestPath.getLength() || bestPath.getLength() == 0)
					{
						bestPath = tempPath;
					}
				}
			}
			
			if (ownPath.getLength() >= bestPath.getLength())
			{
				Round round = wall(board, wallCounter, bestPath);
				if (round.getType() == Type.NONE)
				{
					round = move(board, numPlayer, possibleMoves, numRounds);
					round = decide(round, numPlayer, board, wallCounter, possibleMoves);
				}
				return round;
			}
			else
			{
				Round round = move(board, numPlayer, possibleMoves, numRounds);
				return decide(round, numPlayer, board, wallCounter, possibleMoves);
			}
		}
	}

	@Override
	public String toString()
	{
		return "shiller";
	}
	
	private Round move(ABoard board, int numPlayer, Coordinates[] possibleMoves, int numRounds)
	{
		Path bestPath = null;
		if (numRounds == 0)
		{
			bestPath =  board.findPath(numPlayer, false);
		}
		else
		{
			bestPath = board.findPath(numPlayer, true);
		}
		Coordinates coord = null;
		// If there isn't any available path right now, we take the first possible move
		if (bestPath == null)
		{
			if (possibleMoves.length == 0)
				return new Round(Type.NONE, new Coordinates(-1, -1));
			coord = possibleMoves[0];
		}
		else
		{
			coord = new Coordinates(bestPath.getX(1), bestPath.getY(1));
		}
		return new Round(Type.MOVE, coord);
	}
	
	private Round shiller(ABoard board, int numPlayer)
	{
		Coordinates coord = board.startingPos(numPlayer);
		Random rand = new Random();
		// Player 1
		if (numPlayer == 0 || numPlayer == 1)
		{
			if(rand.nextBoolean())
			{
				coord = new Coordinates(coord.getX()+1, coord.getY());
			}
			else
				coord = new Coordinates(coord.getX()-1, coord.getY());
		}
		// Player 3
		else if (numPlayer == 2 || numPlayer == 3)
		{
			if(rand.nextBoolean())
			{
				coord = new Coordinates(coord.getX(), coord.getY() - 1);
			}
			else
				coord = new Coordinates(coord.getX(), coord.getY() - 1);
		}
		
		if (board.filled(coord) == 1)
		{
			return new Round(Type.NONE, coord);
		}
		return new Round(Type.WALL, coord);
	}
	
	private boolean checkShiller(ABoard board, int numPlayer)
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
		if (coord.getY() == 0)
		{
			if (board.filled(new Coordinates(coord.getX() - 1, coord.getY())) == 1)
				return true;
			else if (board.filled(new Coordinates(coord.getX() + 1, coord.getY())) == 1)
				return true;
			return false;
		}
		else if (coord.getY() == board.getYSize(0)-1)
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

	private Round wall(ABoard board, int wallCounter, Path path)
	{
		if (wallCounter <= 0)
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
	
	private Round decide(Round round, int numPlayer, ABoard board, int wallCounter, Coordinates[] possibleMoves)
	{
		if (round.getType() == Type.NONE)
		{
			Path path = board.findPath((1+numPlayer)%board.getPlayerNumber(), true);
			if (path == null)
				path = board.findPath((1+numPlayer)%board.getPlayerNumber(), false);
			return wall(board, wallCounter, path);
		}
		return round;
	}
}
