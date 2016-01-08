package com.wildbamaboy.beam.automc;

import org.apache.logging.log4j.Logger;

import com.wildbamaboy.beam.automc.command.CommandAutoMC;
import com.wildbamaboy.beam.automc.input.KeyInterpreter;

import net.minecraft.command.CommandHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.interactive.robot.Robot;

@Mod(modid = "AutoMC", name = "AutoMC", version = "0.9.2-BETA")
public class AutoMC
{
	@Instance
	public static AutoMC instance;
	public static KeyInterpreter keyInterpreter;
	public static Logger logger;
	
	private static BeamAPI beam;
	private static BeamUser user;
	private static Robot robot;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		keyInterpreter = new KeyInterpreter();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		try
		{
			beam = new BeamAPI();
		}

		catch (Exception e)
		{
			e.printStackTrace(System.err);
			throw new RuntimeException("Couldn't connect to Beam API. AutoMC cannot be started.");
		}

		CommandHandler handler = ClientCommandHandler.instance;
		handler.registerCommand(new CommandAutoMC());
		
		FMLCommonHandler.instance().bus().register(new EventHooksFML());
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
	
	public static Logger getLog()
	{
		return logger;
	}
}
