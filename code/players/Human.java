package players;

import java.util.Scanner;
import board.*;


/** The human player
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */
public final class Human extends APlayer
{
	/** Constructor
	 * 
	 * @param num The player's number
	 * @param wallsCounter The counter of available walls
	 */
	public Human(int num, int wallsCounter)
	{
		super(num, wallsCounter);
	}
	
	@Override
	public void play(Board board) {
		System.out.println("Player " + (num+1) + ", you can play. You have " + wallsCounter + " wall(s) left.");
		boolean wantsToMove = wannaMove();
		if (wantsToMove)
			move(board);
		else
			setAWall(board);
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
	
	/** Asks the player the coordinates to set the wall
	 * 
	 * @param board A reference to the board
	 */
	private void setAWall(Board board)
	{
		Scanner scan = new Scanner(System.in);
		while (true)
		{
			System.out.println("Enter the x position of the (upper-left corner of the) wall");
			int xw, yw;
			
			while (!scan.hasNextInt())
			{
				System.out.println("Please enter an integer.");
				scan.next();
			}
			
			xw = scan.nextInt();
			System.out.println("Enter the y position of the (upper-left corner of the) wall");
			
			while (!scan.hasNextInt())
			{
				System.out.println("Please enter an integer.");
				scan.next();
			}
			
			yw = scan.nextInt();
			
			if (board.setWall(new Coordinates(xw, yw)))
			{
				wallsCounter--;
				break;
			}
			
			System.out.println("Impossible to place the wall at the given position. Pl0x enter another position.");
		}
		
	}
	
	/** Asks the player to move.
	 * 
	 * @param board A reference to the board
	 */
	private void move(Board board)
	{
		Coordinates coord = board.getCoordinates(num);
		Scanner scan = new Scanner(System.in);
		System.out.println("You're at " + coord);
		
		Coordinates[] possibleMoves = possibleMoves(board, true).toArray(new Coordinates[0]);
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
	}
}
