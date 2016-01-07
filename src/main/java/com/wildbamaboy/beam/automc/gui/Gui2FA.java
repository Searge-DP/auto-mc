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
public class Gui2FA extends JFrame implements ActionListener
{
	public static boolean isOpen;

	private static final int WIDTH = 360;
	private static final int HEIGHT = 150;

	private JLabel labelLogin;
	private JLabel label2FA;

	private JPasswordField textBox2FA;

	private JButton buttonLogin;

	public Gui2FA()
	{
		this.setTitle("AutoMC - Login");
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		//Initialize components.
		int xLoc = 40;
		int yUser = 50;
		int yPass = 85;

		isOpen = true;
		
		labelLogin = new JLabel("Enter your 2FA code.");
		labelLogin.setBounds(120, 15, 130, 45);

		label2FA = new JLabel("2FA Code: ");
		label2FA.setBounds(40, yUser, 130, 25);

		textBox2FA = new JPasswordField();
		textBox2FA.setBounds(110, yUser, 160, 25);

		buttonLogin = new JButton("Login");
		buttonLogin.setBounds(145, 80, 80, 30);
		buttonLogin.addActionListener(this);

		FrameUtils.addAllComponents(this);
		this.getRootPane().setDefaultButton(buttonLogin);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == buttonLogin)
		{
			char[] code = textBox2FA.getPassword();

			if (code.length != 6)
			{
				showMessageDialog(this, "2FA codes must be 6 digits.");
			}

			else
			{
				for (int i = 0; i < code.length; i++)
				{
					code[i] = 0;
				}

				setIsOpen(false);
			}
		}
	}

	public void setIsOpen(boolean value)
	{
		isOpen = value;
		this.setVisible(isOpen);
	}

	public String get2FACode()
	{
		return String.valueOf(textBox2FA.getPassword());
	}
}