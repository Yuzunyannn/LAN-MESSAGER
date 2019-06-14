package client.frame.info;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

import client.event.EventRecv.EventRecvString;
import client.event.EventShow;
import client.event.EventsBridge;
import client.frame.MainFrame;
import client.frame.Theme;
import client.user.UserClient;
import user.User;
public class MemberButton extends JButton {
	private static final long serialVersionUID = 1L;
	public final static int MEMBERBUTTON_HEIGHT = 70;
	private String memberName = "小明";
	private User user;
	public int count;

	public MemberButton(String name) {
		memberName = name;
		count = 0;
		user = new UserClient(name);
		JLabel member = new JLabel(name);
		this.setLayout(null);
		this.add(member);
		member.setSize(150, 30);
		member.setLocation(0, 20);
		member.setFont(Theme.FONT2);
		Dimension size = new Dimension(MainFrame.INFO_RIGION_WIDTH, MEMBERBUTTON_HEIGHT);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.setContentAreaFilled(false);
		MouseAdapter mouse = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// 产生选择事件
				EventsBridge.frontendEventHandle.post(new EventShow(user));
				// count=0;
				/**
				 * 消息计数测试用 EventsBridge.frontendEventHandle.post(new EventRecvString(new
				 * User(memberName), ""));
				 */
				 EventsBridge.frontendEventHandle.post(new EventRecvString(new UserClient(memberName), "test"));
				
			}
		};
		this.addMouseListener(mouse);

	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Theme.COLOR2);
		int width = super.getWidth();
		int height = super.getHeight();
		g.fillRect(0, 0, width, height);
		super.paint(g);
	}

}
