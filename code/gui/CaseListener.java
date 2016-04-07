package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;

import board.Board;
import board.Coordinates;
import players.Human;

public final class CaseListener implements ActionListener
{
	private Human human;
	private Board board;
	
	public CaseListener(Human human, Board board)
	{
		this.human = human;
		this.board = board;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (human.isActive())
		{
			try
			{
				Coordinates coord = Coordinates.parse(((JButton) e.getSource()).getActionCommand());
				human.doMove(board, coord);
			}
			catch (ParseException ex)
			{
				throw new RuntimeException("Can not parse action command" + ex);
			}
		}
	}
}
