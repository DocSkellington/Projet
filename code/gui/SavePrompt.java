package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import game.Game;

public final class SavePrompt extends JDialog implements ActionListener
{
	private Game game;
	
	public SavePrompt(JFrame owner, String title, boolean modal, Game game)
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
		int choice = fileChooser.showSaveDialog(this);
		if (choice == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				game.save(fileChooser.getCurrentDirectory().toString() + "\\" + fileChooser.getSelectedFile().getName());
			}
			catch(IOException exc)
			{
				JOptionPane.showMessageDialog(this, "Cannot open file.", "Error while saving", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
