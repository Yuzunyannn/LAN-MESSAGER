package client.frame.info;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import client.frame.Theme;
import log.Logger;

public class ListScrollPanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	/** 添加列表中的成员数量时可能需要改变 */
	private int height = 0;
	private JPanel p;

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
	/*在加入User类后需要修改*/
	public void setTop(String name) {
		int temp;
		temp=this.getMember(name);
		Component tempbutton;
		tempbutton=p.getComponent(temp);
		
		Component[]  re;
		re=p.getComponents();
		for(int i=temp;i>0;i--) {
			re[i]=re[i-1];
		}
		re[0]=tempbutton;
		p.removeAll();
		for(Component i:re)
		p.add(i);
		
	
//		
		for(Component i:p.getComponents())
			 System.out.println(((MemberButton)i).getMemberName());
		
	}
	public int getMember(String name) {
		MemberButton temp;
		for(int i=0;i<p.getComponentCount();i++) {
			temp=(MemberButton) p.getComponent(i);
		 if(temp.getMemberName().equals(name)) {
//			 System.out.println(i);
			 return i;
			 
		 }
		 }
		return -1;
		
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
			else 
				Logger.log.error(name+"查无此人");
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

}
