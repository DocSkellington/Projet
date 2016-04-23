package pathFinder;

import java.util.*;

import board.*;
import players.*;

/** This class implements the A* algorithm.
 * 
 * Mainly inspired by : http://www.cokeandcode.com/main/tutorials/path-finding/
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class AStarPathFinder
{
    /** Will contain the nodes that have already been processed*/
    protected ArrayList<Node> closed = new ArrayList<Node>();
    /** Will contain the nodes that are accessible*/
    protected SortedList open = new SortedList();
    
    /** A reference to the board (to know where are walls and players)*/
    protected Board board;
    protected Node[][] nodes;
    /** The heuristic formula to be used to determine the best next step*/
    protected IAStarHeuristic heuristic;
    
    /** A basic constructor with the basic heuristic formula
     * @param board The board to be searched
     */
    public AStarPathFinder (Board board)
    {
        this(board, new ClosestHeuristic());
    }
    
    /** The constructor that allows to chose the heuristic formula
     * @param board The board to be searched
     * @param heuristic A reference to the class who contains the heuristic formula
     */
    public AStarPathFinder(Board board, IAStarHeuristic heuristic)
    {
        this.heuristic = heuristic;
        this.board = board;
        
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

    /** Find a path from (sx, sy), the starting location, to (tx, ty), the target location.
     * @param player The player that needs a path
	 * @param withPlayer If we consider the other player(s)
     * @param sx The x coordinate of the start location
	 * @param sy The y coordinate of the start location
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The path found from the starting position to the target (or null if no path can be found).
     */
    public Path findPath(APlayer player, boolean withPlayer, int sx, int sy, int tx, int ty)
    {
        // Init for A*
        nodes[sx][sy].cost = 0;
        closed.clear();
        open.clear();
        open.add(nodes[sx][sy]);
        
        nodes[tx][ty].parent = null;
        
        while(open.size() != 0)
        {
            // We take the first node (which is the most likely to be the next step)
            Node current = (Node) open.first();
            if(current == nodes[tx][ty])
                break;
            
            Coordinates coord = new Coordinates(current.getX(), current.getY());
                
            open.remove(current);
            closed.add(current);
            
            Coordinates[] neighbours = player.possibleMoves(board, withPlayer, coord);
            
            for (Coordinates neighbour : neighbours)
            {
                // Coordinates of the neighbour
                int xp = neighbour.getX(), yp = neighbour.getY();
                
            	// Cost to get to this node = cost so far + movement cost to reach this node.
                float nextStepCost = current.cost + getMovementCost(player, current.x, current.y, xp, yp);
                Node neigh = nodes[xp][yp];
                
                // If the new cost for this node is lower than it's already been calculated : we must check if the node can't be reached by an another path (which may be a better path)
                if (nextStepCost <= neigh.cost)
                {
                    if(open.contains(neigh))
                        open.remove(neigh);
                    if(closed.contains(neigh))
                        closed.remove(neigh);
                }
                
                // If the node hasn't already been processed and discarded (so it isn't in "closed") then reset its cost to the current cost && add it as a next possible step (i.e. we add it in open)
                if((!open.contains(neigh)) && (!closed.contains(neigh)))
                {
                	neigh.cost = nextStepCost;
                	neigh.heuristic = getHeuristicCost(xp, yp, tx, ty);
                	neigh.setParent(current);
                    open.add(neigh);
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
     * @param x The x coordinate of the tile whose cost is being determined
	 * @param y The y coordinate of the tile whose cost is being determined
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The heuristic cost assigned to the tile
	 */
	public float getHeuristicCost(int x, int y, int tx, int ty)
	{
	    return heuristic.getCost(x, y, tx, ty);
	}
	
	/** A simple sorted list (mainly uses the sort algorithm from Java API) */
	private class SortedList
	{
	    /** The list of elements */
	    private ArrayList<Node> list = new ArrayList<Node>();
	    
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
	    
	    /** Add a Node to the list (this implies using sorting)
	     * @param o The Node to add
	     */
	    public void add(Node o)
	    {
	        list.add(o);
	        Collections.sort(list);
	    }
	    
	    /** Remove a Node from the list
	     * @param o The Node to remove
	    */
	    public void remove(Node o)
	    {
	        list.remove(o);
	    }
	    
	    /** Get the number of nodes in the list
	     * @return The size of the list
	     */
	    public int size()
	    {
	        return list.size();
	    }
	    
	    /** Check if a Node is in the list
	     * @param o The Node to search for
	     * @return True if the element is in the list, false otherwise
	     */
	    public boolean contains(Node o)
	    {
	        return list.contains(o);
	    }
	}
	
	/** A single node in the search graph */
	private class Node implements Comparable<Node>
	{
	    private final int x, y;
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
	    
	    /** Gets the x coordinate */
	    public int getX()
	    {
	    	return x;
	    }
	    
	    /** Gets the y coordinate */
	    public int getY()
	    {
	    	return y;
	    }
	    
	    /** @see Comparable#compareTo(Object) */
	    public int compareTo(Node other)
	    {	        
	        float f = heuristic + cost, of = other.heuristic + other.cost;
	        
	        if (f < of)
	            return -1;
            else if (f > of)
                return 1;
            else
                return 0;
	    }
	}
}
