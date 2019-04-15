package core;

import client.frame.MainFrame;
import network.Side;

public class ClientProxy extends Proxy {

	public ClientProxy( ) {
		super(Side.CLIENT);
	}

	/** 窗体 */
	MainFrame frame = null;

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void lunch() {
		super.lunch();
		// 启动窗体
		frame = new MainFrame();
	}
}
