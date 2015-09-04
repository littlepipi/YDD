package net.loonggg.utils;

import java.util.Calendar;

public class ReceiverNewTime {

	public static String NewTime() {
		Calendar calendar = Calendar.getInstance();
		String time = calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " "
				+ calendar.get(Calendar.HOUR) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"
				+ calendar.get(Calendar.SECOND);

		return time;
	}
}
