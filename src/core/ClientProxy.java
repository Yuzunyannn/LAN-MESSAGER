package core;

import client.frame.MainFrame;
import log.Logger;
import network.Side;

public class ClientProxy extends Proxy {

	public ClientProxy() {
		super(Side.CLIENT);
	}

	/** 窗体 */
	MainFrame frame = null;

	@Override
	public void init() {
		super.init();
		Logger.log.impart("正在初始化客户端...");
	}

	@Override
	public void lunch() {
		super.lunch();
		Logger.log.impart("正在启动客户端...");
		// 启动窗体
		frame = new MainFrame();
	}
}
