package com.wildbamaboy.beam.automc;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class EventHooksFML 
{
	private final int INTERVAL_THRESHOLD = 5;
	private int counter = 0;

	@SubscribeEvent
	public void clientTickEventHandler(ClientTickEvent event)
	{
		if (AutoMC.instance.getRobot() != null)
		{
			if (counter >= INTERVAL_THRESHOLD)
			{
				counter = 0;

				EntityPlayer player = Minecraft.getMinecraft().thePlayer;

				//Make sure we're on the ground before jumping in order to prevent double jumps.
				if (player != null && player.isCollidedHorizontally && player.onGround)
				{
					player.jump();
				}
			}

			else
			{
				counter++;
			}
		}
	}
}
