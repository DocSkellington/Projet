package board;

import players.*;
import players.Human;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import gui.TextureHolder;
import pathFinder.*;

/** Manages all information about the board.
 * This includes setting walls, finding path, and so on.
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class Board
{
    protected ACell[][] cells;
    protected Coordinates[] playersPositions;
    protected ArrayList<Coordinates> possibleWalls;
    protected APlayer[] players;
    
    /** The constructor
     * @param players The array of players 
     * @param size The size of the board
     * @param holder The holder of all needed textures (can be null if in console mode)
     *
     */
    public Board(APlayer[] players, int size, TextureHolder holder)
    {
        cells = new ACell[2*size-1][2*size-1];
        this.players = players; 
        possibleWalls = new ArrayList<Coordinates>();
        int numPlayers = players.length;
        
        for (int x = 0 ; x < cells.length ; x++)
        {
            for (int y = 0 ; y < cells[0].length ; y++)
            {
                if((x % 2 == 0) && (y % 2 == 0))
                    cells[x][y] = new Case(holder);
                else
                {
                	cells[x][y] = new Wall(holder);
                	if (x % 2 != 1 || y % 2 != 1)
                		possibleWalls.add(new Coordinates(x, y));
                }
            }
        }
        
        playersPositions = new Coordinates[numPlayers];
        for (int i = 0 ; i < numPlayers ; i++)
        {
        	playersPositions[i] = startingPos(i);
        }
        
        update();
    }
    
    /** Draws the board in the command prompt district */
    public void print()
    {
        System.out.print(" ");
        for(int i = 0 ; i < cells.length ; i++)
        {
            System.out.print("_");
        }
        System.out.println();
        
        for (int x = 0 ; x < cells.length ; x++)
        {
            System.out.print("|");
            for (int y = 0 ; y < cells.length ; y++)
            {
            	cells[x][y].display();
            }
            System.out.println("|");
        }
        
        System.out.print(" ");
        for(int i = 0 ; i < cells.length ; i++)
        {
            //System.out.print("\u0304");
        	System.out.print("-");
        }
        System.out.println();
    }
    
    public void fill(JPanel panel, APlayer[] players)
    {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		for (int i = 0 ; i < cells[0].length ; i++)
		{
			for (int j = 0 ; j < cells.length ; j++)
			{
				// TODO : Size variable
				c.gridx = j;
				c.gridy = i;
				if (i % 2 == 0 && j % 2 == 0)
				{
					c.ipadx = 70;
					c.ipady = 66;
				}
				else if (i % 2 == 1 && j % 2 == 0)
				{
					c.ipadx = 70;
					c.ipady = 17;
				}
				else if (i % 2 == 0 && j % 2 == 1)
				{
					c.ipadx = 17;
					c.ipady = 66;
				}
				else
				{
					c.ipadx = 17;
					c.ipady = 17;
				}
				
				for (APlayer player : players)
				{
					if (player instanceof Human)
					{
						final Human human = (Human) player;
						final Board board = this;
						cells[i][j].setActionCommand("(" + j + ", " + i + ")");
						// Case
						if (i % 2 == 0 && j % 2 == 0)
						{
							cells[i][j].addActionListener(new ActionListener()
									{
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
									});
						}
						// Walls
						else
						{
							if (i % 2 != 1 || j % 2 != 1)
							{
								cells[i][j].addActionListener(new ActionListener()
										{
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
										});
							}
						}
					}
				}
				cells[i][j].setEnabled(false);
				panel.add(cells[i][j], c);
			}
		}
	}
    
    /** Sets a wall if it can
     * 
     * @param coord The coordinates of the wall
     * @return True if the wall is set, false otherwise.
     *  */
    public boolean setWall(Coordinates coord)
    {
    	if (coord.getX() == cells[0].length-1)
    		coord = new Coordinates(coord.getX() - 2, coord.getY());
    	if (coord.getY() == cells.length-1)
    		coord = new Coordinates(coord.getX(), coord.getY() - 2);
    	
    	int x = coord.getX(), y = coord.getY();
    	
        int x2 = x, y2 = y;
        // Vertical
        if (x % 2 == 1)
        	y2 += 2;
        // Horizontal
        else
        	x2 += 2;
        
    	if(canSetWall(coord))
    	{
            cells[y][x].setFilled(1);
            cells[(y2+y)/2][(x2+x)/2].setFilled(1);
            cells[y2][x2].setFilled(1);
            possibleWalls.remove(new Coordinates(y, x));
            possibleWalls.remove(new Coordinates((y2+y)/2, (x2+x)/2));
            possibleWalls.remove(new Coordinates(y2, x2));
            return true;
    	}
    	return false;
    }

    /** Checks if a wall can be set at coordinates (x;y) (The upper/right vertex).
     * Uses the A* algorithm.
     * @param coord The coordinates of the wall
     * @return True if the wall can be set, false otherwise
    */
    public boolean canSetWall(Coordinates coord)
    {
    	int x = coord.getX(), y = coord.getY();
        
        if (!tryWall(coord))
        	return false;
        
        int x2 = x, y2 = y;
        // Vertical
        if (x % 2 == 1)
        	y2 += 2;
        // Horizontal
        else
        	x2 += 2;
        
        // We set the wall
        cells[y][x].setFilled(1);
        cells[(y2+y)/2][(x2+x)/2].setFilled(1);
        cells[y2][x2].setFilled(1);
        
        // Is there still a path ?
        boolean none = false;
        for (APlayer player : players)
        {
        	Path path = findPath(player, false);
        	if (path == null)
        	{
        		none = true;
        		break;
        	}
        }
        // We destroy the walls.
	    cells[y][x].setFilled(0);
        cells[(y2+y)/2][(x2+x)/2].setFilled(0);
        cells[y2][x2].setFilled(0);
        if(none)
        {
        	return false;
        }
        return true;
    }
    
    /** Checks if there is a wall at the given coordinates
     * 
     * @param coord The coordinates
     * @return True if there is a wall (or if the coordinates are wrong), false otherwise
     */
    public boolean hasWall(Coordinates coord)
    {
    	if (!tryWall(coord))
    		return true;
    	
    	if (filled(coord) == 1)
    		return true;
    	return false;
    }
    
    /** Finds the shortest path (uses the A* algorithm)
     * 
     * @param numPlayer The number of the player
	 * @param withPlayer If we consider the other player(s)
     * @return The shortest path
     */
    public Path findPath(int numPlayer, boolean withPlayer)
    {
    	return findPath(players[numPlayer], withPlayer, new ClosestHeuristic());
    }
    
    /** Finds the shortest path (uses the A* algorithm)
     * 
     * @param player A reference to the player who needs a path
	 * @param withPlayer If we consider the other player(s)
     * @return The shortest path
     */
    public Path findPath(APlayer player, boolean withPlayer)
    {
    	return findPath(player, withPlayer, new ClosestHeuristic());
    }
    
    /** Return the best path by using the A* algorithm (which uses the given heuristic method)
     * @param player A reference to the player who needs a path
	 * @param withPlayer If we consider the other player(s)
     * @param heuri A reference to an heuristic class to determinate the best path
     * @return The best path if it exists, null otherwise
     */
    public Path findPath(APlayer player, boolean withPlayer, IAStarHeuristic heuri)
    {
    	AStarPathFinder pathFinder = new AStarPathFinder(this, heuri);
    	Coordinates start = playersPositions[player.getNum()];
    	Coordinates[] target = goal(player.getNum());
    	Path path = null;
    	for (int i = 0 ; i < target.length ; i++)
    	{
    		Path temp = pathFinder.findPath(player, withPlayer, start.getX(), start.getY(), target[i].getX(), target[i].getY());
    		if (path == null)
    			path = temp;
    		else
			{
    			if (temp != null)
	    		{
	    			if(path.getLength() > temp.getLength())
	    				path = temp;
	    			else if (path.getLength() == temp.getLength() &&
	    					target[i].getX() == target[target.length/2].getX() && target[i].getY() == target[target.length/2].getY())
	    				path = temp;
	    		}
			}
    	}
    	return path;
    }
    
    
    /** Return the content of a cell
     * @param x The x coordinate of a cell
     * @param y The y coordinate of a cell
     * @return The content of the cell
     */
    public int filled(int x, int y)
    {
    	if(x >= 0 && x < cells[0].length && y >= 0 && y < cells.length)
        {
        	return cells[y][x].filled();
        }
        return -1;
    }
    
    /** Return the content of a cell
     * @param coord The coordinates of the point
     * @return The content of the cell a the given position
     */
    public int filled (Coordinates coord)
    {
    	return filled(coord.getX(), coord.getY());
    }
    
    /** The function is used by the path finder algorithm to determinate the cost of a movement. For now, it simply returns 1
     * @param player The player who is trying to move
     * @param sx The x coordinate of the case we're coming from
     * @param sy The y coordinate of the case we're coming from
     * @param tx The x coordinate of the case we're coming to
     * @param ty The y coordinate of the case we're coming to
     * @return The relative cost of the movement
     */
    public float getCost(APlayer player, int sx, int sy, int tx, int ty)
    {
        return 1.f;
    }
    
    /** Gets a copy of the array of cells
     * @return An array of array of Cells
     */
    public ACell[][] getCells()
    {
        // We don't want to give a reference to the data, so we copy the array
        ACell[][] map = new ACell[cells.length][cells[0].length];
        for (int i = 0 ; i < map.length ; i++)
        {
            for (int j = 0 ; j < map.length ; j++)
            {
                // Since copying an object would simply copy the reference, we create new cells
                if (cells[i][j] instanceof Wall)
                    map[i][j] = cells[i][j].clone();
                else
                    map[i][j] = cells[i][j].clone();
            }
        }
        return map;
    }
    
    /** Get the x size of the board
     * @return The x size of the board
     */
    public int getXSize()
    {
        return cells.length;
    }

    /** Get the y size of the board
     * @return The y size of the board
     */
    public int getYSize()
    {
        return cells[0].length;
    }
    
    /** Get the number of players
     * 
     * @return The number of players
     */
    public int getPlayerNumber()
    {
    	return players.length;
    }
    
    /** Checks if a player has won
	 *
     * @return The number of the winner
     */
    public int hasWon()
    {
    	for (int i = 0 ; i < players.length ; i++)
    	{
    		if (hasWon(players[i].getNum()))
    			return players[i].getNum();
    	}
    	return -1;
    }
    
    /** Checks if the player with the given number has won
     * @param numPlayer The number of the player
     * @return True if the player has won, false otherwise
     */
    public boolean hasWon(int numPlayer)
    {
    	Coordinates[] g = goal(numPlayer);
    	for (int i = 0 ; i < g.length ; i++)
    	{
    		if (playersPositions[numPlayer].equals(g[i]))
    			return true;
    	}
    	return false;
    }
    
    /** Check if a player can move from start position to target position. These two cases must be adjacent.
     * 
     * @param start The coordinates of the starting position
     * @param target The coordinates of the target position
     * @param withPlayer If we consider other player(s)
     * @return True if a player can move to target position, false otherwise
     */

    public boolean blocked(Coordinates start, Coordinates target, boolean withPlayer)
    {
    	return blocked(start.getX(), start.getY(), target.getX(), target.getY(), withPlayer);
    }

    /** Check if a player can move from start position to target position. These two cases must be adjacent.
     * 
     * @param sx The x coordinate of the starting position
     * @param sy The y coordinate of the starting position
     * @param tx The x coordinate of the target position
     * @param ty The y coordinate of the target position
     * @param withPlayer If we consider other player(s)
     * @return True if the way is blocked between the 2 adjacent cases, false otherwise.
     */
    public boolean blocked(int sx, int sy, int tx, int ty, boolean withPlayer)
    {
    	if (Math.abs(sx - tx) > 2 || Math.abs(sy - ty) > 2)
    	{
    		throw new RuntimeException("Too far");
    	}
    	
    	if (filled((sx+tx)/2, (sy+ty)/2) != 0 || (filled(tx, ty) != 0 && withPlayer))
		{
    		return true;
		}
    	else
		{
    		return false;
		}
    }
    
    /** The starting position for a player
     * 
     * @param playerNum The number of the player
     * @return The starting position (Coordinates) for the given player
     */
    public Coordinates startingPos(int playerNum)
    {
		if (playerNum == 0)
			return new Coordinates(cells.length/2, cells.length-1);
		else if (playerNum == 1)
			return new Coordinates(cells.length/2,0);
		else if (playerNum == 2)
			return new Coordinates(0, cells.length/2);
		else
			return new Coordinates(cells.length-1, cells.length/2);
    }

    /** Returns the goal coordinates for a given player
     * 
     * @param playerNum The number of the player
     * @return The goal coordinates (array) for this player
     */
    public Coordinates[] goal(int playerNum)
    {
    	if (playerNum == 0)
        {
    		Coordinates[] goal = {new Coordinates(0,0), new Coordinates(2,0),new Coordinates(4,0),new Coordinates(6,0),new Coordinates(8,0),new Coordinates(10,0),new Coordinates(12,0),new Coordinates(14,0),new Coordinates(16,0)};
    		return goal;
        }
    	else if (playerNum == 1)
    	{
    		Coordinates[] goal = {new Coordinates(0,16), new Coordinates(2,16),new Coordinates(4,16),new Coordinates(6,16),new Coordinates(8,16),new Coordinates(10,16),new Coordinates(12,16),new Coordinates(14,16),new Coordinates(16,16)};
    		return goal;
    	}
    	else if (playerNum == 2)
    	{
    		Coordinates[] goal = {new Coordinates(16,0), new Coordinates(16,2),new Coordinates(16,4),new Coordinates(16,6),new Coordinates(16,8),new Coordinates(16,10),new Coordinates(16,12),new Coordinates(16,14),new Coordinates(16,16)};
    		return goal;
    	}
    	else
    	{
    		Coordinates[] goal = {new Coordinates(0,0), new Coordinates(0,2),new Coordinates(0,4),new Coordinates(0,6),new Coordinates(0,8),new Coordinates(0,10),new Coordinates(0,12),new Coordinates(0,14),new Coordinates(0,16)};
    		return goal;
    	}
    }
    
    /** Updates the cells */
    public void update ()
    {
    	// We reset cases
    	for (int i = 0 ; i < cells.length ; i+=2)
    	{
    		for (int j = 0 ; j < cells[0].length ; j+=2)
    		{
    			cells[i][j].setFilled(0);
    		}
    	}
    	
    	for (int i = 0 ; i < playersPositions.length ; i++)
    	{
    		Coordinates coord = playersPositions[i];
    		cells[coord.getY()][coord.getX()].setFilled(i + 1);
    	}
    	repaint();
    }
    
    /** A button dis/enabler
     * 
     * @param enabled Whether we want to enable or disable
     * @param coord The coordinates of the buttons to dis/enable
     */
    public void setEnabledButtons(boolean enabled, Coordinates[] coord)
    {
    	for (int i = 0 ; i < coord.length ; i++)
    	{
    		cells[coord[i].getY()][coord[i].getX()].setEnabled(enabled);
    	}
    }
    
    /** Disable every button */
    public void disableAll()
    {
    	for (int i = 0 ; i < cells.length ; i++)
    	{
    		for (int j = 0 ; j < cells[0].length ; j++)
    		{
    			cells[i][j].setEnabled(false);
    		}
    	}
    }
    
    /** Enable all possible walls buttons */
    public void enableWalls()
    {
    	for (Coordinates coord : possibleWalls)
    	{
    		cells[coord.getX()][coord.getY()].setEnabled(true);
    	}
    }
    
    /** Draws a translucent wall over the board
     * 
     * @param coord The upper-left corner of the wall
     * @param coloured Whether we colour the walls or not
     */
    public void colorAdjacentWalls(Coordinates coord, boolean coloured)
    {
    	if (coord.getX() == cells[0].length-1)
    		coord = new Coordinates(coord.getX() - 2, coord.getY());
    	if (coord.getY() == cells.length-1)
    		coord = new Coordinates(coord.getX(), coord.getY() - 2);
    	
    	int filled = 0;
    	if (coloured)
    	{
    		filled = 2;
        	if (coord.getX() % 2 == 0)
        	{
        		if (cells[coord.getY()][coord.getX()].filled() == 0 && cells[coord.getY()][coord.getX()+2].filled() == 0 && cells[coord.getY()][coord.getX()+1].filled() == 0)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(filled);
    	    		cells[coord.getY()][coord.getX()+1].setFilled(filled);
    	    		cells[coord.getY()][coord.getX()+2].setFilled(filled);
        		}
        	}
        	else
        	{
        		if (cells[coord.getY()][coord.getX()].filled() == 0 && cells[coord.getY()+2][coord.getX()].filled() == 0 && cells[coord.getY()+1][coord.getX()].filled() == 0)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(filled);
    	    		cells[coord.getY()+1][coord.getX()].setFilled(filled);
    	    		cells[coord.getY()+2][coord.getX()].setFilled(filled);
        		}
        	}
    	}
    	else
    	{
	    	if (coord.getX() % 2 == 0)
	    	{
        		if (cells[coord.getY()][coord.getX()].filled() != 1 && cells[coord.getY()][coord.getX()+2].filled() != 1 && cells[coord.getY()][coord.getX()+1].filled() != 1)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(filled);
    	    		cells[coord.getY()][coord.getX()+1].setFilled(filled);
    	    		cells[coord.getY()][coord.getX()+2].setFilled(filled);
        		}
	    	}
	    	else
	    	{
        		if (cells[coord.getY()][coord.getX()].filled() != 1 && cells[coord.getY()+2][coord.getX()].filled() != 1 && cells[coord.getY()+1][coord.getX()].filled() != 1)
        		{
    	    		cells[coord.getY()][coord.getX()].setFilled(filled);
    	    		cells[coord.getY()+1][coord.getX()].setFilled(filled);
    	    		cells[coord.getY()+2][coord.getX()].setFilled(filled);
        		}
	    	}
    	}
    }
    
    /** Checks if the player can be moved at the given coordinates, moves it if possible
     * 
     * @param num The number of the player
     * @param coord The coordinate the players wants to move to
     * @return True if the player can make the move, false otherwise
     */
    public boolean move (int num, Coordinates coord)
    {
    	Coordinates playerPos = playersPositions[num];
    	// If the target case is out of the board
    	if (coord.getY() >= cells.length || coord.getX() >= cells.length || coord.getX() < 0 || coord.getY() < 0)
    	{
    		System.out.println("Out of the board");
    		return false;
    	}
    	// If the target case is filled
    	if (cells[coord.getY()][coord.getX()].filled() > 0)
    		return false;
    	// If the player wants to move more than 4 cases away
    	if (Math.abs(coord.getX()-playerPos.getX()) > 4 || Math.abs(coord.getY()-playerPos.getY()) > 4)
    	{
    		System.out.println("Target case is too far from original position");
    		return false;
    	}
    	playerPos.move(coord.getX()-playerPos.getX(), coord.getY()-playerPos.getY());
    	return true;
    }
    
    /** Returns a copy of the coordinates of a player.
     * 
     * @param num The number of a player
     * @return The coordinates of this player
     */
    public Coordinates getCoordinates(int num)
    {
    	return new Coordinates(playersPositions[num].getX(), playersPositions[num].getY());
    }
    
    /** Checks if a wall can be set at a given position
     * 
     * @param coord The coordinates where we try to set the wall
     * @return True if the wall can be set
     */
    private boolean tryWall(Coordinates coord)
    {
    	int x = coord.getX(), y = coord.getY(), x2 = x, y2 = y;
    	
    	// Horizontal
    	if(x % 2 == 0)
    	{
    		if(y % 2 == 0)
    			return false;
    		x2 += 2;
    	}
    	// Vertical
    	else
    	{
    		if(y % 2 == 1)
    			return false;
    		y2 += 2;
    	}
    	
    	// In the board ?
    	if (x < 0 || x2 >= cells[0].length || y < 0 || y2 >= cells.length)
    	{
    		return false;
    	}
    	if ((filled(x, y) != 0 || filled(x2, y2) != 0 || filled((x+x2)/2, (y+y2) / 2) != 0) && (filled(x, y) != 2 || filled(x2, y2) != 2 || filled((x+x2)/2, (y+y2) / 2) != 2))
    		return false;
    	
    	return true;
    }
    
    // Repaint the board
    private void repaint()
    {
    	for (int i = 0 ; i < cells.length ; i++)
    	{
    		for (int j = 0 ; j < cells[0].length ; j++)
    			cells[i][j].repaint();
    	}
    }
    
    /** Returns the maximum number of players for this board
     * 
     * @return The maximum number of players
     */
    public static int maxPlayers()
    {
    	return 4;
    }
}
