package client.frame.selection;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import client.frame.Theme;
import resmgt.UserResource;

public class SelectPane extends JScrollPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JScrollBar scrollBar = this.getVerticalScrollBar();

	public SelectPane(List<String> users, int type) {
		// TODO Auto-generated constructor stub
		super(new JPanel());
		panel = (JPanel) ((JViewport) this.getComponent(0)).getComponent(0);
		scrollBar.setUI(new client.frame.ui.ScrollBarUI());
		this.panel.setLayout(new GridBagLayout());
		addItems(users, type);
	}

	private void addItems(List<String> users, int type) {
		JPanel tmp;
		JCheckBox checkBox;
		JButton button;
		JLabel label;
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.HORIZONTAL;
		con.insets = new Insets(0, 0, 0, 0);
		con.gridx = 0;
		con.weightx = 100;
		con.ipady = 40;
		int row = 0;
		for (String string : users) {
			con.gridy = row;
			tmp = new JPanel();
			tmp.setLayout(new BorderLayout());
			label = new JLabel(string);
			if (type == SelectFrame.VIEW) {
				button = new JButton();
				button.setIcon(UserResource.getHeadIcon(string, UserResource.HeadIconSize.SMALL));
				tmp.add(button, BorderLayout.EAST);
			} else if (type == SelectFrame.CHOOSE) {
				checkBox = createCheckBox(string);
				if (SelectFrame.getSelectedList().contains(string)) {
					checkBox.setSelected(true);
				}
				tmp.add(checkBox, BorderLayout.EAST);
			}
			tmp.add(label, BorderLayout.WEST);
			tmp.setBorder(BorderFactory.createLineBorder(Theme.COLOR6));
			panel.add(tmp, con);
			row++;
		}
	}

	public void updatePanel(List<String> users, int type) {
		panel.removeAll();
		this.addItems(users, type);
		panel.revalidate();
		panel.repaint();
	}

	public void checkAll(boolean isTrue) {
		Component[] cons = panel.getComponents();
		for (Component component : cons) {
			if (component instanceof JPanel) {
				Component[] subCons = ((JPanel) component).getComponents();
				((JCheckBox) subCons[0]).setSelected(isTrue);
			}
			((JPanel)component).revalidate();
			((JPanel)component).repaint();

		}
		panel.revalidate();
		panel.repaint();

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
