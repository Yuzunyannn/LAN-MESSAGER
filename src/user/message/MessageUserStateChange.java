package user.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map.Entry;

import client.event.EventsBridge;
import core.Core;
import nbt.NBTBase;
import nbt.NBTStream;
import nbt.NBTTagCompound;
import nbt.NBTTagInt;
import network.Connection;
import network.IMessage;
import network.Side;
import user.UOnline;
import user.User;

public class MessageUserStateChange implements IMessage, Runnable {

	public NBTTagCompound nbt = new NBTTagCompound();

	public MessageUserStateChange() {

	}

	public MessageUserStateChange(User user, int flags) {
		this.add(user, flags);
	}

	/**
	 * 添加一个用户状态
	 * 
	 * @param flags 第一位1表示在线，0表示离线
	 */
	public void add(User user, int flags) {
		nbt.setInteger(user.getUserName(), flags);
	}

	@Override
	public void fromBytes(ByteBuffer buf) throws IOException {
		nbt = NBTStream.read(buf);
	}

	@Override
	public void toBytes(ByteBuffer buf) throws IOException {
		NBTStream.write(buf, nbt);
	}

	Side side;

	@Override
	public void execute(Connection con) {
		this.side = con.side;
		Core.task(this);

	}

	@Override
	public void run() {
		if (this.side.isClient()) {
			for (Entry<String, NBTBase> entry : nbt) {
				User user = UOnline.getInstance().getUser(entry.getKey());
				NBTTagInt i = (NBTTagInt) entry.getValue();
				int flags = nbt.getInteger("flags");
				EventsBridge.userLoginSate(user, (flags & 1) != 0);
			}
		} else {

		}
	}

}
