package app.example.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Url.UrlPath;
import app.example.netserver.HttpGetWords;
import app.example.netserver.HttpRecord;
import app.example.netserver.MyHttpPerson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 通过开源框架AsyncHttpClient的get/post请求处理
 * 
 * 屏蔽的内容是原本点击注册直接到短信验证的内容
 * @author 
 *
 */
public class LoginActvity extends Activity {

	private EditText login_accounts;// 登陆账号
	private EditText login_password;// 登陆密码
	// private CheckBox checkBox_login;//记住密码
	private Button login_login;// 登陆按钮
	private TextView login_ForgetPassword;// 忘记密码
	private Button login_QuickRegister;// 快速注册

	public SharedPreferences pre = null;// 记住密码用到的数据库
	// public String name;
	private ImageButton back;
	String name, details, place, lostitemText, latLng, title, time, time2,
			itemText, token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_login);
		SysApplication.getInstance().addActivity(this);// 目的是使整个程序退出

		init();

	}

	/************************************/
	private void init() {

		// TODO Auto-generated method stub
		back = (ImageButton) findViewById(R.id.activity_login_back);
		login_accounts = (EditText) findViewById(R.id.activity_login_accounts);
		login_password = (EditText) findViewById(R.id.activity_login_password);

		// checkBox_login = (CheckBox)
		// findViewById(R.id.activity_login_checkBox_login);
		login_login = (Button) findViewById(R.id.activity_login_login);
		login_ForgetPassword = (TextView) findViewById(R.id.activity_login_ForgetPassword);

		login_QuickRegister = (Button) findViewById(R.id.activity_login_register);
		// login_QuickRegister.setOnClickListener(this);

		// 快速注册
		login_QuickRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent intentRegister = new Intent(LoginActvity.this,
						SchoolOrCity.class);
				LoginActvity.this.startActivity(intentRegister);

			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginActvity.this.finish();
				LoginActvity.this.overridePendingTransition(
						R.anim.in_from_left, R.anim.out_to_right);
			}
		});
		/************************************/
		// 忘记密码
		login_ForgetPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent forgetIntent = new Intent(LoginActvity.this,
						Forget_Password.class);
				LoginActvity.this.startActivity(forgetIntent);

			}
		});

		/************************************/

		// 记住密码
		pre = this.getSharedPreferences("phone_password", Context.MODE_PRIVATE);
		// 第一个参数是存储时的名称，第二个参数则是文件的打开方式~
		// 两个参数，第一个参数是preferece的名称(比如：MyPref),第二个参数是打开的方式（一般选择private方式）
		if (pre != null) {
			// 记住了密码
			if (pre.getBoolean("phone_password", false)) {
				login_accounts.setText(pre.getString("phone", null));
				login_password.setText(pre.getString("password", null));
				// checkBox_login.setChecked(true) ;
			} else {

			}
		}

		/************************************/
		// 登录按钮
		login_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				boolean network = detect(LoginActvity.this);

				if (network) {

					final String phone_number = login_accounts.getText()
							.toString();
					final String password = login_password.getText().toString();
					Log.i("TAG", phone_number + "_" + password);

					// 保存电话号码传递给失物招领
					// 实例化SharedPreferences对象（第一步）
					SharedPreferences mySharedPreferences1 = getSharedPreferences(
							"phone_number", Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor1 = mySharedPreferences1
							.edit();
					// 用putString的方法保存数据
					editor1.putString("phone_number", phone_number);
					// 提交当前数据
					editor1.commit();
					SharedPreferences mySharedPreferences3 = getSharedPreferences(
							"password", Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor3 = mySharedPreferences3
							.edit();
					// 用putString的方法保存数据
					editor3.putString("password", password);
					// 提交当前数据
					editor3.commit();
					// 实例化SharedPreferences对象（第一步）
					SharedPreferences mySharedPreferences2 = getSharedPreferences(
							"phone_number" + phone_number,
							Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor2 = mySharedPreferences2
							.edit();
					// 用putString的方法保存数据
					editor2.putString("phone_number", phone_number);
					// 提交当前数据
					editor2.commit();

					/************************************/
					// 记住密码
					boolean autoLogin = true;
					// checkBox_login.isChecked();
					if (autoLogin) {
						Editor editor = pre.edit();
						editor.putString("phone", phone_number);
						editor.putString("password", password);
						editor.putBoolean("phone_password", true);
						editor.commit();
					} else {
						Editor editor = pre.edit();
						editor.putString("phone", null);
						editor.putString("password", null);
						editor.putBoolean("phone_password", false);
						editor.commit();
					}
					/************************************/

					switch (1) {
					case 1:

						if ("".equals(login_accounts.getText().toString())) {

							Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
							vibrator.vibrate(500);
							Toast.makeText(getApplicationContext(), "请输入手机号码！",
									Toast.LENGTH_SHORT).show();
							break;
						}

					case 2:

						if ("".equals(login_password.getText().toString())) {

							Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
							vibrator.vibrate(500);
							Toast.makeText(getApplicationContext(), "请输入密码！",
									Toast.LENGTH_SHORT).show();
							break;
						}

					default:

						loginAsyncHttpClientPost(phone_number, password);

						break;
					}

				} else {

					Toast.makeText(getApplicationContext(), "无网络连接，请检查网络！",
							Toast.LENGTH_LONG).show();

				}
			}
		});
	}

	/**
	 * 通过开源框架AsyncHttpClient的post请求处理
	 * 
	 * @param name
	 * @param pass
	 */
	protected void loginAsyncHttpClientPost(final String phone_number,
			final String password) {
		// TODO Auto-generated method stub
		// 发送请求到服务器
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				

				Intent intent = new Intent(LoginActvity.this,
						MainActivity.class);
				LoginActvity.this.startActivity(intent);
				Toast.makeText(getApplicationContext(), "登录成功",
						Toast.LENGTH_LONG).show();
				MyHttpPerson myHttpPerson = new MyHttpPerson(phone_number);
				myHttpPerson.submitAsyncHttpClientGet();
				HttpRecord httpRecord = new HttpRecord(phone_number, true);
				httpRecord.submitAsyncHttpClientGet();
				HttpGetWords httpGetWords = new HttpGetWords(phone_number, "0",
						"20");
				httpGetWords.submitAsyncHttpClientPost();
				finish();

			}
		};

		AsyncHttpClient client = new AsyncHttpClient();

		String url = UrlPath.loginApi;

		// 创建请求参数
		RequestParams params = new RequestParams();
		params.put("phone_number", phone_number);
		params.put("password", password);
		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			/**
			 * 成功处理的方法 statusCode:响应的状态码; headers:相应的头信息 比如 响应的时间，响应的服务器 ;
			 * responseBody:响应内容的字节
			 */

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				// 跳转必须在主线程中运行
				handler.sendEmptyMessage(0);

				// 解析数据

				/*****************************************/

				// 解析json，獲取数据token
				String messgae = new String(responseBody);
