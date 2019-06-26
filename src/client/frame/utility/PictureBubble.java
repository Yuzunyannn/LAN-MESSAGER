package client.frame.utility;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class PictureBubble extends Bubble {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon imageIcon;
	private JButton picButton = new JButton();

	public PictureBubble(String picName, BubbleType type) {
		super("", type);
		// TODO Auto-generated constructor stub
		if (type == BubbleType.PICTURE) {
			this.imageIcon = new ImageIcon("src/resources/img/pics/" + picName);
		} else if (type == BubbleType.MEME) {
			this.imageIcon = new ImageIcon("src/resources/img/memes/" + picName);
		}
		this.imageIcon.setImage(this.imageIcon.getImage().getScaledInstance(100, 100, 100));
		this.picButton = new JButton();
		this.picButton.setIcon(this.imageIcon);
		this.picButton.setBorderPainted(false);
		this.setLayout(new BorderLayout());
		this.add(this.picButton, BorderLayout.CENTER);
		this.setSize(this.getWidth(), 100);
	}

}
