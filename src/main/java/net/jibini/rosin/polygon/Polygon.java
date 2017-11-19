package net.jibini.rosin.polygon;

import net.jibini.rosin.vector.Vector;

/**
 * A shape with three vertices, a triangle if you will
 * 
 * @author Zach Goethel
 */
public class Polygon
{
	public Vector a, b, c;
	
	public Polygon(Vector a, Vector b, Vector c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}
}
