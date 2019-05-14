package event;

public interface IEventExceptionHandle {
	public void handleException(Throwable e, Event event, EventHandle eventHandle, IEventBus eventBus);
}
