package com.wildbamaboy.beam.automc.command;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.wildbamaboy.beam.automc.AutoMC;
import com.wildbamaboy.beam.automc.Font.Color;
import com.wildbamaboy.beam.automc.Font.Format;
import com.wildbamaboy.beam.automc.gui.Gui2FA;
import com.wildbamaboy.beam.automc.gui.GuiLogin;
import com.wildbamaboy.beam.automc.input.KeyListener;
import com.wildbamaboy.beam.automc.input.MouseListener;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import pro.beam.api.exceptions.BeamException;
import pro.beam.api.exceptions.user.TwoFactorWrongCodeException;
import pro.beam.api.exceptions.user.WrongPasswordException;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.services.impl.UsersService;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.robot.Robot;
import pro.beam.interactive.robot.RobotBuilder;

public class CommandAutoMC extends CommandBase
{
	@Override
	public String getCommandName() 
	{
		return "automc";
	}

	@Override
	public String getCommandUsage(ICommandSender commandSender) 
	{
		return "/automc <subcommand> <arguments>";
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] input) throws CommandException 
	{
		try
		{
			final EntityPlayer player = (EntityPlayer)commandSender;
			String subcommand = input.length > 0 ? input[0] : "";
			String[] arguments = input.length > 0 ? (String[]) Arrays.copyOfRange(input, 1, input.length) : null;

			if (subcommand.isEmpty() || subcommand.equalsIgnoreCase("help"))
			{
				displayHelp();
			}

			else if (subcommand.equals("start"))
			{
				doStart();
			}

			else if (subcommand.equals("stop"))
			{
				doStop();
			}

			else
			{
				displayHelp();
			}
		}

		catch (ClassCastException e)
		{
			throw new CommandException("AutoMC commands cannot be used through rcon.");
		}

		catch (Exception e)
		{
			throw new CommandException("An invalid argument was provided. Usage: " + getCommandUsage(commandSender));
		}
	}

	private void doStart()
	{
		if (AutoMC.instance.getRobot() != null)
		{
			addChatMessage(Color.RED + "Bot is currently running. Stop it with /automc stop.");
			return;
		}

		addChatMessage(Color.YELLOW + "Opening login form...");

		//Create our login GUI and open it, prepare our 2FA gui as well.
		final GuiLogin loginGui = new GuiLogin();
		final Gui2FA authGui = new Gui2FA();
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				loginGui.setIsOpen(true);
			}
		});

		//Create our authentication thread.
		Thread authenticationThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				//Wait for the GUI to close.
				while (loginGui.isOpen)
				{
					try
					{
						Thread.sleep(250);
					}

					catch (Exception e)
					{
						e.printStackTrace(System.err);
					}
				}

				//Once it's closed, we can attempt to authenticate.
				BeamUser user = null;

				try
				{
					AutoMC.getLog().info("Logging in as " + loginGui.getUsername() + ".");
					user = AutoMC.instance.getBeamAPI().use(UsersService.class)
							.login(loginGui.getUsername(), String.valueOf(loginGui.getPassword())).checkedGet();
				}

				catch (TwoFactorWrongCodeException e)
				{
					//Prompt for the 2FA code if we fail the first attempt.
					AutoMC.getLog().info("Prompting for 2FA code.");

					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							authGui.setIsOpen(true);
						}
					});

					while (authGui.isOpen)
					{
						try
						{
							Thread.sleep(250);
						}

						catch (Exception err)
						{
							err.printStackTrace(System.err);
						}
					}

					try
					{
						user = AutoMC.instance.getBeamAPI().use(UsersService.class)
								.login(loginGui.getUsername(), String.valueOf(loginGui.getPassword()), authGui.get2FACode()).checkedGet();
					}
					
					catch (TwoFactorWrongCodeException e2)
					{
						addChatMessage(Color.RED + "Wrong two-factor authentication code.");
						return;
					}
					
					catch (BeamException e3)
					{
						addChatMessage(Color.RED + "An unknown login error occurred.");						
					}
				}

				catch (BeamException e)
				{
					//Handle any problems we may encounter.
					if (e instanceof WrongPasswordException)
					{
						addChatMessage(Color.RED + "Wrong username or password.");	
					}

					else
					{
						addChatMessage(Color.RED + "An unknown login error occurred.");
						e.printStackTrace(System.err);
					}
				}

				//Get rid of our GUI and remove Minecraft's pause menu.
				Minecraft.getMinecraft().setIngameFocus();

				//No matter what, set the Beam user for this mod instance.
				AutoMC.getLog().info("Setting authenticated Beam user " + user.username + ":" + user.id);
				AutoMC.instance.setBeamUser(user);

				if (user != null) //And if the user is not null, we logged in. Notify the player.
				{
					addChatMessage(Color.GREEN + "Successfully logged in to Beam.");

					//Construct the robot.
					addChatMessage(Color.YELLOW + "Starting bot...");

					AutoMC.getLog().info("Building robot with credentials: " + user.username + ", " + user.channel.id);

					ListenableFuture<Robot> future = null;
					
					if (authGui.get2FACode().isEmpty())
					{
						future = new RobotBuilder()
							.username(user.username)
							.password(String.valueOf(loginGui.getPassword()))
							.channel(user.channel)
							.build(AutoMC.instance.getBeamAPI());
					}
					
					else
					{
						future = new RobotBuilder()
								.username(user.username)
								.password(String.valueOf(loginGui.getPassword()))
								.channel(user.channel)
								.twoFactor(authGui.get2FACode())
								.build(AutoMC.instance.getBeamAPI());
					}
					
					try
					{
						Robot robot = future.get();

						Futures.addCallback(future, new FutureCallback<Robot>() 
						{
							@Override 
							public void onSuccess(Robot robot) 
							{
								addChatMessage(Color.GREEN + "Bot connected successfully.");
								AutoMC.instance.setRobot(robot);

								robot.on(Protocol.Report.class, new MouseListener());
								robot.on(Protocol.Report.class, new KeyListener());
							}

							@Override 
							public void onFailure(Throwable throwable) 
							{
								addChatMessage(Color.RED + "Unable to create bot. Details logged to console.");
								throwable.printStackTrace(System.err);
							}
						});
					}

					catch (Exception e)
					{
						e.printStackTrace(System.err);
						addChatMessage(Color.RED + "An error occurred. Details logged to console.");
					}
				}
			}
		});

		authenticationThread.start();
	}

	private void doStop()
	{
		Robot robot = AutoMC.instance.getRobot();

		if (robot != null)
		{
			try
			{
				addChatMessage(Color.YELLOW + "Disconnecting bot...");

				robot.disconnect();
				AutoMC.instance.setRobot(null);

				addChatMessage(Color.GREEN + "Disconnected successfully.");
			}

			catch (Exception e)
			{
				addChatMessage(Color.RED + "An unexpected error occurred - " + e.getClass().getSimpleName());
				e.printStackTrace(System.err);
			}
		}

		else //Robot is null, was never set up.
		{
			addChatMessage(Color.RED + "AutoMC is not running. Use /automc start to begin.");			
		}
	}

	@Override
	public int getRequiredPermissionLevel() 
	{
		return 0;
	}

	private void addChatMessage(String message)
	{
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(Color.GOLD + "[AutoMC] " + Format.RESET + message));
	}

	private void addChatMessage(String message, boolean noPrefix)
	{
		if (noPrefix)
		{
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));			
		}

		else
		{
			addChatMessage(message);
		}
	}

	private void displayHelp()
	{
		addChatMessage(Color.DARKRED + "--- " + Color.GOLD + "AUTOMC COMMANDS" + Color.DARKRED + " ---", true);

		addChatMessage(Color.WHITE + " /automc help " + Color.GOLD + " - Shows this list of commands.", true);
		addChatMessage(Color.WHITE + " /automc start " + Color.GOLD + " - Starts the AutoMC bot.", true);
		addChatMessage(Color.WHITE + " /automc stop " + Color.GOLD + " - Stops the AutoMC bot.", true);
	}
}
