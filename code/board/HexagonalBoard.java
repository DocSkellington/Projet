package board;

import java.util.ArrayList;
import java.util.TreeSet;

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
	private final int maxX = 17, maxY = 32;
	
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
		for (int i = 0 ; i <= maxY ; i++)
		{
			ArrayList<ACell> row = new ArrayList<ACell>();
			// Rows starting by a wall (and with cases)
			if (i % 4 == 2 && (i >= 8 && i <=24))
			{
				for (int j = 0 ; j < maxX ; j++)
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
				int columnSize = maxY;
				if (0 <= i && i <= 6)
					columnSize = 2*i + 1;	
				else if (26 <= i && i <= maxX)
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
					columnSize = maxX - i + 1;
				for (int j = 0 ; j < columnSize ; j++)
					row.add(new HexagonalWall());
			}
			cells.add(row);
		}
		
		Coordinates[] neighbours = getNeighbours(new Coordinates(0, 24));
    	for (int i = 0 ; i < neighbours.length ; i++)
    	{
    		System.err.println(neighbours[i]);
    	}
    	
        playersPositions = new Coordinates[this.players.length];
        for (int i = 0 ; i < this.players.length ; i++)
        {
        	playersPositions[i] = startingPos(i);
        	Coordinates[] coord = possibleMoves(i, true);
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
    public Coordinates[] possibleMoves(int playerNum, boolean withPlayer, Coordinates pos)
    {
    	TreeSet<Coordinates> coord = new TreeSet<Coordinates>();
    	int x = pos.getX(), y = pos.getY();
    	
    	Coordinates[] neighbours = getNeighbours(pos);
    	
    	for (int i = 0 ; i < neighbours.length ; i++)
    	{
			int dx = x - neighbours[i].getX();
			int dy = y - neighbours[i].getY();
			// There is no wall between the two cases
			if (filled(x + dx/2, y + dy/2) == 0)
			{
				// If the case is not empty (and if we consider the other players)
				if (filled(x + dx, y + dy) != 0 && withPlayer)
				{
					// If the case beyond the neighbour is not empty or if there is a wall between them
					if (blocked(x+dx, y+dy, x+2*dx, y+2*dy, true))
					{
						// TODO
					}
				}
			}
    		/*if (!blocked(pos, neighbours[i], withPlayer))
    			coord.add(neighbours[i]);
    		else
    		{
    			
    		}*/
    	}
    	
    	/*for (int i = -2 ; i <= 2 ; i += 2)
		{
			for (int j = -2 ; j <= 2 ; j += 2)
			{
				if (i == 0 && j == 0)
					continue;

				System.err.println(i + " " + j);
				if (!blocked(x, y, x+i, y+j, withPlayer))
				{
					System.err.println(new Coordinates(x+i, y+j));
					coord.add(new Coordinates(x+i, y+j));
				}
				else
				{
					System.err.println("Hello");
					if (filled(x+i, y+j) != 0 && withPlayer)
					{
						System.err.println("Hello2" + " " + new Coordinates(x+i, y+j));
						// If the case is empty and reachable
						if (!blocked(x+i, y+j, x+2*i, y+j, true))
						{
							System.err.println("Not blocked");
							coord.add(new Coordinates(x+2*i, y+j));
						}
						// In this case, we must check if we can move diagonally
						else
						{
							System.err.println("Blocked" + " " + new Coordinates(x+i, y+j));
							if (!blocked(x+i, y+j, x+i, y-2+j, true))
							{
								System.err.println("Blocked2");
								coord.add(new Coordinates(x+i, y-2+j));
							}
							System.err.println("Finish");
							if (!blocked(x+i, y+j, x+i, y+2+j, true))
							{
								System.err.println("Blocked3");
								coord.add(new Coordinates(x+i, y+2+j));
							}
						}
					}
					System.err.println("Bye");
				}
				
		    }
		}*/
    	
    	//System.err.println(coord);
    	return coord.toArray(new Coordinates[0]);
    }
    
	@Override
	public Coordinates getSize()
	{
		return new Coordinates(5, 5);
	}

	@Override
	public int getYSize(int column)
	{
		switch(column)
		{
		case 0:
		case 16:
			return 9;
		case 1:
		case 15:
			return 10;
		case 2:
		case 14:
			return 11;
		case 3:
		case 13:
			return 12;
		case 4:
		case 12:
			return 13;
		case 5:
		case 11:
			return 14;
		case 6:
		case 10:
			return 15;
		case 7:
		case 9:
			return 16;
		case 8:
			return 17;
		default:
				return 0;
		}
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

	// Gets the coordinates of the direct cases neighbours of a case
	private Coordinates[] getNeighbours(Coordinates coord)
	{
		// Si y < 8, (x - 2
		// Si y % == 1 et y > 8, (x - 2)
		/* Regarder 4 lignes en haut, 0 colonnes de décalage
		 * Regarder 2 lignes en haut, -2 et +2 de décalage en x
		 * Regarder 2 lignes en bas, -2 et +2 de décalage en x
		 * Regarder 4 lignes en bas, 0 de décalage en x
		 * */
		TreeSet<Coordinates> neighbours = new TreeSet<Coordinates>();
		int x = coord.getX(), y = coord.getY();
		
		if (y - 4 >= 0)
		{
			if (y == 4)
			{
				if (x == 4)
					neighbours.add(new Coordinates(0, 0));
			}
			else if (y <= 10)
			{
				if (x-4 >= 0 && (x-4) < getXSize(y-4))
					neighbours.add(new Coordinates(x-4, y-4));
			}
			else if (y > 24)
			{
				if (x+4 < getXSize(y-4))
					neighbours.add(new Coordinates(x+4, y-4));
			}
			else if (x < getXSize(y-4))
			{
				neighbours.add(new Coordinates(x, y-4));
			}
		}
		if (y - 2 >= 0)
		{
			if (y < 10)
			{
				if ((x-4) >= 0)
					neighbours.add(new Coordinates(x-4, y-2));
				if (x <= getXSize(y-2))
					neighbours.add(new Coordinates(x, y-2));
			}
			else if (10 <= y && y <= 24)
			{
				if (x-2 >= 0)
					neighbours.add(new Coordinates(x-2, y-2));
				if (x+2 < getXSize(y-2))
					neighbours.add(new Coordinates(x+2, y-2));
			}
			else if (y > 24)
			{
				if (x >= 0)
					neighbours.add(new Coordinates(x, y-2));
				if (x+4 < getXSize(y-2))
					neighbours.add(new Coordinates(x+4, y-2));
			}
		}
		if (y + 2 < maxY)
		{
			if (y < 8)
			{
				if (x >= 0)
					neighbours.add(new Coordinates(x, y+2));
				if (x+4 < getXSize(y+2))
					neighbours.add(new Coordinates(x+4, y+2));
			}
			else if (8 <= y && y < 24)
			{
				if (x - 2 >= 0)
					neighbours.add(new Coordinates(x-2, y+2));
				if (x+2 < getXSize(y+2))
					neighbours.add(new Coordinates(x+2, y+2));
			}
			else
			{
				if (x-4 >= 0)
					neighbours.add(new Coordinates(x-4, y+2));
				if (x < getXSize(y+2))
					neighbours.add(new Coordinates(x, y+2));
			}
		}
		if (y + 4 < maxY)
		{
			System.err.println("Hello");
			if (y == 28)
			{
				if (x == 4)
					neighbours.add(new Coordinates(0, 32));
			}
			else if (y < 6)
			{
				if (x + 4 < getXSize(y+4))
					neighbours.add(new Coordinates(x+4, y+4));
			}
			else if (6 <= y && y <= 20)
			{
				neighbours.add(new Coordinates(x, y+4));
			}
			else
			{
				if (x-4 >= 0)
					neighbours.add(new Coordinates(x-4, y+4));
			}
		}
		
		
		System.err.println(neighbours);
		
		
		return neighbours.toArray(new Coordinates[0]);
	}
}
