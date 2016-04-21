package gui;

import game.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class MenuBar extends JMenuBar
{
	private JMenu fileMenu;
	private JMenu helpMenu;
	
	public MenuBar(final Game game)
	{
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");
		add(fileMenu);
		add(helpMenu);
		fileMenuAdd(game);
		helpMenuAdd();
	}
	
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
					System.out.println("Loaded game");
				}
			});
		saveGame.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("Saved game (trust me)");
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
