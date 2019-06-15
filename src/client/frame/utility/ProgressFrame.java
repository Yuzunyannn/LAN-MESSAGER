package client.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JPanel;

import javax.swing.JProgressBar;

import javax.swing.Timer;

import javax.swing.event.ChangeEvent;

import javax.swing.event.ChangeListener;

import com.sun.xml.internal.ws.org.objectweb.asm.Label;

import javafx.scene.control.ProgressBar;

public class ProgressFrame implements ActionListener, ChangeListener {
	public JFrame frame = null;
	private JProgressBar progressbar;
	private JLabel label;
	private Timer timer;
	private JButton b;
	private boolean transferCompleted = false;
	public JPanel panel = new JPanel();
	
	/** 设置进度条的总时长 */
	public ProgressFrame(int timer) {
		frame = new JFrame("下载文件⏬");
		frame.setBounds(100, 100, 400, 130);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPanel = frame.getContentPane();
		label = new JLabel("", JLabel.CENTER);
		progressbar = new JProgressBar();
		progressbar.setOrientation(JProgressBar.HORIZONTAL);
		progressbar.setMinimum(0);
		progressbar.setMaximum(100);
		progressbar.setValue(0);
		progressbar.setStringPainted(true);
		progressbar.addChangeListener(this);
		progressbar.setPreferredSize(new Dimension(300, 20));
		progressbar.setBorderPainted(true);
		progressbar.setBackground(Color.pink);
		//JPanel panel = new JPanel();
		this.timer = new Timer(timer, this);
		contentPanel.add(panel, BorderLayout.SOUTH);
		contentPanel.add(progressbar, BorderLayout.CENTER);
		contentPanel.add(label, BorderLayout.NORTH);
		// frame.pack();
		b = new JButton("开始");
		b.setForeground(Color.blue);
		b.addActionListener(this);
		panel.add(b);
		frame.setVisible(true);
	}

	/** 滚动进度条 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b) {
			timer.start();
		}
		if (e.getSource() == timer) {
			int value = progressbar.getValue();
			if (value < 100)
				progressbar.setValue(++value);
			else {
				//测试用
				transferCompleted = true;
				if (transferCompleted) {
					timer.stop();
					frame.dispose();
				}
			}
		}
	}

	/** 反馈进度 */
	public void stateChanged(ChangeEvent e1) {
		int value = progressbar.getValue();
		if (e1.getSource() == progressbar) {
			label.setText("文件已下载：" + Integer.toString(value) + "%");
			label.setForeground(Color.blue);
		}
	}

}