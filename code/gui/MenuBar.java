package gui;

import game.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/** The menu bar
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class MenuBar extends JMenuBar
{
	private static final long serialVersionUID = 3255840399684252825L;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenu gameMenu;
	private JMenuItem rewindItem;
	private JFrame frame;
	
	/** The constructor
	 * 
	 * @param game The game
	 * @param frame The main frame
	 */
	public MenuBar(final Game game, final JFrame frame)
	{
		this.frame = frame;
		fileMenu = new JMenu("File");
		gameMenu = new JMenu("Game");
		helpMenu = new JMenu("Help");
		add(fileMenu);
		add(gameMenu);
		add(helpMenu);
		fileMenuAdd(game);
		gameMenuAdd(game);
		helpMenuAdd();
	}

	/** Enables/Disables the rewind menu
	 * 
	 * @param enable Whether the rewind menu must be enabled or not
	 */
	public void setEnabledRewind(boolean enable)
	{
		rewindItem.setEnabled(enable);
	}
	
	// Add the items in fileMenu
	private void fileMenuAdd(final Game game)
	{
		JMenuItem newGame = new JMenuItem("New Game");
		JMenuItem loadGame = new JMenuItem("Load Game");
		JMenuItem saveGame = new JMenuItem("Save Game");
		JMenuItem exitGame = new JMenuItem("Exit Game");
		fileMenu.add(newGame);
		fileMenu.add(loadGame);
		fileMenu.add(saveGame);
		fileMenu.add(exitGame);
		
		newGame.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					game.newGame(true);
				}
			});
		loadGame.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					new LoadPrompt(frame, "LoadPrompt", true, game);
				}
			});
		saveGame.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					new SavePrompt(frame, "SavePrompt", true, game);
				}
			});
		exitGame.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					game.exit();
				}
			});
	}
	
	// Add the items in gameMenu
	private void gameMenuAdd(final Game game)
	{
		rewindItem = new JMenuItem("Rewind");
		gameMenu.add(rewindItem);
		
		rewindItem.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						game.rewind();
					}
				});
	}
	
	// Add the item in helpMenu
	private void helpMenuAdd()
	{
		JMenuItem manual = new JMenuItem("Manual");
		JMenuItem about = new JMenuItem("About");
		helpMenu.add(manual);
		helpMenu.add(about);
		
		manual.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("Come on, you know how to play this.");
				}
			});
		about.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("Entirely concieved by Thibaut De Cooman, with a little help from Ga�tan Staquet. Or was it the other way around?");
				}
			});
	}
}
