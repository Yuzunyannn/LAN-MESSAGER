package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import client.event.EventChatOperation;
import client.event.EventFriendOperation;
import client.event.EventShow;
import client.event.EventsBridge;
import client.frame.MainFrame;
import client.frame.Theme;
import client.frame.utility.UtilityPanel;
import core.Adminsters;
import log.Logger;
import resmgt.ResourceManagement;

public class MemberButton extends JButton {
	private static final long serialVersionUID = 1L;
	public final static int MEMBERBUTTON_HEIGHT = 70;
	public static final String[] MEMBERITEMSTR = { EventFriendOperation.DELETEFRIEND, EventChatOperation.DELETECHAT,
			EventChatOperation.FIXEDCHAT, EventChatOperation.CANELFIXEDCHAT };
	private String memberName;
	public int count;
	private String toolId;
	MouseAdapter mouse;
	// 显示的信封开闭，true开，false闭
	private boolean envelope;
	// 是否正在与该用户聊天
	private boolean isChat;
	// 图片资源
	public static ImageIcon icon_open = new ImageIcon(
			ResourceManagement.instance.getPackResource("img/envelope_open.png").getImage());
	public static ImageIcon icon_closed = new ImageIcon(
			ResourceManagement.instance.getPackResource("img/envelope_closed.png").getImage());

	public MemberButton() {

	}

	public MemberButton(String name) {
		toolId = UtilityPanel.TOOLID_CHATING;
		memberName = name;
		count = 0;
		envelope = true;
		isChat = false;
		// 发布测试
		name = Adminsters.userToInfo(name);
		JLabel member = new JLabel(name);
		MemberButton mtmp = this;
		this.setLayout(null);
		member.setSize(175, 30);
		member.setLocation(20, 20);// 90,20
		member.setFont(Theme.FONT2);
		this.setBackground(Theme.COLOR3);
		this.add(member);
		Dimension size = new Dimension(MainFrame.INFO_RIGION_WIDTH, MEMBERBUTTON_HEIGHT);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.setContentAreaFilled(false);
		ActionListener memberItemListener = new MemberMenuItemMonitor();
		// 右键菜单的监听器
		mouse = new UButtonMouse(MEMBERITEMSTR, memberItemListener) {
			@Override
			public void mousePressed(MouseEvent e) {
				// 产生选择事件
				if (e.getButton() == MouseEvent.BUTTON1) {
					isChat = true;
					count = 0;
					// 产生选择事件
					EventsBridge.frontendEventHandle.post(new EventShow(toolId, memberName));
					// count=0;
//					EventsBridge.frontendEventHandle.post(new EventRecvString(new UserClient("sdsds"),"test"));
				} else if (e.getButton() == MouseEvent.BUTTON3) {
						if (e.isPopupTrigger()) {
						username = ((MemberButton) e.getSource()).getMemberName();
						for (int i = 0; i < item.length; i++) {
							item[i].setActionCommand(username);
							popmenu.add(item[i]);
						}
						popmenu.show(e.getComponent(), e.getX(), e.getY());
					}
					System.out.println("右键点击2");
				}

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (!isChat)
					mtmp.setBackground(Theme.COLOR8);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!isChat)
					mtmp.setBackground(Theme.COLOR3);
			}
		};
		this.addMouseListener(mouse);

	}
	public void envelopechange() {
		if(count!=0)
			envelope=false;
	}
	public void setToolId(String tid) {
		toolId = tid;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public void refresh() {
		this.revalidate();
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = super.getWidth();
		int height = super.getHeight();
		if (isChat)
			g.setColor(Theme.COLOR3.darker());

		else
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

	/** 更改聊天状态，返回是否为正在聊天 */
	public void isChoose(boolean choose) {
		isChat = choose;
		if (choose) {
			envelope = true;
			count = 0;
		}
		if (!choose) {
			this.setBackground(Theme.COLOR3);
			
		}
	}

	/**
	 * 收到消息后执行
	 */
	public void recvMessage() {
		if (isChat) {
			envelope = true;
			count = 0;
		} else {
			count++;
			envelope = false;
		}
	}
}

class UButtonMouse extends MouseAdapter {
	protected JPopupMenu popmenu;
	protected JMenuItem item[];
	protected String username;
	protected ActionListener ItemMonitor;

	public UButtonMouse(String[] str, ActionListener actionListener) {
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
			}
			System.out.println("右键点击1");
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseClicked(e);
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (e.isPopupTrigger()) {
				username = ((MemberButton) e.getSource()).getMemberName();
				for (int i = 0; i < item.length; i++) {
					item[i].setActionCommand(username);
					popmenu.add(item[i]);
				}
				popmenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
}

class MemberMenuItemMonitor implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent event) {

		String[] str = MemberButton.MEMBERITEMSTR;
		String temp = ((JMenuItem) event.getSource()).getText();
		String username = ((JMenuItem) event.getSource()).getActionCommand();
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
