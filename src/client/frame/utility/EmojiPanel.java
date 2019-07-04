package client.frame.utility;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import client.event.EventSendPicture;
import client.event.EventsBridge;
import resmgt.UserResource;;

public class EmojiPanel extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JScrollBar scrollBar = this.getHorizontalScrollBar();

	public EmojiPanel(String toUser) {
		// TODO Auto-generated constructor stub
		super(new JPanel());
		this.contentPanel = (JPanel) ((JViewport) this.getComponent(0)).getComponent(0);
		this.contentPanel.setLayout(new GridLayout(3, 3));
		int cal = 10 / 9;
		int cal_left = 10 % 9;
		int total = cal * 9 + cal_left;
		for (int i = 1; i < total + 1; i++) {
			EmojiButton btn;
			if (i < 10 + 1) {
				btn = new EmojiButton(i, toUser, true);
			} else {
				btn = new EmojiButton(i, toUser, false);
			}

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					System.out.println("pressed");
					if (e.getX() < getX() || e.getX() > (getX() + getWidth())) {
						getParent().getParent().setVisible(false);
					}
				}
			});
			this.contentPanel.add(btn);
		}

		this.contentPanel.setVisible(true);
	}

	public JScrollBar getScrollBar() {
		return scrollBar;
	}

	public void setScrollBar(JScrollBar scrollBar) {
		this.scrollBar = scrollBar;
	}
}
