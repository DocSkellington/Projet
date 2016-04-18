package game;

import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.text.ParseException;
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import board.Board;
import board.Coordinates;
import gui.ActionButton;
import gui.ColorHolder;
import gui.GameFrame;
import gui.MoveButtonListener;
import gui.TextureHolder;
import gui.WallButtonListener;
import players.*;

/** Main class that keeps the game running
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 */
public final class Game
{
	private APlayer[] players;
	private Board board;
	private ArrayList<Round> roundList; 
	private GameFrame frame;
	// Remembers the index in roundList of the move of the last human who has played
	private int previousHuman;
	private int curPlayer;
	
	/** The holder of every texture needed by the graphical interface */
	public static TextureHolder textureHolder;

	/** The holder of every color needed by the graphical interface (to draw players) */
	public static ColorHolder colorHolder;
	
	/** Default constructor */
	public Game()
	{
		players = null;
		board = null;
		roundList = new ArrayList<Round>();
		frame = null;
		previousHuman = -1;
	}
	
	/** What keeps the game running */
	public void run()
	{
        int numPlayers = 2, hum = 2, randAINum = 0;
        
        init(numPlayers, hum, randAINum);

        curPlayer = 0;
        int winner = -1; // -1 means no winner
        
        while (winner == -1)
        {
        	// We update the buttons and the label
        	frame.changeActionButtonColor(curPlayer);
            frame.updateLabels(curPlayer);
            // The current player plays
            roundList.add(players[curPlayer].play(board));
            board.update();
            // As long as we can't refresh the graphical interface, we wait.
            while(!board.repaint())
            {}
            frame.setActionButtonBorder(null);
            
            // TODO: Make it work!
            // Update previousHuman
            if (players[curPlayer] instanceof Human)
            	previousHuman = roundList.size() - 1;
            
            // Next player
            curPlayer = (curPlayer + 1) % numPlayers;
            winner = board.hasWon();
        }
        
        board.update();
        // As long as we can't refresh the graphical interface, we wait.
        while(!board.repaint())
        {}
        printVictory(winner);
	}
	
	/** Cancels the rounds done since the last time a human played 
	 * 
	 */
	public void rewind()
	{
		// Delete the last rounds
		if (previousHuman != -1)
		{
			for (int i = roundList.size() - 1 ; i >= previousHuman ; i--)
			{
				roundList.remove(i);
			}
		}
		
		// Execute all the rounds
		executeRounds();
	}
	
	// Execute the roundList
	private void executeRounds()
	{
		// Reset the players
		int wall = 10;
		if (players.length == 3)
			wall = 7;
		else if (players.length == 4)
			wall = 5;
		
		for (APlayer player : players)
			player.setWallCounter(wall);
		
		// Reset the board
		board = new Board(players, 9);
		
		board.print();
		
		System.out.println(roundList);
		
		// Plays the rounds until the end
		int cur = 0;
		for (Round round : roundList)
		{
			players[cur].play(board, round);
			board.update();
			cur = (cur + 1) % players.length;
		}
		System.out.println("Done");
		board.update();
		curPlayer = cur;
		while(!board.repaint())
		{
			System.out.println("Hello");
		}
		board.print();
	}
	
	/** Asks the number of players
	 * 
	 * @return The number of players
	 */
    private int howManyPlayers()
    {
    	System.out.println("How many players ?");
    	int res = 0;
    	Scanner scan = new Scanner(System.in);
    	
    	do
    	{
    		while (true)
    		{
    			if(!scan.hasNextInt())
    			{
    				System.out.println("An Integer, por favor, Senior");
    				scan.next();
    				continue;
    			}
    			break;
    		}
    		
    		res = scan.nextInt();
    		
    		if (res <= 1 || res > Board.maxPlayers())
    		{
    			System.out.println(Board.maxPlayers() + " players max and at least 2 players");
    		}
    		else
    		{
    			return res;
    		}
    		
    	} while(true);
    }
    
    /** Asks how many humans players
     * 
     * @param maxPlayers The maximum number of players
     * @return The number of human players
     */
    private int howManyHumans(int maxPlayers)
    {
    	Scanner scan = new Scanner(System.in);
    	
    	System.out.println("How many humans ?");
    	
    	int res = 0;
    	
    	do
    	{
    		while (true)
    		{
    			if(!scan.hasNextInt())
    			{
    				System.out.println("An Integer, por favor, Senior");
    				scan.next();
    				continue;
    			}
    			break;
    		}
    		
    		res = scan.nextInt();
    		
    		
    		if (res < 0 || res > maxPlayers)
    		{
    			System.out.println(maxPlayers + " humans max and at least 0 humans");
    		}
    		else
    		{
				return res;
    		}
    		
    	} while(true);
    }

    /** Asks how many random AI players
     * 
     * @param maxPlayers The maximum number of players
     * @param human The number of human players
     * @return The number of random AI players
     */
    private int howManyRandom(int maxPlayers, int human)
    {
    	Scanner scan = new Scanner(System.in);
    	
    	System.out.println("How many RandomAI(s) ?");
    	
    	int res = 0;
    	
    	do
    	{
    		while (true)
    		{
    			if(!scan.hasNextInt())
    			{
    				System.out.println("An Integer, por favor, Senior");
    				scan.next();
    				continue;
    			}
    			break;
    		}
    		
    		res = scan.nextInt();
    		
    		
    		if (res < 0 || res > (maxPlayers-human))
    		{
    			System.out.println((maxPlayers-human) + " RandomAI max and at least 0 RandomAI");
    		}
    		else
    		{
				return res;
    		}
    		
    	} while(true);
    }
    
