package network;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import log.Logger;
import network.event.EventConnectionEnd;

public class Connection implements Runnable {

	/** 表示连接的socket */
	final public Socket socket;
	/** 信息接受线程 */
	final private Thread connectThread;
	/** 连接建立的位置，服务端还是客户端 */
	final public Side side;
	/** 输出流，发送 */
	final public OutputStream out;
	/** 输入流，接受 */
	final public InputStream input;
	/** 处理接受信息 */
	IRecvDeal deal = new RecvDealValidation();
	/** 名称 */
	private String name = null;

	/**
	 * 建立客户端连接
	 * 
	 * @param ip   ip地址
	 * @param port 目标端口
	 */
	public Connection(String ip, int port) throws UnknownHostException, IOException {
		this(new Socket(ip, port), Side.CLIENT);
		// 等待验证成功
		try {
			synchronized (this) {
				this.wait();
			}
		} catch (InterruptedException e) {
			Logger.log.warn("connection再等待验证的时候出现异常：", e);
		}
	}

	protected Connection(Socket socket, Side side) throws IOException {
		// 初始化连接
		this.socket = socket;
		this.side = side;
		this.out = socket.getOutputStream();
		this.input = socket.getInputStream();
		// 启动连接处理线程
		connectThread = new Thread(this);
		connectThread.setPriority(Thread.NORM_PRIORITY - 1);
		// 设置名称
		if (side == Side.SERVER)
			connectThread.setName("ServerConnection");
		else
			connectThread.setName("ClientConnection");
		// 启动
		connectThread.start();
	}

	/** 发送 */
	protected boolean send(ByteBuffer buf) {
		if (this.isClosed())
			return false;
		try {
			out.write(buf.array(), 0, buf.position());
			out.flush();
			return true;
		} catch (IOException e) {
			Logger.log.warn(this.toString() + "send数据时发生异常", e);
			return false;
		}
	}

	/** 接受数据线程 */
	@Override
	public void run() {
		Logger.log.impart(this.toString() + "线程启动！");
		while (!socket.isClosed()) {
			try {
				deal.deal(this);
			} catch (SocketException e) {
				String excType = e.getMessage();
				if (excType.equals("Socket closed"))
					break;
				if (excType.equals("Connection reset")) {
					Logger.log.warn(this.toString() + "socket链接被重置！");
					break;
				}
				Logger.log.warn(this.toString() + "出现socket错误", e);
				break;
			} catch (EOFException e) {
				break;
			} catch (IOException e) {
				Logger.log.error(this.toString() + "出现IO错误", e);
				break;
			} catch (Exception e) {
				Logger.log.error(this.toString() + "出现较严重错误", e);
				break;
			} catch (Throwable e) {
				Logger.log.error(this.toString() + "出现意料之外的非常严重错误", e);
				break;
			}
		}
		this.close();
		Network.eventHandle.post(new EventConnectionEnd(this));
		Logger.log.impart(this.toString() + "线程已退出！");
	}

	/** 设置处理托管对象 */
	public synchronized void setRecvDeal(IRecvDeal deal) {
		if (deal == null)
			return;
		this.deal = deal;
	}

	/** 关闭 */
	public synchronized boolean close() {
		if (this.isClosed())
			return true;
		try {
			socket.close();
		} catch (IOException e) {
			Logger.log.warn(this.toString() + "在close时出现异常", e);
			return false;
		}
		Logger.log.impart(this, "断开连接！");
		return true;
	}

	/** 是否关闭 */
	public boolean isClosed() {
		return socket.isClosed();
	}

	/** 获取连接地址信息 */
	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}

	/** 是否为服务端 */
	public boolean isServer() {
		return side == Side.SERVER;
	}

	/** 是否为客户端 */
	public boolean isClient() {
		return side == Side.CLIENT;
	}

	@Override
	public String toString() {
		if (name == null || name.isEmpty()) {
			return "[连接" + this.getInetAddress() + "]";
		}
		return "[连接" + this.getInetAddress() + ":" + name + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void wake() {
		synchronized (this) {
			this.notify();
		}
	}

}
