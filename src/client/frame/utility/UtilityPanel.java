package client.frame.utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import client.event.EventChatOperation;
import client.event.EventGroupSend;
import client.event.EventIsShowed;
import client.event.EventSearch;
import client.event.EventSendInputWords;
import client.event.EventShow;
import client.event.EventsBridge;
import client.frame.MainFrame;
import client.frame.Theme;
import client.word.Word;
import client.word.WordFile;
import client.word.WordString;
import core.Core;
import event.IEventBus;
import event.SubscribeEvent;
import log.Logger;
import nbt.NBTTagCompound;
import story.ITickable;
import transfer.EventFileRecv;
import transfer.EventFileSend;
import user.UOnline;
import user.User;
import user.UserSpecial;

/** 界面右边的区域 聊天区域 操作区域 */
public class UtilityPanel extends JPanel implements ITickable {

	/** 聊天工具框ID */
	public static final String TOOLID_CHATING = "chat";

	private static final long serialVersionUID = 1L;
	/** 功能区域自定义布局 */
	private LayoutManager layout = new LayoutManager() {

		@Override
		public void addLayoutComponent(String arg0, Component arg1) {
		}

		@Override
		public void layoutContainer(Container arg0) {
			Component major = arg0.getComponent(0);
			major.setSize(arg0.getWidth(), arg0.getHeight());
		}

		@Override
		public Dimension minimumLayoutSize(Container arg0) {
			return new Dimension(640 - MainFrame.INFO_RIGION_WIDTH, 480);
		}

		@Override
		public Dimension preferredLayoutSize(Container arg0) {
			return new Dimension(1280 - MainFrame.INFO_RIGION_WIDTH, 720);
		}

		@Override
		public void removeLayoutComponent(Component arg0) {

		}
	};
	/** 当前面板 */
	private JPanelUtility currPanel;
	/** 当前info */
	private String panelInfo = "";
	/** 空面板 */
	private JPanelUtility blankPanel = new JPanelUtilityBlank();
	/** 记录当前助于内存的面板 */
	private Map<String, PanelInfo> panels = new TreeMap<String, PanelInfo>();

	public UtilityPanel() {
		// 设置默认背景颜色
		this.setBackground(Theme.COLOR0);
		// 更换布局
		this.setLayout(layout);
		// 默认面板
		blankPanel.setBackground(Theme.COLOR2);
		// 当前面板
		currPanel = blankPanel;
		currPanel.setLocation(0, 0);
		this.add(currPanel);
		// 加入tick
		Core.task(this);
	}

	public void initEvent(IEventBus bus) {
		bus.register(this);
	}

	private long tick = 0;

	@Override
	public int update() {
		tick++;
		if (tick % 6000 == 0) {
			for (PanelInfo info : panels.values()) {
				if (this.tick - info.tick > 20 * 60 * 10) {
					info.release();
				}
			}
		}
		return ITickable.SUCCESS;
	}

	/** 获取聊天PaneLInfo */
	public PanelInfo getChatPanelInfo(String username) {
		String newInfo = UtilityPanel.TOOLID_CHATING + username;
		PanelInfo info;
		if (panels.containsKey(newInfo)) {
			info = panels.get(newInfo);
		} else {
			info = new PanelInfo(UtilityPanel.TOOLID_CHATING, username, new ChatPanel(username));
			panels.put(info.info, info);
		}
		return info;
	}

	public void toBlank() {
		panelInfo="";
		currPanel = blankPanel;
		UtilityPanel.this.remove(0);
		UtilityPanel.this.add(currPanel);
		UtilityPanel.this.revalidate();
		UtilityPanel.this.repaint();
	}
	
	/** 切换聊天界面 */
	public void toChat(String username) {
		String newInfo = UtilityPanel.TOOLID_CHATING + username;
		if (newInfo.equals(panelInfo))
			return;
		PanelInfo info = this.getChatPanelInfo(username);
		if (!info.canUse()) {
			info.reborn();
		}
		panelInfo = newInfo;
		currPanel = info.panel;
		
		info.tick = this.tick;
		UtilityPanel.this.remove(0);
		UtilityPanel.this.add(currPanel);
		UtilityPanel.this.revalidate();
		UtilityPanel.this.repaint();
	}

	// 记录驻于内存的面板
	private class PanelInfo {
		final String info;
		JPanelUtility panel = null;
		NBTTagCompound nbt = null;
		Class<? extends JPanelUtility> cls = null;
		// 负责记录上次始终tick，内存调度使用
		long tick = UtilityPanel.this.tick;

		public PanelInfo(String first, String secend, JPanelUtility panel) {
			this.info = first + secend;
			this.panel = panel;
			this.cls = panel.getClass();
			this.panel.firstCreate();
		}

		@Override
		public String toString() {
			return info;
		}

		@Override
		public int hashCode() {
			return info.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return info.equals(obj);
		}

		public boolean canUse() {
			return panel != null;
		}

		public void release() {
			if (panel == null)
				return;
			nbt = panel.serializeNBT();
			panel = null;
		}

