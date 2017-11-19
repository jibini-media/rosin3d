package net.jibini.rosin.frame;

import net.jibini.rosin.pixel.Pixel;

/**
 * Contains screen color and depth information
 * 
 * @author Zach Goethel
 */
public interface FrameData
{
	Pixel getPixel(int x, int y);
	
	int getWidth();
	
	int getHeight();
}
