package app.example.activity;

import net.loonggg.utils.CheckNet;
import net.loonggg.utils.Constant;
import net.loonggg.utils.NetManager;

import org.apache.http.Header;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Url.UrlPath;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PublishWord extends Activity {
	TextView publish;
	EditText editText;
	ImageButton imageButton;
	String commitperson;
	String phone_number;
	String to_phone_number;
	String token;
	String content;
	Button publish_word;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publish_words);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		to_phone_number = intent.getStringExtra("commitperson");
		SharedPreferences mySharedPreferencesphone = getSharedPreferences(
				"phone_number", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		phone_number = mySharedPreferencesphone.getString("phone_number", "")
				.toString().trim();
		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		token = mySharedPreferencestoken.getString("token", "");
		publish = (TextView) findViewById(R.id.publish_word_publish);
		editText = (EditText) findViewById(R.id.publish_word_edit);
		publish_word = (Button) findViewById(R.id.publish_word);
		imageButton = (ImageButton) findViewById(R.id.publish_word_back);
		
		publish_word.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				content = editText.getText().toString();

				boolean network = CheckNet.detect(PublishWord.this);
				if (network) {

					if ("".equals(content)) {

						Toast.makeText(PublishWord.this, "留言不能为空哦！",
								Toast.LENGTH_SHORT).show();

					} else {
						submitAsyncHttpClientPost(phone_number, token,
								to_phone_number, content);

					}

				} else {

					Toast.makeText(PublishWord.this, "无网络连接，请检查网络！",
							Toast.LENGTH_SHORT).show();

				}

			}
		});
		
		publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				content = editText.getText().toString();
				boolean network = CheckNet.detect(PublishWord.this);
				if (network) {

					if ("".equals(content)) {

						Toast.makeText(PublishWord.this, "留言不能为空哦！",
								Toast.LENGTH_SHORT).show();

					} else {
						submitAsyncHttpClientPost(phone_number, token,
								to_phone_number, content);

					}

				} else {

					Toast.makeText(PublishWord.this, "无网络连接，请检查网络！",
							Toast.LENGTH_SHORT).show();

				}
			}
		});
		
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	protected void submitAsyncHttpClientPost(String phone_number, String token,
			String to_phone_number, String content) {
		// TODO Auto-generated method stub

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Intent intent = new Intent();
				setResult(Constant.PERSONAL_ADD_WORDS, intent);
				Toast.makeText(PublishWord.this, "发表成功！", Toast.LENGTH_SHORT)
						.show();
				finish();

			}
		};

		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.wordsAddApi;

		// 创建请求参数
		RequestParams params = new RequestParams();

		params.put("token", token);

		params.put("phone_number", phone_number);

		params.put("to_phone_number", to_phone_number);

		params.put("content", content);

		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
//				Log.i("提交状态", "" + "提交成功");
//				Log.i("statusCode", "" + statusCode);

				// 跳转activity必须放在主线程中
				handler.sendEmptyMessage(0);
				/**************************************/

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// 打印错误信息
				error.printStackTrace();
//				Log.i("提交状态", "" + "提交失败");
//				String i = new String(responseBody);
//				Log.i("statusCode", "" + statusCode);
//				Log.i("responseBody", "" + i);

			}
		});
	}
}
