package net.jibini.rosin.rasterizer;

import java.util.Arrays;
import java.util.Comparator;

import net.jibini.rosin.frame.FrameData;
import net.jibini.rosin.pixel.Pixel;
import net.jibini.rosin.polygon.Polygon;
import net.jibini.rosin.vector.Ray;
import net.jibini.rosin.vector.Vector;

/**
 * Applies world coordinates to the screen and creates solid figures from
 * vertices
 * 
 * @author Zach Goethel
 */
public class Rasterizer
{
	public void rasterize(Polygon poly, FrameData frame)
	{
		rasterize(poly.a, poly.b, poly.c, frame);
	}
	
	public void rasterize(Vector a, Vector b, Vector c, FrameData frame)
	{
		Vector[] ordered =
		{ a, b, c };
		Arrays.sort(ordered, new Comparator<Vector>()
		{
			public int compare(Vector arg0, Vector arg1)
			{
				if (arg0.y < arg1.y)
					return 1;
				if (arg0.y > arg1.y)
					return -1;
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
			Vector d = new Vector(ac.getX(ordered[1].y), ordered[1].y, ac.getZ(ordered[1].y));
			
			// Draw BDA and BDC
			rasterizeFlatSection(ordered[1], d, ordered[0], ordered[0], ordered[1], ordered[2], frame);
			rasterizeFlatSection(ordered[1], d, ordered[2], ordered[0], ordered[1], ordered[2], frame);
		}
	}
	
	public void rasterizeFlat(Vector a, Vector b, Vector c, FrameData frame)
	{
		rasterizeFlatSection(a, b, c, a, b, c, frame);
	}
	
	public void rasterizeFlatSection(Vector a, Vector b, Vector c, Vector aOriginal, Vector bOriginal, Vector cOriginal,
			FrameData frame)
	{
		if (a.y != b.y)
			throw new UnsupportedOperationException("AB must be horizontal");
		if (a.x > b.x)
		{
			// A should be left of B, swap order
			rasterizeFlatSection(b, a, c, aOriginal, bOriginal, cOriginal, frame);
			return;
		}
		
		// Create rays from AC and BC
		Vector acVec = new Vector(c);
		acVec.sub(a);
		Ray ac = new Ray(a, acVec);
		double acLen = acVec.length();
		
		Vector bcVec = new Vector(c);
		bcVec.sub(b);
		Ray bc = new Ray(b, bcVec);
		double bcLen = bcVec.length();
		
		// Upper and lower screen bounds and screen-clamped values
		double start = (a.y - 1.0) / -2.0 * frame.getHeight();
		double stop = (c.y - 1.0) / -2.0 * frame.getHeight();
		int clampStart = Math.max(0, Math.min((int) start, (int) stop));
		int clampStop = Math.min(frame.getHeight() - 1, Math.max((int) start, (int) stop));
		
		for (int y = clampStart; y < clampStop; y += 1)
		{
			// Interpolate left and right screen bounds on AC and BC
			double yProg = (y - start) / (stop - start);
			double begin = (ac.getPoint(acLen * yProg).x + 1.0) / 2.0 * frame.getWidth();
			double end = (bc.getPoint(bcLen * yProg).x + 1.0) / 2.0 * frame.getWidth();
			
			// Screen-clamped values
			int clampBegin = Math.max(0, (int) begin);
			int clampEnd = Math.min(frame.getWidth() - 1, (int) end);
			
			for (int x = clampBegin; x <= clampEnd; x++)
			{
				// double xProg = (double)(x - begin) / (end - begin);
				Pixel pixel = frame.getPixel(x, y);
				double[] weights = new double[3];
				
				// Calculate weights using world coordinate
				double worldX = ((double)x / frame.getWidth() * 2.0) - 1.0;
				double worldY = ((double)y / frame.getHeight()) * -2.0 + 1.0;
				calculateBarycentricWeights(aOriginal, bOriginal, cOriginal, new Vector(worldX, worldY, 0.0), weights);
				
				// Calculate interpolated depth
				double depth = weights[0] * aOriginal.z + weights[1] * bOriginal.z + weights[2] * cOriginal.z;
				boolean valid = (weights[0] <= 1.13) && (weights[1] <= 1.13) && (weights[2] <= 1.13);
				
				if ((pixel.getAlpha() == 0 || pixel.getDepth() > depth) && valid)
				{
					pixel.setRed(Math.min(1.0, Math.max(0.0, weights[0])));
					pixel.setGreen(Math.min(1.0, Math.max(0.0, weights[1])));
					pixel.setBlue(Math.min(1.0, Math.max(0.0, weights[2])));
					pixel.setAlpha(1.0);
					pixel.setDepth(depth);
					
					if (Math.abs(depth) > frame.getDepthScale())
						frame.setDepthScale(Math.abs(depth));
				}
			}
		}
	}
	
	public void calculateBarycentricWeights(Vector a, Vector b, Vector c, Vector p, double[] weights)
	{
		weights[0] = ((b.y - c.y) * (p.x - c.x) + (c.x - b.x) * (p.y - c.y))
				/ ((b.y - c.y) * (a.x - c.x) + (c.x - b.x) * (a.y - c.y));
		weights[1] = ((c.y - a.y) * (p.x - c.x) + (a.x - c.x) * (p.y - c.y))
				/ ((b.y - c.y) * (a.x - c.x) + (c.x - b.x) * (a.y - c.y));
		weights[2] = 1.0 - weights[0] - weights[1];
	}
}
