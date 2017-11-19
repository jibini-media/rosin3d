package net.jibini.rosin.vector;

/**
 * Three-component quantity with no discernible start which has direction and
 * magnitude, describes one position relative to another
 * 
 * @author Zach Goethel
 */
public class Vector
{
	public double x, y, z;
	
	public Vector(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(Vector source)
	{
		this(source.x, source.y, source.z);
	}
	
	public Vector(double x, double y)
	{
		this(x, y, 0.0);
	}
	
	public Vector()
	{
		this(0.0, 0.0);
	}
	
	public double length()
	{
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public void scale(double factor)
	{
		x *= factor;
		y *= factor;
		z *= factor;
	}
	
	public void add(Vector vector)
	{
		x += vector.x;
		y += vector.y;
		z += vector.z;
	}
	
	public void sub(Vector vector)
	{
		x -= vector.x;
		y -= vector.y;
		z -= vector.z;
	}
	
	public void normalize()
	{
		scale(1.0 / length());
	}
	
	@Override
	public String toString()
	{
		return "[" + x + ", " + y + ", " + z + "]";
	}
}
