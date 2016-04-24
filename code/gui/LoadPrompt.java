package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;

import game.Game;

/** Asks the user to choose a file to load
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class LoadPrompt extends JDialog implements ActionListener
{
	private Game game;
	
	/** Constructor
	 * 
	 * @param owner The owner of this dialog
	 * @param title The title of the frame
	 * @param modal Whether the frame must keep the focus or not
	 * @param game The game
	 */
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
		File file = new File("../Save");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Save files", "sav");
		JFileChooser fileChooser = new JFileChooser(file);
		fileChooser.addChoosableFileFilter(filter);
		int choice = fileChooser.showOpenDialog(this);
		if (choice == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				game.load(fileChooser.getCurrentDirectory().toString() + File.separator + fileChooser.getSelectedFile().getName());
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
