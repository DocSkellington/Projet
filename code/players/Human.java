package players;

import java.util.Scanner;
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
	
	private void move()
	{
		Scanner scan = new Scanner(System.in);
		boolean possible = false;
		
		System.out.println("Up, Down, Right, Left ? (U/D/R/L)");
		
		while(!possible)
		{
			String res = scan.nextLine();
			
			// TODO : Le cas où il faut sauter par-dessus un joueur
			
			if (res.equals("U"))
			{
				possible = board.move(num, new Coordinates(coord.getX(), coord.getY()-2));
			}
			else if (res.equals("D"))
			{
				possible = board.move(num, new Coordinates(coord.getX(), coord.getY()+2));
			}
			else if (res.equals("L"))
			{
				possible = board.move(num, new Coordinates(coord.getX()-2, coord.getY()));
			}
			else if (res.equals("R"))
			{
				possible = board.move(num, new Coordinates(coord.getX()+2, coord.getY()));
			}
			else
			{
				System.out.println("U/D/L/R");
				continue;
			}
			
			if (!possible)
				System.out.println("You can't move there!");
		}
		this.coord = board.getCoordinates(num);
	}
}
