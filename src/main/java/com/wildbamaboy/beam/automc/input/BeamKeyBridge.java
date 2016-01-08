package com.wildbamaboy.beam.automc.input;

import net.minecraft.client.settings.KeyBinding;

public class BeamKeyBridge 
{
	/** The ID of the tactile key as defined with Beam. Usually a usable key code. */
	private int beamTactileId;
	
	/** A reference to the Minecraft keybinding that this key will 'press'. */
	private KeyBinding minecraftKey;
	
	/** Whether or not this key should be treated as a simple 'press and release' key, such as movement. 
	 *  False requires a special handler in the KeyListener. */
	private boolean isMovementKey;
	
	public BeamKeyBridge(int beamTactileId, KeyBinding minecraftKey, boolean isMovementKey)
	{
		this.beamTactileId = beamTactileId;
		this.minecraftKey = minecraftKey;
		this.isMovementKey = isMovementKey;
	}
	
	public int getBeamTactileId()
	{
		return beamTactileId;
	}
	
	public KeyBinding getMinecraftKeybinding()
	{
		return minecraftKey;
	}
	
	public boolean getIsMovementKey()
	{
		return isMovementKey;
	}
	
	public void pressKey()
	{
		if (minecraftKey != null)
		{
			KeyBinding.setKeyBindState(minecraftKey.getKeyCode(), true);
		}
	}
	
	public void releaseKey()
	{
		if (minecraftKey != null)
		{
			KeyBinding.setKeyBindState(minecraftKey.getKeyCode(), false);
		}
	}
}
