package com.wildbamaboy.beam.automc.input;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import com.wildbamaboy.beam.automc.Flags;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import pro.beam.interactive.event.EventListener;
import pro.beam.interactive.net.packet.Protocol;

public class KeyListener implements EventListener<Protocol.Report> 
{
	protected Robot keyboard;
	private boolean shiftFlag;

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
		if (Minecraft.getMinecraft().currentScreen instanceof GuiChat)
		{
			//Do not handle reports when chat is open, and flip our shift button off.
			if (!shiftFlag)
			{
				this.keyboard.keyRelease(KeyEvent.VK_SHIFT);
				shiftFlag = true;
			}
			
			return;
		}

		else //Use this to flip the shift flag and allow pressing shift in the chat window.
		{
			shiftFlag = false;
		}

		for (Protocol.Report.TactileInfo tactile : report.getTactileList()) 
		{
			EnumKeys key = EnumKeys.getKey(tactile.getCode());

			double downResults = tactile.getDown().getMean() / report.getQuorum();
			double upResults = tactile.getUp().getMean() / report.getQuorum();
			boolean doDown = downResults > upResults;

			if (doDown)
			{	
				if (key != EnumKeys.SNEAK)
				{
					if (key.getIsMovementKey())
					{
						this.keyboard.keyPress(key.keyCode());
					}

					else
					{
						switch (key)
						{
						case INVENTORY: 
							try
							{
								if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory)
								{
									Minecraft.getMinecraft().displayGuiScreen(null);
								}

								else
								{
									Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(Minecraft.getMinecraft().thePlayer));									
								}
							}

							catch (NullPointerException e)
							{
								//Pass, not sure what ends up being null here but it happens when the button is spammed.
							}

							break;
						case LEFT_CLICK:
							keyboard.mousePress(InputEvent.BUTTON1_DOWN_MASK);
							break;
						case RIGHT_CLICK:
							keyboard.mousePress(InputEvent.BUTTON2_DOWN_MASK);
							break;

						case SHIFT_LEFT:
						case SHIFT_RIGHT:
						case SHIFT_UP:
						case SHIFT_DOWN:
							handleShifterKey(key);
							break;

						default: 
							System.err.println("No handler for key!: " + key);
						}
					}
				}

				else if (key == EnumKeys.SNEAK && !Flags.IS_SNEAKING)
				{
					Flags.IS_SNEAKING = true;
					this.keyboard.keyPress(key.keyCode());
				}

				else if (key == EnumKeys.SNEAK && Flags.IS_SNEAKING)
				{
					Flags.IS_SNEAKING = false;
				}
			}

			else //doUp
			{
				if (key.getIsMovementKey())
				{
					if (key != EnumKeys.SNEAK || (key == EnumKeys.SNEAK && !Flags.IS_SNEAKING))
					{
						this.keyboard.keyRelease(key.keyCode());
					}
				}

				else
				{
					switch (key)
					{
					case LEFT_CLICK:
						keyboard.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
						break;
					case RIGHT_CLICK:
						keyboard.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
						break;
					default: 
						System.err.println("No handler for key!: " + key);
					}
				}
			}
		}
	}

	private void handleShifterKey(EnumKeys key) 
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;

		if (currentScreen instanceof GuiContainerCreative)
		{
			//TODO In a future version.
			//			GuiContainerCreative creativeGui = (GuiContainerCreative)currentScreen;
			//
			//			if (key == EnumKeys.SHIFT_RIGHT || key == EnumKeys.SHIFT_LEFT)
			//			{
			//				int currentIndex = creativeGui.func_147056_g();
			//				int nextIndex = currentIndex + (key == EnumKeys.SHIFT_RIGHT ? 1 : key == EnumKeys.SHIFT_LEFT ? -1 : 0);
			//				CreativeTabs nextTab = CreativeTabs.tabBlock;
			//
			//				for (CreativeTabs tab : CreativeTabs.creativeTabArray)
			//				{
			//					if (tab.getTabIndex() == nextIndex)
			//					{
			//						nextTab = tab;
			//						break;
			//					}
			//				}
			//
			//				try
			//				{
			//					Method setCurrentCreativeTab = creativeGui.getClass().getDeclaredMethod("setCurrentCreativeTab", CreativeTabs.class);
			//					setCurrentCreativeTab.setAccessible(true);
			//					setCurrentCreativeTab.invoke(creativeGui, nextTab);
			//				}
			//
			//				catch (Exception e)
			//				{
			//					e.printStackTrace(System.err);
			//				}
			//			}
		}

		else if (currentScreen instanceof GuiInventory)
		{

		}

		else if (currentScreen == null) //'Ingame' screen
		{
			int indexChange = key == EnumKeys.SHIFT_LEFT ? -1 : key == EnumKeys.SHIFT_RIGHT ? 1 : 0;
			player.inventory.changeCurrentItem(player.inventory.currentItem + indexChange);
		}
	}
}