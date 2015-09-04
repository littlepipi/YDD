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
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
		phone_number = mySharedPreferencesphone.getString("phone_number", "")
				.toString().trim();
		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
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

						Toast.makeText(PublishWord.this, "���Բ���Ϊ��Ŷ��",
								Toast.LENGTH_SHORT).show();

					} else {
						submitAsyncHttpClientPost(phone_number, token,
								to_phone_number, content);

					}

				} else {

					Toast.makeText(PublishWord.this, "���������ӣ��������磡",
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

						Toast.makeText(PublishWord.this, "���Բ���Ϊ��Ŷ��",
								Toast.LENGTH_SHORT).show();

					} else {
						submitAsyncHttpClientPost(phone_number, token,
								to_phone_number, content);

					}

				} else {

					Toast.makeText(PublishWord.this, "���������ӣ��������磡",
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
				Toast.makeText(PublishWord.this, "����ɹ���", Toast.LENGTH_SHORT)
						.show();
				finish();

			}
		};

		// �������󵽷�����
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.wordsAddApi;

		// �����������
		RequestParams params = new RequestParams();

		params.put("token", token);

		params.put("phone_number", phone_number);

		params.put("to_phone_number", to_phone_number);

		params.put("content", content);

		// ִ��post����
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
//				Log.i("�ύ״̬", "" + "�ύ�ɹ�");
//				Log.i("statusCode", "" + statusCode);

				// ��תactivity����������߳���
				handler.sendEmptyMessage(0);
				/**************************************/

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// ��ӡ������Ϣ
				error.printStackTrace();
//				Log.i("�ύ״̬", "" + "�ύʧ��");
//				String i = new String(responseBody);
//				Log.i("statusCode", "" + statusCode);
//				Log.i("responseBody", "" + i);

			}
		});
	}
}
