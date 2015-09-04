package net.loonggg.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//处理时间参数的类
public class TimeUtils {
	
	public static String converTime(long timestamp){
		long currentSeconds = System.currentTimeMillis()/1000;
		long timeGap = currentSeconds-timestamp;//与现在时间相差秒数
		String timeStr = null;
		if(timeGap>24*60*60){//1天以上
			timeStr = timeGap/(24*60*60)+"天前";
		}else if(timeGap>60*60){//1小时-24小时
			timeStr = timeGap/(60*60)+"小时前";
		}else if(timeGap>60){//1分钟-59分钟
			timeStr = timeGap/60+"分钟前";
		}else{//1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}
	
	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}
}
