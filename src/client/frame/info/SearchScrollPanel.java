package client.frame.info;

import client.event.EventSearchRequest;
import event.IEventBus;
import event.SubscribeEvent;
import user.User;

public class SearchScrollPanel extends BaseScrollPanel {
	private static final long serialVersionUID = 1L;
public SearchScrollPanel() {
	super();
}
@Override
public void initEvent(IEventBus bus) {
	bus.register(this);
}
@SubscribeEvent
public void onSearchRequest(EventSearchRequest e) {
for(User utmp:e.name) {
	BasePanelButton button=new SearchListButton(utmp.userName,utmp.userName);
	addNewMember(button);
}
	this.refresh();
}
}
