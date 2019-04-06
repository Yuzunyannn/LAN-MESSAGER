package log;

import java.util.Calendar;

public class Logger {
	/** 日志 */
	static public Logger log = new Logger();

	/** 获取当前时间 */
	private String getCurTime() {
		Calendar cal = Calendar.getInstance();
		return "[" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND)
				+ "]";
	}

	/** 获取当前线程名称 */
	private String getCurThreadInfo() {
		return "[" + Thread.currentThread().toString() + "]";
	}

	/** 输出一条正常信息 */
	public void impart(String str) {
		str = this.getCurTime() + str;
		System.out.println(str);
	}

	/** 输出一条正常信息 */
	public void impart(String format, Object... objs) {
		format = this.getCurTime() + format;
		System.out.printf(format, objs);
		System.out.println();
	}

	/** 输出一组toString */
	public void impart(Object... objs) {
		String str = this.getCurTime();
		for (int i = 0; i < objs.length; i++) {
			str += objs[i].toString();
		}
		System.out.println(str);
	}

	/** 输出一条警告信息 */
	public void warn(String str) {
		str = this.getCurTime() + "[warn]" + this.getCurThreadInfo() + str;
		System.out.println(str);
	}

	/** 输出一条警告信息 */
	public void warn(String str, Exception e) {
		this.warn(str + ":" + e.getMessage());
	}

	/** 输出一条错误信息 */
	public void error(String str) {
		str = this.getCurTime() + "[error]" + this.getCurThreadInfo() + str;
		System.out.println(str);
	}

	/** 输出一条错误信息 */
	public void error(String str, Exception e) {
		this.error(str + ":" + e.getMessage());
	}
}
