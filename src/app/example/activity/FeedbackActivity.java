package app.example.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Url.UrlPath;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FeedbackActivity extends Activity {
	private TextView wordsText;
	private EditText editText, editcontact;
	private Button submit;
	private ImageButton back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_feedback);
		SysApplication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub

		wordsText = (TextView) findViewById(R.id.feedback_text_front);
		editText = (EditText) findViewById(R.id.feedback_edit);
		editcontact = (EditText) findViewById(R.id.contact);
		submit = (Button) findViewById(R.id.feedback_submit);
		back = (ImageButton) findViewById(R.id.feedback_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					String num = (0 + s.toString().getBytes("gbk").length / 2)
							+ "";
					wordsText.setText(num);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();

				}

			}
		});

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String content = editText.getText().toString().trim();
				String contact = editcontact.getText().toString().trim();

				boolean network = detect(FeedbackActivity.this);

				if (network) {

					if ("".equals(content)) {

						Toast.makeText(FeedbackActivity.this, "����δд�±������Ŷ��",
								Toast.LENGTH_SHORT).show();

					} else {
						submitAsyncHttpClientPost(content, contact);
					}

				} else {

					Toast.makeText(FeedbackActivity.this, "���������ӣ��������磡",
							Toast.LENGTH_SHORT).show();

				}

			}
		});

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					String num = (0 + s.toString().getBytes("gbk").length / 2)
							+ "";
					wordsText.setText(num);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();

				}

			}
		});

	}

	protected void submitAsyncHttpClientPost(String content, String contact) {
		// TODO Auto-generated method stub

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Toast.makeText(getApplicationContext(), "�ύ�ɹ���",
						Toast.LENGTH_LONG).show();

				FeedbackActivity.this.finish();
			}
		};

		// �������󵽷�����
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.feedbackOperateApi;
		// �����������
		RequestParams params = new RequestParams();
		params.put("content", content);
		params.put("contact", contact);

		// ִ��post����
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				// ��תactivity����������߳���
				handler.sendEmptyMessage(0);
				/**************************************/

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

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
