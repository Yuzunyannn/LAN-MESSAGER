package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NBTTagList extends NBTBase implements java.lang.Iterable<NBTBase> {
	/** 线性表 */
	private List<NBTBase> tagList = new ArrayList<NBTBase>();
	/** List规定所有的项目必须一样，该变量记录第一个加入的类型 */
	private byte tagType = 0;

	@Override
	void write(DataOutput output) throws IOException {
		// 从新测试第一个类型
		if (this.tagList.isEmpty()) {
			this.tagType = 0;
		} else {
			this.tagType = ((NBTBase) this.tagList.get(0)).getId();
		}
		output.writeByte(this.tagType);
		output.writeInt(this.tagList.size());
		for (int i = 0; i < this.tagList.size(); ++i) {
			((NBTBase) this.tagList.get(i)).write(output);
		}
	}

	@Override
	void read(DataInput input, int depth) throws IOException {
		if (depth > 512)
			throw new RuntimeException("NBT标签的过于复杂，深度：" + depth);
		// 类型
		this.tagType = input.readByte();
		// 数组长度
		int i = input.readInt();
		if (this.tagType == 0 && i > 0)
			throw new RuntimeException("NBTagTList记录的Tag出现错误！");
		this.tagList = new ArrayList<NBTBase>(i);
		for (int j = 0; j < i; j++) {
			NBTBase nbtbase = NBTBase.createNewByType(this.tagType);
			nbtbase.read(input, depth + 1);
			this.tagList.add(nbtbase);
		}

	}

	@Override
	public byte getId() {
		return NBTBase.TAG_LIST;
	}

	@Override
	public NBTBase copy() {
		NBTTagList nbttaglist = new NBTTagList();
		nbttaglist.tagType = this.tagType;
		for (NBTBase nbtbase : this.tagList) {
			nbttaglist.tagList.add(nbtbase.copy());
		}
		return nbttaglist;
	}

	@Override
	public String toString() {
		StringBuilder stringbuilder = new StringBuilder("[");
		for (int i = 0; i < this.tagList.size(); ++i) {
			if (i != 0) {
				stringbuilder.append(',');
			}
			stringbuilder.append(this.tagList.get(i));
		}
		return stringbuilder.append(']').toString();
	}

	@Override
	public boolean hasNoTags() {
		return this.tagList.isEmpty();
	}

	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) {
			return false;
		} else {
			NBTTagList nbttaglist = (NBTTagList) other;
			return this.tagType == nbttaglist.tagType && this.tagList.equals(nbttaglist.tagList);
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ this.tagList.hashCode();
	}

	/** 获取该NBTTagList的类型 */
	public int getTagType() {
		return this.tagType;
	}

	/** 警告使用 */
	private void warn(String str) {

	}

	/** 标签数量 */
	public int tagCount() {
		return this.tagList.size();
	}

	/** 大小（同 标签数量） */
	public int size() {
		return this.tagList.size();
	}

	/** 获取tag */
	public NBTBase get(int idx) {
		return (NBTBase) (idx >= 0 && idx < this.tagList.size() ? (NBTBase) this.tagList.get(idx) : new NBTTagEnd());
	}

	/** 添加tag */
	public void appendTag(NBTBase nbt) {
		if (nbt.getId() == 0) {
			this.warn("NBTagTList中无法添加无效的标签（NBTEnd）");
			return;
		}
		if (this.tagType == 0) {
			this.tagType = nbt.getId();
		} else if (this.tagType != nbt.getId()) {
			this.warn("NBTagTList添加的内容要与上一次添加的内容相同");
			return;
		}

		this.tagList.add(nbt);
	}

	/** 设置tag */
	public void set(int idx, NBTBase nbt) {
		if (nbt.getId() == 0) {
			this.warn("NBTList中无法添加无效的标签（NBTEnd）");
			return;
		}
		if (idx >= 0 && idx < this.tagList.size()) {
			if (this.tagType == 0) {
				this.tagType = nbt.getId();
			} else if (this.tagType != nbt.getId()) {
				this.warn("NBTagTList添加的内容要与上一次添加的内容相同");
				return;
			}
			this.tagList.set(idx, nbt);
		} else {
			this.warn("操作NBTagTList的时下标越界了！");
		}
	}

	/** 删除tag */
	public NBTBase removeTag(int index) {
		return this.tagList.remove(index);
	}

	/** 添加数字 */
	public void appendTag(int i) {
		this.appendTag(new NBTTagInt(i));
	}

	/** 添加字符串 */
	public void appendTag(String str) {
		this.appendTag(new NBTTagString(str));
	}

	/** 获取指定位置的compuound */
	public NBTTagCompound getCompoundTagAt(int idx) {
		NBTBase nbtbase = this.get(idx);
		if (nbtbase.getId() == NBTBase.TAG_COMPOUND)
			return (NBTTagCompound) nbtbase;
		return new NBTTagCompound();
	}

	/** 获取指定位置的int */
	public int getIntAt(int idx) {
		NBTBase nbtbase = this.get(idx);
		if (nbtbase.getId() == NBTBase.TAG_INT)
			return ((NBTTagInt) nbtbase).get();
		return 0;
	}

	/** 获取指定位置的float */
	public float getFloatAt(int idx) {
		NBTBase nbtbase = this.get(idx);
		if (nbtbase.getId() == NBTBase.TAG_FLOAT)
			return ((NBTTagFloat) nbtbase).get();
		return 0;
	}

	@Override
	public Iterator<NBTBase> iterator() {
		return tagList.iterator();
	}

}
