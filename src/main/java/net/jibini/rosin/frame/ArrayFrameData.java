package net.jibini.rosin.frame;

import net.jibini.rosin.pixel.MappedPixel;
import net.jibini.rosin.pixel.Pixel;

public class ArrayFrameData implements FrameData
{
	public static final int ELEMENT_RED = 0;
	public static final int ELEMENT_GREEN = 1;
	public static final int ELEMENT_BLUE = 2;
	public static final int ELEMENT_ALPHA = 3;
	public static final int ELEMENT_DEPTH = 4;
	public static final int ELEMENTS = 5;
	
	private double[][][] frame;
	private int width, height;
	
	public ArrayFrameData(int width, int height)
	{
		frame = new double[width][height][ELEMENTS];
		this.width = width;
		this.height = height;
	}

	public Pixel getPixel(int x, int y)
	{
		return new MappedPixel(this, x, y);
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
	
	public double[][][] getInternal()
	{
		return frame;
	}
	
	public double getInternalElement(int x, int y, int id)
	{
		return frame[x][y][id];
	}
	
	public void setInternalElement(int x, int y, int id, double value)
	{
		frame[x][y][id] = value;
	}
}
