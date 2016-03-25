package players;

import java.text.ParseException;
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
	public Round(Type type, Coordinates coord)
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
	
	@Override
	public String toString()
	{
		String res = type + " " + coord;
		return res;
	}
	
	/** Parses a String and returns the corresponding Round.
	 * Can throw a ParseException if the String is incorrect.
	 * 
	 * @param string The String to parse
	 * @return The Round made from the string
     * @throws ParseException
	 */
	public static Round parse(String string) throws ParseException
	{
		Type type = Type.NONE;
		Coordinates coord = null;
		if (string.charAt(0) == 'M')
			type = Type.MOVE;
		else if (string.charAt(0) == 'W')
			type = Type.WALL;
		else if (string.charAt(0) == 'N')
			type = Type.NONE;
		else
			throw new ParseException("Invalid type", 0);
		
		coord = Coordinates.parse(string.substring(5));
		return new Round(type, coord);
	}
}
