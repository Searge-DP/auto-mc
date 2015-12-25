package com.wildbamaboy.beam.automc;

import java.net.URI;

import com.wildbamaboy.beam.automc.command.CommandAutoMC;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.client.ClientCommandHandler;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.interactive.robot.Robot;

@Mod(modid = "AutoMC", name = "AutoMC", version = "0.9.0-BETA")
public class AutoMC
{
	@Instance
	public static AutoMC instance;
	
	private static BeamAPI beam;
	private static BeamUser user;
	private static Robot robot;
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		try
		{
			beam = new BeamAPI(new URI("https://lab.beam.pro/api/v1/"), "user", "password");
		}

		catch (Exception e)
		{
			e.printStackTrace(System.err);
		}

		CommandHandler handler = ClientCommandHandler.instance;
		handler.registerCommand(new CommandAutoMC());
	}

	public BeamAPI getBeamAPI()
	{
		return beam;
	}
	
	public BeamUser getBeamUser()
	{
		return user;
	}
	
	public void setBeamUser(BeamUser user)
	{
		this.user = user;
	}
	
	public void setRobot(Robot robot)
	{
		this.robot = robot;
	}
	
	public Robot getRobot()
	{
		return robot;
	}
}
