public class Cell
{
    protected int filled;
    
    public Cell()
    {
        filled = 0;
    }
    
    public Cell(int filled)
    {
    	this.filled = filled;
    }
    
    public void setFilled(int f)
    {
        filled = f;
    }
    
    public int filled()
    {
        return filled;
    }
}
