package client.word;

public abstract class Word {

	public abstract String getValue();
	public final byte id;
	public static final byte FILE = 1;
	public static final byte STRING = 2;
	protected Word(byte id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return this.getValue();
	}
}
