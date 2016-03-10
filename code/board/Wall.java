package board;


/** Walls are obstacles set on the board by the players
 * 
 * @author Thibaut De Cooman & Gaetan Staquet
 *
 */
public final class Wall extends ACell
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
    
    @Override
    public void display()
    {
    	if (filled == 0)
    		System.out.print(" ");
    	else
    		System.out.print("#");
    }
}
