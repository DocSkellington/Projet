package board;

import players.*;

/** Cells compose the board
 * 
 * 
 * @author Thibaut De Cooman & Gaetan Staquet
 *
 */
public abstract class ACell
{
    protected int filled;
    
    /** Default constructor*/
    public ACell()
    {
        filled = 0;
    }
    
    /** Constructor
     *  
     * @param filled Whether the cell is filled or not
     */
    public ACell(int filled)
    {
    	this.filled = filled;
    }
    
    /** Fills the cell
     * 
     * @param f How the cell is filled (1 = filled, 0 = empty)
     */
    public void setFilled(int f)
    {
        filled = f;
    }
    
    /** Whether the cell is filled
     * 
     * @return Whether the cell is filled
     */
    public int filled()
    {
        return filled;
    }
    
    /** Displays a cell */
    public abstract void display();
}
