package gui;

import game.Game;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class PlayerPrompt extends JDialog implements ActionListener
{
	protected int numOfPlayers = 2;
	
	protected JPanel panel, players;
	protected JComboBox<String>[] combo;
	
	/** The constructor
	 * 
	 * @param owner Who owns the prompt
	 * @param title The title of the prompt
	 * @param modal Whether the focus cannot be lost
	 * @param game The game
	 * @param playersListInt The array to fill with the data given by the user
	 */
	public PlayerPrompt(JFrame owner, String title, boolean modal, final Game game, final int[] playersListInt)
	{
		super(owner, title, modal);
		this.setSize(600, 200);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		panel = new JPanel();
		players = new JPanel();
		JPanel buttons = new JPanel();
		JButton play = new JButton("Play");
		JButton cancel = new JButton("Cancel");
		buttons.add(play);
		play.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					for (int i = 0 ; i < playersListInt.length ; i++)
						playersListInt[i] = 0;
					
					for (int i = 0 ; i < combo.length ; i++)
					{
						playersListInt[i] = combo[i].getSelectedIndex();
					}

					PlayerPrompt.this.dispatchEvent(new WindowEvent(PlayerPrompt.this, WindowEvent.WINDOW_CLOSING));
				}
			});
		
		cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					PlayerPrompt.this.dispatchEvent(new WindowEvent(PlayerPrompt.this, WindowEvent.WINDOW_CLOSING));
					game.exit();
				}
			});
		
		buttons.add(cancel);
		
		String[] playerString = {"2 players", "3 players", "4 players"};
		JComboBox<String> playersList = new JComboBox<String>(playerString);
		playersList.addActionListener(this);
		playersList.setSelectedIndex(0);
		
		panel.add(playersList);
		
		this.add(panel);
		this.add(players);
		this.add(buttons);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		int index = ((JComboBox<String>) e.getSource()).getSelectedIndex();
		numOfPlayers = index+2;
		update();
	}
	
	private void update()
	{
		players.removeAll();
		String[] playersType = {"Human", "Random", "Shiller", "Straight"};
		Box playersBox = new Box(BoxLayout.X_AXIS);
		for (int i = 0 ; i < numOfPlayers ; i++)
		{
			Box playerBox = new Box(BoxLayout.Y_AXIS);
			
			JLabel text = new JLabel("Player " + (i+1) + ":");
			text.setAlignmentX(CENTER_ALIGNMENT);
			playerBox.add(text);
			playerBox.add(new JComboBox<String>(playersType));
			
			if (i > 0)
				playersBox.add(Box.createRigidArea(new Dimension(50, 0)));
			playersBox.add(playerBox);
		}
		players.add(playersBox);
		players.repaint();
		players.revalidate();
	}
}
