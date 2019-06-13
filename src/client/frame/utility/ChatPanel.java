package client.frame.utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;

import client.event.EventRecv;
import client.event.EventRecv.EventRecvString;
import client.event.EventSendInputWords;
import client.event.EventsBridge;
import client.frame.Theme;
import client.word.Word;

import event.SubscribeEvent;
import nbt.INBTSerializable;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import nbt.NBTTagString;
import user.UOnline;



public class ChatPanel extends JPanelUtility {
	private static final long serialVersionUID = 1L;

	/** 输入区域的大小 */
	private int inputRegionHeight = 225;

	/** 聊天区域自定义布局 */
	private LayoutManager layout = new LayoutManager() {

		@Override
		public void addLayoutComponent(String arg0, Component arg1) {
		}

		@Override
		public void layoutContainer(Container parent) {
			Component[] cons = parent.getComponents();
			Component input = cons[1];
			int height = parent.getHeight();
			int width = parent.getWidth();
			if (inputRegionHeight > height / 2)
				inputRegionHeight = height / 2;
			input.setLocation(0, height - inputRegionHeight);
			input.setSize(width, inputRegionHeight);

			Component dialog = cons[0];
			dialog.setLocation(0, 0);
			dialog.setSize(width, height - inputRegionHeight - 1);
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
	/** 对话面板 */
	private ChatDialogPanel chatDialogPanel = new ChatDialogPanel();
	private ChatInputPanel inputPanel = new ChatInputPanel();

	public ChatPanel() {
		// 默认颜色
		this.setBackground(Theme.COLOR0);
		// 设置默认布局
		this.setLayout(layout);
		// 添加输入和对话面板
		this.add(chatDialogPanel);
		this.add(inputPanel);
		// 添加鼠标监听者
		this.addMouseListener(mouselistener);
		this.addMouseMotionListener(mouselistener);
		// 建立指针
		this.cursorResize = new Cursor(Cursor.N_RESIZE_CURSOR);
		this.cursorNormal = this.getCursor();
	}

	/** 当点击发送时候调用 */
	public void onSendMsg(List<Word> words) {
		for (Word w : words)
			chatDialogPanel.addBubble(true, w.toString(), "ssj");
		//chatDialogPanel.serializeNBT();
		EventsBridge.frontendEventHandle.post(new EventSendInputWords(words, UOnline.getInstance().getUser("guest")));
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		
	 nbt.setTag("chat", chatDialogPanel);
		nbt.setTag("input", inputPanel);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		 chatDialogPanel.deserializeNBT((NBTTagCompound) nbt.getTag("chat"));
		inputPanel.deserializeNBT((NBTTagList) nbt.getTag("input"));
	}

}
