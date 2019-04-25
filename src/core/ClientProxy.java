package core;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import client.frame.LoginFrame;
import client.frame.MainFrame;
import log.Logger;
import network.Side;

public class ClientProxy extends Proxy {

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
		Logger.log.impart("正在初始化客户端...");
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
				logFrame.setVisible(false);
				frame = new MainFrame();
			}
		});
	}
}
