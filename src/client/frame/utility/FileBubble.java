package client.frame.utility;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import javafx.scene.control.Alert;

public class FileBubble extends Bubble {
	
	private JButton fileButton = new JButton();
	private ImageIcon imageIcon;
	public FileBubble(String name) {
		super(name);
		if (name.indexOf(".doc") != -1 || name.indexOf(".txt") != -1) {
			imageIcon = new ImageIcon("src/img/icons/" + "icon-document.jpg");
			imageIcon.setImage(imageIcon.getImage().getScaledInstance(40, 40, 40));
			fileButton = new JButton();
			fileButton.setIcon(imageIcon);
			this.setLayout(new FlowLayout(FlowLayout.RIGHT));
			this.add(fileButton);
		} else {
			System.out.println("暂不支持图标显示的文件类型");
		}
	}
	
}
