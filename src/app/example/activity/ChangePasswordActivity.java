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
	// ��д�Ӷ���SDKӦ�ú�̨ע��õ���APPKEY
	private static String APPKEY;
	// ��д�Ӷ���SDKӦ�ú�̨ע��õ���APPSECRET
	private static String APPSECRET;
	String a = "5aec12823819";
	String b = "b42dafb1c491a7655cdf907b2c608f05 ";

	// ���Һ������
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
		// �ؼ���ʼ��
		initView();

		APPKEY = a.toString().trim();
		APPSECRET = b.toString().trim();
		// isEmpty�жϱ����Ƿ��ʼ��
		if (TextUtils.isEmpty(APPKEY) || TextUtils.isEmpty(APPSECRET)) {
		} else {
			initSDK();
		}
		System.out.println("��2վ");
		// �ռ��ĵ���¼�
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

		// �ӵ�½��ȡ�绰����
		// ͬ�����ڶ�ȡSharedPreferences����ǰҪʵ������һ��SharedPreferences����
		SharedPreferences mySharedPreferencesphone = getSharedPreferences(
				"phone_number", Activity.MODE_PRIVATE);
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
		phone_number = mySharedPreferencesphone.getString("phone_number", "")
				.toString().trim();

		// �ӵ�¼��ȡ����token
		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
		token = mySharedPreferencestoken.getString("token", "").toString()
				.trim();

		operation = "change_password".toString().trim();

