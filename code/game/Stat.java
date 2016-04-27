package game;
import board.*;
import players.*;

import java.text.ParseException;
import java.util.*;

/** The stats class
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class Stat 
{
	private int gameNum;
	private APlayer[] playerList;
	private IStrategy[] strategies;
	
	/** The constructor
	 * 
	 * @param gameNum The number of test games
	 * @param strategy The strategy the AIs use in the tests
	 */
	public Stat(int gameNum, IStrategy[] strategy)
	{
		this.gameNum = gameNum;
		playerList = new APlayer[strategy.length];
		strategies = strategy;
	}
	
	/** Runs the game 
	 * 
	 * @return The number of wins for each player in an array
	 */
	public int[] run()
	{
        int[] winnerList = new int[playerList.length];
        for (int i = 0 ; i < winnerList.length ; i++)
        {
        	winnerList[i] = 0;
        }
        int start = 0, cur = 0;
		while (cur++ < gameNum)
		{
			System.out.println("Starting game n°" + cur + "/" + gameNum);
			Board board = new Board(playerList, 9, 9);
			int wallCounter = 10;
			if (playerList.length == 3)
				wallCounter = 7;
			else if (playerList.length == 4)
				wallCounter = 5;
			
			for (int i = 0 ; i < playerList.length ; i++) 
			{
				playerList[i] = new StrategyAI(i, wallCounter, strategies[(i+start)%strategies.length]);
			}
			
			start = (start+1) % playerList.length;

            int current = 0, winner = -1; // -1 means no winner
            
            while (winner == -1)
            {
                playerList[current].play(board);
                board.update();
                current = (current + 1) % playerList.length;
                winner = board.hasWon();
            }
            if ((winner+start-1)%playerList.length == -1)
                winnerList[winnerList.length-1]++;
		    else
		        winnerList[(winner+start-1)%winnerList.length]++;
		}
		return winnerList;
	}
	
	/** The main method for the stat module
	 * 
	 * @param args The arguments
	 */
	public static void main(String[] args)
	{
		int gameNum = Integer.parseInt(args[0]);
		ArrayList<IStrategy> strategy = new ArrayList<IStrategy>();
		for (int i = 1 ; i < args.length ; i++)
		{
			if (args[i].equals("random"))
				strategy.add(new RandomStrategy());
			else if (args[i].equals("shiller"))
				strategy.add(new ShillerStrategy());
			else if (args[i].equals("straight"))
				strategy.add(new StraightStrategy());
			else
				throw new RuntimeException("No such AI. Please refer to the manual");
		}
		Stat stat = new Stat(gameNum, strategy.toArray(new IStrategy[0]));
		int[] winnerList = stat.run();
		for (int i = 0 ; i < winnerList.length ; i++)
		{
			System.out.printf("Player %d has %d wins. They have won %.3f %% of their games.\n", i+1, winnerList[i], (((double)winnerList[i]/gameNum)*100));
		}
	}
	
}
