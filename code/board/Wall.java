package board;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import game.Game;
import gui.TextureHolder;

/** Walls are obstacles set on the board by the players
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */
public final class Wall extends ACell
{
	private float alpha;
	private boolean destroy;
	
	/** Constructor */
    public Wall()
    {
    	this(0);
    }
    
    /** Constructor
     * 
     * @param filled The content of the case
     */
    public Wall(int filled)
    {
    	super(filled);
    	repaint();
    	destroy = false;
    }
    
    /** Sets destroy
     * 
     * @param destroy The new value of destroy
     */
    public void setDestroy(boolean destroy)
    {
    	this.destroy = destroy;
    }
    
    /** Gets destroy
     * 
     * @return destroy
     */
    public boolean getDestroy()
    {
    	return destroy;
    }
    
    @Override
    public String toString()
    {
    	if (filled == 0)
    		return " ";
    	else
    		return "#";
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
	    		// If the mouse has entered the wall cell, we throw an event to light on a wall
	    		String command = this.getActionCommand();
	    		a = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "l " + command);
	    	}
	    	else if (e.getID() == MouseEvent.MOUSE_EXITED)
	    	{
	    		// If the mouse has exited the wall cell, we throw an event to switch off the wall
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
    	return new Wall(filled);
    }
    
    @Override
    public Dimension getPreferredSize()
    {
    	return new Dimension(0, 0);
    }
    
    @Override
    public void repaint()
    {
    	super.repaint();
    	
    	// We choose the texture according to filled value
    	switch (filled)
    	{
    	case 0:
    		img = Game.getImage("wallEmpty");
    		alpha = 1.0f;
    		break;
    	case 1:
    		img = Game.getImage("wallFilled");
    		alpha = 1.0f;
    		break;
    	case 2:
    		img = Game.getImage("wallFilled");
    		alpha = 0.3f;
    		break;
    	case 3:
    		img = Game.getImage("wallEmpty");
    		alpha = 1.0f;
    		break;
		default:
			throw new RuntimeException("Invalid number: " + filled);
    	}
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	
    	Graphics2D g2D = (Graphics2D) g;

		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    	g2D.drawImage(img, 0, 0, null);
    }
}
