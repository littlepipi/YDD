package app.example.activity;

import java.util.List;
import java.util.Map;

import net.loonggg.utils.MyAlertDialog;
import net.loonggg.utils.SharedConfig;
import tx.ydd.app.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import app.example.netserver.HttpGetWords;
import app.example.netserver.HttpNewsMessage;
import app.example.netserver.HttpRecord;
import app.example.netserver.HttpUtil;
import app.example.netserver.MyHttpPerson;
import app.example.netserver.MySQLiteMethodDetails;
import app.example.service.Myservice;

public class Splash extends Activity {
	private ImageView welcomeImg = null;
	private ImageView welcome_two = null;
	private ImageView welcome_three = null;
	private SharedPreferences shared;
	private String id, title, time, latitude, langitude, kind, information,
			commitperson, contactphone, sharenumber, commentsnumber,
			followingnumber, location, created, comments, image1, image2,
			image3, personcreated, personname, personportrait, personlocation,
			from_person, content, if_read, token, phone_number, password;
	boolean first = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Intent service = new Intent(Splash.this, Myservice.class);
		Splash.this.startService(service);
		first = true;
		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		token = mySharedPreferencestoken.getString("token", "");
		

		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		/**
		 * 预先存十条数据在数据库端
		 */
		List<Map<String, String>> list = MySQLiteMethodDetails
				.list_discover_Db();
		List<Map<String, String>> words = MySQLiteMethodDetails.listWords_Db();
		if (list.size() == 0) {
			for (int i = 0; i < 10; i++) {
				id = String.valueOf(i);
				MySQLiteMethodDetails.insert_discover_Db(id, title, time,
						information, commitperson, image1, image2, image3,
						personcreated, personname, personportrait,
						personlocation, sharenumber, commentsnumber,
						followingnumber);
				MySQLiteMethodDetails.insertDb(id, "lost", title, time,
						latitude, langitude, kind, information, commitperson,
						contactphone, sharenumber, commentsnumber,
						followingnumber, location, created, comments, image1,
						image2, image3, personcreated, personname,
						personportrait, personlocation);
				MySQLiteMethodDetails.insertDb(id, "find", title, time,
						latitude, langitude, kind, information, commitperson,
						contactphone, sharenumber, commentsnumber,
						followingnumber, location, created, comments, image1,
						image2, image3, personcreated, personname,
						personportrait, personlocation);
				;

			}
			if (words.size() == 0) {
				for (int i = 0; i < 20; i++) {
					id = String.valueOf(i);
					MySQLiteMethodDetails.insertWords_Db(id, created,
							from_person, content, if_read, personcreated,
							personname, personportrait, personlocation);
				}

			}

		}

	}

	
	

	@Override
	protected void onStop() {

		first = false;
		super.onStop();
	}

	@Override
	protected void onResume() {
		shared = new SharedConfig(this).GetConfig(); // 得到配置文件
		if (first) {

			welcomeImg = (ImageView) this.findViewById(R.id.splash_welcome);
			welcome_two = (ImageView) this.findViewById(R.id.welcome_two);
			welcome_three = (ImageView) this.findViewById(R.id.welcome_three);

			Animation animation_two = AnimationUtils.loadAnimation(this,
					R.anim.welcome_two);

			Animation animation_three = AnimationUtils.loadAnimation(this,
					R.anim.welcome_three);
			AlphaAnimation anima = new AlphaAnimation(0.9f, 1.0f);
			anima.setDuration(4000);// 设置动画显示时间
			welcomeImg.startAnimation(anima);

			welcome_two.startAnimation(animation_two);

			welcome_three.startAnimation(animation_three);
			anima.setAnimationListener(new AnimationImpl());
		} else {

			animationend();
		}
		super.onResume();
	}

	private class AnimationImpl implements AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {
			welcomeImg.setBackgroundResource(R.drawable.welcome_one);
			welcome_two.setBackgroundResource(R.drawable.welcome_two);
			welcome_three.setBackgroundResource(R.drawable.welcome_three);
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			animationend();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}
	}

	private void animationend() {
		// TODO Auto-generated method stub
		boolean network = detect(Splash.this);

		if (network) {

			if (shared.getBoolean("First", true)) {
				/**
				 * 获取网络数据集
				 */
				HttpUtil httplost = new HttpUtil(0, 5, "全国", "lost", "所有");
				httplost.submitAsyncHttpClientGet();
				HttpUtil httpfind = new HttpUtil(0, 5, "全国", "find", "所有");
				httpfind.submitAsyncHttpClientGet();
				HttpNewsMessage httpNewsMessage = new HttpNewsMessage(0, 5);
				httpNewsMessage.submitAsyncHttpClientGet();
				Editor editor = shared.edit();
				editor.putBoolean("First", false);
				editor.commit();
				Intent intent = new Intent(Splash.this, WelcomeActivity.class);
				startActivity(intent);
				finish();
			} else if (!shared.getBoolean("First", false)) {
				
					MyHttpPerson myHttpPerson = new MyHttpPerson(phone_number);
					myHttpPerson.submitAsyncHttpClientGet();
					HttpRecord httpRecord = new HttpRecord(phone_number, true);
					httpRecord.submitAsyncHttpClientGet();
					HttpGetWords httpGetWords = new HttpGetWords(phone_number, "0",
							"20");
					httpGetWords.submitAsyncHttpClientPost();
					Intent intent = new Intent(Splash.this, MainActivity.class);
					startActivity(intent);
					finish();
			}
		} else {

			new MyAlertDialog(Splash.this).builder().setTitle("提示！")
					.setMsg("无网络连接哦！马上去设置吧？")
					.setPositiveButton("确认", new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = null;
							try {
								String sdkVersion = android.os.Build.VERSION.SDK;
								if (Integer.valueOf(sdkVersion) > 10) {
									intent = new Intent(
											android.provider.Settings.ACTION_SETTINGS);// 进入系统的设置页面

								} else {
									intent = new Intent();

									ComponentName comp = new ComponentName(
											"com.android.settings",
											"com.android.settings.WirelessSettings");
									intent.setComponent(comp);
									intent.setAction("android.intent.action.VIEW");
								}
								Splash.this.startActivity(intent);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

							Splash.this.finish();
						}
					}).show();
		}

	}

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