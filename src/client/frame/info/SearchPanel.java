package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import client.frame.MainFrame;
import client.frame.Theme;

public class SearchPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private TextField search;

	public SearchPanel() {
		this.setLayout(null);
		this.setMinimumSize(new Dimension(MainFrame.INFO_RIGION_WIDTH, 30));
		this.setMaximumSize(new Dimension(MainFrame.INFO_RIGION_WIDTH, 30));
		this.setBackground(Theme.COLOR2);
		search = new TextField();
		search.setFont(Theme.FONT3);
		this.add(search);
		JButton b1 = new JButton();
		JButton set = new JButton();
		this.add(b1);
		this.add(set);
		String info="单行输入";
		
		search.setForeground(Color.GRAY);
		search.setSize(150, 20);
		search.setLocation(5, 8);
		search.setText(info);
		search.addFocusListener(new DefaultFocusListener(info,search));
		b1.setSize(20, 20);
		b1.setLocation(180, 8);
		set.setSize(20, 20);
		set.setLocation(220, 8);

	}
}
class DefaultFocusListener implements FocusListener{
	private String info;
	private TextField textfield;
	public DefaultFocusListener(String info,TextField textfield) {
		this.info = info;
		this.textfield = textfield;
	}
	@Override
	public void focusGained(FocusEvent e) {
		String temp = textfield.getText();
		if(temp.equals(info)) {
			textfield.setText(null);
			textfield.setForeground(Color.BLACK);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		String temp = textfield.getText();
		if(temp.equals("")) {
			textfield.setForeground(Color.GRAY);
			textfield.setText(info);
		}
		
	}
}