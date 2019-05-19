package client.frame.utility;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.text.BadLocationException;

import client.frame.Theme;
import client.word.Word;

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
			Component send = cons[1];
			int width = parent.getWidth();
			int height = parent.getHeight();

			text.setLocation(10, EDIT_MARGIN);
			text.setSize(width - 20, height - EDIT_MARGIN * 2);
			((JTextPane) ((JScrollPane) text).getViewport().getComponent(0)).updateUI();

			send.setLocation(width - 25 - send.getWidth(), height - EDIT_MARGIN + (int) (EDIT_MARGIN * 0.1f));
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
				boolean support = ChatInputPanel.this.textEdit.dragFile(file);
				if (support == false) {
					ChatInputPanel.this.textEdit.insert("[不支持的类型]");
					return false;
				}
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
	private ChatTextEditPane textEdit = new ChatTextEditPane(this);

	public ChatInputPanel() {
		// 默认颜色
		this.setBackground(Theme.COLOR1);
		// 设置最小大小
		this.setMinimumSize(new Dimension(64, 64 * 2));
		// 设置默认布局
		this.setLayout(layout);
		// 文件拖拽
		textEdit.setTransferHandler(fileDrag);
		// 添加文字编辑区域
		JScrollPane scroll = new JScrollPane(textEdit);
		JScrollBar bar = new JScrollBar();
		bar.setUI(new client.frame.ui.ScrollBarUI());
		bar.setBackground(textEdit.getBackground());
		scroll.setVerticalScrollBar(bar);
		scroll.setBorder(null);
		this.add(scroll);
		// 添加按钮
		JButton button = new JButton("发送");
		button.setFont(new Font("黑体", 0, 16));// 这句设置字体，在运行前，会发白一下？
		button.setUI(new client.frame.ui.NormalButtonUI());
		button.setSize(75, (int) (EDIT_MARGIN * 0.8f));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ChatInputPanel.this.sendWords();
			}
		});
		this.add(button);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int width = this.getWidth();
		g.setColor(Theme.COLOR2);
		g.drawLine(0, 0, width, 0);
	}

	public void sendWords() {
		ChatPanel parent = (ChatPanel) ChatInputPanel.this.getParent();
		List<Word> words;
		try {
			words = textEdit.getValue();
			if (words.isEmpty())
				return;
			textEdit.clear();
			parent.onSendMsg(words);
		} catch (BadLocationException e1) {
			log.Logger.log.warn("读取消息输入框内容出现异常：", e1);
		}
	}

}
