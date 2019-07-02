package client.frame.info;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;

import client.event.EventFriendOperation;
import client.event.EventShow;
import client.event.EventsBridge;
import client.frame.Theme;
import client.frame.utility.UtilityPanel;

public class SearchListButton extends BasePanelButton {
	public static final String prompt[] = { "查看信息", "添加好友" };

	public SearchListButton() {
		super();
		addLabel(baseName);
		this.addMouseListener(new BaseButtonMouse(prompt, new SearchMonitor(baseName)));
	}

	public SearchListButton(String id, String name) {
		super(id, name);
		SearchListButton mtmp = this;
		this.addMouseListener(new BaseButtonMouse(prompt, new SearchMonitor(baseName)) {
	
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!choose)
					mtmp.setBackground(Theme.COLOR8);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!choose)
					mtmp.setBackground(Theme.COLOR3);
			}

		});
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Theme.COLOR2);
		int width = super.getWidth();
		int height = super.getHeight();
		g.fillRect(0, 0, width, height);
	}
}
class SearchMonitor implements ActionListener {
	private String buttonId;
	private String toolId = UtilityPanel.TOOLID_CHATING;

	public SearchMonitor(String bid) {
		buttonId = bid;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] str =SearchListButton.prompt;
		String temp = ((JMenuItem) e.getSource()).getText();
		if (temp.equals(str[0])) {
			EventsBridge.frontendEventHandle.post(new EventShow(buttonId, toolId));
//			Logger.log.warn("UtilityPanel部分需要完成一个显示用户信息盘，此处暂时用聊天盘");
		} else if (temp.equals(str[1])) {
			EventsBridge.frontendEventHandle.post(new EventFriendOperation(buttonId, EventFriendOperation.ADDFRIEND));
		}
		}
	}

