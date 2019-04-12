package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagLongArray extends NBTBase {

	/** 数据 */
	private long[] data;

	NBTTagLongArray() {
	}

	public NBTTagLongArray(long[] data) {
		this.data = data;
	}

	public NBTTagLongArray(List<Long> list) {
		this(toArray(list));
	}

	private static long[] toArray(List<Long> list) {
		long[] aint = new long[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			Long oint = list.get(i);
			aint[i] = oint == null ? 0 : oint.intValue();
		}
		return aint;
	}

	@Override
	void write(DataOutput output) throws IOException {
		output.writeInt(this.data.length);
		for (long i : this.data)
			output.writeLong(i);
	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		int i = input.readInt();
		this.data = new long[i];
		for (int j = 0; j < i; j++)
			this.data[j] = input.readLong();
	}

	@Override
	public byte getId() {
		return NBTBase.TAG_LONGS;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagLongArray(this.data.clone());
	}

	@Override
	public String toString() {
		StringBuilder stringbuilder = new StringBuilder("[L;");
		for (int i = 0; i < this.data.length; ++i) {
			if (i != 0) {
				stringbuilder.append(',');
			}

			stringbuilder.append(this.data[i]).append('L');
		}
		return stringbuilder.append(']').toString();
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && Arrays.equals(this.data, ((NBTTagLongArray) other).data);
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(this.data);
	}

	public long[] getLongArray() {
		return this.data;
	}

}
