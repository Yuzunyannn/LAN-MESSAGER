package client.frame.utility;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

public class MemeFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int TITLE_HEIGHT = 20;
	private EmojiPanel emojiPanel;

	public MemeFrame() throws HeadlessException {
		super();
		this.emojiPanel = new EmojiPanel();
		this.setContentPane(this.emojiPanel);
		setUndecorated(true);
		MouseHandler ml = new MouseHandler();
		addMouseListener(ml);
		addMouseMotionListener(ml);
		this.setAlwaysOnTop(true);
	}

	public MemeFrame(String title) throws HeadlessException {
		super(title);
		this.emojiPanel = new EmojiPanel();
		this.setContentPane(this.emojiPanel);
		setUndecorated(true);
		MouseHandler ml = new MouseHandler();
		addMouseListener(ml);
		addMouseMotionListener(ml);
		this.setAlwaysOnTop(true);
	}

	public Insets getInsets() {
		return new Insets(TITLE_HEIGHT, 1, 1, 1);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(0, 0, 128));
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g.fillRect(0, 0, getWidth(), TITLE_HEIGHT);

		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.white);
		g.drawString(getTitle(), 2, (TITLE_HEIGHT - fm.getHeight()) / 2 + fm.getAscent());
	}

	private class MouseHandler extends MouseInputAdapter {
		private Point point;

		public void mousePressed(MouseEvent e) {
			if (e.getY() <= TITLE_HEIGHT) {
				this.point = e.getPoint();
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (point != null) {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

				Point p = e.getPoint();
				int dx = p.x - point.x;
				int dy = p.y - point.y;

				int x = getX();
				int y = getY();
				setLocation(x + dx, y + dy);
			}
		}

		public void mouseReleased(MouseEvent e) {
			point = null;
			setCursor(Cursor.getDefaultCursor());
		}
	}
}
