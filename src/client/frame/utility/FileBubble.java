package client.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;

import client.record.Record;
import log.Logger;
import resmgt.UserResource;
import transfer.EventFile;

public class FileBubble extends Bubble {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton fileButton;
	private ProgressPanel progress;
	public String fileTmpID = "";
	private MouseAdapter mouse = new MouseAdapter() {
		public boolean hasClicked = false;

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			if (progress.getType() == TransferType.DOWNLOAD) {
				try {
					Desktop.getDesktop().open(new File("./tmp/" + fileTmpID));
				} catch (IOException e1) {
					Logger.log.warn("打开下载文件失败", e1);
				}

			} else {
				if (this.hasClicked) {
					progress.setVisible(false);
					hasClicked = false;
					System.out.println("removed");
				} else {
					progress.setVisible(true);
					hasClicked = true;
					System.out.println("added");
				}
			}
		}
	};

	public FileBubble(String name, boolean isMySelf) {
		super(name, BubbleType.FILE);
		int index = name.indexOf(Record.FILEEND);
		if (index > 0) {
			fileTmpID = name.substring(0, index);
		}
		// if (Bubble.checkFileType(name) == BubbleType.FILE) {
		fileButton = new JButton();
		fileButton.setIcon(UserResource.getSysIcon("icon-document"));
		fileButton.setBackground(Color.white);
		fileButton.setBorderPainted(false);
		this.add(fileButton, BorderLayout.EAST);
		if (isMySelf) {
			progress = new ProgressPanel(TransferType.UPLOAD);
		} else {
			progress = new ProgressPanel(TransferType.DOWNLOAD);
		}
		progress.setSize(this.getSize());
		progress.setVisible(false);
		this.add(progress, BorderLayout.SOUTH);
		this.fileButton.addMouseListener(this.mouse);
		// } else {
		// System.out.println("暂不支持图标显示的文件类型");
		// }
	}

	public void toggleProgressBar(EventFile e) {
		this.progress.setVisible(true);
		this.progress.toggleProgress(e);
	}

}
