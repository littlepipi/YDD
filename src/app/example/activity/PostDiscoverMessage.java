package app.example.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import net.loonggg.utils.ActionSheetDialog;
import net.loonggg.utils.ActionSheetDialog.OnSheetItemClickListener;
import net.loonggg.utils.ActionSheetDialog.SheetItemColor;
import net.loonggg.utils.CheckNet;
import net.loonggg.utils.MyAlertDialog;

import org.apache.http.Header;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Url.UrlPath;
import app.example.activity.PostLostMessage.GridAdapter;
import app.example.activity.PostLostMessage.GridAdapter.ViewHolder;

import com.example.testpic.Bimp;
import com.example.testpic.FileUtils;
import com.example.testpic.PhotoActivity;
import com.example.testpic.TestPic_discover_Activity;
import com.example.testpic.TestPic_lost_Activity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PostDiscoverMessage extends Activity {
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	public static Bitmap bimap;

	private TextView submitButton_up;
	private TextView discover1, submitButton;// 发布发现信息
	private ImageButton discover_back;// 返回按钮
	private EditText discover_title;// 标题
	private EditText discover_content;// 正文内容

	String discover, title, information, phone_number;

	// 用于退出该页面时，清空内容用到
	SharedPreferences preferences_discover = null;
	SharedPreferences.Editor editor_discover = null;

	SharedPreferences savedata1 = null;
	SharedPreferences.Editor losteditor1 = null;
	SharedPreferences mySharedPreferences;
	InputStream[] iStream = new ByteArrayInputStream[3];// 上传头像用到的输入流

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(
				R.layout.post_discover_message, null);
		setContentView(parentView);
		SysApplication.getInstance().addActivity(this);

		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);

		/****************/
		discover1 = (TextView) findViewById(R.id.discover);
		discover_back = (ImageButton) findViewById(R.id.post_discover_message_back);
		discover_title = (EditText) findViewById(R.id.post_discover_message_title);
		discover_content = (EditText) findViewById(R.id.post_discover_message_content);

		discover_content.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.setEnabled(true);
				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				v.requestFocus();
				return false;
			}
		});
		// 用于退出该页面时，清空SHareprefences内容用到
		preferences_discover = getSharedPreferences("found_locate_EditText",
				Activity.MODE_PRIVATE);
		editor_discover = preferences_discover.edit();

		// 用于退出该页面时，清空SHareprefences保留的EditView 内容用到
		savedata1 = getSharedPreferences("discover", Activity.MODE_PRIVATE);
		losteditor1 = savedata1.edit();

		Init();// 图片
		submit();
		initback();

	}

	/**
	 * 窗口菜单按钮
	 */
	/**
	 * 窗口菜单按钮
	 */
	/******************************************************************************************/
	public void Init() {

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview3);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new ActionSheetDialog(PostDiscoverMessage.this)
							.builder()
//							.setTitle("走进相册^o^")
							.setCancelable(false)
							.setCanceledOnTouchOutside(true)
							.addSheetItem("拍照", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											photo();
										}
									})
							.addSheetItem("从相册中选取", SheetItemColor.Red,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {

											Intent intent = new Intent(
													PostDiscoverMessage.this,
													TestPic_discover_Activity.class);
											startActivity(intent);
											PostDiscoverMessage.this.finish();
											overridePendingTransition(
													R.anim.activity_translate_in,
													R.anim.activity_translate_out);

										}

									}).show();
				} else {
					Intent intent = new Intent(PostDiscoverMessage.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	/******************************************************************/
	/**
	 * 九宫格上传照片设置
	 * 
	 * @author Administrator
	 * 
	 */
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 3) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));

				// 上传图片
				Bitmap bitmap = Bimp.bmp.get(position);

				// CompressImage compressImage = new CompressImage();
				// Bitmap bitmap2 = compressImage.comp(bitmap);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				// 将图片流以字符串形式存储下来
				iStream[position] = new ByteArrayInputStream(
						stream.toByteArray());

