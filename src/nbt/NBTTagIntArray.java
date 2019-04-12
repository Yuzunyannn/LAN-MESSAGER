package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagIntArray extends NBTBase {
	/** 数据 */
	private int[] data;

	NBTTagIntArray() {
	}

	public NBTTagIntArray(int[] data) {
		this.data = data;
	}

	public NBTTagIntArray(List<Integer> list) {
		this(toArray(list));
	}

	private static int[] toArray(List<Integer> list) {
		int[] aint = new int[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			Integer oint = list.get(i);
			aint[i] = oint == null ? 0 : oint.intValue();
		}
		return aint;
	}

	@Override
	void write(DataOutput output) throws IOException {
		output.writeInt(this.data.length);
		for (int i : this.data)
			output.writeInt(i);
	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		int i = input.readInt();
		this.data = new int[i];
		for (int j = 0; j < i; j++)
			this.data[j] = input.readInt();
	}

	@Override
	public byte getId() {
		return NBTBase.TAG_INTS;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagIntArray(this.data.clone());
	}

	@Override
	public String toString() {
		StringBuilder stringbuilder = new StringBuilder("[I;");
		for (int i = 0; i < this.data.length; ++i) {
			if (i != 0) {
				stringbuilder.append(',');
			}
			stringbuilder.append(this.data[i]);
		}
		return stringbuilder.append(']').toString();
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) && Arrays.equals(this.data, ((NBTTagIntArray) other).data);
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ Arrays.hashCode(this.data);
	}

	public int[] getIntArray() {
		return this.data;
	}

}
