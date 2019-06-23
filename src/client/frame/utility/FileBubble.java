package client.frame.utility;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class FileBubble extends Bubble {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton fileButton;
	private ProgressPanel progress;
	private ImageIcon imageIcon;
	private MouseAdapter mouse = new MouseAdapter() {
		public boolean hasClicked = false;

		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			if (this.hasClicked) {
				progress.setVisible(false);
				hasClicked = false;
				//e.getComponent().getParent().revalidate();
				System.out.println("removed");
			} else {
				progress.setVisible(true);
				//e.getComponent().getParent().revalidate();
				hasClicked = true;
				System.out.println("added");
			}

		};
	};

	public FileBubble(String name, boolean isMySelf) {
		super(name, BubbleType.FILE);
		if (this.checkFileType(name) == BubbleType.FILE) {
			imageIcon = new ImageIcon("src/resources/img/icons/" + "icon-document.jpg");
			imageIcon.setImage(imageIcon.getImage().getScaledInstance(40, 40, 40));
			fileButton = new JButton();
			fileButton.setIcon(imageIcon);
			fileButton.setBorderPainted(false);
			this.add(fileButton, BorderLayout.EAST);
			if (isMySelf) {
				progress = new ProgressPanel(100, TransferType.UPLOAD);
			} else {
				progress = new ProgressPanel(100, TransferType.DOWNLOAD);
			}			
			progress.setSize(this.getSize());
			this.add(progress, BorderLayout.SOUTH);
			this.fileButton.addMouseListener(this.mouse);
		} else {
			System.out.println("暂不支持图标显示的文件类型");
		}
//		if (name.indexOf(".doc") != -1 || name.indexOf(".txt") != -1 || name.indexOf(".docx") != -1) {
//			
//		} 

	}

}
