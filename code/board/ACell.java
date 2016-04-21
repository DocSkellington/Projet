package board;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import gui.TextureHolder;

/** Cells compose the board
 * 
 * 
 * @author Thibaut De Cooman 
 * @author Gaetan Staquet
 *
 */
public abstract class ACell extends JButton implements Cloneable
{
    protected int filled;
	protected BufferedImage img;
    
    /** Default constructor */
    public ACell()
    {
    	this(0);
    }
    
    /** Constructor
     *  
     * @param filled Whether the cell is filled or not
     */
    public ACell(int filled)
    {
    	this.filled = filled;
		this.setBorder(null);
		this.setEnabled(false);
    }
    
    /** Fills the cell
     * 
     * @param f How the cell is filled (1 = filled, 0 = empty)
     */
    public void setFilled(int f)
    {
        filled = f;
    }
    
    @Override
    public void updateUI()
    {
    	super.updateUI();
    	this.setBorder(null);
    }
    
    /** Whether the cell is filled
     * 
     * @return Whether the cell is filled
     */
    public int filled()
    {
        return filled;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    }
    
    @Override
    public abstract ACell clone();
}
