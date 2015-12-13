package com.wildbamaboy.beam.automc.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JWindow;

import com.wildbamaboy.beam.automc.AutoMC;

@SuppressWarnings("serial")
public class GuiSplash extends JWindow 
{
	private static final int WIDTH = 400;
	private static final int HEIGHT = 120;

	private BufferedImage image;

	public GuiSplash() 
	{
		this.setSize(WIDTH, HEIGHT); //Size of the splash image.
		this.setLocationRelativeTo(null);

		//Load the splash image.
		try
		{
			InputStream stream = GuiSplash.class.getClassLoader().getResourceAsStream("splash.png");
			image = ImageIO.read(stream);
		}

		catch (Exception e)
		{
			e.printStackTrace(System.err);
		}
	}
	
	@Override
	public void paint(Graphics gfx)
	{
		gfx.drawImage(image, 0, 0, null);
	}

	public void close() 
	{
		this.setVisible(false);
		this.dispose();
	}
}