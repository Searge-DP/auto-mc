package com.wildbamaboy.beam.automc.input;

import pro.beam.interactive.event.EventListener;
import pro.beam.interactive.net.packet.Protocol;

import java.awt.*;

public class MouseListener implements EventListener<Protocol.Report> 
{
	protected Robot mouse;

	public MouseListener() 
	{
		try 
		{
			this.mouse = new Robot();
		} 

		catch (AWTException ignored) 
		{ 

		}
	}

	@Override 
	public void handle(Protocol.Report report) 
	{
		try
		{
		Protocol.Report.JoystickInfo joystickX = report.getJoystick(0);
//		Protocol.Report.JoystickInfo joystickY = report.getJoystick(1);

		mouse.mouseMove(
				(int) Math.round(joystickX.getInfo().getStdev()),
				(int) Math.round(joystickX.getInfo().getStdev())
				);
		}
		
		catch (Exception e)
		{
			
		}
	}
}