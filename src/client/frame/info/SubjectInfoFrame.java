package client.frame.info;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import client.frame.Theme;
import resmgt.UserResource;
import resmgt.UserResource.HeadIconSize;

/** 用于显示对象的信息 */
public class SubjectInfoFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int TITLE_HEIGHT = 20;
	private JButton userIcon = new JButton();
	private JLabel userLabel = new JLabel();

	public SubjectInfoFrame(String userName) {
		// TODO Auto-generated constructor stub
		this.setTitle(userName + "的个人信息");
		this.setUndecorated(true);
		// 设置最小窗体大小
		this.setMinimumSize(this.getLayout().minimumLayoutSize(this));
		// 设置窗体大小和位置
		this.pack();
		// 居中显示窗体
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		// 得到窗体的宽、高
		this.setSize(new Dimension(600, 400));
		int windowsWidth = this.getWidth();
		int windowsHeight = this.getHeight();
		this.setBounds((width - windowsWidth) / 2, (height - windowsHeight) / 2, windowsWidth, windowsHeight);
		MouseHandler ml = new MouseHandler();
		addMouseMotionListener(ml);
		this.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
		this.setAlwaysOnTop(true);
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.gridx = 0;
		constraint.gridy = 0;
		this.getContentPane().add(userIcon, constraint);
		constraint.gridx = 1;
		constraint.gridy = 0;
		this.getContentPane().add(userLabel, constraint);
		constraint.gridx = 0;
		constraint.gridy = 1;
		this.add(new JLabel("备注："), constraint);
		constraint.gridx = 1;
		constraint.gridy = 1;
		this.getContentPane().add(new JLabel("备注信息"), constraint);
		constraint.gridx = 0;
		constraint.gridy = 2;
		this.getContentPane().add(new JLabel("地区："), constraint);
		constraint.gridx = 1;
		constraint.gridy = 2;
		this.getContentPane().add(new JLabel("地区："), constraint);
		
	}

	public void setUp(String userName) {
		ImageIcon imageIcon = UserResource.getHeadIcon(userName, HeadIconSize.SMALL);
		this.setTitle(userName + "的个人信息");
		userIcon.setIcon(imageIcon);
		userIcon.setBackground(null);
		userIcon.setOpaque(false);
		userLabel.setText(userName);
		userLabel.setBackground(null);
		userLabel.setOpaque(false);
	}

	public void updateInfo(String userName) {
		this.setUp(userName);
		this.setVisible(true);
	}

	private class MouseHandler extends MouseInputAdapter {
		private Point point;

		public void mousePressed(MouseEvent e) {
			if (e.getY() <= TITLE_HEIGHT) {
				this.point = e.getPoint();
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (point != null) {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				Point p = e.getPoint();
				int dx = p.x - point.x;
				int dy = p.y - point.y;
				int x = getX();
				int y = getY();
				setLocation(x + dx, y + dy);
			}
		}

		public void mouseReleased(MouseEvent e) {
			point = null;
			setCursor(Cursor.getDefaultCursor());
		}
	}

}
