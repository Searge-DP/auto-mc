package com.wildbamaboy.beam.automc;

import java.net.URI;

import javax.swing.SwingUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.util.concurrent.ListenableFuture;
import com.wildbamaboy.beam.automc.gui.GuiLogin;
import com.wildbamaboy.beam.automc.gui.GuiSplash;
import com.wildbamaboy.beam.automc.util.CallResult;
import com.wildbamaboy.beam.automc.util.NetUtils;

import pro.beam.api.BeamAPI;
import pro.beam.interactive.robot.Robot;
import pro.beam.interactive.robot.RobotBuilder;

public class AutoMC 
{
	private static BeamAPI beam;
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
		// Lookup the user's ID.
		JSONArray searchResults = NetUtils.jsonArrayGET("https://beam.pro/api/v1/users/search?query=" + username);
		int channelId = -1;

		// Check for errors from jsonArrayGET.
		if (searchResults.getJSONObject(0).has("error"))
		{
			return CallResult.False(searchResults.getJSONObject(0).getString("error"));
		}

		// Find the username and make sure it exactly equals the provided one.
		for (int i = 0; i < searchResults.length(); i++)
		{
			JSONObject obj = searchResults.getJSONObject(i);

			if (obj.getString("username").equalsIgnoreCase(username))
			{
				channelId = obj.getInt("id");
				break;
			}
		}

		// Handle case when it's not found.
		if (channelId == -1)
		{
			return CallResult.False("Your username or password is incorrect.");
		}

		ListenableFuture<Robot> future = new RobotBuilder()
				.username(username)
				.password(password)
				.channel(channelId)
				.build(beam);

		try
		{
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
