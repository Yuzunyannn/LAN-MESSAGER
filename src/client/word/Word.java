package client.word;

import java.text.SimpleDateFormat;

public abstract class Word {

	public abstract String getValue();

	public final long time;
	public final byte id;
	public static final byte FILE = 1;
	public static final byte STRING = 2;

	protected Word(byte id) {
		this.id = id;
		time = System.currentTimeMillis();
	}

	protected Word(byte id, long time) {
		this.id = id;
		this.time = time;
	}

	@Override
	public String toString() {
		return this.getValue();
	}

	public String getTime() {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = dateformat.format(time);
		return dateStr;
	}
}
