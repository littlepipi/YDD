package app.example.netserver;

import net.loonggg.utils.NetManager;

import org.apache.http.Header;

import tx.ydd.app.R;
import android.R.interpolator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import app.example.Url.UrlPath;
import app.example.activity.MessageComments;
import app.example.analytic_data.Analytic_Single_String_Data;
import app.example.application.MyApplication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpReplies {

	private static String name;
	private static String content;
	private static int inte;
	private static String intk;
	private static int close_;

	public static void submitAsyncHttpClientPost(String phone_number,
			String token, final String time, final boolean flag) {
		SharedPreferences mySharedPreferencesphone = MyApplication
				.getAppContext().getSharedPreferences("comment_inte",
						Activity.MODE_PRIVATE);

		intk = mySharedPreferencesphone.getString("comment_ints", "0")
				.toString().trim();

		inte = Integer.valueOf(intk);

		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.replies;

		// 创建请求参数
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("phone_number", phone_number);
		params.put("time", time);

		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				final String res = new String(responseBody);

				if ("[]".equals(res)) {

				} else {

					close_ = Analytic_Single_String_Data.JSONArray(res);
					if (close_ != 1) {
						inte = inte + close_;
					} else {
						inte = inte + 1;
					}

					SharedPreferences systemPreferences = MyApplication
							.getAppContext().getSharedPreferences(
									"comment_inte", Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor = systemPreferences.edit();
					// 用putString的方法保存数据
					String ints = String.valueOf(inte);
					editor.putString("comment_ints", ints);
					// 提交当前数据
					editor.commit();
					if (flag == true) {
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							@Override
							public void run() {
								SharedPreferences mySharedPreferencestoken = MyApplication
										.getAppContext().getSharedPreferences(
												"OtherHttpPerson",
												Activity.MODE_PRIVATE);
								// 使用getString方法获得value，注意第2个参数是value的默认值

								name = mySharedPreferencestoken.getString(
										"personname", "易丢丢");
								content = mySharedPreferencestoken.getString(
										"content", "易丢丢");
								Vibrator vibrator = (Vibrator) MyApplication
										.getAppContext().getSystemService(
												Context.VIBRATOR_SERVICE);
								long[] pattern = { 100, 400, 100, 400 };
								vibrator.vibrate(pattern, -1);
								
								String service = Context.NOTIFICATION_SERVICE;
								NotificationManager nm = (NotificationManager) MyApplication
										.getAppContext().getSystemService(
												service); // 获得系统级服务，用于管理消息
								Notification n = new Notification(); // 定义一个消息类
								n.icon = R.drawable.logo_m; // 设置图标
								n.tickerText = "尊敬的用户，您有一条新的消息！！！"; // 设置消息
								n.when = System.currentTimeMillis();
								n.flags = Notification.FLAG_AUTO_CANCEL;// 设置时间
								// Notification n1 =new
								// Notification(icon,tickerText,when);
								// //也可以用这个构造创建
								Intent intent = new Intent(MyApplication
										.getAppContext(), MessageComments.class);
								PendingIntent pi = PendingIntent.getActivity(
										MyApplication.getAppContext(), 0,
										intent, 0); // 消息触发后调用
								n.setLatestEventInfo(
										MyApplication.getAppContext(), name
												+ "给你的信息:", content, pi); // 设置事件信息就是拉下标题后显示的内容
								nm.notify(0, n); // 发送通知

								// Log.i("推送回复消息！！！", "" + res + "^^^" + name
								// + "~~~~~~" + time);
							}
						}, 1000);
					}

				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// 打印错误信息
				error.printStackTrace();
				if (NetManager.isOpenNetwork(MyApplication.getAppContext())) {
					// Toast.makeText(MyApplication.getAppContext(), "账号异地登陆",
					// Toast.LENGTH_SHORT).show();
				} else {
					// Toast.makeText(MyApplication.getAppContext(), "无网络链接",
					// Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

}
