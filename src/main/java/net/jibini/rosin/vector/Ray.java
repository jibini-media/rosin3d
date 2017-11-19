package net.jibini.rosin.vector;

public class Ray extends Vector
{
	private Vector vector, normal;
	
	public Ray(Vector origin, Vector vector)
	{
		super(origin);
		this.vector = vector;
		
		normal = new Vector(vector);
		normal.normalize();
	}
	
	public double length()
	{
		return vector.length();
	}
	
	public Vector getVector()
	{
		return vector;
	}
	
	public Vector getNormal()
	{
		return normal;
	}
	
	public Vector getPoint(double t)
	{
		Vector point = new Vector(normal);
		point.scale(t);
		point.add(this);
		return point;
	}
	
	public double getX(double y)
	{
		return normal.x * ((y - this.y) / normal.y) + x;
	}
}
