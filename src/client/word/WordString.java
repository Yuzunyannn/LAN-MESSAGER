package client.word;

public class WordString extends Word {

	private String str;

	public WordString(String str) {
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
