package client.frame.selection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

import client.frame.Theme;

public class SendGroupFrame extends SelectFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1910038339404760274L;
	private int confirmHeight = 30;
	private int textAreaHeight = 40;
	private int selectHeight = 350;
	private JTextField textField = new JTextField();

	private ConfirmPane confirmPane = new ConfirmPane();
	private JPanel textPanel = new JPanel();

	private String hintText = "输入要发送的消息";
	private static String sendText = "";
	private LayoutManager layout = new LayoutManager() {

		@Override
		public void removeLayoutComponent(Component comp) {
			// TODO Auto-generated method stub

		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void layoutContainer(Container parent) {
			// TODO Auto-generated method stub
			Component[] cons = parent.getComponents();
			int height = parent.getHeight();
			int width = parent.getWidth();

			Component confirmArea = cons[3];
			confirmArea.setLocation(0, height - confirmHeight - 1);
			confirmArea.setSize(width, confirmHeight);

			Component textArea = cons[2];
			textArea.setLocation(0, height - confirmHeight - textAreaHeight - 1);
			textArea.setSize(width, textAreaHeight);

			Component selectList = cons[1];
			selectList.setLocation(0, height - confirmHeight - textAreaHeight - selectHeight - 1);
			selectList.setSize(width, selectHeight);
			
			Component searchArea = cons[0];
			searchArea.setLocation(0, 0);
			searchArea.setSize(width, height - confirmHeight - textAreaHeight - selectHeight);
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
			// TODO Auto-generated method stub

		}
	};

	/** 获取发送的文字消息，注意要提前判断下SelectFrame中的choosable */
	public static String getSendText() {
		return sendText;
	}

	/** 设置发送的文字消息内容 */
	public static void setSendText(String sendText) {
		SendGroupFrame.sendText = sendText;
	}

	public SendGroupFrame(List<String> users) {
		super(users,  "群发消息", SelectFrame.CHOOSE);
		textField.setText(hintText);
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				// 获取焦点时，清空提示内容
				String temp = textField.getText();
				if (temp.equals(hintText)) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
				}
			}
		});
		textField.setForeground(Theme.COLOR9);
		textPanel.setLayout(new BorderLayout());
		textPanel.add(textField, BorderLayout.CENTER);
		this.add(textPanel);
		this.add(confirmPane);
		this.setLayout(layout);
		this.setVisible(true);
	}

}
