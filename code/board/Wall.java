package board;

import java.awt.Dimension;
import java.awt.Graphics;

import gui.TextureHolder;

/** Walls are obstacles set on the board by the players
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */
public final class Wall extends ACell
{
	/* Constructor
	 * 
     * @param holder The holder of all needed textures
     */
    public Wall(TextureHolder holder)
    {
        super(holder);
    }
    
    /** Constructor
     * 
     * @param filled The content of the case
     * @param holder The holder of all needed textures
     */
    public Wall(TextureHolder holder, int filled)
    {
    	super(holder, filled);
    }
    
    @Override
    public void display()
    {
    	if (filled == 0)
    		System.out.print(" ");
    	else
    		System.out.print("#");
    }
    
    @Override
    public Wall clone()
    {
    	if (holder == null)
    		return new Wall(null, filled);
    	return new Wall(holder.clone(), filled);
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
    	if (filled == 0)
    		g.drawImage(holder.get("wallEmpty"), 0, 0, null);
    	else
    		g.drawImage(holder.get("wallFilled"), 0, 0, null);
    }
}
