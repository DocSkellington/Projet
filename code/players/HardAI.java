package players;

import pathFinder.*;
import board.*;

public class HardAI extends APlayer
{
	public HardAI(Board board, int num, int wallsCounter)
	{
		super(board, num, wallsCounter);
	}
	
	
	/** Decides whether the HardAI moves or sets a wall */
	public void play()
	{

	}

	public void move()
	{
		Path bestPath = board.findPath(this);
		board.move(num, new Coordinates(bestPath.getX(0), bestPath.getY(0)));
	}
	
	public void setAWall()
	{
		
	}
}
