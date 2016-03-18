import java.util.Scanner;
import board.*;
import pathFinder.Path;
import players.*;

/** Main class that keeps the game running
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class Game
{
	private APlayer[] players;
	private Board board;
	
	/** Default constructor
	 * 
	 */
	public Game()
	{
		
	}
	
	/** What keeps the game running
	 * 
	 */
	public void run()
	{
        boolean running = true;
        
        while (running)
        {
            int numPlayers = howManyPlayers(), hum = howManyHumans(numPlayers), randAINum = howManyRandom(numPlayers, hum);
            init(numPlayers, hum, randAINum);
        
            int current = 0, winner = -1; // -1 means no winner
            
            //board.setWall(new Coordinates(8, 3));
            /*board.setWall(new Coordinates(10, 7));
            board.setWall(new Coordinates(5, 8));
            board.setWall(new Coordinates(6, 9));
            board.setWall(new Coordinates(10, 9));
            board.setWall(new Coordinates(14, 9));
            board.setWall(new Coordinates(13, 8));*/
            
            /*Path path = board.findPath(1, true);
            if(path != null)
            {
            	for (int i = 0 ; i < path.getLength() ; i++)
            	System.out.println(path.getX(i) + " " + path.getY(i));
            }
            path = board.findPath(1, false);
            for (int i = 0 ; i < path.getLength() ; i++)
            	System.out.println(path.getX(i) + " " + path.getY(i));*/
            
            while (winner == -1)
            {
            	board.print();
                players[current].play(board);
                board.update();
                current = (current + 1) % numPlayers;
                winner = board.hasWon();
            }
            
            printVictory(winner);
            
            running = keepPlaying();
        }
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
    	while(i < playersNumber-randAINum)
    	{
    		players[i] = new StrategyAI(i++, walls);
    	}
    	while(i < playersNumber)
    	{
    		players[i] = new RandomAI(i++, walls);
    	}
    	
        board = new Board(players);
        // This is used to hash the coordinates:
        Coordinates.size = board.getYSize();
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
