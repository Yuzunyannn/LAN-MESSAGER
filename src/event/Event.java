package event;

public class Event {

	/**
	 * 标识当前事件的状态
	 * 
	 * @category DEFAULT 默认的状态
	 * @category DENY 设置事件的状态为拒绝，认为这个事件不因该继续执行，这为下一个事件处理函数提供依据
	 * @category COMPLETE 设置事件的状态为完成，认为这个事件可以不继续进行，这为下一个事件处理函数提供依据
	 */
	static public enum EventState {
	DEFAULT, DENY, COMPLETE
	}

	/** 事件取消 */
	public static final int CANCEL = 0x04;
	/** 事件停止 */
	public static final int STOP = 0x08;
	/** 事件可以取消 */
	public static final int CAN_CANCEL = 0x01;
	/** 事件可以停止 */
	public static final int CAN_STOP = 0x02;
	/** 事件标签 */
	private int flags = 0;
	/** 当前事件的状态 */
	private EventState state = EventState.DEFAULT;

	public Event() {
		this(true, false);
	}

	public Event(boolean canCancel, boolean canStop) {
		if (canCancel)
			flags |= CAN_CANCEL;
		if (canStop)
			flags |= CAN_STOP;
	}

	/** 设置当前事件取消 */
	public void cancel() {
		if (this.canCancel())
			flags |= CANCEL;
	};

	/** 设置当前事件停止传播，事件如果停止传播，将不会运行接下来的事件，请注意！ */
	public void stop() {
		if (this.canStop())
			flags |= STOP;
	};

	/** 是否取消 */
	public boolean isCancel() {
		return (flags & CANCEL) != 0;
	};

	/** 是否停止传播 */
	public boolean isStop() {
		return (flags & STOP) != 0;
	};

	/** 能否取消 */
	public boolean canCancel() {
		return (flags & CAN_CANCEL) != 0;
	};

	/** 能否停止传播 */
	public boolean canStop() {
		return (flags & CAN_STOP) != 0;
	};

	/** 设置状态 */
	public void setState(EventState state) {
		this.state = state;
	}

	/** 获取状态 */
	public EventState getState() {
		return this.state;
	}

}
