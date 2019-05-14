package network.event;

import event.Event;
import network.Connection;
import network.Network;

/** 一个新的连接进入后 */
public class EventNewConnection extends Event {
	public final Connection con;
	public final Network net;

	public EventNewConnection(Network net, Connection con) {
		super(true, false);
		this.con = con;
		this.net = net;
	}
}
