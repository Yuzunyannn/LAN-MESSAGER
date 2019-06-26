package server.event;

import java.util.Calendar;

import event.Event;

public class EventHour extends Event {
	final public Calendar cal;

	public EventHour(Calendar cal) {
		this.cal = cal;
	}

	/** 是否是新的一天 */
	public boolean newDay() {
		return cal.get(Calendar.HOUR_OF_DAY) == 0;
	}

	/** 和当前时间相差时间 */
	public int daysBetween(Calendar data) {
		java.util.Calendar calst = cal;
		java.util.Calendar caled = data;
		calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calst.set(java.util.Calendar.MINUTE, 0);
		calst.set(java.util.Calendar.SECOND, 0);
		caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
		caled.set(java.util.Calendar.MINUTE, 0);
		caled.set(java.util.Calendar.SECOND, 0);
		int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
		return days;
	}
}
