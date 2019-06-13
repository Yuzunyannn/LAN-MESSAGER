package client.frame.utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import client.frame.MainFrame;
import client.frame.Theme;
import core.Core;
import log.Logger;
import nbt.NBTTagCompound;
import story.ITickable;

/** 界面右边的区域 聊天区域 操作区域 */
public class UtilityPanel extends JPanel implements ITickable {

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
		blankPanel.setBackground(Theme.COLOR4);
		// 当前面板
		currPanel = blankPanel;
		currPanel.setLocation(0, 0);
		this.add(currPanel);
		// 加入tick
		Core.task(this);
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
		
		//临时调试
		if (tick % 100 == 0) {
			if (panelInfo.equals("Ussj"))
				toChat("lly");
			else {
				toChat("ssj");
				System.out.println("fff");
			}
		}
		return ITickable.SUCCESS;
	}

	// 切换聊天界面
	public void toChat(String username) {
		String newInfo = "U" + username;
		if (newInfo.equals(panelInfo))
			return;
		PanelInfo info;
		if (panels.containsKey(newInfo)) {
			info = panels.get(newInfo);
		} else {
			info = new PanelInfo("U", username, new ChatPanel());
			panels.put(info.info, info);
		}
		if (!info.canUse()) {
			info.reborn();
		}

		//临时调试
		if (panels.containsKey(panelInfo)) {
			PanelInfo oldInfo = panels.get(panelInfo);
			oldInfo.release();
		}

		panelInfo = newInfo;
		currPanel = info.panel;
		info.tick = this.tick;
		UtilityPanel.this.remove(0);
		UtilityPanel.this.add(currPanel);
		UtilityPanel.this.revalidate();
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

}
