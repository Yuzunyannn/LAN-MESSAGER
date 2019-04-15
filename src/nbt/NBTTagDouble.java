package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagDouble extends NBTPrimitive {
	/** 数据 */
	private double data;

	NBTTagDouble() {
	}

	public NBTTagDouble(double data) {
		this.data = data;
	}

	@Override
	void write(DataOutput output) throws IOException {
		output.writeDouble(this.data);

	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		this.data = input.readDouble();
	}

	@Override
	public byte getId() {
		return NBTBase.TAG_DOUBLE;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagDouble(this.data);
	}

	@Override
	public String toString() {
		return this.data + "d";
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && this.data == ((NBTTagDouble) other).data;
	}

	@Override
	public int hashCode() {
		long i = Double.doubleToLongBits(this.data);
		return super.hashCode() ^ (int) (i ^ i >>> 32);
	}

	public double get() {
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
