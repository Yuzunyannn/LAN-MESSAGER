package client.frame.info;

import java.awt.Component;

import client.event.EventChatOperation;
import client.event.EventShow;
import client.event.EventsBridge;
import client.event.EventRecv.EventRecvString;
import event.IEventBus;
import event.SubscribeEvent;
import user.User;

public class ChatScrollPanel extends BaseScrollPanel {
public ChatScrollPanel() {
	super();
}
	@Override
	public void initEvent(IEventBus bus) {
		bus.register(this);
	}
	
	/**聊天状态清空*/
	public void chatStateClear() {
			content = p.getComponents();
			for (Component i : content) {
				((ChatListButton) i).isChoose(false);
			}
			upDateButton();
		
	}
	public void upDateButton() {
		p.removeAll();
		for (Component i : content) {
			p.add(i);
		}
		content=p.getComponents();
		this.refresh();
	}
	@SubscribeEvent
	/**选中*/
	public void onShow(EventShow e) {
		for (int i = 0; i < content.length; i++) {
			ChatListButton tmp = (ChatListButton) content[i];
			if (tmp.getName().equals(e.id))
				tmp.isChoose(true);
			else
				tmp.isChoose(false);
		}
		this.refresh();
	}
	private void countChange(User from) {
		if(!BaseScrollPanel.userSet.contains(from)) {
			EventsBridge.frontendEventHandle
			.post(new EventChatOperation(from.userName, EventChatOperation.ADDCHAT, "Chat"));
		}
		else {
		int index=getNameIndex(from.userName);
		((BasePanelButton)content[index]).recvMessageShow();
		upDateButton();
		}
		
	}
	@SubscribeEvent
	public void onCountFile(transfer.EventFileRecv.Start e) {
		countChange(e.getFrom());
	}

	@SubscribeEvent
	public void onCountMsg(EventRecvString e) {
		countChange(e.from);
	}
}
