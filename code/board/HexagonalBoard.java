package board;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import gui.CaseListener;
import gui.WallListener;
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
		
		// 5 de côté
		for (int i = 0 ; i <= 32 ; i++)
		{
			ArrayList<ACell> row = new ArrayList<ACell>();
			// Rows starting by a wall (and with cases)
			if (i % 4 == 2 && (i >= 8 && i <=24))
			{
				for (int j = 0 ; j < 17 ; j++)
				{
					if (j % 4 == 2)
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
				else if (27 == i)
					columnSize = 6;
				else if (29 == i)
					columnSize = 4;
				else if (i == 31)
					columnSize = 2;
				for (int j = 0 ; j < columnSize ; j++)
					row.add(new HexagonalWall());
			}
			cells.add(row);
		}
		
        playersPositions = new Coordinates[this.players.length];
        for (int i = 0 ; i < this.players.length ; i++)
        {
        	playersPositions[i] = startingPos(i);
        }
        
        update();
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
		
		//panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		//panel.add(new JButton("Test"));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.CENTER;

		for (int i = 0 ; i <= 32 ; i++)
		{
			c.gridy = i;
			if (i % 2 == 0)
			{
				for (int j = 0 ; j < getXSize(i) ; j++)
				{
					if (0 <= i && i < 8)
						c.gridx = 8 - i + j;
					else if (i <= 24)
						c.gridx = j;
					else
						c.gridx = 8 - (32 - i) + j;
					
					if (i < 8 || i > 24 || (8 <= i && i <= 24 && i % 4 == 0))
					{
						if (j % 4 == 0)
						{
							c.ipadx = 40;
							c.ipady = 33;
						}
						else
						{
							c.ipadx = 16;
							c.ipady = 10;
							if (j % 4 == 2)
							{
								c.ipadx = 40;
							}
						}
					}
					else
					{
						if (j % 4 == 2)
						{
							c.ipadx = 40;
							c.ipady = 33;
						}
						else
						{
							c.ipadx = 16;
							c.ipady = 10;
							if (j % 4 == 0)
							{
								c.ipadx = 40;
							}
						}
					}
					panel.add(cells.get(i).get(j), c);
					cells.get(i).get(j).setEnabled(false);
					addActionListener(i, j);
				}
			}
			else
			{
				for (int j = 0 ; j < getXSize(i) ; j++)
				{
					if (0 <= i && i < 8)
						c.gridx = 8 - i + 2*j;
					else if (i <= 24)
						c.gridx = 1 + 2*j;
					else
						c.gridx = 8 - (32 - i) + 2*j;

					c.ipadx = 40;
					c.ipady = 10;
					if ((j % 2 == 0 && i % 4 == 3) || (j % 2 == 1 && i % 4 == 1))
					{
						((HexagonalWall) cells.get(i).get(j)).setAngle(20);
					}
					else
					{
						((HexagonalWall) cells.get(i).get(j)).setAngle(-20);
					}
					panel.add(cells.get(i).get(j), c);
					cells.get(i).get(j).setEnabled(false);
					addActionListener(i, j);
				}
			}
		}
		
		panel.repaint();
		panel.revalidate();
	}

    @Override
    public Coordinates[] possibleMoves(int playerNum, boolean withPlayer, Coordinates pos)
    {
    	
    	TreeSet<Coordinates> coord = new TreeSet<Coordinates>();
    	int x = pos.getX(), y = pos.getY();
    	
    	Coordinates[] neighbours = getNeighbours(pos);
    	try
    	{
	    	for (int i = 0 ; i < neighbours.length ; i++)
	    	{
				int dx = neighbours[i].getX() - x;
				int dy = neighbours[i].getY() - y;
				// There is no wall between the two cases
				System.err.println(pos + " " + neighbours[i]);
				if (filled(getWall(pos, neighbours[i])) == 0)
				{
					System.err.println("no wall");
					// If the case is not empty (and if we consider the other players)
					if (filled(neighbours[i]) != 0 && withPlayer)
					{
						System.err.println("not empty");
						// If the case beyond the neighbour is not empty or if there is a wall between them
						if (blocked(x+dx, y+dy, x+2*dx, y+2*dy, true))
						{
							Coordinates[] neigh = getNeighbours(neighbours[i]);
							for (int j = 0 ; j < neigh.length ; j++)
							{
								if (!blocked(neighbours[i], neighbours[j], true))
									coord.add(neigh[j]);
							}
						}
						else
						{
							coord.add(new Coordinates(x + 2*dx, y + 2*dy));
						}
					}
					else
					{
						System.err.println("empty");
						coord.add(neighbours[i]);
					}
				}
	    	}
    	}
    	catch(Exception e)
    	{
    		System.err.println(e);
    		e.printStackTrace();
    	}
    	
    	return coord.toArray(new Coordinates[0]);
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
	
	@Override
	protected Coordinates getWall(Coordinates start, Coordinates target)
	{
		if (start.getX() == target.getX() && start.getY() == target.getY())
			throw new RuntimeException("Invalid coordinates: start and target are the same");
		
		int dy = target.getY() - start.getY();
		
		if (start.getY() == 0)
		{
			if (target.getY() == 2)
				return new Coordinates(target.getX()/4, 1);
			else if (target.getY() == 4)
				return new Coordinates(2, 2);
		}
		else if (start.getY() == 2)
		{
			if (target.getY() == 0)
				return new Coordinates(start.getX()/4, 1);
			else if (target.getY() == 4)
				return new Coordinates(start.getX()/4 + target.getX()/4, 3);
			else if (target.getY() == 6)
				return new Coordinates(start.getX() + 2, 6);
		}
		else if (start.getY() == 4)
		{
			if (target.getY() == 0)
				return new Coordinates(2, 4);
			else if (target.getY() == 2)
				return new Coordinates(start.getX()/4 + target.getX()/4, 3);
			else if (target.getY() == 6)
				return new Coordinates(start.getX()/4 + target.getX()/4, 5);
			else if (target.getY() == 8)
				return new Coordinates(start.getX()+2, 6);
		}
		else if (start.getY() >= 6 && start.getY() < 28)
		{
			if (target.getY() == start.getY()-4)
				return new Coordinates(start.getX(), start.getY()+dy/2);
			else if (target.getY() == start.getY()-2 || target.getY() == start.getY()+2)
				return new Coordinates(start.getX()/4 + target.getX()/4, start.getY()+dy/2);
			else if (target.getY() == start.getY()+4)
				return new Coordinates(target.getX(), start.getY()+dy/2);
		}
		else if (start.getY() == 28)
		{
			if (target.getY() == 32)
				return new Coordinates(2, 28);
			else if (target.getY() == 30 || target.getY() == 26)
				return new Coordinates(start.getX()/4 + target.getX()/4, start.getY()+dy/2);
			else if (target.getY() == 24)
				return new Coordinates(start.getX()+2, 26);
		}
		else if (start.getY() == 30)
		{
			if (target.getY() == 32)
				return new Coordinates(start.getX()/4, 31);
			else if (target.getY() == 28)
				return new Coordinates(start.getX()/2 + target.getX()/4, 29);
			else if (target.getY() == 26)
				return new Coordinates(start.getX() + 2, 28);
		}
		else if (start.getY() == 32)
		{
			if (target.getY() == 30)
				return new Coordinates(target.getX()/4, 31);
			else if (target.getY() == 28)
				return new Coordinates(2, 20);
		}
		
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
		if (y + 2 <= 32)
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
		if (y + 4 <= 32)
		{
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
				if (x-4 >= 0 && x < getXSize(y)-1)
					neighbours.add(new Coordinates(x-4, y+4));
			}
		}
		
		return neighbours.toArray(new Coordinates[0]);
	}
	
	// Add the case designated by (i, j) as listeners (for human)
	private void addActionListener(int i, int j)
	{
		for (APlayer player : players)
		{
			if (player instanceof Human)
			{
				final Human human = (Human) player;
				
				// We set the coordinates of the cell to be able to get them when the button is pressed
				cells.get(i).get(j).setActionCommand("(" + j + ", " + i + ")");
				
				if (i <= 8 || 24 <= i)
				{
					if (i % 2 == 0)
					{
						if (j % 4 == 0)
							cells.get(i).get(j).addActionListener(new CaseListener(human, this));
						else if (j % 4 == 2)
							cells.get(i).get(j).addActionListener(new WallListener(human, this));
					}
					// Just walls
					else
					{
						cells.get(i).get(j).addActionListener(new WallListener(human, this));
					}
				}
				else if (8 < i && i < 24)
				{
					if (i % 4 == 0)
					{
						if (j % 4 == 0)
							cells.get(i).get(j).addActionListener(new CaseListener(human, this));
						else if (j % 4 == 2)
							cells.get(i).get(j).addActionListener(new WallListener(human, this));
					}
					else if (i % 4 == 2)
					{
						if (j % 4 == 2)
							cells.get(i).get(j).addActionListener(new CaseListener(human, this));
						else if (j % 4 == 0)
							cells.get(i).get(j).addActionListener(new WallListener(human, this));
					}
					else
					{
						cells.get(i).get(j).addActionListener(new WallListener(human, this));
					}
				}
			}
		}
	}
}
