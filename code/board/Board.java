package board;

import players.*;
import players.Human;

import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JPanel;

import gui.CaseListener;
import gui.WallListener;
import pathFinder.*;

/** Manages all information about the board.
 * This includes setting walls, finding path, and so on.
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class Board
{
    protected ACell[][] cells;
    protected Coordinates[] playersPositions;
    protected ArrayList<Coordinates> placedWalls;
    protected APlayer[] players;
    
    public static final double tick = 333.333;
    public static double prev_tick = 0.00;
    
    /** The constructor
     * @param players The array of players 
     * @param xSize The size of the board (in number of player cases)
     * @param ySize The size of the board (in number of player cases)
     */
    public Board(APlayer[] players, int xSize, int ySize)
    {
        cells = new ACell[2*ySize-1][2*xSize-1];
        this.players = players; 
        placedWalls = new ArrayList<Coordinates>();
        int numPlayers = players.length;
        
        for (int x = 0 ; x < cells.length ; x++)
        {
            for (int y = 0 ; y < cells[0].length ; y++)
            {
                if((x % 2 == 0) && (y % 2 == 0))
                    cells[x][y] = new Case();
                else
                	cells[x][y] = new Wall();
            }
        }
        
        playersPositions = new Coordinates[numPlayers];
        for (int i = 0 ; i < numPlayers ; i++)
        {
        	playersPositions[i] = startingPos(i);
        }
        
        update();
    }
    
    public void reset()
    {
    	// Reset the walls
    	for(Coordinates wall : placedWalls)
    	{
    		destroyWall(wall);
    	}
    	placedWalls = new ArrayList<Coordinates>();
    	
    	// Reset the players positions
        playersPositions = new Coordinates[playersPositions.length];
        for (int i = 0 ; i < playersPositions.length ; i++)
        {
        	playersPositions[i] = startingPos(i);
        }
        update();
    }
    
    @Override
    public String toString()
    {
    	String res = " ";
        for(int i = 0 ; i < cells[0].length ; i++)
        {
            res += "_";
        }
        res += "\n";
        
        for (int x = 0 ; x < cells.length ; x++)
        {
            res += "|";
            for (int y = 0 ; y < cells[0].length ; y++)
            {
            	res += cells[x][y].toString();
            }
            res += "|\n";
        }
        
        res += " ";
        for(int i = 0 ; i < cells[0].length ; i++)
        {
        	res += "-";
        }
        res += "\n";
        return res;
    }
    
    /** Draws the board in the command prompt district */
    public void print()
    {
    	System.out.println(toString());
    }
    
    /** Fills the panel with every cell/button. Also sets the listeners
     * 
     * @param panel The panel to fill
     * @param players The array of players
     */
    public void fill(JPanel panel, APlayer[] players)
    {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		
		Coordinates numCases = getSize();
		
		for (int i = 0 ; i < cells.length ; i++)
		{
			for (int j = 0 ; j < cells[0].length ; j++)
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
						cells[i][j].setActionCommand("(" + j + ", " + i + ")");
						
						// Case
						if (i % 2 == 0 && j % 2 == 0)
						{
							cells[i][j].addActionListener(new CaseListener(human, this));
						}
						// Walls
						else
						{
							if (i % 2 != 1 || j % 2 != 1)
							{
								cells[i][j].addActionListener(new WallListener(human, this));
							}
						}
					}
				}
				// We deactivate the cell
				cells[i][j].setEnabled(false);
				panel.add(cells[i][j], c);
			}
		}
		
		panel.repaint();
		panel.revalidate();
	}
    
    /** Sets a wall if it can
     * 
     * @param coord The coordinates of the wall
     * @return True if the wall is set, false otherwise.
     */
    public boolean setWall(Coordinates coord)
    {
    	// If outside the board
    	if (coord.getX() < 0 || coord.getY() < 0 || coord.getX() > cells[0].length - 1 || coord.getY() > cells.length - 1)
    		return false;
    	
    	// If the coord is on the edge of the board, we shift from 2 cells
    	if (coord.getX() == cells[0].length-1)
    		coord = new Coordinates(coord.getX() - 2, coord.getY());
    	if (coord.getY() == cells.length-1)
    		coord = new Coordinates(coord.getX(), coord.getY() - 2);
    	
    	// If the cell is already filled by a wall, we can't set a wall here.
    	if (cells[coord.getY()][coord.getX()].filled() == 1)
    		return false;
    	
    	// We remove the translucent wall (gui)
    	coord = removeShift(coord);
    	colorAdjacentWalls(coord, false);
    	coord = shift(coord);
    	
        // If we can set the wall
    	if(canSetWall(coord))
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
            cells[y][x].setFilled(1);
            cells[(y2+y)/2][(x2+x)/2].setFilled(1);
            cells[y2][x2].setFilled(1);
            
            // We add the upper left coordinates in placedWalls
            placedWalls.add(coord);
            return true;
    	}
    	return false;
    }

    /** Checks if a wall can be set at coordinates (x;y) (The upper/right vertex).
     * Uses the A* algorithm.
     * @param coord The coordinates of the wall
     * @return True if the wall can be set, false otherwise
    */
    public boolean canSetWall(Coordinates coord)
    {
    	int x = coord.getX(), y = coord.getY();
        
        if (!tryWall(coord))
        	return false;
        
        int x2 = x, y2 = y;
        // Vertical
        if (x % 2 == 1)
        	y2 += 2;
        // Horizontal
        else
        	x2 += 2;
        
        // We set the wall
        cells[y][x].setFilled(1);
        cells[(y2+y)/2][(x2+x)/2].setFilled(1);
        cells[y2][x2].setFilled(1);
        
        // Is there still a path ?
        boolean none = false;
        for (APlayer player : players)
        {
        	Path path = findPath(player, false);
        	if (path == null)
        	{
        		none = true;
        		break;
        	}
        }
        // We destroy the walls.
	    cells[y][x].setFilled(0);
        cells[(y2+y)/2][(x2+x)/2].setFilled(0);
        cells[y2][x2].setFilled(0);
        if(none)
        {
        	return false;
        }
        return true;
    }
    
    /** Checks if there is a wall at the given coordinates
     * 
     * @param coord The coordinates
     * @return True if there is a wall (or if the coordinates are wrong), false otherwise
     */
    public boolean hasWall(Coordinates coord)
    {
    	// If the coordinates are wrong
    	if (coord.getX() < 0 || coord.getX() >= cells[0].length || coord.getY() < 0 || coord.getY() >= cells[0].length)
    	{
    		throw new RuntimeException("Out of the board");
    	}
    	
    	if (filled(coord) != 0 && filled(coord) != 2)
    		return true;
    	return false;
    }
    
    public boolean destroyWall(Coordinates coord)
    {
    	if (!hasWall(coord))
    		return false;
    	
    	int x = coord.getX(), y = coord.getY();
    	if (coord.getX() == cells[0].length-1)
    		x -= 2;
    	if (coord.getY() == cells.length-1)
    		y -= 2;
    	
    	if (placedWalls.contains(new Coordinates(x, y)))
    	{
    		int x2 = 0, y2 = 0;
    		
    		if (x % 2 == 0)
    			x2 = 2;
    		else
    			y2 = 2;
    		
    		cells[y][x].setFilled(0);
    		cells[y+y2/2][x+x2/2].setFilled(0);
    		cells[y+y2][x+x2].setFilled(0);
    		
    		placedWalls.remove(new Coordinates(x, y));
    		return true;
    	}
    	
    	return false;
    }
    
    /** Finds the shortest path (uses the A* algorithm)
     * 
     * @param numPlayer The number of the player
	 * @param withPlayer If we consider the other player(s)
     * @return The shortest path
     */
    public Path findPath(int numPlayer, boolean withPlayer)
    {
    	return findPath(players[numPlayer], withPlayer, new ClosestHeuristic());
    }
    
    /** Finds the shortest path (uses the A* algorithm)
     * 
     * @param player A reference to the player who needs a path
	 * @param withPlayer If we consider the other player(s)
     * @return The shortest path
     */
    public Path findPath(APlayer player, boolean withPlayer)
    {
    	return findPath(player, withPlayer, new ClosestHeuristic());
    }
    
    /** Return the best path by using the A* algorithm (which uses the given heuristic method)
     * @param player A reference to the player who needs a path
	 * @param withPlayer If we consider the other player(s)
     * @param heuri A reference to an heuristic class to determinate the best path
     * @return The best path if it exists, null otherwise
     */
    public Path findPath(APlayer player, boolean withPlayer, IAStarHeuristic heuri)
    {
    	AStarPathFinder pathFinder = new AStarPathFinder(this, heuri);
    	Coordinates start = playersPositions[player.getNum()];
    	Coordinates[] target = goal(player.getNum());
    	Path path = null;
    	for (int i = 0 ; i < target.length ; i++)
    	{
    		Path temp = pathFinder.findPath(player, withPlayer, start.getX(), start.getY(), target[i].getX(), target[i].getY());
    		if (path == null)
    			path = temp;
    		else
			{
    			if (temp != null)
	    		{
	    			if(path.getLength() > temp.getLength())
	    				path = temp;
	    			else if (path.getLength() == temp.getLength() &&
	    					target[i].getX() == target[target.length/2].getX() && target[i].getY() == target[target.length/2].getY())
	    				path = temp;
	    		}
			}
    	}
    	return path;
    }
    
    
    /** Return the content of a cell
     * @param x The x coordinate of a cell
     * @param y The y coordinate of a cell
     * @return The content of the cell
     */
    public int filled(int x, int y)
    {
    	if(x >= 0 && x < cells[0].length && y >= 0 && y < cells.length)
        {
        	return cells[y][x].filled();
        }
        return -1;
    }
    
    /** Return the content of a cell
     * @param coord The coordinates of the point
     * @return The content of the cell a the given position
     */
    public int filled (Coordinates coord)
    {
    	return filled(coord.getX(), coord.getY());
    }
    
    /** The function is used by the path finder algorithm to determinate the cost of a movement. For now, it simply returns 1
     * @param player The player who is trying to move
     * @param sx The x coordinate of the case we're coming from
     * @param sy The y coordinate of the case we're coming from
     * @param tx The x coordinate of the case we're coming to
     * @param ty The y coordinate of the case we're coming to
     * @return The relative cost of the movement
     */
    public float getCost(APlayer player, int sx, int sy, int tx, int ty)
    {
        return 1.f;
    }
    
    /** Gets a copy of the array of cells
     * @return An array of array of Cells
     */
    public ACell[][] getCells()
    {
        // We don't want to give a reference to the data, so we copy the array
        ACell[][] map = new ACell[cells.length][cells[0].length];
        for (int i = 0 ; i < map.length ; i++)
        {
            for (int j = 0 ; j < map.length ; j++)
            {
                // Since copying an object would simply copy the reference, we create new cells
                map[i][j] = cells[i][j].clone();
            }
        }
        return map;
    }
    
    /** Gets the number of Cases of the board as Coordinates */
    public Coordinates getSize()
    {
    	return new Coordinates((int)Math.round(cells[0].length/2.), (int)Math.round(cells.length/2.));
    }
    
    /** Get the x size of the board
     * @return The x size of the board
     */
    public int getXSize()
    {
        return cells[0].length;
    }

    /** Get the y size of the board
     * @return The y size of the board
     */
    public int getYSize()
    {
        return cells.length;
    }
    
    /** Get the number of players
     * 
     * @return The number of players
     */
    public int getPlayerNumber()
    {
    	return players.length;
    }
    
    /** Checks if a player has won
	 *
     * @return The number of the winner
     */
    public int hasWon()
    {
    	for (int i = 0 ; i < players.length ; i++)
    	{
    		if (hasWon(players[i].getNum()))
    			return players[i].getNum();
    	}
    	return -1;
    }
    
    /** Checks if the player with the given number has won
     * @param numPlayer The number of the player
     * @return True if the player has won, false otherwise
     */
    public boolean hasWon(int numPlayer)
    {
    	Coordinates[] g = goal(numPlayer);
    	for (int i = 0 ; i < g.length ; i++)
    	{
    		if (playersPositions[numPlayer].equals(g[i]))
    			return true;
    	}
    	return false;
    }
    
    /** Check if a player can move from start position to target position. These two cases must be adjacent.
     * 
     * @param start The coordinates of the starting position
     * @param target The coordinates of the target position
     * @param withPlayer If we consider other player(s)
     * @return True if a player can move to target position, false otherwise
     */

    public boolean blocked(Coordinates start, Coordinates target, boolean withPlayer)
    {
    	return blocked(start.getX(), start.getY(), target.getX(), target.getY(), withPlayer);
    }

    /** Check if a player can move from start position to target position. These two cases must be adjacent.
     * 
     * @param sx The x coordinate of the starting position
     * @param sy The y coordinate of the starting position
     * @param tx The x coordinate of the target position
     * @param ty The y coordinate of the target position
     * @param withPlayer If we consider other player(s)
     * @return True if the way is blocked between the 2 adjacent cases, false otherwise.
     */
    public boolean blocked(int sx, int sy, int tx, int ty, boolean withPlayer)
    {
    	if (Math.abs(sx - tx) > 2 || Math.abs(sy - ty) > 2)
    	{
    		throw new RuntimeException("Too far");
    	}
    	
    	// If there isn't any wall and if the target case is empty (only considered if withPlayer == true), we can move
    	if (filled((sx+tx)/2, (sy+ty)/2) != 0 || (filled(tx, ty) != 0 && withPlayer))
		{
    		return true;
		}
    	else
		{
    		return false;
		}
    }
    
    /** The starting position for a player
     * 
     * @param playerNum The number of the player
     * @return The starting position (Coordinates) for the given player
     */
    public Coordinates startingPos(int playerNum)
    {
		if (playerNum == 0)
		{
			if ((cells[0].length / 2) % 2 == 0)
				return new Coordinates(cells[0].length/2, cells.length-1);
			return new Coordinates(cells[0].length / 2 - 1, cells.length-1);
		}
		else if (playerNum == 1)
		{
			if ((cells[0].length / 2) % 2 == 0)
				return new Coordinates(cells[0].length/2, 0);
			return new Coordinates(cells[0].length / 2 - 1, 0);
		}
		else if (playerNum == 2)
		{
			if ((cells.length / 2) % 2 == 0)
				return new Coordinates(0, cells.length/2);
			return new Coordinates(0, cells.length - 1);
		}
		else
		{
			if ((cells.length / 2) % 2 == 0)
				return new Coordinates(cells[0].length - 1, cells.length/2);
			return new Coordinates(cells[0].length - 1, cells.length - 1);
		}
    }

    /** Returns the goal coordinates for a given player
     * 
     * @param playerNum The number of the player
     * @return The goal coordinates (array) for this player
     */
    public Coordinates[] goal(int playerNum)
    {
    	if (playerNum == 0)
        {
    		int numCases = (int)Math.round(cells[0].length / 2.);
    		Coordinates[] goal = new Coordinates[numCases];
    		for (int i = 0 ; i < numCases ; i++)
    		{
    			goal[i] = new Coordinates(2*i, 0);
    		}
    		return goal;
        }
    	else if (playerNum == 1)
    	{
    		int numCases = (int)Math.round(cells[0].length / 2.);
    		Coordinates[] goal = new Coordinates[numCases];
    		for (int i = 0 ; i < numCases ; i++)
    		{
    			goal[i] = new Coordinates(2*i, cells.length-1);
    		}
    		return goal;
    	}
    	else if (playerNum == 2)
    	{
    		int numCases = (int)Math.round(cells.length / 2.);
    		Coordinates[] goal = new Coordinates[numCases];
    		for (int i = 0 ; i < numCases ; i++)
    		{
    			goal[i] = new Coordinates(cells[0].length - 1, 2 * i);
    		}
    		return goal;
    	}
    	else
    	{
    		int numCases = (int)Math.round(cells.length / 2.);
    		Coordinates[] goal = new Coordinates[numCases];
    		for (int i = 0 ; i < numCases ; i++)
    		{
    			goal[i] = new Coordinates(0, 2 * i);
    		}
    		return goal;
    	}
    }
    
    /** Updates the cells */
    public void update ()
    {
    	// We reset cases
    	for (int i = 0 ; i < cells.length ; i+=2)
    	{
    		for (int j = 0 ; j < cells[0].length ; j+=2)
    		{
    			cells[i][j].setFilled(0);
    		}
    	}
    	
    	// We fill the cases with a player on them
    	for (int i = 0 ; i < playersPositions.length ; i++)
    	{
    		Coordinates coord = playersPositions[i];
    		cells[coord.getY()][coord.getX()].setFilled(i + 1);
    	}
    }
    
    /** Repaints the board
     * 
     * @return True if the board has been repainted, false otherwise
     */
    public boolean repaint()
    {
    	// To avoid refreshing too frequently, we check if the difference between the current time and the last time we repainted is bigger than a constant
    	long now = System.currentTimeMillis();
    	if(System.currentTimeMillis() - prev_tick >= tick)
    	{
	    	for (int i = 0 ; i < cells.length ; i++)
	    	{
	    		for (int j = 0 ; j < cells[0].length ; j++)
	    		{
	    			cells[i][j].repaint();
	    		}
	    	}
	    	prev_tick = now;
	    	return true;
    	}
    	return false;
    }
    
    /** A button dis/enabler
     * 
     * @param enabled Whether we want to enable or disable
     * @param coord The coordinates of the buttons to dis/enable
     */
    public void setEnabledButtons(boolean enabled, Coordinates[] coord)
    {
    	for (int i = 0 ; i < coord.length ; i++)
    	{
    		cells[coord[i].getY()][coord[i].getX()].setEnabled(enabled);
    	}
    }
    
    /** Disable every button */
    public void disableAll()
    {
    	for (int i = 0 ; i < cells.length ; i++)
    	{
    		for (int j = 0 ; j < cells[0].length ; j++)
    		{
    			cells[i][j].setEnabled(false);
    		}
    	}
    }
    
    /** Enable all possible walls buttons
     * 
     * @param destroy If we want to destroy the walls
     */
    public void enableWalls(boolean destroy)
    {
    	for (int i = 0 ; i < cells.length ; i++)
    	{
    		for (int j = 0 ; j < cells[0].length ; j++)
    		{
    			if (i % 2 != j % 2)
    			{
    				if (destroy)
    				{
    					if (hasWall(new Coordinates(j, i)))
    					{
    						cells[i][j].setEnabled(true);
    						((Wall)cells[i][j]).setDestroy(true);
    					}
    				}
    				else
    				{
    					cells[i][j].setEnabled(true);
						((Wall)cells[i][j]).setDestroy(false);
    				}
    			}
    		}
    	}
    }
    
    /** Draws a translucent wall over the board
     * 
     * @param coord The upper-left corner of the wall
     * @param coloured Whether we colour the walls or not
     */
    public void colorAdjacentWalls(Coordinates coord, boolean coloured)
    {
		// If it's outside the board
    	if ((coord.getX() % 2 == 1 && coord.getY() % 2 == 1) || coord.getX() < 0 || coord.getY() < 0
    			|| coord.getX() > cells[0].length || coord.getY() > cells.length)
    		return;
    	// If it's on the edge
    	if (coord.getX() == cells[0].length-1)
    		coord = new Coordinates(coord.getX() - 2, coord.getY());
    	if (coord.getY() == cells.length-1)
    		coord = new Coordinates(coord.getX(), coord.getY() - 2);
    	
    	
    	if (coloured)
    	{
        	coord = shift(coord);
    		// If we can set a wall, we set the value of this cells at 2
        	if (coord.getX() % 2 == 0)
        	{
        		if (cells[coord.getY()][coord.getX()].filled() == 0 && cells[coord.getY()][coord.getX()+2].filled() == 0
        				&& cells[coord.getY()][coord.getX()+1].filled() == 0)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(2);
    	    		cells[coord.getY()][coord.getX()+1].setFilled(2);
    	    		cells[coord.getY()][coord.getX()+2].setFilled(2);
        		}
        	}
        	else
        	{
        		if (cells[coord.getY()][coord.getX()].filled() == 0 && cells[coord.getY()+2][coord.getX()].filled() == 0
        				&& cells[coord.getY()+1][coord.getX()].filled() == 0)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(2);
    	    		cells[coord.getY()+1][coord.getX()].setFilled(2);
    	    		cells[coord.getY()+2][coord.getX()].setFilled(2);
        		}
        	}
    	}
    	else
    	{
    		coord = removeShift(coord);
    		// If the cells aren't filled by a wall, we set the value of this cells at 0
	    	if (coord.getX() % 2 == 0)
	    	{
        		if (cells[coord.getY()][coord.getX()].filled() != 1 && cells[coord.getY()][coord.getX()+2].filled() != 1
        				&& cells[coord.getY()][coord.getX()+1].filled() != 1)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(0);
    	    		cells[coord.getY()][coord.getX()+1].setFilled(0);
    	    		cells[coord.getY()][coord.getX()+2].setFilled(0);
        		}
	    	}
	    	else
	    	{
        		if (cells[coord.getY()][coord.getX()].filled() != 1 && cells[coord.getY()+2][coord.getX()].filled() != 1
        				&& cells[coord.getY()+1][coord.getX()].filled() != 1)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(0);
    	    		cells[coord.getY()+1][coord.getX()].setFilled(0);
    	    		cells[coord.getY()+2][coord.getX()].setFilled(0);
        		}
	    	}
    	}
    }
    
    public void colorAdjacentWallsDestroy(Coordinates coord, boolean coloured)
    {
		// If it's outside the board
    	if ((coord.getX() % 2 == 1 && coord.getY() % 2 == 1) || coord.getX() < 0 || coord.getY() < 0
    			|| coord.getX() > cells[0].length || coord.getY() > cells.length)
    		return;
    	// If it's on the edge
    	if (coord.getX() == cells[0].length-1)
    		coord = new Coordinates(coord.getX() - 2, coord.getY());
    	if (coord.getY() == cells.length-1)
    		coord = new Coordinates(coord.getX(), coord.getY() - 2);
    	
    	
    	if (coloured)
    	{
        	//coord = shift(coord);
        	// If there is a wall, we set the value of the cells at 3
        	if (coord.getX() % 2 == 0)
        	{
        		if (cells[coord.getY()][coord.getX()].filled() == 1 && cells[coord.getY()][coord.getX()+2].filled() == 1
        				&& cells[coord.getY()][coord.getX()+1].filled() == 1)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(3);
    	    		cells[coord.getY()][coord.getX()+1].setFilled(3);
    	    		cells[coord.getY()][coord.getX()+2].setFilled(3);
        		}
        	}
        	else
        	{
        		if (cells[coord.getY()][coord.getX()].filled() == 1 && cells[coord.getY()+2][coord.getX()].filled() == 1
        				&& cells[coord.getY()+1][coord.getX()].filled() == 1)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(3);
    	    		cells[coord.getY()+1][coord.getX()].setFilled(3);
    	    		cells[coord.getY()+2][coord.getX()].setFilled(3);
        		}
        	}
    	}
    	else
    	{
    		//coord = removeShift(coord);
    		// If the cases are filled with 3, we set the cells at 1
	    	if (coord.getX() % 2 == 0)
	    	{
        		if (cells[coord.getY()][coord.getX()].filled() == 3 && cells[coord.getY()][coord.getX()+2].filled() == 3
        				&& cells[coord.getY()][coord.getX()+1].filled() == 3)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(1);
    	    		cells[coord.getY()][coord.getX()+1].setFilled(1);
    	    		cells[coord.getY()][coord.getX()+2].setFilled(1);
        		}
	    	}
	    	else
	    	{
        		if (cells[coord.getY()][coord.getX()].filled() == 3 && cells[coord.getY()+2][coord.getX()].filled() == 3
        				&& cells[coord.getY()+1][coord.getX()].filled() == 3)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(1);
    	    		cells[coord.getY()+1][coord.getX()].setFilled(1);
    	    		cells[coord.getY()+2][coord.getX()].setFilled(1);
        		}
	    	}
    	}
    }
    
    /** Checks if the player can be moved at the given coordinates, moves it if possible
     * 
     * @param num The number of the player
     * @param coord The coordinate the players wants to move to
     * @return True if the player can make the move, false otherwise
     */
    public boolean move (int num, Coordinates coord)
    {
    	Coordinates playerPos = playersPositions[num];
    	// If the target case is out of the board
    	if (coord.getY() >= cells.length || coord.getX() >= cells[0].length || coord.getX() < 0 || coord.getY() < 0)
    	{
    		throw new RuntimeException("Out of the board");
    	}
    	// If the target case is filled
    	if (cells[coord.getY()][coord.getX()].filled() > 0)
    		return false;
    	// If the player wants to move more than 4 cases away
    	if (Math.abs(coord.getX()-playerPos.getX()) > 4 || Math.abs(coord.getY()-playerPos.getY()) > 4)
    	{
    		throw new RuntimeException("Target case is too far from original position");
    	}
    	playerPos.move(coord.getX()-playerPos.getX(), coord.getY()-playerPos.getY());
    	return true;
    }
    
    /** Returns a copy of the coordinates of a player.
     * 
     * @param num The number of a player
     * @return The coordinates of this player
     */
    public Coordinates getCoordinates(int num)
    {
    	return playersPositions[num].clone();
    }
    
    /** Checks if a wall can be set at a given position
     * 
     * @param coord The coordinates where we try to set the wall
     * @return True if the wall can be set
     */
    private boolean tryWall(Coordinates coord)
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
    	if (x < 0 || x2 >= cells[0].length || y < 0 || y2 >= cells.length)
    	{
    		return false;
    	}
    	// If one of the case is filled, we can't set a wall
    	if ((filled(x, y) != 0 || filled(x2, y2) != 0 || filled((x+x2)/2, (y+y2) / 2) != 0) && (filled(x, y) != 2
    			|| filled(x2, y2) != 2 || filled((x+x2)/2, (y+y2) / 2) != 2))
    		return false;

    	return true;
    }
    
    // Shift coordinates to left/down 
    private Coordinates shift(Coordinates coord)
    {
    	Coordinates newCoord = coord.clone();
    	// x is even => vertical wall
    	if (coord.getX() % 2 == 0)
		{
    		// If inside the checkable part of the board
    		if (coord.getX() - 2 >= 0 && coord.getX() <= cells[0].length - 3)
    		{
    			// If the 2 cases on the right are filled
    			if(cells[coord.getY()][coord.getX()+1].filled() != 0 || cells[coord.getY()][coord.getX()+2].filled() != 0)
    			{
    				// If the 2 cases on the left are empty
    				if(cells[coord.getY()][coord.getX()-1].filled() == 0 && cells[coord.getY()][coord.getX()-2].filled() == 0)
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
    		if (coord.getY() - 2 >= 0 && coord.getY() <= cells.length - 3)
			{
    			// If the 2 cells below are filled
    			if(cells[coord.getY()+1][coord.getX()].filled() != 0 || cells[coord.getY()+2][coord.getX()].filled() != 0)
				{
    				// If the 2 cells above are empty
    				if(cells[coord.getY()-1][coord.getX()].filled() == 0 && cells[coord.getY()-2][coord.getX()].filled() == 0)
					{
    					// We can shift the coordinates
		    			newCoord = new Coordinates(coord.getX(), coord.getY()-2);
					}
				}
			}
		} 
    	return newCoord;
    }
    
    // Shift coordinates to right/up
    private Coordinates removeShift(Coordinates coord)
    {
    	Coordinates newCoord = coord.clone();
    	
    	// x even => vertical wall
    	if (coord.getX() % 2 == 0)
		{
    		if (coord.getX() - 2 >= 0 && coord.getX() <= cells[0].length - 3)
    		{
    			// If the 2 cases on the right are filled
    			if(cells[coord.getY()][coord.getX()+1].filled() != 0 || cells[coord.getY()][coord.getX()+2].filled() != 0)
    			{
    				// If the 2 cases on the left are translucent
    				if(cells[coord.getY()][coord.getX()-1].filled() == 2 && cells[coord.getY()][coord.getX()-2].filled() == 2)
					{
		    			newCoord = new Coordinates(coord.getX()-2, coord.getY());
					}
	    		}
    		}
		}
    	else if (coord.getX() % 2 == 1 && coord.getY() % 2 == 0)
		{
    		if (coord.getY() - 2 >= 0 && coord.getY() <= (cells.length - 3))
			{
    			// If the 2 cases on the right are filled
    			if (cells[coord.getY()+1][coord.getX()].filled() != 0 || cells[coord.getY()+2][coord.getX()].filled() != 0)
				{
    				// If the 2 cases above are translucent
    				if (cells[coord.getY()-1][coord.getX()].filled() == 2 && cells[coord.getY()-2][coord.getX()].filled() == 2)
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
