package client.frame.utility;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import client.frame.Theme;

public class RLabel extends javax.swing.JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RLabel(String name) {
        this.setText(name);
        this.setFont(Theme.FONT1);
        this.setBackground(Theme.COLOR0);
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
