package client.frame.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import client.frame.Theme;

public class ListScrollPanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	/** 添加列表中的成员数量时可能需要改变 */
	private int height = 0;
	JPanel p;

	public ListScrollPanel() {
		super();
		p = new JPanel();
		int width = super.getWidth();
		p.setPreferredSize(new Dimension(width, getHeight()));
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBackground(Theme.COLOR0);
		this.add(p);
		this.setViewportView(p);
		// 设置垂直滚动条的显示: 一直显示
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// 样式
		JScrollBar bar = new JScrollBar();
		bar.setUI(new client.frame.ui.ScrollBarUI());
		this.setVerticalScrollBar(bar);
		this.setBorder(null);
		bar.setBackground(Theme.COLOR1);

	}

	public int getPHeight() {
		return height;
	}

	public void setPHeight(int height) {
		this.height = height;
	}

	public void addNewMember(String name) {

		p.add(new MemberButton(name));
		height += MemberButton.MEMBERBUTTON_HEIGHT;
		int width = super.getWidth();
		standardHeight(super.getPreferredSize());
		/*测试信息*/
//		System.out.println("scroll"+super.getPreferredSize().height);
//		System.out.println("height"+this.height);
		p.setPreferredSize(new Dimension(width, height));
	}
	public void deductMember(String name) {
		for(int i=p.getComponentCount();i>0;i--) {
			MemberButton temp=(MemberButton) p.getComponent(i);
			if(temp.getText().equals(name)) {
				p.remove(i);
				height-=MemberButton.MEMBERBUTTON_HEIGHT;
				
			}
			int width = super.getWidth();
			standardHeight(super.getPreferredSize());
			
			p.setPreferredSize(new Dimension(width, height));
			}
	}
	public void standardHeight(Dimension d) {
		if(p.getComponentCount()<(int) (d.height/MemberButton.MEMBERBUTTON_HEIGHT))
			this.height=d.height+10;
		/*测试信息*/
//		System.out.println("count"+p.getComponentCount());
	}
	@Override
	public void paint(Graphics g) {
	
		g.setColor(Color.black);
		g.drawRect(0, 0, super.getWidth()+1,super.getHeight());
		
		super.paint(g);
	}
}
