package pathFinder;

import java.util.ArrayList;
import java.util.Collections;

import board.*;
import players.*;

/** This class implements IPathFinder by using the A* algorithm
 * 
 * Mainly inspired by : http://www.cokeandcode.com/main/tutorials/path-finding/
 * @author Gaetan Staquet & Thibaut De Cooman
 *
 */
public class AStarPathFinder implements IPathFinder
{
    /** Will contain the nodes that have already been processed*/
    protected ArrayList closed = new ArrayList();
    /** Will contain the nodes that are accessible*/
    protected SortedList open = new SortedList();
    
    /** A reference to the board (to know where are walls and players)*/
    protected Board board;
    protected Node[][] nodes;
    /** True if we can move diagonally*/
    protected boolean diagMovement;
    /** The heuristic formula to be used to determine the best next step*/
    protected IAStarHeuristic heuristic;
    
    /** A basic constructor with the basic heuristic formula
     * @param board The board to be searched
     * @param diagMovement True if we can move diagonally
     */
    public AStarPathFinder (Board board, boolean diagMovement)
    {
        this(board, diagMovement, new ClosestHeuristic());
    }
    
    /** The constructor that allows to chose the heuristic formula
     * @param board The board to be searched
     * @param diagMovement True if we can move diagonally
     * @param heuristic A reference to the class who contains the heuristic formula
     */
    public AStarPathFinder(Board board, boolean diagMovement, IAStarHeuristic heuristic)
    {
        this.heuristic = heuristic;
        this.board = board;
        this.diagMovement = diagMovement;
        
        int xSize = board.getXSize(), ySize = board.getYSize();
        
        nodes = new Node[xSize][ySize];
        for (int x = 0 ; x < xSize ; x++)
        {
            for (int y = 0 ; y < ySize ; y++)
            {
            	nodes[x][y] = new Node(x, y);
            }
        }
    }
    
    /** 
     * @see PathFinder#findPath(Player, int, int, int, int)
     */
    public Path findPath(APlayer player, int sx, int sy, int tx, int ty)
    {
        // Init for A*
        nodes[sx][sy].cost = 0;
        closed.clear();
        open.clear();
        open.add(nodes[sx][sy]);
        
        nodes[tx][ty].parent = null;
        ACell[][] map = board.getCells();
        
        while(open.size() != 0)
        {
            // We take the first node (which is the most likely to be the next step)
            Node current = (Node) open.first();
            if(current == nodes[tx][ty])
                break;
                
            open.remove(current);
            closed.add(current);
            
            // We go from x = -2 -> x = -4 because of our implementation of the map (one case on two is a wall)
            for (int x = -2 ; x < 4 ; x+=2)
            {
                for (int y = -2 ; y < 4 ; y+=2)
                {
                    // Not a neighbour => current tile => No need to calculate
                    if((x == 0) && (y == 0))
                        continue;
                        
                    // If we don't allow to move diagonally, skip this step
                    // TODO : Check if diagMovement is possible
                    if(!diagMovement)
                    {
                        if((x != 0) && (y != 0))
                            continue;
                    }
                    
                    // Coordinates of the neighbour
                    int xp = x + current.x, yp = y + current.y;
                    
                    if (isValidLocation(player, map, current.x, current.y, xp, yp))
                    {
                        // Cost to get to this node = cost so far + movement cost to reach this node.
                        float nextStepCost = current.cost + getMovementCost(player, current.x, current.y, xp, yp);
                        Node neighbour = nodes[xp][yp];
                        
                        // If the new cost for this node is lower than it's already been calculated : we must check if the node can't be reached by an another path (which may be a better path)
                        if (nextStepCost <= neighbour.cost)
                        {
                            if(open.contains(neighbour))
                                open.remove(neighbour);
                            if(closed.contains(neighbour))
                                closed.remove(neighbour);
                        }
                        
                        // If the node hasn't already been processed and discarded (so it isn't in "closed") then reset its cost to the current cost && add it as a next possible step (i.e. we add it in open)
                        if((!open.contains(neighbour)) && (!closed.contains(neighbour)))
                        {
                            neighbour.cost = nextStepCost;
                            neighbour.heuristic = getHeuristicCost(player, map, xp, yp, tx, ty);
                            neighbour.setParent(current);
                            open.add(neighbour);
                        }
                    }
                }
            }
        }
        
        // We've run out of search => there isn't any path => return null
        if(nodes[tx][ty].parent == null)
        {
        	return null;
        }
            
        // If we get so far, we have found a path. So, we can build our path (from the target to the starting location)
        Path path = new Path();
        Node target = nodes[tx][ty];
        while (target != nodes[sx][sy])
        {
            path.prependStep(target.x, target.y);
            target = target.parent;
        }
        path.prependStep(sx, sy);
        
        return path;
    }
    
