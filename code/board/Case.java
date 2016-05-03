package board;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import game.Game;

/** A Case is a Cell on which a player can walk
 * 
 * @author Thibaut De Cooman
 * @author Gaetan Staquet
 *
 */
public final class Case extends ACell
{
	private static final long serialVersionUID = 4919930061923764584L;
	private float alpha;
	private Color player;
	private Border border;
	
	/** Constructor */
    public Case()
    {
        this(0);
    }
    
    /** Constructor
     * 
     * @param filled The content of the case
     */
    public Case(int filled)
    {
    	super(filled);
        border = BorderFactory.createLineBorder(Color.BLACK, 1);
        this.setBorder(border);
        alpha = 0.3f;
        img = Game.getImage("case");
    }
    
    @Override
    public String toString()
    {
    	String res = filled + "";
    	return res;
    }
    
    @Override
    public Case clone()
    {
    	return new Case(filled);
    }
    
    @Override
    public void updateUI()
    {
    	super.updateUI();
    	this.setBorder(border);
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
    	player = Game.getColor(filled-1);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	Graphics2D g2D = (Graphics2D) g;
    	// We draw the case
    	g2D.drawImage(img, 0, 0, null);
    	
    	if (this.isEnabled())
    	{
    		// We draw a green square if the case is enabled
        	g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    		g2D.setColor(Color.GREEN);
    		g2D.fillRect(0, 0, this.getWidth(), this.getHeight());
    	}

    	g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    	
    	if (player != null)
    	{
    		// We draw the player
    		g2D.setColor(player);
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        	g2D.fillOval(1, 1, this.getWidth()-2, this.getHeight()-2);
    	}
    }
}
