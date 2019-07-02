package user.message;

import client.event.EventsBridge;
import nbt.NBTTagCompound;
import user.User;

public class MessageHasRead extends MessageUser {

	public MessageHasRead() {
	}

	public MessageHasRead(User user) {
		super(user);
	}

	@Override
	protected void executeClient(User from, NBTTagCompound nbt) {
		EventsBridge.recvHasRead(from);
	}

	@Override
	protected void executeServer(User from, User to, NBTTagCompound nbt) {
		//下线接受不到？ycy
		if (to.isOnline()) {
			to.sendMesage(new MessageHasRead(from));
			System.out.println(from.userName+"向"+to.userName+"发送了一条已读消息");
		}
	}

}
