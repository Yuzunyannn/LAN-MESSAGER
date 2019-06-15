package client.frame.info;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;

import client.frame.Theme;

public class SearchButton extends MemberButton {
	private static final long serialVersionUID = 1L;
	public static final String SEARCHITEMSTR[]= {"查看信息","添加好友"};
	public SearchButton(String name) {
		super(name);
		this.removeMouseListener(mouse);
		ActionListener searchItemListener=new SearchMenuItemMonitor();
		mouse= new UButtonMouse (SEARCHITEMSTR , searchItemListener) {
			@Override 
			public void mousePressed(MouseEvent e) {
				
			}
		};
		this.addMouseListener(mouse);
	}
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		g.setColor(Theme.COLOR2);
		int width = super.getWidth();
		int height = super.getHeight();
		g.fillRect(0, 0, width, height);
	}

}
class SearchMenuItemMonitor implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] str =SearchButton.SEARCHITEMSTR;
		String temp = ((JMenuItem) e.getSource()).getText();
		String username = ((JMenuItem) e.getSource()).getActionCommand();
		if(temp.equals(str[0])) {
			System.out.println(str[0]);
		}
		else if(temp.equals(str[1])) {
			System.out.println(str[1]);
		}
	}
	
}