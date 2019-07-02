package client.frame.info;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashSet;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import client.frame.Theme;
import event.IEventBus;
import log.Logger;
import user.UOnline;
import user.User;

public class BaseScrollPanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	
	/** 添加列表中的成员数量时可能需要改变 */
	private int height = 0;
	protected JPanel p;
	protected Component[] content;
	private int fixed = 0;
	static protected HashSet<User> userSet= new HashSet<User>();
	public BaseScrollPanel() {
		super();
		p = new JPanel();
		int width = super.getWidth();
		p.setPreferredSize(new Dimension(width, getHeight()));
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBackground(Theme.COLOR5);
		content = p.getComponents();
		this.add(p);
		this.setViewportView(p);
		// 设置垂直滚动条的显示: 一直显示
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// 样式
		JScrollBar bar = new JScrollBar();
		bar.setUI(new client.frame.ui.ScrollBarUI());
		this.setVerticalScrollBar(bar);
		this.setBorder(null);

	}
	public BaseScrollPanel(JPanel panel) {
		super();
		p = panel;
//		int width = super.getWidth();
//		p.setPreferredSize(new Dimension(width, getHeight()));
//		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
//		p.setBackground(Theme.COLOR5);
		content = p.getComponents();
		this.add(p);
		this.setViewportView(p);
		// 设置垂直滚动条的显示: 一直显示
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// 样式
		JScrollBar bar = new JScrollBar();
		bar.setUI(new client.frame.ui.ScrollBarUI());
		this.setVerticalScrollBar(bar);
		this.setBorder(null);

	}
	public void refresh() {
		this.revalidate();
		this.repaint();
	}
	/** 把成员置顶 */
	public void setTop(String name) {
		int temp;
		temp = getNameIndex(name);
		if (temp == -1)
			return;
		Component tempbutton;
		tempbutton = content[temp];

		for (int i = temp; i > fixed; i--) {
			content[i] = content[i - 1];
		}
		content[fixed] = tempbutton;
		Component[] re = content;
		p.removeAll();
		content = re;
		for (Component i : content) {
			p.add(i);
		}
		this.refresh();

	}
	/**设置按钮固定位置*/
	public void setFixed(String name) {
		setTop(name);
		if(fixed==0) {
			fixed = fixed + 1;
			System.out.println(((BasePanelButton)content[fixed-1]).getName()+"最新置顶");}
		else if(((BasePanelButton)content[fixed-1]).getName().equals(name)) {
		fixed = fixed + 1;
		System.out.println(((BasePanelButton)content[fixed-1]).getName()+"最新置顶");
	}
		else Logger.log.impart("已置顶  ："+name);
		
	}
	public void canelFixed(String name) {
		if (fixed == 0)
			return;
		else {
			int tmpIndex=getNameIndex(name);
			if(tmpIndex>fixed-1) {
				Logger.log.impart(name+" ： 没有被置顶");
				return;
			}
			Component temp=content[tmpIndex];
			for(int i=tmpIndex;i<fixed-1;i++) {
				content[i]=content[i+1];
			}
			fixed = fixed - 1;
			content[fixed]=temp;
			
			p.removeAll();
			for (Component i : content) {
				p.add(i);
				System.out.println(((BasePanelButton)i).getName());
			}
			this.refresh();
//			setTop(name);
	}
	}

	public int getNameIndex(String name) {
		BasePanelButton temp;
		for (int i = 0; i < content.length; i++) {
			temp = (BasePanelButton) content[i];
			if (temp.getName().equals(name)) {
				return i;

			}
		}
		return -1;

	}


	public void addNewMember( BasePanelButton userbutton) {
		if (userSet.contains(UOnline.getInstance().getUser(userbutton.getName()))) {
			Logger.log.impart("已有该成员");
			return;}
		userSet.add(UOnline.getInstance().getUser(userbutton.getName()));
		p.add(userbutton);
		userbutton.recvMessageShow();
		content = p.getComponents();
		height += BasePanelButton.BasePanelButton_HEIGHT;
		int width = super.getWidth();
		standardHeight(super.getPreferredSize());
		p.setPreferredSize(new Dimension(width, height));
	}
	
	public void deductMember(User user) {
		if(getNameIndex(user.userName)<fixed)
			canelFixed(user.userName);
		if(userSet.contains(user)) {
			p.remove(getNameIndex(user.userName));
			userSet.remove(user);
			height -= BasePanelButton.BasePanelButton_HEIGHT;
			content = p.getComponents();
			}	
		else
			Logger.log.warn("查无此成员");
		int width = super.getWidth();
		standardHeight(super.getPreferredSize());
		p.setPreferredSize(new Dimension(width, height));
	}

	public void deleteAllMember() {
		p.removeAll();
		content = p.getComponents();
	}
	public void standardHeight(Dimension d) {
		if (p.getComponentCount() < (int) (d.height / BasePanelButton.BasePanelButton_HEIGHT))
			this.height = d.height + 10;
		/* 测试信息 */
		// System.out.println("count"+p.getComponentCount());
	}
	public void initEvent(IEventBus bus) {
		bus.register(this);
	}


}
