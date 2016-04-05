package board;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

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
    public void processMouseEvent(MouseEvent e)
    {
    	if (this.isEnabled())
    	{
    		super.processMouseEvent(e);
	    	ActionEvent a = null;
	    	if (e.getID() == MouseEvent.MOUSE_ENTERED)
	    	{
	    		String command = this.getActionCommand();
	    		a = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "l " + command);
	    	}
	    	else if (e.getID() == MouseEvent.MOUSE_EXITED)
	    	{
	    		String command = this.getActionCommand();
	    		a = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "u " + command);
	    	}
	    	
	    	if (a != null)
	    	{
	    		for (ActionListener al : getActionListeners())
	    		{
	    			al.actionPerformed(a);
	    		}
	    	}
    	}
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
    	
    	Graphics2D g2D = (Graphics2D) g;
    	
    	if (filled == 0)
    		g2D.drawImage(holder.get("wallEmpty"), 0, 0, null);
    	else if (filled == 1)
    		g2D.drawImage(holder.get("wallFilled"), 0, 0, null);    	
    	else if (filled == 2)
    	{
    		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
    		g2D.drawImage(holder.get("wallFilled"), 0, 0, null);
    	}
    }
}
