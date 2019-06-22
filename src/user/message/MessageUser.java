package user.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import nbt.NBTStream;
import nbt.NBTTagCompound;
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

	public NBTTagCompound getNBT() {
		return nbt;
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
		User user;
		if (username.isEmpty())
			user = User.EMPTY;
		else
			user = UOnline.getInstance().getUser(username);

		if (con.isServer()) {
			this.executeServer(UOnline.getInstance().getUser(con.getName()), user, nbt);
		} else {
			this.executeClient(user, nbt);
		}

	}

	protected abstract void executeClient(User from, NBTTagCompound nbt);

	protected abstract void executeServer(User from, User to, NBTTagCompound nbt);

}
