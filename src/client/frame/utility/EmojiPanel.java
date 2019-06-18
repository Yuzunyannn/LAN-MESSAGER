package client.frame.utility;

import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import jdk.internal.org.objectweb.asm.Label;
import resmgt.UserResource;;

public class EmojiPanel extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JScrollBar scrollBar;
	
	public EmojiPanel() {
		// TODO Auto-generated constructor stub
		super(new JPanel());
		this.contentPanel = (JPanel) ((JViewport) this.getComponent(0)).getComponent(0);
		this.scrollBar = this.getHorizontalScrollBar();
		this.contentPanel.setLayout(new GridLayout(6,6));
		for (int i = 0; i < 36; i++) {
			JButton btn = new JButton();
			ImageIcon imageIcon = new ImageIcon("src/resources/img/memes/1.jpg");
			imageIcon.setImage(imageIcon.getImage().getScaledInstance(40, 40, 40));
			btn.setIcon(imageIcon);
			this.contentPanel.add(btn);
		}
		this.contentPanel.setVisible(true);
	}
}
