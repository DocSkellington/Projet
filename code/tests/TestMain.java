package tests;

import board.RectangularBoard;

public abstract class TestMain
{
	public static boolean print = Boolean.parseBoolean(System.getenv("PRINT"));;
	
	protected void printBoard(RectangularBoard board)
	{
		if (print)
			System.err.println(board.toString());
	}
	
	protected void doPrint(String message)
	{
		if (print)
			System.err.print(message);
	}
}
