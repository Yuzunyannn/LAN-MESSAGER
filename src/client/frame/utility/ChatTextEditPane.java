package client.frame.utility;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import client.frame.Theme;
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
		this.insertIcon(icon);
		return true;
	}

	/** 获取图标 */
	private Icon getIconWithInfo(File file) {
		Icon icon = new ImageIcon(file.getPath());
		// 如果没有读取成功，表明这个文件不是图片
		if (icon.getIconWidth() <= 0) {
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
		return icon;
	}

	/** 遍历输出 ，测试用！！ */
	public void eachChar() {
		StyledDocument doc = this.getStyledDocument();
		for (int i = 0; i < this.getText().length(); i++) {
			Element ele = doc.getCharacterElement(i);
			if (ele.getName().equals("icon")) {
				ImageIcon dd = (ImageIcon) StyleConstants.getIcon(ele.getAttributes());
				System.out.println(dd);
			} else {
				try {
					String str = doc.getText(i, 1);
					System.out.print(str);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
