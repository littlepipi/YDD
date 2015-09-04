package net.loonggg.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetManager {

	// �ж������Ƿ���õķ���
	public static boolean isOpenNetwork(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

//	// �ж�WIFI�����Ƿ���õķ���
//	public static boolean isOpenWifi(Context context) {
//		ConnectivityManager connManager = (ConnectivityManager) context
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo mWifi = connManager
//				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		return mWifi.isConnected();
//	}
}
