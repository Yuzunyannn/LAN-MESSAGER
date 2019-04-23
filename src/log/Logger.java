package log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;

public class Logger {
	/** 日志句柄 */
	static public Logger log = new Logger();
	/** 日志文件 */
	private File logFile = null;

	/** 日志输出流 */
	private PrintStream out = System.out;

	private Logger() {
		Calendar cal = Calendar.getInstance();
		String folderPath = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH);
		File folder = new File("./logs/" + folderPath);
		if (!folder.exists())
			folder.mkdirs();
		String logPath = folderPath + "-" + cal.get(Calendar.DATE);
		logFile = new File("./logs/" + folderPath + "/" + logPath + ".log");
		try {
			if (!logFile.exists())
				logFile.createNewFile();
		} catch (IOException e) {
			logFile = null;
			this.error("日志文件加载失败！", e);
			return;
		}
		try {
			out = new PrintStream(new FileOutputStream(logFile, true));
		} catch (FileNotFoundException e1) {
			out = System.out;
			this.error("日志输出流加载失败！", e1);
			return;
		}
		out.println("--------------------------" + this.getCurTime() + "--------------------------");
		File fileLog = new File("./logs/last_log.log");
		try {
			if (!fileLog.exists())
				fileLog.createNewFile();
		} catch (IOException e) {
			this.warn("日志last文件加载失败！", e);
		}
		PrintStream lastOut = null;
		try {
			lastOut = new PrintStream(new FileOutputStream(fileLog));
			lastOut.print("最后一次记录的日志文件处于  " + logFile.getPath());
		} catch (IOException e) {
			this.warn("日志last文件写入失败！", e);
		} finally {
			if (lastOut != null)
				lastOut.close();
		}

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (out != null)
			out.close();
	}

	/** 获取当前时间 */
	private String getCurTime() {
		Calendar cal = Calendar.getInstance();
		return "[" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND)
				+ "]";
	}

	/** 显示错误的轨迹 */
	private void print(Throwable e) {
		e.printStackTrace(out);
		e.printStackTrace();
	}

	/** 打印 */
	private void print(String str) {
		out.println(this.getCurTime() + str);
		System.out.println(this.getCurTime() + str);
	}

	/** 打印 */
	private void print(String format, Object... objs) {
		out.printf(this.getCurTime() + format, objs);
		out.println();
		System.out.printf(this.getCurTime() + format, objs);
		System.out.println();
	}

	/** 输出一条正常信息 */
	public void impart(String str) {
		this.print(str);
	}

	/** 输出一条正常信息 */
	public void impart(String format, Object... objs) {
		this.print(format, objs);
	}

	/** 输出一组toString */
	public void impart(Object... objs) {
		String str = "";
		for (int i = 0; i < objs.length; i++) {
			str += objs[i].toString();
		}
		this.impart(str);
	}

	/** 输出一条警告信息 */
	public void warn(String str) {
		str = "[warn]" + str;
		this.print(str);
	}

	/** 输出一条警告信息 */
	public void warn(String str, Throwable e) {
		this.warn(str + ":" + e.getMessage());
		this.print(e);
	}

	/** 输出一条错误信息 */
	public void error(String str) {
		str = "[error]" + str;
		this.print(str);
	}

	/** 输出一条错误信息 */
	public void error(String str, Throwable e) {
		this.error(str + ":" + e.getMessage());
		this.print(e);
	}
}
