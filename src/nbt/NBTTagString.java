package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase {
	/** 数据 */
	private String data;

	public NBTTagString() {
		this("");
	}

	public NBTTagString(String data) {
		if (data == null)
			data = "";
		this.data = data;
	}

	@Override
	void write(DataOutput output) throws IOException {
		output.writeUTF(this.data);
	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		this.data = input.readUTF();
	}

	@Override
	public byte getId() {
		return NBTBase.TAG_STRING;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagString(this.data);
	}

	@Override
	public boolean hasNoTags() {
		return this.data.isEmpty();
	}

	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) {
			return false;
		} else {
			NBTTagString nbttagstring = (NBTTagString) other;
			return this.data == null && nbttagstring.data == null || this.data.equals(nbttagstring.data);
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ this.data.hashCode();
	}

	@Override
	public String toString() {
		return this.data;
	}

	/** 获取字符串 */
	public String get() {
		return this.data;
	}

	/** 字符转义化 */
	public static String quoteAndEscape(String str) {
		StringBuilder stringbuilder = new StringBuilder("\"");
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			if (ch == '\\' || ch == '"') {
				stringbuilder.append('\\');
			}
			stringbuilder.append(ch);
		}
		return stringbuilder.append('"').toString();
	}

}
