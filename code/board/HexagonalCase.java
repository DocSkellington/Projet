package board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
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
		System.out.println(getBounds());
		if (contains(e.getPoint()))
		{
			super.processMouseEvent(e);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		//super.paintComponent(g);
		
		// Draw a transparent square
		g.setColor(new Color(0.f, 0.f, 0.f, 0.f));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (isSelected())
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.BLACK);
		
		g.fillPolygon(hexagon);
		//g.drawPolygon(hexagon);
	}
	
	private void calculateCoords()
	{
		Polygon hex = new Polygon();
		int w = getWidth() - 1, h = getHeight() - 1;
		int ratio = (int) (h * .25);

		hex.addPoint(w / 2, 0);
		hex.addPoint(w, ratio);
		hex.addPoint(w, h - ratio);
		hex.addPoint(w / 2, h);
		hex.addPoint(0, h - ratio);
		hex.addPoint(0, ratio);
		
		hexagon = hex;
	}
}
