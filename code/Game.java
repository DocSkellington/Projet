import java.util.Scanner;
import board.*;
import board.Board.*;
import players.*;

/** Main class that keeps the game running
 * 
 * @author Gaetan Staquet & Thibaut De Cooman
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
            int numPlayers = howManyPlayers(), hum = howManyHumans(numPlayers);
            board = new Board(numPlayers);
            init(numPlayers, hum);
        
            int current = 0, winner = -1; // -1 means no winner
            
            while (winner == -1)
            {
            	board.print();
                players[current].play();
                board.update();
                current = (current + 1) % numPlayers;
                winner = board.hasWon(players);
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
    				System.out.println("An Integer, por favor, Señior");
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
				return res;
    		
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
    				System.out.println("An Integer, por favor, Señior");
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
				return res;
    		
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
    			return true;
    		else if (res.equals("N"))
    			return false;
    		
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
    private void init(int playersNumber, int humNumber)
    {
    	players = new APlayer[playersNumber];
    	
    	int i = 0;
    	while(i < humNumber)
    	{
    		players[i] = new Human(board, i);
    		i++;
    	}
    	//while(i < playersNumber)
    		//players[i] = new Bot(board, 0);
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
