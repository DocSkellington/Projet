import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.text.ParseException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import board.*;
import gui.TextureHolder;
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
	private JFrame frame;
	
	/** Default constructor
	 * 
	 */
	public Game()
	{
		roundList = new ArrayList<Round>();
		
		// Init of graphics environment
		GraphicsEnvironment a = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] b = a.getScreenDevices();
		Rectangle c = b[0].getDefaultConfiguration().getBounds();
		frame = new JFrame("Quoridor");
		frame.setSize(1024, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation((c.width-frame.getSize().width)/2, (c.height-frame.getSize().height)/2);
		frame.setResizable(false);
	}
	
	/** What keeps the game running
	 * 
	 */
	public void run()
	{
		BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS);
		frame.setLayout(layout);
		
		JPanel main = new JPanel();
		main.setLayout(new GridBagLayout());
		main.setPreferredSize(new Dimension(768, 768));
		
		JPanel move = new JPanel(), wall = new JPanel();
		move.add(new JButton("You don't want to move"));
		wall.add(new JButton("There is a wall"));
		
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(move);
		box.add(wall);
		
		frame.add(main);
		frame.add(box);
		frame.setVisible(true);
        
        boolean running = true;
        
        //while (running)
        //{
            int numPlayers = 4, hum = 0, randAINum = 0;
            init(numPlayers, hum, randAINum);
    		board.fill(main);
    		main.repaint();
    		main.revalidate();
    		
            int current = 0, winner = -1; // -1 means no winner
            
            while (winner == -1)
            {
                roundList.add(players[current].play(board));
                board.update();
                current = (current + 1) % numPlayers;
                winner = board.hasWon();
            }
            printVictory(winner);
            
            /*boolean save = wannaSave();
            if (save)
            {
            	try
            	{
            		save("Save/Save.txt");
            	}
            	catch(IOException e)
            	{
            		System.out.println(e);
            	}
            }*/
            
            //running = keepPlaying();
            running = false;
        //}
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
    
    /** Initializes the players list
     * 
     * @param playersNumber The number of players
     * @param humNumber The number of human players
     */
    private void init(int playersNumber, int humNumber, int randAINum)
    {
    	players = new APlayer[playersNumber];
    	
    	int walls = 10;
    	if (playersNumber == 4)
    		walls = 5;
    	else if (playersNumber == 3)
    		walls = 7;
    	
    	int i = 0;
    	while(i < humNumber)
    	{
    		players[i] = new Human(i++, walls);
    	}
    	while(i < humNumber + randAINum)
    	{
    		players[i] = new StrategyAI(i++, walls, new RandomStrategy());
    	}
    	while(i < playersNumber)
    	{
    		players[i] = new StrategyAI(i++, walls);
    	}
    	
        TextureHolder textureHolder = new TextureHolder();
        try
        {
        	textureHolder.load("case", "../data/case.jpg");
        	textureHolder.load("wallEmpty", "../data/wallEmpty.jpg");
        	textureHolder.load("wallFilled", "../data/wallFill.jpg");
        }
        catch(IOException e)
        {
        	System.out.println(e);
        }
    	
        board = new Board(players, textureHolder);
        // This is used to hash the coordinates:
        Coordinates.size = board.getYSize();
    }
    
    private void save(String filepath) throws IOException
    {
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
