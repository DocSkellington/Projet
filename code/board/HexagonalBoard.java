package board;

import java.util.ArrayList;

import javax.swing.JPanel;

import players.APlayer;
import players.Human;

/** Manages all information about the hexagonal board.
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class HexagonalBoard extends ABoard
{
	public HexagonalBoard(APlayer[] players)
	{
		super(players);
		
		// TODO : REMOVE!
		this.players = new APlayer[6];
		for (int i = 0 ; i < 6 ; i++)
		{
			this.players[i] = new Human(i, 10, "Henry " + i);
		}
		
		// 5 de côté
		for (int i = 0 ; i <= 32 ; i++)
		{
			ArrayList<ACell> row = new ArrayList<ACell>();
			// Rows starting by a wall (and with cases)
			if (i % 4 == 2 && (i >= 8 && i <=24))
			{
				for (int j = 0 ; j < 17 ; j++)
				{
					if (j % 4 == 3)
						row.add(new HexagonalCase());
					else
						row.add(new HexagonalWall());
				}
			}
			// Cases and walls
			else if (i % 2 == 0)
			{
				int columnSize = 17;
				if (0 <= i && i <= 6)
					columnSize = 2*i + 1;	
				else if (26 <= i && i <= 32)
					columnSize = 2*(33 - i)-1;
				for (int j = 0 ; j < columnSize ; j++)
				{
					if (j % 4 == 0)
						row.add(new HexagonalCase());
					else
						row.add(new HexagonalWall());
				}
			}
			// Just walls
			else
			{
				int columnSize = 8;
				if (1 <= i && i < 9)
					columnSize = i + 1;
				else if (25 <= i && i <= 31)
					columnSize = 32 - i + 1;
				for (int j = 0 ; j < columnSize ; j++)
					row.add(new HexagonalWall());
			}
			cells.add(row);
		}

        playersPositions = new Coordinates[this.players.length];
        for (int i = 0 ; i < this.players.length ; i++)
        {
        	playersPositions[i] = startingPos(i);
        	/*System.out.println(playersPositions[i]);
        	Coordinates[] goals = goal(i);
        	for (int j = 0 ; j < goals.length ; j++)
        		System.out.println(goals[j]);
        	System.out.println("\n");*/
        }
        
        update();
		
		//print();
	}

	@Override
	public String toString()
	{
		String res = "";
		for (ArrayList<ACell> row : cells)
		{
			for (ACell cell : row)
				res += cell.toString();
			res += "\n";
		}
		return res;
	}

	@Override
	public void fill(JPanel panel)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Coordinates getSize()
	{
		return new Coordinates(5, 5);
	}

	@Override
	public Coordinates startingPos(int playerNum)
	{
		if (playerNum == 0)
			return new Coordinates(0, 28);
		else if (playerNum == 1)
			return new Coordinates(8, 4);
		else if (playerNum == 2)
			return new Coordinates(0, 16);
		else if (playerNum == 3)
			return new Coordinates(16, 16);
		else if (playerNum == 4)
			return new Coordinates(0, 4);
		else
			return new Coordinates(8, 28);
	}

	@Override
	public Coordinates[] goal(int playerNum)
	{
		if (playerNum == 0)
		{
			Coordinates[] coords = new Coordinates[5];
			for (int i = 0 ; i < 5 ; i++)
			{
				coords[i] = new Coordinates(this.getXSize(2*i)-1, 2*i);
			}
			return coords;
		}
		else if (playerNum == 1)
		{
			Coordinates[] coords = new Coordinates[5];
			for (int i = 0 ; i < 5 ; i++)
			{
				coords[i] = new Coordinates(0, 24 + 2*i);
			}
			return coords;
		}
		else if (playerNum == 2)
		{
			Coordinates[] coords = new Coordinates[5];
			for (int i = 0 ; i < 5 ; i++)
			{
				coords[i] = new Coordinates(this.getXSize(8 + 4*i)-1, 8 + 4*i);
			}
			return coords;
		}
		else if (playerNum == 3)
		{
			Coordinates[] coords = new Coordinates[5];
			for (int i = 0 ; i < 5 ; i++)
			{
				coords[i] = new Coordinates(0, 8 + 4*i);
			}
			return coords;
		}
		else if (playerNum == 4)
		{
			Coordinates[] coords = new Coordinates[5];
			for (int i = 0 ; i < 5 ; i++)
			{
				coords[i] = new Coordinates(this.getXSize(24 + 2*i)-1, 24 + 2*i);
			}
			return coords;
		}
		else
		{
			Coordinates[] coords = new Coordinates[5];
			for (int i = 0 ; i < 5 ; i++)
			{
				coords[i] = new Coordinates(0, 2*i);
			}
			return coords;
		}
	}

	@Override
	public void colorAdjacentWalls(Coordinates coord, boolean coloured)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void colorAdjacentWallsDestroy(Coordinates coord, boolean coloured)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void fillWall(Coordinates coord, int fill)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean tryWall(Coordinates coord)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Coordinates shift(Coordinates coord)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Coordinates removeShift(Coordinates coord)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
