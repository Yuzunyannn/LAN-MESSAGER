package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import client.event.EventChatOperation;
import client.event.EventFriendOperation;
import client.event.EventsBridge;
import client.frame.MainFrame;
import client.frame.Theme;
import log.Logger;

public class BasePanelButton extends JButton {
	public static BasePanelButton listButton = new BasePanelButton();
	private static final long serialVersionUID = 1L;
	public final static int BasePanelButton_HEIGHT = 70;
	protected String baseName;
	protected String toolId;
	protected boolean choose;

	public BasePanelButton() {

	}

	public BasePanelButton(String id,String name) {
		baseName = id;
		addLabel(name);
		this.setLayout(null);
		
		Dimension size = new Dimension(MainFrame.INFO_RIGION_WIDTH, BasePanelButton_HEIGHT);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.setContentAreaFilled(false);

	}
public void addLabel(String label) {
	JLabel member = new JLabel(label);
	member.setSize(175, 30);
	member.setLocation(20, 20);// 90,20
	member.setFont(Theme.FONT2);
	this.setBackground(Theme.COLOR3);
	this.add(member);
	
}
	public void isChoose(boolean isChoose) {
		choose=isChoose;
		if (isChoose)
			choose();
		else
			notChoose();
	}

	public void removeBindTool() {
		toolId = null;
	}

	public void lclickedShow() {
	}

	public void rclickedShow() {
	}

	public void choose() {
	}

	public void notChoose() {
	}

	public void recvMessageShow() {

	}



	public void bindToolId(String tid) {
		toolId = tid;
	}

	@Override
	public String getName() {
		return baseName;
	}

	@Override
	public void setName(String baseName) {
		this.baseName = baseName;
	}

	public void refresh() {
		this.revalidate();
		this.repaint();
	}

}

class BaseButtonMouse extends MouseAdapter {
	protected JPopupMenu popmenu;
	protected JMenuItem item[];
	protected String username;
	protected ActionListener ItemMonitor;

	public BaseButtonMouse(String[] str, ActionListener actionListener) {
		super();
		popmenu = new JPopupMenu();
		item = new JMenuItem[str.length];
		Border border = BorderFactory.createLineBorder(Theme.COLOR5);
		ItemMonitor = actionListener;
		for (int i = 0; i < item.length; i++) {
			item[i] = new JMenuItem(str[i]);
			item[i].setFont(Theme.FONT4);
			item[i].setBackground(Color.WHITE);
			item[i].setPreferredSize(new Dimension(150, 40));
			item[i].setHorizontalAlignment(SwingConstants.CENTER);
			item[i].setBorder(null);
			item[i].setActionCommand(i + "");
			item[i].addActionListener(ItemMonitor);

		}
		popmenu.setBackground(Color.WHITE);
		popmenu.setBorder(border);
		// popmenu.setPopupSize(160,200);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (e.isPopupTrigger()) {
				username = ((MemberButton) e.getSource()).getMemberName();
				for (int i = 0; i < item.length; i++) {
					item[i].setActionCommand(username);
					popmenu.add(item[i]);
				}
				popmenu.show(e.getComponent(), e.getX(), e.getY());
				System.out.println("右键release弹出");
			}
			
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (e.isPopupTrigger()) {
				username = ((MemberButton) e.getSource()).getMemberName();
				for (int i = 0; i < item.length; i++) {
					item[i].setActionCommand(username);
					popmenu.add(item[i]);
				}
				popmenu.show(e.getComponent(), e.getX(), e.getY());
				System.out.println("右键press弹出");
			}
			
		}

	}

}

//class BaseMenuItemMonitor implements ActionListener {
//	public String []prompt;
//	
//	public BaseMenuItemMonitor(String []prompt) {
//		this.prompt=prompt;
//	}
//	@Override
//	public void actionPerformed(ActionEvent event) {
//
//		String[] str = prompt;
//		String temp = ((JMenuItem) event.getSource()).getText();
//		String username = ((JMenuItem) event.getSource()).getActionCommand();
//		if (temp.equals(str[0])) {
//			EventsBridge.frontendEventHandle.post(new EventFriendOperation(username, str[0]));
//			Logger.log.impart(EventFriendOperation.DELETEFRIEND + username);
//		} else if (temp.equals(str[1])) {
//			EventsBridge.frontendEventHandle.post(new EventChatOperation(username, str[1]));
//			Logger.log.impart(EventChatOperation.DELETECHAT + username);
//		} else if (temp.equals(str[2])) {
//			EventsBridge.frontendEventHandle.post(new EventChatOperation(username, str[2]));
//			Logger.log.impart(EventChatOperation.FIXEDCHAT + username);
//		} else {
//			EventsBridge.frontendEventHandle.post(new EventChatOperation(username, str[3]));
//			Logger.log.impart(EventChatOperation.CANELFIXEDCHAT + username);
//		}
//
//	}

