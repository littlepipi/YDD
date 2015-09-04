package app.example.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import net.loonggg.utils.ActionSheetDialog;
import net.loonggg.utils.ActionSheetDialog.OnSheetItemClickListener;
import net.loonggg.utils.ActionSheetDialog.SheetItemColor;
import net.loonggg.utils.CheckNet;
import net.loonggg.utils.MyAlertDialog;
import net.loonggg.utils.MyGridViewAdapter;

import org.apache.http.Header;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Url.UrlPath;
import app.example.widget.OverScrollView;

import com.example.baidumaplocation.BaiDuMain_lost;
import com.example.testpic.Bimp;
import com.example.testpic.FileUtils;
import com.example.testpic.PhotoActivity;

import com.example.testpic.TestPic_lost_Activity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.widget.time.JudgeDate;
import com.widget.time.ScreenInfo;
import com.widget.time.WheelMain;

@SuppressLint({ "SimpleDateFormat", "InflateParams" })
public class PostLostMessage extends Activity {

	private Button mapbutton, timeButton;
	private TextView submitButton_up;
	public static EditText post_lost_place;// 丢失地点的内容
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	public static Bitmap bimap;
	private TextView wordsText;
	WheelMain wheelMain;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private EditText post_lost_name, post_lost_time, post_lost_details,
			lost_telephone;
	@SuppressWarnings("unused")
	private TextView lost, submitButton;
	private OverScrollView myscrollview;
	private ImageButton backbutton;

	String imagePath;
	boolean leixing = true;// 主要用于判断失物类型是否为空，提交数据时出现提醒
	InputStream[] iStream = new ByteArrayInputStream[3];// 上传头像用到的输入流

	// 用于退出该页面时，清空内容用到
	SharedPreferences preferences = null;
	SharedPreferences.Editor editor = null;

	SharedPreferences savedata1 = null;
	SharedPreferences.Editor losteditor1 = null;

	// 用于退出该页面时，清空失物类型的打勾图标用到
	SharedPreferences position_preferences = null;
	SharedPreferences.Editor position_editor = null;

	// 从登陆获取过来的电话号码
	SharedPreferences mySharedPreferences;
	String phone_number;

	// 用于保存选择失物类型的位置
	SharedPreferences positionpreferences, perstiondata;
	SharedPreferences.Editor positioneditor;

	MyGridViewAdapter adapter2;

