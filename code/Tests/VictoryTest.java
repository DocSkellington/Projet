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

public class VictoryTest
{
	protected Board board;
	protected APlayer[] players;
	
	@Before
	public void init()
	{
		players = new APlayer[2];
		players[0] = new StrategyAI(0, 10, new ShillerStrategy());
		players[1] = new StrategyAI(1, 10, new ShillerStrategy());
		board = new Board(players, null);
		System.out.println("The board is : ");
		board.print();
	}
	
	@After
	public void after()
	{
		System.out.println("End of test");
	}
	
	@Test
	public void victory1()
	{
		System.out.println("Starting victory 1 test");
		
		System.out.println("There isn't any winner");
		Assert.assertTrue(board.hasWon() == -1);
		
		board.move(0, new Coordinates(10, 16));
		for (int i = 14 ; i >= 0 ; i -= 2)
			board.move(0, new Coordinates(10, i));
		
		board.update();
		
		System.out.println("The player 1 has moved:");
		board.print();
		
		Assert.assertTrue(board.hasWon() == 0);
		System.out.println("Player 1 has won");
	}
	
	@Test
	public void victory2()
	{
		System.out.println("Starting victory 2 test");
		
		System.out.println("There isn't any winner");
		Assert.assertTrue(board.hasWon() == -1);
		
		board.move(1, new Coordinates(10, 0));
		for (int i = 2 ; i <= 16 ; i += 2)
			board.move(1, new Coordinates(10, i));
		
		board.update();
		
		System.out.println("The player 2 has moved:");
		board.print();
		
		Assert.assertTrue(board.hasWon() == 1);
		System.out.println("Player 2 has won");
	}
}
