package client.record;

import client.word.Word;
import user.User;

public class RecordValue {
	public final Word word;
	public final User whos;

	public RecordValue(Word word, User user) {
		this.whos = user;
		this.word = word;
	}
}
