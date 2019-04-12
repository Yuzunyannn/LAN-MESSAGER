package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class NBTTagCompound extends NBTBase {

	/** 数据图 */
	private final Map<String, NBTBase> tagMap = new HashMap<String, NBTBase>();

	@Override
	void write(DataOutput output) throws IOException {
		for (String name : this.tagMap.keySet()) {
			NBTBase nbtbase = this.tagMap.get(name);
			output.writeByte(nbtbase.getId());
			if (nbtbase.getId() != 0) {
				output.writeUTF(name);
				nbtbase.write(output);
			}
		}
		output.writeByte(NBTBase.TAG_END);
	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		if (depth > 512)
			throw new RuntimeException("NBT标签的过于复杂，深度：" + depth);
		this.tagMap.clear();
		byte b;
		while ((b = input.readByte()) != 0) {
			String key = input.readUTF();
			NBTBase nbtbase = readNBT(b, key, input, depth + 1);
			this.tagMap.put(key, nbtbase);
		}
	}

	/** 读一个tag */
	static NBTBase readNBT(byte tagId, String key, DataInput input, int depth) throws IOException {
		NBTBase nbtbase = NBTBase.createNewByType(tagId);
		if (nbtbase == null)
			throw new RuntimeException("无效的TAG标签，tagID:" + tagId);
		try {
			nbtbase.read(input, depth);
			return nbtbase;
		} catch (IOException ioexception) {
			NBTStream.ExceptionReport(ioexception);
			throw ioexception;
		}
	}

	@Override
	public byte getId() {
		return NBTBase.TAG_COMPOUND;
	}

	@Override
	public NBTTagCompound copy() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		for (String s : this.tagMap.keySet()) {
			nbttagcompound.setTag(s, ((NBTBase) this.tagMap.get(s)).copy());
		}
		return nbttagcompound;
	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other)
				&& Objects.equals(this.tagMap.entrySet(), ((NBTTagCompound) other).tagMap.entrySet());
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ this.tagMap.hashCode();
	}

	/** 合并 */
	public void merge(NBTTagCompound other) {
		for (String key : other.tagMap.keySet()) {
			NBTBase nbtbase = other.tagMap.get(key);
			if (nbtbase.getId() == NBTBase.TAG_COMPOUND) {
				if (this.hasKey(key, NBTBase.TAG_COMPOUND)) {
					NBTTagCompound nbttagcompound = this.getCompoundTag(key);
					nbttagcompound.merge((NBTTagCompound) nbtbase);
				} else {
					this.setTag(key, nbtbase.copy());
				}
			} else {
				this.setTag(key, nbtbase.copy());
			}
		}
	}

	/** 获取tag数量 */
	public int getSize() {
		return this.tagMap.size();
	}

	/** 获取key的集合 */
	public Set<String> getKeySet() {
		return this.tagMap.keySet();
	}

	/** 设置tag */
	public void setTag(String key, NBTBase value) {
		this.tagMap.put(key, value);
	}

	/** 获取tag的id */
	public byte getTagId(String key) {
		NBTBase nbtbase = this.tagMap.get(key);
		return nbtbase == null ? 0 : nbtbase.getId();
	}

	/** 是否有指定key */
	public boolean hasKey(String key) {
		return this.tagMap.containsKey(key);
	}

	/** 是否有指定类型的key */
	public boolean hasKey(String key, int type) {
		int i = this.getTagId(key);
		if (i == type) {
			return true;
		}
		return false;
	}

	/** 移除tag */
	public void removeTag(String key) {
		this.tagMap.remove(key);
	}

	/** 获取指定位置是Compound的tag */
	public NBTTagCompound getCompoundTag(String key) {
		if (this.hasKey(key, NBTBase.TAG_COMPOUND)) {
			return (NBTTagCompound) this.tagMap.get(key);
		}
		return new NBTTagCompound();
	}

	@Override
	public boolean hasNoTags() {
		return this.tagMap.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder stringbuilder = new StringBuilder("{");
		Collection<String> collection = this.tagMap.keySet();

		for (String key : collection) {
			if (stringbuilder.length() != 1) {
				stringbuilder.append(',');
			}
			stringbuilder.append(handleEscape(key)).append(':').append(this.tagMap.get(key));
		}
		return stringbuilder.append('}').toString();
	}

	private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");

	protected static String handleEscape(String str) {
		return SIMPLE_VALUE.matcher(str).matches() ? str : NBTTagString.quoteAndEscape(str);
	}

}
