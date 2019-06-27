package user.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import client.event.EventsBridge;
import log.Logger;
import nbt.NBTStream;
import nbt.NBTTagCompound;
import network.Connection;

public class MessageEmergency implements network.IMessage {

	NBTTagCompound nbt = new NBTTagCompound();

	public MessageEmergency() {

	}

	public MessageEmergency(String msg) {
		this.nbt.setString("msg", msg);
		this.nbt.setInteger("lv", 1);
	}

	@Override
	public void fromBytes(ByteBuffer buf) throws IOException {
		nbt = NBTStream.read(buf);
	}

	@Override
	public void toBytes(ByteBuffer buf) throws IOException {
		NBTStream.write(buf, nbt);
	}

	@Override
	public void execute(Connection con) {
		if (con.isServer()) {
			Logger.log.warn("紧急消息制作用于客户端！" + con);
			return;
		}
		EventsBridge.emergency(this.nbt.getInteger("lv"), this.nbt.getString("msg"));
	}

}
