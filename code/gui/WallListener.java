package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import board.Board;
import board.Coordinates;
import players.Human;

public final class WallListener implements ActionListener
{
	private Human human;
	private Board board;
	
	public WallListener(Human human, Board board)
	{
		this.human = human;
		this.board = board;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (human.isActive())
		{
			String message = e.getActionCommand();
			
			if (message.charAt(0) == 'l')
			{
				try
				{
					Coordinates coord = Coordinates.parse(message.substring(2));
					board.colorAdjacentWalls(coord, true);
				}
				catch(ParseException ex)
				{
					throw new RuntimeException("Can not parse action command" + ex);
				}
			}
			else if (message.charAt(0) == 'u')
			{
				try
				{
					Coordinates coord = Coordinates.parse(message.substring(2));
					board.colorAdjacentWalls(coord, false);
				}
				catch(ParseException ex)
				{
					throw new RuntimeException("Can not parse action command" + ex);
				}
			}
			else
			{

				try
				{
					Coordinates coord = Coordinates.parse(message);
					human.setWall(board, coord);
				}
				catch(ParseException ex)
				{
					throw new RuntimeException("Can not parse action command" + ex);
				}
			}
		}
	}
}