//				System.out.println("iStream-------" + iStream);
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

		StringBuffer sDir = new StringBuffer();
		if (hasSDcard()) {
			sDir.append(Environment.getExternalStorageDirectory()
					+ "/MyPicture/");
		} else {
			String dataPath = Environment.getRootDirectory().getPath();
			sDir.append(dataPath + "/MyPicture/");
		}

		File fileDir = new File(sDir.toString());
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		File file = new File(fileDir,
				String.valueOf(System.currentTimeMillis()) + ".jpg");

		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	public static boolean hasSDcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
			}
			break;
		}
	}

	// 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
	class ItemClickListener implements OnItemClickListener {
		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {
			// 在本例中arg2=arg3
			HashMap<String, Object> item = (HashMap<String, Object>) arg0
					.getItemAtPosition(arg2);
			// 显示所选Item的ItemText
			setTitle((String) item.get("ItemText"));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
//		System.out.println("-----------onDestroy------");
		super.onDestroy();
	};

	private void initback() {
		discover_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				back();
			}
		});
	}

	private void back() {

		new MyAlertDialog(PostDiscoverMessage.this).builder().setTitle("提示！")
				.setMsg("确定放弃编辑？")
				.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(View v) {

						// 退出该页面时 ，把已选择的图片清空，借鉴于删除图片
						Bimp.bmp.clear();
						Bimp.drr.clear();
						Bimp.max = 0;
						FileUtils.deleteDir();
						Intent intent = new Intent("data.broadcast.action");
						sendBroadcast(intent);

						// 退出该页面时，清空shareprefences保存的内容
						editor_discover.clear();
						editor_discover.commit();

						// 退出清shareprefences保留的EditView内容
						losteditor1.clear();
						losteditor1.commit();

						discover_title.setText("");
						discover_content.setText("");

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

	/**
	 * 信息传递到服务器
	 */
	private void submitmethod() {

		// 从登录获取令牌token
		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		String token = mySharedPreferencestoken.getString("token", "")
				.toString().trim();
		System.out.println("token: " + token);
		mySharedPreferences = getSharedPreferences("phone_number",
				Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		phone_number = mySharedPreferences.getString("phone_number", "")
				.toString().trim();

		/*****************************************/

		discover = discover1.getText().toString().trim();
		title = discover_title.getText().toString().trim();
		information = discover_content.getText().toString().trim();
//		System.out.println(title + "---" + token + "---" + phone_number + "---"
//				+ information + "---" + iStream);

		submitAsyncHttpClientPost(phone_number, token, title, information,
				iStream);

	}

	// discover_title,discover_content
	public void submit() {
		submitButton = (TextView) findViewById(R.id.post_discover_message_submit);
		submitButton_up = (TextView) findViewById(R.id.postdiscovermessage_submit);
		submitButton_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				boolean network = CheckNet.detect(PostDiscoverMessage.this);

				if (network) {

					notnull();// 判断各个内容是否为空

				} else {
					Toast.makeText(PostDiscoverMessage.this, "无网络连接，请检查网络！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean network = CheckNet.detect(PostDiscoverMessage.this);

				if (network) {

					notnull();// 判断各个内容是否为空

				} else {
					Toast.makeText(PostDiscoverMessage.this, "无网络连接，请检查网络！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	protected void notnull() {

		switch (1) {

		case 1:

			if ("".equals(discover_title.getText().toString())) {
				Toast.makeText(PostDiscoverMessage.this, "标题不能为空！",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 2:

			if ("".equals(discover_content.getText().toString())) {
				Toast.makeText(PostDiscoverMessage.this, "正文内容不能为空！",
						Toast.LENGTH_SHORT).show();
				break;
			}

		default:
			submitmethod();
			break;
		}

	}

	protected void submitAsyncHttpClientPost(String phone_number, String token,
			String title, String information, InputStream[] iStream2) {
		// TODO Auto-generated method stub

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Toast.makeText(getApplicationContext(), "提交成功！",
						Toast.LENGTH_LONG).show();

				Intent intent = new Intent(PostDiscoverMessage.this,
						MainActivity.class);

				PostDiscoverMessage.this.startActivity(intent);

				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);

				PostDiscoverMessage.this.finish();

				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				FileUtils.deleteDir();
				Intent intent3 = new Intent("data.broadcast.action");
				sendBroadcast(intent3);

				// 提交成功后，清空保存的
				// 退出该页面时，清空shareprefences保存的内容
				editor_discover.clear();
				editor_discover.commit();

				// 退出清shareprefences保留的EditView内容
				losteditor1.clear();
				losteditor1.commit();

				discover_title.setText("");
				discover_content.setText("");

				finish();

			}
		};

		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.newsAddApi;
		// 创建请求参数
		RequestParams params = new RequestParams();
		params.put("title", title);
		params.put("token", token);
		params.put("phone_number", phone_number);
		params.put("information", information);

		// 上传照片
		params.put("image1", iStream[0]);
		params.put("image2", iStream[1]);
		params.put("image3", iStream[2]);

		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				// 跳转activity必须放在主线程中
				handler.sendEmptyMessage(0);
				// 返回的是json数据，需要解析
				// String iString=;
				// System.out.println( new String (responseBody));

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// 打印错误信息
//				System.out.println("返回码" + statusCode);
//				System.out.println("返回码" + new String(responseBody));
				error.printStackTrace();
			}
		});
	}

	// 用于保留跳转到其他页面时，EditView内容的保留
	/******************************************************************/
	@Override
	public void onStop() {
		// 暂停：onStart()->onResume()->onPause()
		super.onStop();
		// 保存activity销毁钱的数据

		// System.out.println("》》》》》》》》》》》》开始.onPause");
		// 把数据保存到类似于Session之类的存储集合里面
		// 实例化SharedPreferences对象（第一步）
		SharedPreferences savedata = getSharedPreferences("discover",
				Activity.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		SharedPreferences.Editor losteditor = savedata.edit();

		losteditor.putString("discover", discover);
		losteditor.putString("title", discover_title.getText().toString());
		losteditor.putString("content", discover_content.getText().toString());

		losteditor.commit();

		// System.out.println("》》》》》》》》》》》》" + discover);

	}

	/******************************************************************/
	@Override
	protected void onResume() {
		super.onResume();

		// System.out.println("-----------onResume()------开始onResume");

		// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
		SharedPreferences getdata = getSharedPreferences("discover",
				Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		String discover = getdata.getString("discover", null);
		String title = getdata.getString("title", null);
		String content = getdata.getString("content", null);

//		System.out.println("discover=" + discover);
		// discover,title,content;
		if (title != null) {

			discover_title.setText(title);
		}

		if (content != null) {

			discover_content.setText(content);

		}

		// System.out.println("?????????????" + content + title);

	}

}
