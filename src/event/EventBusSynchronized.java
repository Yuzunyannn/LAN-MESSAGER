package event;

public class EventBusSynchronized implements IEventBus {
	public final IEventBus bus;

	public EventBusSynchronized(IEventBus bus) {
		this.bus = bus;
	}

	public EventBusSynchronized() {
		this.bus = new EventBus();
	}

	@Override
	synchronized public void register(Object obj) {
		this.bus.register(obj);
	}

	@Override
	synchronized public boolean post(Event event) {
		return this.bus.post(event);
	}

}
