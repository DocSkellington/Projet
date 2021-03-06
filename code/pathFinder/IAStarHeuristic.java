package pathFinder;

/** The description of a class providing a cost for a given map.
 * This heuristic controls what priority is placed on different tiles during the search for a path 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 */
public interface IAStarHeuristic {
	
	/** Get the additional heuristic cost of the given tile. The lower the cost, the more likely the tile will be searched.
	 * @param x The x coordinate of the tile being evaluated
	 * @param y The y coordinate of the tile being evaluated
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The cost associated with the given tile
	 */
	public float getCost(int x, int y, int tx, int ty);
}
