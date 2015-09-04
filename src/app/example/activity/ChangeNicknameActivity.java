package app.example.activity;

import net.loonggg.utils.CheckNet;

import org.apache.http.Header;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;
import app.example.netserver.MyHttpPerson;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ChangeNicknameActivity extends Activity {

	private EditText nickname;
	private ImageButton changeNicknameBack;
	private Button buttonFinish;
	String name;
	// 从登陆获取过来的电话号码

	String phone_number;
	String token;
	String password;
	String location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_nickname);
		SysApplication.getInstance().addActivity(this);
		init();
	}

	private void init() {

		nickname = (EditText) findViewById(R.id.nickname);

		changeNicknameBack = (ImageButton) findViewById(R.id.change_nickname_back);

		changeNicknameBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				ChangeNicknameActivity.this.overridePendingTransition(
						R.anim.in_from_left, R.anim.out_to_right);
			}
		});
		buttonFinish = (Button) findViewById(R.id.change_nickname_finish);
		Bundle bundle = this.getIntent().getExtras();

		String nick_name = bundle.getString("nickname");

		nickname.setText(nick_name);

		buttonFinish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (nickname.getText().toString().equals("")) {
					Toast.makeText(MyApplication.getAppContext(), "用户名不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					// 从登陆获取电话号码
					// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
					SharedPreferences mySharedPreferencesphone = getSharedPreferences(
							"phone_number", Activity.MODE_PRIVATE);
					// 使用getString方法获得value，注意第2个参数是value的默认值
					phone_number = mySharedPreferencesphone
							.getString("phone_number", "").toString().trim();
					SharedPreferences mysSharedPreferencespassword = getSharedPreferences(
							"phone_password", MODE_PRIVATE);

					password = mysSharedPreferencespassword
							.getString("password", "").toString().trim();
					SharedPreferences MyHttpPerson = getSharedPreferences(
							"MyHttpPerson", MODE_PRIVATE);

					location = MyHttpPerson.getString("personlocation", "")
							.toString();
					// 从登录获取令牌token
					SharedPreferences mySharedPreferencestoken = getSharedPreferences(
							"token", Activity.MODE_PRIVATE);
					// 使用getString方法获得value，注意第2个参数是value的默认值
					token = mySharedPreferencestoken.getString("token", "")
							.toString().trim();
					// location=
					name = nickname.getText().toString().trim();
//					System.out.println(phone_number + "======" + password
//							+ "=======" + token);

					boolean net = CheckNet.detect(ChangeNicknameActivity.this);

					if (net) {

						AsyncHttpClientPost(name, location, token,
								phone_number, password, "change_others");

					} else {

						Toast.makeText(ChangeNicknameActivity.this,
								"网络不可用，请检查网络！", Toast.LENGTH_SHORT).show();
					}

				}

			}
		});

	}

	private void AsyncHttpClientPost(final String name, String location,
			String token, final String phone_number, String password,
			String operation) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {

					Toast.makeText(getApplicationContext(), "修改成功",
							Toast.LENGTH_LONG).show();

					Intent intent = new Intent(ChangeNicknameActivity.this,
							AccountSettingActivity.class);
					intent.putExtra("name", name);
					ChangeNicknameActivity.this.setResult(4, intent);

					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);

					ChangeNicknameActivity.this.finish();

					MyHttpPerson myHttpPerson = new MyHttpPerson(phone_number);
					myHttpPerson.submitAsyncHttpClientGet();

				}
			}
		};

		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.patchApi;
		// 创建请求参数
		RequestParams params = new RequestParams();
		params.put("operation", "change_others");
		params.put("password", password);
		params.put("token", token);
		params.put("phone_number", phone_number);
		params.put("name", name);
		params.put("location", location);

		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				System.out.println("成功");// 打印
				// String i = new String(responseBody);
				System.out.print("statusCode" + statusCode);

				handler.sendEmptyMessage(0);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				System.out.print("修改失败");

				System.out.print("statusCode" + statusCode);

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			ChangeNicknameActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);

	}
}
