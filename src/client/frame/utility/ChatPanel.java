package client.frame.utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

import client.event.EventSendInputWords;
import client.event.EventsBridge;
import client.frame.Theme;
import client.user.UserClient;
import client.word.Word;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import transfer.EventFileRecv;
import transfer.EventFileSend;
import user.UOnline;
import user.User;

public class ChatPanel extends JPanelUtility {
	private static final long serialVersionUID = 1L;

	/** 输入区域的大小 */
	private int inputRegionHeight = 225;

	/** 聊天对象信息显示区域大小 */
	private int chatInfoRegionHeight = 80;

	/** 聊天区域自定义布局 */
	private LayoutManager layout = new LayoutManager() {

		@Override
		public void addLayoutComponent(String arg0, Component arg1) {
		}

		@Override
		public void layoutContainer(Container parent) {
			Component[] cons = parent.getComponents();
			Component input = cons[2];
			int height = parent.getHeight();
			int width = parent.getWidth();
			if (inputRegionHeight > height / 2)
				inputRegionHeight = height / 2;
			input.setLocation(0, height - inputRegionHeight);
			input.setSize(width, inputRegionHeight);

			Component dialog = cons[1];
			dialog.setLocation(0, chatInfoRegionHeight + 1);
			dialog.setSize(width, height - inputRegionHeight - chatInfoRegionHeight - 1);

			Component chatInfo = cons[0];
			chatInfo.setLocation(0, 0);
			chatInfo.setSize(width, chatInfoRegionHeight);
		}

		@Override
		public Dimension minimumLayoutSize(Container arg0) {
			return new Dimension(arg0.getWidth(), 480 / 3);
		}

		@Override
		public Dimension preferredLayoutSize(Container arg0) {
			return new Dimension(arg0.getWidth(), 720 / 4);
		}

		@Override
		public void removeLayoutComponent(Component arg0) {

		}

	};

	private final Cursor cursorResize;
	private final Cursor cursorNormal;

