package client.frame.info;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import client.event.EventChatOperation;
import client.event.EventRecv.EventRecvString;
import client.event.EventSearchRequest;
import client.event.EventShow;
import client.event.EventsBridge;
import client.frame.Theme;
import event.IEventBus;
import event.SubscribeEvent;
import log.Logger;
import user.User;

public class ListScrollPanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	/**
	 * 用于区分实例化对象是聊天还是搜索 未来可能通过继承的方式重构实现区分
	 */
	public static final String FRIENDPANEL = "好友聊天列表";
	public static final String SEARCHPANEL = "搜索列表";
	/** 添加列表中的成员数量时可能需要改变 */
	private int height = 0;
	private JPanel p;
	private Component[] content;
	private int fixed = 0;
	protected String state;

	public ListScrollPanel() {
		super();
		state = FRIENDPANEL;
		p = new JPanel();
		int width = super.getWidth();
		p.setPreferredSize(new Dimension(width, getHeight()));
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBackground(Theme.COLOR5);
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

	}

	/** 置顶并且消息数置为recvcount */
	public void setTop(String name, int recvcount) {
		int temp;
		temp = getMember(name);
		if (temp == -1)
			return;
		Component tempbutton;
		tempbutton = content[temp];

		for (int i = temp; i > fixed; i--) {
			content[i] = content[i - 1];
		}
		((MemberButton) tempbutton).count = recvcount;
		content[fixed] = tempbutton;
		Component[] re = content;
		p.removeAll();
		content = re;
		for (Component i : content) {
			p.add(i);
		}
		this.refresh();
	}

	/** 把成员置顶 */
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
		this.refresh();
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

	public void setState(String str) {
		state = str;
	}

	public int getPHeight() {
		return height;
	}

	public void setPHeight(int height) {
		this.height = height;
	}

	public void addNewMember(User name, Boolean isSearch) {
		if (isSearch) {
			p.add(new SearchButton(name.getUserName()));
			content = p.getComponents();
			height += MemberButton.MEMBERBUTTON_HEIGHT;
			int width = super.getWidth();
			standardHeight(super.getPreferredSize());
			p.setPreferredSize(new Dimension(width, height));
		} else
			addNewMember(name);
	}

	public void addNewMember(User user) {
		if (InfoPanel.userSet.contains(user))
			return;
		InfoPanel.userSet.add(user);
		p.add(new MemberButton(user.getUserName()));

		content = p.getComponents();
		height += MemberButton.MEMBERBUTTON_HEIGHT;
		int width = super.getWidth();
		standardHeight(super.getPreferredSize());
		p.setPreferredSize(new Dimension(width, height));
	}
	public void addNewMember(User user,int count) {
		if (InfoPanel.userSet.contains(user))
			return;
		InfoPanel.userSet.add(user);
		MemberButton tmp=new MemberButton(user.getUserName());
		tmp.count=count;
		p.add(tmp);
		tmp.envelopechange();
		content = p.getComponents();
		height += MemberButton.MEMBERBUTTON_HEIGHT;
		int width = super.getWidth();
		standardHeight(super.getPreferredSize());
		p.setPreferredSize(new Dimension(width, height));
	}
	public void deductMember(User user) {
		boolean done = false;
		for (int i = p.getComponentCount(); i > 0; i--) {
			MemberButton temp = (MemberButton) p.getComponent(i - 1);
			if (temp.getMemberName().equals(user.getUserName())) {
				p.remove(i - 1);
				height -= MemberButton.MEMBERBUTTON_HEIGHT;
				content = p.getComponents();
				done = true;
				break;
			} else
				done = false;
		}
		if (!done)
			Logger.log.warn("查无此人");
		int width = super.getWidth();
		standardHeight(super.getPreferredSize());
		p.setPreferredSize(new Dimension(width, height));
	}

	public void deleteAllMember() {
		p.removeAll();
		content = p.getComponents();
	}

	public void standardHeight(Dimension d) {
		if (p.getComponentCount() < (int) (d.height / MemberButton.MEMBERBUTTON_HEIGHT))
			this.height = d.height + 10;
		/* 测试信息 */
		// System.out.println("count"+p.getComponentCount());
	}

	@SubscribeEvent
	public void onCountFile(transfer.EventFileRecv.Start e) {
		boolean have = false;
		for (Component i : content)
			if (((MemberButton) i).getMemberName().equals(e.getFrom().getUserName())) {
				have = true;
				break;
			} else
				have = false;
		if (!have) {
			EventsBridge.frontendEventHandle
					.post(new EventChatOperation(e.getFrom().getUserName(), EventChatOperation.ADDCHAT, state));
		}

		for (int i = 0; i < content.length; i++)
			if (((MemberButton) content[i]).getMemberName().equals(e.getFrom().getUserName())) {
				((MemberButton) content[i]).RecvMessage();
				System.out.println("name :" + ((MemberButton) content[i]).getMemberName() + " count :"
						+ ((MemberButton) content[i]).count);
				setTop(((MemberButton) content[i]).getMemberName());
				System.out.println(((MemberButton) content[i]).getMemberName() + ((MemberButton) content[i]).count);
			}
		p.removeAll();
		for (Component i : content) {
			p.add(i);
		}
		content = p.getComponents();
		this.refresh();
	}

	@SubscribeEvent
	public void onCountMsg(EventRecvString e) {
		boolean have = false;

		for (Component i : content)
			if (((MemberButton) i).getMemberName().equals(e.from.getUserName())) {
				have = true;
				break;
			} else
				have = false;
		if (!have)
			EventsBridge.frontendEventHandle
					.post(new EventChatOperation(e.from.getUserName(), EventChatOperation.ADDCHAT, state));

		for (int i = 0; i < content.length; i++)
			if (((MemberButton) content[i]).getMemberName().equals(e.from.getUserName())) {
				((MemberButton) content[i]).RecvMessage();
				// ((MemberButton) content[i]).count = ((MemberButton) content[i]).count + 1;
				System.out.println("name :" + ((MemberButton) content[i]).getMemberName() + " count :"
						+ ((MemberButton) content[i]).count);
				setTop(((MemberButton) content[i]).getMemberName());
				System.out.println(((MemberButton) content[i]).getMemberName() + ((MemberButton) content[i]).count);
			}
		p.removeAll();
		for (Component i : content) {
			p.add(i);
		}
		content = p.getComponents();
		this.refresh();
	}

	public void refresh() {
		this.revalidate();
		this.repaint();
	}

	/**
	 * 好友盘专属的函数 好友聊天状态全部还原
	 */
	public void ChatStateClear() {
		if (state == FRIENDPANEL) {
			content = p.getComponents();
			for (Component i : content) {
				((MemberButton) i).isChoose(false);

			}
			p.removeAll();
			for (Component i : content) {
				p.add(i);
			}
			this.refresh();
		} else
			return;
	}

	/** 事件处理 */
	@SubscribeEvent
	public void onSearchRequest(EventSearchRequest e) {
		for (Component i : content) {
			p.add(i);
		}
		this.refresh();
	}

	@SubscribeEvent
	public void onShow(EventShow e) {
		for (int i = 0; i < content.length; i++) {
			MemberButton tmp = (MemberButton) content[i];
			if (tmp.getMemberName().equals(e.id))
				tmp.isChoose(true);
			else
				tmp.isChoose(false);
		}
		this.refresh();
	}

	public void initEvent(IEventBus bus) {
		bus.register(this);
	}
}
