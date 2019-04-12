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

	/** 从buffer里读入 */
	public static NBTTagCompound read(ByteBuffer buffer) throws IOException {
		InputStream inputStream = new ByteArrayInputStream(buffer.array());
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		try {
			return read(dataInputStream);
		} finally {
			dataInputStream.close();
		}
	}

	/** 向buffer里写入 */
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

	/** 读取NBT从File中 */
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

	/** 写入NBT到File中 */
	public static void write(File file, NBTTagCompound nbt) throws IOException {
		DataOutputStream dataInputStream = new DataOutputStream(new FileOutputStream(file));
		try {
			write(dataInputStream, nbt, "major");
		} finally {
			dataInputStream.close();
		}
	}

	/** 读取NBT从Stream中 */
	public static NBTTagCompound read(DataInput input) throws IOException {
		NBTBase nbtbase = read(input, 0);
		if (nbtbase instanceof NBTTagCompound) {
			return (NBTTagCompound) nbtbase;
		} else {
			throw new IOException("根目tag必须为compound");
		}
	}

	/** 写入NBT到Stream中 */
	public static void write(DataOutput output, NBTTagCompound nbt, String name) throws IOException {
		output.writeByte(NBTBase.TAG_COMPOUND);
		output.writeUTF(name);
		nbt.write(output);
	}

	/** 读取NBT从Stream中 */
	private static NBTBase read(DataInput input, int depth) throws IOException {
		byte tagId = input.readByte();
		if (tagId == 0) {
			return new NBTTagEnd();
		} else {
			input.readUTF();
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
	}

	/** 记录错误报告 */
	protected static void ExceptionReport(IOException e) {

	}
}
