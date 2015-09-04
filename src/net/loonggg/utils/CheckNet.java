package net.loonggg.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 检查手机网络
 * @author Administrator
 *
 */
public class CheckNet {
	
	/*
	 * 
	 * 判断系统网络
	 */
	public static boolean detect(Activity act) {

		ConnectivityManager manager = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}

		NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}

		return true;
	}
	
	

}
