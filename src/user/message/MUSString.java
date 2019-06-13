package user.message;

import client.event.EventsBridge;
import client.user.UserClient;
import log.Logger;
import nbt.NBTTagCompound;
import user.User;

public class MUSString extends MessageUser {

	public MUSString() {

	}

	public MUSString(User user, String str) {
		super(user);
		nbt.setString("str", str);
	}

	public MUSString(String user, String str) {
		super(user);
		nbt.setString("str", str);
	}

	@Override
	void executeClient(User from, NBTTagCompound nbt) {
		EventsBridge.recvString(from, nbt.getString("str"));
	}

	@Override
	void executeServer(User from, User to, NBTTagCompound nbt) {
		if (to.isOnline()) {
			to.sendMesage(new MUSString(from, nbt.getString("str")));
		} else {
			Logger.log.impart(from, "发送给", to, "消息：", nbt.getString("str"), "但是，目前并不支持发送给离线的用户！");
		}
	}

}
