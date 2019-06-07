package client.frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JFrame;

import client.frame.info.InfoPanel;
import client.frame.utility.UtilityPanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	/** 窗体标题 */
	public static final String FRAME_TITLE = "Chatroom";
	/** 左信息区域宽度 */
	public static final int INFO_RIGION_WIDTH = 275;
	/** 主窗体布局 */
	private LayoutManager layout = new LayoutManager() {

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		/** 排版 */
		@Override
		public void layoutContainer(Container parent) {
			Component[] cons = parent.getComponents();
			Component info = cons[0];
			Component utility = cons[1];
			int height = parent.getHeight();
			int width = parent.getWidth();
			info.setSize(INFO_RIGION_WIDTH, height);
			utility.setSize(width - INFO_RIGION_WIDTH, height);
		}

		@Override
		public Dimension minimumLayoutSize(Container arg0) {
			return new Dimension(640, 480);
		}

		@Override
		public Dimension preferredLayoutSize(Container arg0) {
			return new Dimension(1280, 720);
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

	};

	/** 左侧信息区域 */
	private InfoPanel infoPanel = new InfoPanel();
	/** 右侧功能区域 */
	private UtilityPanel utilityPanel = new UtilityPanel();

	/** 构造函数 */
	public MainFrame() {
		// 设置标题
		this.setTitle(FRAME_TITLE);
		this.setIconImage(null);
		// 设置关闭操作
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置布局
		Container content = this.getContentPane();
		content.setLayout(layout);
		content.add(infoPanel);
		infoPanel.setLocation(0, 0);
		content.add(utilityPanel);
		utilityPanel.setLocation(INFO_RIGION_WIDTH, 0);
		// 设置最小窗体大小
		this.setMinimumSize(layout.minimumLayoutSize(this));
		// 设置窗体大小和位置
		this.fixed();
	}

	/** 初始化窗体大小和位置 */
	public void fixed() {
		this.pack();
	}

	/** 最大化 */
	public void maximize() {
		if (MainFrame.this.getExtendedState() == JFrame.MAXIMIZED_BOTH)
			return;
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	/** 还原 */
	public void normal() {
		this.setExtendedState(JFrame.NORMAL);
	}

}
