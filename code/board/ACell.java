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
    protected TextureHolder holder;
	protected BufferedImage img;
    
    /** Default constructor
     * 
     * @param holder The holder of all needed textures
     * */
    public ACell(TextureHolder holder)
    {
        filled = 0;
        this.holder = holder;
		this.setBorder(null);
		this.setEnabled(false);
    }
    
    /** Constructor
     *  
     * @param filled Whether the cell is filled or not
     * @param holder The holder of all needed textures
     */
    public ACell(TextureHolder holder, int filled)
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
    
    @Override
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    }
    
    @Override
    public abstract ACell clone();
    
    /** Displays a cell */
    public abstract void display();
}
