package client.frame.utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import client.frame.Theme;
import util.FileHelper;

public class ChatInputPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/** 输入框的上下边距 */
	public static final int EDIT_MARGIN = 35;

	/** 输入区域自定义布局 */
	private LayoutManager layout = new LayoutManager() {

		@Override
		public void addLayoutComponent(String arg0, Component arg1) {
		}

		/** 布局 */
		@Override
		public void layoutContainer(Container parent) {
			Component[] cons = parent.getComponents();
			Component text = cons[0];
			int width = parent.getWidth();
			int height = parent.getHeight();
			text.setLocation(10, EDIT_MARGIN);
			text.setSize(width - 20, height - EDIT_MARGIN * 2);
			((JTextPane) ((JScrollPane) text).getViewport().getComponent(0)).updateUI();
		}

		@Override
		public Dimension minimumLayoutSize(Container arg0) {
			return new Dimension(arg0.getWidth(), 64 * 2);
		}

		@Override
		public Dimension preferredLayoutSize(Container arg0) {
			return new Dimension(arg0.getWidth(), 64 * 4);
		}

		@Override
		public void removeLayoutComponent(Component arg0) {

		}
	};

	/** 输入栏文件拖拽 */
	private TransferHandler fileDrag = new TransferHandler() {
		private static final long serialVersionUID = 1L;

		/** 导入数据（拖入文件） */
		@Override
		public boolean importData(JComponent comp, Transferable t) {
			try {
				// 获取拖入的所有文件
				Object obj = t.getTransferData(DataFlavor.javaFileListFlavor);
				List<?> files = null;
				if (obj instanceof List) {
					files = (List<?>) obj;
				} else
					return false;
				// 获取拖入的第一个文件
				File file = (File) files.get(0);
				ChatInputPanel.this.textEdit.dragFile(file);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public boolean canImport(JComponent comp, DataFlavor[] flavors) {
			for (int i = 0; i < flavors.length; i++) {
				if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
					return true;
				}
			}
			return false;
		}
	};
	/** 文本输入框 */
	private TextEditPane textEdit = new TextEditPane();

	public ChatInputPanel() {
		// 默认颜色
		this.setBackground(Theme.COLOR1);
		// 设置最小大小
		this.setMinimumSize(new Dimension(64, 64 * 2));
		// 设置默认布局
		this.setLayout(layout);
		// 文件拖拽
		textEdit.setTransferHandler(fileDrag);
		// 添加文字编辑
		JScrollPane scroll = new JScrollPane(textEdit);
		scroll.setBorder(null);
		this.add(scroll);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int width = this.getWidth();
		g.setColor(Theme.COLOR2);
		g.drawLine(0, 0, width, 0);
	}

	/** 文本输入框的类 */
	private class TextEditPane extends JTextPane {
		private static final long serialVersionUID = 1L;

		public TextEditPane() {
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
			textEdit.insertIcon(icon);
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
		@SuppressWarnings("unused")
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

}
