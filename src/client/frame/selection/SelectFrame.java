package client.frame.selection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import client.frame.Theme;

public class SelectFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SelectPane selectPane;
	private int type;
	private JTextField searchField = new JTextField("搜索用户");
	private JButton revertButton = new JButton("全选");
	private JPanel searchPanel = new JPanel();
	private int selectHeight = 440;
	private List<String> selectUsers = new ArrayList<String>();
	static private List<String> selectedList = new ArrayList<String>();
	static private boolean choosable = false;
	public static final int CHOOSE = 0;
	public static final int VIEW = 1;
	public boolean entry = false;

	private LayoutManager layout = new LayoutManager() {

		@Override
		public void removeLayoutComponent(Component comp) {

		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return null;
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return null;
		}

		@Override
		public void layoutContainer(Container parent) {
			Component[] cons = parent.getComponents();
			int height = parent.getHeight();
			int width = parent.getWidth();

			Component selectList = cons[1];
			selectList.setLocation(0, height - selectHeight - 1);
			selectList.setSize(width, selectHeight);

			Component searchArea = cons[0];
			searchArea.setLocation(0, 0);
			searchArea.setSize(width, height - selectHeight);
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {

		}
	};

	public SelectPane getSelectPane() {
		return selectPane;
	}

	public SelectFrame(List<String> users, String title, int type) {
		selectedList.clear();
		for (String str : users) {
			if (str.indexOf("#G") != 0) {
				selectUsers.add(str);
			}
		}
		revertButton.setFocusPainted(false);
		this.setVisible(false);
		if (users.size() != 1) {
			this.setTitle(title);
			this.setSize(400, 500);
			this.setResizable(false);
			this.setAlwaysOnTop(true);
			this.setContentPane(new JPanel());
			this.type = type;
			if (this.type == VIEW) {
				revertButton.setText("显示全部");
			}
			choosable = false;
			selectPane = new SelectPane(selectUsers, type);
			searchField.setSize(new Dimension(100, 40));
			searchField.setForeground(Theme.COLOR9);
			searchField.setFocusable(false);
			searchField.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					searchField.setFocusable(entry);
					searchField.requestFocus();
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					entry = true;
				}

				@Override
				public void mouseExited(MouseEvent e) {
					entry = false;
				};
				
			});
			searchField.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {

				}

				@Override
				public void focusGained(FocusEvent e) {
					if (searchField.getText().equals("搜索用户")) {
						searchField.setForeground(Color.BLACK);
						searchField.setText("");
					}
				}
			});
			searchField.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (searchField.getText().equals("")) {

					} else {
						System.out.println("changed!");
					}

				}
			});
			Document document = searchField.getDocument();
			document.addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					if (searchField.getText().equals("") && type != VIEW) {
						revertButton.setText("全选");
					} else {
						List<String> u = new ArrayList<String>();
						for (String string : selectUsers) {
							if (string.indexOf(searchField.getText()) != -1) {
								u.add(string);
							}
						}
						if (type == VIEW) {
							selectPane.updatePanel(u, VIEW);
						} else {
							selectPane.updatePanel(u, CHOOSE);
						}

					}
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					revertButton.setText("显示全部");
					List<String> u = new ArrayList<String>();
					for (String string : selectUsers) {
						if (string.indexOf(searchField.getText()) != -1) {
							u.add(string);
						}
					}
					if (type == VIEW) {
						selectPane.updatePanel(u, VIEW);
					} else {
						selectPane.updatePanel(u, CHOOSE);
					}
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					System.out.println("change!");
				}
			});
			revertButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (revertButton.getText().equals("显示全部")) {
						searchField.setText("搜索用户");
						searchField.setForeground(Theme.COLOR9);
						if (type == VIEW) {
							selectPane.updatePanel(selectUsers, VIEW);
						} else {
							selectPane.updatePanel(selectUsers, CHOOSE);
							revertButton.setText("全选");
						}

					} else if (revertButton.getText().equals("全选")) {
						for (String string : selectUsers) {
							if (!selectedList.contains(string)) {
								selectedList.add(string);
							}
						}
						selectPane.checkAll(true);
						revertButton.setText("取消全选");
					} else if (revertButton.getText().equals("取消全选")) {
						selectedList.clear();
						selectPane.checkAll(false);
						revertButton.setText("全选");
					}

				}
			});
			revertButton.setBackground(Color.WHITE);
			revertButton.requestFocus();
			searchPanel.setLayout(new BorderLayout());
			searchPanel.add(searchField, BorderLayout.CENTER);
			searchPanel.add(revertButton, BorderLayout.EAST);
		} else {
			this.setTitle("个人信息");
			this.setResizable(false);
			selectHeight = 100;
			this.setSize(200, 120);
			title = "个人信息";
			selectPane = new SelectPane(users, type);
			searchField.setVisible(false);
		}
		this.add(searchPanel);
		this.add(selectPane);
		this.setLayout(layout);
		// 居中显示窗体
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		// 得到窗体的宽、高
		int windowsWidth = this.getWidth();
		int windowsHeight = this.getHeight();
		// System.out.println(windowsWidth+","+windowsHeight);
		this.setBounds((width - windowsWidth) / 2, (height - windowsHeight) / 2, windowsWidth, windowsHeight);
	}

	/** 获取被选中的用户们（在获取前一定要判断choosable） */
	static public List<String> getSelectedList() {
		return selectedList;
	}

	/** 判断selectedList能否被获取 */
	static public boolean getChoosable() {
		return choosable;
	}

	/** 设置selectedList能否被获取 */
	public static void setChoosable(boolean choosable) {
		SelectFrame.choosable = choosable;
	}
}
