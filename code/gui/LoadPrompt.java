package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import game.Game;

public final class LoadPrompt extends JDialog implements ActionListener
{
	private Game game;
	
	public LoadPrompt(JFrame owner, String title, boolean modal, Game game)
	{
		super(owner, title, modal);
		this.game = game;
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		actionPerformed(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fileChooser = new JFileChooser();
		int choice = fileChooser.showOpenDialog(this);
		if (choice == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				game.load(fileChooser.getCurrentDirectory().toString() + "\\" + fileChooser.getSelectedFile().getName());
			}
			catch(IOException exc)
			{
				JOptionPane.showMessageDialog(this, "Cannot open file.", "Error while loading", JOptionPane.ERROR_MESSAGE);
			}
			catch(ParseException exc)
			{
				JOptionPane.showMessageDialog(this, "Incorrect file.", "Error while loading", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
