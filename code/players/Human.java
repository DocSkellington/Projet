package players;

import java.util.Scanner;
import board.*;
import pathFinder.*;

public class Human extends APlayer {

	public Human(Board board, int num)
	{
		super(board, num);
	}
	
	@Override
	public void play() {
		// TODO Auto-generated method stub
		boolean move = wannaMove();

	}

	private boolean wannaMove()
	{
		Scanner scan = new Scanner(System.in);
		String res = "";
		
		System.out.println("Do you want to move ? (Y/N)");
		
		do
		{
			res = scan.nextLine();
		
			if (res.equals("Y"))
				return true;
			else if (res.equals("N"))
				return false;
			System.out.println("Y or N");
		} while(true);
	}
	
}
