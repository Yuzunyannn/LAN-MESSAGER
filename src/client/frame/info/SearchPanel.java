package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import client.event.EventIPC;
import client.event.EventSearch;
import client.event.EventsBridge;
import client.frame.MainFrame;
import client.frame.Theme;
import event.IEventBus;

public class SearchPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private TextField search;
	private KeyListener keyListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == '\n')
				EventsBridge.frontendEventHandle.post(new EventSearch(search.getText()));

		}
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 自动生成的方法存根

		}

	};

	public SearchPanel() {
		this.setLayout(null);
		this.setMinimumSize(new Dimension(MainFrame.INFO_RIGION_WIDTH, 30));
		this.setMaximumSize(new Dimension(MainFrame.INFO_RIGION_WIDTH, 30));
		this.setBackground(Theme.COLOR2);
		search = new TextField();
		search.setFont(Theme.FONT3);
		search.setFocusable(false);
		//鼠标点击事件
		search.addMouseListener(new MouseListener() 
		{
			boolean entry=false;
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				search.setFocusable(entry);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				entry=true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				entry=false;
			};
		});
		//焦点获得/失去
		search.addFocusListener(new FocusListener(){
			public void focusLost(FocusEvent e) {
			//失去焦点执行的代码
				boolean a=search.isFocusOwner();
				System.out.println(a+"search失去焦点");
			}
			public void focusGained(FocusEvent e) {
			//获得焦点执行的代码
				boolean a=search.isFocusOwner();
				System.out.println(a+"search获得焦点");
				EventsBridge.frontendEventHandle.post(new EventIPC(EventIPC.SEARCH));
			}
		});
		this.add(search);
		JButton b1 = new JButton();
		JButton set = new JButton();
		this.add(b1);
		this.add(set);
		String info = "单行输入";
		search.addKeyListener(keyListener);
		search.setForeground(Color.GRAY);
		search.setSize(150, 20);
		search.setLocation(5, 8);
		search.setText(info);
		search.addFocusListener(new DefaultFocusListener(info, search));
		b1.setSize(20, 20);
		b1.setLocation(180, 8);
		set.setSize(20, 20);
		set.setLocation(220, 8);

	}

	public void initEvent(IEventBus bus) {
		bus.register(this);
	}

}

class DefaultFocusListener implements FocusListener {
	private String info;
	private TextField textfield;

	public DefaultFocusListener(String info, TextField textfield) {
		this.info = info;
		this.textfield = textfield;
	}

	@Override
	public void focusGained(FocusEvent e) {
		String temp = textfield.getText();
		if (temp.equals(info)) {
			textfield.setText(null);
			textfield.setForeground(Color.BLACK);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		String temp = textfield.getText();
		if (temp.equals("")) {
			textfield.setForeground(Color.GRAY);
			textfield.setText(info);
		}

	}
	
}