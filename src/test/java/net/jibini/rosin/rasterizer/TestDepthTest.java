package net.jibini.rosin.rasterizer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import net.jibini.rosin.frame.ArrayFrameData;
import net.jibini.rosin.frame.FrameData;
import net.jibini.rosin.polygon.Polygon;
import net.jibini.rosin.vector.Vector;

public class TestDepthTest
{
	@BeforeClass
	public static void cleanOutput()
	{
		TestRenderSave.cleanSubfolder("depth_test");
	}
	
	private void testDepthTest(String name, Polygon... poly) throws IOException
	{
		int width = 1280, height = 960;
		FrameData frame = new ArrayFrameData(width, height);
		TestRenderSave.render(frame, poly);
		BufferedImage image = TestRenderSave.convertDepthImage(frame);
		TestRenderSave.saveImage("depth_test", name, image);
		image = TestRenderSave.convertImage(frame);
		TestRenderSave.saveImage("depth_test", name + "_color", image);
	}
	
	@Test
	public void testTwoTriangles() throws IOException
	{
		Vector a0 = new Vector(-1.0, 1.0, 1.0);
		Vector b0 = new Vector(0.0, -1.0, 1.0);
		Vector c0 = new Vector(1.0, 1.0, 1.0);
		Polygon p0 = new Polygon(a0, b0, c0);
		
		Vector a1 = new Vector(-1.0, -1.0, 2.0);
		Vector b1 = new Vector(0.0, 1.0, 2.0);
		Vector c1 = new Vector(1.0, -1.0, 2.0);
		Polygon p1 = new Polygon(a1, b1, c1);
		
		testDepthTest("two_triangles", p0, p1);
	}
	
	@Test
	public void testIntersectingTriangles() throws IOException
	{
		Vector a0 = new Vector(-1.0, 1.0, 1.0);
		Vector b0 = new Vector(0.0, -1.0, 3.0);
		Vector c0 = new Vector(1.0, 1.0, 3.0);
		Polygon p0 = new Polygon(a0, b0, c0);
		
		Vector a1 = new Vector(-1.0, -1.0, 3.0);
		Vector b1 = new Vector(0.0, 1.0, 3.0);
		Vector c1 = new Vector(1.0, -1.0, 1.0);
		Polygon p1 = new Polygon(a1, b1, c1);
		
		testDepthTest("intersecting_triangles", p0, p1);
	}
	
	@Test
	public void testRandomDepths() throws IOException
	{
		for (int r = 0; r < 4; r++)
		{
			Polygon[] poly = new Polygon[5];
			Random rand = new Random();
			
			for (int i = 0; i < poly.length; i ++)
			{
				Vector a = new Vector(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0,
						rand.nextDouble() * 5.0);
				Vector b = new Vector(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0,
						rand.nextDouble() * 5.0);
				Vector c = new Vector(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0,
						rand.nextDouble() * 5.0);
				poly[i] = new Polygon(a, b, c);
			}
			
			testDepthTest("random_depths" + r, poly);
		}
	}
}
