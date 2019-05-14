package network.event;

import java.net.Socket;

import event.Event;
import network.Network;

/** 当接受到一个新的socket时候的事件 */
public class EventSocketAccept extends Event {
	public final Socket socket;
	public final Network net;

	public EventSocketAccept(Network net, Socket socket) {
		super(true, true);
		this.socket = socket;
		this.net = net;
	}
}