	/** 拖动聊天输入区域 */
	private MouseAdapter mouselistener = new MouseAdapter() {
		int lastY;

		@Override
		public void mousePressed(MouseEvent e) {
			this.lastY = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			ChatPanel.this.setCursor(ChatPanel.this.cursorNormal);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			ChatPanel cathPanel = ChatPanel.this;
			Cursor cursor = cathPanel.getCursor();
			// 获取鼠标的Y
			int mouseY = e.getY();
			int targetY = cathPanel.getHeight() - cathPanel.inputRegionHeight;
			// 检测Y是否处于拖动位置
			if (mouseY == targetY || mouseY == targetY + 1) {
				if (cursor != cathPanel.cursorResize)
					cathPanel.setCursor(cathPanel.cursorResize);
			} else {
				if (cursor != cathPanel.cursorNormal)
					cathPanel.setCursor(cathPanel.cursorNormal);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			ChatPanel cathPanel = ChatPanel.this;
			Cursor cursor = cathPanel.getCursor();
			if (cursor == cursorNormal)
				return;
			int yOff = lastY - e.getY();
			lastY = e.getY();
			// 测试鼠标移动
			if (yOff < 0) {
				if (lastY < cathPanel.getHeight() - cathPanel.inputRegionHeight)
					return;
			} else {
				if (lastY > cathPanel.getHeight() - cathPanel.inputRegionHeight)
					return;
			}
			// 测试最小状态
			Dimension size = cathPanel.inputPanel.getMinimumSize();
			cathPanel.inputRegionHeight += yOff;
			if (size.height > cathPanel.inputRegionHeight)
				cathPanel.inputRegionHeight = size.height;
			// 重新排版
			cathPanel.doLayout();
			inputPanel.doLayout();
			chatDialogPanel.doLayout();
		}
	};
	/** 用户 */
	private User chatTo;
	private ChatInfoPanel chatInfoPanel;
	private ChatDialogPanel chatDialogPanel = new ChatDialogPanel();
	private ChatInputPanel inputPanel = new ChatInputPanel();

	public ChatPanel() {
		this("");
	}

	public ChatPanel(String chatToUsername) {
		// 默认颜色
		this.setBackground(Theme.COLOR1);
		// 设置默认布局
		this.setLayout(layout);
		// 添加信息、输入和对话面板
		chatInfoPanel = new ChatInfoPanel(chatToUsername);
		this.add(chatInfoPanel);
		this.add(chatDialogPanel);
		this.add(inputPanel);
		// 添加鼠标监听者
		this.addMouseListener(mouselistener);
		this.addMouseMotionListener(mouselistener);
		// 建立指针
		this.cursorResize = new Cursor(Cursor.N_RESIZE_CURSOR);
		this.cursorNormal = this.getCursor();
		// 获取user
		chatTo = UOnline.getInstance().getUser(chatToUsername);
	}

	/** 时间差大于5分钟则显示时间线 */
	public void timeLapse(String nowDate, String lastDate) {
		if (chatDialogPanel.firstTime) {
			int now = Integer.parseInt(nowDate.substring(14, 15));
			int last = Integer.parseInt(lastDate.substring(14, 15));
			if ((now - last) >= 5) {
				chatDialogPanel.addBubble(false, "", "", BubbleType.TIME, nowDate.toString(), "");
			}
		} else {
			chatDialogPanel.firstTime = true;
			chatDialogPanel.addBubble(false, "", "", BubbleType.TIME, nowDate.toString(), "");
		}
	}

	/** 发送图片的UI更新 */
	public void onSendPics(String name, BubbleType type) {
		Date date = new Date();
		chatDialogPanel.addBubble(true, "", "", BubbleType.TIME, date.toString(), "");
		chatDialogPanel.addBubble(true, name, UserClient.getClientUsername(), type, date.toString(), "");
		this.revalidate();
	}

	/** 发送文件UI更新 */
	public void onSendFile(String name, String storyID) {
		Date date = new Date();

		chatDialogPanel.addBubble(true, "", "", BubbleType.TIME, date.toString(), "");
		chatDialogPanel.addBubble(true, name, UserClient.getClientUsername(), BubbleType.FILE, date.toString(),
				storyID);
		this.revalidate();
	}

	/** 发送文件进度UI更新 */
	public void onSendFileProgress(String storyID, EventFileSend.Start e) {
		chatDialogPanel.updateFileProgress(storyID, e);
	}

	/** 接收文件UI更新 */
	public void onRevFile(String name, String storyID) {
		Date date = new Date();
		chatDialogPanel.addBubble(false, "", "", BubbleType.TIME, date.toString(), "");
		chatDialogPanel.addBubble(false, name, UserClient.getClientUsername(), BubbleType.FILE, date.toString(),
				storyID);
		this.revalidate();
	}

	/** 接收文件进度UI更新 */
	public void onRevFileProgress(String storyID, EventFileRecv.Start e) {
		chatDialogPanel.updateFileProgress(storyID, e);
	}

	/** 检查发送和接收的消息类型 */
	private BubbleType checkType(int id) {
		BubbleType type = BubbleType.NULL;
		switch (id) {
		case Word.FILE:
			type = BubbleType.FILE;
			break;
		case Word.STRING:
			type = BubbleType.WORD;
			break;
		default:
			break;
		}
		return type;
	}

	/** 当点击发送时候调用 */
	public void onSendMsg(List<Word> words) {
		for (Word w : words) {
			BubbleType type = checkType(w.id);
			if (type == BubbleType.FILE) {
				continue;
			}
			Date date = new Date();
			timeLapse(date.toString(), chatDialogPanel.lastTime);
			chatDialogPanel.addBubble(true, w.toString(), UserClient.getClientUsername(), type, date.toString(), "");
		}
		this.revalidate();
		EventsBridge.frontendEventHandle.post(new EventSendInputWords(words, chatTo));
	}

	/** 当点击收到消息的时候调用 */
	public void onRecvMsg(Word word) {
		BubbleType type = checkType(word.id);
		Date date = new Date();
		timeLapse(date.toString(), chatDialogPanel.lastTime);
		chatDialogPanel.addBubble(false, word.toString(), chatTo.getUserName(), type, date.toString(), "");

	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("chat", chatDialogPanel);
		nbt.setTag("input", inputPanel);
		nbt.setString("user", chatTo.getUserName());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		chatDialogPanel.deserializeNBT((NBTTagCompound) nbt.getTag("chat"));
		inputPanel.deserializeNBT((NBTTagList) nbt.getTag("input"));
		chatTo = UOnline.getInstance().getUser(nbt.getString("user"));
		chatInfoPanel = new ChatInfoPanel(chatTo.getUserName());
	}

}
