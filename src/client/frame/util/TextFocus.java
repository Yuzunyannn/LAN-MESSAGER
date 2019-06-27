package client.frame.util;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JPasswordField;
import javax.swing.text.JTextComponent;

import client.frame.Theme;

public class TextFocus extends FocusAdapter {
	final String str;

	public TextFocus(JTextComponent p, String str) {
		this.str = str;
		if (p instanceof JPasswordField)
			((JPasswordField) p).setEchoChar('好');
		this.focusLost(new FocusEvent(p, 0));
		p.addFocusListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {
		String tmp = ((JTextComponent) e.getComponent()).getText();
		if (tmp.equals(str)) {
			if (e.getComponent() instanceof JPasswordField) {
				((JPasswordField) e.getComponent()).setEchoChar('好');
			}
			((JTextComponent) (e.getComponent())).setText("");
			((JTextComponent) (e.getComponent())).setForeground(Color.black);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		String tmp = ((JTextComponent) e.getComponent()).getText();
		if (tmp == null || tmp.isEmpty()) {
			if (e.getComponent() instanceof JPasswordField) {
				((JPasswordField) e.getComponent()).setEchoChar((char) 0);
			}
			((JTextComponent) e.getComponent()).setText(str);
			((JTextComponent) (e.getComponent())).setForeground(Theme.COLOR9);
		}
	}
}
