package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class TextureHolder implements Cloneable
{
	private HashMap<String, BufferedImage> images;
	
	public TextureHolder()
	{
		images = new HashMap<String, BufferedImage>();
	}
	
	public void load(String key, String filepath) throws IOException
	{
		images.put(key, ImageIO.read(new File(filepath)));
	}
	
	public BufferedImage get(String key)
	{
		return images.get(key);
	}
	
	public void clear()
	{
		images.clear();
	}
	
	public TextureHolder clone()
	{
		/*HashMap<String, BufferedImage> img = (HashMap<String, BufferedImage>) images.clone();
		TextureHolder tex = new TextureHolder();
		tex.images = img;
		return tex;*/
		return null;
	}
}
