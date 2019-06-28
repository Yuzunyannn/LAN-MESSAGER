package client.frame.info;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import client.event.EventChatOperation;
import client.event.EventFriendOperation;
import client.event.EventIPC;
import client.event.EventSearchRequest;
import client.event.EventULChange;
import client.event.EventsBridge;
import client.frame.Theme;
import client.user.UserClient;
import event.IEventBus;
import event.SubscribeEvent;
import log.Logger;
import user.UOnline;
import user.User;
import user.message.MUSSearch;

/** 界面左边的区域 用户区域 */
public class InfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	// 好友列表管理面板
	private ListScrollPanel memberField;
	// 搜索结果展示面板
	private ListScrollPanel searchMemberField;
	// 上方搜索面板
	private SearchPanel searchField;
	// 用户信息面板
	private UserPanel userField;
	// 当前状态
	private int state;
	private LinkedList<UserClient> ul;
	static public HashSet<User> userSet = new HashSet<User>();

	public InfoPanel() {
		this.setBackground(Theme.COLOR5);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		userField = new UserPanel();
		searchField = new SearchPanel();
		memberField = new ListScrollPanel();
		searchMemberField = new ListScrollPanel();
		ul = new LinkedList<UserClient>();
		searchMemberField.setState(ListScrollPanel.SEARCHPANEL);
		this.add(userField);
		this.add(searchField);
		this.add(memberField);
		this.setFocusable(true);
		state = EventIPC.FRIENDS;
		userField.setPreferredSize(new Dimension(0, UserPanel.USERLENGTH));
		searchField.setPreferredSize(new Dimension(0, SearchPanel.SEARCH_FIELD_LENGTH));
		// 好友测试用
		ul.add(new UserClient("lyl"));
		ul.add(new UserClient("ycy"));
		ul.add(new UserClient("ssj"));
		ul.add(new UserClient("myk"));
	}

	// 事件注册
	public void initEvent(IEventBus bus) {
		bus.register(this);
		memberField.initEvent(bus);
		searchField.initEvent(bus);
		searchMemberField.initEvent(bus);
	}

	// 重绘
	public void refresh() {
		this.revalidate();
		this.repaint();
	}

	// 响应事件函数
	/**
	 * 当infoPanel状态改变 如果要实现切盘操作，需要向searchMemberField 中调用addNewMember(String
	 * name,Boolean isSearch)
	 */
	@SubscribeEvent
	public void onStateChange(client.event.EventIPC e) {
		System.out.println("infoPanel的状态发生了变化，从状态" + state + "变为状态" + e.state);
		// 判断状态转换合法
		// isVaildChage(int state_b,int state_a)
		Component b = stateToPanel(state);
		Component a = stateToPanel(e.state);
		// 原面板执行操作
		BPanelChange(state);
		// 新面板执行操作
		APanelChange(e.state);
		// 交换面板
		this.remove(b);
		this.add(a);
		state = e.state;
		/**
		 * 状态切换后刷新（暂定）
		 */
		this.refresh();
		/**
		 * 测试searchButton的右键事件
		 */
	}

	@SubscribeEvent
	public void onChatOperator(EventChatOperation e) {
		if (e.optype.equals(EventChatOperation.FIXEDCHAT))
			memberField.setFixed(e.username);
		else if (e.optype.equals(EventChatOperation.DELETECHAT))
			memberField.deductMember(UOnline.getInstance().getUser(e.username));
		else if (e.optype.equals(EventChatOperation.CANELFIXEDCHAT))
			memberField.canelFixed();
		else if (e.optype.equals(EventChatOperation.ADDCHAT)) {
			/** 此处需要添加对于是否处于好友列表的判断 */
			if (e.recvpanel.equals(ListScrollPanel.FRIENDPANEL)) {
				memberField.addNewMember(UOnline.getInstance().getUser(e.username),1);
				memberField.setTop(e.username, 1);
			}
		}
		this.refresh();
	}

	/** 好友操作事件的响应 */
	@SubscribeEvent
	public void onFriendOperation(EventFriendOperation e) {
		boolean have = false;
		switch (e.type) {
		case EventFriendOperation.ADDFRIEND: {
			for (UserClient tmp : ul)
				if (tmp.userName.equals(e.username)) {
					have = true;
					Logger.log.warn("该好友已经是你的好友");
					break;
				} else
					have = false;
			if (!have) {
				ul.add(new UserClient(e.username));
				EventsBridge.frontendEventHandle.post(new EventChatOperation(e.username, EventChatOperation.ADDCHAT));
			}

			break;
		}
		case EventFriendOperation.DELETEFRIEND: {
			for (UserClient tmp : ul)
				if (tmp.userName.equals(e.username)) {
					ul.remove(tmp);
					have = true;
					break;
				} else
					have = false;
			if (!have) {
				Logger.log.impart("并无此好友，删除失败");
			}
			// 测试输出ul
			System.out.print(" 好友列表  ：");
			for (UserClient tmp : ul) {
				System.out.print("  " + tmp.userName);
			}
			System.out.println();
		}
		}
	}

	@SubscribeEvent
	public void onSearchRequest(EventSearchRequest e) {
		boolean remoteSearch;
		if (state == EventIPC.FRIENDS)
			remoteSearch = false;
		else {
			remoteSearch = true;
			for (int i = 0; i < e.name.size(); i++)
				searchMemberField.addNewMember(e.name.get(i), remoteSearch);

		}
		System.out.println(searchMemberField.getComponentCount());
		this.refresh();
	}

	/**
	 * 发送搜索内容到服务器 (这个事件处理可以把代码放到别的位置)
	 */
	@SubscribeEvent
	public void onSearch(client.event.EventSearch e) {
		UserClient.sendToServer(new MUSSearch(e.search));
	}

	@SubscribeEvent
	public void onULChange(client.event.EventULChange e) {
		memberField.deleteAllMember();
		for (EventULChange.ChangeInfo info : e.infos) {
			if ((info.flags & EventULChange.ADD) != 0) {
				addMember(info.user);
			} else if ((info.flags & EventULChange.REMOVE) != 0) {
				removeMember(info.user);
			}

		}
		this.refresh();

	}

	public void setUserName(String name) {
		userField.setName(name);
	}

	/**
	 * 状态转换时，原面板执行操作
	 * 
	 * @param state_b 原面板状态
	 */
	public void BPanelChange(int state_b) {
		switch (state_b) {
		case EventIPC.FRIENDS:
			break;
		case EventIPC.SEARCH:
			break;
		}
	}

	/**
	 * 状态转换时，新面板执行操作
	 * 
	 * @param state_a 原面板状态
	 */
	public void APanelChange(int state_a) {
		switch (state_a) {
		case EventIPC.FRIENDS:
			memberField.ChatStateClear();
			break;
		case EventIPC.SEARCH:
			searchMemberField.deleteAllMember();
//				searchField.searchInit();
			Logger.log.warn("进入搜索盘后焦点失去，无法获得焦点");
			break;
		}
	}

	/**
	 * 将转换的状态与面板对应
	 * 
	 * @param state 状态
	 */
	public Component stateToPanel(int state) {
		switch (state) {
		case EventIPC.FRIENDS:
			return memberField;
		case EventIPC.SEARCH:
			return searchMemberField;
		default:
			return memberField;
		}

	}

	/* 暂时作为测试 */
	public void addMembers(String[] members) {
		for (int i = 0; i < members.length; i++) {
			this.addMember(UOnline.getInstance().getUser(members[i]));
		}

	}

	public void addMember(User user) {
		memberField.addNewMember(user);
	}

	public void removeMember(String[] members) {
		for (String i : members)
			this.removeMember(UOnline.getInstance().getUser(i));
	}

	public void removeMember(User user) {
		memberField.deductMember(user);
	}

	public void setMemberTop(String member) {
		memberField.setTop(member);
	}

	public void setUserList(ArrayList<User> ul) {
		// 需要修改
		for (int i = 0; i < ul.size(); i++) {
			this.addMember(ul.get(i));
		}
	}
}
