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

	private EditText three_nickname;// 昵称
	private EditText three_PutInPassword;// 密码
	private EditText three_confirm;// 确认密码
	private Button button;// 完成注册
	private ImageView password_confirm;// 密码和默认密码相同时打钩

	private ImageView img;// 用户头像
	String portrait;
	InputStream iStream = null;// 上传头像用到的输入流
	boolean image = true;// 主要用于判断头像是否为空，提交数据时出现提醒

	// 用于退出该页面时，清空内容用到
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

		// 是为了使底部弹出相册，拍照
		parentView = getLayoutInflater().inflate(
				R.layout.register_ind_page_three, null);
		setContentView(parentView);
		// 控件初始化
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		/*****************************************/
		three_nickname = (EditText) findViewById(R.id.register_ind_page_three_nickname);
		three_PutInPassword = (EditText) findViewById(R.id.register_ind_page_three_PutInPassword);
		three_confirm = (EditText) findViewById(R.id.register_ind_page_three_confirm);
		three_confirm.addTextChangedListener(Password);
		password_confirm = (ImageView) findViewById(R.id.forget_password_confirm);// 打钩
		register_ind_page_three_back = (ImageButton) findViewById(R.id.register_ind_page_three_back);
		img = (ImageView) findViewById(R.id.register_ind_page_three_head_portrait); // 头像
		img.setOnClickListener(this);

		button = (Button) findViewById(R.id.register_ind_page_three_finish);// 注册

		/*****************************************/
		register_ind_page_three_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RegisterPage registerPage = new RegisterPage();
				registerPage.show(MyApplication.getAppContext());
				finish();

			}
		});
		// 点击完成注册
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// 获取从IdentifyNumPage传过来的电话号码
				SharedPreferences mySharedPreferences4 = getSharedPreferences(
						"messgae", Activity.MODE_PRIVATE);
				// 使用getString方法获得value，注意第2个参数是value的默认值
				String phone = mySharedPreferences4
						.getString("phone_number", "").toString().trim();

				String identify = mySharedPreferences4
						.getString("verificationCode", "").toString().trim();

				String password = three_PutInPassword.getText().toString()
						.trim();// 密码
				String name = three_nickname.getText().toString().trim();// 昵称

				// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"school", Activity.MODE_PRIVATE);
				// 使用getString方法获得value，注意第2个参数是value的默认值
				String location1 = mySharedPreferences.getString("school", "")
						.toString().trim();

				// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
				SharedPreferences mySharedPreferences2 = getSharedPreferences(
						"city", Activity.MODE_PRIVATE);
				// 使用getString方法获得value，注意第2个参数是value的默认值
				String location2 = mySharedPreferences2.getString("city", "")
						.toString().trim();

				// Log.i("RegisterPersonalPageActivity", phone + password + name
				// + location2 + identify + iStream);

				if (location1 != "") {


					switch (1) {

					case 1:

						if (image) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"请设置头像！", Toast.LENGTH_SHORT).show();
							break;
						}

					case 2:

						if ("".equals(name.toString())) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"请设置昵称！", Toast.LENGTH_SHORT).show();
							break;
						}

					case 3:

						if ("".equals(password.toString())) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"请设置密码！", Toast.LENGTH_SHORT).show();
							break;
						}

						if (three_PutInPassword.getText().toString().trim()
								.length() < 6
								|| three_PutInPassword.getText().toString()
										.trim().length() > 18) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"密码必须大于6位小于18位", Toast.LENGTH_SHORT).show();
							break;
						}

					case 4:

						if (!(password.equals(three_confirm.getText()
								.toString()))) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"两次密码输入不一致！", Toast.LENGTH_SHORT).show();
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
									"网络不可用，请检查网络！", Toast.LENGTH_SHORT).show();
						}

						break;
					}

				} else {
					switch (1) {

					case 1:

						if (image) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"请设置头像！", Toast.LENGTH_SHORT).show();
							break;
						}

					case 2:

						if ("".equals(name.toString())) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"请设置昵称！", Toast.LENGTH_SHORT).show();
							break;
						}

					case 3:

						if ("".equals(password.toString())) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"请设置密码！", Toast.LENGTH_SHORT).show();
							break;
						}

						if (three_PutInPassword.getText().toString().trim()
								.length() < 6
								|| three_PutInPassword.getText().toString()
										.trim().length() > 18) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"密码必须大于6位小于18位", Toast.LENGTH_SHORT).show();
							break;
						}

					case 4:

						if (!(password.equals(three_confirm.getText()
								.toString()))) {
							Toast.makeText(RegisterPersonalPageActivity.this,
									"两次密码输入不一致！", Toast.LENGTH_SHORT).show();
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
									"网络不可用，请检查网络！", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(getApplicationContext(), "注册成功",
								Toast.LENGTH_LONG).show();

						Intent intent = new Intent(
								RegisterPersonalPageActivity.this,
								LoginActvity.class);
						RegisterPersonalPageActivity.this.startActivity(intent);

						overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);

						RegisterPersonalPageActivity.this.finish();

						/****************************/
						// 上传数据后删除city和school文件
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

				// 发送请求到服务器
				AsyncHttpClient client = new AsyncHttpClient();
				String url = UrlPath.registApi;
				// 创建请求参数
				RequestParams params = new RequestParams();
				params.put("phone_number", phone_number);
				params.put("password", password);
				params.put("name", name);
				params.put("portrait", iStream);// 传流，头像
				params.put("location", location);
				params.put("identify", identify);

				// 执行post方法
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
								"该电话号码已注册过，请直接登录", Toast.LENGTH_LONG);

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
	// 设置头像
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register_ind_page_three_head_portrait:
			// TODO Auto-generated method stub
			new ActionSheetDialog(RegisterPersonalPageActivity.this).builder()
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
			break;
		default:
			break;
		}

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
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

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
		startActivityForResult(intent, 3);

	}

	/**
	 * 保存裁剪之后的图片数据
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
			// 将图片流以字符串形式存储下来
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

		paint.setAntiAlias(true);// 设置画笔无锯齿

		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

		// 以下有两种方法画圆,drawRounRect和drawCircle
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
		// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

		return output;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 按下的如果是BACK，同时没有重复
			RegisterPage registerPage = new RegisterPage();
			registerPage.show(RegisterPersonalPageActivity.this);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}

}