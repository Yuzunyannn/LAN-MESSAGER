package nbt;

public interface INBTSerializable<T extends NBTBase> {
	T serializeNBT();

	void deserializeNBT(T nbt);
}
