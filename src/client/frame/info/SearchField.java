package client.frame.info;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

import client.frame.Theme;
import resmgt.ResourceManagement;

public class SearchField extends JTextField {

	private boolean entry;
	private JButton cancel = new JButton() {
		// 打印容器
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			this.setBackground(Theme.COLOR0);
			this.setBorderPainted(false);
		}

		// 容器首选大小
		@Override
		public Dimension getPreferredSize() {
			Dimension dimension = SearchField.super.getPreferredSize();
			dimension.height -= 6;
			dimension.width = dimension.height;
			return dimension;
		}
	};

	private SearchField self = this;

	public SearchField() {
		super();
		init();
	}

	private void init() {
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				SearchField.this.onCancel(self);
			}
		});
		//cancel.setIcon(new ImageIcon(ResourceManagement.instance.getPackResource("img/icons/cancel.png").getImage()));
		cancel.setIcon(new ImageIcon("img/icons/cancel.png"));
		this.setBackground(Color.gray);
		this.setPreferredSize(new Dimension(180, 36));
		this.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		this.setBorder(BorderFactory.createLineBorder(Color.black,2));
		this.setFont(Theme.FONT3);
		this.setFocusable(false);
		this.onFocusLost(self);
		// 監聽器
		// 搜索栏鼠标点击事件
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				self.setFocusable(entry);
				self.requestFocus();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				entry = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				entry = false;
			};
		});
		// 焦点获得/失去
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				// 失去焦点执行的代码
				System.out.println("search失去焦点" + entry);
				self.setFocusable(false);
				self.onFocusLost(self);
			}

			@Override
			public void focusGained(FocusEvent e) {
				// 获得焦点执行的代码
				System.out.println("search获得焦点" + entry);
				self.onFocusGained(self);
			}
		});
	}

	// JComponent通知此组件它现在已有了一个父组件
	@Override
	public void addNotify() {
		super.addNotify();
		add(cancel);// Container中方法将指定组件追加到此容器的尾部
	}

	public void onCancel(SearchField sf) {		
	};

	public void onFocusGained(SearchField sf) {
		this.setBackground(Theme.COLOR0);
		this.setForeground(Color.black);
		cancel.setVisible(true);
		if(this.getText().equals("单行输入,回车键搜索"))
		{
			this.setText("");
		}
	};

	public void onFocusLost(SearchField sf) {
		String temp = this.getText();
		if (temp.equals("")) {
			this.setBackground(Color.gray);
			this.setForeground(Theme.COLOR0);
			this.setText("单行输入,回车键搜索");
			cancel.setVisible(false);
		}
	};}