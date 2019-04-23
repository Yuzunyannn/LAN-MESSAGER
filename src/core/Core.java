package core;

import log.Logger;

public class Core {

	static Proxy proxy = new ClientProxy();

	public static void main(String[] args) throws Exception {
		proxy.init();
		proxy.lunch();
	}

	/** 因为错误导致程序关闭时候 */
	synchronized public static void shutdownWithError() {
		Logger.log.error("系统由于严重错误关闭！");
		System.exit(-1);
	}

}
