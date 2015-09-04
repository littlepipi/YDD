package app.example.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import net.loonggg.utils.ActionSheetDialog;
import net.loonggg.utils.ActionSheetDialog.OnSheetItemClickListener;
import net.loonggg.utils.ActionSheetDialog.SheetItemColor;
import net.loonggg.utils.CheckNet;

import org.apache.http.Header;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;
import cn.smssdk.gui.RegisterPage;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("ShowToast")
public class RegisterPersonalPageActivity extends Activity implements
		OnClickListener {

	private EditText three_nickname;// �ǳ�
	private EditText three_PutInPassword;// ����
	private EditText three_confirm;// ȷ������
	private Button button;// ���ע��
	private ImageView password_confirm;// �����Ĭ��������ͬʱ��

	private ImageView img;// �û�ͷ��
	String portrait;
	InputStream iStream = null;// �ϴ�ͷ���õ���������
	boolean image = true;// ��Ҫ�����ж�ͷ���Ƿ�Ϊ�գ��ύ����ʱ��������

	// �����˳���ҳ��ʱ����������õ�
	SharedPreferences preferences = null;
	SharedPreferences.Editor cityeditor = null;

	SharedPreferences data = null;
	SharedPreferences.Editor shooleditor = null;

	SharedPreferences message = null;
	SharedPreferences.Editor messageditor = null;

	private View parentView;
	private ImageButton register_ind_page_three_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);

		// ��Ϊ��ʹ�ײ�������ᣬ����
		parentView = getLayoutInflater().inflate(
				R.layout.register_ind_page_three, null);
		setContentView(parentView);
		// �ؼ���ʼ��
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		/*****************************************/
		three_nickname = (EditText) findViewById(R.id.register_ind_page_three_nickname);
		three_PutInPassword = (EditText) findViewById(R.id.register_ind_page_three_PutInPassword);
		three_confirm = (EditText) findViewById(R.id.register_ind_page_three_confirm);
		three_confirm.addTextChangedListener(Password);
		password_confirm = (ImageView) findViewById(R.id.forget_password_confirm);// ��
		register_ind_page_three_back = (ImageButton) findViewById(R.id.register_ind_page_three_back);
		img = (ImageView) findViewById(R.id.register_ind_page_three_head_portrait); // ͷ��
		img.setOnClickListener(this);

		button = (Button) findViewById(R.id.register_ind_page_three_finish);// ע��

		/*****************************************/
		register_ind_page_three_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RegisterPage registerPage = new RegisterPage();
				registerPage.show(MyApplication.getAppContext());
				finish();

			}
		});
		// ������ע��
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// ��ȡ��IdentifyNumPage�������ĵ绰����
				SharedPreferences mySharedPreferences4 = getSharedPreferences(
						"messgae", Activity.MODE_PRIVATE);
				// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
				String phone = mySharedPreferences4
						.getString("phone_number", "").toString().trim();

				String identify = mySharedPreferences4
						.getString("verificationCode", "").toString().trim();

				String password = three_PutInPassword.getText().toString()
						.trim();// ����
				String name = three_nickname.getText().toString().trim();// �ǳ�

				// ͬ�����ڶ�ȡSharedPreferences����ǰҪʵ������һ��SharedPreferences����
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"school", Activity.MODE_PRIVATE);
				// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
				String location1 = mySharedPreferences.getString("school", "")
						.toString().trim();

				// ͬ�����ڶ�ȡSharedPreferences����ǰҪʵ������һ��SharedPreferences����
				SharedPreferences mySharedPreferences2 = getSharedPreferences(
						"city", Activity.MODE_PRIVATE);
				// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
				String location2 = mySharedPreferences2.getString("city", "")
						.toString().trim();

				// Log.i("RegisterPersonalPageActivity", phone + password + name
				// + location2 + identify + iStream);

				if (location1 != "") {


					switch (1) {

					case 1:

						if (image) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"������ͷ��", Toast.LENGTH_SHORT).show();
							break;
						}

					case 2:

						if ("".equals(name.toString())) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"�������ǳƣ�", Toast.LENGTH_SHORT).show();
							break;
						}

					case 3:

						if ("".equals(password.toString())) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"���������룡", Toast.LENGTH_SHORT).show();
							break;
						}

						if (three_PutInPassword.getText().toString().trim()
								.length() < 6
								|| three_PutInPassword.getText().toString()
										.trim().length() > 18) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"����������6λС��18λ", Toast.LENGTH_SHORT).show();
							break;
						}

					case 4:

						if (!(password.equals(three_confirm.getText()
								.toString()))) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"�����������벻һ�£�", Toast.LENGTH_SHORT).show();
							break;
						}

					default:

						boolean net = CheckNet
								.detect(RegisterPersonalPageActivity.this);

						if (net) {
							registerAsyncHttpClientPost(phone, password, name,
									location1, identify, iStream);

						} else {

							Toast.makeText(RegisterPersonalPageActivity.this,
									"���粻���ã��������磡", Toast.LENGTH_SHORT).show();
						}

						break;
					}

				} else {
					switch (1) {

					case 1:

						if (image) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"������ͷ��", Toast.LENGTH_SHORT).show();
							break;
						}

					case 2:

						if ("".equals(name.toString())) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"�������ǳƣ�", Toast.LENGTH_SHORT).show();
							break;
						}

					case 3:

						if ("".equals(password.toString())) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"���������룡", Toast.LENGTH_SHORT).show();
							break;
						}

						if (three_PutInPassword.getText().toString().trim()
								.length() < 6
								|| three_PutInPassword.getText().toString()
										.trim().length() > 18) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"����������6λС��18λ", Toast.LENGTH_SHORT).show();
							break;
						}

					case 4:

						if (!(password.equals(three_confirm.getText()
								.toString()))) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"�����������벻һ�£�", Toast.LENGTH_SHORT).show();
							break;
						}

					default:

						boolean net = CheckNet
								.detect(RegisterPersonalPageActivity.this);

						if (net) {

							registerAsyncHttpClientPost(phone, password, name,
									location2, identify, iStream);

						} else {

							Toast.makeText(RegisterPersonalPageActivity.this,
									"���粻���ã��������磡", Toast.LENGTH_SHORT).show();
						}

						break;
					}

				}

			}

			/**
			 * @throws IOException
			 * @throws MalformedURLException
			 ***************************************/

			@SuppressLint("HandlerLeak")
			private void registerAsyncHttpClientPost(final String phone_number,
					final String password, String name, String location,
					String identify, InputStream iStream) {

				final Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						Toast.makeText(getApplicationContext(), "ע��ɹ�",
								Toast.LENGTH_LONG).show();

						Intent intent = new Intent(
								RegisterPersonalPageActivity.this,
								LoginActvity.class);
						RegisterPersonalPageActivity.this.startActivity(intent);

						overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);

						RegisterPersonalPageActivity.this.finish();

						/****************************/
						// �ϴ����ݺ�ɾ��city��school�ļ�
						preferences = getSharedPreferences("city",
								Activity.MODE_PRIVATE);
						cityeditor = preferences.edit();
						cityeditor.clear();
						cityeditor.commit();

						data = getSharedPreferences("school",
								Activity.MODE_PRIVATE);
						shooleditor = data.edit();
						shooleditor.clear();
						shooleditor.commit();

						message = getSharedPreferences("message",
								Activity.MODE_PRIVATE);
						messageditor = message.edit();
						messageditor.clear();
						messageditor.commit();

					}
				};

				// �������󵽷�����
				AsyncHttpClient client = new AsyncHttpClient();
				String url = UrlPath.registApi;
				// �����������
				RequestParams params = new RequestParams();
				params.put("phone_number", phone_number);
				params.put("password", password);
				params.put("name", name);
				params.put("portrait", iStream);// ������ͷ��
				params.put("location", location);
				params.put("identify", identify);

				// ִ��post����
				client.post(url, params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {

						
						handler.sendEmptyMessage(0);

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
					
						Toast.makeText(RegisterPersonalPageActivity.this,
								"�õ绰������ע�������ֱ�ӵ�¼", Toast.LENGTH_LONG);

					}
				});
			}
		});
	}

	TextWatcher Password = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			if (arg0.length() >= 6 && arg0.length() <= 18) {
				if ((arg0.toString()).equals(three_PutInPassword.getText()
						.toString())) {
					password_confirm
							.setImageResource(R.drawable.forget_password_true);

				} else {
					password_confirm
							.setImageResource(R.drawable.forget_password_confirm);

				}
			} else {
				password_confirm
						.setImageResource(R.drawable.forget_password_confirm);
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

	/*****************************************/
	// ����ͷ��
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register_ind_page_three_head_portrait:
			// TODO Auto-generated method stub
			new ActionSheetDialog(RegisterPersonalPageActivity.this).builder()
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
			break;
		default:
			break;
		}

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
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

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
		startActivityForResult(intent, 3);

	}

	/**
	 * ����ü�֮���ͼƬ����
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {

		Bundle extras = picdata.getExtras();

		if (extras != null) {

			Bitmap bitmap = extras.getParcelable("data");

			Bitmap bitmap2 = toRoundBitmap(bitmap);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
			// ��ͼƬ�����ַ�����ʽ�洢����
			byte[] b = stream.toByteArray();

			iStream = new ByteArrayInputStream(b);
			image = false;
			// System.out.println(iStream.toString() + ">>>>>>>>>>>");
			img.setImageBitmap(bitmap2);
		}
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// ���µ������BACK��ͬʱû���ظ�
			RegisterPage registerPage = new RegisterPage();
			registerPage.show(RegisterPersonalPageActivity.this);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}

}