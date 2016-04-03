package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/** Holds the textures used by the gui
 * 
 * @author Gaetan Staquet
 * @author Thibaut De Cooman
 *
 */
public class TextureHolder implements Cloneable
{
	private HashMap<String, BufferedImage> images;
	
	/** Default constructor
	 * 
	 */
	public TextureHolder()
	{
		images = new HashMap<String, BufferedImage>();
	}
	
	/** Loads an image in the holder
	 * 
	 * @param key The key is the ID of the image
	 * @param filepath The file path to the image to store
	 * @throws IOException If the file can't be read
	 */
	public void load(String key, String filepath) throws IOException
	{
		images.put(key, ImageIO.read(new File(filepath)));
	}
	
	/** Returns the image corresponding to the key
	 * 
	 * @param key The ID
	 * @return An image
	 */
	public BufferedImage get(String key)
	{
		return images.get(key);
	}
	
	/** Resets the holder
	 * 
	 */
	public void clear()
	{
		images.clear();
	}
	
	@Override
	public TextureHolder clone()
	{
		return null;
	}
}
