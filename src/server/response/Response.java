package server.response;

import java.util.ArrayList;
import java.util.Collection;

import log.Logger;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import nbt.NBTTagString;
import user.UOnline;
import user.User;
import user.message.MUGULRequest;

public class Response implements Runnable {

	private User user;

	public Response() {
	}

	public Response(User u) {
		user = u;
	}

	public boolean query() {
		ArrayList<String> listU = new ArrayList<String>();
		listU.add("yuzunyannn");
		listU.add("Bevis");
		listU.add("tatsuu");
		listU.add("dispute");
		Collection<User> users = UOnline.getInstance().getOnlineUsers();
		int n = 0;
		for (User user : users) {
			if (n > 20)
				break;
			if (user.equals(this.user))
				continue;
			listU.add(user.getUserName());
			n++;
		}
		MUGULRequest msg = new MUGULRequest(user);
		NBTTagCompound nbt = msg.getNBT();
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < listU.size(); i++) {
			list.appendTag(new NBTTagString(listU.get(i)));
		}
		nbt.setTag("userlist", list);
		user.sendMesage(msg);
		return true;
	}

	@Override
	public void run() {
		if (query()) {
			Logger.log.impart(user.userName + "用户列表查询成功");
		} else {
			Logger.log.impart(user.userName + "用户列表查询");
		}
	}
}
