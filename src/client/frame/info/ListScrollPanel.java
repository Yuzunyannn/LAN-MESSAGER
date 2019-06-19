package client.frame.info;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import client.event.EventChatOperation;
import client.event.EventFriendOperation;
import client.event.EventRecv.EventRecvString;
import client.event.EventSearchRequest;
import client.event.EventUserSelect;
import client.event.EventsBridge;
import client.frame.Theme;
import event.IEventBus;
import event.SubscribeEvent;
import log.Logger;

public class ListScrollPanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	/**用于区分实例化对象是聊天还是搜索
	 * 未来可能通过继承的方式重构实现区分*/
	public static final String FRIENDPANEL="好友聊天列表";
	public static final String SEARCHPANEL="搜索列表";
	/** 添加列表中的成员数量时可能需要改变 */
	private int height = 0;
	private JPanel p;
	private Component[] content;
	private int fixed = 0;
	protected String state;
	public ListScrollPanel() {
		super();
		state=FRIENDPANEL;
		p = new JPanel();
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
		state=str;
	}
	public int getPHeight() {
		return height;
	}

	public void setPHeight(int height) {
		this.height = height;
	}

	public void addNewMember(String name, Boolean isSearch) {
		if (isSearch) {
			p.add(new SearchButton(name));
			content = p.getComponents();
			height += MemberButton.MEMBERBUTTON_HEIGHT;
			int width = super.getWidth();
			standardHeight(super.getPreferredSize());
			p.setPreferredSize(new Dimension(width, height));
		} else
			addNewMember(name);
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

	public void deductMember(String name) {

		for (int i = p.getComponentCount(); i > 0; i--) {
			MemberButton temp = (MemberButton) p.getComponent(i - 1);
			if (temp.getMemberName().equals(name)) {
				p.remove(i - 1);
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
		content=p.getComponents();
	}

	public void standardHeight(Dimension d) {
		if (p.getComponentCount() < (int) (d.height / MemberButton.MEMBERBUTTON_HEIGHT))
			this.height = d.height + 10;
		/* 测试信息 */
		// System.out.println("count"+p.getComponentCount());
	}
	/**
	 *当用户点击memberbutton是触发*/
	@SubscribeEvent
	public void onUserSelect(EventUserSelect e) 
	{
		for(Component i:content)
		{
			((MemberButton) i).isChoose(e.getUsername());
		}
	}
	@SubscribeEvent
	public void onCountMsg(EventRecvString e) {
		boolean have = false;

		for(Component i:content)
			if(((MemberButton) i).getMemberName().equals(e.from.getUserName()))
			{
				have=true;
				break;
			}	
			else have=false;
		if(!have)
			EventsBridge.frontendEventHandle.post(new EventChatOperation(e.from.getUserName(),EventChatOperation.ADDCHAT,state));

		for (int i = 0; i < content.length; i++)
			if (((MemberButton) content[i]).getMemberName().equals(e.from.getUserName())) {
				((MemberButton) content[i]).RecvMessage();
				//((MemberButton) content[i]).count = ((MemberButton) content[i]).count + 1;
				System.out.println("name :" + ((MemberButton) content[i]).getMemberName() + " count :"
						+ ((MemberButton) content[i]).count);
				setTop(((MemberButton) content[i]).getMemberName());
				System.out.println(((MemberButton) content[i]).getMemberName() + ((MemberButton) content[i]).count);
			}
		p.removeAll();
		for (Component i : content) {
			p.add(i);
		}
		content=p.getComponents();
		this.refresh();
	}

	public void refresh() {
		this.revalidate();
		this.repaint();
	}

	/** 事件处理 */
	

	@SubscribeEvent
	public void onFreindOperator(EventFriendOperation e) {

		/**
		 * 添加好友应在添加search的panel中响应事件 if (e.type.equals(EventFriendOperation.ADDFRIEND))
		 * 好友列表添加 EventsBridge.frontendEventHandle.post(new
		 * EventChatOperation(e.username,EventChatOperation.ADDCHAT))
		 */
		if (e.type.equals(EventFriendOperation.DELETEFRIEND))
			/**
			 * 好友列表删除
			 */
			EventsBridge.frontendEventHandle.post(new EventChatOperation(e.username, EventChatOperation.DELETECHAT));
		this.refresh();
	}

	public void initEvent(IEventBus bus) {
		bus.register(this);
	}
}
