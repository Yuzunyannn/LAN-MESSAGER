package debug;

import java.nio.ByteBuffer;

import network.Connection;
import network.IMessage;

public class DebugMessageString implements IMessage {
	public String str = new String();

	@Override
	public void fromBytes(ByteBuffer buf) {
		byte[] bits = buf.array();
		if (bits.length == 0)
			return;
		str = new String(bits);
	}

	@Override
	public void toBytes(ByteBuffer buf) {
		if (str.isEmpty())
			return;
		byte[] bits = str.getBytes();
		buf.put(bits);
	}

	@Override
	public void execute(Connection con) {
		System.out.println("debug" + con + "收到：" + str);
	}
}
