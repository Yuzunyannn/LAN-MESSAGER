package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

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
	public static ImageIcon icon_open = new ImageIcon("src/img/envelope_open.png");
	public static ImageIcon icon_closed = new ImageIcon("src/img/envelope_closed.png");
	//显示的信封开闭，true开，false闭
	private boolean envelope;
	//是否正在与该用户聊天
	private boolean isChat;
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
		envelope = true;
		MouseAdapter mouse = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// 产生选择事件
				EventsBridge.frontendEventHandle.post(new EventShow(user));
				MemberButton mb=(MemberButton)e.getComponent();
				mb.isChoose();
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
		int x=200,y=23;
		if (envelope) {
			g.drawImage(icon_open.getImage(), x, y-6, null);
		} else {
			g.drawImage(icon_closed.getImage(), x, y, null);
			g.setColor(Color.red);
			if (count > 99)
				g.fillOval(x+25, y+14, 10, 10);
			else {
				g.fillOval(x+20, y+12, 20, 20);
				g.setColor(Color.white);
				if (count < 10)
					g.drawString("" + count, x+28, y+27);
				else
					g.drawString("" + count, x+24, y+27);
			}
			g.setColor(Color.black);
		}

		super.paint(g);
	}
/***/
	public void isChoose()
	{
		isChat=true;
		count=0;
	}
/**
 * 收到消息后执行*/
public void RecvMessage() 
{
	if(isChat)
	{
		envelope=true;
		count=0;
	}
	else
	{
		count++;
		envelope=false;
	}
}
}
