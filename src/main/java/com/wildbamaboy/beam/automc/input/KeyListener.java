package com.wildbamaboy.beam.automc.input;

import java.awt.AWTException;
import java.awt.Robot;

import com.wildbamaboy.beam.automc.EnumKeys;
import com.wildbamaboy.beam.automc.Flags;

import pro.beam.interactive.event.EventListener;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.net.packet.Protocol.Report.InfoOrBuilder;

public class KeyListener implements EventListener<Protocol.Report> 
{
	protected Robot keyboard;
	
	public KeyListener() 
	{
		try 
		{
			this.keyboard = new Robot();
		}

		catch (AWTException ignored) 
		{ 

		}
	}

	@Override 
	public void handle(Protocol.Report report) 
	{
		for (Protocol.Report.TactileInfo tactile : report.getTactileList()) 
		{
			EnumKeys key = EnumKeys.getKey(tactile.getCode());
			
			double downResults = tactile.getDown().getMean() / report.getQuorum();
			double upResults = tactile.getUp().getMean() / report.getQuorum();
			boolean doDown = downResults > upResults;
			
			if (doDown)
			{
				System.out.println("DO " + key);
				
				if (key != EnumKeys.SNEAK)
				{
					this.keyboard.keyPress(key.keyCode());
				}
				
				else if (key == EnumKeys.SNEAK && !Flags.IS_SNEAKING)
				{
					Flags.IS_SNEAKING = true;
					this.keyboard.keyPress(key.keyCode());
				}
				
				else if (key == EnumKeys.SNEAK && Flags.IS_SNEAKING)
				{
					Flags.IS_SNEAKING = false;
					System.out.println("REVERSE");
				}
			}
			
			else //doUp
			{
				if (key != EnumKeys.SNEAK || (key == EnumKeys.SNEAK && !Flags.IS_SNEAKING))
				{
					System.out.println("NO SNEAK");
					this.keyboard.keyRelease(key.keyCode());
				}
			}
		}
	}
}