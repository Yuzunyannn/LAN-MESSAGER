package client.frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import client.event.EventNeedSave;
import client.event.EventsBridge;
import client.frame.info.InfoPanel;
import client.frame.utility.UtilityPanel;
import event.IEventBus;
import user.User;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	/** 窗体标题 */
	public static final String FRAME_TITLE = "Chatroom";
	/** 左信息区域宽度 */
	public static final int INFO_RIGION_WIDTH = 300;
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
			return new Dimension(1000, 628);
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
		// 居中显示
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		// 窗体关闭
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				EventsBridge.dealEventHandle.post(new EventNeedSave());
			}
		});
		// 得到窗体的宽、高
		int windowsWidth = this.getWidth();
		int windowsHeight = this.getHeight();
		// System.out.println(windowsWidth+","+windowsHeight);
		this.setBounds((width - windowsWidth) / 2, (height - windowsHeight) / 2, windowsWidth, windowsHeight);
	}

	public void initEvent(IEventBus bus) {
		infoPanel.initEvent(bus);
		utilityPanel.initEvent(bus);

	}

	public InfoPanel getInfoPanel() {
		return infoPanel;
	}

	public void setUserName(String name) {
		infoPanel.setUserName(name);
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

	/** 设置用户列表 */
	public void setUserList(ArrayList<User> ul) {
		infoPanel.setUserList(ul);
	}

}
