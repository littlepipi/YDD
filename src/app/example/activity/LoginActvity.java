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
 * ͨ����Դ���AsyncHttpClient��get/post������
 * 
 * ���ε�������ԭ�����ע��ֱ�ӵ�������֤������
 * @author 
 *
 */
public class LoginActvity extends Activity {

	private EditText login_accounts;// ��½�˺�
	private EditText login_password;// ��½����
	// private CheckBox checkBox_login;//��ס����
	private Button login_login;// ��½��ť
	private TextView login_ForgetPassword;// ��������
	private Button login_QuickRegister;// ����ע��

	public SharedPreferences pre = null;// ��ס�����õ������ݿ�
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
		SysApplication.getInstance().addActivity(this);// Ŀ����ʹ���������˳�

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

		// ����ע��
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
		// ��������
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

		// ��ס����
		pre = this.getSharedPreferences("phone_password", Context.MODE_PRIVATE);
		// ��һ�������Ǵ洢ʱ�����ƣ��ڶ������������ļ��Ĵ򿪷�ʽ~
		// ������������һ��������preferece������(���磺MyPref),�ڶ��������Ǵ򿪵ķ�ʽ��һ��ѡ��private��ʽ��
		if (pre != null) {
			// ��ס������
			if (pre.getBoolean("phone_password", false)) {
				login_accounts.setText(pre.getString("phone", null));
				login_password.setText(pre.getString("password", null));
				// checkBox_login.setChecked(true) ;
			} else {

			}
		}

		/************************************/
		// ��¼��ť
		login_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				boolean network = detect(LoginActvity.this);

				if (network) {

					final String phone_number = login_accounts.getText()
							.toString();
					final String password = login_password.getText().toString();
					Log.i("TAG", phone_number + "_" + password);

					// ����绰���봫�ݸ�ʧ������
					// ʵ����SharedPreferences���󣨵�һ����
					SharedPreferences mySharedPreferences1 = getSharedPreferences(
							"phone_number", Activity.MODE_PRIVATE);
					// ʵ����SharedPreferences.Editor���󣨵ڶ�����
					SharedPreferences.Editor editor1 = mySharedPreferences1
							.edit();
					// ��putString�ķ�����������
					editor1.putString("phone_number", phone_number);
					// �ύ��ǰ����
					editor1.commit();
					SharedPreferences mySharedPreferences3 = getSharedPreferences(
							"password", Activity.MODE_PRIVATE);
					// ʵ����SharedPreferences.Editor���󣨵ڶ�����
					SharedPreferences.Editor editor3 = mySharedPreferences3
							.edit();
					// ��putString�ķ�����������
					editor3.putString("password", password);
					// �ύ��ǰ����
					editor3.commit();
					// ʵ����SharedPreferences���󣨵�һ����
					SharedPreferences mySharedPreferences2 = getSharedPreferences(
							"phone_number" + phone_number,
							Activity.MODE_PRIVATE);
					// ʵ����SharedPreferences.Editor���󣨵ڶ�����
					SharedPreferences.Editor editor2 = mySharedPreferences2
							.edit();
					// ��putString�ķ�����������
					editor2.putString("phone_number", phone_number);
					// �ύ��ǰ����
					editor2.commit();

					/************************************/
					// ��ס����
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
							Toast.makeText(getApplicationContext(), "�������ֻ����룡",
									Toast.LENGTH_SHORT).show();
							break;
						}

					case 2:

						if ("".equals(login_password.getText().toString())) {

							Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
							vibrator.vibrate(500);
							Toast.makeText(getApplicationContext(), "���������룡",
									Toast.LENGTH_SHORT).show();
							break;
						}

					default:

						loginAsyncHttpClientPost(phone_number, password);

						break;
					}

				} else {

					Toast.makeText(getApplicationContext(), "���������ӣ��������磡",
							Toast.LENGTH_LONG).show();

				}
			}
		});
	}

	/**
	 * ͨ����Դ���AsyncHttpClient��post������
	 * 
	 * @param name
	 * @param pass
	 */
	protected void loginAsyncHttpClientPost(final String phone_number,
			final String password) {
		// TODO Auto-generated method stub
		// �������󵽷�����
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				

				Intent intent = new Intent(LoginActvity.this,
						MainActivity.class);
				LoginActvity.this.startActivity(intent);
				Toast.makeText(getApplicationContext(), "��¼�ɹ�",
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

		// �����������
		RequestParams params = new RequestParams();
		params.put("phone_number", phone_number);
		params.put("password", password);
		// ִ��post����
		client.post(url, params, new AsyncHttpResponseHandler() {

			/**
			 * �ɹ�����ķ��� statusCode:��Ӧ��״̬��; headers:��Ӧ��ͷ��Ϣ ���� ��Ӧ��ʱ�䣬��Ӧ�ķ����� ;
			 * responseBody:��Ӧ���ݵ��ֽ�
			 */

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				// ��ת���������߳�������
				handler.sendEmptyMessage(0);

				// ��������

				/*****************************************/

				// ����json���@ȡ����token
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

					// ����token�����ύʧ�����������ʱ�õ�
					// ʵ����SharedPreferences���󣨵�һ����
					SharedPreferences mySharedPreferences = getSharedPreferences(
							"token", Activity.MODE_PRIVATE);
					// ʵ����SharedPreferences.Editor���󣨵ڶ�����
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// ��putString�ķ�����������
					editor.putString("token", token);
					// �ύ��ǰ����
					editor.commit();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/*****************************************/

			}

			/**
			 * ʧ�ܴ���ķ��� error����Ӧʧ�ܵĴ�����Ϣ��װ������쳣������
			 */
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

				// ��ӡ������Ϣ
				error.printStackTrace();
//				Log.i("LoginActivity-------", statusCode + "");

				if (statusCode == 401) {

					Toast.makeText(LoginActvity.this, "��������������������룡",
							Toast.LENGTH_LONG).show();

				}
				if (statusCode == 404) {
					Toast.makeText(LoginActvity.this, "�õ绰������δע�ᣡ",
							Toast.LENGTH_LONG).show();

				}

			}
		});
	}

	/************************************/
	// ˫�����ؼ��˳�ϵͳ
	// private long exitTime = 0;
	//
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK
	// && event.getAction() == KeyEvent.ACTION_DOWN) {
	//
	// if ((System.currentTimeMillis() - exitTime) > 2000) {
	// Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
	// Toast.LENGTH_SHORT).show();
	// exitTime = System.currentTimeMillis();
	// } else {
	// // finish();
	// // System.exit(0);
	// SysApplication.getInstance().exit();// ��ֱ�ӹر����е�Acitivity���˳�Ӧ�ó���
	// }
	// return true;
	// }
	// return true;
	// }

	/*
	 * 
	 * �ж�ϵͳ����
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
