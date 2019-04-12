package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd extends NBTBase {

	
	@Override
	void write(DataOutput output) throws IOException {
	}

	@Override
	void read(DataInput input, int depth) throws IOException {
	}

	@Override
	public byte getId() {
		return NBTBase.TAG_END;
	}

	@Override
	public NBTBase copy() {
		return new NBTTagEnd();
	}

	@Override
	public String toString() {
		return "END";
	}

}
