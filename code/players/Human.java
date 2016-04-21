package players;

import board.*;
import players.Round.Type;


/** The human player
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */
public final class Human extends APlayer
{	
	private boolean activated;
	private Round round;
	
	/** Constructor
	 * 
	 * @param num The player's number
	 * @param wallCounter The counter of available walls
	 */
	public Human(int num, int wallCounter)
	{
		super(num, wallCounter);
	}
	
	/** Whether the human is active
	 * 
	 * @return True if the player is active, false otherwise
	 */
	public boolean isActive()
	{
		return activated;
	}

	/** Activate the possible positions
	 * 
	 * @param board A reference to the board
	 */
	public void move(Board board)
	{
		board.disableAll();
		Coordinates[] coord = possibleMoves(board, true).toArray(new Coordinates[0]);
		board.setEnabledButtons(true, coord);
	}
	
	/** Activate the possible walls
	 * 
	 * @param board A reference to the board
	 */
	public void walls(Board board)
	{
		if (wallCounter > 0)
		{
			board.disableAll();
			board.enableWalls(false);
		}
	}
	
	/** Activate the walls who can be destroyed
	 * 
	 * @param board The board
	 */
	public void removeWalls(Board board)
	{
		board.disableAll();
		board.enableWalls(true);
	}
	
	/** Effectively moves
	 * 
	 * @param board A reference to the board
	 * @param coord The coordinates to reach
	 */
	public void doMove(Board board, Coordinates coord)
	{
		board.move(num, coord);
		activated = false;
		round = new Round(Type.MOVE, coord);
	}
	
	/** Effectively sets a wall
	 * 
	 * @param board The board
	 * @param coord The upper-left coordinates of the wall to destroy
	 */
	public void setWall(Board board, Coordinates coord)
	{
		if(board.setWall(coord))
		{
			activated = false;
			round = new Round(Type.WALL, coord);
			wallCounter--;
		}
	}

	/** Removes a wall
	 * 
	 * @param board The board
	 * @param coord The upper left coordinates of the wall to destroy
	 */
	public void destroyWall(Board board, Coordinates coord)
	{
		// TODO: Make the player wait 1 turn
		if(board.destroyWall(coord))
		{
			activated = false;
			round = new Round(Type.DEST, coord);
			waitingTurns++;
		}
	}
	
	@Override
	public Round skip()
	{
		activated = false;
		return super.skip();
	}
	
	@Override
	protected Round doPlay(Board board)
	{
		activated = true;
		while (activated)
		{
        	// Stop to play if the thread is interrupted
			if(Thread.currentThread().isInterrupted())
			{
				return new Round(Type.NONE, new Coordinates(-1, -1));
			}
			
			board.repaint();
		}
		
		board.disableAll();
		
		// We wait to be sure that round is updated
		try
		{
			Thread.sleep(50);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} 
		return round;
	}
	
}
