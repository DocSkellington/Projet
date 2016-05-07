package board;

import players.*;
import players.Human;

import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.JPanel;

import gui.CaseListener;
import gui.WallListener;

/** Manages all information about the rectangular board.
 * The rectangular board 9x9 is the basic board for Quoridor
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class RectangularBoard extends ABoard
{
    /** The constructor
     * @param players The array of players 
     * @param xSize The size of the board (in number of player cases)
     * @param ySize The size of the board (in number of player cases)
     */
    public RectangularBoard(APlayer[] players, int xSize, int ySize)
    {
    	super(players);
        int numPlayers = players.length;        
        for (int x = 0 ; x < 2*ySize-1 ; x++)
        {
        	ArrayList<ACell> row = new ArrayList<ACell>();
            for (int y = 0 ; y < 2*xSize-1 ; y++)
            {
                if((x % 2 == 0) && (y % 2 == 0))
                    row.add(new Case());
                else
                    row.add(new Wall());
            }
            cells.add(row);
        }
        
        playersPositions = new Coordinates[numPlayers];
        for (int i = 0 ; i < numPlayers ; i++)
        {
        	playersPositions[i] = startingPos(i);
        }
        
        update();
    }
    
    @Override
    public String toString()
    {
    	String res = " ";
        for(int i = 0 ; i < cells.get(0).size() ; i++)
        {
            res += "_";
        }
        res += "\n";
        
        for (int x = 0 ; x < cells.size() ; x++)
        {
            res += "|";
            for (int y = 0 ; y < cells.get(0).size() ; y++)
            {
            	res += cells.get(x).get(y).toString();
            }
            res += "|\n";
        }
        
        res += " ";
        for(int i = 0 ; i < cells.get(0).size() ; i++)
        {
        	res += "-";
        }
        res += "\n";
        return res;
    }
    
    @Override
    public void fill(JPanel panel)
    {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		
		Coordinates numCases = getSize();
		
		for (int i = 0 ; i < cells.size() ; i++)
		{
			for (int j = 0 ; j < cells.get(0).size() ; j++)
			{
				c.gridx = j;
				c.gridy = i;
				if (i % 2 == 0 && j % 2 == 0)
				{
					c.ipadx = 630 / numCases.getX();
					c.ipady = 594 / numCases.getY();
				}
				else if (i % 2 == 1 && j % 2 == 0)
				{
					c.ipadx = 630 / numCases.getX();
					c.ipady = 153 / numCases.getY();
				}
				else if (i % 2 == 0 && j % 2 == 1)
				{
					c.ipadx = 153 / numCases.getX();
					c.ipady = 594 / numCases.getY();
				}
				else
				{
					c.ipadx = 153 / numCases.getX();
					c.ipady = 153 / numCases.getY();
				}
				
				for (APlayer player : players)
				{
					if (player instanceof Human)
					{
						final Human human = (Human) player;
						
						// We set the coordinates of the cell to be able to get them when the button is pressed
						cells.get(i).get(j).setActionCommand("(" + j + ", " + i + ")");
						
						// Case
						if (i % 2 == 0 && j % 2 == 0)
						{
							cells.get(i).get(j).addActionListener(new CaseListener(human, this));
						}
						// Walls
						else
						{
							if (i % 2 != 1 || j % 2 != 1)
							{
								cells.get(i).get(j).addActionListener(new WallListener(human, this));
							}
						}
					}
				}
				// We deactivate the cell
				cells.get(i).get(j).setEnabled(false);
				panel.add(cells.get(i).get(j), c);
			}
		}
		
		panel.repaint();
		panel.revalidate();
	}
    
    @Override
    public Coordinates getSize()
    {
    	return new Coordinates((int)Math.round(cells.get(0).size()/2.), (int)Math.round(cells.size()/2.));
    }
    
    @Override
    public Coordinates startingPos(int playerNum)
    {
		if (playerNum == 0)
		{
			if ((cells.get(0).size() / 2) % 2 == 0)
				return new Coordinates(cells.get(0).size()/2, cells.size()-1);
			return new Coordinates(cells.get(0).size() / 2 - 1, cells.size()-1);
		}
		else if (playerNum == 1)
		{
			if ((cells.get(0).size() / 2) % 2 == 0)
				return new Coordinates(cells.get(0).size()/2, 0);
			return new Coordinates(cells.get(0).size() / 2 - 1, 0);
		}
		else if (playerNum == 2)
		{
			if ((cells.size() / 2) % 2 == 0)
				return new Coordinates(0, cells.size()/2);
			return new Coordinates(0, cells.size() - 1);
		}
		else
		{
			if ((cells.size() / 2) % 2 == 0)
				return new Coordinates(cells.get(0).size() - 1, cells.size()/2);
			return new Coordinates(cells.get(0).size() - 1, cells.size() - 1);
		}
    }

    @Override
    public Coordinates[] goal(int playerNum)
    {
    	if (playerNum == 0)
        {
    		int numCases = (int)Math.round(cells.get(0).size() / 2.);
    		Coordinates[] goal = new Coordinates[numCases];
    		for (int i = 0 ; i < numCases ; i++)
    		{
    			goal[i] = new Coordinates(2*i, 0);
    		}
    		return goal;
        }
    	else if (playerNum == 1)
    	{
    		int numCases = (int)Math.round(cells.get(0).size() / 2.);
    		Coordinates[] goal = new Coordinates[numCases];
    		for (int i = 0 ; i < numCases ; i++)
    		{
    			goal[i] = new Coordinates(2*i, cells.size()-1);
    		}
    		return goal;
    	}
    	else if (playerNum == 2)
    	{
    		int numCases = (int)Math.round(cells.size() / 2.);
    		Coordinates[] goal = new Coordinates[numCases];
    		for (int i = 0 ; i < numCases ; i++)
    		{
    			goal[i] = new Coordinates(cells.get(0).size() - 1, 2 * i);
    		}
    		return goal;
    	}
    	else
    	{
    		int numCases = (int)Math.round(cells.size() / 2.);
    		Coordinates[] goal = new Coordinates[numCases];
    		for (int i = 0 ; i < numCases ; i++)
    		{
    			goal[i] = new Coordinates(0, 2 * i);
    		}
    		return goal;
    	}
    }
    
    @Override
    public void colorAdjacentWalls(Coordinates coord, boolean coloured)
    {
		// If it's outside the board
    	if ((coord.getX() % 2 == 1 && coord.getY() % 2 == 1) || coord.getX() < 0 || coord.getY() < 0
    			|| coord.getX() > cells.get(0).size() || coord.getY() > cells.size())
    		return;
    	// If it's on the edge
    	if (coord.getX() == cells.get(0).size()-1)
    		coord = new Coordinates(coord.getX() - 2, coord.getY());
    	if (coord.getY() == cells.size()-1)
    		coord = new Coordinates(coord.getX(), coord.getY() - 2);
    	
    	
    	if (coloured)
    	{
        	coord = shift(coord);
    		// If we can set a wall, we set the value of this cells at 2
        	if (coord.getX() % 2 == 0)
        	{
        		if (cells.get(coord.getY()).get(coord.getX()).filled() == 0 && cells.get(coord.getY()).get(coord.getX()+2).filled() == 0
        				&& cells.get(coord.getY()).get(coord.getX()+1).filled() == 0)
        		{
    	    		fillWall(coord, 2);
        		}
        	}
        	else
        	{
        		if (cells.get(coord.getY()).get(coord.getX()).filled() == 0 && cells.get(coord.getY()+2).get(coord.getX()).filled() == 0
        				&& cells.get(coord.getY()+1).get(coord.getX()).filled() == 0)
        		{
    	    		fillWall(coord, 2);
        		}
        	}
    	}
    	else
    	{
    		coord = removeShift(coord);
    		// If the cells aren't filled by a wall, we set the value of this cells at 0
	    	if (coord.getX() % 2 == 0)
	    	{
        		if (cells.get(coord.getY()).get(coord.getX()).filled() != 1 && cells.get(coord.getY()).get(coord.getX()+2).filled() != 1
        				&& cells.get(coord.getY()).get(coord.getX()+1).filled() != 1)
        		{
    	    		fillWall(coord, 0);
        		}
	    	}
	    	else
	    	{
        		if (cells.get(coord.getY()).get(coord.getX()).filled() != 1 && cells.get(coord.getY()+2).get(coord.getX()).filled() != 1
        				&& cells.get(coord.getY()+1).get(coord.getX()).filled() != 1)
        		{
    	    		fillWall(coord, 0);
        		}
	    	}
    	}
    }
    
    @Override
    public void colorAdjacentWallsDestroy(Coordinates coord, boolean coloured)
    {
		// If it's outside the board
    	if ((coord.getX() % 2 == 1 && coord.getY() % 2 == 1) || coord.getX() < 0 || coord.getY() < 0
    			|| coord.getX() > cells.get(0).size() || coord.getY() > cells.size())
    		return;
    	// If it's on the edge
    	if (coord.getX() == cells.get(0).size()-1)
    		coord = new Coordinates(coord.getX() - 2, coord.getY());
    	if (coord.getY() == cells.size()-1)
    		coord = new Coordinates(coord.getX(), coord.getY() - 2);
    	
    	
    	if (coloured)
    	{
        	//coord = shift(coord);
        	// If there is a wall, we set the value of the cells at 3
        	if (coord.getX() % 2 == 0)
        	{
        		if (cells.get(coord.getY()).get(coord.getX()).filled() == 1 && cells.get(coord.getY()).get(coord.getX()+2).filled() == 1
        				&& cells.get(coord.getY()).get(coord.getX()+1).filled() == 1)
        		{
    	    		cells.get(coord.getY()).get(coord.getX()).setFilled(3);
    	    		cells.get(coord.getY()).get(coord.getX()+1).setFilled(3);
    	    		cells.get(coord.getY()).get(coord.getX()+2).setFilled(3);
        		}
        	}
        	else
        	{
        		if (cells.get(coord.getY()).get(coord.getX()).filled() == 1 && cells.get(coord.getY()+2).get(coord.getX()).filled() == 1
        				&& cells.get(coord.getY()+1).get(coord.getX()).filled() == 1)
        		{
    	    		cells.get(coord.getY()).get(coord.getX()).setFilled(3);
    	    		cells.get(coord.getY()+1).get(coord.getX()).setFilled(3);
    	    		cells.get(coord.getY()+2).get(coord.getX()).setFilled(3);
        		}
        	}
    	}
    	else
    	{
    		//coord = removeShift(coord);
    		// If the cases are filled with 3, we set the cells at 1
	    	if (coord.getX() % 2 == 0)
	    	{
        		if (cells.get(coord.getY()).get(coord.getX()).filled() == 3 && cells.get(coord.getY()).get(coord.getX()+2).filled() == 3
        				&& cells.get(coord.getY()).get(coord.getX()+1).filled() == 3)
        		{
    	    		cells.get(coord.getY()).get(coord.getX()).setFilled(1);
    	    		cells.get(coord.getY()).get(coord.getX()+1).setFilled(1);
    	    		cells.get(coord.getY()).get(coord.getX()+2).setFilled(1);
        		}
	    	}
	    	else
	    	{
        		if (cells.get(coord.getY()).get(coord.getX()).filled() == 3 && cells.get(coord.getY()+2).get(coord.getX()).filled() == 3
        				&& cells.get(coord.getY()+1).get(coord.getX()).filled() == 3)
        		{
    	    		cells.get(coord.getY()).get(coord.getX()).setFilled(1);
    	    		cells.get(coord.getY()+1).get(coord.getX()).setFilled(1);
    	    		cells.get(coord.getY()+2).get(coord.getX()).setFilled(1);
        		}
	    	}
    	}
    }    

    @Override
    protected void fillWall(Coordinates coord, int fill)
    {
    	int x = coord.getX(), y = coord.getY();
    	
        int x2 = x, y2 = y;
        // Vertical
        if (x % 2 == 1)
        {
        	y2 += 2;
        }
        // Horizontal
        else
        	x2 += 2;
    
		// We effectively set the wall
        cells.get(y).get(x).setFilled(fill);
        cells.get((y2+y)/2).get((x2+x)/2).setFilled(fill);
        cells.get(y2).get(x2).setFilled(fill);
    }
    
    @Override
    protected boolean tryWall(Coordinates coord)
    {
    	int x = coord.getX(), y = coord.getY(), x2 = x, y2 = y;
    	
    	// Horizontal
    	if(x % 2 == 0)
    	{
    		// If x and y are even, it's a player case. So it's impossible to set a wall
    		if(y % 2 == 0)
    			return false;
    		x2 += 2;
    	}
    	// Vertical
    	else
    	{
    		// If x and y are odd, it's impossible to set a wall
    		if(y % 2 == 1)
    			return false;
    		y2 += 2;
    	}
    	
    	// In the board ?
    	if (x < 0 || x2 >= cells.get(0).size() || y < 0 || y2 >= cells.size())
    	{
    		return false;
    	}
    	// If one of the case is filled, we can't set a wall
    	if ((filled(x, y) != 0 || filled(x2, y2) != 0 || filled((x+x2)/2, (y+y2) / 2) != 0) && (filled(x, y) != 2
    			|| filled(x2, y2) != 2 || filled((x+x2)/2, (y+y2) / 2) != 2))
    		return false;

    	return true;
    }
    
    @Override
    protected Coordinates shift(Coordinates coord)
    {
    	Coordinates newCoord = coord.clone();
    	// x is even => vertical wall
    	if (coord.getX() % 2 == 0)
		{
    		// If inside the checkable part of the board
    		if (coord.getX() - 2 >= 0 && coord.getX() <= cells.get(0).size() - 3)
    		{
    			// If the 2 cases on the right are filled
    			if(cells.get(coord.getY()).get(coord.getX()+1).filled() != 0 || cells.get(coord.getY()).get(coord.getX()+2).filled() != 0)
    			{
    				// If the 2 cases on the left are empty
    				if(cells.get(coord.getY()).get(coord.getX()-1).filled() == 0 && cells.get(coord.getY()).get(coord.getX()-2).filled() == 0)
					{
    					// We can shift the coordinates
		    			newCoord = new Coordinates(coord.getX()-2, coord.getY());
					}
    			}
    		}
		}
    	// horizontal wall
    	else if (coord.getX() % 2 == 1 && coord.getY() % 2 == 0)
		{
    		if (coord.getY() - 2 >= 0 && coord.getY() <= cells.size() - 3)
			{
    			// If the 2 cells below are filled
    			if(cells.get(coord.getY()+1).get(coord.getX()).filled() != 0 || cells.get(coord.getY()+2).get(coord.getX()).filled() != 0)
				{
    				// If the 2 cells above are empty
    				if(cells.get(coord.getY()-1).get(coord.getX()).filled() == 0 && cells.get(coord.getY()-2).get(coord.getX()).filled() == 0)
					{
    					// We can shift the coordinates
		    			newCoord = new Coordinates(coord.getX(), coord.getY()-2);
					}
				}
			}
		} 
    	return newCoord;
    }
    
    @Override
    protected Coordinates removeShift(Coordinates coord)
    {
    	Coordinates newCoord = coord.clone();
    	
    	// x even => vertical wall
    	if (coord.getX() % 2 == 0)
		{
    		if (coord.getX() - 2 >= 0 && coord.getX() <= cells.get(0).size() - 3)
    		{
    			// If the 2 cases on the right are filled
    			if(cells.get(coord.getY()).get(coord.getX()+1).filled() != 0 || cells.get(coord.getY()).get(coord.getX()+2).filled() != 0)
    			{
    				// If the 2 cases on the left are translucent
    				if(cells.get(coord.getY()).get(coord.getX()-1).filled() == 2 && cells.get(coord.getY()).get(coord.getX()-2).filled() == 2)
					{
		    			newCoord = new Coordinates(coord.getX()-2, coord.getY());
					}
	    		}
    		}
		}
    	else if (coord.getX() % 2 == 1 && coord.getY() % 2 == 0)
		{
    		if (coord.getY() - 2 >= 0 && coord.getY() <= (cells.size() - 3))
			{
    			// If the 2 cases on the right are filled
    			if (cells.get(coord.getY()+1).get(coord.getX()).filled() != 0 || cells.get(coord.getY()+2).get(coord.getX()).filled() != 0)
				{
    				// If the 2 cases above are translucent
    				if (cells.get(coord.getY()-1).get(coord.getX()).filled() == 2 && cells.get(coord.getY()-2).get(coord.getX()).filled() == 2)
					{
		    			newCoord = new Coordinates(coord.getX(), coord.getY()-2);
					}
				}
			}
		}
    	return newCoord;
    }
    
    /** Returns the maximum number of players for this board
     * 
     * @return The maximum number of players
     */
    public static int maxPlayers()
    {
    	return 4;
    }
}
