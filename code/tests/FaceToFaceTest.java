package tests;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.*;
import board.*;
import players.*;

public class FaceToFaceTest extends TestMain
{
	protected RectangularBoard board;
	protected APlayer[] players;
	
	@Before
	public void init()
	{		
		players = new APlayer[2];
		players[0] = new StrategyAI(0, 10, "0", new ShillerStrategy());
		players[1] = new StrategyAI(1, 10, "1", new ShillerStrategy());
		board = new RectangularBoard(players, 9, 9);
	}
	
	@After
	public void after()
	{
		doPrint("End of test\n");
	}
	
	@Test
	public void faceToFaceVertical()
	{
		doPrint("Starting face-to-face vertical test\n");
		for (int i = 0 ; i < 4 ; i++)
			board.move(0, new Coordinates(board.getCoordinates(0).getX(), board.getCoordinates(0).getY()-2));
		for (int i = 0 ; i < 3 ; i++)
			board.move(1, new Coordinates(board.getCoordinates(1).getX(), board.getCoordinates(1).getY()+2));

		board.update();
		doPrint("The board is : \n");
		printBoard(board);
		// Player 1
		ArrayList<Coordinates> coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(0, true)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 4)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 10)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 8)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 8)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 8 || j != 4) && (i != 8 || j != 10) && (i != 6 || j != 8) && (i != 10 || j != 8))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
		
		// Player 2
		coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(1, true)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 4)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 10)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 6)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 8 || j != 4) && (i != 8 || j != 10) && (i != 6 || j != 6) && (i != 10 || j != 6))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
	}
	
	@Test
	public void faceToFaceHorizontal()
	{
		doPrint("Starting face-to-face horizontal test\n");
		for (int i = 0 ; i < 4 ; i++)
			board.move(0, new Coordinates(board.getCoordinates(0).getX(), board.getCoordinates(0).getY()-2));
		board.move(0, new Coordinates(board.getCoordinates(0).getX()+2, board.getCoordinates(0).getY()));
		for (int i = 0 ; i < 4 ; i++)
			board.move(1, new Coordinates(board.getCoordinates(1).getX(), board.getCoordinates(1).getY()+2));
		
		board.update();
		doPrint("The board is : \n");
		printBoard(board);

		// Player 1
		ArrayList<Coordinates> coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(0, true)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 10)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 8)));
		Assert.assertTrue(coord.contains(new Coordinates(12, 8)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 10 || j != 6) && (i != 10 || j != 10) && (i != 6 || j != 8) && (i != 12 || j != 8))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
		
		// Player 2
		coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(1, true)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 10)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 8)));
		Assert.assertTrue(coord.contains(new Coordinates(12, 8)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 8 || j != 6) && (i != 8 || j != 10) && (i != 6 || j != 8) && (i != 12 || j != 8))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
	}
	
	@Test
	public void faceToFaceVerticalWithWalls()
	{
		doPrint("Starting face-to-face vertical with walls test\n");
		for (int i = 0 ; i < 4 ; i++)
			board.move(0, new Coordinates(board.getCoordinates(0).getX(), board.getCoordinates(0).getY()-2));
		for (int i = 0 ; i < 3 ; i++)
			board.move(1, new Coordinates(board.getCoordinates(1).getX(), board.getCoordinates(1).getY()+2));

		board.setWall(new Coordinates(8, 9));
		board.setWall(new Coordinates(8, 5));
		board.update();
		doPrint("The board is : \n");
		printBoard(board);

		// Player 1
		ArrayList<Coordinates> coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(0, true)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 8)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 8)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 6 || j != 6) && (i != 10 || j != 6) && (i != 6 || j != 8) && (i != 10 || j != 8))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
		
		// Player 2
		coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(1, true)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 8)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 8)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 6 || j != 6) && (i != 10 || j != 6) && (i != 6 || j != 8) && (i != 10 || j != 8))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
	}

	@Test
	public void faceToFaceHorizontalWithWalls()
	{
		doPrint("Starting face-to-face horizontal with walls test\n");
		for (int i = 0 ; i < 4 ; i++)
			board.move(0, new Coordinates(board.getCoordinates(0).getX(), board.getCoordinates(0).getY()-2));
		board.move(0, new Coordinates(board.getCoordinates(0).getX()+2, board.getCoordinates(0).getY()));
		for (int i = 0 ; i < 4 ; i++)
			board.move(1, new Coordinates(board.getCoordinates(1).getX(), board.getCoordinates(1).getY()+2));
		
		board.setWall(new Coordinates(7, 8));
		board.setWall(new Coordinates(11, 8));
		board.update();
		doPrint("The board is : \n");
		printBoard(board);

		// Player 1
		ArrayList<Coordinates> coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(0, true)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 10)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 10)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 8 || j != 6) && (i != 8 || j != 10) && (i != 10 || j != 6) && (i != 10 || j != 10))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
		
		// Player 2
		coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(1, true)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 10)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 10)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 8 || j != 6) && (i != 8 || j != 10) && (i != 10 || j != 6) && (i != 10 || j != 10))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
	}
	
	@Test
	public void faceToFaceVerticalWithWallsAndSide()
	{
		doPrint("Starting face-to-face vertical with walls and side test\n");
		for (int i = 0 ; i < 4 ; i++)
			board.move(0, new Coordinates(board.getCoordinates(0).getX(), board.getCoordinates(0).getY()-2));
		for (int i = 0 ; i < 3 ; i++)
			board.move(1, new Coordinates(board.getCoordinates(1).getX(), board.getCoordinates(1).getY()+2));

		board.setWall(new Coordinates(8, 9));
		board.setWall(new Coordinates(8, 5));
		board.setWall(new Coordinates(9, 6));
		board.update();
		doPrint("The board is : \n");
		printBoard(board);

		// Player 1
		ArrayList<Coordinates> coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(0, true)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 8)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 6 || j != 6) && (i != 6 || j != 8))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
		
		// Player 2
		coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(1, true)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 6)));
		Assert.assertTrue(coord.contains(new Coordinates(6, 8)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 6 || j != 6) && (i != 6 || j != 8))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
	}
	
	@Test
	public void faceToFaceHorizontalWithWallsAndSide()
	{
		doPrint("Starting face-to-face horizontal with walls and side test\n");
		for (int i = 0 ; i < 4 ; i++)
			board.move(0, new Coordinates(board.getCoordinates(0).getX(), board.getCoordinates(0).getY()-2));
		board.move(0, new Coordinates(board.getCoordinates(0).getX()+2, board.getCoordinates(0).getY()));
		for (int i = 0 ; i < 4 ; i++)
			board.move(1, new Coordinates(board.getCoordinates(1).getX(), board.getCoordinates(1).getY()+2));
		
		board.setWall(new Coordinates(7, 8));
		board.setWall(new Coordinates(11, 8));
		board.setWall(new Coordinates(8, 7));
		board.update();
		doPrint("The board is : \n");
		printBoard(board);

		// Player 1
		ArrayList<Coordinates> coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(0, true)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 10)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 10)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 8 || j != 10) && (i != 10 || j != 10))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
		
		// Player 2
		coord = new ArrayList<Coordinates>(Arrays.asList(board.possibleMoves(1, true)));
		Assert.assertTrue(coord.contains(new Coordinates(8, 10)));
		Assert.assertTrue(coord.contains(new Coordinates(10, 10)));
		for (int i = 0 ; i < 16 ; i++)
		{
			for (int j = 0 ; j < 16 ; j++)
			{
				if ((i != 8 || j != 10) && (i != 10 || j != 10))
				{
					Assert.assertFalse(coord.contains(new Coordinates(i, j)));
				}
			}
		}
	}
}
