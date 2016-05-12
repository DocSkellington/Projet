package board;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

public final class HexagonalCase extends Case
{
	private static final long serialVersionUID = 6728182116411995302L;
	private Polygon hexagon = new Polygon();
	
	public HexagonalCase()
	{
		super();
		setOpaque(false);
		setBorderPainted(false);
	}
	
	@Override
	public boolean contains(Point p)
	{
		return hexagon.contains(p);
	}
	
	@Override
	public boolean contains(int x, int y)
	{
		return hexagon.contains(x, y);
	}
	
	@Override
	public void setSize(Dimension d)
	{
		super.setSize(d);
		calculateCoords();
	}
	
	@Override
	public void setSize(int w, int h)
	{
		super.setSize(w, h);
		calculateCoords();
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		calculateCoords();
	}
	
	@Override
	public void setBounds(Rectangle r)
	{
		super.setBounds(r);
		calculateCoords();
	}
	
	@Override
	protected void processMouseEvent(MouseEvent e)
	{
		if (contains(e.getPoint()))
		{
			super.processMouseEvent(e);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D)g;
		g.setColor(new Color(69, 31, 22));
		
		g.fillPolygon(hexagon);
		
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
	
	private void calculateCoords()
	{
		Polygon hex = new Polygon();
		int w = getWidth() - 1, h = getHeight() - 1;

		hex.addPoint(2*w/10, h);
		hex.addPoint(0, h/2);
		hex.addPoint(2*w/10, 0);
		hex.addPoint(8*w/10, 0);
		hex.addPoint(w, h/2);
		hex.addPoint(8*w/10, h);
		
		hexagon = hex;
	}
}
