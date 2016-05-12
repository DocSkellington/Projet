package board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import game.Game;

public final class HexagonalWall extends Wall
{
	private static final long serialVersionUID = 4830093036605727011L;
	protected double angle = 0D;
	protected Polygon rectangle = new Polygon();

	public HexagonalWall()
	{
		super();
		this.setOpaque(false);
		this.setBorderPainted(false);
		//calculateBounds();
	}
	
	/** Sets a new value to angle
	 * 
	 * @param angle The value in degree of the new angle
	 */
	public void setAngle(double angle)
	{
		this.angle = angle;
		calculateBounds();
	}


	@Override
	public void setSize(Dimension d)
	{
		super.setSize(d);
	}
	
	@Override
	public void setSize(int w, int h)
	{
		super.setSize(w, h);
	}
	
	@Override
	protected void processMouseEvent(MouseEvent e)
	{
		System.out.println(getBounds());
		if (this.contains(e.getPoint()))
			super.processMouseEvent(e);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		//g2.fillRect(0, 0, 500, 500);
		//g2.fillRect(100, 100, -100, -100);
		//g2.drawPolygon(rectangle);
		Rectangle bounds = getBounds(new Rectangle());
        g2.rotate(Math.toRadians(angle), bounds.getWidth()/2, bounds.getHeight()/2);
        double diagonal = Math.sqrt(bounds.getWidth() * bounds.getWidth() + bounds.getHeight() * bounds.getHeight());
        double scaleX = bounds.getWidth() / diagonal;
        double scaleY = bounds.getHeight() / diagonal;
        
        //Apply the scaling from the center
        g2.translate(bounds.getWidth()/2, bounds.getHeight()/2);
        g2.scale(scaleX, scaleY);
        g2.translate(-bounds.getWidth()/2, -bounds.getHeight()/2);

        //Paints the component
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, (int)bounds.getWidth(), (int)bounds.getHeight());
	}
	
	private void calculateBounds()
	{
		Polygon rect = new Polygon();
		System.out.println(getWidth());
		rect.addPoint(0, 0);
		rect.addPoint((int)(this.getWidth()*angle), (int) (this.getHeight()*angle));
		rectangle = rect;
	}
}
