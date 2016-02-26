import java.util.Scanner;
import board.*;
import players.*;

public class Game
{
	private APlayer[] players;
	private Board board;
	
	public Game()
	{
		
	}
	
	public void run()
	{
        boolean running = true;
        
        while (running)
        {
            int hum = howManyPlayers();
            init(hum);
        
            int current = 0, winner = -1; // -1 means no winner
            Board board = new Board();
            
            while (winner == -1)
            {
            	board.print();
                players[current].play();
                current = (current + 1) % 2;
                winner = board.hasWon(players);
            }
            
            printVictory(winner);
            
            running = keepPlaying();
        }
	}
	
    private int howManyPlayers()
    {
        // TODO : Asking user(s) how many Human players (+ exceptions)
        return 1;
    }
    
    private boolean keepPlaying()
    {
        // TODO : Asking if Player(s) want(s) to keep playing
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
    
    private void printVictory(int winner)
    {
        System.out.println("Congratulations, Player " + winner + " !");
    }
    
    private void init(int humNumber)
    {
    	players = new APlayer[humNumber];
    	
    	players[0] = new Human(board, 0);
    }
    
    
    
    public static void main(String[] args)
    {
    	Game game = new Game();
    	game.run();
    }
    
}
