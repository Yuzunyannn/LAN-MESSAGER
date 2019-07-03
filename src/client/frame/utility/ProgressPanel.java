package client.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import client.frame.Theme;
import transfer.EventFile;

public class ProgressPanel extends JPanel implements ActionListener, ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar progressbar;
	private Timer timer;
	private JButton b;
	// private boolean transferCompleted = false;
	private JLabel label = new JLabel("下载文件");
	private TransferType type;
	private EventFile e;

	/** 获取传输类型 */
	public TransferType getType() {
		return type;
	}

	/** 获取文件传输事件 */
	public EventFile getE() {
		if (e.getProgress() == 1) {
			return e;
		}
		return null;
	}

	public ProgressPanel(TransferType type) {
		// this.setBounds(50, 50, 100, 100);
		// this.downloadTime = timer;
		this.type = type;
		this.setBackground(Theme.COLOR0);
		progressbar = new JProgressBar();
		progressbar.setOrientation(JProgressBar.HORIZONTAL);
		progressbar.setMinimum(0);
		progressbar.setMaximum(100);
		progressbar.setValue(0);
		progressbar.setStringPainted(true);
		progressbar.addChangeListener(this);
		progressbar.setPreferredSize(new Dimension(100, 20));
		progressbar.setBorderPainted(true);
		progressbar.setBackground(Color.pink);
		// JPanel panel = new JPanel();
		if (this.type == TransferType.UPLOAD) {
			this.label.setText("上传文件");
		}
		b = new JButton("开始");
		b.setForeground(Color.blue);
		b.addActionListener(this);
		this.timer = new Timer(0, this);
		this.setLayout(new BorderLayout());
		this.add(this.label, BorderLayout.WEST);
		this.add(progressbar, BorderLayout.CENTER);
		if (type == TransferType.DOWNLOAD) {
			this.add(b, BorderLayout.EAST);
			this.b.setVisible(false);
			this.setVisible(true);
		} else if (type == TransferType.UPLOAD) {
			this.setVisible(true);
		}

	}

	/** 设置进度条，应调取事件后响应 */
	public void toggleProgress(EventFile e) {
		this.e = e;
		((FileBubble)getParent()).fileTmpID = e.getTempName();
		this.timer.start();
	}

	/** 滚动进度条 */
	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == b) {
//			if (this.b.getText().equals("开始") || this.b.getText().equals("继续")) {
//				this.timer.start();
//				this.b.setText("暂停");
//			} else if (this.b.getText().equals("删除")) {
//				this.b.setText("开始");
//				this.label.setText("下载文件");
//				this.progressbar.setValue(0);
//				this.timer = new Timer(this.downloadTime, this);
//			} else {
//				this.timer.stop();
//				this.b.setText("继续");
//			}
//		}
		if (e.getSource() == timer) {
			int value = this.progressbar.getValue();
			if (value < this.e.getProgress() * 100)
				this.progressbar.setValue(++value);
			else if (this.e.getProgress() == 1) {
				this.progressbar.setValue(100);
				if (this.type == TransferType.UPLOAD) {
					this.label.setText("上传完成");
					this.setVisible(false);
				} else if (this.type == TransferType.DOWNLOAD) {
					this.label.setText("下载完成");
					this.b.setText("删除");
				}
				this.timer.stop();
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
	}

}

enum TransferType {
	UPLOAD, DOWNLOAD
}