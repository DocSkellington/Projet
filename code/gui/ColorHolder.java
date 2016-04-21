package gui;

import java.awt.Color;
import java.util.HashMap;

/** Holds the colours used by the gui
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public final class ColorHolder
{
	private HashMap<Integer, Color> colors;
	
	/** Default constructor
	 * 
	 */
	public ColorHolder()
	{
		colors = new HashMap<Integer, Color>();
	}
	
	/** Loads a colour in the holder
	 * 
	 * @param key The key is the ID of the image
	 * @param color The colour to store
	 */
	public void load(Integer key, Color color)
	{
		colors.put(key, color);
	}
	
	/** Returns the colour corresponding to the key
	 * 
	 * @param key The ID
	 * @return An image
	 */
	public Color get(Integer key)
	{
		return colors.get(key);
	}
	
	/** Resets the holder
	 * 
	 */
	public void clear()
	{
		colors.clear();
	}
}
