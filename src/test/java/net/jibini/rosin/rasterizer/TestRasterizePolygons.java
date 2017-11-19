package net.jibini.rosin.rasterizer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.junit.BeforeClass;
import org.junit.Test;

import net.jibini.rosin.frame.ArrayFrameData;
import net.jibini.rosin.frame.FrameData;
import net.jibini.rosin.pixel.Pixel;
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
		System.out.println("Cleaning render output directory");
		File output = new File("output/");
		output.mkdir();
		for (File file : output.listFiles())
			file.delete();
		
		System.out.println("Rendering test polygons");
	}
	
	private void testRasterize(Vector a, Vector b, Vector c, String name) throws IOException
	{
		int width = 640, height = 480;
		System.out.println(name);
		
		Polygon poly = new Polygon(a, b, c);
		Rasterizer rasterizer = new Rasterizer();
		FrameData frame = new ArrayFrameData(width, height);
		
		System.out.print("Rendering...");
		rasterizer.rasterize(poly, frame);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		System.out.print(" Converting...");
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				Pixel p = frame.getPixel(x, y);
				Color color = new Color((int) (p.getRed() * 255), (int) (p.getGreen() * 255),
						(int) (p.getBlue() * 255));
				image.setRGB(x, y, color.getRGB());
			}
		}
		
		System.out.println(" Saving...");
		File output = new File("output/" + name + ".png");
		ImageIO.write(image, "png", output);
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
			Vector a = new Vector(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0);
			Vector b = new Vector(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0);
			Vector c = new Vector(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0);
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