//		System.out.println(phone_number + "======" + password + "======="
//				+ token + "======" + identify);
	}

	private void initSDK() {
		// ��ʼ������SDK
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		handler = new EventHandler() {
			@SuppressWarnings("unchecked")
			public void afterEvent(final int event, final int result,
					final Object data) {

				runOnUiThread(new Runnable() {
					public void run() {
						if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
							// �ύ��֤��
							afterSubmit(result, data);

						} else if (result == SMSSDK.RESULT_COMPLETE) {

							if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
								// ����֧�ֹ����б�
								onCountryListGot((ArrayList<HashMap<String, Object>>) data);

							}

							else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

								// ������֤�����ת����֤����дҳ��
								System.out.println("��ȡ��֤��ɹ�");

							}
						} else {
							if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE
									&& data != null
									&& (data instanceof UserInterruptException)) {
								// ���ڴ˴��ǿ������Լ�����Ҫ�жϷ��͵ģ����ʲô��������
								return;
							}

							// ���ݷ��������ص�������󣬸�toast��ʾ
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
		// ��ȡ��֤���¼�
		GetAuthCode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// �����Ͷ�����֤��
				if (countryRules == null || countryRules.size() <= 0) {

					boolean net = CheckNet.detect(ChangePasswordActivity.this);

					if (net) {

						switch (1) {

						case 1:
							if ("".equals(textPhone.getText().toString().trim())) {
								Toast.makeText(ChangePasswordActivity.this,
										"�������ֻ����룡", Toast.LENGTH_SHORT).show();
								break;
							}
							if (textPhone.getText().toString().trim().length() != 11) {
								Toast.makeText(ChangePasswordActivity.this,
										"�ֻ��������", Toast.LENGTH_SHORT).show();
								break;
							}

						default:
							AsyncHttpClientGet(textPhone.getText().toString());
							break;
						}

					} else {

						Toast.makeText(ChangePasswordActivity.this,
								"���粻���ã��������磡", Toast.LENGTH_SHORT).show();
					}

				} else {

					Toast.makeText(ChangePasswordActivity.this, "��ȡ��֤��ʧ�ܣ�",
							Toast.LENGTH_LONG).show();

				}

			}
		});

		// �ղ�����֤�����¼�
		TimeDown.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new MyAlertDialog(ChangePasswordActivity.this).builder()
						.setTitle("��ʾ��").setMsg("���»�ȡ��֤�룿")
						.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(View v) {

								// �����Ͷ�����֤��
								SMSSDK.getSupportedCountries();

								// ��ȡ��֤��ɹ����ִ�ж���
								Toast.makeText(ChangePasswordActivity.this,
										"��֤���ѷ��ͣ�", Toast.LENGTH_LONG).show();
								// ����ʱ
								time = 60;// ��Ϊһ��ִ����֮��timeΪ0��������������
								Message message = handler4.obtainMessage(1);
								handler4.sendMessageDelayed(message, 1000);

								// �Զ���д��֤��
								handler2.sendEmptyMessage(0);

							}
						}).setNegativeButton("ȡ��", new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();

			}
		});

		// ����޸ģ��ϴ�
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
									"�������ֻ����룡", Toast.LENGTH_SHORT).show();
							break;
						}
						if (textPhone.getText().toString().trim().length() != 11) {
							Toast.makeText(ChangePasswordActivity.this,
									"�ֻ��������", Toast.LENGTH_SHORT).show();
							break;
						}

					default:

						SMSSDK.submitVerificationCode("86", phone_number,
								identify);
						break;
					}

				} else {

					Toast.makeText(ChangePasswordActivity.this, "���粻���ã��������磡",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// �����¼�
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
		// ���������б�

		System.out.println("��3վ");
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
		// ����ֻ�����

		String phone = textPhone.getText().toString().trim()
				.replaceAll("\\s*", "");

		String code = "+86".toString().trim();

		
		checkPhoneNum(phone, code);

	}

	/** ���绰���� */
	private void checkPhoneNum(String phone, String code) {

//		Log.i("������֤--------------->", code + "------" + phone);

		if (code.startsWith("+")) {
			code = code.substring(1);
		}
//		System.out.println("��5վ" + code + phone);

		SMSSDK.getVerificationCode(code, phone.trim(), osmHandler);

//		System.out.println("��6վ------->" + code + phone);

	}

	// �Զ���д��֤��
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

	// ����ʱ
	final Handler handler4 = new Handler() {
		public void handleMessage(Message msg) { // handle message
			switch (msg.what) {
			case 1:
				time--;
				TimeDown.setVisibility(View.VISIBLE);
				TimeDown.setFocusable(false);
				TimeDown.setClickable(false);
				TimeDown.setText("��ȡ��֤�뻹��" + time + "��");

				if (time > 0) {
					Message message = handler4.obtainMessage(1);
					handler4.sendMessageDelayed(message, 1000); // send message
				} else {
					TimeDown.setFocusable(true);
					TimeDown.setClickable(true);
					TimeDown.setText("�ղ�����֤�룿");
				}
			}

			super.handleMessage(msg);
		}
	};

	protected void onDestroy() {

		// ���ٻص������ӿ�
		SMSSDK.unregisterAllEventHandler();

		super.onDestroy();
	}

	public void onRestart() {
		SMSSDK.registerEventHandler(handler);
		super.onRestart();
	}

	// ���绰�����Ƿ��Ѿ�ע��
	private void AsyncHttpClientGet(String phone) {
		// TODO Auto-generated method stub
		// �������󵽷�����
		AsyncHttpClient client = new AsyncHttpClient();

		String urlr = UrlPath.exist + phone;

		String url = urlr;

		// ִ��post����
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				// ������֤��
				SMSSDK.getSupportedCountries();
				Toast.makeText(ChangePasswordActivity.this, "��֤���ѷ��ͣ�",
						Toast.LENGTH_LONG).show();

				// ����ʱ
				Message message = handler4.obtainMessage(1);
				handler4.sendMessageDelayed(message, 1000);
				// �Զ���д��֤��
				handler2.sendEmptyMessage(0);

				GetAuthCode.setEnabled(false);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				Toast.makeText(ChangePasswordActivity.this, "���ֻ�������δע�ᣡ",
						Toast.LENGTH_LONG).show();

			}
		});

	}

	/**
	 * �ύ��֤��ɹ����ִ���¼�
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
									"�������ֻ����룡", Toast.LENGTH_SHORT).show();
							break;
						}

					case 2:

						if ("".equals(identify.toString())) {
							Toast.makeText(ChangePasswordActivity.this,
									"��������֤�룡", Toast.LENGTH_SHORT).show();
							break;
						}
						if (AuthCode.getText().toString().trim().length() != 4) {
							Toast.makeText(ChangePasswordActivity.this,
									"��֤���ʽ���ԣ���������λ���֣�", Toast.LENGTH_SHORT)
									.show();
							break;
						}

					case 3:

						if ("".equals(password.toString())) {

							Toast.makeText(ChangePasswordActivity.this,
									"�����������룡", Toast.LENGTH_SHORT).show();
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
									"���粻���ã��������磡", Toast.LENGTH_SHORT).show();
						}
						break;

					}
				} else {
					((Throwable) data).printStackTrace();
					// ��֤�벻��ȷ
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

	// �ϴ�����
	protected void AsyncHttpClientPost(String phone_number, String password,
			String token, String identify, String operation) {
		// TODO Auto-generated method stub
//		System.out.println("----------�����޸������ϴ�");// ��ӡ
		// ���ص�����ҳ��
		final Handler handler9 = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				Intent intent = new Intent(ChangePasswordActivity.this,
						AccountSettingActivity.class);
				ChangePasswordActivity.this.startActivity(intent);

				ChangePasswordActivity.this.finish();

				Toast.makeText(getApplicationContext(), "�޸ĳɹ�",
						Toast.LENGTH_LONG).show();

			}
		};
		// �������󵽷�����
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.patchApi;
		// �����������
		RequestParams params = new RequestParams();
		params.put("phone_number", phone_number);
		params.put("password", password);
		params.put("token", token);
		params.put("identify", identify);
		params.put("operation", "change_password");
//		System.out.println("�ϴ�����--------->" + phone_number + "----" + password
//				+ "----" + token + "----" + identify + "----" + operation);
		// ִ��post����
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

//				System.out.println("----------�ɹ�");// ��ӡ
//				System.out.println("statusCode---------->" + statusCode);
				// ���ص�����ҳ��
				handler9.sendEmptyMessage(0);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
//				System.out.println("�޸�ʧ��");
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
