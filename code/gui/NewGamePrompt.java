package gui;

import game.Game;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import board.Coordinates;

/** Asks the user to choose how many players and which ones
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class NewGamePrompt extends JDialog implements ActionListener
{
	protected int numOfPlayers = 2;
	
	protected JPanel panel, players;
	protected ArrayList<JComboBox<String>> combo;
	protected ArrayList<JTextField> fields;
	protected JComboBox<String> widthList, heightList;
	
	/** The constructor
	 * 
	 * @param owner Who owns the prompt
	 * @param title The title of the prompt
	 * @param modal Whether the focus cannot be lost
	 * @param game The game
	 * @param playersListInt The array to fill with the data given by the user
	 * @param sizeBoard The coordinates to store the selected size of the board
	 * @param names The ArrayList to store the names of the players
	 */
	public NewGamePrompt(JFrame owner, String title, boolean modal, final Game game, final ArrayList<Integer> playersListInt,
			final Coordinates sizeBoard, final ArrayList<String> names)
	{
		super(owner, title, modal);
		combo = new ArrayList<JComboBox<String>>();
		fields = new ArrayList<JTextField>();
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
					playersListInt.clear();
					
					for (int i = 0 ; i < combo.size() ; i++)
					{
						playersListInt.add(combo.get(i).getSelectedIndex());
					}

					sizeBoard.setX(widthList.getSelectedIndex()+5);
					sizeBoard.setY(heightList.getSelectedIndex()+5);
					
					names.clear();
					
					for (JTextField field : fields)
						names.add(field.getText());
					
					NewGamePrompt.this.dispatchEvent(new WindowEvent(NewGamePrompt.this, WindowEvent.WINDOW_CLOSING));
				}
			});
		
		cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					NewGamePrompt.this.dispatchEvent(new WindowEvent(NewGamePrompt.this, WindowEvent.WINDOW_CLOSING));
					game.exit();
				}
			});
		
		buttons.add(cancel);
		
		String[] playerString = {"2 players", "3 players", "4 players"};
		JComboBox<String> playersList = new JComboBox<String>(playerString);
		playersList.addActionListener(this);
		playersList.setSelectedIndex(0);
		
		String[] widthString = new String[21];
		for (int i = 5 ; i <= 25 ; i++)
		{
			widthString[i-5] = Integer.toString(i);
		}
		widthList = new JComboBox<String>(widthString);
		widthList.setSelectedIndex(4);
		
		String[] heightString = new String[21];
		for (int i = 5 ; i <= 25 ; i++)
		{
			heightString[i-5] = Integer.toString(i);
		}
		heightList = new JComboBox<String>(heightString);
		heightList.setSelectedIndex(4);
		
		JLabel x = new JLabel();
		x.setText("x");
		
		panel.add(playersList);
		panel.add(widthList);
		panel.add(x);
		panel.add(heightList);
		
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
		combo.clear();
		fields.clear();
		String[] playersType = {"Human", "Random", "Shiller", "Straight"};
		Box playersBox = new Box(BoxLayout.X_AXIS);
		for (int i = 0 ; i < numOfPlayers ; i++)
		{
			Box playerBox = new Box(BoxLayout.Y_AXIS);
			
			JTextField textField = new JTextField("Player " + (i+1));
			textField.setAlignmentX(CENTER_ALIGNMENT);
			playerBox.add(textField);
			fields.add(textField);
			JComboBox<String> comboBox = new JComboBox<String>(playersType);
			playerBox.add(comboBox);
			combo.add(comboBox);
			
			if (i > 0)
				playersBox.add(Box.createRigidArea(new Dimension(50, 0)));
			playersBox.add(playerBox);
		}
		players.add(playersBox);
		players.repaint();
		players.revalidate();
	}
}
