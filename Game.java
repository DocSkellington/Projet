public class Game
{
    private Board board;
    private Player[] players;

    private int howManyPlayers()
    {
        // TODO : Asking user(s) how many Human players (+ exceptions)
        return 1;
    }
    
    private boolean keepPlaying()
    {
        // TODO : Asking if Player(s) want(s) to keep playing
        return true;
    }
    
    private void printVictory(int winner)
    {
        System.out.println("Congratulations, Player " + winner + " !");
    }
    
    private void init(int humNumber)
    {
        // TODO : Setting the good number of AI(s) and Human(s)
    }
    
    public static void main(String[] args)
    {
        boolean running = true;
        
        while (running)
        {
            int hum = howManyPlayers();
            init(hum);
        
            int current = 0, winner = -1; // -1 means no winner
            
            while (winner == -1)
            {
                Players[current].play();
                current = (current + 1) % 2;
                isWon = board.hasWon(players.length());
            }
            
            printVictory(winner);
            
            running = keepPlaying();
        }
    }
    
}
