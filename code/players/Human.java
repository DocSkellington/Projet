package players;

import java.util.Scanner;
import board.*;
import pathFinder.*;

/** The human player
 * 
 * @author Thibaut De Cooman & Gaetan Staquet
 *
 */
public class Human extends APlayer
{
	/** Constructor
	 * 
	 * @param board A reference to the board
	 * @param num The player's number
	 */
	public Human(Board board, int num)
	{
		super(board, num);
	}
	
	@Override
	public void play() {
		// TODO Auto-generated method stub
		boolean wantsToMove = wannaMove();
		if (wantsToMove)
			move();
		else
			setAWall();
	}

	/** Asks the player whether they want to move or not
	 * 
	 * @return Returns whether the player wants to move or not
	 */
	private boolean wannaMove()
	{
		Scanner scan = new Scanner(System.in);
		String res = "";
		
		System.out.println("Do you want to move or to set a wall ? (M/W)");
		
		do
		{
			res = scan.nextLine();
		
			if (res.equals("M"))
				return true;
			else if (res.equals("W"))
				return false;
			System.out.println("M to move or W to set a wall");
		} while(true);
	}
	
	private void move()
	{
		Scanner scan = new Scanner(System.in);
	}
}
