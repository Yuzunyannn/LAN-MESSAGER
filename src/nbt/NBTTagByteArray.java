package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagByteArray extends NBTBase {
	/** 数据 */
	private byte[] data;

	NBTTagByteArray() {
	}

	public NBTTagByteArray(byte[] data) {
		this.data = data;
	}

	public NBTTagByteArray(List<Byte> list) {
		this(toArray(list));
	}

	private static byte[] toArray(List<Byte> list) {
		byte[] abyte = new byte[list.size()];

		for (int i = 0; i < list.size(); ++i) {
			Byte obyte = list.get(i);
			abyte[i] = obyte == null ? 0 : obyte.byteValue();
		}
		return abyte;
	}

	@Override
	void write(DataOutput output) throws IOException {
		output.writeInt(this.data.length);
		output.write(this.data);
	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		int i = input.readInt();
		this.data = new byte[i];
		input.readFully(this.data);
	}

	@Override
	public byte getId() {
		return NBTBase.TAG_BYTES;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagByteArray(this.data.clone());
	}

	@Override
	public String toString() {
		StringBuilder stringbuilder = new StringBuilder("[B;");
		for (int i = 0; i < this.data.length; ++i) {
			if (i != 0) {
				stringbuilder.append(',');
			}
			stringbuilder.append((int) this.data[i]).append('B');
		}
		return stringbuilder.append(']').toString();
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(this.data);
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && Arrays.equals(this.data, ((NBTTagByteArray) other).data);
	}

	public byte[] getByteArray() {
		return this.data;
	}

}
