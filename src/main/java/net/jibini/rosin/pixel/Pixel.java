package net.jibini.rosin.pixel;

/**
 * Screen texel with RGBA and depth attributes
 * 
 * @author Zach Goethel
 */
public interface Pixel
{
	double getRed();
	
	void setRed(double value);
	
	double getGreen();
	
	void setGreen(double value);
	
	double getBlue();
	
	void setBlue(double value);
	
	double getAlpha();
	
	void setAlpha(double value);
	
	double getDepth();
	
	void setDepth(double value);
}
