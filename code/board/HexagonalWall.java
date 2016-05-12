package board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;


public final class HexagonalWall extends Wall
{
	private static final long serialVersionUID = 4830093036605727011L;
	protected double angle = 0D;
	protected Polygon rectangle = new Polygon();
	protected Color color = new Color(89, 25, 16);

	public HexagonalWall()
	{
		super();
		this.setOpaque(false);
		this.setBorderPainted(false);
		setAngle(0);
		//calculateBounds();
	}
	
	/** Sets a new value to the angle.
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
	public void setPreferredSize(Dimension d)
	{
		super.setPreferredSize(d);
		calculateBounds();
	}
	
	@Override
	public void setSize(int w, int h)
	{
		super.setSize(w, h);
	}
	
	@Override
	public boolean contains(Point p)
	{
		return rectangle.contains(p);
	}
	
	@Override
	public void repaint()
	{
		super.repaint();
		
		if (filled == 0 || filled == 3)
			color = new Color(89, 25, 16);
		else if (filled == 1 || filled == 2)
			color = new Color(201, 199, 0);
	}
	
	@Override
	protected void processMouseEvent(MouseEvent e)
	{
		if (this.contains(e.getPoint()))
			super.processMouseEvent(e);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		g2D.setColor(color);
		
		g2D.fillPolygon(rectangle);
	}
	
	private void calculateBounds()
	{
		Polygon rect = new Polygon();
		
		if (rectangle.npoints == 0)
		{
			Dimension d = new Dimension(100, 30);
			Point p = rotation(new Point(0, 0));
			rect.addPoint(p.x, p.y);
			p = rotation(new Point((int)d.getWidth(), 0));
			rect.addPoint(p.x, p.y);
			p = rotation(new Point((int)d.getWidth(), (int)d.getHeight()));
			rect.addPoint(p.x, p.y);
			p = rotation(new Point(0, (int)d.getHeight()));
			rect.addPoint(p.x, p.y);
		}
		else
		{
			for (int i = 0 ; i < 4 ; i++)
			{
				Point p = rotation(new Point(rectangle.xpoints[i], rectangle.ypoints[i]));
				rect.addPoint(p.x, p.y);
			}
		}
		
		rectangle = rect;
		repaint();
	}
	
	private Point rotation(Point p)
	{
		int xc = getX() + 20;
		int yc = getY() + 5;
		
		Point point = new Point();
		point.x = (int) (Math.cos(Math.toRadians(angle))*(p.x-xc) - Math.sin(Math.toRadians(angle))*(p.y-yc) + xc);
		point.y = (int) (Math.sin(Math.toRadians(angle))*(p.x-xc) + Math.cos(Math.toRadians(angle))*(p.y-yc) + yc);
		return point;
	}
}
