package core;

import java.lang.reflect.Field;
import java.util.LinkedList;

import log.Logger;
import network.Side;
import user.UOnline;

public class Core implements Runnable {

	static final String SERVER_IP = "127.0.0.1";
	static final int SERVER_PORT = 35275;

	static final Proxy proxy = new DebugProxy(Side.SERVER);
	static final Core core = new Core();
	private LinkedList<Runnable> tasks = new LinkedList<Runnable>();

	public static void main(String[] args) {
		proxy.init();
		proxy.launch();
		core.run();
	}

	@Override
	public void run() {
		while (true) {
			this.coreWait();
			while (true) {
				Runnable run = this.getTask();
				if (run == null)
					break;
				run.run();
			}
		}
	}

	/** 挂起核心同步线程 */
	private void coreWait() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				Logger.log.error("同步线程挂起的的时候出现异常！", e);
				Core.shutdownWithError();
			}
		}
	}

	/** 唤醒核心同步线程 */
	private void coreNotify() {
		synchronized (this) {
			this.notify();
		}
	}

	/** 添加一个同步任务 */
	private void addTask(Runnable run) {
		this.coreNotify();
		synchronized (tasks) {
			tasks.addFirst(run);
		}
	}

	/** 获取同步任务 */
	private Runnable getTask() {
		Runnable run = null;
		synchronized (tasks) {
			if (!tasks.isEmpty()) {
				run = tasks.getLast();
				tasks.removeLast();
			}
		}
		return run;
	}

	/** 添加一个全局的同步任务 */
	public synchronized static void task(Runnable run) {
		if (run == null) {
			Logger.log.warn("添加同步任务的时候run出现null");
			return;
		}
		core.addTask(run);
	}

	/** 因为错误导致程序关闭时候 */
	public synchronized static void shutdownWithError() {
		Logger.log.error("系统由于严重错误关闭！");
		System.exit(-1);
	}

	/** 设置useronline */
	static synchronized void setUOnline(UOnline online) {
		try {
			Class<?> cls = Class.forName("user.UOnline");
			Field instance = cls.getDeclaredField("instance");
			instance.setAccessible(true);
			instance.set(cls, online);
		} catch (IllegalArgumentException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException
				| SecurityException e) {
			Logger.log.warn("设置UOnline出现问题！", e);
		}
	}

}
