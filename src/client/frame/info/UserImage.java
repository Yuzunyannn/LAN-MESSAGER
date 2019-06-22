package client.frame.info;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import resmgt.UserResource;

public class UserImage extends JPanel {
	private static final long serialVersionUID = 1L;

	public UserImage() {
		JLabel label = new JLabel(UserResource.getHeadIcon("guest", UserResource.HeadIconSize.BIG));
		this.add(label);
		Dimension imgsize = new Dimension(80, 80);
		label.setSize(imgsize);
		this.setSize(imgsize);
		this.setLocation(10, 5);
		// this.setMaximumSize(imgsize);
	}
}
