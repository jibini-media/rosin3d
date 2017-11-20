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

/**
 * Various scenarios of types of triangles
 * 
 * @author Zach Goethel
 */
public class TestRasterizePolygons
{
	@BeforeClass
	public static void cleanOutput()
	{
		TestRenderSave.cleanSubfolder("rasterized");
	}
	
	private void testRasterize(Vector a, Vector b, Vector c, String name) throws IOException
	{
		int width = 1280, height = 960;
		Polygon poly = new Polygon(a, b, c);
		FrameData frame = new ArrayFrameData(width, height);
		TestRenderSave.render(frame, poly);
		BufferedImage image = TestRenderSave.convertImage(frame);
		TestRenderSave.saveImage("rasterized", name, image);
	}
	
	@Test
	public void testFlatTop() throws IOException
	{
		Vector a = new Vector(-1.0, 1.0);
		Vector b = new Vector(0.0, -1.0);
		Vector c = new Vector(1.0, 1.0);
		testRasterize(a, b, c, "flat_top");
	}
	
	@Test
	public void testFlatTopSkewed() throws IOException
	{
		Vector a = new Vector(1.0, -1.0);
		Vector b = new Vector(-1.0, 1.0);
		Vector c = new Vector(0.0, 1.0);
		testRasterize(a, b, c, "flat_top_skewed");
	}
	
	@Test
	public void testFlatBottom() throws IOException
	{
		Vector a = new Vector(-1.0, -1.0);
		Vector b = new Vector(0.0, 1.0);
		Vector c = new Vector(1.0, -1.0);
		testRasterize(a, b, c, "flat_bottom");
	}
	
	@Test
	public void testFlatBottomSkewed() throws IOException
	{
		Vector a = new Vector(0.0, -1.0);
		Vector b = new Vector(-1.0, -1.0);
		Vector c = new Vector(1.0, 1.0);
		testRasterize(a, b, c, "flat_bottom_skewed");
	}
	
	@Test
	public void testOddPolygon() throws IOException
	{
		Vector a = new Vector(-0.5, -0.5);
		Vector b = new Vector(0.5, 0.0);
		Vector c = new Vector(0.0, 0.5);
		testRasterize(a, b, c, "odd_polygon");
	}
	
	@Test
	public void testRandPolygon() throws IOException
	{
		Random rand = new Random();
		for (int i = 0; i < 4; i++)
		{
			Vector a = new Vector(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0,
					rand.nextDouble() * 2.0);
			Vector b = new Vector(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0,
					rand.nextDouble() * 2.0);
			Vector c = new Vector(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0,
					rand.nextDouble() * 2.0);
			testRasterize(a, b, c, "rand_polygon" + i);
		}
	}
	
	@Test
	public void testHorizontalCollinear() throws IOException
	{
		Vector a = new Vector(-1.0, 0.0);
		Vector b = new Vector(0.0, 0.0);
		Vector c = new Vector(1.0, 0.0);
		testRasterize(a, b, c, "horizontal_collinear");
	}
	
	@Test
	public void testVerticalCollinear() throws IOException
	{
		Vector a = new Vector(0.0, -1.0);
		Vector b = new Vector(0.0, 0.0);
		Vector c = new Vector(0.0, 1.0);
		testRasterize(a, b, c, "vertical_collinear");
	}
	
	@Test
	public void testDiagCollinear() throws IOException
	{
		Vector a = new Vector(-1.0, -1.0);
		Vector b = new Vector(0.0, 0.0);
		Vector c = new Vector(1.0, 1.0);
		testRasterize(a, b, c, "diag_collinear");
	}
}
