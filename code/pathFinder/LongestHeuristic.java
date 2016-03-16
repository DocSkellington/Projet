package pathFinder;

import players.*;
import board.*;

/** This heuristic is used to get the longest path (while avoiding going back)
 * 
 * @author Gaetan Staquet
 *
 */
public class LongestHeuristic implements IAStarHeuristic
{
	@Override
	public float getCost(ACell[][] map, APlayer player, int x, int y, int tx, int ty)
	{
		
		return 0;
	}

}
