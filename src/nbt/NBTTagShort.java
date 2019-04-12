package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTBase {

	/** 数据 */
	private short data;

	public NBTTagShort() {
	}

	public NBTTagShort(short data) {
		this.data = data;
	}

	@Override
	void write(DataOutput output) throws IOException {
		output.writeShort(this.data);
	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		this.data = input.readShort();
	}

	@Override
	public byte getId() {
		return NBTBase.TAG_SHORT;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagShort(this.data);
	}

	@Override
	public String toString() {
		return this.data + "s";
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ this.data;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && this.data == ((NBTTagShort) other).data;
	}

	public short get() {
		return this.data;
	}
}
