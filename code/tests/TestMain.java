package tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import board.Board;

public abstract class TestMain
{
	public static boolean print;
	
	protected void printBoard(Board board)
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
