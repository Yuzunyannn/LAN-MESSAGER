package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import client.frame.MainFrame;
import client.frame.Theme;

public class SearchPanel extends JPanel {
	private TextField search;
public SearchPanel() {
	this.setLayout(null);
	this.setMinimumSize(new Dimension(MainFrame.INFO_RIGION_WIDTH,30));
	this.setMaximumSize(new Dimension(MainFrame.INFO_RIGION_WIDTH,30));
	this.setBackground(Theme.COLOR2);
	search=new TextField();
	this.add(search);
	JButton b1=new JButton();
	JButton set=new JButton();
	this.add(b1);
	this.add(set);

	search.setSize(150, 20);
	search.setLocation(5,8 );
	search.setText("单行输入");
	b1.setSize(20,20);
	b1.setLocation(180, 8);
	set.setSize(20,20);
	set.setLocation(220, 8);

}
}
