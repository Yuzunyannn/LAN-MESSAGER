package server.response;

import log.Logger;
import nbt.NBTTagCompound;
import nbt.NBTTagList;
import nbt.NBTTagString;
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
		String[] s = new String[] { "ssj", "lyl", "ycy", "myk", "debug" };
		MUGULRequest msg = new MUGULRequest(user);
		NBTTagCompound nbt = msg.getNBT();
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < s.length; i++) {
			list.appendTag(new NBTTagString(s[i]));
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
