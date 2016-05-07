package board;

import java.util.ArrayList;

import javax.swing.JPanel;

import players.APlayer;

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
		// 5 de côté
		/* IDEAS:
		 * Un rectangle amputé de 4 triangles ? -> Permettrai de tout gérer assez facilement (plus facile qu'avec une vraie gestion des hexa)
		*/
		for (int i = 0 ; i <= 32 ; i++)
		{
			ArrayList<ACell> row = new ArrayList<ACell>();
			// Rows starting by a wall (and with cases)
			if (i % 4 == 2 && (i >= 8 && i <=24))
			{
				for (int j = 0 ; j < 9 ; j++)
				{
					if (j % 2 == 0)
						row.add(new HexagonalWall());
					else
						row.add(new HexagonalCase());
				}
			}
			// Cases and walls
			else if (i % 2 == 0)
			{
				int columnSize = 9;
				if (0 <= i && i <= 6)
					columnSize = i + 1;
				else if (26 <= i && i <= 32)
					columnSize = 33 - i;
				for (int j = 0 ; j < columnSize ; j++)
				{
					if (j % 2 == 0)
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
		
		for (int i = 0 ; i < cells.size() ; i++)
		{
			for (int j = 0 ; j < cells.get(i).size() ; j++)
				cells.get(i).get(j).setFilled(1);
		}
		
		print();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coordinates startingPos(int playerNum)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coordinates[] goal(int playerNum)
	{
		// TODO Auto-generated method stub
		return null;
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
