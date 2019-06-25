package client.frame.selection;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import client.frame.Theme;

public class ConfirmPane extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton confirm = new JButton("确认");
	private JButton cancel = new JButton("取消");
	private MouseListener listen = new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			System.out.println(SelectFrame.getSelectedList().size());
			if (e.getSource() == confirm) {
				if (SelectFrame.getSelectedList().isEmpty()) {
					SelectFrame.setChoosable(false);
				} else {
					SelectFrame.setChoosable(true);
				}
				((SelectFrame)getParent().getParent().getParent().getParent()).dispose();
			} else if (e.getSource() == cancel) {
				SelectFrame.getSelectedList().clear();
				SelectFrame.setChoosable(false);
				((SelectFrame)getParent()).dispose();
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
	};

	public ConfirmPane() {
		// TODO Auto-generated constructor stub
		this.setBackground(Theme.COLOR0);
		confirm.setBackground(null);
		cancel.setBackground(null);
		confirm.addMouseListener(listen);
		cancel.addMouseListener(listen);
		this.add(confirm);
		this.add(cancel);
		this.setVisible(true);
	}

}
