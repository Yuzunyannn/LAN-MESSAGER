package event;

import core.Core;

public class EventBusTask implements IEventBus {

	public final IEventBus bus;

	public EventBusTask(IEventBus bus) {
		this.bus = bus;
	}

	public EventBusTask() {
		this.bus = new EventBus();
	}
	
	
	@Override
	synchronized public void register(Object obj) {
		this.bus.register(obj);
	}

	/** 因为是同步任务，不是立刻调用，该返回值仅表示是否成功加入同步列表 */
	@Override
	synchronized public boolean post(Event event) {
		if (event == null)
			return false;
		Core.task(new EventBusTaskRun(this.bus, event));
		return true;
	}

	private static class EventBusTaskRun implements Runnable {

		final IEventBus bus;
		final Event event;

		public EventBusTaskRun(IEventBus bus, Event event) {
			this.bus = bus;
			this.event = event;
		}

		@Override
		public void run() {
			synchronized (bus) {
				bus.post(event);
			}
		}
	}

}
