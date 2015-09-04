package net.loonggg.utils;

import java.util.Calendar;

public class System_time_New {
	static int year,month,day,hour,minute,second;
	public static String system_time(boolean time) {
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH)+1;
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		second=c.get(Calendar.SECOND);
	if (time==true) {
		return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
	}else {
		return year+"-"+month+"-"+day+"T"+hour+":"+minute+":"+second;
	}
		
	}
}
