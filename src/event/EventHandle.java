package event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventHandle {

	final Object obj;
	final Method method;

	EventHandle(Object obj, Method method) {
		this.obj = obj;
		this.method = method;
	}

	public void invoke(Event event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		method.invoke(obj, event);
	}

}
