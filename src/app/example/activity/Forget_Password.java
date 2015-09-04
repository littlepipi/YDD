package app.example.activity;

import static cn.smssdk.framework.utils.R.getStringRes;

import java.util.ArrayList;
import java.util.HashMap;

import net.loonggg.utils.CheckNet;
import net.loonggg.utils.MyAlertDialog;

import org.apache.http.Header;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Forget_Password extends Activity {

	private EditText TeleNum;// 手机号

	private EditText AuthCode;// 验证码

	private Button GetAuthCode;// 获取验证码按钮

	private EditText NewPassword;// 新密码
	private String EditNewPassword;// 新密码
	private TextView TimeDown;// 倒计时
	private EditText AffirmNewPassword;// 确认新密码
	private String EditAffirmNewPassword;// 确认新密码
	private Button Next;// 完成按钮
	private ImageView confirm;
	private ImageView back;

	String phone_number, identify, password, operation;

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
	private static final int RETRY_INTERVAL = 60;
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
		setContentView(R.layout.forget_the_password);
		SysApplication.getInstance().addActivity(this);
		// 控件初始化
		init();

		APPKEY = a.toString().trim();
		APPSECRET = b.toString().trim();
		// isEmpty判断变量是否初始化
		if (TextUtils.isEmpty(APPKEY) || TextUtils.isEmpty(APPSECRET)) {
		} else {
			initSDK();
		}
	

		// 空件的点击事件
		onClick();

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

	private void init() {
		// TODO Auto-generated method stub

		back = (ImageView) findViewById(R.id.forget_password_back);
		TeleNum = (EditText) findViewById(R.id.forget_passwordTeleNum);
		// TeleNum.addTextChangedListener(Number);
		AuthCode = (EditText) findViewById(R.id.forget_passwordAuthCode);
		GetAuthCode = (Button) findViewById(R.id.forget_passwordGetAuthCode);
		NewPassword = (EditText) findViewById(R.id.forget_passwordNewPassword);
		AffirmNewPassword = (EditText) findViewById(R.id.forget_passwordAffirmNewPassword);
		AffirmNewPassword.addTextChangedListener(Password);
		confirm = (ImageView) findViewById(R.id.forget_password_confirm);
		Next = (Button) findViewById(R.id.forget_passwordNext);
		TimeDown = (TextView) findViewById(R.id.timedown);
		TimeDown.setFocusable(false);
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

						if ("".equals(TeleNum.getText().toString().trim())) {
							Toast.makeText(Forget_Password.this, "请输入手机号码！",
									Toast.LENGTH_SHORT).show();
							break;
						}
						if (TeleNum.getText().toString().trim().length() != 11) {
							Toast.makeText(Forget_Password.this, "手机号码错误！",
									Toast.LENGTH_SHORT).show();
							break;
						}

					case 2:

						if ("".equals(AuthCode.getText().toString().trim())) {
							Toast.makeText(Forget_Password.this, "请输入验证码！",
									Toast.LENGTH_SHORT).show();
							break;
						}
						if (AuthCode.getText().toString().trim().length() != 4) {
							Toast.makeText(Forget_Password.this,
									"验证码格式不对！必须是四位数字！", Toast.LENGTH_SHORT)
									.show();
							break;
						}

					case 3:

						if ("".equals(NewPassword.getText().toString().trim())) {
							Toast.makeText(Forget_Password.this, "请设置密码！",
									Toast.LENGTH_SHORT).show();
							break;
						}

						if (NewPassword.getText().toString().trim().length() < 6
								|| NewPassword.getText().toString().trim()
										.length() > 18) {
							Toast.makeText(Forget_Password.this,
									"密码必须大于6位小于18位", Toast.LENGTH_SHORT).show();
							break;
						}

					case 4:

						if (!(password.equals(AffirmNewPassword.getText()
								.toString()))) {
							Toast.makeText(Forget_Password.this, "两次密码输入不一致！",
									Toast.LENGTH_SHORT).show();
							break;
						}

					default:

						boolean net = CheckNet.detect(Forget_Password.this);

						if (net) {

							AsyncHttpClientPost(phone_number, password,
									identify, operation);

						} else {

							Toast.makeText(Forget_Password.this,
									"网络不可用，请检查网络！", Toast.LENGTH_SHORT).show();
						}
						break;

					}
				} else {
					((Throwable) data).printStackTrace();
					// 验证码不正确
					int resId = getStringRes(Forget_Password.this,
							"smssdk_virificaition_code_wrong");

					if (resId > 0) {
						Toast.makeText(Forget_Password.this, resId,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private void onClick() {
		// 获取验证码
		GetAuthCode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// 请求发送短信验证码
				if (countryRules == null || countryRules.size() <= 0) {

					boolean net = CheckNet.detect(Forget_Password.this);

					if (net) {

						switch (1) {

						case 1:
							if ("".equals(TeleNum.getText().toString().trim())) {
								Toast.makeText(Forget_Password.this,
										"请输入手机号码！", Toast.LENGTH_SHORT).show();
								break;
							}
							if (TeleNum.getText().toString().trim().length() != 11) {
								Toast.makeText(Forget_Password.this, "手机号码错误！",
										Toast.LENGTH_SHORT).show();
								break;
							}

						default:
							AsyncHttpClientGet(TeleNum.getText().toString()
									.trim());
							break;
						}

					} else {

						Toast.makeText(Forget_Password.this, "网络不可用，请检查网络！",
								Toast.LENGTH_SHORT).show();
					}

				} else {

					Toast.makeText(Forget_Password.this, "获取验证码失败！",
							Toast.LENGTH_LONG).show();

				}

			}
		});

		// 完成修改
		Next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				phone_number = TeleNum.getText().toString().trim();
				identify = AuthCode.getText().toString().trim();
				password = NewPassword.getText().toString().trim();
				operation = "change_password".toString().trim();

				boolean net = CheckNet.detect(Forget_Password.this);
				if (net) {

					switch (1) {

					case 1:
						if ("".equals(TeleNum.getText().toString().trim())) {
							Toast.makeText(Forget_Password.this, "请输入手机号码！",
									Toast.LENGTH_SHORT).show();
							break;
						}
						if (TeleNum.getText().toString().trim().length() != 11) {
							Toast.makeText(Forget_Password.this, "手机号码错误！",
									Toast.LENGTH_SHORT).show();
							break;
						}

					default:

						SMSSDK.submitVerificationCode("86", phone_number,
								identify);
						break;
					}

				} else {

					Toast.makeText(Forget_Password.this, "网络不可用，请检查网络！",
							Toast.LENGTH_SHORT).show();
				}

			}

		});

		// 收不到验证码点击事件

		TimeDown.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new MyAlertDialog(Forget_Password.this).builder()
						.setTitle("提示！").setMsg("重新获取验证码？")
						.setPositiveButton("确认", new OnClickListener() {
							@Override
							public void onClick(View v) {

								// 请求发送短信验证码
								SMSSDK.getSupportedCountries();

								// 获取验证码成功后的执行动作
								Toast.makeText(Forget_Password.this, "验证码已发送！",
										Toast.LENGTH_LONG).show();
								// 倒计时

								time = 60;// 因为一次执行完之后，time为0，必须重新设置
								Message message = handler4.obtainMessage(1);
								handler4.sendMessageDelayed(message, 1000);
								// 自动填写验证码
								handler2.sendEmptyMessage(0);

								finish();
							}
						}).setNegativeButton("取消", new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();

			}
		});
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Forget_Password.this.finish();
				Forget_Password.this.overridePendingTransition(
						R.anim.in_from_left, R.anim.out_to_right);
			}
		});

	}

	protected void AsyncHttpClientPost(final String phone_number,
			final String password, String identify, String operation) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
