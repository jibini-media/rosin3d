package net.jibini.rosin.model;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import net.jibini.rosin.frame.ArrayFrameData;
import net.jibini.rosin.frame.FrameData;
import net.jibini.rosin.polygon.Polygon;
import net.jibini.rosin.rasterizer.TestRenderSave;

public class TestModelRender
{
	@BeforeClass
	public static void cleanOutput()
	{
		TestRenderSave.cleanSubfolder("model");
	}
	
	private void testModelRender(String name, double depthScale, Polygon[] model) throws IOException
	{
		int width = 1920, height = 1600;
		FrameData frame = new ArrayFrameData(width, height);
		TestRenderSave.render(frame, model);
		if (depthScale > 0.0)
			frame.setDepthScale(depthScale);
		BufferedImage image = TestRenderSave.convertDepthImage(frame);
		TestRenderSave.saveImage("model", name, image);
		image = TestRenderSave.convertImage(frame);
		TestRenderSave.saveImage("model", name + "_color", image);
	}
	
	@Test
	public void testBunnyModel() throws IOException
	{
		Polygon[] bunny = TestModel.loadOBJ("bunny");
		
		for (Polygon poly : bunny)
		{
			poly.a.scale(10);
			poly.b.scale(10);
			poly.c.scale(10);
			
			poly.a.z += 0.5;
			poly.b.z += 0.5;
			poly.c.z += 0.5;
			poly.a.y -= 1.1;
			poly.b.y -= 1.1;
			poly.c.y -= 1.1;
			poly.a.x += 0.2;
			poly.b.x += 0.2;
			poly.c.x += 0.2;
		}

		testModelRender("bunny", 1.6, bunny);
	}
	
	@Test
	public void testDragonModel() throws IOException
	{
		Polygon[] bunny = TestModel.loadOBJ("dragon");
		
		for (Polygon poly : bunny)
		{
			poly.a.scale(0.13);
			poly.b.scale(0.13);
			poly.c.scale(0.13);
			
			poly.a.z += 0.5;
			poly.b.z += 0.5;
			poly.c.z += 0.5;
			poly.a.y -= 0.7;
			poly.b.y -= 0.7;
			poly.c.y -= 0.7;
		}

		testModelRender("dragon", 1.6, bunny);
	}
}
