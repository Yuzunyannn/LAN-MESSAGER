package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import client.frame.Theme;

public class UButtonMouse extends MouseAdapter {
	protected JPopupMenu popmenu;
	protected JMenuItem item[];
	protected String username;
	protected ActionListener ItemMonitor;
	protected int type;
	public UButtonMouse(String[] str, ActionListener actionListener) {
		super();
		type=MouseEvent.BUTTON3;
		popmenu = new JPopupMenu();
		item = new JMenuItem[str.length];
		Border border = BorderFactory.createLineBorder(Theme.COLOR5);
		ItemMonitor = actionListener;
		for (int i = 0; i < item.length; i++) {
			item[i] = new JMenuItem(str[i]);
			item[i].setFont(Theme.FONT4);
			item[i].setBackground(Color.WHITE);
			item[i].setPreferredSize(new Dimension(150, 40));
			item[i].setHorizontalAlignment(SwingConstants.CENTER);
			item[i].setBorder(null);
			item[i].setActionCommand(i + "");
			item[i].addActionListener(ItemMonitor);

		}
		popmenu.setBackground(Color.WHITE);
		popmenu.setBorder(border);
		// popmenu.setPopupSize(160,200);
	}
	public UButtonMouse(String[] str, ActionListener actionListener,int type) {
		super();
		this.type=type;
		popmenu = new JPopupMenu();
		item = new JMenuItem[str.length];
		Border border = BorderFactory.createLineBorder(Theme.COLOR5);
		ItemMonitor = actionListener;
		for (int i = 0; i < item.length; i++) {
			item[i] = new JMenuItem(str[i]);
			item[i].setFont(Theme.FONT4);
			item[i].setBackground(Color.WHITE);
			item[i].setPreferredSize(new Dimension(150, 40));
			item[i].setHorizontalAlignment(SwingConstants.CENTER);
			item[i].setBorder(null);
			item[i].setActionCommand(i + "");
			item[i].addActionListener(ItemMonitor);

		}
		popmenu.setBackground(Color.WHITE);
		popmenu.setBorder(border);
		// popmenu.setPopupSize(160,200);
	}
protected void popupShow(MouseEvent e) {

		if (e.isPopupTrigger()) {
			username = ((MemberButton) e.getSource()).getMemberName();
			for (int i = 0; i < item.length; i++) {
				item[i].setActionCommand(username);
				popmenu.add(item[i]);
			}
			popmenu.show(e.getComponent(), e.getX(), e.getY());
		}

	

}
@Override 
public void mousePressed(MouseEvent e) {
	if (e.getButton() == type) {
		popupShow(e);
	}
}
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == type) {
			popupShow(e);
		}

	}

}