package players;

import java.util.Scanner;
import board.*;


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
	public Human(Board board, int num, int wallsCounter)
	{
		super(board, num, wallsCounter);
	}
	
	@Override
	public void play() {
		System.out.println("Player " + (num+1) + ", you can play. You have " + wallsCounter + " wall(s) left.");
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
		Scanner scan = new Scanner(System.in);
		while (true)
		{
			System.out.println("Enter the x position of the (upper-left corner of the) wall");
			int xw, yw;
			boolean horizontal = false;
			
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
			System.out.println("Do you want the wall to be horizontal? (true/false)");
			
			while (!scan.hasNextBoolean())
			{
				System.out.println("Please enter 'true' or 'false'.");
				scan.next();
			}
			
			horizontal = scan.nextBoolean();
			if (board.setWall(num, new Coordinates(xw, yw), horizontal))
			{
				wallsCounter--;
				break;
			}
			
			System.out.println("Impossible to place the wall at the given position. Pl0x enter another position.");
		}
		
	}
	
	/** Asks the player to move. */
	private void move()
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("You're at " + coord);
		
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
