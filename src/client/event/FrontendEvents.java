package client.event;

import event.EventBusSynchronized;
import event.IEventBus;

public class FrontendEvents {
	public static IEventBus eventHandle = new EventBusSynchronized();
}