//		System.out.println("----------进入忘记密码上传");// 打印
		// 返回到登陆页面
		final Handler handler9 = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				Intent intent = new Intent(Forget_Password.this,
						LoginActvity.class);

				Forget_Password.this.startActivity(intent);

				Forget_Password.this.finish();

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
		params.put("identify", identify);
		params.put("operation", "change_password");
//		System.out.println("上传数据--------->" + phone_number + "----" + password
//				+ "----" + identify + "----" + operation);
		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

//				System.out.println("----------成功");// 打印
//				// String i = new String(responseBody);
//				System.out.println("statusCode---------->" + statusCode);
				// 返回到设置页面
				handler9.sendEmptyMessage(0);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (statusCode == 404) {

					Toast.makeText(Forget_Password.this, "该电话号码尚未注册！",
							Toast.LENGTH_SHORT).show();

				}

			}
		});

	}

	private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
		// 解析国家列表

//		System.out.println("第3站");
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

		String phone = TeleNum.getText().toString().trim()
				.replaceAll("\\s*", "");
		// String phone = "18844189217".toString().trim().replaceAll("\\s*",
		// "");
		String code = "+86".toString().trim();

		checkPhoneNum(phone, code);

	}

	/** 检查电话号码 */
	private void checkPhoneNum(String phone, String code) {

//		Log.i("短信验证--------------->", code + "------" + phone);

		if (code.startsWith("+")) {
			code = code.substring(1);
		}

		SMSSDK.getVerificationCode(code, phone.trim(), osmHandler);

	}

	// 自动填写验证码
	final Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			smsReceiver = new SMSReceiver(new SMSSDK.VerifyCodeReadListener() {
				@Override
				public void onReadVerifyCode(final String verifyCode) {
					runOnUiThread(new Runnable() {
						@SuppressLint("HandlerLeak")
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
					TimeDown.setClickable(true);
					TimeDown.setFocusable(true);
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

	

	TextWatcher Password = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			

			if ((arg0.toString()).equals(NewPassword.getText().toString())) {
				confirm.setImageResource(R.drawable.forget_password_true);
			} else {
				confirm.setImageResource(R.drawable.forget_password_confirm);
			}

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}
	};

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
				Toast.makeText(Forget_Password.this, "验证码已发送！",
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

				Toast.makeText(Forget_Password.this, "该手机号码尚未注册！",
						Toast.LENGTH_LONG).show();

			}
		});

	}

}
