package client.frame.utility;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.frame.Theme;
import client.frame.info.SubjectInfoFrame;
import client.frame.selection.SelectFrame;
import core.Adminsters;
import resmgt.UserResource;

public class ChatInfoPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel userName = new JLabel("None");
	private JButton userButton;
	private String chatToUserName = "";
	//private SubjectInfoFrame infoFrame;
	private SelectFrame group;

	public ChatInfoPanel(String chatTo) {
		// TODO Auto-generated constructor stub
		this.chatToUserName = Adminsters.userToInfo(chatTo);
		if (chatToUserName.indexOf("#G") == 0) {
			List<String> users = new ArrayList<String>();
			group = new SelectFrame(users,"群成员",SelectFrame.VIEW);
		} else {
			//infoFrame = new SubjectInfoFrame(chatToUserName);
		}
		this.setBackground(Theme.COLOR0);
		this.setLayout(new BorderLayout());
		this.userName.setText("  " + this.chatToUserName);
		this.userName.setFont(Theme.FONT2);
		this.userButton = new JButton();
		this.userButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (chatToUserName.indexOf("#G") == 0) {
					group.setVisible(true);
				} else {
					//infoFrame.setVisible(true);
				}
			}
		});
		this.userButton.setIcon(UserResource.getSysIcon("icon-user"));
		this.userButton.setBorderPainted(false);
		this.userButton.setBackground(null);
		this.userButton.setOpaque(false);
		this.add(this.userName, BorderLayout.WEST);
		this.add(this.userButton, BorderLayout.EAST);
		this.setBackground(Theme.COLOR2);
		this.setBorder(BorderFactory.createLineBorder(Theme.COLOR6, 1));
		this.setVisible(true);
	}

	public void addGroupMemeber(List<String> users) {
		// TODO Auto-generated method stub
		group = new SelectFrame(users,"群成员",SelectFrame.VIEW);
	}

}
