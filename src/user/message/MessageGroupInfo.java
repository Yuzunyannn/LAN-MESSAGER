package user.message;

import java.util.LinkedList;
import java.util.List;

import client.event.EventGroupInfoGet;
import client.event.EventsBridge;
import core.Core;
import log.Logger;
import nbt.NBTBase;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import nbt.NBTTagString;
import server.user.UGroup;
import user.UOnline;
import user.User;
import user.UserSpecial;

public class MessageGroupInfo extends MessageUser implements Runnable {

	public MessageGroupInfo() {

	}

	public MessageGroupInfo(UserSpecial sp) {
		this.nbt.setString("sp", sp.specialName);
	}

	public MessageGroupInfo(UserSpecial sp, User boss, User... users) {
		NBTTagList list = new NBTTagList();
		for (User user : users) {
			list.appendTag(user.getUserName());
		}
		this.nbt.setTag("list", list);
		this.nbt.setString("boss", boss.getUserName());
		this.nbt.setString("sp", sp.specialName);
	}

	public MessageGroupInfo(UserSpecial sp, User boss, List<User> users) {
		NBTTagList list = new NBTTagList();
		for (User user : users) {
			list.appendTag(user.getUserName());
		}
		this.nbt.setTag("list", list);
		this.nbt.setString("boss", boss.getUserName());
		this.nbt.setString("sp", sp.specialName);
	}

	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {
		List<User> list = new LinkedList<>();
		User boss = UOnline.getInstance().getUser(nbt.getString("boss"));
		NBTTagList nlist = (NBTTagList) nbt.getTag("list");
		for (NBTBase base : nlist) {
			NBTTagString str = (NBTTagString) base;
			list.add(UOnline.getInstance().getUser(str.get()));
		}
		UserSpecial sp = new UserSpecial(nbt.getString("sp"));
		EventsBridge.frontendEventHandle.post(new EventGroupInfoGet(boss, list, sp));
	}

	User from;

	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
		this.from = from;
		Core.task(this);
	}

	@Override
	public void run() {
		if (nbt.hasKey("sp"))
			UGroup.tellUser(new UserSpecial(nbt.getString("sp")), from);
		else
			Logger.log.warn("查询的群ID不明！");
	}

}
