package core;

import java.io.IOException;
import java.net.BindException;

import log.Logger;
import network.Network;
import network.Side;
import server.database.Database;
import server.user.ULogin;
import server.user.UOnlineServer;
import transfer.FileSenderManager;
import user.UOnline;

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
		// 初始化UOnline
		UOnlineServer uoserver = new UOnlineServer();
		Core.setUOnline(uoserver);
		Core.task(uoserver);
		// 注册网络事件
		Network.eventHandle.register(ULogin.class);
		Network.eventHandle.register(UOnline.getInstance());
		UOnlineServer.eventHandle.register(UOnline.getInstance());
		UOnlineServer.eventHandle.register(FileSenderManager.class);
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
		} catch (BindException e) {
			Logger.log.error("端口" + Core.SERVER_PORT + "已经被占用！服务端只能启动一次啊！");
			Core.shutdownWithError();
		} catch (IOException e) {
			Logger.log.error("网络启动失败！", e);
			Core.shutdownWithError();
		}
	}
}
