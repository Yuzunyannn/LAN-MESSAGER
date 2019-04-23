package client.frame.info;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.frame.Theme;

/** 界面左边的区域 用户区域 */
public class InfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public InfoPanel() {
		this.setBackground(Theme.COLOR2);
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		UserPanel c1=new UserPanel();
		SearchPanel c2=new SearchPanel();
		ListScrollPanel c3=new ListScrollPanel();
		this.add(c1);
		this.add(c2);
		this.add(c3);
		c3.addNewMember("1");
		c3.addNewMember("1");
		c3.addNewMember("1");
		c3.addNewMember("1");
		c3.addNewMember("1");
		c3.addNewMember("1");
		c3.addNewMember("1");
		c3.addNewMember("1");
		c3.addNewMember("1");
		c3.addNewMember("1");
		c3.addNewMember("1");
		c3.addNewMember("1");
		c3.addNewMember("1");
//		this.add(c2,BorderLayout.CENTER);
		c1.setPreferredSize(new Dimension(0,80));
		c2.setPreferredSize(new Dimension(0,50));
		

	}
}
