package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import client.frame.MainFrame;
import client.frame.Theme;

public class UserPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int USERLENGTH=140;
	private UserImage img;
	private JLabel username;
	public UserPanel() {
		this.setLayout(null);
		this.setBackground(Theme.COLOR4);
		img = new UserImage();
		this.add(img);
		username = new JLabel();
		username.setForeground(Theme.COLOR0);
		username.setText("");
		username.setFont(Theme.FONT0);
		this.add(username);
		username.setSize(150, 50);
		username.setLocation(MainFrame.INFO_RIGION_WIDTH - 180, 40);

		this.setMinimumSize(new Dimension(MainFrame.INFO_RIGION_WIDTH, USERLENGTH));
		this.setMaximumSize(new Dimension(MainFrame.INFO_RIGION_WIDTH, USERLENGTH));
	}

	public String getName() {
		return username.getText();
	}

	public void setName(String name) {
		username.setText(name);
		this.repaint();
		this.revalidate();
	}


	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int width = super.getWidth();
		g.setColor(Color.BLACK);
		g.drawLine(0, 0, width, 0);
	}

}
