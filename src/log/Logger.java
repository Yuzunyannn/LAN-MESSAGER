package log;

import java.util.Calendar;

public class Logger {
	/** 日志句柄 */
	static public Logger log = new Logger();

	public Logger() {

	}

	/** 获取当前时间 */
	private String getCurTime() {
		Calendar cal = Calendar.getInstance();
		return "[" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND)
				+ "]";
	}

	/** 显示错误的轨迹 */
	private void print(Throwable e) {
		e.printStackTrace();
	}

	/** 打印 */
	private void print(String str) {
		System.out.println(this.getCurTime() + str);
	}

	/** 打印 */
	private void print(String format, Object... objs) {
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
