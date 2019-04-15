package core;

import debug.DebugMessageString;
import network.Network;
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
	public void lunch() {
		super.lunch();
		try {
			Network net = new Network(25565);
			for (int i = 0; i < 32 * 10; i++)
				Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
