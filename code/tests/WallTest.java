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

public class WallTest
{
	protected Board board;
	protected APlayer[] players;
	
	@Before
	public void init()
	{
		players = new APlayer[2];
		players[0] = new StrategyAI(0, 10, new ShillerStrategy());
		players[1] = new StrategyAI(1, 10, new ShillerStrategy());
		board = new Board(players, 9, null);
	}
	
	@After
	public void after()
	{
		System.out.println("Board after");
		board.print();
		System.out.println("End of test");
	}
	
	@Test
	public void smokeTest()
	{
		System.out.println("Starting smoke test");

		System.out.println("Board before : ");
		board.print();
		
		Assert.assertTrue(board.setWall(new Coordinates(7, 8)));
		
	}
	
	@Test
	public void wrongWalls()
	{
		System.out.println("Starting the wrong positions test");

		System.out.println("Board before : ");
		board.print();
		
		for (int i = 0 ; i <= 16 ; i += 2)
		{
			for (int j = 0 ; j <= 16 ; j += 2)
			{
				Assert.assertFalse(board.setWall(new Coordinates(i, j)));
			}
		}
		
		for (int i = 1 ; i <= 15 ; i += 2)
		{
			for (int j = 1 ; j <= 15 ; j += 2)
			{
				Assert.assertFalse(board.setWall(new Coordinates(i, j)));
			}
		}
		
		Assert.assertFalse(board.setWall(new Coordinates(-2, 0)));
		Assert.assertFalse(board.setWall(new Coordinates(0, -2)));
		
		System.out.println("The board is :");
		board.print();
		
		board.setWall(new Coordinates(1, 0));
		board.setWall(new Coordinates(8, 7));
		board.setWall(new Coordinates(8, 5));
		
		System.out.println("Checking if we can't set a wall over another one, we're now using the board :");
		board.print();
		
		Assert.assertFalse(board.setWall(new Coordinates(0, 1)));
		Assert.assertFalse(board.setWall(new Coordinates(9, 6)));
	}
	
	@Test
	public void blockPath()
	{
		System.out.println("Starting the block path test");

		System.out.println("Board before : ");
		board.print();
		
		for (int i = 0 ; i <= 12 ; i += 4)
			Assert.assertTrue(board.setWall(new Coordinates(i, 1)));
		
		Assert.assertTrue(board.setWall(new Coordinates(13, 2)));
		System.out.println("Trying to block 2 with a wall at (14, 5)");
		Assert.assertFalse(board.setWall(new Coordinates(14, 5)));
		
		Assert.assertTrue(board.setWall(new Coordinates(0, 15)));
		Assert.assertTrue(board.setWall(new Coordinates(4, 15)));
		Assert.assertTrue(board.setWall(new Coordinates(10, 15)));
		Assert.assertTrue(board.setWall(new Coordinates(14, 15)));
		Assert.assertTrue(board.setWall(new Coordinates(7, 12)));
		Assert.assertTrue(board.setWall(new Coordinates(9, 12)));

		System.out.println("Trying to block 1 with a wall at (6, 11)");
		Assert.assertFalse(board.setWall(new Coordinates(6, 11)));
		System.out.println("Trying to block 1 with a wall at (8, 11)");
		Assert.assertFalse(board.setWall(new Coordinates(8, 11)));
	}
	
	@Test
	public void shiftTest()
	{
		System.out.println("Starting shift test");
		
		System.out.println("Board before : ");
		board.print();
		
		board.setWall(new Coordinates(8, 7));
		Assert.assertTrue(board.setWall(new Coordinates(6, 7)));
		Assert.assertTrue(board.setWall(new Coordinates(9, 6)));
	}
	
	@Test
	public void shiftTestRightEdge()
	{
		for (int i = 1 ; i < board.getYSize() ; i += 2)
		{
			Assert.assertTrue(board.setWall(new Coordinates(16, i)));
		}
	}

	@Test
	public void shiftTestBottomEdge()
	{
		for (int j = 1 ; j < board.getXSize() ; j += 2)
		{
			Assert.assertTrue(board.setWall(new Coordinates(j, 16)));
		}
	}
}
