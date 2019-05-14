package event;

public interface IEventBus {
	/**
	 * 注册一个携带有event的对象
	 * 
	 * @param obj
	 *            携带有event的对象
	 */
	public void register(Object obj);

	/***
	 * 传递事件，在EventBus上
	 * 
	 * @param event
	 *            需要传递的事件
	 * @return 事件正常完成（未被取消），没取消为true
	 */
	public boolean post(Event event);
}
