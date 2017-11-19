package net.jibini.rosin.frame;

import net.jibini.rosin.pixel.Pixel;

public interface FrameData
{
	Pixel getPixel(int x, int y);
	
	int getWidth();
	
	int getHeight();
}
