package nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NBTTagList extends NBTBase implements java.lang.Iterable<NBTBase> {
	/** ���Ա� */
	private List<NBTBase> tagList = new ArrayList<NBTBase>();
	/** List�涨���е���Ŀ����һ�����ñ�����¼��һ����������� */
	private byte tagType = 0;

	@Override
	void write(DataOutput output) throws IOException {
		// ���²��Ե�һ������
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
			throw new RuntimeException("NBT��ǩ�Ĺ��ڸ��ӣ���ȣ�" + depth);
		// ����
		this.tagType = input.readByte();
		// ���鳤��
		int i = input.readInt();
		if (this.tagType == 0 && i > 0)
			throw new RuntimeException("NBTagTList��¼��Tag���ִ���");
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

	/** ��ȡ��NBTTagList������ */
	public int getTagType() {
		return this.tagType;
	}

	private void warn(String str) {

	}

	public int tagCount() {
		return this.tagList.size();
	}

	public int size() {
		return this.tagList.size();
	}

	public NBTBase get(int idx) {
		return (NBTBase) (idx >= 0 && idx < this.tagList.size() ? (NBTBase) this.tagList.get(idx) : new NBTTagEnd());
	}

	public void appendTag(NBTBase nbt) {
		if (nbt.getId() == 0) {
			this.warn("NBTagTList���޷������Ч�ı�ǩ��NBTEnd��");
			return;
		}
		if (this.tagType == 0) {
			this.tagType = nbt.getId();
		} else if (this.tagType != nbt.getId()) {
			this.warn("NBTagTList��ӵ�����Ҫ����һ����ӵ�������ͬ");
			return;
		}

		this.tagList.add(nbt);
	}

	public void set(int idx, NBTBase nbt) {
		if (nbt.getId() == 0) {
			this.warn("NBTList���޷������Ч�ı�ǩ��NBTEnd��");
			return;
		}
		if (idx >= 0 && idx < this.tagList.size()) {
			if (this.tagType == 0) {
				this.tagType = nbt.getId();
			} else if (this.tagType != nbt.getId()) {
				this.warn("NBTagTList��ӵ�����Ҫ����һ����ӵ�������ͬ");
				return;
			}
			this.tagList.set(idx, nbt);
		} else {
			this.warn("����NBTagTList��ʱ�±�Խ���ˣ�");
		}
	}

	/** ɾ��tag */
	public NBTBase removeTag(int index) {
		return this.tagList.remove(index);
	}

	@Override
	public Iterator<NBTBase> iterator() {
		return tagList.iterator();
	}

}
