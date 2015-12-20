package com.wildbamaboy.beam.automc.input;

import java.awt.event.KeyEvent;

public enum EnumKeys 
{
	UP(0, KeyEvent.VK_W, true),
	DOWN(1, KeyEvent.VK_S, true),
	LEFT(2, KeyEvent.VK_A, true),
	RIGHT(3, KeyEvent.VK_D, true),
	JUMP(4, KeyEvent.VK_SPACE, true),
	SNEAK(5, KeyEvent.VK_SHIFT, false),
	INVENTORY(6, -1, false),
	SHIFT_RIGHT(7, -1, false),
	SHIFT_LEFT(8, -1, false),
	SHIFT_UP(9, -1, false),
	SHIFT_DOWN(10, -1, false),
	LEFT_CLICK(11, -1, false),
	RIGHT_CLICK(12, -1, false);
	
	private int id;
	private int keyCode;
	private boolean isMovementKey;
	
	EnumKeys(int id, int keyCode, boolean isMovementKey)
	{
		this.id = id;
		this.keyCode = keyCode;
		this.isMovementKey = isMovementKey;
	}
	
	public int keyCode()
	{
		return keyCode;
	}
	
	public boolean getIsMovementKey()
	{
		return isMovementKey;
	}
	
	public static EnumKeys getKey(int id)
	{
		for (EnumKeys key : values())
		{
			if (key.id == id)
			{
				return key;
			}
		}
		
		return null;
	}
	
	public static int getKeyCode(int id)
	{
		EnumKeys key = getKey(id);
		
		return key != null ? key.id : -1;
	}
}
