package pathFinder;

import java.util.ArrayList;

import board.*;
import players.*;

/** Manages the paths
 * 
 * @author Gaetan Staquet & Thibaut De Cooman
 *
 */
public class Path
{
    private ArrayList<Step> steps = new ArrayList<Step>();
    
    /** Default constructor */
    public Path()
    {
    }
    
    public String toString()
    {
    	String res = "";
    	for (Step step : steps)
    	{
    		res += (step + "\n");
    	}
    	return res;
    }
    
    /** Gets the length of the path
     * 
     * @return the length of the path
     */
    public int getLength()
    {
        return steps.size();
    }
    
    /** Gets the step at a specific index
     * 
     * @param index The index of the step
     * @return The step at the given index
     */
    public Step getStep(int index)
    {
        return (Step) steps.get(index);
    }
    
    /** Gets the x coordinate of the step at a specific index
     * 
     * @param index The index of the step
     * @return The x coordinate of the step at the given index
     */
    public int getX(int index)
    {
        return getStep(index).x;
    }

    /** Gets the y coordinate of the step at a specific index
     * 
     * @param index The index of the step
     * @return The y coordinate of the step at the given index
     */
    public int getY(int index)
    {
        return getStep(index).y;
    }
    
    /** Adds a step of value (x, y) at the end of the path
     * 
     * @param x The x coordinate of the step
     * @param y The y coordinate of the step
     */
    public void appendStep(int x, int y)
    {
        steps.add(new Step(x, y));
    }
    
    /** Adds  a step of value (x, y) at the beginning of the path
     * 
     * @param x The x coordinate of the step
     * @param y The y coordinate of the step
     */
    public void prependStep(int x, int y)
    {
        steps.add(0, new Step(x, y));
    }
    
    /** Checks if the step of value (x, y) is in the path
     * 
     * @param x The x coordinate of the step
     * @param y The y coordinate of the step
     * @return True if the step is in the path, false otherwise
     */
    public boolean contains(int x, int y)
    {
        return steps.contains(new Step(x, y));
    }
    
    /** Manages a step of a path
     * 
     * @author Gaetan Staquet & Thibaut De Cooman
     *
     */
    public class Step
    {
        private int x, y;
        
        /** The constructor of a Step
         * 
         * @param x The x coordinate of the step
         * @param y The y coordinate of the step
         */
        public Step(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
        
        /** Gets the x coordinate of the step
         * 
         * @return The x coordinate of the step
         */
        public int getX()
        {
            return x;
        }

        /** Gets the y coordinate of the step
         * 
         * @return The y coordinate of the step
         */
        public int getY()
        {
            return y;
        }
        
        public String toString()
        {
        	String res = x + " " + y;
        	return res;
        }
        
        public int hashCode()
        {
            return x*y;
        }
        
        /** 
         * @see: Object@equals
         */
        public boolean equals(Object other)
        {
            if (other instanceof Step)
            {
                Step o = (Step) other;
                return (o.x == x) && (o.y == y);
            }
            return false;
        }
    }
}
