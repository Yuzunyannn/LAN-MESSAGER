package client.frame.selection;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.List;

public class SelectGroupFrame extends SelectFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int confirmHeight = 70;
	private ConfirmPane confirmPane = new ConfirmPane();
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

			Component confirmArea = cons[1];
			confirmArea.setLocation(0, height - confirmHeight - 1);
			confirmArea.setSize(width, confirmHeight);

			Component selectList = cons[0];
			selectList.setLocation(0, 0);
			selectList.setSize(width, height - confirmHeight);
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
			// TODO Auto-generated method stub

		}
	};
	
	public SelectGroupFrame(List<String> users, String title) {
		super(users, title, SelectFrame.CHOOSE);
		// TODO Auto-generated constructor stub
		this.getContentPane().add(confirmPane);
		this.getContentPane().setLayout(this.layout);
		
	}

}
