package client.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Delayed;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;

import event.SubscribeEvent;;

public class ProgressPanel extends JPanel implements ActionListener, ChangeListener {
	private JProgressBar progressbar;
	private Timer timer;
	private JButton b;
	private boolean transferCompleted = false;
	private JLabel label = new JLabel("下载文件");
	private int downloadTime = 0;
	private TransferType type;

	public ProgressPanel(int timer, TransferType type) {
		// this.setBounds(50, 50, 100, 100);
		this.downloadTime = timer;
		this.type = type;
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
		this.timer = new Timer(timer, this);
		this.setLayout(new BorderLayout());
		this.add(this.label, BorderLayout.WEST);
		this.add(progressbar, BorderLayout.CENTER);
		if (type == TransferType.DOWNLOAD) {
			this.add(b, BorderLayout.EAST);
			this.setVisible(false);
		} else if (type == TransferType.UPLOAD) {
			this.setVisible(true);
		}
		

	}

	/** 滚动进度条 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b) {
			if (this.b.getText().equals("开始") || this.b.getText().equals("继续")) {
				this.timer.start();
				this.b.setText("暂停");
			} else if (this.b.getText().equals("删除")) {
				this.b.setText("开始");
				this.label.setText("下载文件");
				this.progressbar.setValue(0);
				this.timer = new Timer(this.downloadTime, this);
			} else {
				this.timer.stop();
				this.b.setText("继续");
			}
		}
		if (e.getSource() == timer) {
			int value = this.progressbar.getValue();
			if (value < 100)
				this.progressbar.setValue(++value);
			else {
				if (this.type == TransferType.UPLOAD) {
					this.label.setText("上传完成");
					this.setVisible(false);
				}
				// 测试用
				this.transferCompleted = true;
				// 应调取事件后响应
				this.label.setText("下载完成");
				this.b.setText("删除");
				if (this.transferCompleted) {
					this.timer.stop();
				}
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void setVisible(boolean aFlag) {
		// TODO Auto-generated method stub
		super.setVisible(aFlag);
		if (aFlag) {
			this.timer.start();
		}
	}
	
}

enum TransferType {
	UPLOAD, DOWNLOAD
}