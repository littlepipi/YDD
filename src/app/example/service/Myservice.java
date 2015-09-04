package app.example.service;

import java.util.Timer;
import java.util.TimerTask;

import net.loonggg.utils.System_time;
import net.loonggg.utils.System_time_about_system;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import app.example.netserver.HttpReplies;
import app.example.netserver.HttpSystemMessage;

public class Myservice extends Service {

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {

		super.onCreate();
		Timer timer = new Timer();
		timer.schedule(new Comments(), 0, 10000);
		timer.schedule(new System(), 0, 1000 * 60 * 10);

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	class Comments extends TimerTask {

		@Override
		public void run() {

			Message message = new Message();
			message.what = 1;
			comments.sendMessage(message);
		}
	}

	Handler comments = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			SharedPreferences mySharedPreferences = getSharedPreferences(
					"phone_number", Activity.MODE_PRIVATE);
			// 使用getString方法获得value，注意第2个参数是value的默认值
			String phone_number = mySharedPreferences
					.getString("phone_number", "").toString().trim();
			SharedPreferences mySharedPreferencestoken = getSharedPreferences(
					"token", Activity.MODE_PRIVATE);
			// 使用getString方法获得value，注意第2个参数是value的默认值
			String token = mySharedPreferencestoken.getString("token", "")
					.toString().trim();
			SharedPreferences mySharedPreferencestime = getSharedPreferences(
					"STAYTIME", Activity.MODE_PRIVATE);
			// 使用getString方法获得value，注意第2个参数是value的默认值
			String time = mySharedPreferencestime.getString("STAYTIME", "")
					.toString().trim();
			super.handleMessage(msg);
			if (msg.what == 1) {
				if ("".equals(token)) {

				} else {
					if ("".equals(time)) {
						HttpReplies.submitAsyncHttpClientPost(phone_number,
								token, System_time.system_time(), true);
					} else {
						HttpReplies.submitAsyncHttpClientPost(phone_number,
								token, time, false);
						SharedPreferences mySharedPreferenceSystemTime = getSharedPreferences(
								"STAYTIME", Activity.MODE_PRIVATE);

						mySharedPreferenceSystemTime.edit().clear().commit();
					}

				}

			}
		}

	};

	class System extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message = new Message();
			message.what = 1;
			system.sendMessage(message);
		}
	}

	Handler system = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			SharedPreferences mySharedPreferencesSystemTime = getSharedPreferences(
					"SYSTEMTIME", Activity.MODE_PRIVATE);
			// 使用getString方法获得value，注意第2个参数是value的默认值
			String time = mySharedPreferencesSystemTime
					.getString("SYSTEMTIME", "").toString().trim();
			super.handleMessage(msg);
			if (msg.what == 1) {

				if ("".equals(time)) {
					HttpSystemMessage.submitAsyncHttpClientPost(
							System_time_about_system.system_time(), true);
				} else {
					HttpSystemMessage.submitAsyncHttpClientPost(time, false);
					SharedPreferences mySharedPreferenceSystemSystemTime = getSharedPreferences(
							"SYSTEMTIME", Activity.MODE_PRIVATE);

					mySharedPreferenceSystemSystemTime.edit().clear().commit();

				}
			}
		}
	};

}
