package user.message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import core.Core;
import core.EventsBridge;
import log.Logger;
import network.Connection;
import network.IMessage;
import network.Side;
import user.UOnline;
import util.Cast;
import util.StreamHelper;

public class MessageLogin implements IMessage {

	byte[] username;
	byte[] password;

	public MessageLogin() {
	}

	public MessageLogin(String username, String password, Side side) {
		try {
			this.username = username.getBytes("UTF-8");
			this.password = this.dataToBytes(password, side);
		} catch (UnsupportedEncodingException e) {
			Logger.log.warn("字符传话byte错误！", e);
		}
	}

	@Override
	public void fromBytes(ByteBuffer buf) {
		username = StreamHelper.readBytes(buf);
		password = StreamHelper.readBytes(buf);
	}

	@Override
	public void toBytes(ByteBuffer buf) throws IOException {
		StreamHelper.writeBytes(buf, username);
		StreamHelper.writeBytes(buf, password);
	}

	@Override
	public void execute(Connection con) {
		String username = Cast.toStringUTF8(this.username);
		String password = this.dataToString(this.password, con.side);
		if (con.isClient()) {
			EventsBridge.login(username,password);
			return;
		}
		Core.task(new Runnable() {
			@Override
			public void run() {
				UOnline.getInstance().login(username, password, con);
			}
		});
	}

	private byte[] dataToBytes(String password, Side side) {
		return Cast.toBytesUTF8(password);
	}

	private String dataToString(byte[] password, Side side) {
		return Cast.toStringUTF8(password);
	}

}