    /** Check if the given location is valid for the given player
     * @param player The player who needs a path
     * @param map The map data
     * @param sx The starting x coordinate
     * @param sy The starting y coordinate
     * @param x The x coordinate of the location to be checked
     * @param y The y coordinate of the location to be checked
     * @retrun True if the location is valid for the given player
     */
    protected boolean isValidLocation(APlayer player, ACell[][] map, int sx, int sy, int x, int y)
    {
        boolean invalid = (x < 0) || (y < 0) || (x >= map.length) || (y >= map[0].length);
        
        if((!invalid) && ((sx != x) || (sy != y)))
        {
        	if(board.blocked(sx, sy, x, y))
        	{
        		invalid = true;
        	}
        	else
    			invalid = false;
        }

        return !invalid;
    }
    
    /** Get the cost to move through a given location
     * @param player The player who tries to move
     * @param sx The x coordinate of the tile whose cost is being determined
     * @param sy The y coordinate of the tile whose cost is being determined
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The cost of movement through the given location
	 */
    public float getMovementCost(APlayer player, int sx, int sy, int tx, int ty)
    {
        return board.getCost(player, sx, sy, tx, ty);
    }
    
    /** Get the heuristic cost for the given location. This will be used to determinate the order of the locations.
     * @param player The player who wants a path
     * @param map The map data
     * @param x The x coordinate of the tile whose cost is being determined
	 * @param y The y coordinate of the tile whose cost is being determined
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The heuristic cost assigned to the tile
	 */
	public float getHeuristicCost(APlayer player, ACell[][] map, int x, int y, int tx, int ty)
	{
	    return heuristic.getCost(map, player, x, y, tx, ty);
	}
	
	/** A simple sorted list (mainly uses the sort algorithm from Java API) */
	private class SortedList
	{
	    /** The list of elements */
	    private ArrayList list = new ArrayList();
	    
	    /** Returns the first element in the list
	     * @return The first element from the list
	     */
	    public Object first()
	    {
	        return list.get(0);
	    }
	    
	    /** Empty the list */
	    public void clear()
	    {
	        list.clear();
	    }
	    
	    /** Add an element to the list (this implies using sorting)
	     * @param o The element to add
	     */
	    public void add(Object o)
	    {
	        list.add(o);
	        Collections.sort(list);
	    }
	    
	    /** Remove an element from the list
	     * @param o The element to remove
	    */
	    public void remove(Object o)
	    {
	        list.remove(o);
	    }
	    
	    /** Get the number of elements in the list
	     * @return The size of the list
	     */
	    public int size()
	    {
	        return list.size();
	    }
	    
	    /** Check if an element is in the list
	     * @param o The element to search for
	     * @return True if the element is in the list, false otherwise
	     */
	    public boolean contains(Object o)
	    {
	        return list.contains(o);
	    }
	}
	
	/** A single node in the search graph */
	private class Node implements Comparable
	{
	    private int x, y;
	    private float cost, heuristic;
	    private Node parent;
	    
	    /** The constructor
	     * @param x The x coordinate of the node
	     * @param y The y coordinate of the node
	     */
	    public Node(int x, int y)
	    {
	        this.x = x;
	        this.y = y;
	    }
	    
	    /** Set the parent of this node
	     * @param parent The parent node which lead to the node
	     */
	    public void setParent(Node parent)
	    {
	        this.parent = parent;
	    }
	    
	    /** @see Comparable#compareTo(Object) */
	    public int compareTo(Object other)
	    {
	        Node o = (Node) other;
	        
	        float f = heuristic + cost, of = o.heuristic + o.cost;
	        
	        if (f < of)
	            return -1;
            else if (f > of)
                return 1;
            else
                return 0;
	    }
	}
}