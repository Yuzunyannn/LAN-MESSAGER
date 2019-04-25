package core;

import java.io.IOException;

import debug.DebugMessageString;
import network.Connection;
import network.RecvDealMessage;
import network.Side;

public class DebugProxy extends Proxy {

	public DebugProxy(Side side) {
		super(side);
	}

	@Override
	public void init() {
		super.init();
		RecvDealMessage.registerMessage(1, DebugMessageString.class);
	}

	@SuppressWarnings("unused")
	@Override
	public void launch() {
		super.launch();
		try {
			Connection con = new Connection("10.26.23.115", 25565);
			DebugMessageString msg = new DebugMessageString();
			msg.str = "gay";
			RecvDealMessage.send(con, msg);
			con.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
