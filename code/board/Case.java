package board;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

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
	/* Constructor
	 * 
     * @param holder The holder of all needed textures
     */
    public Case(TextureHolder holder)
    {
        super(holder);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        this.setBorder(border);
        alpha = 0.3f;
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
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	Graphics2D g2D = (Graphics2D) g;
    	g2D.drawImage(holder.get("case"), 0, 0, null);
    	if (this.isEnabled())
    	{
        	g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    		g2D.setColor(Color.GREEN);
    		g2D.fillRect(0, 0, this.getWidth(), this.getHeight());
    	}

    	g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    	if (filled == 1)
    	{
        	g2D.setColor(Color.RED);
        	g2D.fillOval(0, 0, this.getWidth(), this.getHeight());
    	}
    	else if (filled == 2)
    	{
        	g2D.setColor(Color.CYAN);
        	g2D.fillOval(0, 0, this.getWidth(), this.getHeight());
    	}
    	else if (filled == 3)
    	{
        	g2D.setColor(Color.YELLOW);
        	g2D.fillOval(0, 0, this.getWidth(), this.getHeight());
    	}
    	else if (filled == 4)
    	{
        	g2D.setColor(Color.GREEN);
        	g2D.fillOval(0, 0, this.getWidth(), this.getHeight());
    	}
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
}
