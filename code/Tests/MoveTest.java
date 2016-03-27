package tests;

import java.util.HashSet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import board.Board;
import board.Coordinates;
import players.APlayer;
import players.ShillerStrategy;
import players.StrategyAI;

public class MoveTest
{
	protected Board board;
	protected APlayer[] players;
	
	@Before
	public void init()
	{
		players = new APlayer[4];
		players[0] = new StrategyAI(0, 10, new ShillerStrategy());
		players[1] = new StrategyAI(1, 10, new ShillerStrategy());
		players[2] = new StrategyAI(2, 10, new ShillerStrategy());
		players[3] = new StrategyAI(3, 10, new ShillerStrategy());
		board = new Board(players);
	}
	
	@After
	public void after()
	{
		System.out.println("End of test");
	}
	
	@Test
	public void smokeTest()
	{
		System.out.println("Starting smoke test");
		for (int i = 0 ; i < 4 ; i++)
			board.move(0, new Coordinates(board.getCoordinates(0).getX(), board.getCoordinates(0).getY()-2));
		
		board.update();
		System.out.println("The board is : ");
		board.print();
		
		HashSet<Coordinates> coord = players[0].possibleMoves(board, true);
		Assert.assertTrue(coord.contains(new Coordinates(8, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 10)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 8)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 8)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 8 || j != 6) && (i != 8 || j != 10) && (i != 6 || j != 8) && (i != 10 || j != 8))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
	}
	
	@Test
	public void edges()
	{
		System.out.println("Starting edges test");

		board.update();
		System.out.println("The board is : ");
		board.print();

		// Player 1
		HashSet<Coordinates> coord = players[0].possibleMoves(board, true);
		Assert.assertTrue(coord.contains(new Coordinates(8, 14)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 16)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 16)));
		Assert.assertFalse(coord.contains(new Coordinates(8, 18)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 8 || j != 14) && (i != 6 || j != 16) && (i != 10 || j != 16))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
		
		// Player 2
		coord = players[1].possibleMoves(board, true);
		Assert.assertTrue(coord.contains(new Coordinates(6, 0)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 0)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 2)));
		Assert.assertFalse(coord.contains(new Coordinates(8, -2)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 6 || j != 0) && (i != 10 || j != 0) && (i != 8 || j != 2))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
		
		// Player 3
		coord = players[2].possibleMoves(board, true);
		Assert.assertTrue(coord.contains(new Coordinates(0, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(0, 10)));
		Assert.assertTrue(coord.contains(new Coordinates(2, 8)));
		Assert.assertFalse(coord.contains(new Coordinates(-2, 8)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 0 || j != 6) && (i != 0 || j != 10) && (i != 2 || j != 8))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
		
		// Player 4
		coord = players[3].possibleMoves(board, true);
		Assert.assertTrue(coord.contains(new Coordinates(14, 8)));
		Assert.assertTrue(coord.contains(new Coordinates(16, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(16, 10)));
		Assert.assertFalse(coord.contains(new Coordinates(18, 8)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 14 || j != 8) && (i != 16 || j != 6) && (i != 16 || j != 10))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
	}
	
	@Test
	public void moveThroughWalls()
	{
		System.out.println("Starting move through walls test");

		board.setWall(new Coordinates(8, 1));
		board.setWall(new Coordinates(7, 14));
		board.update();
		System.out.println("The board is : ");
		board.print();

		// Player 1
		HashSet<Coordinates> coord = players[0].possibleMoves(board, true);
		Assert.assertTrue(coord.contains(new Coordinates(8, 14)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 16)));
		Assert.assertFalse(coord.contains(new Coordinates(6, 16)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 8 || j != 14) && (i != 6 || j != 16) && (i != 10 || j != 16))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
		
		// Player 2
		coord = players[1].possibleMoves(board, true);
		Assert.assertTrue(coord.contains(new Coordinates(6, 0)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 0)));
		Assert.assertFalse(coord.contains(new Coordinates(8, 2)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 6 || j != 0) && (i != 10 || j != 0) && (i != 8 || j != 2))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
	}
}
