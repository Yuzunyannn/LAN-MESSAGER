package client.frame.info;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextPanel extends JPanel {
private JButton button;
private JTextField textField;
	public TextPanel() {
		super();
	int w=	400;
	int h=400;
	Point p=this.getLocation();
	this.setLayout(null);
	textField=new JTextField("aacs");
	button=new JButton();
	button.addMouseListener(new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("点击按钮");
		}
	});
	textField.setBounds(0, 0, w-20, h-30);
	button.setBounds( w-20, 0, 20, h-30);
	button.setBackground(Color.white);
	textField.setBackground(Color.yellow);
	this.setBackground(Color.gray);
//	this.add(button);
	button.setOpaque(false);
	textField.setOpaque(false);
	textField.setBorder(null);
	this.add(textField);

}
	@Override 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d=(Graphics2D)g.create();
		Color tmp=g2d.getColor();
		int w=	this.getWidth();
		int h=this.getHeight();
		g2d.setColor(Color.green);
		g2d.fillRoundRect(0, 0, w+3, h, 3, 3);
		g2d.setColor(Color.RED);
		g2d.fillOval(w-20, h/2-10, 20, 20);
		g2d.setColor(tmp);
	}
	public JTextField getTextField() {
		return textField;
	}
	public static void main (String[] args) {
		JFrame f=new JFrame();
		f.setLayout(null);
		TextPanel t=new TextPanel();
		f.add(t);
		t.setBounds(100, 100, 400, 400);
		f.setSize(500, 500);
		f.setVisible(true);
	}
}
