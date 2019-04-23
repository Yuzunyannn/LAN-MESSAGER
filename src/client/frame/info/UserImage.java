package client.frame.info;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserImage extends JPanel{
public UserImage() {
	BufferedImage img=null;
	try {
		img=ImageIO.read(new File("C:\\Users\\dispute\\git\\LAN-MESSAGER\\src\\img\\1.png"));
	} catch (IOException e) {
		// TODO 自动生成的 catch 块
		e.printStackTrace();
	}
	JLabel label=new JLabel(new ImageIcon(img));
	this.add(label);
	Dimension imgsize=new Dimension(80,80);
	label.setSize(imgsize);
	this.setSize(imgsize);
	this.setLocation(10, 5);
//	this.setMaximumSize(imgsize);
}
}
