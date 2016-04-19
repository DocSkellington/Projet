package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import board.Board;
import game.Game;
import players.APlayer;
import players.Human;

/** The main frame. It manages the button, labels and menu needed.
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class GameFrame extends JFrame
{
	private JPanel board, right;
	private ActionButton moveButton, wallButton, removeButton, rewindButton;
	private JLabel labels[];
	private APlayer[] players;
	
	/** Constructor
	 * 
	 * @param players The array of players
	 */
	public GameFrame(APlayer[] players, Game game)
	{
		super("Quoridor");
		this.players = players;

		// Init of graphics environment
		GraphicsEnvironment a = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] b = a.getScreenDevices();
		Rectangle c = b[0].getDefaultConfiguration().getBounds();
		this.setSize(1050, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation((c.width-this.getSize().width)/2, (c.height-this.getSize().height)/2);
		this.setResizable(false);
		
		init(game);
	}
	
	/** Updates the labels (changes colour of the first line and updates the number of available walls)
	 * 
	 * @param curPlayer The number of the current player
	 */
	public void updateLabels(int curPlayer)
	{
    	labels[0].setText("Current Player: " + (curPlayer+1));
    	labels[0].setForeground(Game.getColor(curPlayer));
        for (int i = 2 ; i < labels.length ; i++)
        {
        	if (players[i-2].getWallCounter() == 1)
        		labels[i].setText("J" + (i-1) + ": 1 wall");
        	else
        		labels[i].setText("J" + (i-1) + ": " + players[i-2].getWallCounter() + " walls");
        }
	}
	
	/** Changes the colour of the action buttons
	 * 
	 * @param curPlayer The number of the current Player
	 */
	public void changeActionButtonColor(int curPlayer)
	{
		moveButton.changeColor(curPlayer);
		wallButton.changeColor(curPlayer);
	}

	/** Add the listeners to move and wall buttons
	 * 
	 * @param human A reference to a human player
	 * @param board A reference to the board
	 */
	public void addActionButtonListener(Human human, Board board)
	{
		moveButton.addActionListener(new MoveButtonListener(wallButton, human, board));
		wallButton.addActionListener(new WallButtonListener(moveButton, human, board));
		removeButton.addActionListener(new RemoveWallButtonListener(wallButton, human, board));
	}
	
	/** Changes the border of the action buttons
	 * 
	 * @param border The new border
	 */
	public void setActionButtonBorder(Border border)
	{
        moveButton.setBorder(border);
        wallButton.setBorder(border);
	}
	
	/** Gets a reference to the board panel
	 * 
	 * @return A reference to the board panel
	 */
	public JPanel getBoard()
	{
		return board;
	}
	
	public void reset(APlayer[] players, Game game)
	{
		this.players = players;
		this.getContentPane().removeAll();
		board = right = null;
		labels = null;
		moveButton = wallButton = removeButton = rewindButton = null;
		this.setJMenuBar(null);
		init(game);
		validate();
		repaint();
	}
	
	// Initialise everything
	private void init(Game game)
	{
		int numPlayers = players.length;
		
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
	
    	// The panel for the board
		board = new JPanel();
		board.setLayout(new GridBagLayout());
		board.setMaximumSize(new Dimension(790, 790));

		// The panel for everything on the right of the board
		right = new JPanel(new BorderLayout());
		
		// The buttons for the human players
		Box buttons = new Box(BoxLayout.Y_AXIS);
        Box moveWallButtons = new Box(BoxLayout.X_AXIS);
        Box removeRewindButtons = new Box(BoxLayout.X_AXIS);
		moveButton = new ActionButton();
		wallButton = new ActionButton();
		removeButton = new ActionButton();
		rewindButton = new ActionButton();

		moveButton.setMargin(new Insets(0, 0, 0, 0));
		moveButton.setBorder(null);
        moveButton.setIcon(new ImageIcon(Game.getImage("moveButton")));
        
		wallButton.setMargin(new Insets(0, 0, 0, 0));
		wallButton.setBackground(Color.BLUE);
		wallButton.setBorder(null);
        wallButton.setIcon(new ImageIcon(Game.getImage("wallButton")));

        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.setBorder(null);
        removeButton.setText("Remove wall");
        //removeButton.setIcon(new ImageIcon(Game.getImage("removeButton")));

        rewindButton.setMargin(new Insets(0, 0, 0, 0));
        rewindButton.setBorder(null);
        rewindButton.setText("Rewind");
        rewindButton.addActionListener(new RewindListener(game));
        
        moveWallButtons.add(moveButton);
        moveWallButtons.add(Box.createRigidArea(new Dimension(10, 0)));
        moveWallButtons.add(wallButton);
        removeRewindButtons.add(removeButton);
        removeRewindButtons.add(Box.createRigidArea(new Dimension(10, 0)));
        removeRewindButtons.add(rewindButton);
        
        buttons.add(moveWallButtons);
        buttons.add(Box.createRigidArea(new Dimension(0, 10)));
        buttons.add(removeRewindButtons);

		right.add(buttons, BorderLayout.NORTH);
		
		// Font used for the labels
		Font font = new Font("Impact", Font.PLAIN, 20);
		// The labels to show the current player and the number of available walls for each player
		Box labelsBox = new Box(BoxLayout.Y_AXIS);
        labels = new JLabel[numPlayers+2];
        labels[0] = new JLabel("");
        labels[1] = new JLabel("Number of available walls:");
        labelsBox.add(labels[0]);
        labelsBox.add(labels[1]);
    	labels[0].setAlignmentX(Label.CENTER_ALIGNMENT);
    	labels[0].setFont(font);
    	labels[1].setAlignmentX(Label.CENTER_ALIGNMENT);
    	labels[1].setFont(font);
    	
        for (int i = 0 ; i < numPlayers ; i++)
        {
        	labels[i+2] = new JLabel("");
        	labels[i+2].setFont(font);
        	labels[i+2].setAlignmentX(Label.CENTER_ALIGNMENT);
        	labelsBox.add(labels[i+2]);
        }
        
        updateLabels(0);
        
        right.add(labelsBox, BorderLayout.CENTER);
        
        setJMenuBar(new MenuBar(game));
        
        this.add(board);
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        this.add(right);
        this.setVisible(true);
	}
	
}
