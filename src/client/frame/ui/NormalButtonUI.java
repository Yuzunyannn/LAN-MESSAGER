package client.frame.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ButtonUI;

import client.frame.Theme;

public class NormalButtonUI extends ButtonUI {

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		c.setFont(new Font("黑体", 0, 16));// 这句设置字体，在运行前，会发白一下？
		c.setBackground(Theme.COLOR1);
		c.setBorder(BorderFactory.createLineBorder(Theme.COLOR2));
		c.addMouseListener(mouseListenser);
	}

	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
		c.removeMouseListener(mouseListenser);
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		int width = c.getWidth();
		int height = c.getHeight();
		if (!c.isEnabled()) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, width, height);
		} else {
			// 按下
			if (this.isPress) {
				g.setColor(Theme.COLOR3);
				g.fillRect(0, 0, width, height);
			}
			// 位于上方
			else if (this.isOver) {
				g.setColor(Theme.COLOR2);
				g.fillRect(0, 0, width, height);
			}
		}
		// 文字
		String str = ((JButton) c).getText();
		int strWidth = g.getFontMetrics(g.getFont()).stringWidth(str);
		int strHeight = g.getFontMetrics(g.getFont()).getHeight();
		g.setColor(Color.BLACK);
		g.drawString(str, (width - strWidth) / 2, strHeight + (height - strHeight) / 4);
	}

	boolean isOver = false;
	boolean isPress = false;

	/** 鼠标监听 */
	MouseAdapter mouseListenser = new MouseAdapter() {
		@Override
		public void mouseEntered(MouseEvent e) {
			e.getComponent().repaint();
			isOver = true;
		}

		@Override
		public void mouseExited(MouseEvent e) {
			e.getComponent().repaint();
			isOver = false;
		};

		@Override
		public void mousePressed(MouseEvent e) {
			e.getComponent().repaint();
			isPress = true;
		};

		@Override
		public void mouseReleased(MouseEvent e) {
			e.getComponent().repaint();
			isPress = false;
		};
	};

}
