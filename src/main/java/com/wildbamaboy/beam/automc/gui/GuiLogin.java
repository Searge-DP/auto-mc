package com.wildbamaboy.beam.automc.gui;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.wildbamaboy.beam.automc.util.FrameUtils;

@SuppressWarnings("serial")
public class GuiLogin extends JFrame implements ActionListener
{
	private static final int WIDTH = 360;
	private static final int HEIGHT = 200;
	
	private JLabel labelLogin;
	private JLabel labelUsername;
	private JLabel labelPassword;
	private JLabel label2FA;
	
	private JTextField textBoxUsername;
	private JPasswordField textBoxPassword;
	private JTextField textBox2FA;
	
	private JButton buttonLogin;
	
	public GuiLogin()
	{
		this.setTitle("AutoMC - Login");
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Initialize components.
		int xLoc = 40;
		int yUser = 50;
		int yPass = 85;
		
		labelLogin = new JLabel("Please login to Beam.");
		labelLogin.setBounds(120, 15, 130, 25);
		
		labelUsername = new JLabel("Username: ");
		labelUsername.setBounds(40, yUser, 130, 25);
		
		labelPassword = new JLabel("Password: ");
		labelPassword.setBounds(40, yPass, 130, 25);
		
		label2FA = new JLabel("2-Factor Authentication Code: ");

		textBoxUsername = new JTextField();
		textBoxUsername.setBounds(110, yUser, 160, 25);

		textBoxPassword = new JPasswordField();
		textBoxPassword.setBounds(110, yPass, 160, 25);
		
		textBox2FA = new JTextField("2FA Code");
		buttonLogin = new JButton("Login");
		buttonLogin.setBounds(145, 115, 80, 30);
		buttonLogin.addActionListener(this);
		
		FrameUtils.addAllComponents(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == buttonLogin)
		{
			char[] password = textBoxPassword.getPassword();
			
			if (textBoxUsername.getText().isEmpty())
			{
				showMessageDialog(this, "Username cannot be empty!");
			}
			
			else if (password.length == 0)
			{
				showMessageDialog(this, "Password cannot be empty!");				
			}
			
			for (int i = 0; i < password.length; i++)
			{
				password[i] = 0;
			}
		}
	}
}
