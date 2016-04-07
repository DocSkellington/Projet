package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import board.Board;
import players.Human;

public final class MoveButtonListener implements ActionListener
{
	private ActionButton wall;
	private Human human;
	private Board board;
	
	public MoveButtonListener(ActionButton wall, Human human, Board board)
	{
		this.wall = wall;
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
			wall.setBorder(null);
			human.move(board);
		}
	}
}
