package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public final class ActionButton extends JButton
{
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	
	/** Changes the background color corresponding to numPlayer
	 * 
	 * @param numPlayer The number of the current player
	 */
    public void changeColor (int numPlayer)
    {
    	if (numPlayer == 0)
    		this.setBackground(Color.RED);
    	else if (numPlayer == 1)
    		this.setBackground(Color.CYAN);
    	else if (numPlayer == 2)
    		this.setBackground(Color.YELLOW);
    	else if (numPlayer == 3)
    		this.setBackground(Color.GREEN);
    }
}
