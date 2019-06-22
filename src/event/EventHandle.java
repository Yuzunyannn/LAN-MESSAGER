package event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import log.Logger;

public class EventHandle {

	final Object obj;
	final Method method;

	EventHandle(Object obj, Method method) {
		this.obj = obj;
		this.method = method;
	}

	public void invoke(Event event) {
		try {
			method.invoke(obj, event);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.log.warn("事件invoke时候出现异常！", e);
		}
	}
	
	
}
