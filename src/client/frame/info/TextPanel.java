package client.frame.info;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.frame.Theme;
import resmgt.ResourceManagement;

public class TextPanel extends JPanel {
	public static final  int TEXTFIELD_WIDTH=190;
	public static final  int TEXTFIELD_HEIGHT=36;
private JButton button;
private JTextField textField;
public boolean isSearch=false;
public static ImageIcon icon_cross = new ImageIcon(
		ResourceManagement.instance.getPackResource("img/cross.png").getImage());
	public TextPanel() {
		super();
	int w=190;
	int h=36;
	this.setLayout(null);
	textField=new JTextField("aacs");
	button=new JButton();
	button.addMouseListener(new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("点击按钮");
		}
	});
	textField.setBounds(3, 3, w-26, h-6);
	button.setBounds( w-26, 3, 25, h-6);
	button.setBackground(Color.white);
	textField.setBackground(Theme.COLOR4);
	this.setBackground(Theme.COLOR5);
	this.add(button);
	button.setBorder(null);
	button.setOpaque(false);
	textField.setOpaque(true);
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
		g2d.setColor(textField.getBackground());
		g2d.fillRoundRect(0, 0, w-3, h, 3, 3);
		g2d.setColor(Color.RED);
		if(isSearch)
		g2d.drawImage(icon_cross.getImage(), w-23,h/2-8 ,16,16, null);
//		g2d.fillOval( w-23, h/2-10, 20, 20);
		g2d.setColor(tmp);
	}
	public JTextField getTextField() {
		return textField;
	}
	public JButton getButton() {
		return button;
	}
}
