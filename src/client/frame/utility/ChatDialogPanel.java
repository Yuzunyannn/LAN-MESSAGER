package client.frame.utility;

import java.awt.GridLayout;
import client.frame.utility.ChatBubblePanel;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class ChatDialogPanel extends JScrollPane {
	private ChatBubblePanel chatBubble;
	private static JPanel panel = new JPanel();

	public ChatDialogPanel() {
		super(panel);
		panel.setLayout(new GridLayout(0, 1));
		panel.setVisible(true);
		this.setVisible(true);
		
		
		//测试区域
		this.addBubble(true, "hhhhhhhh");
		this.addBubble(false, "yyyyyyy");
		this.addBubble(false, "yyyyyyy");
		this.addBubble(true, "zzzzzz");
		this.addBubble(false, "fffffffff");
		this.addBubble(true, "lllllllll");
		this.addBubble(false, "iiiiiiiiiiiiiiii");
		this.addBubble(true, "hhhhhhhh");
		this.addBubble(false, "yyyyyyy");
		this.addBubble(false, "yyyyyyy");
		this.addBubble(true, "zzzzzz");
		this.addBubble(false, "fffffffff");
		this.addBubble(true, "lllllllll");
		this.addBubble(false, "iiiiiiiiiiiiiiii");
		JScrollBar scrollBar = this.getVerticalScrollBar();
		//数据添加可能是在调用setValue之后发生，所以此处引入runnable
		SwingUtilities.invokeLater(new Runnable() {
			  public void run() {
			   scrollBar.setValue(scrollBar.getMaximum());
			 }
		}
		);
		
	}
	
	/** 添加一个对话气泡 */
	public void addBubble(boolean isMySelf, String info) {
		// TODO Auto-generated method stub
		panel.setSize(panel.getWidth(), panel.getHeight() + 20);
		chatBubble = new ChatBubblePanel(isMySelf, info);
		panel.add(chatBubble);
		panel.setVisible(true);
		System.out.println(this.verticalScrollBar.getMaximum());
		
	}
}
