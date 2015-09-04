package app.example.activity;

import static cn.smssdk.framework.utils.R.getStringRes;

import java.util.ArrayList;
import java.util.HashMap;

import net.loonggg.utils.CheckNet;
import net.loonggg.utils.MyAlertDialog;

import org.apache.http.Header;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;
import cn.smssdk.gui.SMSReceiver;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ChangePasswordActivity extends Activity {

	private ImageButton BackButton;

	private Button GetAuthCode;
	private EditText textPhone;
	private EditText AuthCode;
	private EditText newPassword;
	private Button Next;
	private TextView TimeDown;
	String phone_number, token, identify, password, operation;
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY;
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET;
	String a = "5aec12823819";
	String b = "b42dafb1c491a7655cdf907b2c608f05 ";

	// 国家号码规则
	private HashMap<String, String> countryRules;
	private OnSendMessageHandler osmHandler;
	private EventHandler callback;
	private EventHandler handler;
	int time = 60;;
	private BroadcastReceiver smsReceiver;

	public void setOnSendMessageHandler(OnSendMessageHandler h) {
		osmHandler = h;
	}

	public void setRegisterCallback(EventHandler callback) {
		this.callback = callback;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.change_password);
		// 控件初始化
		initView();

		APPKEY = a.toString().trim();
		APPSECRET = b.toString().trim();
		// isEmpty判断变量是否初始化
		if (TextUtils.isEmpty(APPKEY) || TextUtils.isEmpty(APPSECRET)) {
		} else {
			initSDK();
		}
		System.out.println("第2站");
		// 空件的点击事件
		OnClick();
	}

	private void initView() {

		BackButton = (ImageButton) findViewById(R.id.change_password_back);
		GetAuthCode = (Button) findViewById(R.id.forget_passwordGetAuthCode);
		textPhone = (EditText) findViewById(R.id.change_password_accounts);
		AuthCode = (EditText) findViewById(R.id.change_passwordAuthCode);
		newPassword = (EditText) findViewById(R.id.change_password_new_password);
		Next = (Button) findViewById(R.id.change_passwordNext);
		TimeDown = (TextView) findViewById(R.id.timedown1);

		// 从登陆获取电话号码
		// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
		SharedPreferences mySharedPreferencesphone = getSharedPreferences(
				"phone_number", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		phone_number = mySharedPreferencesphone.getString("phone_number", "")
				.toString().trim();

		// 从登录获取令牌token
		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		token = mySharedPreferencestoken.getString("token", "").toString()
				.trim();

		operation = "change_password".toString().trim();

//		System.out.println(phone_number + "======" + password + "======="
//				+ token + "======" + identify);
	}

	private void initSDK() {
		// 初始化短信SDK
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		handler = new EventHandler() {
			@SuppressWarnings("unchecked")
			public void afterEvent(final int event, final int result,
					final Object data) {

				runOnUiThread(new Runnable() {
					public void run() {
						if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
							// 提交验证码
							afterSubmit(result, data);

						} else if (result == SMSSDK.RESULT_COMPLETE) {

							if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
								// 请求支持国家列表
								onCountryListGot((ArrayList<HashMap<String, Object>>) data);

							}

							else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

								// 请求验证码后，跳转到验证码填写页面
								System.out.println("获取验证码成功");

							}
						} else {
							if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE
									&& data != null
									&& (data instanceof UserInterruptException)) {
								// 由于此处是开发者自己决定要中断发送的，因此什么都不用做
								return;
							}

							// 根据服务器返回的网络错误，给toast提示
							try {
								((Throwable) data).printStackTrace();
								Throwable throwable = (Throwable) data;

								JSONObject object = new JSONObject(
										throwable.getMessage());
								String des = object.optString("detail");

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				});
			}

		};
		SMSSDK.registerEventHandler(handler);

	}

	private void OnClick() {
		// TODO Auto-generated method stub
		// 获取验证码事件
		GetAuthCode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// 请求发送短信验证码
				if (countryRules == null || countryRules.size() <= 0) {

					boolean net = CheckNet.detect(ChangePasswordActivity.this);

					if (net) {

						switch (1) {

						case 1:
							if ("".equals(textPhone.getText().toString().trim())) {
								Toast.makeText(ChangePasswordActivity.this,
										"请输入手机号码！", Toast.LENGTH_SHORT).show();
								break;
							}
							if (textPhone.getText().toString().trim().length() != 11) {
								Toast.makeText(ChangePasswordActivity.this,
										"手机号码错误！", Toast.LENGTH_SHORT).show();
								break;
							}

						default:
							AsyncHttpClientGet(textPhone.getText().toString());
							break;
						}

					} else {

						Toast.makeText(ChangePasswordActivity.this,
								"网络不可用，请检查网络！", Toast.LENGTH_SHORT).show();
					}

				} else {

					Toast.makeText(ChangePasswordActivity.this, "获取验证码失败！",
							Toast.LENGTH_LONG).show();

				}

			}
		});

		// 收不到验证码点击事件
		TimeDown.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new MyAlertDialog(ChangePasswordActivity.this).builder()
						.setTitle("提示！").setMsg("重新获取验证码？")
						.setPositiveButton("确认", new OnClickListener() {
							@Override
							public void onClick(View v) {

								// 请求发送短信验证码
								SMSSDK.getSupportedCountries();

								// 获取验证码成功后的执行动作
								Toast.makeText(ChangePasswordActivity.this,
										"验证码已发送！", Toast.LENGTH_LONG).show();
								// 倒计时
								time = 60;// 因为一次执行完之后，time为0，必须重新设置
								Message message = handler4.obtainMessage(1);
								handler4.sendMessageDelayed(message, 1000);

								// 自动填写验证码
								handler2.sendEmptyMessage(0);

							}
						}).setNegativeButton("取消", new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();

			}
		});

		// 完成修改，上传
		Next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean net = CheckNet.detect(ChangePasswordActivity.this);
				if (net) {

					identify = AuthCode.getText().toString().trim();
					password = newPassword.getText().toString().trim();
					switch (1) {

					case 1:
						if ("".equals(textPhone.getText().toString().trim())) {
							Toast.makeText(ChangePasswordActivity.this,
									"请输入手机号码！", Toast.LENGTH_SHORT).show();
							break;
						}
						if (textPhone.getText().toString().trim().length() != 11) {
							Toast.makeText(ChangePasswordActivity.this,
									"手机号码错误！", Toast.LENGTH_SHORT).show();
							break;
						}

					default:

						SMSSDK.submitVerificationCode("86", phone_number,
								identify);
						break;
					}

				} else {

					Toast.makeText(ChangePasswordActivity.this, "网络不可用，请检查网络！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// 返回事件
		BackButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

				ChangePasswordActivity.this.overridePendingTransition(
						R.anim.in_from_left, R.anim.out_to_right);
			}
		});

	}

	private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
		// 解析国家列表

		System.out.println("第3站");
		for (HashMap<String, Object> country : countries) {
			String code = (String) country.get("zone");
			String rule = (String) country.get("rule");
			if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
				continue;
			}

			if (countryRules == null) {
				countryRules = new HashMap<String, String>();
			}
			countryRules.put(code, rule);
		}
		// 检查手机号码

		String phone = textPhone.getText().toString().trim()
				.replaceAll("\\s*", "");

		String code = "+86".toString().trim();

		
		checkPhoneNum(phone, code);

	}

	/** 检查电话号码 */
	private void checkPhoneNum(String phone, String code) {

//		Log.i("短信验证--------------->", code + "------" + phone);

		if (code.startsWith("+")) {
			code = code.substring(1);
		}
//		System.out.println("第5站" + code + phone);

		SMSSDK.getVerificationCode(code, phone.trim(), osmHandler);

//		System.out.println("第6站------->" + code + phone);

	}

	// 自动填写验证码
	final Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			smsReceiver = new SMSReceiver(new SMSSDK.VerifyCodeReadListener() {
				@Override
				public void onReadVerifyCode(final String verifyCode) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AuthCode.setText(verifyCode);

						}
					});
				}
			});
			MyApplication.getAppContext()
					.registerReceiver(
							smsReceiver,
							new IntentFilter(
									"android.provider.Telephony.SMS_RECEIVED"));

		}
	};

	// 倒计时
	final Handler handler4 = new Handler() {
		public void handleMessage(Message msg) { // handle message
			switch (msg.what) {
			case 1:
				time--;
				TimeDown.setVisibility(View.VISIBLE);
				TimeDown.setFocusable(false);
				TimeDown.setClickable(false);
				TimeDown.setText("获取验证码还有" + time + "秒");

				if (time > 0) {
					Message message = handler4.obtainMessage(1);
					handler4.sendMessageDelayed(message, 1000); // send message
				} else {
					TimeDown.setFocusable(true);
					TimeDown.setClickable(true);
					TimeDown.setText("收不到验证码？");
				}
			}

			super.handleMessage(msg);
		}
	};

	protected void onDestroy() {

		// 销毁回调监听接口
		SMSSDK.unregisterAllEventHandler();

		super.onDestroy();
	}

	public void onRestart() {
		SMSSDK.registerEventHandler(handler);
		super.onRestart();
	}

	// 检查电话号码是否已经注册
	private void AsyncHttpClientGet(String phone) {
		// TODO Auto-generated method stub
		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();

		String urlr = UrlPath.exist + phone;

		String url = urlr;

		// 执行post方法
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				// 请求验证码
				SMSSDK.getSupportedCountries();
				Toast.makeText(ChangePasswordActivity.this, "验证码已发送！",
						Toast.LENGTH_LONG).show();

				// 倒计时
				Message message = handler4.obtainMessage(1);
				handler4.sendMessageDelayed(message, 1000);
				// 自动填写验证码
				handler2.sendEmptyMessage(0);

				GetAuthCode.setEnabled(false);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				Toast.makeText(ChangePasswordActivity.this, "该手机号码尚未注册！",
						Toast.LENGTH_LONG).show();

			}
		});

	}

	/**
	 * 提交验证码成功后的执行事件
	 * 
	 * @param result
	 * @param data
	 */
	private void afterSubmit(final int result, final Object data) {
		runOnUiThread(new Runnable() {
			public void run() {

				if (result == SMSSDK.RESULT_COMPLETE) {
					HashMap<String, Object> res = new HashMap<String, Object>();
					res.put("res", true);
					res.put("page", 2);
					res.put("phone", data);

					switch (1) {

					case 1:

						if ("".equals(textPhone.getText().toString())) {
							Toast.makeText(ChangePasswordActivity.this,
									"请输入手机号码！", Toast.LENGTH_SHORT).show();
							break;
						}

					case 2:

						if ("".equals(identify.toString())) {
							Toast.makeText(ChangePasswordActivity.this,
									"请输入验证码！", Toast.LENGTH_SHORT).show();
							break;
						}
						if (AuthCode.getText().toString().trim().length() != 4) {
							Toast.makeText(ChangePasswordActivity.this,
									"验证码格式不对！必须是四位数字！", Toast.LENGTH_SHORT)
									.show();
							break;
						}

					case 3:

						if ("".equals(password.toString())) {

							Toast.makeText(ChangePasswordActivity.this,
									"请设置新密码！", Toast.LENGTH_SHORT).show();
							break;
						}

					default:

						boolean net = CheckNet
								.detect(ChangePasswordActivity.this);

						if (net) {

							AsyncHttpClientPost(phone_number, password, token,
									identify, operation);

						} else {

							Toast.makeText(ChangePasswordActivity.this,
									"网络不可用，请检查网络！", Toast.LENGTH_SHORT).show();
						}
						break;

					}
				} else {
					((Throwable) data).printStackTrace();
					// 验证码不正确
					int resId = getStringRes(ChangePasswordActivity.this,
							"smssdk_virificaition_code_wrong");

					if (resId > 0) {
						Toast.makeText(ChangePasswordActivity.this, resId,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	// 上传数据
	protected void AsyncHttpClientPost(String phone_number, String password,
			String token, String identify, String operation) {
		// TODO Auto-generated method stub
//		System.out.println("----------进入修改密码上传");// 打印
		// 返回到设置页面
		final Handler handler9 = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				Intent intent = new Intent(ChangePasswordActivity.this,
						AccountSettingActivity.class);
				ChangePasswordActivity.this.startActivity(intent);

				ChangePasswordActivity.this.finish();

				Toast.makeText(getApplicationContext(), "修改成功",
						Toast.LENGTH_LONG).show();

			}
		};
		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.patchApi;
		// 创建请求参数
		RequestParams params = new RequestParams();
		params.put("phone_number", phone_number);
		params.put("password", password);
		params.put("token", token);
		params.put("identify", identify);
		params.put("operation", "change_password");
//		System.out.println("上传数据--------->" + phone_number + "----" + password
//				+ "----" + token + "----" + identify + "----" + operation);
		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

//				System.out.println("----------成功");// 打印
//				System.out.println("statusCode---------->" + statusCode);
				// 返回到设置页面
				handler9.sendEmptyMessage(0);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
//				System.out.println("修改失败");
//
//				System.out.println("statusCode---------->" + statusCode);

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();

			ChangePasswordActivity.this.overridePendingTransition(
					R.anim.in_from_left, R.anim.out_to_right);
		}
		return super.onKeyDown(keyCode, event);
	}
}
