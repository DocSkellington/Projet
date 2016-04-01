package board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import gui.TextureHolder;

/** A Case is a Cell on which a player can walk
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */
public final class Case extends ACell
{
	/* Constructor
	 * 
     * @param holder The holder of all needed textures
     */
    public Case(TextureHolder holder)
    {
        super(holder);
    }
    
    /** Constructor
     * 
     * @param filled The content of the case
     * @param holder The holder of all needed textures
     */
    public Case(TextureHolder holder, int filled)
    {
    	super(holder, filled);
    }
    
    @Override
    public void display()
    {
    	System.out.print(filled);
    }
    
    @Override
    public Case clone()
    {
    	if (holder == null)
    		return new Case(null, filled);
    	return new Case(holder.clone(), filled);
    }
    
    @Override
    public Dimension getPreferredSize()
    {
    	return new Dimension(0, 0);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	g.drawImage(holder.get("case"), 0, 0, null);
    }
}
