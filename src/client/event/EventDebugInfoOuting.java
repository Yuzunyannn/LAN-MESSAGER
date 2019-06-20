package client.event;

import java.util.LinkedList;
import java.util.List;

import event.Event;

public class EventDebugInfoOuting extends Event {
	public final List<String> debufInfos = new LinkedList<>();

	public EventDebugInfoOuting() {
		super(false, false);
	}
}
