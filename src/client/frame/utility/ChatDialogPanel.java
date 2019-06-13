package client.frame.utility;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import core.Core;
import nbt.INBTSerializable;
import nbt.NBTTagCompound;

public class ChatDialogPanel extends JScrollPane implements INBTSerializable<NBTTagCompound> {
	private static final long serialVersionUID = 1L;
	private ChatBubblePanel chatBubble;
	private JPanel panel;
	public JScrollBar scrollBar = this.getVerticalScrollBar();
	private String user = "";

	public ChatDialogPanel() {
		super(new JPanel());
		panel = (JPanel) ((JViewport) this.getComponent(0)).getComponent(0);
		panel.setLayout(new GridLayout(0, 1));
		panel.setVisible(true);
		this.setVisible(true);

		// 测试区域
//		this.addBubble(true, "hhhhhhhh", "ssj");
//		this.addBubble(false, "yyyyyyy", "lyl");
//		this.addBubble(false, "yyyyyyy", "lyl");
//		this.addBubble(true, "zzzzzz", "ssj");
//		this.addBubble(false, "fffffffff", "lyl");
//		this.addBubble(true, "lllllllll", "ssj");
//		this.addBubble(false, "iiiiiiiiiiiiiiii", "lyl");
//		this.addBubble(true, "hhhhhhhh", "ssj");
//		this.addBubble(false, "yyyyyyy", "lyl");
//		this.addBubble(false, "yyyyyyy", "lyl");
//		this.addBubble(true, "zzzzzz", "ssj");
//		this.addBubble(false, "fffffffff", "lyl");
//		this.addBubble(true, "lllllllll", "ssj");
//		this.addBubble(false, "iiiiiiiiiiiiiiii", "lyl");
		// JScrollBar scrollBar = this.getVerticalScrollBar();
		// 数据添加可能是在调用setValue之后发生，所以此处引入runnable
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				scrollBar.setValue(scrollBar.getMaximum());
			}
		});
	}

	/** 添加一个对话气泡 */
	public void addBubble(boolean isMySelf, String info, String name) {
		// TODO Auto-generated method stub
		panel.setSize(panel.getWidth(), panel.getHeight() + 20);
		chatBubble = new ChatBubblePanel(isMySelf, info, name, Type.WORD);
		panel.add(chatBubble);
		Core.task(new Runnable() {
			public void run() {
				scrollBar.setValue(scrollBar.getMaximum());
			}
		}, 50);
		// scrollBar.setValue(scrollBar.getMaximum());
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		Component[] com = this.getComponents();
		ChatBubblePanel bubble = (ChatBubblePanel) com[0];
		int top = this.scrollBar.getValue() - this.getHeight();
		int bottom = this.scrollBar.getValue();
		int firstCom = (top / (bubble.getHeight())) - 1;
		int lastCom = (bottom / (bubble.getHeight())) - 1;
		Integer comNum = 0;
		for (int i = firstCom; i < lastCom; i++) {
			nbt.setTag(comNum.toString(), (ChatBubblePanel) com[i]);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
	}

}
