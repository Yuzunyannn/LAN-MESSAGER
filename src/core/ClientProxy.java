package core;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import client.frame.LoginFrame;
import client.frame.MainFrame;
import client.user.UOnlineClient;
import client.user.UserClient;
import log.Logger;
import network.Connection;
import network.RecvDealValidation;
import network.Side;
import user.UOnline;

public class ClientProxy extends Proxy {

	public ClientProxy() {
		super(Side.CLIENT);
	}

	/** 窗体 */
	MainFrame frame = null;
	/** 登录窗体 */
	LoginFrame logFrame = null;

	/** 对于server的连接 */
	Connection toServer = null;

	@Override
	public void init() {
		super.init();
		Thread.currentThread().setName("Client");
		Logger.log.impart("正在初始化客户端...");
		if (UOnline.getInstance() == null)
			Core.setUOnline(new UOnlineClient());
	}

	@Override
	public void launch() {
		super.launch();
		Logger.log.impart("正在启动客户端...");
		// 启动窗体
		logFrame = new LoginFrame();
		logFrame.setLoginListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				try {
					toServer = new Connection(Core.SERVER_IP, Core.SERVER_PORT);
					if (!RecvDealValidation.check(toServer)) {
						toServer = null;
						Logger.log.warn("服务器拒绝您的登录！");
						return;
					}
					UserClient.toServer = toServer;
					logFrame.setVisible(false);
					frame = new MainFrame();
				} catch (IOException e1) {
					Logger.log.warn("连接服务器出现异常！", e1);
				}
			}
		});
	}
}
