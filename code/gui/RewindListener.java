package gui;

import game.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** The listener of the rewind button
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class RewindListener implements ActionListener
{
	Game game;
	
	/** The constructor
	 * 
	 * @param game The game
	 */
	public RewindListener(Game game)
	{
		this.game = game;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		game.rewind();
	}
}
