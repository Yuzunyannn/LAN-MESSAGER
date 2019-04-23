package client.frame.info;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserImage extends JPanel {
	private static final long serialVersionUID = 1L;

	public UserImage() {
		BufferedImage img = null;
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("img/1.png");
			img = ImageIO.read(inputStream);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		JLabel label = new JLabel(new ImageIcon(img));
		this.add(label);
		Dimension imgsize = new Dimension(80, 80);
		label.setSize(imgsize);
		this.setSize(imgsize);
		this.setLocation(10, 5);
		// this.setMaximumSize(imgsize);
	}
}
