package user.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import log.Logger;
import nbt.NBTStream;
import nbt.NBTTagCompound;
import nbt.NBTTagInt;
import network.Connection;
import network.IMessage;
import user.UOnline;
import user.User;

abstract public class MessageUser implements IMessage {

	protected NBTTagCompound nbt = new NBTTagCompound();

	public MessageUser() {

	}

	public MessageUser(User user) {
		this.nbt.setString("user", user.getUserName());
	}

	public MessageUser(String username) {
		this.nbt.setString("user", username);
	}

	@Override
	final public void fromBytes(ByteBuffer buf) throws IOException {
		nbt = NBTStream.read(buf);
	}

	@Override
	final public void toBytes(ByteBuffer buf) throws IOException {
		NBTStream.write(buf, nbt);
	}

	@Override
	final public void execute(Connection con) {
		String username = nbt.getString("user");
		if (username.isEmpty()) {
			Logger.log.warn("接收到的user信息中找不到User");
			return;
		}
		User user = UOnline.getInstance().getUser(username);
		if (con.isServer()) {
			this.executeServer(UOnline.getInstance().getUser(con.getName()), user, nbt);
		} else {
			this.executeClient(user, nbt);
		}
		
	}

	abstract void executeClient(User from, NBTTagCompound nbt);

	abstract void executeServer(User from, User to, NBTTagCompound nbt);

}
