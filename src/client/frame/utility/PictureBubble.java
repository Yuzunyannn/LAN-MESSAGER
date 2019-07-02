package client.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;

import resmgt.UserResource;

public class PictureBubble extends Bubble {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton picButton = new JButton();

	public PictureBubble(String picName, BubbleType type) {
		super("", type);
		// TODO Auto-generated constructor stub
//		if (type == BubbleType.PICTURE) {
//			this.imageIcon = new ImageIcon("src/resources/img/pics/" + picName);
//		} else if (type == BubbleType.MEME) {
//			this.imageIcon = new ImageIcon("src/resources/img/memes/" + picName);
//		}
		this.picButton = new JButton();
		this.picButton.setIcon(UserResource.getMeme("meme-1"));
		this.picButton.setBackground(Color.white);
		this.picButton.setBorderPainted(false);
		this.setLayout(new BorderLayout());
		this.add(this.picButton, BorderLayout.CENTER);
		this.setSize(this.getWidth(), 100);
	}

}
