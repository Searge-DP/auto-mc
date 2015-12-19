package com.wildbamaboy.beam.automc;

import java.awt.event.KeyEvent;

public enum EnumKeys 
{
	UP(0, KeyEvent.VK_W),
	DOWN(1, KeyEvent.VK_S),
	LEFT(2, KeyEvent.VK_A),
	RIGHT(3, KeyEvent.VK_D),
	JUMP(4, KeyEvent.VK_SPACE),
	SNEAK(5, KeyEvent.VK_SHIFT),
	INVENTORY(6, KeyEvent.VK_E);
	
	private int id;
	private int keyCode;
	
	EnumKeys(int id, int keyCode)
	{
		this.id = id;
		this.keyCode = keyCode;
	}
	
	public int keyCode()
	{
		return keyCode;
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
