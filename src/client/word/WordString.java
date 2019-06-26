package client.word;

public class WordString extends Word {

	private String str;

	public WordString(String str) {
		super(Word.STRING);
		this.str = str;
	}

	public WordString(String str, long time) {
		super(Word.STRING, time);
		this.str = str;
	}

	public String getString() {
		return this.str;
	}

	@Override
	public String getValue() {
		return str;
	}

}
