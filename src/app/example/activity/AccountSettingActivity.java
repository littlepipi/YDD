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
	InputStream iStream;// 上传头像用到的输入流
	String location;
	private String personname, personlocation, personportrait;

	String name;
	// 从登陆获取过来的电话号码

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
		if (personlocation.indexOf("学") != -1) {
			KIND = "school";
		} else {
			KIND = "society";
		}
		initView();

	}

	/********************************************************************************/
	// 初始化控件
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

		// 获取电话号码
		// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
		SharedPreferences mySharedPreferences = getSharedPreferences(
				"phone_number", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		String phone_number = mySharedPreferences.getString("phone_number", "")
				.toString().trim();

		telephone.setText(phone_number.toString());

		portraitUseless.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new ActionSheetDialog(AccountSettingActivity.this).builder()
						.setTitle("步入相册^o^").setCancelable(false)
						.setCanceledOnTouchOutside(true)
						// .addSheetItem("拍照", SheetItemColor.Blue,
						// new OnSheetItemClickListener() {
						// @Override
						// public void onClick(int which) {
						//
						// Intent intent = new Intent(
						// MediaStore.ACTION_IMAGE_CAPTURE);
						// // 下面这句指定调用相机拍照后的照片存储的路径
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
						.addSheetItem("从相册中选取", SheetItemColor.Red,
								new OnSheetItemClickListener() {
									@Override
									public void onClick(int which) {

										/**
										 * 刚开始，我自己也不知道ACTION_PICK是干嘛的，
										 * 后来直接看Intent源码，
										 * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
										 */
										Intent intent = new Intent(
												Intent.ACTION_PICK, null);

										/**
										 * 下面这句话，与其它方式写是一样的效果，如果：
										 * intent.setData(MediaStore.Images
										 * .Media.EXTERNAL_CONTENT_URI);
										 * intent.setType(""image/*");设置数据类型
										 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如
										 * ："image/jpeg 、 image/png等的类型"
										 * 这个地方小马有个疑问，希望高手解答下：
										 * 就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
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
				 * 设置标志，城市与学校的区分，具体的操作，城市为1，学校为0，
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
						.setTitle("注销后将退出登录")
						.setCancelable(false)
						.setCanceledOnTouchOutside(true)
						.addSheetItem("注销", SheetItemColor.Red,
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
		// 如果是直接从相册获取
		case 1:
			if (data != null) {
				startPhotoZoom(data.getData());
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/icon_guest_count_small");

			if (data != null) {

				startPhotoZoom(Uri.fromFile(temp));
			}
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
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

	// 裁剪图片并保存为图片流

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);

		intent.putExtra("flag", true);
		startActivityForResult(intent, 3);

	}

	/**
	 * 保存裁剪之后的图片数据
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
			// 将图片流以字符串形式存储下来
			byte[] b = stream.toByteArray();

			iStream = new ByteArrayInputStream(b);

			portrait.setImageBitmap(bitmap2);

			// 从登陆获取电话号码
			// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
			SharedPreferences mySharedPreferencesphone = getSharedPreferences(
					"phone_number", Activity.MODE_PRIVATE);
			// 使用getString方法获得value，注意第2个参数是value的默认值
			phone_number = mySharedPreferencesphone
					.getString("phone_number", "").toString().trim();
			SharedPreferences mysSharedPreferencespassword = getSharedPreferences(
					"phone_password", MODE_PRIVATE);

			password = mysSharedPreferencespassword.getString("password", "")
					.toString().trim();

			// 从登录获取令牌token
			SharedPreferences mySharedPreferencestoken = getSharedPreferences(
					"token", Activity.MODE_PRIVATE);
			// 使用getString方法获得value，注意第2个参数是value的默认值
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

					Toast.makeText(getApplicationContext(), "修改成功",
							Toast.LENGTH_LONG).show();

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
		params.put("operation", "change_portrait");
		params.put("password", password);
		params.put("token", token);
		params.put("portrait", iStream);
		params.put("phone_number", phone_number);

		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				// System.out.println("成功");// 打印
				// // String i = new String(responseBody);
				// System.out.print("statusCode" + statusCode);

				handler.sendEmptyMessage(0);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// System.out.print("修改失败");
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

		paint.setAntiAlias(true);// 设置画笔无锯齿

		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

		// 以下有两种方法画圆,drawRounRect和drawCircle
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
		// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

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
