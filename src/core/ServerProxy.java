package core;

import log.Logger;
import network.Side;
import server.database.Database;

public class ServerProxy extends Proxy {

	public ServerProxy() {
		super(Side.SERVER);
	}

	@Override
	public void init() {
		super.init();

	}

	@Override
	public void lunch() {
		super.lunch();
		// 加载数据库
		try {
			Database.launch();
		} catch (Exception e) {
			Logger.log.error("数据库加载错误！", e);
			Core.shutdownWithError();
		}
	}
}
