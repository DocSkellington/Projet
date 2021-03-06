package game;

import java.util.concurrent.*;
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

import board.RectangularBoard;
import board.ABoard;
import board.Coordinates;
import board.HexagonalBoard;
import gui.ColorHolder;
import gui.GameFrame;
import gui.NewGamePrompt;
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
	private ABoard board;
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
		
		try
		{
			Thread.sleep(30);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
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
					ArrayList<String> names = new ArrayList<String>();
					Coordinates sizeBoard = new Coordinates(0, 0);
					NewGamePrompt prompt = new NewGamePrompt(frame, "Players choice", true, Game.this, playersList, sizeBoard, names);
					prompt.setVisible(true);
					init(playersList.toArray(new Integer[0]), sizeBoard, names);
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
			Thread.sleep(1000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	
		// We want to cancel every move done since the last time the current player played
		
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
					init(players, board.getSize());
					executeRounds();
					play();
				}
			});
	}
	
	// What keeps the game running
	private void play()
	{		
		int numPlayers = players.length;
		int numRounds = 0;
        int winner = -1; // -1 means no winner
        
        while (winner == -1)
        {
        	// Stop to play if the thread is interrupted
        	if (Thread.currentThread().isInterrupted())
        		return;

        	// We update the buttons and the label
        	frame.updateActionButton(curPlayer, board);
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
            RectangularBoard.prev_tick = System.currentTimeMillis();
            
            frame.setActionButtonBorder(null);
            
            // Next player
            curPlayer = (curPlayer + 1) % numPlayers;
            winner = board.hasWon();
            numRounds++;
            if (numRounds >= numPlayers)
            	frame.setEnabledRewind(true);
        }
        
        board.update();
        // As long as we can't refresh the graphical interface, we wait.
        while(!board.repaint());
        printVictory(winner);
	}
	
	// Execute the roundList
	private void executeRounds()
	{		
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
	
    /* See: FF7 victory fanfare
     * 
     * @param winner Number of the winning player
     */
    private void printVictory(int winner)
    {
		String message = "Congratulations, " + players[winner].getName();
    	JOptionPane.showMessageDialog(frame, message, "Victory", JOptionPane.PLAIN_MESSAGE);
    }
    
    // Creates the player from the playersInt and call init(APlayer[], Coordinates)
    private void init(Integer[] playersInt, Coordinates sizeBoard, ArrayList<String> names)
    {
    	APlayer[] playersList = new APlayer[playersInt.length];
    	
    	for (int i = 0 ; i < playersInt.length ; i++)
    	{
    		switch(playersInt[i])
    		{
    		case 0:
    			playersList[i] = new Human(i, 10, names.get(i));
				break;
    		case 1:
    			playersList[i] = new StrategyAI(i, 10, names.get(i), new RandomStrategy());
    			break;
    		case 2:
    			playersList[i] = new StrategyAI(i, 10, names.get(i), new ShillerStrategy());
    			break;
    		case 3:
    			playersList[i] = new StrategyAI(i, 10, names.get(i), new StraightStrategy());
    			break;
    		}
    	}
    	init(playersList, sizeBoard);
    }
    
    /* Initialises the game */
    private void init(APlayer[] playersList, Coordinates sizeBoard)
    {
    	int playersNumber = playersList.length;
    	
    	players = playersList;
    	
    	// The number of available walls per player
    	int walls = (sizeBoard.getX() * sizeBoard.getY()) / 8;
    	if (playersNumber == 4)
    		walls = (sizeBoard.getX() * sizeBoard.getY()) / 16;
    	else if (playersNumber == 3)
    		walls = (sizeBoard.getX() * sizeBoard.getY()) / 12;
    	
    	for (int i = 0 ; i < playersNumber ; i++)
    	{
    		players[i].reset(walls);
    	}
    	
    	// We recreate the frame
        frame.reset(players, this);
        
        //board = new RectangularBoard(players, sizeBoard.getX(), sizeBoard.getY());
        board = new HexagonalBoard(players);
        
        // Add the listeners
        for (int i = 0 ; i < players.length ; i++)
        {
        	if (players[i] instanceof Human)
        	{
        		frame.addActionButtonListener((Human) players[i], board);
    		}
        }
        
        // Fills the board panel
		board.fill(frame.getBoard());
		
		// Update the frame
		frame.repaint();
		frame.revalidate();
		
		frame.setEnabledRewind(false);
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
        colorHolder.load(4, Color.MAGENTA);
        colorHolder.load(5, Color.BLUE);
    }
    
    public void save(String filepath) throws IOException
    {
    	Path file = Paths.get(filepath);
    	List<String> lines = new ArrayList<String>();
    	
    	// Save the size of the board
    	lines.add(board.getSize().toString());
    	
    	// Save the players
    	lines.add(players.length + "");
    	
    	for (APlayer player : players)
    		lines.add(player.toString());
    	
    	// Save the rounds
    	for (Round round : roundList)
    		lines.add(round.toString());
    	
    	Files.write(file, lines, Charset.forName("UTF-8"));
    }
    
    public void load(String filepath) throws IOException, ParseException
    {
    	Path file = Paths.get(filepath);
    	List<String> list = Files.readAllLines(file, Charset.forName("UTF-8"));
    	ArrayList<Round> rounds = new ArrayList<Round> ();
    	
    	int i = 0;
    	// Load the size
    	String size = list.get(i++);
    	final Coordinates sizeBoard = Coordinates.parse(size);
    	
    	// Load the players
    	int numPlayers = Integer.parseInt(list.get(i++));
    	final APlayer[] newPlayers = new APlayer[numPlayers];
    	
		for (; i <= numPlayers+1 ; i++)
		{
			newPlayers[i-2] = APlayer.parse(i-2, list.get(i));
		}
    	
		// Load the rounds
    	for (; i < list.size() ; i++)
    	{
    		rounds.add(Round.parse(list.get(i)));
    	}
    	
    	// If we are here, we have successfully parsed the file. We can now stop the play thread
    	
		// Cancelling the current thread
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

    	roundList = rounds;
		gameTask = gameExecutor.submit(new Runnable()
		{
			public void run()
			{
				init(newPlayers, sizeBoard);
				executeRounds();
				play();
			}
		});
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
