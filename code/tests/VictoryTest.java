package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import board.Board;
import board.Coordinates;
import players.APlayer;
import players.ShillerStrategy;
import players.StrategyAI;

public class VictoryTest extends TestMain
{
	protected Board board;
	protected APlayer[] players;
	
	@Before
	public void init()
	{
		players = new APlayer[2];
		players[0] = new StrategyAI(0, 10, new ShillerStrategy());
		players[1] = new StrategyAI(1, 10, new ShillerStrategy());
		board = new Board(players, 9, 9);
		doPrint("The board is : \n");
		printBoard(board);
	}
	
	@After
	public void after()
	{
		doPrint("End of test\n");
	}
	
	@Test
	public void victory1()
	{
		doPrint("Starting victory 1 test\n");
		
		doPrint("There isn't any winner\n");
		Assert.assertTrue(board.hasWon() == -1);
		
		board.move(0, new Coordinates(10, 16));
		for (int i = 14 ; i >= 0 ; i -= 2)
			board.move(0, new Coordinates(10, i));
		
		board.update();
		
		doPrint("The player 1 has moved:\n");
		printBoard(board);
		
		Assert.assertTrue(board.hasWon() == 0);
		doPrint("Player 1 has won\n");
	}
	
	@Test
	public void victory2()
	{
		doPrint("Starting victory 2 test\n");
		
		doPrint("There isn't any winner\n");
		Assert.assertTrue(board.hasWon() == -1);
		
		board.move(1, new Coordinates(10, 0));
		for (int i = 2 ; i <= 16 ; i += 2)
			board.move(1, new Coordinates(10, i));
		
		board.update();
		
		doPrint("The player 2 has moved:\n");
		printBoard(board);
		
		Assert.assertTrue(board.hasWon() == 1);
		doPrint("Player 2 has won\n");
	}
}
