package client.word;

public abstract class Word {

	public abstract String getValue();

	@Override
	public String toString() {
		return this.getValue();
	}
}
