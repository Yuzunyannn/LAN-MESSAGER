package client.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

import client.frame.Theme;
import javafx.scene.control.ButtonBar.ButtonData;

public class Bubble extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel words;
	private RButton rbutton;
	private int h;

	public Bubble(String words, BubbleType type) {
		this.words = new JLabel(words);
		this.setLayout(new BorderLayout());
		if (type == BubbleType.FILE) {
			this.words.setSize(150, 0);
		} else {
			this.words.setSize(300, 0);
		}
		int lines = this.JlabelSetText(this.words, words);
//		View v = javax.swing.plaf.basic.BasicHTML.createHTMLView(this.words, this.words.getText());
//		v.setSize(300, Integer.MAX_VALUE);
//		this.h = (int) v.getMinimumSpan(View.Y_AXIS);
//		if (h <30) {
//			h = 40;
//		}
		h = lines * this.words.getFontMetrics(this.words.getFont()).getHeight();
		this.words.setSize(new Dimension(300, h));
		// System.out.println(this.words.getText());
		this.rbutton = new RButton(this.words.getText());
		// this.add(this.words);
		this.add(rbutton, BorderLayout.WEST);
		this.setOpaque(true);
		this.setBackground(Theme.COLOR0);
		this.setVisible(true);
	}

	public int getH() {
		return h;
	}

	/** 文字换行 */
	public int JlabelSetText(JLabel jLabel, String longString) {
		int lines = 0;
		StringBuilder builder = new StringBuilder("<html>");
		char[] chars = longString.toCharArray();
		FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
		int start = 0;
		int len = 0;
		while (start + len < longString.length()) {
			while (true) {
				len++;
				if (start + len > longString.length())
					break;
				if (fontMetrics.charsWidth(chars, start, len) > jLabel.getWidth()) {
					break;
				}
			}
			builder.append(chars, start, len - 1).append("<br/>");
			lines++;
			start = start + len - 1;
			len = 0;
		}
		builder.append(chars, start, longString.length() - start);
		builder.append("</html>");
		jLabel.setText(builder.toString());
		return lines;
	}

	public JLabel getWords() {
		return words;
	}

	static public BubbleType checkFileType(String name) {
		if (name.length() <= 4) {
			return BubbleType.NULL;
		}
		if (name.indexOf('.') < 0) {
			return BubbleType.FILE;
		}
		String suffix = name.substring(name.length() - 5, name.length());
		if (suffix.indexOf('.') == 0) {
			switch (suffix) {
			case ".jpeg":
				return BubbleType.PICTURE;
			case ".docx":
			case ".pptx":
			case ".pdf":
			case ".c":
			case ".h":
			case ".java":
			case ".cpp":
			case ".hpp":
				return BubbleType.FILE;
			default:
				break;
			}
		} else {
			suffix = name.substring(name.length() - 4, name.length());
			if (suffix.indexOf('.') == 0) {
				switch (suffix) {
				case ".jpg":
					return BubbleType.PICTURE;
				default:
					return BubbleType.FILE;
				}
			} else {
				suffix = name.substring(name.length() - 2, name.length());
				if (suffix.indexOf('.') == 0) {
					switch (suffix) {
					case "":
						return BubbleType.PICTURE;
					case ".c":
					case ".h":
						return BubbleType.FILE;
					default:
						break;
					}
				}
			}
		}
		return BubbleType.NULL;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.WHITE);
		int h = getHeight();
		int w = getWidth();
		RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, 10, 10);
		Shape clip = g2d.getClip();
		g2d.clip(r2d);
		g2d.fillRect(0, 0, w, h);
		g2d.setClip(clip);
		g2d.drawRoundRect(1, 1, w - 3, h - 3, 4, 4);
		g2d.dispose();
		super.paintComponent(g);
	}

}
