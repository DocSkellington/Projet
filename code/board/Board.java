package board;

import java.util.ArrayList;
import players.*;
import pathFinder.*;

/** Manages all information about the board.
 * This includes setting walls, finding path
 * @author Gaetan Staquet && Thibaut De Cooman
 *
 */
public class Board
{
    protected ACell[][] cells;
    protected Coordinates[] playersPositions;
    protected APlayer[] players;
    
    /** The constructor
     * @param numPlayers The number of players 
    */
    public Board(int numPlayers)
    {
        cells = new ACell[17][17];
        
        for (int x = 0 ; x < 17 ; x++)
        {
            for (int y = 0 ; y < 17 ; y++)
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
        
        cells[playersPositions[0].getY()][playersPositions[0].getX()-1].setFilled(1);
        cells[playersPositions[0].getY()][playersPositions[0].getX()+1].setFilled(1);
        //cells[9][8].setFilled(1);
        playersPositions[1] = new Coordinates(8, 10);
        playersPositions[0] = new Coordinates (8, 12);
        
        update();
    }
    
    /** Draws the board in the command prompt district */
    public void print()
    {
        System.out.print(" ");
        for(int i = 0 ; i < cells.length ; i++)
        {
            System.out.print("_");
        }
        System.out.println();
        
        for (int x = 0 ; x < cells.length ; x++)
        {
            System.out.print("|");
            for (int y = 0 ; y < cells.length ; y++)
            {
            	cells[x][y].display();
            }
            System.out.println("|");
        }
        
        System.out.print(" ");
        for(int i = 0 ; i < cells.length ; i++)
        {
            //System.out.print("\u0304");
        	System.out.print("-");
        }
        System.out.println();
    }
    
    /** Set the reference to the players
     * 
     * @param players An array of players
     */
    public void setPlayers(APlayer[] players)
    {
    	this.players = players;
    }
    
    /** Tries to set a wall at coordinate (x;y) (The upper/right vertex).
     * If possible, effectively sets the wall and returns true. Otherwise, returns false without modifying the board.
     * Uses the A* algorithm.
     * @param num The number of the player who wants to set a wall
     * @param coord The coordinates of the wall
     * @param horizontal If the wall is horizontal
     * @return The shortest path if possible, null otherwise
    */
    public boolean setWall(int num, Coordinates coord, boolean horizontal)
    {
    	int x = coord.getX(), y = coord.getY();
        if(x % 2 == 0 && y % 2 == 0)
            return false;
        if (x % 2 != 0 && y % 2 != 0)
        	return false;
        
        if (!tryWall(coord, horizontal))
        	return false;
        
        int x2 = x, y2 = y;
        if (horizontal)
        	x2 += 2;
        else
        	y2 += 2;
        
        // We set the wall
        cells[y][x].setFilled(1);
        cells[y2][x2].setFilled(1);
        
        // Is there still a path ?
        boolean none = false;
        for (APlayer player : players)
        {
        	Path path = findPath(player);
        	if (path == null)
        	{
        		none = true;
        		break;
        	}
        }
        if (none)
        {
        	// We destroy the walls
	        cells[y][x].setFilled(0);
	        cells[y2][x2].setFilled(0);
	        return false;
        }
        return true;
    }
    
    /** Finds the shortest path (uses the A* algorithm)
     * 
     * @param player A reference to the player who needs a path
     * @return The shortest path
     */
    public Path findPath(APlayer player)
    {
    	return findPath(player, new ClosestHeuristic());
    }
    
    /** Return the best path by using the A* algorithm (which uses the given heuristic method)
     * @param player A reference to the player who needs a path
     * @param heuri A reference to an heuristic class to determinate the best path
     * @return The best path if it exists, null otherwise
     */
    public Path findPath(APlayer player, IAStarHeuristic heuri)
    {
    	AStarPathFinder pathFinder = new AStarPathFinder(this, false, heuri);
    	Coordinates start = playersPositions[player.getNum()];
    	Coordinates[] target = goal(player.getNum());
    	for (int i = 0 ; i < target.length ; i++)
    	{
    		Path path = pathFinder.findPath(player, start.getX(), start.getY(), target[i].getX(), target[i].getY());
    		if (path != null)
    			return path;
    	}
    	return null;
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
                if (cells[i][j] instanceof Wall)
                    map[i][j] = new Wall(cells[i][j].filled());
                else
                    map[i][j] = new Case(cells[i][j].filled());
            }
        }
        return map;
    }
    
    /** Get the x size of the board
     * @return The x size of the board
     */
    public int getXSize()
    {
        return cells.length;
    }

    /** Get the y size of the board
     * @return The y size of the board
     */
    public int getYSize()
    {
        return cells[0].length;
    }
    
    /** Checks if a player has won
     * @param players The array of players
     * @return The number of the winner
     */
    public int hasWon(APlayer[] players)
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
    
    /** Check if there is wall between (sx, sy) and (tx, ty). These two cases must be adjacent.
     * 
     * @param sx The x coordinate of the starting position
     * @param sy The y coordinate of the starting position
     * @param tx The x coordinate of the target position
     * @param ty The y coordinate of the target position
     * @return
     */
    public boolean blocked(int sx, int sy, int tx, int ty)
    {
    	if (Math.abs(sx - tx) > 2 || Math.abs(sy - ty) > 2)
    	{
    		// TODO : Throw an exception
    		return true;
    	}
    	
    	if (filled((sx+tx)/2, (sy+ty)/2) != 0 || filled(tx, ty) != 0)
		{
    		return true;
		}
    	else
		{
    		return false;
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
    	
    	for (int i = 0 ; i < playersPositions.length ; i++)
    	{
    		Coordinates coord = playersPositions[i];
    		cells[coord.getY()][coord.getX()].setFilled(i + 1);
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
    	if (coord.getY() >= cells.length || coord.getX() >= cells.length || coord.getX() < 0 || coord.getY() < 0)
    	{
    		System.out.println("Out of the board");
    		return false;
    	}
    	// If the target case is filled
    	if (cells[coord.getY()][coord.getX()].filled() > 0)
    		return false;
    	// If the player wants to move more than 4 cases away
    	if (Math.abs(coord.getX()-playerPos.getX()) > 4 || Math.abs(coord.getY()-playerPos.getY()) > 4)
    	{
    		System.out.println("Target case is too far from original position");
    		return false;
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
    	return new Coordinates(playersPositions[num].getX(), playersPositions[num].getY());
    }
    
    private boolean tryWall(Coordinates coord, boolean horizontal)
    {
    	int x = coord.getX(), y = coord.getY(), x2 = x, y2 = y;
    	
    	if (horizontal)
    	{
    		x2 += 2;
    	}
    	else
    	{
    		y2 += 2;
    	}
    	
    	// In the board ?
    	if (x < 0 || x2 >= cells[0].length || y < 0 || y2 >= cells.length)
    	{
    		return false;
    	}
    	if (filled(x, y) != 0 || filled(x2, y2) != 0)
    		return false;
    	
    	return true;
    }
    
    /** Manages coordinates for a point */
    public static class Coordinates
    {
        private int x, y;
        
        /** Constructor
         * @param x The x position
         * @param y The y position
         */
        public Coordinates(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
        
        /** Gets the x coordinate 
         * @return The x coordinate
         */
        public int getX()
        {
        	return x;
        }
        
        /** Gets the y coordinate
         * @return The y coordinate
         */
        public int getY()
        {
        	return y;
        }
        
        /** Moves by dx and dy
         * 
         * @param dx The difference between the x coordinate of the starting position and the target position
         * @param dy The difference between the y coordinate of the starting position and the target position
         */
        public void move (int dx, int dy)
        {
        	this.x += dx;
        	this.y += dy;
        }
        
        @Override
        public String toString()
        {
        	String res = "(" + x + ", " + y + ")";
        	return res;
        }
        
        @Override
        public boolean equals(Object o)
        {
        	if (o == this)
        		return true;
        	if (o == null)
        		return false;
        	if(getClass() != o.getClass())
        		return false;
        	Coordinates other = (Coordinates) o;
        	if (other.x == this.x && this.y == other.y)
        		return true;
        	return false;
        }
        
        @Override
        public int hashCode()
        {
        	// TODO : Make sure there isn't twice the same hash
        	int hash = x + y;
        	return hash;
        }
    }

    /** The starting position for a player
     * 
     * @param playerNum The number of the player
     * @return The starting position (Coordinates) for the given player
     */
    public static Coordinates startingPos(int playerNum)
    {
		if (playerNum == 0)
			return new Coordinates(8, 16);
		else if (playerNum == 1)
			return new Coordinates(8,0);
		else if (playerNum == 2)
			return new Coordinates(0, 8);
		else
			return new Coordinates(16,8);
    }
    
    /** Returns the maximum number of players for this board
     * 
     * @return The maximum number of players
     */
    public static int maxPlayers()
    {
    	return 4;
    }
    
    /** Returns the goal coordinates for a given player
     * 
     * @param playerNum The number of the player
     * @return The goal coordinates (array) for this player
     */
    public static Coordinates[] goal(int playerNum)
    {
    	if (playerNum == 0)
        {
    		Coordinates[] goal = {new Coordinates(0,0), new Coordinates(2,0),new Coordinates(4,0),new Coordinates(6,0),new Coordinates(8,0),new Coordinates(10,0),new Coordinates(12,0),new Coordinates(14,0),new Coordinates(16,0)};
    		return goal;
        }
    	else if (playerNum == 1)
    	{
    		Coordinates[] goal = {new Coordinates(0,16), new Coordinates(2,16),new Coordinates(4,16),new Coordinates(6,16),new Coordinates(8,16),new Coordinates(10,16),new Coordinates(12,16),new Coordinates(14,16),new Coordinates(16,16)};
    		return goal;
    	}
    	else if (playerNum ==2)
    	{
    		Coordinates[] goal = {new Coordinates(16,0), new Coordinates(16,2),new Coordinates(16,4),new Coordinates(16,6),new Coordinates(16,8),new Coordinates(16,10),new Coordinates(16,12),new Coordinates(16,14),new Coordinates(16,16)};
    		return goal;
    	}
    	else
    	{
    		Coordinates[] goal = {new Coordinates(0,0), new Coordinates(0,2),new Coordinates(0,4),new Coordinates(0,6),new Coordinates(0,8),new Coordinates(0,10),new Coordinates(0,12),new Coordinates(0,14),new Coordinates(0,16)};
    		return goal;
    	}
    }
}
