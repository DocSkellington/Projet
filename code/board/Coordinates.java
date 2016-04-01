package board;

import java.text.ParseException;

/** Manages coordinates for a point 
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 * 
 */
public final class Coordinates implements Cloneable, Comparable<Coordinates>
{
    private int x, y;
    public static int size;
    
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
    
    /** Moves by dx and dy
     * 
     * @param dx The difference between the x coordinate of the starting position and the target position
     * @param dy The difference between the y coordinate of the starting position and the target position
     */
    public void move (int dx, int dy)
    {
    	this.x += dx;
    	this.y += dy;
    }
    
    @Override
    public String toString()
    {
    	String res = "(" + x + ", " + y + ")";
    	return res;
    }
    
    @Override
    public boolean equals(Object o)
    {
    	if (o == this)
    		return true;
    	if (o == null)
    		return false;
    	if(getClass() != o.getClass())
    		return false;
    	Coordinates other = (Coordinates) o;
    	if (other.x == this.x && this.y == other.y)
    		return true;
    	return false;
    }
    
    @Override
    public int compareTo(Coordinates other)
    {    	
    	if (other.x < x)
    		return 1;
    	if (other.x > x)
    		return -1;
    	if (other.y < y)
    		return 1;
    	if (other.y > y)
    		return -1;
    	return 0;
    }
    
    @Override
    public int hashCode()
    {
    	int hash = size * x + y;
    	return hash;
    }
    
    @Override
    public Coordinates clone()
    {
    	return new Coordinates(x, y);
    }
    
    /** Parses a string and returns the corresponding coordinates
     * Can throw a ParseException if the string is incorrect
     * 
     * @param string The string to parse
     * @return The coordinates made by the string
     * @throws ParseException If the string cannot be parsed
     */
    public static Coordinates parse(String string) throws ParseException
    {
    	if (string.charAt(0) != '(')
    		throw new ParseException("Coordinates must start with a \'(\'", 0);
    	// We look for the comma in the coordinates
    	int i = 1;
    	while (i < string.length())
    	{
    		if (string.charAt(i) == ',')
    			break;
    		i++;
    	}
    	if (i == string.length())
    		throw new ParseException("Missing \',\'", i);
    	String ss = string.substring(1, i);
    	int x = Integer.parseInt(ss);
    	
    	if (string.charAt(i+1) != ' ')
    		throw new ParseException("A Space is needed", i+1);
    	
    	// We look for the closing bracket
    	int j = i;
    	while (j < string.length())
    	{
    		if (string.charAt(j) == ')')
    			break;
    		j++;
    	}
    	if (j == string.length())
    		throw new ParseException("Missing \')\'", j);
    	ss = string.substring(i+2, j);
    	int y = Integer.parseInt(ss);
    	
    	return new Coordinates(x, y);
    }
}
