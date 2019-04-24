package client.frame.info;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import client.frame.Theme;

/** 界面左边的区域 用户区域 */
public class InfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ListScrollPanel memberField;
	private SearchPanel searchField;
	private UserPanel userField;
	public InfoPanel() {
		this.setBackground(Theme.COLOR2);
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		 userField=new UserPanel();
		 searchField=new SearchPanel();
		memberField=new ListScrollPanel();
		this.add(userField);
		this.add(searchField);
		this.add(memberField);
		String[] members=new String[]{"1","1","1"};
//		String[] members=new String[]{"1","1","1","1","1","1","1","1","1","1","1","1","1"};
		this.addMembers(members);
//		this.add(searchField,BorderLayout.CENTER);
		userField.setPreferredSize(new Dimension(0,80));
		searchField.setPreferredSize(new Dimension(0,50));
		

	}
	/*暂时作为测试*/
	public void addMembers(String[] members) {
		for(int i=0;i<members.length;i++) {
			memberField.addNewMember(members[i]);
		}
	}
}
