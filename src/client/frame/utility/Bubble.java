package client.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.frame.Theme;

public class Bubble extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel words;
	private RButton rbutton;
	
	public Bubble(String words, Type type) {
		this.words = new JLabel(words);
		this.setLayout(new BorderLayout());
		if (type == Type.FILE) {
			this.words.setSize(150, 0);
		} else {
			this.words.setSize(300, 0);
		}
		this.JlabelSetText(this.words, words);
		//System.out.println(this.words.getText());
		this.rbutton = new RButton(this.words.getText());
		//this.add(this.words);
		this.add(rbutton, BorderLayout.WEST);
		this.setOpaque(true);
		this.setBackground(Theme.COLOR0);
		this.setVisible(true);
	}
	
	/** 文字换行 */
	public void JlabelSetText(JLabel jLabel, String longString) {
        StringBuilder builder = new StringBuilder("<html>");
        char[] chars = longString.toCharArray();
        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        int start = 0;
        int len = 0;
        while (start + len < longString.length()) {
            while (true) {
                len++;
                if (start + len > longString.length())break;
                if (fontMetrics.charsWidth(chars, start, len) 
                        > jLabel.getWidth()) {
                    break;
                }
            }
            builder.append(chars, start, len-1).append("<br/>");
            start = start + len - 1;
            len = 0;
        }
        builder.append(chars, start, longString.length()-start);
        builder.append("</html>");
        jLabel.setText(builder.toString());
        //Button.setText(builder.toString());
    }
	
	@Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        int h = getHeight();
        int w = getWidth();
        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,
                h - 1, 10, 10);
        Shape clip = g2d.getClip();
        g2d.clip(r2d);
        g2d.fillRect(0, 0, w, h);
        g2d.setClip(clip); 
        g2d.drawRoundRect(1, 1, w - 3, h - 3, 4, 4);
        g2d.dispose();
        super.paintComponent(g);
    }

}
