package client.frame.utility;

import javax.swing.JOptionPane;

public class FileDownload extends javax.swing.JOptionPane {
	public FileDownload() {
		super();
		this.showConfirmDialog(null, "下载文件？", "", JOptionPane.YES_NO_OPTION);
	}
}
