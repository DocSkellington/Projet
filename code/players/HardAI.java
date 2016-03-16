package players;

import java.util.Random;
import pathFinder.*;
import board.*;


/** This AI uses two different opening strategies (depending on the other player), then moves if it can reach his goal faster than his opponent(s)
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */

public final class HardAI extends APlayer
{
	private int numRounds;
	/** The constructor
	 * 
	 * @param num The number of this player
	 * @param wallsCounter The number of available walls
	 */
	public HardAI(int num, int wallsCounter)
	{
		super(num, wallsCounter);
		numRounds = 0;
	}
	
	
	/** Decides whether the HardAI moves or sets a wall
	 * 
	 * @param board The board
	 * 
	 * */
	public void play(Board board)
	{
		System.out.println("Player " + (num+1) + " (HardAI) processing...");
		
		if (numRounds < 3)
		{
			System.out.println("Hello");
			move(board);
		}
		else if (numRounds == 3)
		{
			if(num == 0)
			{
				shiller(board);
			}
			else
			{
				// TODO : Generaliser !
				if (board.hasWall(new Coordinates(7, 0)) || board.hasWall(new Coordinates(9, 0)))
					shiller(board);
				else if (board.hasWall(new Coordinates(7, 14)) || board.hasWall(new Coordinates(9, 14)))
					shiller(board);
				else
					move(board);
			}
		}
		else
		{
			if (num == 0)
			{
				Path path = board.findPath(1);
				if (path.getLength() < board.findPath(num).getLength())
				{
					if(!setAWall(board, path))
						move(board);
				}
				else
					move(board);
			}
			else if (num == 1)
			{
				Path path = board.findPath(0);
				if (path.getLength() < board.findPath(num).getLength())
				{
					if(!setAWall(board, path))
						move(board);
				}
				else
					move(board);
			}
		}
		numRounds++;
	}
	
	/** Does the shiller opening */
	private void shiller(Board board)
	{
		if (num == 0)
		{
			Random rand = new Random();
			boolean left = rand.nextBoolean();
			if(left)
				board.setWall(new Coordinates(7, 14));
			else
				board.setWall(new Coordinates(9, 14));
		}
		else if (num == 1)
		{
			Random rand = new Random();
			boolean left = rand.nextBoolean();
			if(left)
				board.setWall(new Coordinates(7, 0));
			else
				board.setWall(new Coordinates(9, 0));
		}
	}
	
	
	/** Follows the shortest path */
	private boolean move(Board board)
	{
		Path bestPath = board.findPath(num);
		return board.move(num, new Coordinates(bestPath.getX(1), bestPath.getY(1)));
	}
	
	/** Sets a wall to block the given path
	 * 
	 * @param board The board
	 * @param path The path
	 * @return True if a wall has been set, false otherwise
	 * 
	 * */
	private boolean setAWall(Board board, Path path)
	{
		System.out.println("Hello");
		for (int i = 0 ; i < path.getLength()-1 ; i++)
		{
			System.out.println(path.getX(i) + " " + path.getY(i) + " " + path.getX(i+1) + " " + path.getY(i+1));
			int dx = (path.getX(i+1)-path.getX(i));
			int dy = (path.getY(i+1)-path.getY(i));
			
			if (board.setWall(new Coordinates(path.getX(i)+dx/2, path.getY(i)+dy/2)))
				return true;
			else
			{
				// Vertical
				if (dx != 0)
				{
					if (board.setWall(new Coordinates(path.getX(i)+dx/2-2, path.getY(i))))
						return true;
				}
				// Horizontal
				if (dy != 0)
				{
					if (board.setWall(new Coordinates(path.getX(i), path.getY(i)+dy/2-2)))
						return true;
				}
			}
		}
		return false;
	}
}
