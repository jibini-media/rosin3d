package net.jibini.rosin.rasterizer;

import java.util.Arrays;
import java.util.Comparator;

import net.jibini.rosin.frame.FrameData;
import net.jibini.rosin.polygon.Polygon;
import net.jibini.rosin.vector.Ray;
import net.jibini.rosin.vector.Vector;

public class Rasterizer
{
	public void rasterize(Polygon poly, FrameData frame)
	{
		rasterize(poly.a, poly.b, poly.c, frame);
	}
	
	public void rasterize(Vector a, Vector b, Vector c, FrameData frame)
	{
		Vector[] ordered = { a, b, c };
		Arrays.sort(ordered, new Comparator<Vector>()
				{
					public int compare(Vector arg0, Vector arg1)
					{
						if (arg0.y < arg1.y) return 1;
						if (arg0.y > arg1.y) return -1;
						return 0;
					}
				});
		
		if (ordered[0].y == ordered[1].y)
		{
			// Polygon has flat bottom
			rasterizeFlat(ordered[1], ordered[0], ordered[2], frame);
		} else if (ordered[1].y == ordered[2].y)
		{
			// Polygon has flat top
			rasterizeFlat(ordered[1], ordered[2], ordered[0], frame);
		} else
		{
			// Calculate D as split in AC horizontal with B
			Vector acVec = new Vector(ordered[2]);
			acVec.sub(ordered[0]);
			Ray ac = new Ray(ordered[0], acVec);
			Vector d = new Vector(ac.getX(ordered[1].y), ordered[1].y, 0.0);
			
			// Draw BDA and BDC
			rasterizeFlat(ordered[1], d, ordered[0], frame);
			rasterizeFlat(ordered[1], d, ordered[2], frame);
		}
	}
	
	public void rasterizeFlat(Vector a, Vector b, Vector c, FrameData frame)
	{
		if (a.y != b.y)
			throw new UnsupportedOperationException("AB must be horizontal");
		if (a.x > b.x)
		{
			// A should be left of b, swap order
			rasterizeFlat(b, a, c, frame);
			return;
		}
		
		Vector acVec = new Vector(c);
		acVec.sub(a);
		Ray ac = new Ray(a, acVec);
		double acLen = acVec.length();
		
		Vector bcVec = new Vector(c);
		bcVec.sub(b);
		Ray bc = new Ray(b, bcVec);
		double bcLen = bcVec.length();
		
		// Decide whether to go up or down
		int start = (int)a.y, stop = (int)c.y;
		int inc = stop > start ? 1 : -1;
		
		for (int y = start; y != stop + inc; y += inc)
		{
			try
			{
				int begin = (int)ac.getPoint(acLen * (double)(y - start) / (stop - start)).x;
				int end = (int)bc.getPoint(bcLen * (double)(y - start) / (stop - start)).x;
				
				for (int x = begin; x <= end; x ++)
				{
					frame.getPixel(x, y).setRed(1.0);
					frame.getPixel(x, y).setGreen(1.0);
					frame.getPixel(x, y).setBlue(1.0);
				}
			} catch (IndexOutOfBoundsException ignore)
			{ }
		}
	}
}
