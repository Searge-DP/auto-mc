package com.wildbamaboy.beam.automc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class EventHooksFML 
{
	private final int INTERVAL_THRESHOLD = 5;
	private int counter = 0;

	@SubscribeEvent
	public void clientTickEventHandler(ClientTickEvent event)
	{
		if (counter >= INTERVAL_THRESHOLD)
		{
			if (AutoMC.instance.getRobot() != null)
			{	
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				counter = 0;

				//Disable pausing on losing focus.
				if (Minecraft.getMinecraft().gameSettings.pauseOnLostFocus)
				{
					Minecraft.getMinecraft().gameSettings.pauseOnLostFocus = false;
					AutoMC.getLog().info("Disabled game pause on losing focus.");
				}

				//Make sure we're on the ground before jumping in order to prevent double jumps.
				if (player != null && player.isCollidedHorizontally && player.onGround)
				{
					player.jump();
				}
			}
			
			else //Not connected so reset any of our changed game settings.
			{
				if (!Minecraft.getMinecraft().gameSettings.pauseOnLostFocus)
				{
					Minecraft.getMinecraft().gameSettings.pauseOnLostFocus = true;
					AutoMC.getLog().info("Enabled game pause on losing focus.");
				}
			}
		}
		
		else
		{
			counter++;
		}
	}
}
