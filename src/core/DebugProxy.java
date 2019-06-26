package core;

import debug.DebugMessageString;
import network.RecvDealMessage;
import network.Side;

public class DebugProxy extends Proxy {

	ClientProxy clientProxy = new ClientProxy();
	ServerProxy serverProxy = new ServerProxy();

	public DebugProxy() {
		super(Side.SERVER);
	}

	@Override
	public void init() {
		RecvDealMessage.registerMessage(1, DebugMessageString.class);
		Thread server = new Thread(new Runnable() {
			@Override
			public void run() {
				serverProxy.init();
				serverProxy.launch();
			}
		});
		server.setName("debug");
		server.start();
		Thread client = new Thread(new Runnable() {
			@Override
			public void run() {
				clientProxy.init();
				clientProxy.launch();
			}
		});
		client.setName("debug");
		client.start();
	}

	@Override
	public void launch() {

	}
}
