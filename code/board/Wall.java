package board;

import players.*;

/** Walls are obstacles set on the board by the players
 * 
 * @author Thibaut De Cooman & Gaetan Staquet
 *
 */
public class Wall extends Cell
{
	
	/** Default constructor*/
    public Wall()
    {
        super();
    }
    
    /** Constructor
     * 
     * @param filled Whether the wall cell is filled
     */
    public Wall(int filled)
    {
    	super(filled);
    }
}
