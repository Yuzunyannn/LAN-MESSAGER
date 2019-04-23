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
		Logger.log.impart("正在初始化服务端...");
	}

	@Override
	public void lunch() {
		super.lunch();
		Logger.log.impart("正在启动服务端...");
		// 加载数据库
		try {
			Database.launch();
		} catch (Exception e) {
			Logger.log.error("数据库加载错误！", e);
			Core.shutdownWithError();
		}
		Logger.log.impart("数据库驱动加载成功！");
	}
}
