package core;

import java.io.IOException;

import log.Logger;
import network.Network;
import network.Side;
import server.database.Database;
import server.user.ULogin;
import server.user.UOnlineServer;

public class ServerProxy extends Proxy {

	Network net;

	public ServerProxy() {
		super(Side.SERVER);
	}

	@Override
	public void init() {
		super.init();
		Thread.currentThread().setName("Server");
		Logger.log.impart("正在初始化服务端...");
		Core.setUOnline(new UOnlineServer());
		Network.eventHandle.register(ULogin.class);
	}

	@Override
	public void launch() {
		super.launch();
		Logger.log.impart("正在启动服务端...");
		// 加载数据库
		try {
			Database.launch();
		} catch (Exception e) {
			Logger.log.error("数据库加载错误！", e);
			Core.shutdownWithError();
		}
		Logger.log.impart("数据库驱动加载成功！");
		try {
			net = new Network(Core.SERVER_PORT);
		} catch (IOException e) {
			Logger.log.error("网络启动失败！", e);
			Core.shutdownWithError();
		}
	}
}
