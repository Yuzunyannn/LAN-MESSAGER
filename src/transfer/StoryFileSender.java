package transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import client.user.UserClient;
import log.Logger;
import nbt.NBTTagCompound;
import story.Story;
import user.UOnline;
import user.User;

public class StoryFileSender extends Story {

	static public final byte FLAG_FINISH = 0x20;
	static public final byte FLAG_FAIL = 0x10;
	static public final byte FLAG_FORBIDDEN = 0x01;

	/** 提供并发送文件的用户 */
	User daddy;
	/** 发送文件的用户作为验证的字符串 */
	int daddysKey;
	/** 文件信息，发送者是要发送的文件，接收者是接受的文件 */
	File want = null;
	/** 文件大小 */
	long fileSize = 0;
	/** 文件名称 */
	String fileName = "";
	/** 标记 */
	byte flags = 0;
	/** 文件输入流 */
	FileInputStream fileIStream = null;
	/** 文件输出流个 */
	FileOutputStream fileOStream = null;
	/** 就收文件的用户信息表 */
	List<UserFileInfo> usersFileInfoList = new LinkedList<UserFileInfo>();
	/** 成功接受的人数 */
	int sons = 0;

	/** 获取传输或接受的文件，文件命是临时名称 */
	public File getFile() {
		return this.want;
	}

	/** 获取真实文件命 */
	public String getFileName() {
		return this.fileName;
	}

	/** 获取发送者 */
	public User getSender() {
		return this.daddy;
	}

	/** 获取当前传输接受进度 */
	public float getProgress() {
		return (float) this.byteAt / this.fileSize;
	}

	private static class UserFileInfo {
		public long byteAt = 0;
		public User user;
		public FileInputStream fileIStream = null;

		public void close() {
			if (fileIStream == null)
				return;
			try {
				fileIStream.close();
			} catch (Exception e) {
			}
			fileIStream = null;
		}
	}

	public static void initStory(StoryFileSender story, User sender, int key) {
		story.daddy = sender;
		story.daddysKey = key;
		story.addMember(sender);
	}

	public static void initStory(StoryFileSender story, User sender, String realName, File file) {
		story.daddy = sender;
		story.daddysKey = 0;
		story.flags = FLAG_FINISH;
		story.want = file;
		story.fileSize = file.length();
		story.fileName = realName;
		story.addMember(sender);
	}

