package pathFinder;

import board.*;
import players.*;

/** This class implements IAStarHeuristic by using the Manhattan distance approximation
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class ClosestHeuristic implements IAStarHeuristic {
	@Override
	public float getCost(ACell[][] map, APlayer player, int x, int y, int tx, int ty)
	{
		// Uses the Manhattan method
		float dx = Math.abs(tx - x);
		float dy = Math.abs(ty - y);
		
		float result = dx+dy;
		
		return result;
	}
}
