package core;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

import client.event.EventNeedSave;
import client.event.EventsBridge;
import client.frame.LoginFrame;
import client.frame.MainFrame;
import client.record.RecordLogInfo;
import client.record.RecordManagement;
import client.user.UOnlineClient;
import client.user.UserClient;
import event.SubscribeEvent;
import log.Logger;
import network.Connection;
import network.Network;
import network.RecvDealValidation;
import network.Side;
import transfer.FileSenderManager;
import user.UOnline;
import user.message.MUGULRequest;
import user.message.MessageLogin;

public class ClientProxy extends Proxy implements Runnable {

	public ClientProxy() {
		super(Side.CLIENT);
	}

	/** 窗体 */
	MainFrame frame = null;
	/** 登录窗体 */
	LoginFrame logFrame = null;

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
		EventsBridge.dealEventHandle.register(RecordManagement.class);
		FileSenderManager.eventHandle.register(EventsBridge.class);
		Network.eventHandle.register(UOnlineClient.class);
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
				logFrame.setHint("");
				Core.task(ClientProxy.this);
			}
		});
	}

	/** 发送的用户名 */
	private String sendUsername;

	@Override
	public void run() {
		try {
			if (UserClient.toServer == null) {
				UserClient.toServer = new Connection(Core.SERVER_IP, Core.SERVER_PORT);
				if (!RecvDealValidation.check(UserClient.toServer)) {
					UserClient.toServer = null;
					Logger.log.warn("服务器拒绝您的登录！");
					return;
				}
			}
			String username = logFrame.getUserName();
			if (username == null || username.isEmpty()) {
				logFrame.setLoginButtonEnable(true);
				logFrame.setHint("请输入用户名！");
				return;
			}
			sendUsername = username;
			UserClient.sendToServer(new MessageLogin(username, logFrame.getPassword(), Side.CLIENT));
		} catch (IOException e1) {
			logFrame.setLoginButtonEnable(true);
			logFrame.setHint("无法连接到服务器！");
			Logger.log.warn("连接服务器出现异常！", e1);
		}
	}

	@SubscribeEvent
	public void onLogin(client.event.EventLogin e) {
		logFrame.setLoginButtonEnable(true);
		if (e.username.equals(sendUsername)) {
			if (!e.info.equals(Network.VALIDATION)) {
				Logger.log.impart(e.username + "登录失败！");
				if (e.info.equals("online"))
					logFrame.setHint("该用户已在线！");
				else
					logFrame.setHint("用户名或密码无效！");
				return;
			}
			Logger.log.impart(e.username + "登录成功！");
			RecordLogInfo lInfo = new RecordLogInfo();
			lInfo.password = logFrame.getPassword();
			lInfo.username = logFrame.getUserName();
			RecordManagement.setLogInfo(lInfo);
			// 登录成功
			UserClient.sendToServer(new MUGULRequest(UOnline.getInstance().getUser(e.username)));
			UserClient.toServer.setName(e.username);
			logFrame.setVisible(false);
			frame.setUserName(UOnline.getInstance().getUser(e.username).getUserName());
			frame.setVisible(true);
			// test Area
			String tmp = UOnline.getInstance().getUser(e.username).getUserName();
			if (tmp.length() > 3) {
				if (tmp.substring(0, 4).equals("test")) {
					for (int i = 0; i < 1000; i++) {
						System.out.println("send: " + i);
						EventsBridge.sendString("message", UOnline.getInstance().getUser("Bevis"));
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
							System.exit(0);
						}
					}
				}
			}
			// test Area end
		} else {
			Logger.log.warn("登录失败，传送回的用户名和发送的不符！" + e.username);
			logFrame.setHint("登陆失败！");
		}
	}

	@SubscribeEvent
	public void onLogout(client.event.EventLogout e) {
		EventsBridge.dealEventHandle.post(new EventNeedSave());
		UserClient.toServer.close();
		UserClient.toServer = null;
		logFrame.setHint("");
		logFrame.setVisible(true);
		frame.setVisible(false);
	}

	@SubscribeEvent
	public void emergency(client.event.EventEmergency e) {
		JOptionPane.showMessageDialog(null, e.info, "服务端异常", JOptionPane.ERROR_MESSAGE);
	}
}
