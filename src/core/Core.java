package core;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import log.Logger;
import platform.Platform;
import resmgt.ResourceManagement;
import resmgt.UserResource;
import story.ITickable;
import user.UOnline;
import user.User;
import user.message.MessageEmergency;

public class Core {

	//debug
//	static final String SERVER_IP = "127.0.0.1";
//	static final int SERVER_PORT = 35275;
//	static final Proxy proxy = new DebugProxy();
	
	static final String SERVER_IP = "39.107.94.231";
	static final int SERVER_PORT = 35275;
	static final Proxy proxy = new ClientProxy();
	
	static final Core core = new Core();

	// 所有运行的任务队列
	private LinkedList<Runnable> tasks = new LinkedList<Runnable>();
	// 所有运行的tick队列
	private LinkedList<ITickable> tickTasks = new LinkedList<ITickable>();
	// 负责计实的计时器
	private Timer timer = new Timer();
	// 负责精准循环tick的计时器
	private ScheduledExecutorService scheduExec = Executors.newSingleThreadScheduledExecutor();

	// 主函数
	public static void main(String[] args) {
		try {
			Logger.newLogger();
			Logger.log.impart("当前平台：" + Platform.platform);
			ResourceManagement.instance.init();
			UserResource.init();
			// 初始化代理
			proxy.init();
			proxy.launch();
			// 启动核心
			core.launch();
		} catch (Throwable e) {
			Logger.log.error("程序加载或称中，出现无法意料的错误！", e);
			Core.shutdownWithError();
		}
	}

	private void launch() {
		scheduExec.scheduleAtFixedRate(new TickTask(), 0, 50, TimeUnit.MILLISECONDS);
	}

	private class TickTask extends TimerTask {
		@Override
		public void run() {
			try {
				// tick更新部份
				Iterator<ITickable> itr = tickTasks.iterator();
				while (itr.hasNext()) {
					ITickable tick = itr.next();
					try {
						int flags = tick.update();
						if (flags == ITickable.END) {
							synchronized (tickTasks) {
								itr.remove();
							}
						}
					} catch (Exception e) {
						int flags = tick.recover(e);
						if (flags == ITickable.END) {
							synchronized (tickTasks) {
								itr.remove();
							}
						}
					}
				}
				// 同步任务更新部份
				while (true) {
					Runnable run = Core.this.getTask();
					if (run == null)
						break;
					try {
						run.run();
					} catch (Exception e) {
						Logger.log.warn("同步任务运行中，出现异常！", e);
					}
				}
			} catch (Throwable e) {
				Logger.log.error("同步进程出现未知错误！", e);
				Core.shutdownWithError();
			}
		}
	}

	/** 添加一个同步任务 */
	public void addTask(Runnable run) {
		synchronized (tasks) {
			tasks.addFirst(run);
		}
	}

	/** 添加一个计时器任务 */
	public void addTask(Runnable run, int ms) {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Core.task(run);
			}
		}, ms);
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

	/** 添加一个tick任务 */
	public void addtask(ITickable run) {
		synchronized (tickTasks) {
			tickTasks.add(run);
		}
	}

	/** 添加一个全局的同步任务 */
	public static void task(Runnable run) {
		if (run == null) {
			Logger.log.warn("添加同步任务的时候run出现null");
			return;
		}
		core.addTask(run);
	}

	/** 添加一个全局的同步任务 */
	public synchronized static void task(Runnable run, int msDelay) {
		if (run == null) {
			Logger.log.warn("添加计时同步任务的时候run出现null");
			return;
		}
		core.addTask(run, msDelay);
	}

	/** 添加一个tick任务 */
	public static void task(ITickable run) {
		core.addtask(run);
	}

	/** 因为错误导致程序关闭时候 */
	public synchronized static void shutdownWithError() {
		if (proxy.side.isServer()) {
			Collection<User> cUers = UOnline.getInstance().getOnlineUsers();
			for (User user : cUers) {
				user.sendMesage(new MessageEmergency("服务器崩溃！请联系管理员！bjshenshijun@emails.bjut.edu.cn"));
			}
		}
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
