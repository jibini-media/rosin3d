package net.jibini.rosin.rasterizer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.jibini.rosin.frame.FrameData;
import net.jibini.rosin.pixel.Pixel;
import net.jibini.rosin.polygon.Polygon;

public class TestRenderSave
{
	public static void cleanSubfolder(String name)
	{
		File output = new File("output/" + name + "/");
		output.getParentFile().mkdirs();
		output.mkdir();
		for (File file : output.listFiles())
			file.delete();
	}
	
	public static BufferedImage convertImage(FrameData frame)
	{
		BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < frame.getWidth(); x++)
		{
			for (int y = 0; y < frame.getHeight(); y++)
			{
				Pixel p = frame.getPixel(x, y);
				Color color = new Color((int) (p.getRed() * 255), (int) (p.getGreen() * 255),
						(int) (p.getBlue() * 255));
				image.setRGB(x, y, color.getRGB());
			}
		}
		
		return image;
	}
	
	public static BufferedImage convertDepthImage(FrameData frame)
	{
		BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < frame.getWidth(); x++)
		{
			for (int y = 0; y < frame.getHeight(); y++)
			{
				Pixel p = frame.getPixel(x, y);
				int depth = Math.max(0, Math.min(255, 255 - (int)(p.getDepth() / frame.getDepthScale() * 255)));
				depth *= p.getAlpha();
				Color color = new Color(depth, depth, depth);
				if (p.getAlpha() == 0)
					color = new Color(255, 0, 255);
				image.setRGB(x, y, color.getRGB());
			}
		}
		
		return image;
	}
	
	public static void saveImage(String subfolder, String name, BufferedImage image) throws IOException
	{
		File output = new File("output/" + subfolder + "/" + name + ".png");
		ImageIO.write(image, "png", output);
	}
	
	public static void render(FrameData frame, Polygon... poly)
	{
		Rasterizer rasterizer = new Rasterizer();
		for (Polygon p : poly)
			rasterizer.rasterize(p, frame);
	}
}
