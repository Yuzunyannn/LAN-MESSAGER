package client.frame.info;

import java.awt.Color;

import javax.swing.JTextField;

import client.frame.Theme;


public class RTextField extends JTextField{

	public RTextField() {
		super();
		this.setBackground(Theme.COLOR4);
		this.setBorder(new TextBorderUtlis(new Color(192,192,192), 4, true));
	}

	
	}

