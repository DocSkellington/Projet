package players;

import board.*;
import pathFinder.*;
import board.Board.Coordinates;

/** Abstract class to handle a player.
 * This can't be used because play() must have a specific behavior depending on the type of the player (Human/AI).
 * 
 * @author Gaetan Staquet & Thibaut De Cooman
 *
 */
public abstract class APlayer
{
	Board board;
	int num;
	Coordinates coord;
	
	/** The default constructor */
	public APlayer()
	{
		
	}
	
	/** The constructor that must be used
	 * 
	 * @param board A reference to the board
	 * @param num The number of the Player
	 */
	public APlayer(Board board, int num)
	{
		this.board = board;
		this.num = num;
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
	
	
}
