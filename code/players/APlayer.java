package players;

import board.*;
import java.util.HashSet;

/** Abstract class to handle a player.
 * This can't be used because play() must have a specific behaviour depending on the type of the player (Human/AI).
 * 
 * @author Gaetan Staquet & Thibaut De Cooman
 *
 */
public abstract class APlayer
{
	protected Board board;
	protected int num, wallsCounter;
	protected Coordinates coord;
	
	/** The default constructor */
	public APlayer()
	{
		wallsCounter = 10;
	}
	
	/** The constructor that must be used
	 * 
	 * @param board A reference to the board
	 * @param num The number of the Player
	 */
	public APlayer(Board board, int num, int wallsCounter)
	{
		this.board = board;
		this.num = num;
		this.wallsCounter = wallsCounter;
		this.coord = board.getCoordinates(num);
	}
	
	/** This function handles the turn of a player.*/
	public abstract void play();
	
	/** Gets the number of the Player
	 * 
	 * @return The number of the Player
	 */
	public int getNum()
	{
		return num;
	}
	
	/** Gets the coordinates of this player */
	public Coordinates getCoordinates()
	{
		return coord.clone();
	}
	
	/** Gives the possible moves of this player for their current position */
	public HashSet<Coordinates> possibleMoves()
	{
		return possibleMoves(coord);
	}
	
	/** Gives the possible moves of this player for a precise position
	 * 
	 * @param pos The position we look at
	 * @return The (relative) coordinates of the accessible cases
	 */
	public HashSet<Coordinates> possibleMoves(Coordinates pos)
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
				if(board.filled(pos.getX()+i, pos.getY()) != 0 && i != 0)
				{
					// Can we jump over it ?
					int x = pos.getX() + i, y = pos.getY();
					if(!board.blocked(x, y, x + i, y))
					{
						// Yes
						coord.add(new Coordinates(x+i, y));
					}
					else
					{
						// No
						// Up
						if(!board.blocked(x, y, x, y-2))
						{
							coord.add(new Coordinates(x, y-2));
						}
						// Down
						if(!board.blocked(x, y, x, y+2))
						{
							coord.add(new Coordinates(x, y+2));
						}
					}
				}
				else if (pos.getX() + i != pos.getX())
				{
					if(!board.blocked(pos.getX(), pos.getY(), pos.getX()+i, pos.getY()))
						coord.add(new Coordinates(pos.getX() + i, pos.getY()));
				}
				// y coordinate
				if(board.filled(pos.getX(), pos.getY()+j) != 0 && j != 0)
				{
					// Can we jump over it ?
					int x = pos.getX(), y = pos.getY() + j;
					if(!board.blocked(x, y, x, y + j))
					{
						// Yes
						coord.add(new Coordinates(x, y + j));
					}
					else
					{
						// No
						// Left
						if(!board.blocked(x, y, x - 2, y))
						{
							coord.add(new Coordinates(x - 2, y));
						}
						// Right
						if(!board.blocked(x, y, x + 2, y))
						{
							coord.add(new Coordinates(x + 2, y));
						}
					}
				}
				else if (pos.getY() + j != pos.getY())
				{
					if (!board.blocked(pos.getX(), pos.getY(), pos.getX(), pos.getY() + j))
						coord.add(new Coordinates(pos.getX(), pos.getY() + j));
				}
			}
		}
		
		return coord;
	}
}
