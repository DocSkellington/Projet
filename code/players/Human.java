package players;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;
import board.*;
import board.Board.*;
import pathFinder.*;


/** The human player
 * 
 * @author Thibaut De Cooman & Gaetan Staquet
 *
 */
public final class Human extends APlayer
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
		System.out.println("Player " + (num+1) + ", you can play.");
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
	
	private void setAWall()
	{
		
	}
	
	/** Asks the player to move. */
	private void move()
	{
		Scanner scan = new Scanner(System.in);
		
		Coordinates[] possibleMoves = possibleMoves().toArray(new Coordinates[0]);
		for(int i = 0 ; i < possibleMoves.length ; i++)
		{
			System.out.println("(" + (i+1) + ") " + possibleMoves[i]);
		}
		
		int choice = 0;
		
		while(true)
		{
			while (!scan.hasNextInt())
			{
				System.out.println("Please enter an integer.");
				scan.next();
			}
		
			choice = scan.nextInt() - 1;
			
			if(choice >= 0 && choice < possibleMoves.length)
			{
				board.move(num, possibleMoves[choice]);
				break;
			}
			System.out.println("The integer must be between 1 and " + possibleMoves.length + ".");
		}
		this.coord = board.getCoordinates(num);
	}
}
