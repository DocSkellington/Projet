package board;

/** Manages coordinates for a point 
 * 
 * @author Thibaut De Cooman & Gaetan Staquet
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
}