	String itemText, itemText1;
	String lost_place;
	String lost_jingweiduString;
	String miao = ":00";
	String name, details, place, lostitemText, latLng, title, time, time2,
			contact_phone;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(R.layout.post_lost_message,
				null);
		setContentView(parentView);
		SysApplication.getInstance().addActivity(this);

		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);

		post_lost_name = (EditText) findViewById(R.id.post_lost_message_Name);
		post_lost_place = (EditText) findViewById(R.id.post_lost_message_Place);
		lost = (TextView) findViewById(R.id.lost);// 标题
		lost_telephone = (EditText) findViewById(R.id.post_lost_message_telephone);// 联系电话

		// myscrollview作用是一进页面就显示在最上面
		myscrollview = (OverScrollView) findViewById(R.id.post_lost_message_scrollView);
		myscrollview.smoothScrollTo(0, 20);
		// 从登陆获取电话号码
		// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
		mySharedPreferences = getSharedPreferences("phone_number",
				Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		phone_number = mySharedPreferences.getString("phone_number", "")
				.toString().trim();

		// 可在此处获取联系人电话
		lost_telephone.setText(phone_number);

		/****************/
		// 用于退出该页面时，清空从地图传过来的SHareprefences 内容用到
		preferences = getSharedPreferences("lost_locate_EditText",
				Activity.MODE_PRIVATE);
		editor = preferences.edit();

		// 用于退出该页面时，清空SHareprefences保留的EditView 内容用到
		savedata1 = getSharedPreferences("data", Activity.MODE_PRIVATE);
		losteditor1 = savedata1.edit();

		// 用于退出该页面时，清空SHareprefences保留的失物类型p位置ersion内容用到
		position_preferences = getSharedPreferences("position",
				Activity.MODE_PRIVATE);
		position_editor = position_preferences.edit();

		/****************/

		// 获取地点
		// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
		SharedPreferences sharedPreferences = getSharedPreferences(
				"lost_locate_EditText", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		lost_place = sharedPreferences.getString("lost_place", ""); // 丢失地点
		lost_jingweiduString = sharedPreferences.getString(
				"lost_jingweiduString", ""); // 丢失的经纬度
		post_lost_place.setText(lost_place);// 修改时，经纬度去掉，上传需要经纬度

		/****************/
		mapbutton = (Button) findViewById(R.id.post_lost_message_locate_Button);

		mapbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent mapintentIntent = new Intent(PostLostMessage.this,
						BaiDuMain_lost.class);
				startActivity(mapintentIntent);
				PostLostMessage.this.finish();
			}
		});
		/****************/

		Init();
		Time();
		wordlimit();
		submit();
		initback();
	}

	/**
	 * gridview选择类别设置
	 * 
	 * @return
	 */
	public void gridView() {
		final GridView gridview = (GridView) findViewById(R.id.post_lost_message_gridview);
		// 生成动态数组，并且转入数据(arraylist是动态数组，add方法为给list增加元素)
		String Title[] = { "钱包", "证件", "钥匙", "数码", "饰品", "衣服", "文件", "书籍",
				"宠物", "找人", "车辆", "其他" };
		int ResId[] = { R.drawable.purse_ch, R.drawable.card_ch,
				R.drawable.keys_ch, R.drawable.digital_ch, R.drawable.ring_ch,
				R.drawable.cloth_ch, R.drawable.file_ch, R.drawable.book_ch,
				R.drawable.pat_ch, R.drawable.people_ch, R.drawable.car_ch,
				R.drawable.others_ch };
		final ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 12; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", ResId[i]);// 添加图像资源的ID
			map.put("ItemText", Title[i]);// 按序号做ItemText
			map.put("ItemImageImage", R.drawable.plugin_camera_choosed);
			lstImageItem.add(map);
		}
		adapter2 = new MyGridViewAdapter(this, lstImageItem);

		// 添加并且显示
		gridview.setAdapter(adapter2);

		// 获取失物类型的在gridview中的位置
		perstiondata = getSharedPreferences("position", Activity.MODE_PRIVATE);
		int position = perstiondata.getInt("position", 20);


		if (position==20) {

			System.out.println("leixing1-----" + leixing);
			
		} else {
			adapter2.setSelectPosition(position);
			leixing = false;
			System.out.println("leixing2-----" + leixing);
		}

		// 添加消息处理
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, Object> item = lstImageItem.get(position);
				itemText = item.get("ItemText").toString();
				Toast.makeText(PostLostMessage.this, itemText,
						Toast.LENGTH_SHORT).show();

				adapter2.setSelectPosition(position);
				leixing = false;

				positionpreferences = getSharedPreferences("position",
						Activity.MODE_PRIVATE);
				positioneditor = positionpreferences.edit();

				positioneditor.putInt("position", position);
				positioneditor.commit();

			}
		});

	}

	/**
	 * 时间设置
	 */
	public void Time() {

		post_lost_time = (EditText) findViewById(R.id.post_lost_message_Time);

		timeButton = (Button) findViewById(R.id.post_lost_message_time_Button);
		timeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LayoutInflater inflater = LayoutInflater
						.from(PostLostMessage.this);
				final View timepickerview = inflater.inflate(
						R.layout.timepicker, null);
				ScreenInfo screenInfo = new ScreenInfo(PostLostMessage.this);
				wheelMain = new WheelMain(timepickerview, true);
				wheelMain.screenheight = screenInfo.getHeight();
				String time = post_lost_time.getText().toString();
				Calendar calendar = Calendar.getInstance();
				if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
					try {
						calendar.setTime(dateFormat.parse(time));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				int h = calendar.getTime().getHours();
				int m = calendar.getTime().getMinutes();
				wheelMain.initDateTimePicker(year, month, day, h, m);
				new Builder(PostLostMessage.this)
						.setTitle("选择时间")
						.setIcon(R.drawable.time_choose_orange_gray)
						.setView(timepickerview)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										post_lost_time.setText(wheelMain
												.getTime());
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		});

	}

	/**************************/

	/**
	 * 窗口菜单按钮
	 */
	/******************************************************************************************/
	public void Init() {

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new ActionSheetDialog(PostLostMessage.this)
							.builder()
							// .setTitle("走进相册^o^")
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
													PostLostMessage.this,
													TestPic_lost_Activity.class);
											startActivity(intent);
											PostLostMessage.this.finish();
											overridePendingTransition(
													R.anim.activity_translate_in,
													R.anim.activity_translate_out);

										}

									}).show();
				} else {
					Intent intent = new Intent(PostLostMessage.this,
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

				// System.out.println("iStream-------"+iStream);

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

	/******************************************************************************************/
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

	/******************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/******************************************************************/
	@Override
	protected void onDestroy() {

		// System.out.println("-----------onDestroy()------");
		super.onDestroy();
	};

	/******************************************************************/
	private void initback() {
		backbutton = (ImageButton) findViewById(R.id.post_lost_message_back);
		backbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				back();
			}
		});
	}

	private void back() {

		new MyAlertDialog(PostLostMessage.this).builder().setTitle("提示！")
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

						// 退出時设置locate_EditText为空
						post_lost_place.setText("");// 修改时，经纬度去掉，上传需要经纬度
						post_lost_time.setText("");
						post_lost_details.setText("");
						post_lost_name.setText("");

						// 退出该页面时，清空shareprefences保存的内容
						editor.clear();
						editor.commit();
						// 退出清shareprefences保留的EditView内容
						losteditor1.clear();
						losteditor1.commit();

						// 用于清空失物类型的位置
						position_editor.clear();
						position_editor.commit();

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

	private void wordlimit() {
		// TODO Auto-generated method stub
		post_lost_details = (EditText) findViewById(R.id.post_lost_message_BriefIntroduction);
		wordsText = (TextView) findViewById(R.id.post_lost_message_Num);

		post_lost_details.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.setEnabled(true);
				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				v.requestFocus();
				return false;
			}
		});
		post_lost_details.addTextChangedListener(new TextWatcher() {

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

	/**
	 * 信息传递到服务器
	 */
	private void submitmethod() {

		// TODO Auto-generated method stub
		/*****************************************/

		// 从登录获取令牌token
		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		String token = mySharedPreferencestoken.getString("token", "")
				.toString().trim();

		/*****************************************/

		// System.out.println("进入提交");

		name = post_lost_name.getText().toString().trim();
		String time1 = post_lost_time.getText().toString().trim();
		details = post_lost_details.getText().toString().trim();
		place = post_lost_place.getText().toString().trim();
		lostitemText = itemText.toString().trim();
		latLng = lost_jingweiduString.toString().trim();
		contact_phone = lost_telephone.getText().toString().trim();// 联系电话
		//
		title = "lost";

		time2 = time1.replace(" ", "T");
		time = time2 + miao;

		// System.out.println(token + "+" + title + "+" + phone_number + "+"
		// + name + " + " + time + " + " + details + " + " + place + " + "
		// + lostitemText + " + " + latLng);

		submitAsyncHttpClientPost(token, phone_number, name, time, details,
				place, lostitemText, latLng, title, iStream, contact_phone);

	}

	public void submit() {

		submitButton = (TextView) findViewById(R.id.post_lost_message_submit);
		submitButton_up = (TextView) findViewById(R.id.postlostmessage_submit);
		submitButton_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean network = CheckNet.detect(PostLostMessage.this);

				if (network) {

					notnull();// 判断各个内容是否为空

				} else {
					Toast.makeText(PostLostMessage.this, "无网络连接，请检查网络！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean network = CheckNet.detect(PostLostMessage.this);

				if (network) {

					notnull();// 判断各个内容是否为空

				} else {
					Toast.makeText(PostLostMessage.this, "无网络连接，请检查网络！",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	protected void notnull() {

		switch (1) {
		case 1:

			if ("".equals(post_lost_name.getText().toString())) {
				Toast.makeText(PostLostMessage.this, "标题不能为空！",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 2:

			if ("".equals(post_lost_time.getText().toString())) {
				Toast.makeText(PostLostMessage.this, "丢失时间不能为空！",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 3:

			if ("".equals(post_lost_place.getText().toString())) {
				Toast.makeText(PostLostMessage.this, "丢失地点不能为空！",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 4:

			if ("".equals(lost_telephone.getText().toString())) {
				Toast.makeText(PostLostMessage.this, "联系电话不能为空！",
						Toast.LENGTH_SHORT).show();
				break;
			}

			if (lost_telephone.getText().toString().length() != 11) {
				Toast.makeText(PostLostMessage.this, "联系电话格式不对！",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 5:

			if (leixing) {
				Toast.makeText(PostLostMessage.this, "失物类型不能为空！",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 6:
			if ("".equals(post_lost_details.getText().toString())) {
				Toast.makeText(PostLostMessage.this, "失物简介不能为空！",
						Toast.LENGTH_SHORT).show();
				break;
			}

		default:

			boolean net = CheckNet.detect(PostLostMessage.this);

			if (net) {

				submitmethod();

			} else {

				Toast.makeText(PostLostMessage.this, "网络不可用，请检查网络！",
						Toast.LENGTH_SHORT).show();
			}

			break;
		}

	}

	protected void submitAsyncHttpClientPost(String token, String phone_number,
			String name, String time, String details, String place,
			String itemText, String latLng, String title,
			InputStream[] iStream, String contact_phone) {
		// TODO Auto-generated method stub

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Toast.makeText(getApplicationContext(), "提交成功！",
						Toast.LENGTH_LONG).show();

				Intent intent = new Intent(PostLostMessage.this,
						MainActivity.class);

				PostLostMessage.this.startActivity(intent);

				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);

				PostLostMessage.this.finish();

				// 退出该页面时 ，把已选择的图片清空，借鉴于删除图片
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				FileUtils.deleteDir();
				Intent intent1 = new Intent("data.broadcast.action");
				sendBroadcast(intent1);

				// 退出時设置locate_EditText为空
				post_lost_place.setText("");// 修改时，经纬度去掉，上传需要经纬度
				post_lost_time.setText("");
				post_lost_details.setText("");
				post_lost_name.setText("");
				// 退出该页面时，清空shareprefences保存的内容
				editor.clear();
				editor.commit();
				// 退出清shareprefences保留的EditView内容
				losteditor1.clear();
				losteditor1.commit();

				position_editor.clear();
				position_editor.commit();

				finish();

			}
		};

		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.messageAddApi;

		// System.out.println("提交的数据------" + "title--:" + title + "" +
		// "token--:"
		// + token + "name--:" + name + "phone_number---:" + phone_number
		// + "latLng---:" + latLng + "time---:" + time + "place:---"
		// + place + "itemText--:" + itemText + "contact_phone---:"
		// + contact_phone);

		// 创建请求参数
		RequestParams params = new RequestParams();
		params.put("what", title);
		params.put("token", token);
		params.put("title", name);
		params.put("phone_number", phone_number);
		params.put("latlng", latLng);
		params.put("time", time);
		params.put("information", details);
		params.put("location", place);// 失物地点
		params.put("kind", itemText);
		params.put("contact_phone", contact_phone);// 联系电话

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
				/**************************************/

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// 打印错误信息
				error.printStackTrace();

				// Log.i("提交状态", "" + "提交失败");
				// String i = new String(responseBody);
				// Log.i("statusCode", "" + statusCode);
				// Log.i("responseBody", "" + i);

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

		System.out.println("》》》》》》》》》》》》开始.onPause");
		// 把数据保存到类似于Session之类的存储集合里面
		// 实例化SharedPreferences对象（第一步）
		SharedPreferences savedata = getSharedPreferences("data",
				Activity.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		SharedPreferences.Editor losteditor = savedata.edit();

		losteditor.putString("itemText", itemText);
		losteditor.putString("lostname", post_lost_name.getText().toString());
		losteditor.putString("time1", post_lost_time.getText().toString());
		losteditor.putString("lostdetails", post_lost_details.getText()
				.toString());
		losteditor.putString("lost_telephone", lost_telephone.getText()
				.toString());
		// losteditor.putString("telephone", telephone);
		// losteditor.putString("lostplace",
		// post_lost_place.getText().toString());

		losteditor.commit();

	}

	/******************************************************************/
	@Override
	protected void onResume() {
		super.onResume();
		gridView();

		// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
		SharedPreferences getdata = getSharedPreferences("data",
				Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		String lostname = getdata.getString("lostname", null);
		String time1 = getdata.getString("time1", null);
		String lostdetails = getdata.getString("lostdetails", null);
		// String lostplace = getdata.getString("lostplace", null);
		itemText1 = getdata.getString("itemText", null);
		String telephone1 = getdata.getString("lost_telephone", null);

		if (lostname != null) {

			post_lost_name.setText(lostname);

		}

		if (itemText1 != null) {

			itemText = itemText1;
		}

		if (time1 != null) {

			post_lost_time.setText(time1);

		}

		if (lostdetails != null) {

			post_lost_details.setText(lostdetails);

		}

		if (telephone1 != null) {

			lost_telephone.setText(telephone1);

		} else {

			// 可在此处获取联系人电话
			lost_telephone.setText(phone_number);

		}

		// System.out.println(itemText + "联系人" + telephone1 + "账号" +
		// phone_number);

	};

}