		public void reborn() {
			this.nbt = this.nbt == null ? new NBTTagCompound() : this.nbt;
			try {
				panel = cls.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				panel = UtilityPanel.this.blankPanel;
				Logger.log.warn("在还原panel的时候，反射出现异常！", e);
			}
			panel.deserializeNBT(nbt);
		}

	}

//----------------------------------use----------------------------------
	/** 接受到消息 */
	public void recvString(User from, UserSpecial sp, String str) {
		PanelInfo info;
		if (sp == null)
			info = this.getChatPanelInfo(from.getUserName());
		else {
			info = this.getChatPanelInfo(sp.specialName);
		}
		if (!info.canUse()) {
			info.reborn();
		}
		info.tick = this.tick;
		((ChatPanel) info.panel).onRecvMsg(from, new WordString(str));
	}

	/** 发送消息给多个用户 */
	public static void sendWordToUsers(List<User> users, Word word) {
		if (word.id != Word.STRING) {
			Logger.log.warn("目前多人发消息只支持文字");
			return;
		}
		for (User user : users) {
			EventsBridge.sendString(word.getValue(), user);
		}
	}

// ----------------------------------event----------------------------------

	// 处理切换事件
	@SubscribeEvent
	public void changeChat(EventShow e) {
		if (e.toolId.equals(UtilityPanel.TOOLID_CHATING)) {
			this.toChat(e.id);
//判断是个人不是群组	
//			if(isPerson(e.id)) 
			{
				User u = UOnline.getInstance().getUser(e.id);
				EventsBridge.sendHasRead(u);
			}
		}
	}
	
	@SubscribeEvent
	public void changeBlankChatOnDelete(EventChatOperation e) {
		if (e.optype.equals(EventChatOperation.DELETECHAT)) {
			this.toBlank();
		}
	}
	
	@SubscribeEvent
	public void changeBlankChatOnSearch(EventSearch e) {
		this.toBlank();
	}
	
	@SubscribeEvent
	public void checkGroup(EventShow e) {
		if (e.id.indexOf("#G") == 0) {
			List<String> users = new ArrayList<String>();
			for (User user : e.userlist) {
				users.add(user.getUserName());
			}
			PanelInfo info = this.getChatPanelInfo(e.id);
			if (!info.canUse()) {
				info.reborn();
			}
			((ChatPanel) info.panel).addGroupMemeber(users);
		}
	}

	@SubscribeEvent
	public void recvString(client.event.EventRecv.EventRecvString e) {
		this.recvString(e.from, e.sp, e.str);
	}

	@SubscribeEvent
	public void sendWords(client.event.EventSendInputWords e) {
		for (Word w : e.words) {
			if (w.id == Word.FILE) {
				EventsBridge.sendFile(((WordFile) w).getFile(), e.toUser);
			} else {
				EventsBridge.sendString(w.getValue(), e.toUser);
			}
		}
	}

	@SubscribeEvent
	public void debug(client.event.EventDebugInfoOuting e) {
		e.debufInfos.add("UtilityPanel(" + tick + ")当前板子的id为:" + panelInfo);
		EventsBridge.frontendEventHandle.post(new EventIsShowed(UOnline.getInstance().getUser("debug")));
	}

	/** 已读标记 */
	@SubscribeEvent
	public void hasRead(client.event.EventIsShowed e) {
		PanelInfo info = this.getChatPanelInfo(e.getUser().getUserName());
		if (!info.canUse()) {
			info.reborn();
		}
		((ChatPanel) info.panel).displayRead();
	}

	/** 发送图片or表情 */
	@SubscribeEvent
	public void sendPicture(client.event.EventSendPicture e) {
		System.out.println("Pic event got!");
		if (e.type == BubbleType.MEME) {
			List<Word> words = new LinkedList<Word>();
			Word word = new WordString("{/0x" + e.picName.substring(0, e.picName.length() - 4) + "/}");
			System.out.println(word.getValue());
			words.add(word);
			User user = UOnline.getInstance().getUser(e.toUser);
			EventsBridge.frontendEventHandle.post(new EventSendInputWords(words, user));
		}
		((ChatPanel) this.currPanel).onSendPics(e.picName, e.type);

	}

	/** 文件发送UI更新 */
	@SubscribeEvent
	public void sendFile(EventFileSend.Start e) {
		System.out.println("sendFile Updating!");
		((ChatPanel) this.currPanel).onSendFile(e.getFileName(), e.getTempName());
		((ChatPanel) this.currPanel).onSendFileProgress(e.getTempName(), e);
	}

	/** 文件接收UI更新 */
	@SubscribeEvent
	public void revFile(EventFileRecv.Start e) {
		PanelInfo info = this.getChatPanelInfo(e.getFrom().getUserName());
		if (!info.canUse()) {
			info.reborn();
		}
		((ChatPanel) info.panel).onRevFile(e.getFileName(), e.getTempName());
		((ChatPanel) info.panel).onRevFileProgress(e.getTempName(), e);
	}

	/** 群发UI更新 */
	@SubscribeEvent
	public void sendGroupMsg(EventGroupSend e) {
		List<String> users = new ArrayList<String>();
		users = e.getUsers();

		for (String string : users) {
			PanelInfo info = this.getChatPanelInfo(string);
			if (!info.canUse()) {
				info.reborn();
			}
			((ChatPanel) info.panel).sendGroupMsg(e.getMsg());
		}
	}

	/** 切换到空面板 */
	@SubscribeEvent
	public void changeToVoid(client.event.EventIPC e) {
		if (e.state == client.event.EventIPC.SEARCH) {
			this.currPanel = null;
			this.revalidate();
			this.repaint();
		}
	}

}
