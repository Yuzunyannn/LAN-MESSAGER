package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTPrimitive {

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

	@Override
	public byte getByte() {
		return (byte) this.data;
	}

	@Override
	public short getShort() {
		return (short) this.data;
	}

	@Override
	public int getInt() {
		return (int) this.data;
	}

	@Override
	public long getLong() {
		return (long) this.data;
	}

	@Override
	public float getFloat() {
		return (float) this.data;
	}

	@Override
	public double getDouble() {
		return (double) this.data;
	}

}
