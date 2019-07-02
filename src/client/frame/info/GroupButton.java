package client.frame.info;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import client.event.EventShow;
import client.event.EventsBridge;
import client.frame.Theme;
import user.User;

public class GroupButton extends MemberButton {
private List<User> userlist;
private User boss;
public GroupButton(String id,String label,List<User> user,User boss) {
	super(id,label);
	this.userlist=user;
	this.boss=boss;
	GroupButton	mtmp =this;
	this.removeMouseListener(mouse);
	mouse = new UButtonMouse(MEMBERITEMSTR, new MemberMenuItemMonitor()) {
		@Override
		public void mousePressed(MouseEvent e) {
			// 产生选择事件
			if (e.getButton() == MouseEvent.BUTTON1) {
				isChat = true;
				count = 0;
				// 产生选择事件
				EventsBridge.frontendEventHandle.post(new EventShow(toolId, id,userlist,mtmp.boss));
	
			} else if (e.getButton() == MouseEvent.BUTTON3) {
					if (e.isPopupTrigger()) {
					username = ((MemberButton) e.getSource()).getMemberName();
					for (int i = 0; i < item.length; i++) {
						item[i].setActionCommand(username);
						popmenu.add(item[i]);
					}
					popmenu.show(e.getComponent(), e.getX(), e.getY());
				}
				System.out.println("右键点击2");
			}

		}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {
			if (!isChat)
				mtmp.setBackground(Theme.COLOR8);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (!isChat)
				mtmp.setBackground(Theme.COLOR3);
		}
	};
	this.addMouseListener(mouse);
}
public List<User> getUserlist() {
	return userlist;
}
public User getBoss() {
	return boss;
}

}
