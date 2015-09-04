package app.example.activity;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.loonggg.utils.MyAlertDialog;
import tx.ydd.app.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import app.example.service.Myservice;

public class SendMessage extends Activity {
	private TextView wordsText;
	private EditText messgae;
	private Button send;
	private Button cancel;
	private String phoneno;
	private String smsText;
	private PendingIntent sentPI;
	private PendingIntent deliverPI;
	private ImageButton back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_message);
		SysApplication.getInstance().addActivity(this);
	
		wordsText = (TextView) findViewById(R.id.feedback_text_front);
		send = (Button) findViewById(R.id.send);
		messgae = (EditText) findViewById(R.id.message);
		back = (ImageButton) findViewById(R.id.back);
	
		Intent phone = getIntent();
		final String phonenumber = phone.getStringExtra("number");
		String SENT_SMS_ACTION = "SENT_SMS_ACTION";
		Intent sentIntent = new Intent(SENT_SMS_ACTION);
		sentPI = PendingIntent.getBroadcast(SendMessage.this, 0, sentIntent, 0);
		
		SendMessage.this.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context _context, Intent _intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(SendMessage.this, "消息发送成功！",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					break;
				}
			}
		}, new IntentFilter(SENT_SMS_ACTION));

		String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
		// create the deilverIntent parameter
		Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
		deliverPI = PendingIntent.getBroadcast(SendMessage.this, 0,
				deliverIntent, 0);

		SendMessage.this.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context _context, Intent _intent) {

			}
		}, new IntentFilter(DELIVERED_SMS_ACTION));

		messgae.addTextChangedListener(new TextWatcher() {

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

		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				phoneno = phonenumber;
				smsText = messgae.getText().toString();
				if (smsText == null || "".equals(smsText.trim())) {
					Toast.makeText(getApplicationContext(), "对不起，您发送的消息不能为空！",
							Toast.LENGTH_SHORT).show();
				} else {

					/*
					 * 
					 * Uri uri = Uri.parse("smsto:"+phoneno); Intent it = new
					 * Intent(Intent.ACTION_SENDTO, uri);
					 * it.putExtra("sms_body",smsText); startActivity(it);
					 */

					SmsManager smsManager = SmsManager.getDefault();

					if (smsText.length() > 500) {
						List<String> contents = smsManager
								.divideMessage(smsText);
						for (String sms : contents) {
							smsManager.sendTextMessage(phoneno, null, sms,
									sentPI, deliverPI);
						}

						Toast.makeText(SendMessage.this, "消息发送成功！",
								Toast.LENGTH_SHORT).show();
						SendMessage.this.finish();// 直接finish页面

					} else {
						smsManager.sendTextMessage(phoneno, null, smsText,
								sentPI, deliverPI);
						Toast.makeText(SendMessage.this, "消息发送成功！",
								Toast.LENGTH_SHORT).show();

						SendMessage.this.finish();
					}
				}
			}
		});
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				back();
			}
		});

	}

	private void back() {

		new MyAlertDialog(SendMessage.this).builder().setTitle("提示！")
				.setMsg("是否丢弃已编辑短信？")
				.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(View v) {
						SendMessage.this.finish();

						finish();
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				}).show();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			back();
		}
		return true;
	}
}
