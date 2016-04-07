package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.JButton;

import board.Board;
import players.Human;

public class WallButtonListener implements ActionListener
{
	private ActionButton move;
	private Human human;
	private Board board;
	
	public WallButtonListener(ActionButton move, Human human, Board board)
	{
		this.move = move;
		this.human = human;
		this.board = board;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (human.isActive())
		{
			Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			((JButton) e.getSource()).setBorder(border);
			move.setBorder(null);
			human.walls(board);
		}
	}
}
