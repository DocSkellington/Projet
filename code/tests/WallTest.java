package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import board.RectangularBoard;
import board.Coordinates;
import players.APlayer;
import players.ShillerStrategy;
import players.StrategyAI;

public class WallTest extends TestMain
{
	protected RectangularBoard board;
	protected APlayer[] players;
	
	@Before
	public void init()
	{
		players = new APlayer[2];
		players[0] = new StrategyAI(0, 10, "0", new ShillerStrategy());
		players[1] = new StrategyAI(1, 10, "0", new ShillerStrategy());
		board = new RectangularBoard(players, 9, 9);
	}
	
	@After
	public void after()
	{
		doPrint("Board after\n");
		printBoard(board);
		doPrint("End of test\n");
	}
	
	@Test
	public void smokeTest()
	{
		doPrint("Starting smoke test\n");

		doPrint("Board before : \n");
		printBoard(board);
		
		Assert.assertTrue(board.setWall(new Coordinates(7, 8)));
		
	}
	
	@Test
	public void wrongWalls()
	{
		doPrint("Starting the wrong positions test\n");

		doPrint("Board before : \n");
		printBoard(board);
		
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
		
		doPrint("The board is :\n");
		printBoard(board);
		
		board.setWall(new Coordinates(1, 0));
		board.setWall(new Coordinates(8, 7));
		board.setWall(new Coordinates(8, 5));
		
		doPrint("Checking if we can't set a wall over another one, we're now using the board :\n");
		printBoard(board);
		
		Assert.assertFalse(board.setWall(new Coordinates(0, 1)));
		Assert.assertFalse(board.setWall(new Coordinates(9, 6)));
	}
	
	@Test
	public void blockPath()
	{
		doPrint("Starting the block path test\n");

		doPrint("Board before : \n");
		printBoard(board);
		
		for (int i = 0 ; i <= 12 ; i += 4)
			Assert.assertTrue(board.setWall(new Coordinates(i, 1)));
		
		Assert.assertTrue(board.setWall(new Coordinates(13, 2)));
		doPrint("Trying to block 2 with a wall at (14, 5)\n");
		Assert.assertFalse(board.setWall(new Coordinates(14, 5)));
		
		Assert.assertTrue(board.setWall(new Coordinates(0, 15)));
		Assert.assertTrue(board.setWall(new Coordinates(4, 15)));
		Assert.assertTrue(board.setWall(new Coordinates(10, 15)));
		Assert.assertTrue(board.setWall(new Coordinates(14, 15)));
		Assert.assertTrue(board.setWall(new Coordinates(7, 12)));
		Assert.assertTrue(board.setWall(new Coordinates(9, 12)));

		doPrint("Trying to block 1 with a wall at (6, 11)\n");
		Assert.assertFalse(board.setWall(new Coordinates(6, 11)));
		doPrint("Trying to block 1 with a wall at (8, 11)\n");
		Assert.assertFalse(board.setWall(new Coordinates(8, 11)));
	}
	
	@Test
	public void shiftTest()
	{
		doPrint("Starting shift test\n");
		
		doPrint("Board before : \n");
		printBoard(board);
		
		board.setWall(new Coordinates(8, 7));
		Assert.assertTrue(board.setWall(new Coordinates(6, 7)));
		Assert.assertTrue(board.setWall(new Coordinates(9, 6)));
	}
	
	@Test
	public void shiftTestRightEdge()
	{
		for (int i = 1 ; i < board.getYSize(0) ; i += 2)
		{
			Assert.assertTrue(board.setWall(new Coordinates(16, i)));
		}
	}

	@Test
	public void shiftTestBottomEdge()
	{
		for (int j = 1 ; j < board.getXSize(0) ; j += 2)
		{
			Assert.assertTrue(board.setWall(new Coordinates(j, 16)));
		}
	}
}
