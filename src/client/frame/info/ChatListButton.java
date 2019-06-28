package client.frame.info;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import client.event.EventChatOperation;
import client.event.EventFriendOperation;
import client.event.EventShow;
import client.event.EventsBridge;
import client.frame.Theme;
import log.Logger;
import resmgt.ResourceManagement;

public class ChatListButton extends BasePanelButton {
	static final String [] prompt= {EventFriendOperation.DELETEFRIEND, EventChatOperation.DELETECHAT,
			EventChatOperation.FIXEDCHAT, EventChatOperation.CANELFIXEDCHAT};
	private String chatName;
	// 显示的信封开闭，true开，false闭
		private boolean envelope;
		private int count=0;
		public static ImageIcon icon_open = new ImageIcon(
				ResourceManagement.instance.getPackResource("img/envelope_open.png").getImage());
		public static ImageIcon icon_closed = new ImageIcon(
				ResourceManagement.instance.getPackResource("img/envelope_closed.png").getImage());
public ChatListButton() {
	super();
	addLabel(baseName);
	ChatListButton mtmp=this;
	this.addMouseListener(new BaseButtonMouse(prompt,new ChatMonitor()) {
		@Override 
		public void mouseClicked(MouseEvent e) {
			EventsBridge.frontendEventHandle.post(new EventShow(toolId,baseName));
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			if (!choose)
				mtmp.setBackground(Theme.COLOR8);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (!choose)
				mtmp.setBackground(Theme.COLOR3);
		}
		
	} );
}
public ChatListButton(String id,String name) {
	super(id,name);
	chatName=name;
	ChatListButton mtmp=this;
	this.addMouseListener(new BaseButtonMouse(prompt,new ChatMonitor()) {
		@Override 
		public void mouseClicked(MouseEvent e) {
			EventsBridge.frontendEventHandle.post(new EventShow(toolId,baseName));
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			if (!choose)
				mtmp.setBackground(Theme.COLOR8);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (!choose)
				mtmp.setBackground(Theme.COLOR3);
		}
		
	} );
}
@Override
public void recvMessageShow() {
	if(count!=0)
		envelope=false;
}
@Override
public void choose() {
	envelope = true;
	count = 0;
	this.setBackground(Theme.COLOR3.darker());
}
@Override 
public void notChoose() {
	this.setBackground(Theme.COLOR3);
}
@Override
public void paintComponent(Graphics g) {
	super.paintComponent(g);
	int width = super.getWidth();
	int height = super.getHeight();
	g.setColor(getBackground());
	g.fillRect(0, 0, width, height);
	int x = 200, y = 23;
	if (envelope) {
		g.drawImage(icon_open.getImage(), x, y - 6, null);
	} else {
		g.drawImage(icon_closed.getImage(), x, y, null);
		g.setColor(Color.red);
		if (count > 99)
			g.fillOval(x + 25, y + 14, 10, 10);
		else {
			g.fillOval(x + 20, y + 12, 20, 20);
			g.setColor(Color.white);
			if (count < 10)
				g.drawString("" + count, x + 28, y + 27);
			else
				g.drawString("" + count, x + 24, y + 27);
		}
		g.setColor(Color.black);
	}
}
}
class ChatMonitor implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] str = ChatListButton.prompt;
		String temp = ((JMenuItem) e.getSource()).getText();
		String username = ((JMenuItem) e.getSource()).getActionCommand();
		if (temp.equals(str[0])) {
			EventsBridge.frontendEventHandle.post(new EventFriendOperation(username, str[0]));
			Logger.log.impart(EventFriendOperation.DELETEFRIEND + username);
		} else if (temp.equals(str[1])) {
			EventsBridge.frontendEventHandle.post(new EventChatOperation(username, str[1]));
			Logger.log.impart(EventChatOperation.DELETECHAT + username);
		} else if (temp.equals(str[2])) {
			EventsBridge.frontendEventHandle.post(new EventChatOperation(username, str[2]));
			Logger.log.impart(EventChatOperation.FIXEDCHAT + username);
		} else {
			EventsBridge.frontendEventHandle.post(new EventChatOperation(username, str[3]));
			Logger.log.impart(EventChatOperation.CANELFIXEDCHAT + username);
		}
		
	}
	
}