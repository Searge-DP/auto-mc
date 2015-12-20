package com.wildbamaboy.beam.automc.input;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import pro.beam.interactive.event.EventListener;
import pro.beam.interactive.net.packet.Protocol;

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
		if (Minecraft.getMinecraft().currentScreen instanceof GuiChat)
		{
			//Do not handle reports when chat is open.
			return;
		}
		
		try
		{
			if (report.getJoystickCount() > 0 && report.getQuorum() > 0)
			{
				Protocol.Report.JoystickInfo joystickX = report.getJoystick(0);
				Protocol.Report.JoystickInfo joystickY = report.getJoystick(1);

				Point mouseLocation = MouseInfo.getPointerInfo().getLocation(); 

				//Determine how much we want to move by multiplying the mean times a constant.
				int moveAmountX = (int) (joystickX.getInfo().getMean() * 15);
				int moveAmountY = (int) (joystickY.getInfo().getMean() * 15);

				//Calculate our end location for smooth movement.
				int endX = (int) (mouseLocation.getX() + moveAmountX);
				int endY = (int) (mouseLocation.getY() + moveAmountY);
				
				//And calculate the distance we will travel.
				double deltaX = Math.abs(moveAmountX);
				double deltaY = Math.abs(moveAmountY);

				//Only move when we need to travel a certain amount.
				if (deltaX > 3.0D || deltaY > 3.0D)
				{
					//Divide the movement into equal parts and execute them throughout the entire report period.
					for (int i = 0; i < 10; i++)
					{
						int pointX = deltaX > 3.0D ? (int) (((endX * i)/10) + (mouseLocation.getX() * (10 - i) / 10)) : (int)mouseLocation.getX();
						int pointY = deltaY > 3.0D ? (int) (((endY * i)/10) + (mouseLocation.getY() * (10 - i) / 10)) : (int)mouseLocation.getY();
						mouse.mouseMove(pointX,pointY);
						mouse.delay(10);
					}
				}
			}
		}

		catch (Exception e)
		{

		}
	}
}