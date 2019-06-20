package client.frame.ui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.LineBorder;

public class RoundedRecBorder extends LineBorder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1733648128327385862L;
	Color color;
	int soft;
	int thickness;
	Shape outer;
	Shape inner;

	public RoundedRecBorder(Color color, int thickness, int soft) {
		/**
		 * @param color
		 *            边框颜色
		 * @param thickness
		 *            边框厚度
		 * @param softnum
		 *            圆角半径
		 */
		super(color, thickness);
		this.soft = soft;
		this.color = color;
		this.thickness = thickness;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {


		outer = new RoundRectangle2D.Float(x, y, width, height, soft, soft);//外边框形状
		inner = new RoundRectangle2D.Float(x + thickness, x + thickness, width - 2 * thickness, height - 2 * thickness,
				soft - thickness, soft - thickness);//内边框形状
		
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(this.lineColor);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
		Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
		path.append(outer, false);
		path.append(inner, false);
		g2d.fill(path);//填充边框
		c.getGraphics().setClip(null);//清除组件原来的剪辑区域
		//System.out.println("--原始剪辑区域："+c.getGraphics().getClip());
		c.getGraphics().setClip(inner);//重设原来的剪辑区域，内边框形状之外的部分失效
		//System.out.println("--剪辑区域："+c.getGraphics().getClip());
	}
	
}