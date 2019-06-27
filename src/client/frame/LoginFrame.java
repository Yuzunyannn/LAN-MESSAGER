package client.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.frame.util.TextFocus;
import client.record.RecordLogInfo;
import client.record.RecordManagement;

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
			Component button = cons[3];
			Component userName = cons[0];
			Component password = cons[1];
			Component label = cons[2];

			userName.setSize(size.width / 2, 26);
			userName.setLocation((size.width - userName.getWidth()) / 2, (size.height - userName.getHeight()) / 4);
			password.setSize(size.width / 2, 26);
			password.setLocation((size.width - password.getWidth()) / 2,
					(size.height - password.getHeight()) / 4 + userName.getHeight() + 16);

			button.setSize(size.width / 3, 35);
			button.setLocation((size.width - button.getWidth()) / 2, (size.height - button.getHeight()) / 4 * 3);

			label.setSize(size.width / 2, 35);
			label.setLocation((size.width - label.getWidth()) / 2,
					(size.height - password.getHeight()) / 4 + userName.getHeight() + 16 + password.getHeight() + 16);

			Component help = cons[4];
			help.setSize(size.width / 3, 35);
			help.setLocation((size.width - help.getWidth()) / 2,
					(size.height - help.getHeight()) / 4 * 3 + button.getHeight() + 5);

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
	JTextField userNameText;
	JPasswordField password;
	JLabel hint;
	JButton help;

	public LoginFrame() {
		// 设置标题
		this.setTitle(MainFrame.FRAME_TITLE);
		this.setIconImage(null);
		// 设置布局
		this.setLayout(layout);
		// 颜色
		this.getContentPane().setBackground(Theme.COLOR0);

		// 账号框
		userNameText = new JTextField();

		userNameText.setBorder(BorderFactory.createLineBorder(Theme.COLOR6));
		this.add(userNameText);

		// 密码框
		password = new JPasswordField();
		password.setBorder(BorderFactory.createLineBorder(Theme.COLOR6));
		this.add(password);
		// 信息提示
		hint = new JLabel("", JLabel.CENTER);
		hint.setForeground(Color.RED);
		this.add(hint);
		// 登录按钮
		loginButton = new JButton("登录");
		loginButton.setUI(new client.frame.ui.NormalButtonUI());
		loginButton.setSize(75, 35);
		this.add(loginButton);
		this.setLocationRelativeTo(null);
		// 临时的帮助信息
		help = new JButton("帮助");
		help.setUI(new client.frame.ui.NormalButtonUI());
		help.setSize(75, 35);
		this.add(help);
		// 设置关闭操作
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置最小窗体大小
		this.setMinimumSize(layout.minimumLayoutSize(this));
		// 设置窗体大小和位置
		this.pack();
		// 居中显示窗体
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		// 得到窗体的宽、高
		int windowsWidth = this.getWidth();
		int windowsHeight = this.getHeight();
		// System.out.println(windowsWidth+","+windowsHeight);
		this.setBounds((width - windowsWidth) / 2, (height - windowsHeight) / 2, windowsWidth, windowsHeight);
		// 回复记录
		RecordLogInfo lInfo = RecordManagement.getLogInfo();
		userNameText.setText(lInfo.username);
		password.setText(lInfo.password);
		new TextFocus(userNameText, "请输入用户名");
		new TextFocus(password, "请输入密码");
		
		help.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(LoginFrame.this,
						"这是软工3组所制作的局域网聊天系统。当前系统处于测试中。随便输入一个3位以上文字作为用户名，无需密码登陆。登陆以后搜索框可以搜索当前在线人数。如果程序出现闪退，请发送logs文件到：bjshenshijun@emails.bjut.edu.cn",
						"帮助", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

	public void setLoginListener(MouseListener listener) {
		loginButton.addMouseListener(listener);
	}

	public String getUserName() {
		return userNameText.getText();
	}

	public String getPassword() {
		return new String(password.getPassword());
	}

	public void setLoginButtonEnable(boolean enable) {
		loginButton.setEnabled(enable);
	}

	public boolean isEnable() {
		return loginButton.isEnabled();
	}

	public void setHint(String str) {
		hint.setText(str);
	}
}
