package net.loonggg.utils;

import java.util.Calendar;

public class System_time {
	static int year, month, day, hour, minute, second;
	static int yearend, monthend, dayend, hourend, minuteend, secondend;

	public static String system_time() {
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		second = c.get(Calendar.SECOND);
		if (second >= 10) {
			yearend = year;
			monthend = month;
			dayend = day;
			hourend = hour;
			minuteend = minute;
			secondend = second - 10;

		} else {
			secondend = second + 50;
			if (minute > 0) {
				minuteend = minute - 1;
				yearend = year;
				monthend = month;
				dayend = day;
				hourend = hour;

			} else {
				minuteend = 59;
				if (hour > 0) {
					hourend = hour - 1;
					yearend = year;
					monthend = month;
					dayend = day;
				} else {
					hourend = 23;

					if (day > 1) {
						dayend = day - 1;
						yearend = year;
						monthend = month;
					} else {
						if (month == 1 || month == 3 || month == 5
								|| month == 7 || month == 8 || month == 10
								|| month == 12) {
							dayend = 30;
							yearend = year;
							if (month == 1) {
								monthend = 12;
								yearend = year - 1;
							} else {
								yearend = year;
								monthend = month - 1;
							}

						} else if (month == 4 || month == 6 || month == 9
								|| month == 11) {
							dayend = 29;
							yearend = year;
							monthend = month - 1;

						} else {
							if (year % 100 == 0) {
								if (year % 400 == 0) {
									dayend = 28;
									monthend = 1;
									yearend = year;
								} else {
									dayend = 27;
									monthend = 1;
									yearend = year;
								}
							} else {
								if (year % 4 == 0) {
									monthend = 1;
									yearend = year;
									dayend = 28;
								} else {
									dayend = 27;
									monthend = 1;
									yearend = year;
								}
							}
						}
					}
				}

			}

		}
		return yearend + "-" + monthend + "-" + dayend + " " + hourend + ":" + minuteend
				+ ":" + secondend;
	}
}
