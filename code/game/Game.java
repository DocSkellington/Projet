package game;

import java.util.concurrent.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.text.ParseException;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;

import board.Board;
import board.Coordinates;
import gui.ColorHolder;
import gui.GameFrame;
import gui.PlayerPrompt;
import gui.TextureHolder;
import players.*;

/** Main class that keeps the game running
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 */
public final class Game
{
	// We want to use an unique thread to run the game
	private ExecutorService gameExecutor = Executors.newSingleThreadExecutor();
	private Future<?> gameTask;
	
	private APlayer[] players;
	private Board board;
	private ArrayList<Round> roundList; 
	private GameFrame frame;
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
        curPlayer = 0;
        texAndColorLoad();
	}
	
	/** Create a new game and launch it
	 * 
	 * @param askSure If the method must ask if the user really wants to launch a new game
	 */
	public void newGame(boolean askSure)
	{
		if (askSure)
		{
			int choice = JOptionPane.showConfirmDialog(frame, "Do you really want to start a new game?");
			if (choice != JOptionPane.OK_OPTION)
				return;
		}
		
		// Canceling the current thread
		if (gameTask != null)
			gameTask.cancel(true);
		
		roundList.clear();
		
		// Starting a new thread (init + play)
		gameTask = gameExecutor.submit(new Runnable()
			{
				public void run()
				{
					if (frame == null)
					{
						frame = new GameFrame(new APlayer[0], Game.this);
						frame.setVisible(true);
					}
					
					ArrayList<Integer> playersList = new ArrayList<Integer>();
					PlayerPrompt prompt = new PlayerPrompt(frame, "Players choice", true, Game.this, playersList);
					prompt.setVisible(true);
					init(playersList.toArray(new Integer[0]));
			        curPlayer = 0;
					play();
				}
			});
	}
	
	
	/** Exits the game */
	public void exit()
	{
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	
	/** Cancels the rounds done since the last time the current player played */
	public void rewind()
	{
		// Canceling the current thread
		if (gameTask != null)
			gameTask.cancel(true);

		// To be sure that the thread is finished
		try
		{
			Thread.sleep(600);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}

		// We want to cancel every move done since the last time the current player played
		// If the current player has not already played
		if (roundList.size() < players.length)
		{
			// We simply start a new game
			newGame(false);
			return;
		}
		
		// Delete the last rounds
		int start = roundList.size() - 1 - players.length;
		for (int i = roundList.size() - 1 ; i >= start && i >= 0 ; i--)
			roundList.remove(i);
		
		// Starting a new thread (init + play).
		// We do not change curPlayer because we do not want to allow the other player(s) to play before the human who asked to rewind.
		gameTask = gameExecutor.submit(new Runnable()
			{
				public void run()
				{
					init(players);
					executeRounds();
					play();
				}
			});
	}
	
	// What keeps the game running
	private void play()
	{		
		int numPlayers = players.length;
        int winner = -1; // -1 means no winner
        
        while (winner == -1)
        {
        	// Stop to play if the thread is interrupted
        	if (Thread.currentThread().isInterrupted())
        		return;
        	
        	// We update the buttons and the label
        	frame.changeActionButtonColor(curPlayer);
            frame.updateLabels(curPlayer);
            // The current player plays
            roundList.add(players[curPlayer].play(board));
            
            // Update logical part of the board
            board.update();
            
            // As long as we can't refresh the graphical interface, we wait.
            SwingUtilities.updateComponentTreeUI(frame);
            frame.invalidate();
            while(!board.repaint());
            frame.revalidate();
            Board.prev_tick = System.currentTimeMillis();
            
            frame.setActionButtonBorder(null);
            
            // Next player
            curPlayer = (curPlayer + 1) % numPlayers;
            winner = board.hasWon();
        }
        
        board.update();
        // As long as we can't refresh the graphical interface, we wait.
        while(!board.repaint());
        printVictory(winner);
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
			player.reset(wall);
		
		// Reset the board
		board.reset();
		
		// Plays the rounds until the end
		int cur = 0;
		for (Round round : roundList)
		{
			players[cur].play(board, round);
			board.update();
			cur = (cur + 1) % players.length;
		}
		board.update();
		curPlayer = cur;
		while(!board.repaint());
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
    
    private void init(Integer[] playersInt)
    {
    	APlayer[] playersList = new APlayer[playersInt.length];
    	
    	for (int i = 0 ; i < playersInt.length ; i++)
    	{
    		switch(playersInt[i])
    		{
    		case 0:
    			playersList[i] = new Human(i, 10);
				break;
    		case 1:
    			playersList[i] = new StrategyAI(i, 10, new RandomStrategy());
    			break;
    		case 2:
    			playersList[i] = new StrategyAI(i, 10, new ShillerStrategy());
    			break;
    		case 3:
    			playersList[i] = new StrategyAI(i, 10, new StraightStrategy());
    			break;
    		}
    	}
    	init(playersList);
    }
    
    /** Initialises the players list
     * 
     * @param playersNumber The number of players
     * @param humNumber The number of human players
     */
    private void init(APlayer[] playersList)
    {
    	int playersNumber = playersList.length;
    	
    	players = playersList;
    	
    	// The number of available walls per player
    	int walls = 10;
    	if (playersNumber == 4)
    		walls = 5;
    	else if (playersNumber == 3)
    		walls = 7;
    	
    	for (int i = 0 ; i < playersNumber ; i++)
    	{
    		players[i].reset(walls);
    	}
    	
    	// We recreate the frame
        frame.reset(players, this);
        
        board = new Board(players, 9);
        
        // This is used to hash the coordinates:
        Coordinates.size = board.getYSize();

        // Add the listeners
        for (int i = 0 ; i < players.length ; i++)
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
    
    private void texAndColorLoad()
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
        	System.err.println(e);
        }
        
        // Colour holder loads every colour
        colorHolder = new ColorHolder();
        colorHolder.load(0, Color.RED);
        colorHolder.load(1, Color.CYAN);
        colorHolder.load(2, Color.YELLOW);
        colorHolder.load(3, Color.GREEN);
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
    	game.newGame(false);
    }
    
}
