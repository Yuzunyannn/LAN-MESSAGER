package event;

public interface IEventExceptionHandle {
	public void handleException(Throwable e, Event event, EventHandle eventHandle, EventBus eventBus);
}
