package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/** 使用Minecraft的NBT结构的基类 */
public abstract class NBTBase {

	/** NBT不同的类型对应的编号 */

	public static final int TAG_END = 0;
	public static final int TAG_BYTE = 1;
	public static final int TAG_SHORT = 2;
	public static final int TAG_INT = 3;
	public static final int TAG_LONG = 4;
	public static final int TAG_FLOAT = 5;
	public static final int TAG_DOUBLE = 6;
	public static final int TAG_BYTES = 7;
	public static final int TAG_STRING = 8;
	public static final int TAG_LIST = 9;
	public static final int TAG_COMPOUND = 10;
	public static final int TAG_INTS = 11;
	public static final int TAG_LONGS = 12;
	/** 数字判定使用 */
	public static final int TAG_NUMBER = 99;

	/** tag写数据 */
	abstract void write(DataOutput output) throws IOException;

	/** tag读数据 */
	abstract void read(DataInput input, int depth) throws IOException;

	/** tag的id */
	public abstract byte getId();

	/** 返回tag是否没有内容 */
	public boolean hasNoTags() {
		return false;
	}

	/** 复制 */
	public abstract NBTBase copy();

	@Override
	public int hashCode() {
		return this.getId();
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof NBTBase && this.getId() == ((NBTBase) other).getId();
	}

	@Override
	public Object clone() {
		return this.copy();
	}

	/** 根据id创建Tag */
	protected static NBTBase createNewByType(byte id) {
		switch (id) {
		case 0:
			return new NBTTagEnd();
		case 1:
			return new NBTTagByte();
		case 2:
			return new NBTTagShort();
		case 3:
			return new NBTTagInt();
		case 4:
			return new NBTTagLong();
		case 5:
			return new NBTTagFloat();
		case 6:
			return new NBTTagDouble();
		case 7:
			return new NBTTagByteArray();
		case 8:
			return new NBTTagString();
		case 9:
			return new NBTTagList();
		case 10:
			return new NBTTagCompound();
		case 11:
			return new NBTTagIntArray();
		case 12:
			return new NBTTagLongArray();

		default:
			return null;
		}
	}
}
