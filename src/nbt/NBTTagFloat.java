package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagFloat extends NBTBase {
	/** 数据 */
	private float data;

	NBTTagFloat() {
	}

	public NBTTagFloat(float data) {
		this.data = data;
	}

	@Override
	void write(DataOutput output) throws IOException {
		output.writeFloat(this.data);

	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		this.data = input.readFloat();
	}

	@Override
	public byte getId() {
		return NBTBase.TAG_FLOAT;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagFloat(this.data);
	}

	@Override
	public String toString() {
		return this.data + "f";
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && this.data == ((NBTTagFloat) other).data;
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Float.floatToIntBits(this.data);
	}

	public float get() {
		return this.data;
	}
}
