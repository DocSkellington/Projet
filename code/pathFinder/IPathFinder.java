package pathFinder;

import players.*;

/** A description of an implementation that can find a path from one location on a map to another
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 */
public interface IPathFinder
{
    /** Find a path from (sx, sy), the starting location, to (tx, ty), the target location.
     * @param player The player that needs a path
	 * @param withPlayer If we consider the other player(s)
     * @param sx The x coordinate of the start location
	 * @param sy The y coordinate of the start location
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The path found from the starting position to the target (or null if no path can be found).
     */
    public Path findPath(APlayer player, boolean withPlayer, int sx, int sy, int tx, int ty);
}
