package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong extends NBTBase {

	/** 数据 */
	private long data;

	NBTTagLong() {
	}

	public NBTTagLong(long data) {
		this.data = data;
	}

	@Override
	void write(DataOutput output) throws IOException {
		output.writeLong(this.data);
	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		this.data = input.readLong();
	}

	@Override
	public byte getId() {
		return NBTBase.TAG_LONG;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagLong(this.data);
	}

	@Override
	public String toString() {
		return this.data + "L";
	}

	@Override
	public boolean equals(Object ohter) {
		return super.equals(ohter) && this.data == ((NBTTagLong) ohter).data;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ (int) (this.data ^ this.data >>> 32);
	}

	public long get() {
		return this.data;
	}

}
