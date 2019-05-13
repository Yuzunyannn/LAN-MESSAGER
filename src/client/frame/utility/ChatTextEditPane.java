package client.frame.utility;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import client.frame.Theme;
import client.word.Word;
import client.word.WordFile;
import client.word.WordString;
import util.FileHelper;

/** 文本输入框的类 */
public class ChatTextEditPane extends JTextPane {
	private static final long serialVersionUID = 1L;

	public ChatTextEditPane() {
		this.setBackground(Theme.COLOR1);
		this.setFont(new Font(null, 0, 20));
		// 重新创建EditorKit
		ViewFactory oldFactory = this.getEditorKit().getViewFactory();
		this.setEditorKit(new StyledEditorKit() {
			private static final long serialVersionUID = 1L;
			private WarpViewFactory vf = new WarpViewFactory(oldFactory);

			@Override
			public ViewFactory getViewFactory() {
				return vf;
			}
		});
		// 鼠标双击图片
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int st = getSelectionStart();
				int ed = getSelectionEnd();
				if (ed - st == 0)
					return;
				Element e1 = getStyledDocument().getCharacterElement(st);
				if (e1.getName().equals("icon")) {

				}
			}
		});
		// 按键监听
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						if (e.isControlDown()) {
							ChatTextEditPane.this.getStyledDocument().insertString(
									ChatTextEditPane.this.getCaretPosition(), "\n", new SimpleAttributeSet());
						}
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	/** 自动换行的ViewFactory */
	class WarpViewFactory implements ViewFactory {
		final ViewFactory parent;

		public WarpViewFactory(ViewFactory parent) {
			this.parent = parent;
		}

		@Override
		public View create(Element elem) {
			String kind = elem.getName();
			if (kind.equals(AbstractDocument.ContentElementName)) {
				return new LabelView(elem) {
					@Override
					public float getMinimumSpan(int axis) {
						switch (axis) {
						case View.X_AXIS:
							return 0;
						default:
							return super.getMinimumSpan(axis);
						}
					}
				};
			} else
				return parent.create(elem);
		}
	}

	/** 加入一个文件 */
	public boolean dragFile(File file) {
		Icon icon = this.getIconWithInfo(file);
		if (icon == null)
			return false;
		this.insertIcon(icon);
		return true;
	}

	public void insert(String str) {
		Document docs = this.getDocument();
		try {
			docs.insertString(docs.getLength(), str, null);
		} catch (BadLocationException e) {
			log.Logger.log.warn("插入文字出现异常", e);
		}
	}

	/** 获取图标 */
	private Icon getIconWithInfo(File file) {
		Icon icon = new ImageIcon(file.getPath());
		boolean isFile = false;
		// 如果没有读取成功，表明这个文件不是图片
		if (icon.getIconWidth() <= 0) {
			if (!file.isFile())
				return null;
			if (file.getName().lastIndexOf(".lnk") != -1)
				return null;
			isFile = true;
			// 获取文件的图标
			icon = FileHelper.getIconFromFile(file);
		}
		if (icon != null && icon instanceof ImageIcon) {
			ImageIcon imgIcon = (ImageIcon) icon;
			// 根据逻辑进行缩放
			if (imgIcon.getIconWidth() > 64 || imgIcon.getIconHeight() > 64) {
				if (imgIcon.getIconWidth() > imgIcon.getIconHeight()) {
					imgIcon.setImage(imgIcon.getImage().getScaledInstance(64,
							64 * imgIcon.getIconHeight() / imgIcon.getIconWidth(), Image.SCALE_DEFAULT));
				} else {
					imgIcon.setImage(imgIcon.getImage().getScaledInstance(
							64 * imgIcon.getIconWidth() / imgIcon.getIconHeight(), 64, Image.SCALE_DEFAULT));

				}
			}
		}
		return new IconInfo(icon, file, isFile ? IconInfo.FILE : IconInfo.IMAGE);
	}

	/** 清空内容 */
	public void clear() {
		this.setText(null);
	}

	/** 获取内容 */
	public List<Word> getValue() throws BadLocationException {
		List<Word> words = new LinkedList<Word>();
		StyledDocument doc = this.getStyledDocument();
		int lastStart = 0;
		int length = this.getText().length();
		for (int i = 0; i < length; i++) {
			Element ele = doc.getCharacterElement(i);
			if (!ele.getName().equals("content")) {
				if (lastStart != i)
					words.add(new WordString(doc.getText(lastStart, i - lastStart)));
				lastStart = i + 1;
				if (ele.getName().equals("icon")) {
					IconInfo iifo = (IconInfo) StyleConstants.getIcon(ele.getAttributes());
					words.add(new WordFile(iifo.file));
				}
			}
		}
		if (lastStart != length)
			words.add(new WordString(doc.getText(lastStart, length - lastStart)));
		return words;
	}

	static private class IconInfo implements Icon {

		final public static int IMAGE = 0;
		final public static int FILE = 0;

		public final Icon icon;
		public final File file;
		@SuppressWarnings("unused")
		public final int type;

		public IconInfo(Icon icon, File file, int type) {
			this.icon = icon;
			this.file = file;
			this.type = type;
		}

		@Override
		public int getIconHeight() {
			return icon.getIconHeight();
		}

		@Override
		public int getIconWidth() {
			return icon.getIconWidth();
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			icon.paintIcon(c, g, x, y);
		}

	}
}