	@Override
	public NBTTagCompound packetToClientUpdate() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("sd", this.daddy.getUserName());
		nbt.setInteger("key", this.daddysKey);
		return nbt;
	}

	@Override
	public void updateFromServerPacket(NBTTagCompound nbt) {
		this.daddy = UOnline.getInstance().getUser(nbt.getString("sd"));
		this.daddysKey = nbt.getInteger("key");
	}

	@Override
	protected void onGet(NBTTagCompound nbt, User from) {
		if (this.isServer()) {
			// 服务器只接受发送者个flags
			if (from.equals(daddy)) {
				if (nbt.hasKey("flags"))
					this.flags = nbt.getByte("flags");
				if (this.isFinish())
					this.finishRecv();
			}
		} else {
			if (nbt.hasKey("flags"))
				this.flags = nbt.getByte("flags");
			if (this.isFail()) {
				this.whenFail();
				return;
			} else if (this.isFinish()) {
				this.finishRecv();
				return;
			}
			if (UserClient.getClientUser().equals(daddy)) {
				return;
			}
		}
		if (nbt.hasKey("size")) {
			fileSize = nbt.getLong("size");
			fileName = nbt.getString("filename");
			if (this.want == null)
				this.fileRecvInit();
		} else {
			if (nbt.hasKey("datas"))
				this.recvTick(nbt);
		}
	}

	@Override
	protected void onCreate() {
		if (this.isClient()) {
			if (UserClient.getClientUser().equals(this.daddy)) {
				this.want = FileSenderManager.getFileWithKey(this.daddysKey);
				try {
					// 开发文件
					fileIStream = new FileInputStream(this.want);
					this.clientStartSend();
				} catch (FileNotFoundException e) {
					// 开发失败，直接结束
					Logger.log.warn("发送文件时候，打开文件失败！", e);
					this.fail();
				}
			}
		} else {
			Logger.log.impart(this, "文件传输已启动，发件者用户：" + this.daddy + "的文件");
		}
	}

	@Override
	protected void onEnd() {
		this.close();
	}

	// 初始化接收端临时文件存储
	private void fileRecvInit() {
		if (this.isServer()) {
			// 判断是否合法
			if (fileSize <= 0 || fileSize >= 1024 * 1024 * 10) {
				this.forbidden();
				return;
			}
		}
		Calendar cal = Calendar.getInstance();
		// 检测tmp目录
		File tmpFolder = new File("tmp");
		if (!tmpFolder.exists())
			tmpFolder.mkdir();
		// 生成临时名称
		String tmpName = "" + cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + +cal.get(Calendar.DATE)
				+ cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.MONTH) + fileName.hashCode();
		want = new File("tmp/" + tmpName);
		if (want.exists()) {
			tmpName = tmpName + cal.get(Calendar.MILLISECOND) + System.nanoTime();
			want = new File("tmp/" + tmpName);
		}
		try {
			want.createNewFile();
		} catch (IOException e) {
			Logger.log.warn("服务器创建临时文件时候出现异常！", e);
			this.fail();
			return;
		}
		// 创建输入流
		try {
			fileOStream = new FileOutputStream(want);
		} catch (FileNotFoundException e) {
			Logger.log.warn("服务器打开临时文件[输入流]时候出现异常！", e);
			this.fail();
			return;
		}
		FileSenderManager.eventHandle.post(new EventFileRecv.Start(this));
		Logger.log.impart(this, "准备接受用户：", this.daddy, "的文件:“", fileName, "”，临时文件名：“", tmpName, "”");
	}

	public boolean isFinish() {
		return (this.flags & FLAG_FINISH) != 0;
	}

	public boolean isFail() {
		return (this.flags & FLAG_FAIL) != 0;
	}

	// 生成头
	private NBTTagCompound getRecvHead() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("size", fileSize);
		nbt.setString("filename", fileName);
		return nbt;
	}

	// 发送者客户端开始
	private void clientStartSend() {
		fileSize = want.length();
		fileName = want.getName();
		this.sendData(this.getRecvHead());
		FileSenderManager.eventHandle.post(new transfer.EventFileSend.Start(this));
	}

	// 发送者客户端完成
	private void clientFinishSend() {
		if (fileIStream == null)
			return;
		this.close();
		fileIStream = null;
		this.flags |= FLAG_FINISH;
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte("flags", FLAG_FINISH);
		this.sendData(nbt);
		FileSenderManager.eventHandle.post(new transfer.EventFileSend.Finish(this));
	}

	// 服务端禁止发送
	private void forbidden() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte("flags", (byte) (FLAG_FAIL | FLAG_FORBIDDEN));
		this.sendData(nbt);
		this.setEnd();
	}

	// 完成接收
	private void finishRecv() {
		if (this.isServer()) {
			Logger.log.impart(this, "服务器临时文件：“", want.getName(), "”已全部接受完成！");
		} else {
			Logger.log.impart(this, "文件“", want.getName(), "”已全部接受完成！");
		}
		try {
			fileOStream.close();
		} catch (IOException e) {
			Logger.log.warn("接受文件后，关闭输出流失败！", e);
		}
		fileOStream = null;
		FileSenderManager.eventHandle.post(new EventFileRecv.Finish(this));
	}

	private void close() {
		if (fileIStream != null) {
			try {
				fileIStream.close();
			} catch (IOException e) {
				Logger.log.warn("关闭输入流失败！", e);
			}
			fileIStream = null;
		}
		if (fileOStream != null) {
			try {
				fileOStream.close();
			} catch (IOException e) {
				Logger.log.warn("关闭输出流失败！", e);
			}
			fileOStream = null;
		}
	}

	// 失败
	private void fail() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte("flags", FLAG_FAIL);
		this.sendData(nbt);
		want = null;
		this.setEnd();
		this.close();
	}

	/** 成功结束 */
	protected void successEnd() {
		Logger.log.impart(this, "文件传输已成功结束！总共发给了" + sons + "个用户");
	}

	// 处理失败情况
	private void whenFail() {
		if ((this.flags & FLAG_FORBIDDEN) != 0) {
			Logger.log.impart(this, "服务器拒绝用户" + this.daddy + "希望传输的文件！");
		} else {
			Logger.log.impart(this, "用户" + this.daddy + "文件传输失败！");
		}
		this.setEnd();
	}

	// 添加新成员
	@Override
	protected boolean onMemberJoin(User user) {
		if (this.isClient())
			return true;
		if (user.equals(this.daddy))
			return true;
		UserFileInfo info = new UserFileInfo();
		info.user = user;
		usersFileInfoList.add(info);
		return true;
	}

	@Override
	protected void onTick() {
		// 如果flgas是失败
		if (this.isFail()) {
			this.whenFail();
			return;
		}
		if (this.isClient()) {
			if (this.isFinish())
				return;
			// 读取流不为空，则发送数据
			if (fileIStream != null) {
				NBTTagCompound nbt = new NBTTagCompound();
				boolean finish = this.sendTick(nbt);
				if (finish)
					this.clientFinishSend();
				else
					this.sendData(nbt);
			}
			return;
		} else {
			if (usersFileInfoList.isEmpty()) {
				if (this.isFinish()) {
					this.successEnd();
					this.setEnd();
					return;
				}
			}
			if (this.want == null)
				return;
			// 根据每个用户的状态发送文件数据
			Iterator<UserFileInfo> itr = usersFileInfoList.iterator();
			while (itr.hasNext()) {
				UserFileInfo info = itr.next();
				if (info.byteAt == 0 && info.fileIStream == null) {
					this.sendData(this.getRecvHead(), info.user);
					try {
						info.fileIStream = new FileInputStream(this.want);
					} catch (FileNotFoundException e) {
						Logger.log.warn(this + "在给" + info.user + "建立输出流的时候，出现异常！");
						itr.remove();
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setByte("flags", FLAG_FAIL);
						this.sendData(nbt, info.user);
						return;
					}
				}
				if (!this.isFinish()) {
					if (info.byteAt > this.byteAt - bytesPreSend) {
						continue;
					}
				}
				NBTTagCompound nbt = new NBTTagCompound();
				boolean finish = this.sendTick(nbt, info);
				if (finish) {
					itr.remove();
					info.close();
					nbt = new NBTTagCompound();
					nbt.setByte("flags", FLAG_FINISH);
					this.sendData(nbt, info.user);
					this.removeMember(info.user);
					sons++;
				} else
					this.sendData(nbt, info.user);

			}
		}
	}

	public static final int bytesPreSend = 256 * 64;
	// 目前读的位置
	long byteAt = 0;

	// 发送一次,发送方调用（理应发送的用户调用）
	private boolean sendTick(NBTTagCompound nbt) {
		byte[] bytes = new byte[bytesPreSend];
		int length = 0;
		try {
			length = fileIStream.read(bytes);
			if (length == -1)
				return true;
			if (length < bytesPreSend)
				bytes = Arrays.copyOf(bytes, length);
			nbt.setByteArray("datas", bytes);
			nbt.setLong("at", byteAt);
		} catch (IOException e) {
			Logger.log.warn("发数据的时候，读取文件流出现异常！", e);
			this.fail();
		}
		byteAt += length;
		return false;
	}

	// 发送一次,发送方调用
	private boolean sendTick(NBTTagCompound nbt, UserFileInfo info) {
		byte[] bytes = new byte[bytesPreSend];
		int length = 0;
		try {
			length = info.fileIStream.read(bytes);
			if (length == -1)
				return true;
			if (length < bytesPreSend)
				bytes = Arrays.copyOf(bytes, length);
			nbt.setByteArray("datas", bytes);
			nbt.setLong("at", info.byteAt);
		} catch (IOException e) {
			Logger.log.warn("发数据的时候，读取文件流出现异常！", e);
			this.fail();
		}
		info.byteAt += length;
		return false;
	}

	// 接受一次，接收方调用，这包括其他客户端，和服务端
	private void recvTick(NBTTagCompound nbt) {
		long byteAt = nbt.getLong("at");
		byte[] bytes = nbt.getByteArray("datas");
		if (this.byteAt != byteAt) {
			Logger.log.warn(this + "文件数据包接受出现不连续的中断！！");
		}
		try {
			fileOStream.write(bytes);
		} catch (IOException e) {
			Logger.log.warn(this + "接受文件数据的时候，写取文件流出现异常！", e);
			if (this.isServer()) {
				this.fail();
			} else {
				// 这里是客户端接数据出现问题的地方
			}
		}
		this.byteAt = byteAt + bytes.length;
	}
}
