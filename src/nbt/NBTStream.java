package nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

public class NBTStream {

	/** ��buffer����� */
	public static NBTTagCompound read(ByteBuffer buffer) throws IOException {
		InputStream inputStream = new ByteArrayInputStream(buffer.array());
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		try {
			return read(dataInputStream);
		} finally {
			dataInputStream.close();
		}
	}

	/** ��buffer��д�� */
	public static void write(ByteBuffer buffer, NBTTagCompound nbt) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		try {
			write(dataOutputStream, nbt, "net");
			buffer.put(outputStream.toByteArray());
		} finally {
			dataOutputStream.close();
		}
	}

	/** ��ȡNBT��File�� */
	@SuppressWarnings("resource")
	public static NBTTagCompound read(File file) throws IOException {
		if (!file.exists())
			return null;
		InputStream inputStream = new FileInputStream(file);
		int a = inputStream.read();
		int b = inputStream.read();
		inputStream = new FileInputStream(file);
		if (a == 0x1F && b == 0x8B) {
			inputStream = new GZIPInputStream(inputStream);
		} 
		DataInputStream dataInputStream = new DataInputStream(inputStream);

		NBTTagCompound nbt;
		try {
			nbt = read(dataInputStream);
		} finally {
			dataInputStream.close();
		}
		return nbt;
	}

	/** д��NBT��File�� */
	public static void write(File file, NBTTagCompound nbt) throws IOException {
		DataOutputStream dataInputStream = new DataOutputStream(new FileOutputStream(file));
		try {
			write(dataInputStream, nbt, "major");
		} finally {
			dataInputStream.close();
		}
	}

	/** ��ȡNBT��Stream�� */
	public static NBTTagCompound read(DataInput input) throws IOException {
		NBTBase nbtbase = read(input, 0);
		if (nbtbase instanceof NBTTagCompound) {
			return (NBTTagCompound) nbtbase;
		} else {
			throw new IOException("��Ŀtag����Ϊcompound");
		}
	}

	/** д��NBT��Stream�� */
	public static void write(DataOutput output, NBTTagCompound nbt, String name) throws IOException {
		output.writeByte(NBTBase.TAG_COMPOUND);
		output.writeUTF(name);
		nbt.write(output);
	}

	/** ��ȡNBT��Stream�� */
	private static NBTBase read(DataInput input, int depth) throws IOException {
		byte tagId = input.readByte();
		if (tagId == 0) {
			return new NBTTagEnd();
		} else {
			input.readUTF();
			NBTBase nbtbase = NBTBase.createNewByType(tagId);
			if (nbtbase == null)
				throw new RuntimeException("��Ч��TAG��ǩ��tagID:" + tagId);
			try {
				nbtbase.read(input, depth);
				return nbtbase;
			} catch (IOException ioexception) {
				NBTStream.ExceptionReport(ioexception);
				throw ioexception;
			}
		}
	}

	/** ��¼���󱨸� */
	protected static void ExceptionReport(IOException e) {

	}
}
