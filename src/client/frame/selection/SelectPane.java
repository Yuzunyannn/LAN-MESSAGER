package client.frame.selection;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridBagLayoutInfo;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneLayout;

import client.frame.Theme;
import client.frame.ui.ScrollBarUI;



public class SelectPane extends JScrollPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JScrollBar scrollBar = this.getVerticalScrollBar();

	public SelectPane(List<String> users) {
		// TODO Auto-generated constructor stub
		super(new JPanel());
		panel = (JPanel) ((JViewport) this.getComponent(0)).getComponent(0);
		scrollBar.setUI(new client.frame.ui.ScrollBarUI());
		JPanel tmp;
		JCheckBox checkBox;
		JLabel label;
		this.panel.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.HORIZONTAL;
		con.insets = new Insets(0,0,0,0);
		con.gridx = 0;
		con.weightx = 100;
		con.ipady = 40;
		int row = 0;
		for (String string : users) {
			con.gridy = row;
			tmp = new JPanel();
			tmp.setLayout(new BorderLayout());
			label = new JLabel(string);
			checkBox = createCheckBox(string);
			tmp.add(label, BorderLayout.WEST);
			tmp.add(checkBox, BorderLayout.EAST);
			tmp.setBorder(BorderFactory.createLineBorder(Theme.COLOR6));
			panel.add(tmp, con);
			row++;
		}

	}

	// 这个方法用于动态的生成JCheckBox
	private JCheckBox createCheckBox(String name) {
		JCheckBox cb = new JCheckBox("");
		// 给JCheckBox对象注册事件监听，也可以去监听其它事件，比如鼠标事件什么的
		cb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox cb = (JCheckBox) e.getSource();
				if (cb.isSelected()) {
					if (!SelectFrame.getSelectedList().contains(name)) {
						SelectFrame.getSelectedList().add(name);
					}
				}
			}
		});
		return cb;
	}

}
