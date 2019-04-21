package client.frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	/** 主窗体布局 */
	private LayoutManager layout = new LayoutManager() {

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		/** 排版 */
		@Override
		public void layoutContainer(Container parent) {
			Component[] cons = parent.getComponents();
			Dimension size = parent.getSize();
			Component button = cons[2];
			Component userName = cons[0];
			Component password = cons[1];

			userName.setSize(size.width / 2, 26);
			userName.setLocation((size.width - userName.getWidth()) / 2, (size.height - userName.getHeight()) / 4);
			password.setSize(size.width / 2, 26);
			password.setLocation((size.width - password.getWidth()) / 2,
					(size.height - password.getHeight()) / 4 + userName.getHeight() + 16);

			button.setSize(size.width / 3, 35);
			button.setLocation((size.width - button.getWidth()) / 2, (size.height - button.getHeight()) / 4 * 3);

		}

		@Override
		public Dimension minimumLayoutSize(Container arg0) {
			return new Dimension(225, 275);
		}

		@Override
		public Dimension preferredLayoutSize(Container arg0) {
			return new Dimension(275, 350);
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

	};

	JButton loginButton;

	public LoginFrame() {
		// 设置标题
		this.setTitle(MainFrame.FRAME_TITLE);
		this.setIconImage(null);
		// 设置布局
		this.setLayout(layout);
		// 颜色
		this.getContentPane().setBackground(Theme.COLOR0);
		// 账号框
		JTextField text = new JTextField();
		this.add(text);
		// 密码框
		JPasswordField password = new JPasswordField();
		this.add(password);
		// 登录按钮
		loginButton = new JButton("登录");
		loginButton.setUI(new client.frame.ui.NormalButtonUI());
		loginButton.setSize(75, 35);
		this.add(loginButton);
		// 设置关闭操作
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置最小窗体大小
		this.setMinimumSize(layout.minimumLayoutSize(this));
		// 设置窗体大小和位置
		this.pack();
		// 展示窗体
		this.setVisible(true);
	}

	public void setLoginListener(MouseListener listener) {
		loginButton.addMouseListener(listener);
	}
}
