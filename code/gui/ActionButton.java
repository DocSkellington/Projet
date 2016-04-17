package gui;

import java.awt.Graphics;

import javax.swing.JButton;

import game.Game;

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
    	this.setBackground(Game.getColor(numPlayer));
    }
}
