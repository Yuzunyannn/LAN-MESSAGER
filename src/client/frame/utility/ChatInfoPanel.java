package client.frame.utility;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.frame.Theme;

public class ChatInfoPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel userName = new JLabel("None");
	private JButton userButton;
	private ImageIcon imageIcon;
	private String chatToUserName;

	public ChatInfoPanel(String chatTo) {
		// TODO Auto-generated constructor stub
		this.chatToUserName = chatTo;
		this.setBackground(Theme.COLOR0);
		this.setLayout(new BorderLayout());
		this.userName.setText(this.chatToUserName);
		this.userName.setFont(Theme.FONT2);
		this.add(this.userName, BorderLayout.WEST);
		this.imageIcon = new ImageIcon("src/resources/img/icons/" + "icon-user.png");
		this.imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, 30));
		this.userButton = new JButton();
		this.userButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Pressed!");
				JFrame frame = new JFrame();
				frame.setSize(640, 320);
				frame.setTitle("好友" + chatToUserName + "信息");
				frame.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		this.userButton.setIcon(imageIcon);
		this.userButton.setBorderPainted(false);
		this.userButton.setBackground(null);
		this.userButton.setOpaque(false);
		this.add(this.userButton, BorderLayout.EAST);
		this.setBackground(Theme.COLOR2);
		this.setBorder(BorderFactory.createLineBorder(Theme.COLOR6, 1));
		this.setVisible(true);
	}

}
