package board;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import gui.TextureHolder;

/** A Case is a Cell on which a player can walk
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */
public final class Case extends ACell
{
	private float alpha;
	private Color player;
	/** Constructor
	 * 
     * @param holder The holder of all needed textures
     */
    public Case(TextureHolder holder)
    {
        super(holder);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        this.setBorder(border);
        alpha = 0.3f;
        img = holder.get("case");
    }
    
    /** Constructor
     * 
     * @param filled The content of the case
     * @param holder The holder of all needed textures
     */
    public Case(TextureHolder holder, int filled)
    {
    	super(holder, filled);
        img = holder.get("case");
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
    public void processMouseEvent(MouseEvent e)
    {
    	super.processMouseEvent(e);
    	if(e.getID() == MouseEvent.MOUSE_ENTERED)
    	{
    		alpha = 0.55f;
    	}
    	else if (e.getID() == MouseEvent.MOUSE_EXITED)
    	{
    		alpha = 0.3f;
    	}
    	repaint();
    }
    
    @Override
    public void repaint()
    {
    	super.repaint();
    	if (filled == 1)
    	{
        	player = Color.RED;
    	}
    	else if (filled == 2)
    	{
        	player = Color.CYAN;
    	}
    	else if (filled == 3)
    	{
        	player = Color.YELLOW;
    	}
    	else if (filled == 4)
    	{
        	player = Color.GREEN;
    	}
    	else
    		player = null;
    	// TODO : Remove if we don't do a board for 6 players
    	/*else if (filled == 5)
    	{
        	g2D.setColor(Color.CYAN);
        	g2D.fillOval(0, 0, this.getWidth(), this.getHeight());
    	}
    	else if (filled == 6)
    	{
        	g2D.setColor(Color.CYAN);
        	g2D.fillOval(0, 0, this.getWidth(), this.getHeight());
    	}*/
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	Graphics2D g2D = (Graphics2D) g;
    	g2D.drawImage(img, 0, 0, null);
    	if (this.isEnabled())
    	{
        	g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    		g2D.setColor(Color.GREEN);
    		g2D.fillRect(0, 0, this.getWidth(), this.getHeight());
    	}

    	g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    	if (player != null)
    	{
    		g2D.setColor(player);
            g2D.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
        	g2D.fillOval(1, 1, this.getWidth()-2, this.getHeight()-2);
    	}
    }
}
