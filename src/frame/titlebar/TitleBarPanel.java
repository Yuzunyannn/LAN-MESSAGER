package frame.titlebar;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import frame.MainFrame;

public class TitleBarPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public MainFrame frame;
	/** 自定义标题栏拖动 */
	private MouseAdapter titleBarController = new MouseAdapter() {
		int lastX, lastY;

		@Override
		public void mouseClicked(MouseEvent e) {
			// 双击切换
			int times = e.getClickCount();
			if (times == 2) {
				if (TitleBarPanel.this.frame.getExtendedState() == JFrame.NORMAL)
					TitleBarPanel.this.frame.maximize();
				else
					TitleBarPanel.this.frame.normal();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// 记录初始位置点
			lastX = e.getX();
			lastY = e.getY();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getXOnScreen() - lastX;
			int y = e.getYOnScreen() - lastY;
			if (x == 0 && y == 0)
				return;
			// 恢复正常状态
			if (TitleBarPanel.this.frame.getExtendedState() != JFrame.NORMAL) {
				int fullWidth = TitleBarPanel.this.frame.getWidth();
				TitleBarPanel.this.frame.normal();
				lastX = lastX * TitleBarPanel.this.frame.getWidth() / fullWidth;
				int dis = TitleBarPanel.this.frame.getWidth() / 4;
				lastX = Math.max(lastX, dis);
				lastX = Math.min(lastX, dis * 3);
				x = e.getXOnScreen() - lastX;
			}
			TitleBarPanel.this.frame.setLocation(x, y);
		}
	};

	public TitleBarPanel() {
		this.addMouseListener(titleBarController);
		this.addMouseMotionListener(titleBarController);
		this.setBackground(new Color(0, 0, 0, 25));
	}

}
