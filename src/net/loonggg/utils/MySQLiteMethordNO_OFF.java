package net.loonggg.utils;

import java.util.Map;

import app.example.netserver.MySQLiteMethodDetails;

public class MySQLiteMethordNO_OFF {
	static Map<String, String> map;

	public static void mySqliteMethordNO_OFF(String id) {

		map = MySQLiteMethodDetails.map_fans_DB(id);

	
		String NO_OFF = map.get("NO_OFF");
		if (NO_OFF.equals(Constant.NO)) {
			MySQLiteMethodDetails.updata_fans_Db(id, Constant.OFF);
		} else if (NO_OFF.equals(Constant.OFF)) {
			MySQLiteMethodDetails.updata_fans_Db(id, Constant.NO);
		}

	}
}
