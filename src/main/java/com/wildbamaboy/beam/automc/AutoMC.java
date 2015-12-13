package com.wildbamaboy.beam.automc;

import java.net.URI;

import javax.swing.SwingUtilities;

import com.google.common.util.concurrent.ListenableFuture;
import com.wildbamaboy.beam.automc.gui.GuiLogin;
import com.wildbamaboy.beam.automc.gui.GuiSplash;
import com.wildbamaboy.beam.automc.util.CallResult;

import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.services.impl.UsersService;
import pro.beam.interactive.robot.Robot;
import pro.beam.interactive.robot.RobotBuilder;

public class AutoMC 
{
	private static BeamAPI beam;
	private static BeamUser user;
	private static Robot robot;

	public static void main(String[] args)
	{
		try
		{
			beam = new BeamAPI(new URI("https://lab.beam.pro/api/v1/"), args[0], args[1]);
			loginToTetris(args[2], args[3]);
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static CallResult loginToTetris(String username, String password)
	{
		try
		{
			BeamUser user = beam.use(UsersService.class).login(username, password).checkedGet();

			ListenableFuture<Robot> future = new RobotBuilder()
					.username(username)
					.password(password)
					.channel(user.channel.id)
					.build(beam);


			robot = future.get();
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}

		return CallResult.True();
	}

	private static GuiSplash showSplash()
	{
		final GuiSplash splashGui = new GuiSplash();

		SwingUtilities.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				splashGui.setVisible(true);
			}
		});

		return splashGui;
	}

	private static GuiLogin showLogin()
	{
		final GuiLogin loginGui = new GuiLogin();

		SwingUtilities.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				loginGui.setVisible(true);
			}
		});

		return loginGui;
	}

	public static void wait(int seconds)
	{
		try
		{
			Thread.sleep(seconds * 1000);
		}

		catch (InterruptedException e)
		{
			e.printStackTrace(System.err);
		}
	}
}