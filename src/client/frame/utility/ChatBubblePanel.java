package client.frame.utility;

import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.frame.Theme;

public class ChatBubblePanel extends JPanel {
	private JButton userIcon;
	private JLabel userName;
	private JLabel userDialog;

	/** 构造函数，生成一个对话气泡，显示信息的参数待定！*/
	public ChatBubblePanel(boolean isMySelf, String info) {
		// TODO Auto-generated constructor stub
		ImageIcon imageIcon = new ImageIcon("src/img/1.png");
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(40, 40, 40));
		if (isMySelf) {
			this.setLayout(new FlowLayout(FlowLayout.RIGHT));
			userIcon = new JButton();
			userName = new JLabel(":自己");
			userIcon.setIcon(imageIcon);
			userDialog = new JLabel(info);
			this.add(userDialog);
			this.add(userName);
			this.add(userIcon);
		}else {
			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			userIcon = new JButton();
			userName = new JLabel("对方:");
			userIcon.setIcon(imageIcon);
			userDialog = new JLabel(info);
			this.add(userIcon);
			this.add(userName);
			this.add(userDialog);
		}
		userDialog.setFont(Theme.FONT1);
		userName.setFont(Theme.FONT1);
		
		this.setVisible(true);
	}	

}
