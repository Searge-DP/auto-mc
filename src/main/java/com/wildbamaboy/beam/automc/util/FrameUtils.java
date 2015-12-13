package com.wildbamaboy.beam.automc.util;

import java.awt.Component;
import java.lang.reflect.Field;

import javax.swing.JFrame;

public class FrameUtils 
{
	private FrameUtils(){}

	public static void addAllComponents(JFrame frame)
	{
		//Add all components to this frame.
		for (Field f : frame.getClass().getDeclaredFields())
		{
			f.setAccessible(true);
			
			if (Component.class.isAssignableFrom(f.getType()))
			{
				try
				{
					frame.add((Component)f.get(frame));
				}

				catch (Exception e)
				{
					e.printStackTrace(System.err);
					continue;
				}
			}
			
			f.setAccessible(false);
		}
	}
}
