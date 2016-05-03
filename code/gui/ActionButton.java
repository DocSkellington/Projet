package gui;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JButton;

import game.Game;

/** This class defines an action button (move/wall/...)
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class ActionButton extends JButton
{
	private static final long serialVersionUID = -4177825063097743815L;

	/** Constructor */
	public ActionButton()
	{
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setBorder(null);
	}
	
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
    
    @Override
    public void updateUI()
    {
    	super.updateUI();
    	this.setBorder(null);
    }
}