    // TODO : Remove
    private boolean wannaSave()
    {
    	Scanner scan = new Scanner(System.in);
    	System.out.println("Do you want to save ?");
    	while(!scan.hasNextBoolean())
    	{
    		System.out.println("Please enter a boolean (true or false)");
    		scan.next();
    	}
    	boolean choice = scan.nextBoolean();
    	return choice;
    }
    
    /** Asks if the player(s) want(s) to keep playing
     * 
     * @return Whether the game keeps running or not
     */
    private boolean keepPlaying()
    {
    	Scanner scan = new Scanner(System.in);
    	System.out.println("Do you want to keep playing ? (Y/N)");
    	String res = "";
    	
    	do
    	{
    		res = scan.nextLine();
    		if(res.equals("Y"))
    		{
    			return true;
    		}
    		else if (res.equals("N"))
    		{
    			return false;
    		}
    		
    		System.out.println("Y or N!");
    	} while (true);
    }
    
    /** See: FF7 victory fanfare
     * 
     * @param winner Number of the winning player
     */
    private void printVictory(int winner)
    {
        System.out.println("Congratulations, Player " + (winner + 1) + " ! You can leave the Arena now and rest... You've earned it!");
        System.out.println("The list of rounds : " + roundList);
    }
    
    /** Initialises the players list
     * 
     * @param playersNumber The number of players
     * @param humNumber The number of human players
     */
    private void init(int playersNumber, int humNumber, int randAINum)
    {
    	players = new APlayer[playersNumber];
    	
    	// The number of available walls per player
    	int walls = 10;
    	if (playersNumber == 4)
    		walls = 5;
    	else if (playersNumber == 3)
    		walls = 7;
    	
    	int i = 0;
    	// The human players
    	while(i < humNumber)
    	{
    		players[i] = new Human(i++, walls);
    	}
    	// The random AIs
    	while(i < humNumber + randAINum)
    	{
    		players[i] = new StrategyAI(i++, walls, new RandomStrategy());
    	}
    	// The Shiller AIs
    	while(i < playersNumber)
    	{
    		players[i] = new StrategyAI(i++, walls);
    	}

        graphInit(playersNumber);
        
        board = new Board(players, 9);
        
        // This is used to hash the coordinates:
        Coordinates.size = board.getYSize();

        // Add the listeners
        for (i = 0 ; i < players.length ; i++)
        {
        	if (players[i] instanceof Human)
        	{
        		frame.addActionButtonListener((Human) players[i], board);
    		}
        }
        
        // Fills the board panel
		board.fill(frame.getBoard(), players);
		
		// Update the frame
		frame.repaint();
		frame.revalidate();
		
    }
    
    /* Initialises the graphical components */
    private void graphInit(int numPlayers)
    {
    	// Texture holder loads every image
    	textureHolder = new TextureHolder();
        try
        {
        	textureHolder.load("case", "../data/case.jpg");
        	textureHolder.load("wallEmpty", "../data/wallEmpty.jpg");
        	textureHolder.load("wallFilled", "../data/wallFill.jpg");
        	textureHolder.load("moveButton", "../data/moveButton.png");
        	textureHolder.load("wallButton", "../data/wallButton.png");
        }
        catch(IOException e)
        {
        	System.out.println(e);
        }
        
        // Colour holder loads every colour
        colorHolder = new ColorHolder();
        colorHolder.load(0, Color.RED);
        colorHolder.load(1, Color.CYAN);
        colorHolder.load(2, Color.YELLOW);
        colorHolder.load(3, Color.GREEN);

        frame = new GameFrame(players, this);
    }
    
    private void save(String filepath) throws IOException
    {
    	// TODO : utiliser flux d'objet
    	Path file = Paths.get(filepath);
    	List<String> lines = new ArrayList<String>();
    	
    	for (Round round : roundList)
    		lines.add(round.toString());
    	
    	Files.write(file, lines, Charset.forName("UTF-8"));
    }
    
    private void load(String filepath) throws IOException, ParseException
    {
    	Path file = Paths.get(filepath);
    	List<String> list = Files.readAllLines(file, Charset.forName("UTF-8"));
    	ArrayList<Round> rounds = new ArrayList<Round> ();
    	for (String string : list)
    	{
    		rounds.add(Round.parse(string));
    	}
    	roundList = rounds;
    }
    
    /** Gets the image inside textureHolder binded to the key
     * 
     * @param key The key of the image
     * 
     * @return An image if it exists, null otherwise
     */
    public static BufferedImage getImage(String key)
    {
    	if(textureHolder != null)
    		return textureHolder.get(key);
    	return null;
    }

    /** Gets the colour inside colorHolder binded to the key
     * 
     * @param key The key of the image
     * 
     * @return A Color if it exists, null otherwise
     */
    public static Color getColor(Integer key)
    {
    	if(colorHolder != null)
    		return colorHolder.get(key);
    	return null;
    }
    
    /** Main method
     *  
     * @param args Arguments
     */
    public static void main(String[] args)
    {    	
    	Game game = new Game();
    	game.run();
    }
    
}
