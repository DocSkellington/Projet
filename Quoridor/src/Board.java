import java.util.ArrayList;

/** Manages all information about the board.
 * This includes setting walls, finding path
 * @author Gaëtan Staquet && Thibaut De Cooman
 *
 */
public class Board
{
    protected Cell[][] cells;
    protected Coordinates[] playersPositions;
    
    /** The basic constructor
    */
    public Board()
    {
        cells = new Cell[17][17];
        
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
        
        cells[2][4].setFilled(1);
        cells[2][6].setFilled(2);
        for (int i = 1 ; i < 17 ; i+=2)
        {
        	cells[2][i].setFilled(0);
        }
        for (int i = 1 ; i <= 3 ; i+=2)
        {
        	for(int j = 0 ; j < 17 ; j++)
        	{
        		cells[i][j].setFilled(0);
        	}
        }
        
        System.out.println(cells[6][1].filled());
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
                if (cells[x][y] instanceof Wall)
                {
                    if(cells[x][y].filled() == 0)
                        System.out.print(" ");
                    else
                        System.out.print("#");
                }
                else
                {
                    // 1 = J1
                    if (cells[x][y].filled() == 1)
                        System.out.print("1");
                    else if (cells[x][y].filled() == 2)
                        System.out.print("2");
                    else
                        System.out.print("0");
                }
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
    
    /** Tries to set a wall at coordinate (x;y) (The upper/right vertex).
     * If possible, effectively sets the wall and returns true. Otherwise, returns false without modifying the board.
     * Uses the A* algorithm.
     * @param x The x coordinate
     * @param y The y coordinate
     * @param horizontal If the wall is horizontal
     * @return The shortest path if possible, null otherwise
    */
    public Path setWall(int x, int y, boolean horizontal)
    {
        if(cells[x][y] instanceof Wall)
            return null;
        
        Player player = new Player();
        ClosestHeuristic heuris = new ClosestHeuristic();
        AStarPathFinder pathFinder = new AStarPathFinder(this, false, heuris);
        Path path = pathFinder.findPath(player, 0, 6, 8, 6);
        return path;
    }
    
    /** Finds the shortest path (uses the A* algorithm)
     * 
     * @param player A reference to the player who needs a path
     * @return The shortest path
     */
    public Path findPath(Player player)
    {
    	AStarPathFinder pathFinder = new AStarPathFinder(this, false);
    	Coordinates start = playersPositions[player.getNum()], target = getTarget(player.getNum());
    	return pathFinder.findPath(player, start.getX(), start.getY(), target.getX(), target.getY());
    }
    
    /** Return the best path by using the A* algorithm (which uses the given heuristic method)
     * @param player A reference to the player who needs a path
     * @param heuri A reference to an heuristic class to determinate the best path
     * @return The best path if it exists, null otherwise
     */
    public Path findPath(Player player, IAStarHeuristic heuri)
    {
    	AStarPathFinder pathFinder = new AStarPathFinder(this, false, heuri);
    	Coordinates start = playersPositions[player.getNum()], target = getTarget(player.getNum());
    	return pathFinder.findPath(player, start.getX(), start.getY(), target.getX(), target.getY());
    }
    
    public Coordinates[] goal(int player)
    {
        Coordinates[] goal = {new Coordinates(0,0), new Coordinates(0,2),new Coordinates(0,4),new Coordinates(0,6),new Coordinates(0,8),new Coordinates(0,10),new Coordinates(0,12),new Coordinates(0,14),new Coordinates(0,16)};
        return goal;
    }
    
    /** Return the content of a cell
     * @param x The x coordinate of a cell
     * @param y The y coordinate of a cell
     * @return The content of the cell
     */
    public int filled(int x, int y)
    {
        if(x >= 0 && x < cells.length && y >= 0 && y < cells[0].length)
            return cells[x][y].filled();
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
    public float getCost(Player player, int sx, int sy, int tx, int ty)
    {
        return 1.f;
    }
    
    /** Gets a copy of the array of cells
     * @return An array of array of Cells
     */
    public Cell[][] getCells()
    {
        // We don't want to give a reference to the data, so we copy the array
        Cell[][] map = new Cell[cells.length][cells[0].length];
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
    	
    	if (filled((sx+tx)/2, (sy+ty)/2) == 1)
		{
			return true;
		}
    	else
			return false;
    }
    
    /** Manages coordinates for a point */
    public class Coordinates
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
    }

}
