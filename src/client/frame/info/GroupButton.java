package client.frame.info;

import java.util.List;

import user.User;

public class GroupButton extends MemberButton {
private List<User> userlist;
private User boss;
public GroupButton(String id,String label,List<User> user,User boss) {
	super(id,label);
	this.userlist=user;
	this.boss=boss;
}

}
