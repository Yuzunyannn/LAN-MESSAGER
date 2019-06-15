package client.frame.info;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import client.event.EventChatOperation;
import client.event.EventFriendOperation;
import client.event.EventRecv.EventRecvString;

import client.event.EventSearchRequest;
import client.event.EventShow;
import client.event.EventsBridge;
import client.frame.Theme;
import client.user.UserClient;
import event.Event;
import event.IEventBus;
import event.SubscribeEvent;
import log.Logger;
import user.User;

public class ListScrollPanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	/** 添加列表中的成员数量时可能需要改变 */
	private int height = 0;
	private JPanel p;
	private Component[] content;
	private int fixed = 0;

	public ListScrollPanel() {
		super();
		p = new JPanel();
		Component temp = this;
		int width = super.getWidth();
		p.setPreferredSize(new Dimension(width, getHeight()));
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBackground(Theme.COLOR0);
		content = p.getComponents();
		this.add(p);

		this.setViewportView(p);
		// 设置垂直滚动条的显示: 一直显示
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// 样式
		JScrollBar bar = new JScrollBar();
		bar.setUI(new client.frame.ui.ScrollBarUI());
		this.setVerticalScrollBar(bar);
		this.setBorder(null);
		bar.setBackground(Theme.COLOR1);

	}

	/* 在加入User类后需要修改 */
	public void setTop(String name) {
		int temp;
		temp = getMember(name);
		if (temp == -1)
			return;
		Component tempbutton;
		tempbutton = content[temp];

		for (int i = temp; i > fixed; i--) {
			content[i] = content[i - 1];
		}
		content[fixed] = tempbutton;
		Component[] re = content;
		p.removeAll();
		content = re;
		for (Component i : content) {
			p.add(i);
		}
		// for (Component i : p.getComponents())
		// System.out.println(((MemberButton) i).getMemberName());

	}

	public void setFixed(String name) {
		setTop(name);
		fixed = fixed + 1;
	}

	public void canelFixed() {
		if (fixed == 0)
			return;
		else
			fixed = fixed - 1;
	}

	public int getMember(String name) {
		MemberButton temp;
		for (int i = 0; i < content.length; i++) {
			temp = (MemberButton) content[i];
			if (temp.getMemberName().equals(name)) {
				// System.out.println(i);
				return i;

			}
		}
		return -1;

	}

	public int getPHeight() {
		return height;
	}

	public void setPHeight(int height) {
		this.height = height;
	}

	public void addNewMember(String name) {
		p.add(new MemberButton(name));

		content = p.getComponents();
		height += MemberButton.MEMBERBUTTON_HEIGHT;
		int width = super.getWidth();
		standardHeight(super.getPreferredSize());
		/* 测试信息 */
		// System.out.println("scroll"+super.getPreferredSize().height);
		// System.out.println("height"+this.height);
		p.setPreferredSize(new Dimension(width, height));
	}
/**删除到两个的时候刷新出现问题*/
	public void deductMember(String name) {
		if(content.length==2)
			Logger.log.warn("删除到两个的时候刷新出现问题");
		for (int i = p.getComponentCount(); i > 0; i--) {
			MemberButton temp = (MemberButton) p.getComponent(i-1);
			if (temp.getMemberName().equals(name)) {
				p.remove(i-1);
				height -= MemberButton.MEMBERBUTTON_HEIGHT;
				content = p.getComponents();
				break;

			} else
				Logger.log.error(name + "查无此人");
			int width = super.getWidth();

			
			standardHeight(super.getPreferredSize());
			p.setPreferredSize(new Dimension(width, height));
		}
	}

	public void deleteAllMember() {
		p.removeAll();
	}

	public void standardHeight(Dimension d) {
		if (p.getComponentCount() < (int) (d.height / MemberButton.MEMBERBUTTON_HEIGHT))
			this.height = d.height + 10;
		/* 测试信息 */
		// System.out.println("count"+p.getComponentCount());
	}

	@SubscribeEvent
	public void onCountMsg(EventRecvString e) {
		p.removeAll();

		for (int i = 0; i < content.length; i++)
			if (((MemberButton) content[i]).getMemberName().equals(e.from.getUserName())) {
				((MemberButton) content[i]).count = ((MemberButton) content[i]).count + 1;
				System.out.println("name :" + ((MemberButton) content[i]).getMemberName() + " count :"
						+ ((MemberButton) content[i]).count);
				setTop(((MemberButton) content[i]).getMemberName());

				// break;
			}

		for (Component i : content) {
			p.add(i);
		}
		this.revalidate();

	}

	/** 事件处理 */
	@SubscribeEvent
	public void onSearchRequest(EventSearchRequest e) {

		for (int i = 0; i < content.length; i++)
			if (((MemberButton) content[i]).getMemberName().equals(e.from.getUserName())) {
				((MemberButton) content[i]).RecvMessage();
			}

		for (Component i : content) {
			p.add(i);
		}
	}

	@SubscribeEvent
	public void onFreindOperator(EventFriendOperation e) {
		if (e.type.equals(EventFriendOperation.ADDFRIEND))
			addNewMember(e.username);
		else if (e.type.equals(EventFriendOperation.DELETEFRIEND))
			deductMember(e.username);
		this.revalidate();
	}

	@SubscribeEvent
	public void onChatOperator(EventChatOperation e) {
		if (e.type.equals(EventChatOperation.FIXEDCHAT))
			setFixed(e.username);
		else if (e.type.equals(EventChatOperation.DELETECHAT))
			deductMember(e.username);
		else if (e.type.equals(EventChatOperation.CANELFIXEDCHAT))
			canelFixed();
		this.revalidate();
	}

	public void initEvent(IEventBus bus) {
		bus.register(this);
	}
}
