package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTBase {

	/** Êý¾Ý */
	private byte data;

	NBTTagByte() {
	}

	public NBTTagByte(byte data) {
		this.data = data;
	}

	@Override
	void write(DataOutput output) throws IOException {
		output.writeByte(this.data);
	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		this.data = input.readByte();
	}

	@Override
	public byte getId() {
		return NBTBase.TAG_BYTE;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagByte(this.data);
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ this.data;
	}

	@Override
	public String toString() {
		return this.data + "b";
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && this.data == ((NBTTagByte) other).data;
	}

	public byte get() {
		return this.data;
	}

}
