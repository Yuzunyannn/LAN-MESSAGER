package client.event;

import java.util.List;

import client.word.Word;
import event.Event;
import user.User;

public class EventSendInputWords extends Event {

	public final List<Word> words;
	public final User toUser;

	public EventSendInputWords(List<Word> words, User toUser) {
		super(false, false);
		this.words = words;
		this.toUser = toUser;
	}
}
