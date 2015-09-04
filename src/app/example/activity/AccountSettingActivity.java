package app.example.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import net.loonggg.utils.ActionSheetDialog;
import net.loonggg.utils.ActionSheetDialog.OnSheetItemClickListener;
import net.loonggg.utils.ActionSheetDialog.SheetItemColor;

import org.apache.http.Header;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Url.UrlPath;
import app.example.internet.picture.ImageLoader;
import app.example.netserver.MyHttpPerson;
import app.example.netserver.MySQLiteMethodDetails;
import app.example.service.Myservice;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AccountSettingActivity extends Activity {

	private TextView portraitUseless, changePassword, nickname1,
			nicknameUseless, changePlace, exit, telephone, change;

	public ImageLoader imageLoader;
	private ImageView portrait;
	private ImageButton settingBack;
	private View parentView;
	public static Bitmap bimap;
	InputStream iStream;// �ϴ�ͷ���õ���������
	String location;
	private String personname, personlocation, personportrait;

	String name;
	// �ӵ�½��ȡ�����ĵ绰����

	String phone_number;
	String token;
	String password;
	private boolean flag = false;
	private String KIND = null;
	private boolean flag_location = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_settings);
		SysApplication.getInstance().addActivity(this);
		// parentView = getLayoutInflater().inflate(
		// R.layout.activity_account_settings, null);
		// setContentView(parentView);

		imageLoader = new ImageLoader(AccountSettingActivity.this);

		SharedPreferences MyHttpPerson = getSharedPreferences("MyHttpPerson",
				MODE_PRIVATE);

		personname = MyHttpPerson.getString("personname", "").toString();

		personportrait = MyHttpPerson.getString("personportrait", "")
				.toString();

		personlocation = MyHttpPerson.getString("personlocation", "")
				.toString();
		if (personlocation.indexOf("ѧ") != -1) {
			KIND = "school";
		} else {
			KIND = "society";
		}
		initView();

	}

	/********************************************************************************/
	// ��ʼ���ؼ�
	private void initView() {

		portraitUseless = (TextView) findViewById(R.id.portrait_useless);
		portrait = (ImageView) findViewById(R.id.portrait);
		nickname1 = (TextView) findViewById(R.id.nickname);
		nicknameUseless = (TextView) findViewById(R.id.nickname_useless);
		telephone = (TextView) findViewById(R.id.telephone);
		changePlace = (TextView) findViewById(R.id.change_place_useless);
		change = (TextView) findViewById(R.id.change_place);

		exit = (TextView) findViewById(R.id.exit);
		changePassword = (TextView) findViewById(R.id.change_password);
		settingBack = (ImageButton) findViewById(R.id.setting_back);

		nickname1.setText(personname);

		Intent intent1 = getIntent();

		location = intent1.getStringExtra("location");

		flag_location = intent1.getBooleanExtra("flag_location", false);

		if (flag_location == false) {

			change.setText(personlocation);
		} else {
			change.setText(location);
		}

		if (flag == false) {
			imageLoader.DisplayImage(personportrait, portrait);

		} else if (flag == true) {

		}

		// ��ȡ�绰����
		// ͬ�����ڶ�ȡSharedPreferences����ǰҪʵ������һ��SharedPreferences����
		SharedPreferences mySharedPreferences = getSharedPreferences(
				"phone_number", Activity.MODE_PRIVATE);
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
		String phone_number = mySharedPreferences.getString("phone_number", "")
				.toString().trim();

		telephone.setText(phone_number.toString());

		portraitUseless.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new ActionSheetDialog(AccountSettingActivity.this).builder()
						.setTitle("�������^o^").setCancelable(false)
						.setCanceledOnTouchOutside(true)
						// .addSheetItem("����", SheetItemColor.Blue,
						// new OnSheetItemClickListener() {
						// @Override
						// public void onClick(int which) {
						//
						// Intent intent = new Intent(
						// MediaStore.ACTION_IMAGE_CAPTURE);
						// // �������ָ������������պ����Ƭ�洢��·��
						// intent.putExtra(
						// MediaStore.EXTRA_OUTPUT,
						// Uri.fromFile(new File(
						// Environment
						// .getExternalStorageDirectory(),
						// "icon_guest_count_small")));
						// startActivityForResult(intent, 2);
						//
						// }
						// })
						.addSheetItem("�������ѡȡ", SheetItemColor.Red,
								new OnSheetItemClickListener() {
									@Override
									public void onClick(int which) {

										/**
										 * �տ�ʼ�����Լ�Ҳ��֪��ACTION_PICK�Ǹ���ģ�
										 * ����ֱ�ӿ�IntentԴ�룬
										 * ���Է�������ܶණ����Intent�Ǹ���ǿ��Ķ��������һ����ϸ�Ķ���
										 */
										Intent intent = new Intent(
												Intent.ACTION_PICK, null);

										/**
										 * ������仰����������ʽд��һ����Ч���������
										 * intent.setData(MediaStore.Images
										 * .Media.EXTERNAL_CONTENT_URI);
										 * intent.setType(""image/*");������������
										 * ���������Ҫ�����ϴ�����������ͼƬ����ʱ����ֱ��д��
										 * ��"image/jpeg �� image/png�ȵ�����"
										 * ����ط�С���и����ʣ�ϣ�����ֽ���£�
										 * �����������URI������ΪʲôҪ��������ʽ��дѽ����ʲô����
										 */
										intent.setDataAndType(
												MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
												"image/*");
										startActivityForResult(intent, 1);

									}

								}).show();
			}
		});

		changePlace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AccountSettingActivity.this,
						Province.class);

				/**
				 * ���ñ�־��������ѧУ�����֣�����Ĳ���������Ϊ1��ѧУΪ0��
				 */

				intent.putExtra("kind", KIND);
				intent.putExtra("from", "Setting");
				intent.putExtra("Setting", "Setting");

				AccountSettingActivity.this.startActivity(intent);

				// TODO Auto-generated method stub

			}
		});

		nicknameUseless.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(AccountSettingActivity.this,
						ChangeNicknameActivity.class);

				Bundle bundle = new Bundle();

				bundle.putString("nickname", nickname1.getText().toString());

				intent2.putExtras(bundle);

				AccountSettingActivity.this.startActivityForResult(intent2, 4);

			}
		});

		changePassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent3 = new Intent(AccountSettingActivity.this,
						ChangePasswordActivity.class);
				AccountSettingActivity.this.startActivity(intent3);

			}
		});

		settingBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(AccountSettingActivity.this,
						MainActivity.class);

				intent.putExtra("settingback", "settingback");
				AccountSettingActivity.this.startActivity(intent);

				AccountSettingActivity.this.finish();
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				new ActionSheetDialog(AccountSettingActivity.this)
						.builder()
						.setTitle("ע�����˳���¼")
						.setCancelable(false)
						.setCanceledOnTouchOutside(true)
						.addSheetItem("ע��", SheetItemColor.Red,
								new OnSheetItemClickListener() {
									@Override
									public void onClick(int which) {

										Intent intent4 = new Intent(
												AccountSettingActivity.this,
												MainActivity.class);
										MySQLiteMethodDetails
												.EmptyTable("lostrecord");
										MySQLiteMethodDetails
												.EmptyTable("findrecord");
										MySQLiteMethodDetails
												.EmptyTable("discoverrecord");
										SharedPreferences mySharedPreferencestoken = getSharedPreferences(
												"token", Activity.MODE_PRIVATE);

										mySharedPreferencestoken.edit().clear()
												.commit();
										AccountSettingActivity.this
												.startActivity(intent4);
										finish();

									}
								}).show();

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// �����ֱ�Ӵ�����ȡ
		case 1:
			if (data != null) {
				startPhotoZoom(data.getData());
			}
			break;
		// ����ǵ����������ʱ
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/icon_guest_count_small");

			if (data != null) {

				startPhotoZoom(Uri.fromFile(temp));
			}
			break;
		// ȡ�òü����ͼƬ
		case 3:
			/**
			 * �ǿ��жϴ��һ��Ҫ��֤���������֤�Ļ��� �ڼ���֮��������ֲ����⣬Ҫ���²ü�������
			 * ��ǰ����ʱ���ᱨNullException��С��ֻ ������ط����£���ҿ��Ը��ݲ�ͬ����ں��ʵ� �ط����жϴ����������
			 * 
			 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		case 4:
			setTextView(data);
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setTextView(Intent data) {
		if (data != null) {

			Bundle extras = data.getExtras();
			String name = extras.getString("name");
			nickname1.setText(name);
		}

	}

	// �ü�ͼƬ������ΪͼƬ��

	/**
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * �����������Intent��ACTION����ô֪���ģ���ҿ��Կ����Լ�·���µ�������ҳ
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * ֱ��������Ctrl+F�ѣ�CROP ��֮ǰС��û��ϸ��������ʵ��׿ϵͳ���Ѿ����Դ�ͼƬ�ü�����, ��ֱ�ӵ����ؿ�ģ�С����C C++
		 * ���������ϸ�˽�ȥ�ˣ������Ӿ������ӣ������о���������ô ��������...���
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// �������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);

		intent.putExtra("flag", true);
		startActivityForResult(intent, 3);

	}

	/**
	 * ����ü�֮���ͼƬ����
	 * 
	 * @param picdata
	 */
	@SuppressWarnings("deprecation")
	private void setPicToView(Intent picdata) {

		Bundle extras = picdata.getExtras();

		if (extras != null) {

			flag = picdata.getBooleanExtra("flag", true);

			Bitmap bitmap = extras.getParcelable("data");

			Bitmap bitmap2 = toRoundBitmap(bitmap);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap2.compress(Bitmap.CompressFormat.PNG, 60, stream);
			// ��ͼƬ�����ַ�����ʽ�洢����
			byte[] b = stream.toByteArray();

			iStream = new ByteArrayInputStream(b);

			portrait.setImageBitmap(bitmap2);

			// �ӵ�½��ȡ�绰����
			// ͬ�����ڶ�ȡSharedPreferences����ǰҪʵ������һ��SharedPreferences����
			SharedPreferences mySharedPreferencesphone = getSharedPreferences(
					"phone_number", Activity.MODE_PRIVATE);
			// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
			phone_number = mySharedPreferencesphone
					.getString("phone_number", "").toString().trim();
			SharedPreferences mysSharedPreferencespassword = getSharedPreferences(
					"phone_password", MODE_PRIVATE);

			password = mysSharedPreferencespassword.getString("password", "")
					.toString().trim();

			// �ӵ�¼��ȡ����token
			SharedPreferences mySharedPreferencestoken = getSharedPreferences(
					"token", Activity.MODE_PRIVATE);
			// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
			token = mySharedPreferencestoken.getString("token", "").toString()
					.trim();

			// System.out.println(phone_number + "======" + password + "======="
			// + token);
			AsyncHttpClientPost(token, phone_number, password, iStream);

		}
	}

	private void AsyncHttpClientPost(final String token,
			final String phone_number, String password, InputStream iStream) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {

					Toast.makeText(getApplicationContext(), "�޸ĳɹ�",
							Toast.LENGTH_LONG).show();

					MyHttpPerson myHttpPerson = new MyHttpPerson(phone_number);
					myHttpPerson.submitAsyncHttpClientGet();

				}

			}
		};
		// �������󵽷�����
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.patchApi;
		// �����������
		RequestParams params = new RequestParams();
		params.put("operation", "change_portrait");
		params.put("password", password);
		params.put("token", token);
		params.put("portrait", iStream);
		params.put("phone_number", phone_number);

		// ִ��post����
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				// System.out.println("�ɹ�");// ��ӡ
				// // String i = new String(responseBody);
				// System.out.print("statusCode" + statusCode);

				handler.sendEmptyMessage(0);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// System.out.print("�޸�ʧ��");
				//
				// System.out.print("statusCode" + statusCode);

			}
		});
	}

	public Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;

			left = 0;
			top = 0;
			right = width;
			bottom = width;

			height = width;

			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;

			float clip = (width - height) / 2;

			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;

			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);// ���û����޾��

		canvas.drawARGB(0, 0, 0, 0); // �������Canvas

		// ���������ַ�����Բ,drawRounRect��drawCircle
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// ��Բ�Ǿ��Σ���һ������Ϊͼ����ʾ���򣬵ڶ��������͵����������ֱ���ˮƽԲ�ǰ뾶�ʹ�ֱԲ�ǰ뾶��
		// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// ��������ͼƬ�ཻʱ��ģʽ,�ο�http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // ��Mode.SRC_INģʽ�ϲ�bitmap���Ѿ�draw�˵�Circle

		return output;
	}

	/*******************************************************************/

	/********************************************************************************/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			Intent intent = new Intent(AccountSettingActivity.this,
					MainActivity.class);

			intent.putExtra("settingback", "settingback");
			AccountSettingActivity.this.startActivity(intent);

			AccountSettingActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
