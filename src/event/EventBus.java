package event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import log.Logger;

public class EventBus implements IEventBus, IEventExceptionHandle {

	/** 记录事件和处理事件对象的图 */
	private Map<Class<? extends Event>, ArrayList<EventHandle>> eventsMap;

	/** 记录所有被注册的对象 */
	private Set<Object> objSet = new HashSet<Object>();

	/** 事件异常处理句柄 */
	private IEventExceptionHandle exceptionHandle = this;

	public EventBus() {
		this.init();
	}

	public EventBus(IEventExceptionHandle exceptionHandle) {
		this.exceptionHandle = exceptionHandle;
		this.init();
	}

	private void init() {
		eventsMap = new HashMap<Class<? extends Event>, ArrayList<EventHandle>>();
		// eventsMap = Collections.synchronizedMap(eventsMap);
	}

	/**
	 * 注册一个携带有event的对象
	 * 
	 * @param obj
	 *            携带有event的对象
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void register(Object obj) {
		if (obj == null) {
			Logger.log.warn("EventBus注册事件的对象不能为空！");
			return;
		}
		if (objSet.contains(obj)) {
			Logger.log.warn("EventBus无法添加重复的事件对象");
			return;
		}
		boolean isStatic = obj instanceof Class;
		boolean haveEvent = false;
		Method[] methods = isStatic ? ((Class<?>) obj).getMethods() : obj.getClass().getMethods();
		// 遍历所有方法
		for (Method method : methods) {
			// 如果是方法和传入的静态不符合
			if (!isStatic && Modifier.isStatic(method.getModifiers()))
				continue;
			else if (isStatic && !Modifier.isStatic(method.getModifiers()))
				continue;
			// 如果没有注解
			if (!method.isAnnotationPresent(SubscribeEvent.class))
				continue;
			// 参数们
			Class<?>[] parameters = method.getParameterTypes();
			if (parameters.length != 1) {
				Logger.log.warn("注册：事件函数的参数必须有且仅有一个！出错的函数:" + method);
				continue;
			}
			Class<?> eventClass = parameters[0];
			if (!Event.class.isAssignableFrom(eventClass)) {
				Logger.log.warn("注册：事件函数的参数必须为Event的子类！出错的函数:" + method);
				continue;
			}
			haveEvent = true;
			this.register(obj, method, (Class<? extends Event>) eventClass);
		}
		if (haveEvent)
			objSet.add(obj);
	}

	/** 注册入一个函数 */
	private void register(Object obj, Method method, Class<? extends Event> event) {
		EventHandle handle = new EventHandle(obj, method);
		if (!eventsMap.containsKey(event))
			eventsMap.put(event, new ArrayList<EventHandle>());
		ArrayList<EventHandle> list = eventsMap.get(event);
		list.add(handle);
	}

	/***
	 * 传递事件，在EventBus上
	 * 
	 * @param event
	 *            需要传递的事件
	 * @return 事件正常完成（未被取消）
	 */
	@Override
	public boolean post(Event event) {
		ArrayList<EventHandle> list = null;
		if (eventsMap.containsKey(event.getClass()))
			list = eventsMap.get(event.getClass());
		if (list == null)
			return true;
		EventHandle handle = null;
		try {
			for (int i = 0; i < list.size(); i++) {
				handle = list.get(i);
				handle.invoke(event);
				if (event.isStop())
					break;
			}
		} catch (Throwable throwable) {
			Logger.log.warn("在传递事件的时候出现异常！", throwable);
			exceptionHandle.handleException(throwable, event, handle, this);
		}
		return !event.isCancel();
	}

	@Override
	public void handleException(Throwable e, Event event, EventHandle eventHandle, IEventBus eventBus) {
		Logger.log.warn("在传递事件的时候出现异常的事件：" + event);
		Logger.log.warn("在传递事件的时候出现异常的函数：" + eventHandle.method);
		Logger.log.warn("在传递事件的时候出现异常的EventBus：" + eventBus);
	}
}
