package client.frame.utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import client.event.EventSendInputWords;
import client.event.EventsBridge;
import client.frame.Theme;
import client.record.Record;
import client.record.RecordManagement;
import client.record.RecordValue;
import client.user.UserClient;
import client.word.Word;
import client.word.WordString;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import transfer.EventFileRecv;
import transfer.EventFileSend;
import user.UOnline;
import user.User;

public class ChatPanel extends JPanelUtility {
	private static final long serialVersionUID = 1L;

	private static Pattern regularRule = Pattern.compile("\\{/0x(\\d+)/\\}");

	/** 输入区域的大小 */
	private int inputRegionHeight = 170;

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

	public User getChatTo() {
		return chatTo;
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

	@Override
	public void firstCreate() {
		Record rec = RecordManagement.getRecord(chatTo);
		List<RecordValue> rs = rec.getRecordValues(rec.getLastDayOff());
		int n = Math.min(rs.size(), 5);
		LinkedList<RecordValue> show = new LinkedList<RecordValue>();
		for (int i = 0; i < n; i++) {
			show.add(rs.get(rs.size() - 1 - i));
		}
		while (!show.isEmpty()) {
			RecordValue v = show.getLast();
			show.removeLast();
			String str = v.word.toString();
			BubbleType type = BubbleType.WORD;
			if (v.word.toString().indexOf(Record.FILEMARK) == 0) {
				type = BubbleType.FILE;
				str = v.word.toString().substring(Record.FILEMARK.length(), v.word.toString().length());
			} else if (v.word.toString().indexOf(Record.MEMEMARK) == 0) {
				type = BubbleType.MEME;
				System.out.println(str);
				str = v.word.toString().substring(Record.MEMEMARK.length(), v.word.toString().length());
			} else if (v.word.toString().indexOf(Record.TIMEMARK) == 0) {
				type = BubbleType.TIME;
				str = v.word.toString().substring(Record.TIMEMARK.length(), v.word.toString().length());
				chatDialogPanel.addBubble(false, str, chatTo.getUserName(), type, str, "");
				continue;
			}
			if (v.whos.equals(chatTo)) {
				chatDialogPanel.addBubble(false, str, chatTo.getUserName(), type, v.word.getTime(), "");
			} else {
				chatDialogPanel.addBubble(true, str, UserClient.getClientUsername(), type, v.word.getTime(), "");
			}
		}

	}

	/** 时间差大于5分钟则显示时间线 */
	public void timeLapse(String nowDate, String lastDate) {
		Record rec = RecordManagement.getRecord(chatTo);
		Word w;
		if (chatDialogPanel.firstTime) {
			int now = Integer.parseInt(nowDate.substring(14, 15));
			int last = Integer.parseInt(lastDate.substring(14, 15));
			if ((now - last) >= 5) {
				chatDialogPanel.addBubble(false, "", "", BubbleType.TIME, nowDate.toString(), "");
				w = new WordString(Record.TIMEMARK + nowDate);
				rec.addNew(w, chatTo);
			}
		} else {
			chatDialogPanel.firstTime = true;
			chatDialogPanel.addBubble(false, "", "", BubbleType.TIME, nowDate.toString(), "");
			w = new WordString(Record.TIMEMARK + nowDate);
			rec.addNew(w, chatTo);
		}
	}

	/** 发送图片的UI更新 */
	public void onSendPics(String name, BubbleType type) {
		Date date = new Date();
		Record rec = RecordManagement.getRecord(chatTo);
		Word w = new WordString(Record.TIMEMARK + date.toString());
		rec.addNew(w, UserClient.getClientUser());
		w = new WordString(Record.MEMEMARK + name);
		rec.addNew(w, UserClient.getClientUser());
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
		Record rec = RecordManagement.getRecord(chatTo);
		Word w = new WordString(Record.TIMEMARK + date.toString());
		rec.addNew(w, chatTo);
		w = new WordString(Record.FILEMARK + storyID + Record.FILEEND + name);
		rec.addNew(w, chatTo);
	}

	/** 接收图片UI更新 */
	public void onRevPic(String name, String storyID) {
		Date date = new Date();
		chatDialogPanel.addBubble(false, "", "", BubbleType.TIME, date.toString(), "");
		chatDialogPanel.addBubble(false, name, UserClient.getClientUsername(), BubbleType.PICTURE, date.toString(),
				storyID);
		this.revalidate();
		Word w = new WordString(Record.MEMEMARK + name);
		Record rec = RecordManagement.getRecord(chatTo);
		rec.addNew(w, chatTo);
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
			if (type.equals(BubbleType.FILE) ) {
				if (chatTo.getUserName().indexOf("#G")==0) {
					JOptionPane.showMessageDialog(this, "群组不支持发文件");
					return;
				}
			}
			Record rec = RecordManagement.getRecord(chatTo);
			Date date = new Date();
			timeLapse(date.toString(), chatDialogPanel.lastTime);
			if (type.equals(BubbleType.FILE)) {
				System.out.println(chatTo.getSpecialUser().getId());
				System.out.println(chatTo.getUserName());
				w = new WordString(Record.FILEMARK + w.toString());
				rec.addNew(w, UserClient.getClientUser());
				continue;
			} else {
				rec.addNew(w, UserClient.getClientUser());
			}
			chatDialogPanel.addBubble(true, w.toString(), UserClient.getClientUsername(), type, date.toString(), "");
		}
		this.revalidate();
		EventsBridge.frontendEventHandle.post(new EventSendInputWords(words, chatTo));
	}

	/** 群发UI改变 */
	public void sendGroupMsg(String msg) {
		Date date = new Date();
		chatDialogPanel.addBubble(true, "", UserClient.getClientUsername(), BubbleType.TIME, date.toString(), "");
		chatDialogPanel.addBubble(true, msg, UserClient.getClientUsername(), BubbleType.WORD, date.toString(), "");
		Word w = new WordString(msg);
		Record rec = RecordManagement.getRecord(chatTo);
		rec.addNew(w, UserClient.getClientUser());
	}

	/** 当点击收到消息的时候调用 */
	public void onRecvMsg(User user, Word word) {
		Record rec = RecordManagement.getRecord(chatTo);
		BubbleType type = checkType(word.id);
		java.util.regex.Matcher m = regularRule.matcher(word.getValue());
		Date date = new Date();
		timeLapse(date.toString(), chatDialogPanel.lastTime);
		boolean found = false;
		Word tmp;
		while (m.find()) {
			found = true;
			String id = m.group(1);
			System.out.print(id);
			chatDialogPanel.addBubble(false, id + ".jpg", user.getUserName(), BubbleType.MEME, date.toString(), "");
			tmp = new WordString(Record.MEMEMARK + id);
			rec.addNew(tmp, chatTo);
		}
		if (found) {
			String[] texts = regularRule.split(word.getValue());
			if (texts != null) {
				for (int i = 0; i < texts.length; i++) {
					chatDialogPanel.addBubble(false, texts[i], user.getUserName(), type, date.toString(), "");
					tmp = new WordString(texts[i]);
					rec.addNew(tmp, chatTo);
				}
			}
		} else {
			chatDialogPanel.addBubble(false, word.toString(), user.getUserName(), type, date.toString(), "");
			rec.addNew(word, chatTo);
		}
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

	public void displayRead() {
		chatDialogPanel.displayRead();
	}

	public void addGroupMemeber(List<String> users) {
		chatInfoPanel.addGroupMemeber(users);
	}

}
