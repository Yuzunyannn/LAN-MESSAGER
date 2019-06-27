package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.event.EventIPC;
import client.event.EventSearch;
import client.event.EventsBridge;
import client.frame.MainFrame;
import client.frame.Theme;
import event.IEventBus;
import log.Logger;

public class SearchPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField search;
	public static final int SEARCH_FIELD_LENGTH = 80;
	public final String INFO = "单行输入,回车键搜索";
	boolean entry = false;
	private KeyListener keyListener = new KeyListener() {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == '\n')
				EventsBridge.frontendEventHandle.post(new EventSearch(search.getText()));
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

	};

	public SearchPanel() {
		this.setLayout(null);
		this.setMinimumSize(new Dimension(MainFrame.INFO_RIGION_WIDTH, SEARCH_FIELD_LENGTH));
		this.setMaximumSize(new Dimension(MainFrame.INFO_RIGION_WIDTH, SEARCH_FIELD_LENGTH));
		this.setBackground(Theme.COLOR5);
		search = new RTextField();
		search.setFont(Theme.FONT3);
		search.setFocusable(entry);

		// 搜索栏鼠标点击事件
		search.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				search.setFocusable(entry);
				search.requestFocus();
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
		search.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				// 失去焦点执行的代码
				boolean a = search.isFocusOwner();
				System.out.println(a + "search失去焦点" + entry);
				search.setFocusable(false);
			}

			public void focusGained(FocusEvent e) {
				// 获得焦点执行的代码
				boolean a = search.isFocusOwner();
				System.out.println(a + "search获得焦点" + entry);
				EventsBridge.frontendEventHandle.post(new EventIPC(EventIPC.SEARCH));
			}
		});

		JButton b1 = new JButton() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				final int x = 5;
				g.drawLine(x, x, x + 20, x + 20);
				g.drawLine(x + 20, x, x, x + 20);
			}
		};
		JButton b2 = new JButton();
		SearchPanel stmp = this;
		MouseAdapter mb = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				EventsBridge.frontendEventHandle.post(new EventIPC(EventIPC.FRIENDS));
				stmp.searchInit();
				// 测试
				Logger.log.impart("点击按钮切换时的测试");
				search.setBackground(Theme.COLOR4);
			}
		};
		b1.addMouseListener(mb);
		search.addKeyListener(keyListener);
		search.setForeground(Color.GRAY);
		search.setSize(190, 36);
		search.setLocation(10, 20);
		search.setText(INFO);
		search.addFocusListener(new DefaultFocusListener(INFO, search));
		int buttonsize = 30;
		b1.setSize(buttonsize, buttonsize);
		b1.setLocation(220, 23);
		b2.setSize(buttonsize, buttonsize);
		b2.setLocation(260, 23);
		this.add(b1);
		this.add(b2);
		this.add(search);

	}

	public void searchInit() {
		search.setForeground(Color.GRAY);
		search.setText(INFO);
		this.setBackground(Theme.COLOR5);
		entry = false;
		search.setFocusable(entry);

	}

	public void initEvent(IEventBus bus) {
		bus.register(this);
	}

}

class DefaultFocusListener implements FocusListener {
	private String info;
	private JTextField textfield;

	public DefaultFocusListener(String info, JTextField textfield) {
		this.info = info;
		this.textfield = textfield;

	}

	@Override
	public void focusGained(FocusEvent e) {
		String temp = textfield.getText();
		if (temp.equals(info)) {
			textfield.setText(null);
			textfield.setBackground(Theme.COLOR0);
			textfield.setForeground(Color.BLACK);

		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		String temp = textfield.getText();
		if (temp.equals("")) {
			textfield.setForeground(Color.GRAY);
			textfield.setText(info);
		}

	}

}