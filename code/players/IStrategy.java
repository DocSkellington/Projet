package players;

import board.*;

/** Base shape of the strategies
 * 
 * @author De Cooman Thibaut
 * @author Staquet Gaetan
 *
 */
public interface IStrategy
{
	/** Determines the best round to use, according to the strategy
	 * 
	 * @param board The board
	 * @param numPlayer The number of the player
	 * @param possibleMoves The possible moves of the player
	 * @param numRounds The number of already played rounds
	 * @return A round
	 */
	Round strategy(Board board, int numPlayer, Coordinates[] possibleMoves, int numRounds);
}
