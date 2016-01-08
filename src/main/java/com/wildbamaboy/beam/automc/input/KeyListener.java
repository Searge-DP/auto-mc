package com.wildbamaboy.beam.automc.input;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.wildbamaboy.beam.automc.AutoMC;
import com.wildbamaboy.beam.automc.Flags;
import com.wildbamaboy.beam.automc.Font.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import pro.beam.interactive.event.EventListener;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.net.packet.Protocol.ProgressUpdate;

public class KeyListener implements EventListener<Protocol.Report> 
{
	private final float THRESHOLD = 0.5F;

	protected Robot keyboard;
	private KeyInterpreter interpreter;
	private boolean shiftFlag;

	public KeyListener() 
	{
		interpreter = AutoMC.keyInterpreter;

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
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(Color.GOLD + "[AutoMC] " + Color.YELLOW + "Chat is open, bot is disabled."));
				this.keyboard.keyRelease(KeyEvent.VK_SHIFT);
				shiftFlag = true;
			}

			return;
		}

		else //Use this to flip the shift flag and allow pressing shift in the chat window.
		{
			if (shiftFlag)
			{
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(Color.GOLD + "[AutoMC] " + Color.YELLOW + "Chat closed, bot enabled."));
				shiftFlag = false;	
			}
		}

		ProgressUpdate.Builder builder = ProgressUpdate.newBuilder();

		for (Protocol.Report.TactileInfo tactile : report.getTactileList()) 
		{
			// Ask our interpreter which Beam key was pressed.
			BeamKeyBridge key = interpreter.getKey(tactile.getCode());

			// Determine whether we are going to press down or release the key.
			double downResults = tactile.getDown().getMean() / report.getQuorum();
			double upResults = tactile.getUp().getMean() / report.getQuorum();
			boolean doDown = downResults > upResults;

			// Now determine whether or not the button will fire.
			// Always fire when the button is coming up. If it's going down, check the needed threshold.
			// The calculated progress is reported to Beam so viewers can see what is being pressed.
			boolean doFire = doDown ? downResults > THRESHOLD : true;
			float progress = report.getQuorum() > 0 ? (float) (downResults / report.getQuorum()) : 0.0F;

			if (doDown && doFire)
			{
				if (key != interpreter.keySneak)
				{
					if (key.getIsMovementKey())
					{
						key.pressKey();
						System.out.println(key.getMinecraftKeybinding().getIsKeyPressed());
					}

					else //Handle special cases.
					{
						if (key == interpreter.keyInventory)
						{
							try
							{
								if (Minecraft.getMinecraft().currentScreen instanceof GuiChat)
								{
									break;
								}

								else if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory || Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative)
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
						}

						else if (key == interpreter.keyLeftClick)
						{
							try
							{
								Method clickMouse = Minecraft.getMinecraft().getClass().getDeclaredMethod("clickMouse");
								clickMouse.setAccessible(true);
								clickMouse.invoke(Minecraft.getMinecraft());
							}

							catch (Exception e)
							{
								e.printStackTrace(System.err);
							}
						}

						else if (key == interpreter.keyRightClick)
						{
							try
							{
								Method clickMouse = Minecraft.getMinecraft().getClass().getDeclaredMethod("rightClickMouse");
								clickMouse.setAccessible(true);
								clickMouse.invoke(Minecraft.getMinecraft());
							}

							catch (Exception e)
							{
								e.printStackTrace(System.err);
							}
							
							//keyboard.mousePress(InputEvent.BUTTON2_DOWN_MASK);
						}

						else if (key == interpreter.keySelectionLeft || key == interpreter.keySelectionRight)
						{
							handleShifterKey(key);
						}

						else
						{
							System.err.println("No handler for key!: " + key);
						}
					}
				}

				else if (key == interpreter.keySneak && !Flags.IS_SNEAKING)
				{
					Flags.IS_SNEAKING = true;
					key.pressKey();
					//this.keyboard.keyPress(key.keyCode());
				}

				else if (key == interpreter.keySneak && Flags.IS_SNEAKING)
				{
					Flags.IS_SNEAKING = false;
				}
			}

			else if (!doDown && doFire)//doUp
			{
				if (key.getIsMovementKey())
				{
					if (key != interpreter.keySneak || (key == interpreter.keySneak && !Flags.IS_SNEAKING))
					{
						key.releaseKey();
						System.out.println("KEY UP");
						//						this.keyboard.keyRelease(key.keyCode());
					}
				}
				//
				//				else
				//				{
				//					switch (key)
				//					{
				//					case LEFT_CLICK:
				//						keyboard.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				//						break;
				//					case RIGHT_CLICK:
				//						keyboard.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
				//						break;
				//					default: 
				//						System.err.println("No handler for key!: " + key);
				//					}
				//				}
			}

			builder.addProgress(builder.getProgressCount(),
					ProgressUpdate.Progress
					.newBuilder()
					.setCode(tactile.getCode())
					.setFired(doFire)
					.setTarget(ProgressUpdate.Progress.TargetType.TACTILE)
					.setProgress(progress));
		}
	}

	private void handleShifterKey(BeamKeyBridge key) 
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
			int indexChange = key == interpreter.keySelectionLeft ? -1 : key == interpreter.keySelectionRight ? 1 : 0;
			player.inventory.changeCurrentItem(player.inventory.currentItem + indexChange);
		}
	}
}