package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagDouble extends NBTBase {
	/** Êı¾İ */
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
}
