package client.frame.selection;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.event.EventGroupSend;
import client.event.EventsBridge;
import client.frame.Theme;
import client.frame.utility.UtilityPanel;
import client.word.WordString;
import event.Event;
import user.UOnline;
import user.User;
import user.message.MessageGroupCreate;

public class ConfirmPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton confirm = new JButton("确认");
	private JButton cancel = new JButton("取消");
	private MouseListener listen = new MouseAdapter() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			System.out.println(SelectFrame.getSelectedList().size());
			if (e.getSource() == confirm) {
				if (SelectFrame.getSelectedList().isEmpty() && SendGroupFrame.getSendText() == "") {
					SelectFrame.setChoosable(false);
				} else {
					SelectFrame.setChoosable(true);
				}
				if (getParent().getParent().getParent().getParent() instanceof SendGroupFrame && SelectFrame.getChoosable()) {
					Component[] cons = ((SendGroupFrame)getParent().getParent().getParent().getParent()).getContentPane().getComponents();
					Component[] textCons = ((JPanel)cons[1]).getComponents();
					SendGroupFrame.setSendText(((JTextField)textCons[0]).getText());
					WordString ws = new WordString(SendGroupFrame.getSendText());
					List<User> users = new ArrayList<User>();
					List<String> userList = new ArrayList<String>(SelectFrame.getSelectedList());
					for (String string : userList) {
						users.add(UOnline.getInstance().getUser(string));
					}
					EventsBridge.frontendEventHandle.post(new EventGroupSend(userList, ws.getString()));
					UtilityPanel.sendWordToUsers(users, ws);
				}
				if (getParent().getParent().getParent().getParent() instanceof SelectGroupFrame && SelectFrame.getChoosable()) {
					List<User> users = new ArrayList<User>();
					List<String> userList = new ArrayList<String>();
					userList = SelectFrame.getSelectedList();
					for (String string : userList) {
						users.add(UOnline.getInstance().getUser(string));
					}
					MessageGroupCreate GroupMessage = new MessageGroupCreate(users);
					EventsBridge.groupCreateRequest(GroupMessage);
				}
				((SelectFrame) getParent().getParent().getParent().getParent()).dispose();
			} else if (e.getSource() == cancel) {
				SelectFrame.getSelectedList().clear();
				SendGroupFrame.setSendText("");
				SelectFrame.setChoosable(false);
				((SelectFrame) getParent().getParent().getParent().getParent()).dispose();
			}
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
	};

	public ConfirmPane() {
		// TODO Auto-generated constructor stub
		this.setBackground(Theme.COLOR0);
		confirm.setBackground(null);
		cancel.setBackground(null);
		confirm.addMouseListener(listen);
		cancel.addMouseListener(listen);
		this.add(confirm);
		this.add(cancel);
		this.setVisible(true);
	}

}
