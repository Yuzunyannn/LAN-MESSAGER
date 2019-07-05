package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTPrimitive {

	/** 数据 */
	private int data;

	public NBTTagInt() {
	}

	public NBTTagInt(int data) {
		this.data = data;
	}

	@Override
	void write(DataOutput output) throws IOException {
		output.writeInt(this.data);

	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		this.data = input.readInt();

	}

	@Override
	public byte getId() {
		return NBTBase.TAG_INT;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagInt(this.data);
	}

	@Override
	public String toString() {
		return String.valueOf(this.data);
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ this.data;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && this.data == ((NBTTagInt) other).data;
	}

	public int get() {
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
