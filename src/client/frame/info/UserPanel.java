package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import client.frame.Theme;

public class UserPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final static int USERPANEL_WIDTH = 275;
	private String name = "Tatssu";
	private SearchPanel sp;
	private UserImage img;

	public UserPanel() {
		this.setLayout(null);
		this.setBackground(Theme.COLOR4);
		img = new UserImage();
		this.add(img);

		JLabel username = new JLabel();
		username.setText(name);
		username.setFont(Theme.FONT0);
		this.add(username);
		username.setSize(150, 50);
		username.setLocation(USERPANEL_WIDTH - 175, 15);

		this.setMinimumSize(new Dimension(USERPANEL_WIDTH, 80));
		this.setMaximumSize(new Dimension(USERPANEL_WIDTH, 80));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SearchPanel getSp() {
		return sp;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int width = super.getWidth();
		g.setColor(Color.BLACK);
		g.drawLine(0, 0, width, 0);
		/* 名字大小测试 */
		g.drawRect(USERPANEL_WIDTH - 175, 15, 150, 50);
	}

}
