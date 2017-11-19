package net.jibini.rosin.pixel;

import net.jibini.rosin.frame.ArrayFrameData;

public class MappedPixel implements Pixel
{
	private ArrayFrameData frame;
	private int x, y;
	
	public MappedPixel(ArrayFrameData frame, int x, int y)
	{
		this.frame = frame;
		this.x = x;
		this.y = y;
	}
	
	public double getRed()
	{
		return frame.getInternalElement(x, y, ArrayFrameData.ELEMENT_RED);
	}
	
	public void setRed(double value)
	{
		frame.setInternalElement(x, y, ArrayFrameData.ELEMENT_RED, value);
	}

	public double getGreen()
	{
		return frame.getInternalElement(x, y, ArrayFrameData.ELEMENT_GREEN);
	}
	
	public void setGreen(double value)
	{
		frame.setInternalElement(x, y, ArrayFrameData.ELEMENT_GREEN, value);
	}

	public double getBlue()
	{
		return frame.getInternalElement(x, y, ArrayFrameData.ELEMENT_BLUE);
	}
	
	public void setBlue(double value)
	{
		frame.setInternalElement(x, y, ArrayFrameData.ELEMENT_BLUE, value);
	}

	public double getAlpha()
	{
		return frame.getInternalElement(x, y, ArrayFrameData.ELEMENT_ALPHA);
	}
	
	public void setAlpha(double value)
	{
		frame.setInternalElement(x, y, ArrayFrameData.ELEMENT_ALPHA, value);
	}

	public double getDepth()
	{
		return frame.getInternalElement(x, y, ArrayFrameData.ELEMENT_DEPTH);
	}
	
	public void setDepth(double value)
	{
		frame.setInternalElement(x, y, ArrayFrameData.ELEMENT_DEPTH, value);
	}
}
