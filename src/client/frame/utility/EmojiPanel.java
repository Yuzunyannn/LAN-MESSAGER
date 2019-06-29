package client.frame.utility;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import client.event.EventSendPicture;
import client.event.EventsBridge;
import resmgt.UserResource;;

public class EmojiPanel extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JScrollBar scrollBar = this.getHorizontalScrollBar();
	
	public EmojiPanel(String toUser) {
		// TODO Auto-generated constructor stub
		super(new JPanel());
		this.contentPanel = (JPanel) ((JViewport) this.getComponent(0)).getComponent(0);
		this.contentPanel.setLayout(new GridLayout(3,3));
		for (int i = 0; i < 9; i++) {
			JButton btn = new JButton();
			btn.setBackground(Color.white);
			btn.setIcon(UserResource.getMeme("meme-1"));
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if (e.getSource() instanceof JButton) {
						System.out.println("SUCCESS");
						EventsBridge.frontendEventHandle.post(new EventSendPicture("1.jpg", BubbleType.MEME, toUser) );
						getParent().getParent().getParent().setVisible(false);
					} else {
						System.out.println("Fail");
					}
				}
			});
			this.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					System.out.println("pressed");
					if (e.getX()<getX() || e.getX()>(getX()+getWidth())) {
						getParent().getParent().setVisible(false);
					}
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			this.contentPanel.add(btn);
		}
		
		this.contentPanel.setVisible(true);
	}

	public JScrollBar getScrollBar() {
		return scrollBar;
	}

	public void setScrollBar(JScrollBar scrollBar) {
		this.scrollBar = scrollBar;
	}
}
