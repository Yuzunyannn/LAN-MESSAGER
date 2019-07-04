package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.event.EventIPC;
import client.event.EventSearch;
import client.event.EventsBridge;
import client.frame.MainFrame;
import client.frame.Restart;
import client.frame.Theme;
import client.frame.selection.SelectGroupFrame;
import client.frame.selection.SendGroupFrame;
import event.IEventBus;
import log.Logger;
import resmgt.ResourceManagement;
import user.User;

public class SearchPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField search;
	private TextPanel textpanel;
	private SearchPanel sptmp = this;
	public static final int SEARCH_FIELD_LENGTH = 80;
	public static final String INFO = "单行输入,回车键搜索";
	public static final String[] FUCTIONLIST = { "发起群聊", "群发消息" };
	public static final String[] SETLIST = { "注销" };
	boolean entry = false;

	private KeyListener keyListener = new KeyListener() {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == '\n') {
				EventsBridge.frontendEventHandle.post(new EventSearch(search.getText()));

			}

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
		textpanel = new TextPanel();
		search = textpanel.getTextField();
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

		JButton b1 = textpanel.getButton();
		JButton b2 = new JButton();
		JButton b3 = new JButton();
		UButtonMouse mouse = new UButtonMouse(FUCTIONLIST, new fuctionListListener()) {
			@Override
			protected void popupShow(MouseEvent e) {
				for (int i = 0; i < item.length; i++) {
					popmenu.add(item[i]);
				}
				popmenu.show(e.getComponent(), 10, 20);
			}

			@Override
			public void mousePressed(MouseEvent e) {

				popupShow(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				popupShow(e);
			}

		};
		b2.addMouseListener(mouse);
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
		b3.addMouseListener(new UButtonMouse(SETLIST, new SetListListener(), MouseEvent.BUTTON1) {
			@Override
			protected void popupShow(MouseEvent e) {

				for (int i = 0; i < item.length; i++) {
					item[i].setActionCommand(username);
					popmenu.add(item[i]);
				}
				popmenu.show(e.getComponent(), 10, 20);
			}

		});
		search.addKeyListener(keyListener);
		search.setForeground(Color.GRAY);
		textpanel.setBounds(10, 20, 190, 36);
		search.setText(INFO);
		search.addFocusListener(new DefaultFocusListener(INFO, textpanel));
		int buttonsize = 30;
		b2.setBounds(220, 23, buttonsize, buttonsize);
		b3.setBounds(260, 23, buttonsize, buttonsize);
		b2.setIcon(new ImageIcon(ResourceManagement.instance.getTmpResource("icon-plane").getImage()));
		b3.setIcon(new ImageIcon(ResourceManagement.instance.getTmpResource("icon-add").getImage()));
		b2.setBackground(Color.black);
		b3.setBackground(Color.black);
		b2.setOpaque(false);
		b3.setOpaque(false);
		this.add(b2);
		this.add(b3);
		this.add(textpanel);
		// this.add(search);

	}

	public void searchInit() {
		search.setForeground(Color.GRAY);
		search.setText(INFO);
		this.setBackground(Theme.COLOR5);
		entry = false;
		search.setFocusable(entry);
		textpanel.isSearch = false;

	}

	public void initEvent(IEventBus bus) {
		bus.register(this);
	}
}

class fuctionListListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] str = SearchPanel.FUCTIONLIST;
		String temp = ((JMenuItem) e.getSource()).getText();
		ArrayList<String> ulist = new ArrayList<String>();
		for (User tmp : InfoPanel.userSet) {
			ulist.add(tmp.userName);
		}
		/** 群聊 */
		if (temp.equals(str[0])) {
			SelectGroupFrame sf = new SelectGroupFrame(ulist, "群聊");
			Logger.log.impart(str[0]);
		}
		/** 群发 */
		else if (temp.equals(str[1])) {
			SendGroupFrame sgf = new SendGroupFrame(ulist);
			Logger.log.impart(str[1]);
		}
	}
}

class SetListListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] str = SearchPanel.SETLIST;
		String temp = ((JMenuItem) e.getSource()).getText();

		/** 注销 */
		if (temp.equals(str[0])) {
			Restart.restartApplication();
			Logger.log.impart(str[0]);
		}

	}
}

class DefaultFocusListener implements FocusListener {
	private String info;
	private TextPanel textfield;

	public DefaultFocusListener(String info, TextPanel textfield) {
		this.info = info;
		this.textfield = textfield;
	}

	@Override
	public void focusGained(FocusEvent e) {
		String temp = textfield.getTextField().getText();
		if (temp.equals(info)) {
			textfield.getTextField().setText(null);
			textfield.getTextField().setBackground(Theme.COLOR0);
			textfield.getTextField().setForeground(Color.BLACK);
			textfield.isSearch = true;

		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		String temp = textfield.getTextField().getText();
		if (temp.equals("")) {
			textfield.getTextField().setForeground(Color.GRAY);
			textfield.getTextField().setText(info);
			textfield.isSearch = false;
		}

	}

}