package client.frame.utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import client.frame.Theme;
import core.Core;
import nbt.INBTSerializable;
import nbt.NBTTagCompound;
import transfer.EventFile;

public class ChatDialogPanel extends JScrollPane implements INBTSerializable<NBTTagCompound> {
	private static final long serialVersionUID = 1L;
	private ChatBubblePanel chatBubble;
	private JPanel panel;
	private JScrollBar scrollBarV = this.getVerticalScrollBar();
	private JScrollBar scrollBarH = this.getHorizontalScrollBar();
	private int height = 0;
	public String lastTime;
	public boolean firstTime = false;
	int countDialogNum = 0;

	private LayoutManager chatDialogLayout = new LayoutManager() {

		@Override
		public void removeLayoutComponent(Component comp) {
			// TODO Auto-generated method stub

		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void layoutContainer(Container parent) {
			// TODO Auto-generated method stub
			Component[] cons = parent.getComponents();
			parent.setSize(parent.getParent().getWidth(), parent.getHeight());
			scrollBarH.setVisible(false);
			for (int i = 0; i < cons.length; i++) {
				if (cons[i] instanceof ChatBubblePanel) {
					cons[i].setSize(parent.getWidth(), cons[i].getHeight());
					//bubbleNum++;
				}
			}
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
			// TODO Auto-generated method stub

		}
	};

	public ChatDialogPanel() {
		super(new JPanel());
		panel = (JPanel) ((JViewport) this.getComponent(0)).getComponent(0);
		panel.setSize(this.getWidth() - this.scrollBarV.getWidth(), this.getHeight());
		panel.setLayout(this.chatDialogLayout);
		panel.setVisible(true);
		scrollBarV.setUI(new client.frame.ui.ScrollBarUI());
		this.setVisible(true);
		// 开始区域
		this.addBubble(false, "", "", BubbleType.LINE, "", "");
		// 数据添加可能是在调用setValue之后发生，所以此处引入runnable
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				scrollBarV.setValue(scrollBarV.getMaximum());
			}
		});
		this.setBackground(Theme.COLOR2);
		this.panel.setOpaque(true);
		this.panel.setBackground(Theme.COLOR2);
		this.setBorder(BorderFactory.createLineBorder(Theme.COLOR6, 1));
		this.panel.setBorder(null);
	}

	/** 更新此时下载文件的Bubble进度UI */
	public void updateFileProgress(String ID, EventFile e) {
		Component[] coms = this.panel.getComponents();
		for (int i = 0; i < coms.length; i++) {
			if (coms[i] instanceof ChatBubblePanel) {
				String str = ((ChatBubblePanel) coms[i]).getCBPID();
				if (str.indexOf(ID) != -1) {
					((FileBubble) ((ChatBubblePanel) coms[i]).getDialog()).toggleProgressBar(e);
				}
			}
		}
	}

	/** 添加一个对话气泡 */
	public void addBubble(boolean isMySelf, String info, String name, BubbleType type, String time, String ID) {
		// TODO Auto-generated method stub
		if (type == BubbleType.NULL) {
			System.out.println("添加气泡失败！类型为NULL");
			return;
		}
		this.countDialogNum++;
		String BCPID = "";
		if (ID.equals("")) {
			BCPID = String.valueOf(this.countDialogNum) + "_";
		} else {
			BCPID = String.valueOf(this.countDialogNum) + "_" + ID;
		}
		this.lastTime = time;
		chatBubble = new ChatBubblePanel(isMySelf, info, name, type, time, BCPID);
		int addHeight = chatBubble.getHeight();
		// testArea
//		if (isMySelf) {
//			chatBubble.setBorder(BorderFactory.createLineBorder(Theme.COLOR6, 3));
//		} else {      
//			chatBubble.setBorder(BorderFactory.createLineBorder(Theme.COLOR4, 3));
//		}
		 chatBubble.setBounds(0, this.panel.getHeight(), this.panel.getWidth(),
		 addHeight);
		
		chatBubble.setBounds(0, this.height, this.panel.getWidth(), addHeight);
		this.height = this.height + addHeight;
		// panel.setSize(panel.getWidth(), panel.getHeight() + chatBubble.getHeight());
		panel.add(chatBubble);
		if (this.height > panel.getHeight()) {
			panel.setPreferredSize(
					new Dimension(panel.getWidth() - this.scrollBarV.getWidth(), panel.getHeight() + addHeight));
			panel.setSize(panel.getWidth(), panel.getHeight() + addHeight);
		}
		
		
		Core.task(new Runnable() {
			public void run() {
				scrollBarV.setValue(scrollBarV.getMaximum());
				scrollBarV.revalidate();
				scrollBarV.repaint();
			}
		}, 50);
		panel.revalidate();
		panel.repaint();
		this.revalidate();
		this.repaint();
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
		nbt.setBoolean("firstTime", this.firstTime);
		nbt.setString("lastTime", this.lastTime);
		nbt.setInteger("height", this.height);
		// System.out.println(this.getVerticalScrollBar().getValue());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.firstTime = nbt.getBoolean("firstTime");
		this.lastTime = nbt.getString("lastTime");
		this.height = nbt.getInteger("height");
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
			ChatBubblePanel bubble = new ChatBubblePanel(true, "", "", BubbleType.NULL, "", "");
			bubble.deserializeNBT(upperNBT);
			this.panel.add(bubble);
		}
		Core.task(new Runnable() {
			public void run() {
				scrollBarV.setValue(nbt.getInteger("ScrollValue"));
			}
		}, 20);
		this.revalidate();
	}

	public void displayRead() {
		Component[] cons = panel.getComponents();
		for (Component component : cons) {
			if (component instanceof ChatBubblePanel) {
				((ChatBubblePanel)component).setRead();
			}
		}
	}

}
