package board;

import java.util.ArrayList;

import javax.swing.JPanel;

import pathFinder.AStarPathFinder;
import pathFinder.ClosestHeuristic;
import pathFinder.IAStarHeuristic;
import pathFinder.Path;
import players.APlayer;

public abstract class ABoard
{
    protected ArrayList<ArrayList<ACell>> cells;
    protected Coordinates[] playersPositions;
    protected ArrayList<Coordinates> placedWalls;
    protected APlayer[] players;
    
    public static final double tick = 333.333;
    public static double prev_tick = 0.00;
    
    @Override
    public abstract String toString();

    /** Draws the board in the command prompt district */
    public void print()
    {
    	System.out.println(toString());
    }

    /** Fills the panel with every cell/button. Also sets the listeners
     * 
     * @param panel The panel to fill
     */
    public abstract void fill (JPanel panel);

    
    /** Sets a wall if it can
     * 
     * @param coord The coordinates of the wall
     * @return True if the wall is set, false otherwise.
     */
    public boolean setWall(Coordinates coord)
    {
    	// If outside the board
    	if (coord.getX() < 0 || coord.getY() < 0 || coord.getX() > cells.get(0).size() - 1 || coord.getY() > cells.size() - 1)
    		return false;
    	
    	// If the coord is on the edge of the board, we shift from 2 cells
    	if (coord.getX() == cells.get(0).size()-1)
    		coord = new Coordinates(coord.getX() - 2, coord.getY());
    	if (coord.getY() == cells.size()-1)
    		coord = new Coordinates(coord.getX(), coord.getY() - 2);
    	
    	// If the cell is already filled by a wall, we can't set a wall here.
    	if (cells.get(coord.getY()).get(coord.getX()).filled() == 1)
    		return false;
    	
    	// We remove the translucent wall (gui)
    	coord = removeShift(coord);
    	colorAdjacentWalls(coord, false);
    	coord = shift(coord);
    	
        // If we can set the wall
    	if(canSetWall(coord))
    	{
    		fillWall(coord, 1);
    		
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
        
        fillWall(coord, 1);
        
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
        fillWall(coord, 0);
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
    	if (coord.getX() < 0 || coord.getX() >= cells.get(0).size() || coord.getY() < 0 || coord.getY() >= cells.size())
    	{
    		throw new RuntimeException("Out of the board");
    	}
    	
    	if (filled(coord) != 0 && filled(coord) != 2)
    		return true;
    	return false;
    }

    /** Destroys the wall at the given position
     * 
     * @param coord The coordinates of the wall to destroy
     * @return If we have successfully destroy the wall, or not
     */
    public boolean destroyWall(Coordinates coord)
    {
    	if (!hasWall(coord))
    		return false;
    	
    	int x = coord.getX(), y = coord.getY();
    	if (coord.getX() == cells.get(0).size()-1)
    		x -= 2;
    	if (coord.getY() == cells.size()-1)
    		y -= 2;
    	
    	if (placedWalls.contains(new Coordinates(x, y)))
    	{
    		int x2 = 0, y2 = 0;
    		
    		if (x % 2 == 0)
    			x2 = 2;
    		else
    			y2 = 2;

            cells.get(y).get(x).setFilled(0);
            cells.get((y2+y)/2).get((x2+x)/2).setFilled(0);
            cells.get(y2).get(x2).setFilled(0);
            
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
    	if(x >= 0 && x < cells.get(0).size() && y >= 0 && y < cells.size())
        {
        	return cells.get(y).get(x).filled();
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

    /** Gets a copy of the array of cells
     * @return An array of array of Cells
     */
    public ArrayList<ArrayList<ACell>> getCells()
    {
        // We don't want to give a reference to the data, so we copy the array
    	ArrayList<ArrayList<ACell>> map = new ArrayList<ArrayList<ACell>>();
        for (int i = 0 ; i < map.size() ; i++)
        {
        	ArrayList<ACell> row = new ArrayList<ACell>();
            for (int j = 0 ; j < map.get(i).size() ; j++)
            {
                // Since copying an object would simply copy the reference, we create new cells
                row.add(cells.get(i).get(j).clone());
            }
            map.add(row);
        }
        return map;
    }
    
    /** Gets the number of Cases of the board as Coordinates
     * 
     * @return The number of cases in a Coordinates
     */
    public abstract Coordinates getSize();
    
    /** Get the number of cells of the row ofthe board
     * @return The x size of the board
     */
    public int getXSize(int row)
    {
        return cells.get(row).size();
    }

    /** Get the y size of the board
     * @return The y size of the board
     */
    public int getYSize()
    {
        return cells.size();
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
    public abstract Coordinates startingPos(int playerNum);
    
    /** Returns the goal coordinates for a given player
     * 
     * @param playerNum The number of the player
     * @return The goal coordinates (array) for this player
     */
    public abstract Coordinates[] goal(int playerNum);

    /** Updates the cells */
    public void update ()
    {
    	// We reset cases
    	for (int i = 0 ; i < cells.size() ; i+=2)
    	{
    		for (int j = 0 ; j < cells.get(i).size() ; j+=2)
    		{
    			cells.get(i).get(j).setFilled(0);
    		}
    	}
    	
    	// We fill the cases with a player on them
    	for (int i = 0 ; i < playersPositions.length ; i++)
    	{
    		Coordinates coord = playersPositions[i];
    		cells.get(coord.getY()).get(coord.getX()).setFilled(i + 1);
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
	    	for (int i = 0 ; i < cells.size() ; i++)
	    	{
	    		for (int j = 0 ; j < cells.get(i).size() ; j++)
	    		{
	    			cells.get(i).get(j).repaint();
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
    		cells.get(coord[i].getY()).get(coord[i].getX()).setEnabled(enabled);
    	}
    }

    /** Disable every button */
    public void disableAll()
    {
    	for (int i = 0 ; i < cells.size() ; i++)
    	{
    		for (int j = 0 ; j < cells.get(0).size() ; j++)
    		{
    			cells.get(i).get(j).setEnabled(false);
    		}
    	}
    }

    /** Enable all possible walls buttons
     * 
     * @param destroy If we want to destroy the walls
     */
    public void enableWalls(boolean destroy)
    {
    	for (int i = 0 ; i < cells.size() ; i++)
    	{
    		for (int j = 0 ; j < cells.get(i).size() ; j++)
    		{
    			if (i % 2 != j % 2)
    			{
    				if (destroy)
    				{
    					if (hasWall(new Coordinates(j, i)))
    					{
    						cells.get(i).get(j).setEnabled(true);
    						((Wall)cells.get(i).get(j)).setDestroy(true);
    					}
    				}
    				else
    				{
    					cells.get(i).get(j).setEnabled(true);
						((Wall)cells.get(i).get(j)).setDestroy(false);
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
    public abstract void colorAdjacentWalls(Coordinates coord, boolean coloured);

    /** Uncolors the adjacent walls while hovering
     * 
     * @param coord The coordinates at which we must uncolour the wall
     * @param coloured Whether the wall must be coloured or not
     */
    public abstract void colorAdjacentWallsDestroy(Coordinates coord, boolean coloured);
    
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
    	if (coord.getY() >= cells.size() || coord.getX() >= cells.get(0).size() || coord.getX() < 0 || coord.getY() < 0)
    	{
    		throw new RuntimeException("Out of the board");
    	}
    	// If the target case is filled
    	if (cells.get(coord.getY()).get(coord.getX()).filled() > 0)
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
    
    // Fills the walls corresponding to the coord with fill
    protected abstract void fillWall(Coordinates coord, int fill);

    /** Checks if a wall can be set at a given position
     * 
     * @param coord The coordinates where we try to set the wall
     * @return True if the wall can be set
     */
    protected abstract boolean tryWall(Coordinates coord);
    
    // Shift coordinates to left/down 
    protected abstract Coordinates shift(Coordinates coord);
    // Shift coordinates to right/up
    protected abstract Coordinates removeShift(Coordinates coord);
}
