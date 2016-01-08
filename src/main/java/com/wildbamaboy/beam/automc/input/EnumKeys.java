//package com.wildbamaboy.beam.automc.input;
//
//import java.awt.event.KeyEvent;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.settings.KeyBinding;
//
//public enum EnumKeys 
//{
//	// This setup is admittedly a bit strange. Was an early
//	// setup that I see doesn't make much sense. But, it works 
//	// for now.
//	UP(KeyEvent.VK_W, Minecraft.getMinecraft().gameSettings.keyBindForward, true),
//	DOWN(KeyEvent.VK_S, KeyEvent.VK_S, true),
//	LEFT(KeyEvent.VK_A, KeyEvent.VK_A, true),
//	RIGHT(KeyEvent.VK_D, KeyEvent.VK_D, true),
//	JUMP(KeyEvent.VK_SPACE, KeyEvent.VK_SPACE, true),
//	SNEAK(KeyEvent.VK_SHIFT, KeyEvent.VK_SHIFT, true),
//	INVENTORY(KeyEvent.VK_I, -1, false),
//	SHIFT_RIGHT(KeyEvent.VK_RIGHT, -1, false),
//	SHIFT_LEFT(KeyEvent.VK_LEFT, -1, false),
//	SHIFT_UP(9, -1, false),
//	SHIFT_DOWN(10, -1, false),
//	LEFT_CLICK(11, -1, false),
//	RIGHT_CLICK(12, -1, false),
//	DROP_ITEM(KeyEvent.VK_Q, KeyEvent.VK_Q, true);
//	
//	private int beamId;
//	private KeyBinding minecraftKey;
//	private boolean isMovementKey;
//	
//	EnumKeys(int id, int keyCode, boolean isMovementKey)
//	{
//		this.id = id;
//		this.keyCode = keyCode;
//		this.isMovementKey = isMovementKey;
//	}
//	
//	public int keyCode()
//	{
//		return keyCode;
//	}
//	
//	public boolean getIsMovementKey()
//	{
//		return isMovementKey;
//	}
//	
//	public static EnumKeys getKey(int id)
//	{
//		for (EnumKeys key : values())
//		{
//			if (key.id == id)
//			{
//				return key;
//			}
//		}
//		
//		return null;
//	}
//	
//	public static int getKeyCode(int id)
//	{
//		EnumKeys key = getKey(id);
//		
//		return key != null ? key.id : -1;
//	}
//}
