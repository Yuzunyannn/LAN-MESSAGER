package client.frame.utility;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import client.frame.MainFrame;
import client.frame.Theme;

/** 界面右边的区域 聊天区域 操作区域 */
public class UtilityPanel extends JPanel {

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
	/** 聊天面板 */
	private ChatPanel chatPanel = new ChatPanel();


	public UtilityPanel() {
		// 设置默认背景颜色
		this.setBackground(Theme.COLOR0);
		// 更换布局
		this.setLayout(layout);
		// 添加默认聊天（临时！）
		chatPanel.setLocation(0, 0);
		this.add(chatPanel);
		

	}

}
