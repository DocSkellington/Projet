package players;

import board.Coordinates;

/** One action of a player
 * 
 * @author Staquet Gaetan
 * @author De Cooman Thibaut
 *
 */
public final class Round
{
	private Type type;
	private Coordinates coord;
	
	/** Describe the Type of a round*/
	public enum Type
	{
		NONE,
		MOVE,
		WALL;
	}
	
	/** Constructor
	 * 
	 * @param type The round type
	 * @param coord The target coordinates
	 */
	Round(Type type, Coordinates coord)
	{
		this.type = type;
		this.coord = coord;
	}
	
	/** Gets the type
	 * 
	 * @return The round type
	 */
	public Type getType()
	{
		return this.type;
	}
	
	/** Gets the coordinates
	 * 
	 * @return The target coordinates
	 */
	public Coordinates getCoord()
	{
		return coord.clone();
	}
}
