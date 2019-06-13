package client.event;
import event.Event;
import user.User;
/** 点击聊天用户事件，用于显示某个用户对本地的发送消息*/
public class EventShow extends Event {
	public final User user;
public  EventShow(User choose) {
	super(false,false);
	this.user=choose;
}
}
