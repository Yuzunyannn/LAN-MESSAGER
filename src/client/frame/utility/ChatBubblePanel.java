package client.frame.utility;

import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChatBubblePanel extends JPanel {
	private JButton user;
	private JLabel ta;

	//构造函数，生成一个对话气泡，显示信息的参数待定！
	public ChatBubblePanel(boolean isMySelf, String info) {
		// TODO Auto-generated constructor stub
		ImageIcon imageIcon = new ImageIcon("src/img/1.png");
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(40, 40, 40));
		if (isMySelf) {
			this.setLayout(new FlowLayout(FlowLayout.RIGHT));
			user = new JButton(":自己");
			user.setIcon(imageIcon);
			ta = new JLabel(info);
			this.add(ta);
			this.add(user);
		}else {
			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			user = new JButton("对方:");
			user.setIcon(imageIcon);
			ta = new JLabel(info);
			this.add(user);
			this.add(ta);
		}
		//icon 
		
		this.setVisible(true);
	}	

}
