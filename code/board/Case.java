package board;


/** A Case is a Cell on which a player can walk
 * 
 * @author Thibaut De Cooman & Gaetan Staquet
 *
 */
public final class Case extends ACell
{
	/** Default constructor*/
    public Case()
    {
        super();
    }
    
    /** Constructor
     * 
     * @param filled The content of the case
     */
    public Case(int filled)
    {
    	super(filled);
    }
    
    @Override
    public void display()
    {
    	System.out.print(filled);
    }
}
