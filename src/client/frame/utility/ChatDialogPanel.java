package client.frame.utility;

import java.awt.Color;
import java.awt.GridLayout;
import client.frame.utility.ChatBubblePanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class ChatDialogPanel extends JScrollPane {
	private ChatBubblePanel chatBubble;
	private static JPanel panel = new JPanel();

	public ChatDialogPanel() {
		super(panel);
		chatBubble = new ChatBubblePanel(true, "hhhhh");
		panel.setLayout(new GridLayout(0, 1));
		panel.add(chatBubble);
		panel.setVisible(true);
		this.setVisible(true);
		JScrollBar sBar = this.getVerticalScrollBar(); 
		sBar.setValue(sBar.getMaximum());
		this.addBubble(false, "yyyyyyy");
		this.addBubble(true, "zzzzzz");
		this.addBubble(false, "fffffffff");
		this.addBubble(true, "lllllllll");
		this.addBubble(false, "iiiiiiiiiiiiiiii");
		
	}
	
	public void addBubble(boolean isMySelf, String info) {
		// TODO Auto-generated method stub
		chatBubble = new ChatBubblePanel(isMySelf, info);
		panel.add(chatBubble);
		panel.setVisible(true);
	}
}
