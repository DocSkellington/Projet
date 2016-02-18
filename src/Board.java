import java.util.ArrayList;

public class Board
{
    protected Cell[][] cells;
    
    /** The basic constructor
    */
    public Board()
    {
        cells = new Cell[17][17];
        
        for (int x = 0 ; x < 17 ; x++)
        {
            for (int y = 0 ; y < 17 ; y++)
            {
                if((x % 2 == 0) && (y % 2 == 0))
                    cells[x][y] = new Case();
                else
                    cells[x][y] = new Wall();
            }
        }
        
        cells[2][4].setFilled(1);
        cells[2][6].setFilled(2);
        cells[2][5].setFilled(1);
        
        ArrayList<Coordinates> coords = neighbours(new Coordinates(0, 16));
        for (int i = 0 ; i < coords.size() ; i++)
        {
            System.out.println(coords.get(i).x + " " + coords.get(i).y);
        }
    }
    
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
                if (cells[x][y] instanceof Wall)
                {
                    if(cells[x][y].filled() == 0)
                        System.out.print(" ");
                    else
                        System.out.print("#");
                }
                else
                {
                    // 1 = J1
                    if (cells[x][y].filled() == 1)
                        System.out.print("1");
                    else if (cells[x][y].filled() == 2)
                        System.out.print("2");
                    else
                        System.out.print("0");
                }
            }
            System.out.println("|");
        }
        
        System.out.print(" ");
        for(int i = 0 ; i < cells.length ; i++)
        {
            System.out.print("‾");
        }
        System.out.println();
    }
    
    /** Tries to set a wall at coordinate (x;y) (The upper/right vertex). If possible, effectively sets the wall and returns true. Otherwise, returns false without modifying the board.
        @param x The x coordinate
        @param y The y coordinate
        @param horizontal If the wall is horizontal
    */
    public boolean setWall(int x, int y, boolean horizontal)
    {
        if(cells[x][y] instanceof Wall)
            return false;
        return true;
    }
    
    /** Uses the A* algorithm 
    See : http://www.cokeandcode.com/main/tutorials/path-finding/
    https://fr.wikipedia.org/wiki/Algorithme_A* */
    public boolean isTherePath(Coordinates departure, int player)
    {
        /*ArrayList<Coordinates> closed = new ArrayList();
        ArrayList<Coordinates> open = new ArrayList();
        open.add(depart);
        Coordinates[] goal = goal(player);
        for (int i = 0 ; i < goal.length ; i++)
        {
            while (open.length > 0)
            {
                Coordinates u = open.remove(0);
                if ((u.x == goal[i].x) && (u.y == goal[i].y))
                  return true;
                else
                {
                    ArrayList<Coordinates> neigh = neighbours(u);
                    for (int i = 0 ; i < neigh.size() ; i++)
                    {
                        if (neigh[i] == 0)
                        {
                            if(closed.contains(neigh[i]) || closed.contains(neigh[i]))
                            {
                            
                            }
                            else
                            {
                                neigh
                            }
                        }
                    }
                }
            }
        }*/
        return true;
    }
    
    public Coordinates[] goal(int player)
    {
        Coordinates[] goal = {new Coordinates(0,0), new Coordinates(0,2),new Coordinates(0,4),new Coordinates(0,6),new Coordinates(0,8),new Coordinates(0,10),new Coordinates(0,12),new Coordinates(0,14),new Coordinates(0,16)};
        return goal;
        
    }
    
    public int filled(int x, int y)
    {
        if(x >= 0 && x < cells.length && y >= 0 && y < cells[0].length)
            return cells[x][y].filled();
        return -1;
    }
    
    /** The function is used by the path finder algorithm to determinate the cost of a movement. For now, it simply returns 1
     * @param player The player who is trying to move
     * @param sx The x coordinate of the case we're coming from
     * @param sy The y coordinate of the case we're coming from
     * @param tx The x coordinate of the case we're coming to
     * @param ty The y coordinate of the case we're coming to
     * @return The relative cost of the movement
     */
    public float getCost(Player player, int sx, int sy, int tx, int ty)
    {
        return 1.;
    }
    
    public Cells[][] getCells()
    {
        // We don't want to give a reference to the data, so we copy the array
        Cells[][] map = new Cells[cells.length][cells[0].length];
        for (int i = 0 ; i < map.length ; i++)
        {
            for (int j = 0 ; j < map.length ; j++)
            {
                // Since copying an object would simply copy the reference, we create new 
                if (cells[i][j] instanceof Wall)
                    map[i][j] = Wall(cells[i][j].filled());
                else
                    map[i][j] = Case(cells[i][j].filled());
            }
        }
    }
    
    public int getXSize()
    {
        return map.length;
    }
    
    public int getYSize()
    {
        return map[0].length;
    }
    
    private ArrayList<Coordinates> neighbours(Coordinates coord)
    {
        ArrayList<Coordinates> neighbours = new ArrayList();
        if ((coord.x - 2 >= 0) && (filled(coord.x - 1, coord.y) == 0))
            neighbours.add(new Coordinates(coord.x-2, coord.y));
        if ((coord.x + 2 < cells.length) && (filled(coord.x + 1, coord.y) == 0))
            neighbours.add(new Coordinates(coord.x+2, coord.y));
        if ((coord.y - 2 >= 0) && (filled(coord.x, coord.y - 1) == 0))
            neighbours.add(new Coordinates(coord.x, coord.y - 2));
        if ((coord.y + 2 < cells.length) && (filled(coord.x, coord.y + 1) == 0))
            neighbours.add(new Coordinates(coord.x, coord.y + 2));
        return neighbours;       
    }
    
    private int compare2Nodes(Node n1, Node n2)
    {
        if (n1.heuristic < n2.heuristic)
            return 1;
        else if (n1.heuristic == n2.heuristic)
            return 0;
        else
            return -1;
    }
}
