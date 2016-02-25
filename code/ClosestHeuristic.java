/** This class implements IAStarHeuristic by using the Manhattan distance approximation
 * 
 * @author Gaëtan Staquet
 *
 */
public class ClosestHeuristic implements IAStarHeuristic {
	/** @see AStarHeuristic@getCost(Board, Player, int, int, int, int) */
	public float getCost(Cell[][] map, Player player, int x, int y, int tx, int ty)
	{
		// Uses the Manhat
		float dx = Math.abs(tx - x);
		float dy = Math.abs(ty - y);
		
		float result = dx+dy;
		
		return result;
	}
}
