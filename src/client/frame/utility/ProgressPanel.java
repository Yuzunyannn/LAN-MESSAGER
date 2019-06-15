package client.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
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
import javax.swing.plaf.ButtonUI;;

public class ProgressPanel extends JPanel implements ActionListener, ChangeListener {
	private JProgressBar progressbar;
	private Timer timer;
	private JButton b;
	private boolean transferCompleted = false;

	public ProgressPanel(int timer) {
		//this.setBounds(50, 50, 100, 100);
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
		b = new JButton("开始");
		b.setForeground(Color.blue);
		b.addActionListener(this);
		this.timer = new Timer(timer, this);
		this.setLayout(new BorderLayout());
		this.add(new JLabel("下载文件"), BorderLayout.WEST);
		this.add(progressbar, BorderLayout.CENTER);
		this.add(b, BorderLayout.EAST);
		// this.add(label, BorderLayout.NORTH);
		// frame.pack();

	}

	/** 滚动进度条 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b) {
			if (b.getText().equals("开始")) {
				timer.start();
				b.setText("暂停");
			} else {
				timer.stop();
				b.setText("开始");
			}
		}
		if (e.getSource() == timer) {
			int value = progressbar.getValue();
			if (value < 100)
				progressbar.setValue(++value);
			else {
				// 测试用
				transferCompleted = true;
				if (transferCompleted) {
					timer.stop();
				}
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}
}
