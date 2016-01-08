package com.wildbamaboy.beam.automc.input;

import java.lang.reflect.Field;

import com.sun.glass.events.KeyEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class KeyInterpreter 
{
	private static KeyInterpreter instance;
	private static GameSettings mcSettings;

	public final BeamKeyBridge keyForward;
	public final BeamKeyBridge keyBack;
	public final BeamKeyBridge keyStrafeLeft;
	public final BeamKeyBridge keyStrafeRight;
	public final BeamKeyBridge keyJump;
	public final BeamKeyBridge keySneak;
	public final BeamKeyBridge keyInventory;
	public final BeamKeyBridge keySelectionLeft;
	public final BeamKeyBridge keySelectionRight;
	public final BeamKeyBridge keyLeftClick;
	public final BeamKeyBridge keyRightClick;
	public final BeamKeyBridge keyDropItem;

	public KeyInterpreter()
	{
		instance = this;
		mcSettings = Minecraft.getMinecraft().gameSettings;

		keyForward = new BeamKeyBridge(KeyEvent.VK_W, mcSettings.keyBindForward, true);
		keyBack = new BeamKeyBridge(KeyEvent.VK_S, mcSettings.keyBindBack, true);
		keyStrafeLeft = new BeamKeyBridge(KeyEvent.VK_A, mcSettings.keyBindLeft, true);
		keyStrafeRight = new BeamKeyBridge(KeyEvent.VK_D, mcSettings.keyBindRight, true);
		keyJump = new BeamKeyBridge(KeyEvent.VK_SPACE, mcSettings.keyBindJump, true);
		keySneak = new BeamKeyBridge(KeyEvent.VK_SHIFT, mcSettings.keyBindSneak, true);
		keyInventory = new BeamKeyBridge(KeyEvent.VK_I, mcSettings.keyBindInventory, false);
		keySelectionLeft = new BeamKeyBridge(KeyEvent.VK_LEFT, null, false);
		keySelectionRight = new BeamKeyBridge(KeyEvent.VK_RIGHT, null, false);
		keyLeftClick = new BeamKeyBridge(11, mcSettings.keyBindAttack, false);
		keyRightClick = new BeamKeyBridge(12, mcSettings.keyBindUseItem, false);
		keyDropItem = new BeamKeyBridge(KeyEvent.VK_Q, mcSettings.keyBindDrop, true);
	}

	public BeamKeyBridge getKey(int beamId)
	{
		try
		{
			for (Field f : KeyInterpreter.class.getDeclaredFields())
			{
				if (f.getType().getSimpleName().equals(BeamKeyBridge.class.getSimpleName()))
				{
					f.setAccessible(true);
					BeamKeyBridge theKey = (BeamKeyBridge) f.get(this);
					f.setAccessible(false);

					if (theKey.getBeamTactileId() == beamId)
					{
						return theKey;
					}
				}
			}
		}

		catch (Exception e)
		{
			e.printStackTrace(System.err);
		}

		return null;
	}
}
