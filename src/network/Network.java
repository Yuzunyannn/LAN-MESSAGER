package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import event.EventBusSynchronized;
import event.IEventBus;
import log.Logger;
import network.event.EventNewConnection;
import network.event.EventSocketAccept;

public class Network implements Runnable {

	/** 网络事件 */
	final public static IEventBus eventHandle = new EventBusSynchronized();

	/** 首次验证数据 */
	final public static String VALIDATION = "nyan~";
	/** 服务端socket */
	final public ServerSocket serverSocket;
	/** 服务器线程 */
	final private Thread serverThread;
	/** 记录所有的连接 */
	final public List<Connection> connectList = Collections.synchronizedList(new LinkedList<Connection>());

	public Network(int port) throws IOException {
		Logger.log.impart("Network正在启动....");
		// 创建socket
		serverSocket = new ServerSocket(port);
		// 创建线程
		serverThread = new Thread(this);
		// 设置优先级
		serverThread.setPriority(Thread.NORM_PRIORITY - 2);
		// 设置名称
		serverThread.setName("ServerNetwork");
		// 启动服务器
		serverThread.start();
		Logger.log.impart("Network启动启动构造完成！");
	}

	/** 是否关闭 */
	public boolean isClosed() {
		return serverSocket.isClosed();
	}

	/* 服务器运行线程 */
	@Override
	public void run() {
		Logger.log.impart("Network线程启动！");
		// 开始主循环
		while (!serverSocket.isClosed()) {
			Socket socket = null;
			try {
				// 接受连接
				socket = serverSocket.accept();
			} catch (IOException e) {
				Logger.log.error("服务端accpet出现异常", e);
				break;
			}
			// 没收到
			if (socket == null) {
				Logger.log.error("服务端accpet接受到的socket为null");
				break;
			}
			// 传播事件
			boolean ok = eventHandle.post(new EventSocketAccept(this, socket));
			if (ok == false) {
				try {
					socket.close();
				} catch (IOException e) {
					Logger.log.warn("服务器拒绝socket连接调用close的时候出现异常！", e);
				}
				continue;
			}
			Connection con = null;
			try {
				con = new Connection(socket, Side.SERVER);
				Logger.log.impart("新的连接：" + con.getInetAddress());
			} catch (IOException e) {
				Logger.log.warn("连接建立失败", e);
				continue;
			}
			// 发送验证消息
			RecvDealValidation.sendValidation(con);
			// 加入列表
			connectList.add(con);
			// 检查更新删除不需要的连接
			this.checkAndDealConnects();
			// 传播事件
			ok = eventHandle.post(new EventNewConnection(this, con));
			if (ok == false) {
				con.close();
				this.checkAndDealConnects();
			}
		}
		Logger.log.impart("Network线程已退出！");
	}

	/** 检测处理连接 */
	private void checkAndDealConnects() {
		Iterator<Connection> iter = connectList.iterator();
		while (iter.hasNext()) {
			Connection con = iter.next();
			// 如果连接的socket已经关闭，就处理它
			if (con.isClosed()) {
				iter.remove();
			}
		}
	}

}
