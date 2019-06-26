package user.message;

import java.util.LinkedList;
import java.util.List;

import core.Core;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import nbt.NBTTagString;
import server.user.UGroup;
import user.UOnline;
import user.User;

public class MessageGroupCreate extends MessageUser implements Runnable {

	public MessageGroupCreate() {

	}

	public MessageGroupCreate(User... users) {
		NBTTagList list = new NBTTagList();
		for (User user : users) {
			list.appendTag(user.getUserName());
		}
		this.nbt.setTag("list", list);
	}
	
	public MessageGroupCreate(List<User> users) {
		NBTTagList list = new NBTTagList();
		for (User user : users) {
			list.appendTag(user.getUserName());
		}
		this.nbt.setTag("list", list);
	}

	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {

	}

	User from;

	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
		this.from = from;
		Core.task(this);
	}

	@Override
	public void run() {
		NBTTagList list = (NBTTagList) this.nbt.getTag("list");
		if (list == null)
			return;
		List<User> ulist = new LinkedList<User>();
		for (NBTBase base : list) {
			NBTTagString str = (NBTTagString) base;
			ulist.add(UOnline.getInstance().getUser(str.get()));
		}
		UGroup.createGroup(from, ulist);
	}

}
