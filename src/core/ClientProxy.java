package core;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import client.event.EventsBridge;
import client.frame.LoginFrame;
import client.frame.MainFrame;
import client.frame.info.ListScrollPanel;
import client.frame.utility.UtilityPanel;
import client.user.UOnlineClient;
import client.user.UserClient;
import event.SubscribeEvent;
import log.Logger;
import network.Connection;
import network.Network;
import network.RecvDealValidation;
import network.Side;
import user.UOnline;
import user.message.MUGULRequest;
import user.message.MessageLogin;

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
		// 初始化UOnline
		if (UOnline.getInstance() == null)
			Core.setUOnline(new UOnlineClient());
		// 初始化句柄
		logFrame = new LoginFrame();
		frame = new MainFrame();
		// 注册事件
		frame.initEvent(EventsBridge.frontendEventHandle);
		EventsBridge.frontendEventHandle.register(this);
		EventsBridge.frontendEventHandle.register(UtilityPanel.class);
	}

	@Override
	public void launch() {
		super.launch();
		Logger.log.impart("正在启动客户端...");
		// 启动窗体
		logFrame.setVisible(true);
		logFrame.setLoginListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (!logFrame.isEnable())
					return;
				logFrame.setLoginButtonEnable(false);
				try {
					if (toServer == null) {
						toServer = new Connection(Core.SERVER_IP, Core.SERVER_PORT);
						if (!RecvDealValidation.check(toServer)) {
							toServer = null;
							Logger.log.warn("服务器拒绝您的登录！");
							return;
						}
						UserClient.toServer = toServer;
					}
					String username = logFrame.getUserName();
					if (username == null || username.isEmpty()) {
						Logger.log.impart("请输入用户名");
						return;
					}
					sendUsername = username;
					UserClient.sendToServer(new MessageLogin(username, logFrame.getPassword(), Side.CLIENT));
				} catch (IOException e1) {
					Logger.log.warn("连接服务器出现异常！", e1);
				}
			}
		});
	}

	/** 发送的用户名 */
	private String sendUsername;

	@SubscribeEvent
	public void onLogin(client.event.EventLogin e) {
		logFrame.setLoginButtonEnable(true);
		if (e.username.equals(sendUsername)) {
			if (!e.info.equals(Network.VALIDATION)) {
				Logger.log.impart(e.username + "登录失败！");
				return;
			}
			Logger.log.impart(e.username + "登录成功！");
			// 登录成功
			UserClient.sendToServer(new MUGULRequest(UOnline.getInstance().getUser(e.username)));
			UserClient.toServer.setName(e.username);
			logFrame.setVisible(false);
			frame.setVisible(true);
		} else {
			Logger.log.warn("登录失败，传送回的用户名和发送的不符！" + e.username);
		}
	}
}
