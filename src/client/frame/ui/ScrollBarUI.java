package client.frame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

import client.frame.Theme;

public class ScrollBarUI extends BasicScrollBarUI {

	public ScrollBarUI() {
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		return createZeroButton();
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		return createZeroButton();
	}

	/** 空按钮 */
	private JButton createZeroButton() {
		JButton button = new JButton();
		button.setPreferredSize(new Dimension(0, 0));
		button.setMinimumSize(new Dimension(0, 0));
		button.setMaximumSize(new Dimension(0, 0));
		return button;
	}

	/** 重绘滑动条 */
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		g.setColor(new Color(Theme.COLOR2.getRed(), Theme.COLOR2.getGreen(), Theme.COLOR2.getBlue(), 150));
		g.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 7, 7);
	}

	/** 重绘底色 */
	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
	}
}
