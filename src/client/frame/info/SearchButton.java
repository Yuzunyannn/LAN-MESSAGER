package client.frame.info;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JMenuItem;

import client.frame.Theme;

public class SearchButton extends MemberButton {
	private static final long serialVersionUID = 1L;
	public static final String SEARCHITEMSTR[]= {"查看信息","添加好友"};
	public SearchButton(String name) {
		super(name);
		this.setMemberName(name);
		SearchButton temp=this;
		JLabel member = new JLabel(name);
		this.setLayout(null);
		this.add(member);
		member.setSize(150, 30);
		member.setLocation(0, 20);
		member.setFont(Theme.FONT2);
		this.removeMouseListener(mouse);
		ActionListener searchItemListener=new SearchMenuItemMonitor();
		mouse= new UButtonMouse (SEARCHITEMSTR , searchItemListener) {
			@Override 
			public void mousePressed(MouseEvent e) {
				System.out.println(temp.getMemberName());
			}
		};
		this.addMouseListener(mouse);
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
class SearchMenuItemMonitor implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] str =SearchButton.SEARCHITEMSTR;
		String temp = ((JMenuItem) e.getSource()).getText();
		if(temp.equals(str[0])) {
			System.out.println(str[0]);
		}
		else if(temp.equals(str[1])) {
			System.out.println(str[1]);
		}
	}
	
}