//				System.out.println("messgae:" + messgae);

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(messgae);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {

					final String token = jsonObject.getString("token");

					// 保存token，在提交失物或招领数据时用到
					// 实例化SharedPreferences对象（第一步）
					SharedPreferences mySharedPreferences = getSharedPreferences(
							"token", Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// 用putString的方法保存数据
					editor.putString("token", token);
					// 提交当前数据
					editor.commit();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/*****************************************/

			}

			/**
			 * 失败处理的方法 error：响应失败的错误信息封装到这个异常对象中
			 */
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				// 打印错误信息
				error.printStackTrace();
//				Log.i("LoginActivity-------", statusCode + "");

				if (statusCode == 401) {

					Toast.makeText(LoginActvity.this, "密码错误，请重新输入密码！",
							Toast.LENGTH_LONG).show();

				}
				if (statusCode == 404) {
					Toast.makeText(LoginActvity.this, "该电话号码尚未注册！",
							Toast.LENGTH_LONG).show();

				}

			}
		});
	}

	/************************************/
	// 双击返回键退出系统
	// private long exitTime = 0;
	//
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK
	// && event.getAction() == KeyEvent.ACTION_DOWN) {
	//
	// if ((System.currentTimeMillis() - exitTime) > 2000) {
	// Toast.makeText(getApplicationContext(), "再按一次退出程序",
	// Toast.LENGTH_SHORT).show();
	// exitTime = System.currentTimeMillis();
	// } else {
	// // finish();
	// // System.exit(0);
	// SysApplication.getInstance().exit();// 可直接关闭所有的Acitivity并退出应用程序。
	// }
	// return true;
	// }
	// return true;
	// }

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
