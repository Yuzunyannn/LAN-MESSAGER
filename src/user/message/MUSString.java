package user.message;

import client.event.EventsBridge;
import core.Core;
import log.Logger;
import nbt.NBTTagCompound;
import server.user.UGroup;
import user.User;
import user.UserSpecial;
import user.UserSpecialType;

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
	protected void executeClient(User from, NBTTagCompound nbt) {
		EventsBridge.recvString(from, nbt.getString("str"));
	}

	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
		String str = nbt.getString("str");
		if (to.isSpecial()) {
			UserSpecial sp = to.getSpecialUser();
			if (sp.getType() == UserSpecialType.GROUP) {
				Core.task(new Runnable() {
					@Override
					public void run() {
						UGroup.msgToGruop(from, sp, str);
					}
				});
			} else
				Logger.log.impart(from, "发送给的特殊用户", to, "消息：", str, "该特殊用户类型不正确！", sp.getType());
			return;
		}
		if (to.isOnline()) {
			to.sendMesage(new MUSString(from, str));
		} else {
			Logger.log.impart(from, "发送给", to, "消息：", str, "但是，目前并不支持发送给离线的用户！");
		}
	}

}
