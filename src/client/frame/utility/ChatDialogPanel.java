package client.frame.utility;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import client.frame.Theme;
import core.Core;
import event.SubscribeEvent;
import javafx.scene.layout.BorderStroke;
import nbt.INBTSerializable;
import nbt.NBTTagCompound;
import user.User;

public class ChatDialogPanel extends JScrollPane implements INBTSerializable<NBTTagCompound> {
	private static final long serialVersionUID = 1L;
	private ChatBubblePanel chatBubble;
	private JPanel panel;
	private User chatToUser;
	private JScrollBar scrollBar = this.getVerticalScrollBar();

	public ChatDialogPanel(User chatToUser) {
		super(new JPanel());
		this.chatToUser = chatToUser;
		panel = (JPanel) ((JViewport) this.getComponent(0)).getComponent(0);
		// panel.setLayout(new GridLayout(0, 1));
		System.out.println("1/:" + this.getWidth());
		System.out.println("2/:" + this.panel.getWidth());
		panel.setSize(this.getWidth() - this.scrollBar.getWidth(), this.getHeight());
		System.out.println("3/:" + this.panel.getWidth());
		panel.setLayout(null);
		panel.setVisible(true);
		scrollBar.setUI(new client.frame.ui.ScrollBarUI());
		this.setVisible(true);
		// 开始区域
//		panel.add(                                                                                                                                                                                                                                                                                                                                                                                                                                                                          new ChatBubblePanel(true, "", "", Type.NULL, ""));
//		panel.add(new ChatBubblePanel(true, "", "", Type.NULL, ""));
//		panel.add(new ChatBubblePanel(true, "", "", Type.NULL, ""));
//		panel.add(new ChatBubblePanel(true, "", "", Type.NULL, ""));
//		panel.add(new ChatBubblePanel(true, "", "", Type.NULL, ""));
//		panel.add(new ChatBubblePanel(true, "", "", Type.NULL, ""));
//		panel.add(new ChatBubblePanel(true, "", "", Type.NULL, ""));
//		panel.add(new ChatBubblePanel(true, "", "", Type.NULL, ""));
		this.addBubble(true, "", "", Type.LINE, "");
		// 数据添加可能是在调用setValue之后发生，所以此处引入runnable
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
		this.panel.setOpaque(true);
		this.panel.setBackground(Theme.COLOR1);
	}

	/** 添加一个对话气泡 */
	public void addBubble(boolean isMySelf, String info, String name, Type type, String time) {
		// TODO Auto-generated method stub
		if (type == Type.NULL) {
			System.out.println("添加气泡失败！类型为NULL");
			return;
		}
		// panel.setSize(panel.getWidth(), panel.getHeight() + 20);
		chatBubble = new ChatBubblePanel(isMySelf, info, name, type, time);
		int addHeight = 0;
		switch (type) {
		case WORD:
			addHeight = (info.length() / 50) * 30 + 60;
			break;
		case FILE:
			addHeight = 200;
			break;
		case EXTENSION:
			addHeight = 200;
			break;
		case LINE:
			addHeight = 40;
			break;
		case TIME:
			addHeight = 40;
			break;
		default:
			break;
		}
//		if (isMySelf) {
//			chatBubble.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
//		} else {
//			chatBubble.setBorder(BorderFactory.createLineBorder(Color.red, 3));
//		}

		chatBubble.setBounds(0, this.panel.getHeight(), this.panel.getWidth(), addHeight);
		// panel.setSize(panel.getWidth(), panel.getHeight() + chatBubble.getHeight());
		panel.setPreferredSize(new Dimension(panel.getWidth() - this.scrollBar.getWidth(), panel.getHeight() + addHeight));
		panel.setSize(panel.getWidth(), panel.getHeight() + addHeight);
		panel.add(chatBubble);
		panel.revalidate();
		Core.task(new Runnable() {
			public void run() {
				scrollBar.setValue(scrollBar.getMaximum());
			}
		}, 50);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		Component[] com = this.panel.getComponents();
		Integer comNum = 0;
		for (int j = 0; j < com.length; j++) {
			if (com[j] instanceof ChatBubblePanel) {
				NBTTagCompound tmpNBT = ((ChatBubblePanel) com[j]).serializeNBT();
				nbt.setTag(comNum.toString(), tmpNBT);
				comNum++;
			}
		}
		nbt.setInteger("Count", comNum);
		nbt.setInteger("ScrollValue", this.getVerticalScrollBar().getValue());
		// System.out.println(this.getVerticalScrollBar().getValue());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		int count = nbt.getInteger("Count");
		if (count == 0) {
			// System.out.println("组件数量为0");
			return;
		}
		Component[] com = this.panel.getComponents();
		int length = com.length;
		for (int i = length - 1; i >= 0; i--) {
			if (com[i] instanceof ChatBubblePanel) {
				this.panel.remove(i);
			}
		}
		for (Integer i = 0; i < count; i++) {
			NBTTagCompound upperNBT = (NBTTagCompound) nbt.getTag(i.toString());
			ChatBubblePanel bubble = new ChatBubblePanel(true, "", "", Type.NULL, "");
			bubble.deserializeNBT(upperNBT);
			this.panel.add(bubble);
		}
		Core.task(new Runnable() {
			public void run() {
				scrollBar.setValue(nbt.getInteger("ScrollValue"));
			}
		}, 20);
		this.revalidate();
	}

	@SubscribeEvent
	public void selectDownload(client.event.EventSelectDownload e) {

	}

}
