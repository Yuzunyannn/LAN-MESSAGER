package client.frame.selection;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SelectFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SelectPane selectPane;
	
	static private List<String> selectedList = new ArrayList<String>();
	static private boolean choosable = false;
	public static final int CHOOSE = 0;
	public static final int VIEW = 1;
	
	private LayoutManager layout = new LayoutManager() {

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
			int height = parent.getHeight();
			int width = parent.getWidth();

			Component selectList = cons[0];
			selectList.setLocation(0, 0);
			selectList.setSize(width, height);
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
			// TODO Auto-generated method stub

		}
	};
	
	public SelectPane getSelectPane() {
		return selectPane;
	}

	public SelectFrame(List<String> users, String title, int type) {
		// TODO Auto-generated constructor stub
		this.setTitle(title);
		this.setSize(400, 500);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setContentPane(new JPanel());
		choosable = false;
		selectPane = new SelectPane(users, type);
		this.getContentPane().add(selectPane);
		this.getContentPane().setLayout(layout);
		// 居中显示窗体
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		// 得到窗体的宽、高
		int windowsWidth = this.getWidth();
		int windowsHeight = this.getHeight();
		// System.out.println(windowsWidth+","+windowsHeight);
		this.setBounds((width - windowsWidth) / 2, (height - windowsHeight) / 2, windowsWidth, windowsHeight);
		//失焦即销毁
		this.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				selectedList.clear();
				SendGroupFrame.setSendText("");
				dispose();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
		this.setVisible(true);
	}

	/** 获取被选中的用户们（在获取前一定要判断choosable） */
	static public List<String> getSelectedList() {
		return selectedList;
	}

	/** 判断selectedList能否被获取 */
	static public boolean getChoosable() {
		return choosable;
	}

	/** 设置selectedList能否被获取 */
	public static void setChoosable(boolean choosable) {
		SelectFrame.choosable = choosable;
	}
}

