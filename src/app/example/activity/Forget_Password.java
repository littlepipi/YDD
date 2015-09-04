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

	private EditText TeleNum;// �ֻ���

	private EditText AuthCode;// ��֤��

	private Button GetAuthCode;// ��ȡ��֤�밴ť

	private EditText NewPassword;// ������
	private String EditNewPassword;// ������
	private TextView TimeDown;// ����ʱ
	private EditText AffirmNewPassword;// ȷ��������
	private String EditAffirmNewPassword;// ȷ��������
	private Button Next;// ��ɰ�ť
	private ImageView confirm;
	private ImageView back;

	String phone_number, identify, password, operation;

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
		// �ؼ���ʼ��
		init();

		APPKEY = a.toString().trim();
		APPSECRET = b.toString().trim();
		// isEmpty�жϱ����Ƿ��ʼ��
		if (TextUtils.isEmpty(APPKEY) || TextUtils.isEmpty(APPSECRET)) {
		} else {
			initSDK();
		}
	

		// �ռ��ĵ���¼�
		onClick();

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

						if ("".equals(TeleNum.getText().toString().trim())) {
							Toast.makeText(Forget_Password.this, "�������ֻ����룡",
									Toast.LENGTH_SHORT).show();
							break;
						}
						if (TeleNum.getText().toString().trim().length() != 11) {
							Toast.makeText(Forget_Password.this, "�ֻ��������",
									Toast.LENGTH_SHORT).show();
							break;
						}

					case 2:

						if ("".equals(AuthCode.getText().toString().trim())) {
							Toast.makeText(Forget_Password.this, "��������֤�룡",
									Toast.LENGTH_SHORT).show();
							break;
						}
						if (AuthCode.getText().toString().trim().length() != 4) {
							Toast.makeText(Forget_Password.this,
									"��֤���ʽ���ԣ���������λ���֣�", Toast.LENGTH_SHORT)
									.show();
							break;
						}

					case 3:

						if ("".equals(NewPassword.getText().toString().trim())) {
							Toast.makeText(Forget_Password.this, "���������룡",
									Toast.LENGTH_SHORT).show();
							break;
						}

						if (NewPassword.getText().toString().trim().length() < 6
								|| NewPassword.getText().toString().trim()
										.length() > 18) {
							Toast.makeText(Forget_Password.this,
									"����������6λС��18λ", Toast.LENGTH_SHORT).show();
							break;
						}

					case 4:

						if (!(password.equals(AffirmNewPassword.getText()
								.toString()))) {
							Toast.makeText(Forget_Password.this, "�����������벻һ�£�",
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
									"���粻���ã��������磡", Toast.LENGTH_SHORT).show();
						}
						break;

					}
				} else {
					((Throwable) data).printStackTrace();
					// ��֤�벻��ȷ
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
		// ��ȡ��֤��
		GetAuthCode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// �����Ͷ�����֤��
				if (countryRules == null || countryRules.size() <= 0) {

					boolean net = CheckNet.detect(Forget_Password.this);

					if (net) {

						switch (1) {

						case 1:
							if ("".equals(TeleNum.getText().toString().trim())) {
								Toast.makeText(Forget_Password.this,
										"�������ֻ����룡", Toast.LENGTH_SHORT).show();
								break;
							}
							if (TeleNum.getText().toString().trim().length() != 11) {
								Toast.makeText(Forget_Password.this, "�ֻ��������",
										Toast.LENGTH_SHORT).show();
								break;
							}

						default:
							AsyncHttpClientGet(TeleNum.getText().toString()
									.trim());
							break;
						}

					} else {

						Toast.makeText(Forget_Password.this, "���粻���ã��������磡",
								Toast.LENGTH_SHORT).show();
					}

				} else {

					Toast.makeText(Forget_Password.this, "��ȡ��֤��ʧ�ܣ�",
							Toast.LENGTH_LONG).show();

				}

			}
		});

		// ����޸�
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
							Toast.makeText(Forget_Password.this, "�������ֻ����룡",
									Toast.LENGTH_SHORT).show();
							break;
						}
						if (TeleNum.getText().toString().trim().length() != 11) {
							Toast.makeText(Forget_Password.this, "�ֻ��������",
									Toast.LENGTH_SHORT).show();
							break;
						}

					default:

						SMSSDK.submitVerificationCode("86", phone_number,
								identify);
						break;
					}

				} else {

					Toast.makeText(Forget_Password.this, "���粻���ã��������磡",
							Toast.LENGTH_SHORT).show();
				}

			}

		});

		// �ղ�����֤�����¼�

		TimeDown.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new MyAlertDialog(Forget_Password.this).builder()
						.setTitle("��ʾ��").setMsg("���»�ȡ��֤�룿")
						.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(View v) {

								// �����Ͷ�����֤��
								SMSSDK.getSupportedCountries();

								// ��ȡ��֤��ɹ����ִ�ж���
								Toast.makeText(Forget_Password.this, "��֤���ѷ��ͣ�",
										Toast.LENGTH_LONG).show();
								// ����ʱ

								time = 60;// ��Ϊһ��ִ����֮��timeΪ0��������������
								Message message = handler4.obtainMessage(1);
								handler4.sendMessageDelayed(message, 1000);
								// �Զ���д��֤��
								handler2.sendEmptyMessage(0);

								finish();
							}
						}).setNegativeButton("ȡ��", new OnClickListener() {
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
//		System.out.println("----------�������������ϴ�");// ��ӡ
		// ���ص���½ҳ��
		final Handler handler9 = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				Intent intent = new Intent(Forget_Password.this,
						LoginActvity.class);

				Forget_Password.this.startActivity(intent);

				Forget_Password.this.finish();

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
		params.put("identify", identify);
		params.put("operation", "change_password");
//		System.out.println("�ϴ�����--------->" + phone_number + "----" + password
//				+ "----" + identify + "----" + operation);
		// ִ��post����
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

//				System.out.println("----------�ɹ�");// ��ӡ
//				// String i = new String(responseBody);
//				System.out.println("statusCode---------->" + statusCode);
				// ���ص�����ҳ��
				handler9.sendEmptyMessage(0);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				if (statusCode == 404) {

					Toast.makeText(Forget_Password.this, "�õ绰������δע�ᣡ",
							Toast.LENGTH_SHORT).show();

				}

			}
		});

	}

	private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
		// ���������б�

//		System.out.println("��3վ");
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

		String phone = TeleNum.getText().toString().trim()
				.replaceAll("\\s*", "");
		// String phone = "18844189217".toString().trim().replaceAll("\\s*",
		// "");
		String code = "+86".toString().trim();

		checkPhoneNum(phone, code);

	}

	/** ���绰���� */
	private void checkPhoneNum(String phone, String code) {

//		Log.i("������֤--------------->", code + "------" + phone);

		if (code.startsWith("+")) {
			code = code.substring(1);
		}

		SMSSDK.getVerificationCode(code, phone.trim(), osmHandler);

	}

	// �Զ���д��֤��
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
					TimeDown.setClickable(true);
					TimeDown.setFocusable(true);
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
				Toast.makeText(Forget_Password.this, "��֤���ѷ��ͣ�",
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

				Toast.makeText(Forget_Password.this, "���ֻ�������δע�ᣡ",
						Toast.LENGTH_LONG).show();

			}
		});

	}

}
