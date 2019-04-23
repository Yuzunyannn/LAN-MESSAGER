package client.frame.info;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import client.frame.Theme;

public class ListScrollPanel extends JScrollPane{
	/*添加列表中的成员数量时可能需要改变*/
	private int height=0;
	JPanel p;
public ListScrollPanel() {
	super();
	p=new JPanel();
	int width=super.getWidth();
	p.setPreferredSize(new Dimension(width,getHeight()));
	p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
	p.setBackground(Theme.COLOR0);
	this.add(p);
	 this.setViewportView(p);
     //设置垂直滚动条的显示: 一直显示
     this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

}
public int getHeight() {
	return height;
}
public void setHeight(int height) {
	this.height = height;
}
public  void addNewMember(String name) {
		
		p.add(new MemberButton(name));
		height+=MemberButton.MEMBERBUTTON_HEIGHT;
		int width=super.getWidth();
		p.setPreferredSize(new Dimension(width,height));
}
}
