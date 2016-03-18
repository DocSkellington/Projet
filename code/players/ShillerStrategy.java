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
public class ShillerStrategy implements IStrategy
{
	@Override
	public Round strategy(Board board, int numPlayer, Coordinates[] possibleMoves, int numRounds)
	{
		System.out.println(numRounds);
		if(numRounds < 3)
		{
			return move(board, numPlayer, possibleMoves);
		}
		else if(numRounds == 3)
		{
			System.out.println("Trying to shiller");
			if(numPlayer == 0)
				return shiller(board, numPlayer);
			else
			{
				System.out.println("I'm not number 1 :'(");
				if(checkShiller(board, numPlayer))
				{
					System.out.println("Ahaha, you've shillered");
					Round round = shiller(board, numPlayer);
					if (round.getType() == Type.NONE)
					{
						System.out.println("I can't answer!");
						return move(board, numPlayer, possibleMoves);
					}
					System.out.println("Were you expecting this?!");
					return round;
				}
				System.out.println("Well, I'm moving");
				return move(board, numPlayer, possibleMoves);
			}
		}
		else
		{

			// Seek shortest paths for all players
			// Take the shortest
			// If it's shortest than its own, block
			// Else, move
			
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
				System.out.println("I want to set a wall!");
				Round round = wall(board, bestPath);
				if (round.getType() == Type.NONE)
					return move(board, numPlayer, possibleMoves);
				return round;
			}
			else
			{
				System.out.println("I want to move!");
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
		// Player 1 || 2
		if (coord.getY() == 16 || coord.getY() == 0)
		{
			if(rand.nextBoolean())
			{
				coord = new Coordinates(coord.getX()+1, coord.getY());
			}
			coord = new Coordinates(coord.getX()-1, coord.getY());
		}
		// Player 3 || 4
		else if (coord.getX() == 0 || coord.getX() == 16)
		{
			if(rand.nextBoolean())
			{
				coord = new Coordinates(coord.getX(), coord.getY() - 1);
			}
			coord = new Coordinates(coord.getX(), coord.getY() - 1);
		}
		
		System.out.println(coord);
		
		if (board.filled(coord) == 1)
		{
			System.out.println("Impossible to shiller");
			return new Round(Type.NONE, coord);
		}
		System.out.println("Possible to shiller");
		return new Round(Type.WALL, coord);
	}
	
	private boolean checkShiller(Board board, int numPlayer)
	{
		System.out.println("Hello. I'm your biggest fan.");
		Coordinates coord = null;
		if (numPlayer == 1)
		{
			System.out.println("I'm 2");
			coord = board.startingPos(0);
		}
		System.out.println(coord);
		if (coord.getY() == 0)
		{
			System.out.println("a");
			if (board.filled(new Coordinates(coord.getX() - 1, coord.getY())) == 1)
				return true;
			else if (board.filled(new Coordinates(coord.getX() + 1, coord.getY())) == 1)
				return true;
			return false;
		}
		else if (coord.getY() == 16)
		{
			System.out.println("You're too slow");
			if (board.filled(new Coordinates(coord.getX() - 1, coord.getY())) == 1)
			{
				return true;
			}
			else if (board.filled(new Coordinates(coord.getX() + 1, coord.getY())) == 1)
			{
				System.out.println("There is a wall");
				return true;
			}
			return false;
		}
		else if (coord.getX() == 0)
		{
			System.out.println("c");
			if (board.filled(new Coordinates(coord.getX(), coord.getY() - 1)) == 1)
				return true;
			else if (board.filled(new Coordinates(coord.getX(), coord.getY() + 1)) == 1)
				return true;
			return false;
		}
		else
		{
			System.out.println("d");
			if (board.filled(new Coordinates(coord.getX(), coord.getY() - 1)) == 1)
				return true;
			else if (board.filled(new Coordinates(coord.getX(), coord.getY() + 1)) == 1)
				return true;
			return false;
		}
	}

	private Round wall(Board board, Path path)
	{
		// TODO : What if there's a wall ? (4 INSTEAD OF 2)
		for (int i = 0 ; i < path.getLength()-1 ; i++)
		{
			int dx = (path.getX(i+1)-path.getX(i));
			int dy = (path.getY(i+1)-path.getY(i));
			
			if (board.filled(new Coordinates(path.getX(i)+dx/2, path.getY(i)+dy/2)) == 0)
				return new Round(Type.WALL, new Coordinates(path.getX(i)+dx/2, path.getY(i)+dy/2));
			
			// Vertical
			if (dx != 0)
			{
				if (board.filled(new Coordinates(path.getX(i), path.getY(i)+dy/2-2)) == 0)
				{
					return new Round(Type.WALL, new Coordinates(path.getX(i), path.getY(i)+dy/2-2));
				}
			}
			// Horizontal
			if (dy != 0)
			{
				if (board.filled(new Coordinates(path.getX(i)+dx/2-2, path.getY(i))) == 0)
				{
					return new Round(Type.WALL, new Coordinates(path.getX(i)+dx/2-2, path.getY(i)));
				}
			}
		}
		return new Round(Type.NONE, new Coordinates(-1, -1));
	}
}
