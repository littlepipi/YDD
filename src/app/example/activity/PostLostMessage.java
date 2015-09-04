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
	public static EditText post_lost_place;// ��ʧ�ص������
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
	boolean leixing = true;// ��Ҫ�����ж�ʧ�������Ƿ�Ϊ�գ��ύ����ʱ��������
	InputStream[] iStream = new ByteArrayInputStream[3];// �ϴ�ͷ���õ���������

	// �����˳���ҳ��ʱ����������õ�
	SharedPreferences preferences = null;
	SharedPreferences.Editor editor = null;

	SharedPreferences savedata1 = null;
	SharedPreferences.Editor losteditor1 = null;

	// �����˳���ҳ��ʱ�����ʧ�����͵Ĵ�ͼ���õ�
	SharedPreferences position_preferences = null;
	SharedPreferences.Editor position_editor = null;

	// �ӵ�½��ȡ�����ĵ绰����
	SharedPreferences mySharedPreferences;
	String phone_number;

	// ���ڱ���ѡ��ʧ�����͵�λ��
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
		lost = (TextView) findViewById(R.id.lost);// ����
		lost_telephone = (EditText) findViewById(R.id.post_lost_message_telephone);// ��ϵ�绰

		// myscrollview������һ��ҳ�����ʾ��������
		myscrollview = (OverScrollView) findViewById(R.id.post_lost_message_scrollView);
		myscrollview.smoothScrollTo(0, 20);
		// �ӵ�½��ȡ�绰����
		// ͬ�����ڶ�ȡSharedPreferences����ǰҪʵ������һ��SharedPreferences����
		mySharedPreferences = getSharedPreferences("phone_number",
				Activity.MODE_PRIVATE);
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
		phone_number = mySharedPreferences.getString("phone_number", "")
				.toString().trim();

		// ���ڴ˴���ȡ��ϵ�˵绰
		lost_telephone.setText(phone_number);

		/****************/
		// �����˳���ҳ��ʱ����մӵ�ͼ��������SHareprefences �����õ�
		preferences = getSharedPreferences("lost_locate_EditText",
				Activity.MODE_PRIVATE);
		editor = preferences.edit();

		// �����˳���ҳ��ʱ�����SHareprefences������EditView �����õ�
		savedata1 = getSharedPreferences("data", Activity.MODE_PRIVATE);
		losteditor1 = savedata1.edit();

		// �����˳���ҳ��ʱ�����SHareprefences������ʧ������pλ��ersion�����õ�
		position_preferences = getSharedPreferences("position",
				Activity.MODE_PRIVATE);
		position_editor = position_preferences.edit();

		/****************/

		// ��ȡ�ص�
		// ͬ�����ڶ�ȡSharedPreferences����ǰҪʵ������һ��SharedPreferences����
		SharedPreferences sharedPreferences = getSharedPreferences(
				"lost_locate_EditText", Activity.MODE_PRIVATE);
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
		lost_place = sharedPreferences.getString("lost_place", ""); // ��ʧ�ص�
		lost_jingweiduString = sharedPreferences.getString(
				"lost_jingweiduString", ""); // ��ʧ�ľ�γ��
		post_lost_place.setText(lost_place);// �޸�ʱ����γ��ȥ�����ϴ���Ҫ��γ��

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
	 * gridviewѡ���������
	 * 
	 * @return
	 */
	public void gridView() {
		final GridView gridview = (GridView) findViewById(R.id.post_lost_message_gridview);
		// ���ɶ�̬���飬����ת������(arraylist�Ƕ�̬���飬add����Ϊ��list����Ԫ��)
		String Title[] = { "Ǯ��", "֤��", "Կ��", "����", "��Ʒ", "�·�", "�ļ�", "�鼮",
				"����", "����", "����", "����" };
		int ResId[] = { R.drawable.purse_ch, R.drawable.card_ch,
				R.drawable.keys_ch, R.drawable.digital_ch, R.drawable.ring_ch,
				R.drawable.cloth_ch, R.drawable.file_ch, R.drawable.book_ch,
				R.drawable.pat_ch, R.drawable.people_ch, R.drawable.car_ch,
				R.drawable.others_ch };
		final ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 12; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", ResId[i]);// ���ͼ����Դ��ID
			map.put("ItemText", Title[i]);// �������ItemText
			map.put("ItemImageImage", R.drawable.plugin_camera_choosed);
			lstImageItem.add(map);
		}
		adapter2 = new MyGridViewAdapter(this, lstImageItem);

		// ��Ӳ�����ʾ
		gridview.setAdapter(adapter2);

		// ��ȡʧ�����͵���gridview�е�λ��
		perstiondata = getSharedPreferences("position", Activity.MODE_PRIVATE);
		int position = perstiondata.getInt("position", 20);


		if (position==20) {

			System.out.println("leixing1-----" + leixing);
			
		} else {
			adapter2.setSelectPosition(position);
			leixing = false;
			System.out.println("leixing2-----" + leixing);
		}

		// �����Ϣ����
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
	 * ʱ������
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
						.setTitle("ѡ��ʱ��")
						.setIcon(R.drawable.time_choose_orange_gray)
						.setView(timepickerview)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										post_lost_time.setText(wheelMain
												.getTime());
									}
								})
						.setNegativeButton("ȡ��",
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
	 * ���ڲ˵���ť
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
							// .setTitle("�߽����^o^")
							.setCancelable(false)
							.setCanceledOnTouchOutside(true)
							.addSheetItem("����", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											photo();
										}
									})
							.addSheetItem("�������ѡȡ", SheetItemColor.Red,
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
	 * �Ź����ϴ���Ƭ����
	 * 
	 * @author Administrator
	 * 
	 */
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // ��ͼ����
		private int selectedPosition = -1;// ѡ�е�λ��
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
		 * ListView Item����
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

				// �ϴ�ͼƬ
				Bitmap bitmap = Bimp.bmp.get(position);

				// CompressImage compressImage = new CompressImage();
				// Bitmap bitmap2 = compressImage.comp(bitmap);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				// ��ͼƬ�����ַ�����ʽ�洢����
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
	// ��AdapterView������(���������߼���)���򷵻ص�Item�����¼�
	class ItemClickListener implements OnItemClickListener {
		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {
			// �ڱ�����arg2=arg3
			HashMap<String, Object> item = (HashMap<String, Object>) arg0
					.getItemAtPosition(arg2);
			// ��ʾ��ѡItem��ItemText
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

		new MyAlertDialog(PostLostMessage.this).builder().setTitle("��ʾ��")
				.setMsg("ȷ�������༭��")
				.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(View v) {

						// �˳���ҳ��ʱ ������ѡ���ͼƬ��գ������ɾ��ͼƬ
						Bimp.bmp.clear();
						Bimp.drr.clear();
						Bimp.max = 0;
						FileUtils.deleteDir();

						Intent intent = new Intent("data.broadcast.action");
						sendBroadcast(intent);

						// �˳��r����locate_EditTextΪ��
						post_lost_place.setText("");// �޸�ʱ����γ��ȥ�����ϴ���Ҫ��γ��
						post_lost_time.setText("");
						post_lost_details.setText("");
						post_lost_name.setText("");

						// �˳���ҳ��ʱ�����shareprefences���������
						editor.clear();
						editor.commit();
						// �˳���shareprefences������EditView����
						losteditor1.clear();
						losteditor1.commit();

						// �������ʧ�����͵�λ��
						position_editor.clear();
						position_editor.commit();

						finish();
					}
				}).setNegativeButton("ȡ��", new OnClickListener() {
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
	 * ��Ϣ���ݵ�������
	 */
	private void submitmethod() {

		// TODO Auto-generated method stub
		/*****************************************/

		// �ӵ�¼��ȡ����token
		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
		String token = mySharedPreferencestoken.getString("token", "")
				.toString().trim();

		/*****************************************/

		// System.out.println("�����ύ");

		name = post_lost_name.getText().toString().trim();
		String time1 = post_lost_time.getText().toString().trim();
		details = post_lost_details.getText().toString().trim();
		place = post_lost_place.getText().toString().trim();
		lostitemText = itemText.toString().trim();
		latLng = lost_jingweiduString.toString().trim();
		contact_phone = lost_telephone.getText().toString().trim();// ��ϵ�绰
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

					notnull();// �жϸ��������Ƿ�Ϊ��

				} else {
					Toast.makeText(PostLostMessage.this, "���������ӣ��������磡",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean network = CheckNet.detect(PostLostMessage.this);

				if (network) {

					notnull();// �жϸ��������Ƿ�Ϊ��

				} else {
					Toast.makeText(PostLostMessage.this, "���������ӣ��������磡",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	protected void notnull() {

		switch (1) {
		case 1:

			if ("".equals(post_lost_name.getText().toString())) {
				Toast.makeText(PostLostMessage.this, "���ⲻ��Ϊ�գ�",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 2:

			if ("".equals(post_lost_time.getText().toString())) {
				Toast.makeText(PostLostMessage.this, "��ʧʱ�䲻��Ϊ�գ�",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 3:

			if ("".equals(post_lost_place.getText().toString())) {
				Toast.makeText(PostLostMessage.this, "��ʧ�ص㲻��Ϊ�գ�",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 4:

			if ("".equals(lost_telephone.getText().toString())) {
				Toast.makeText(PostLostMessage.this, "��ϵ�绰����Ϊ�գ�",
						Toast.LENGTH_SHORT).show();
				break;
			}

			if (lost_telephone.getText().toString().length() != 11) {
				Toast.makeText(PostLostMessage.this, "��ϵ�绰��ʽ���ԣ�",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 5:

			if (leixing) {
				Toast.makeText(PostLostMessage.this, "ʧ�����Ͳ���Ϊ�գ�",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 6:
			if ("".equals(post_lost_details.getText().toString())) {
				Toast.makeText(PostLostMessage.this, "ʧ���鲻��Ϊ�գ�",
						Toast.LENGTH_SHORT).show();
				break;
			}

		default:

			boolean net = CheckNet.detect(PostLostMessage.this);

			if (net) {

				submitmethod();

			} else {

				Toast.makeText(PostLostMessage.this, "���粻���ã��������磡",
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
				Toast.makeText(getApplicationContext(), "�ύ�ɹ���",
						Toast.LENGTH_LONG).show();

				Intent intent = new Intent(PostLostMessage.this,
						MainActivity.class);

				PostLostMessage.this.startActivity(intent);

				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);

				PostLostMessage.this.finish();

				// �˳���ҳ��ʱ ������ѡ���ͼƬ��գ������ɾ��ͼƬ
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				FileUtils.deleteDir();
				Intent intent1 = new Intent("data.broadcast.action");
				sendBroadcast(intent1);

				// �˳��r����locate_EditTextΪ��
				post_lost_place.setText("");// �޸�ʱ����γ��ȥ�����ϴ���Ҫ��γ��
				post_lost_time.setText("");
				post_lost_details.setText("");
				post_lost_name.setText("");
				// �˳���ҳ��ʱ�����shareprefences���������
				editor.clear();
				editor.commit();
				// �˳���shareprefences������EditView����
				losteditor1.clear();
				losteditor1.commit();

				position_editor.clear();
				position_editor.commit();

				finish();

			}
		};

		// �������󵽷�����
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.messageAddApi;

		// System.out.println("�ύ������------" + "title--:" + title + "" +
		// "token--:"
		// + token + "name--:" + name + "phone_number---:" + phone_number
		// + "latLng---:" + latLng + "time---:" + time + "place:---"
		// + place + "itemText--:" + itemText + "contact_phone---:"
		// + contact_phone);

		// �����������
		RequestParams params = new RequestParams();
		params.put("what", title);
		params.put("token", token);
		params.put("title", name);
		params.put("phone_number", phone_number);
		params.put("latlng", latLng);
		params.put("time", time);
		params.put("information", details);
		params.put("location", place);// ʧ��ص�
		params.put("kind", itemText);
		params.put("contact_phone", contact_phone);// ��ϵ�绰

		params.put("image1", iStream[0]);
		params.put("image2", iStream[1]);
		params.put("image3", iStream[2]);

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
				// ��ӡ������Ϣ
				error.printStackTrace();

				// Log.i("�ύ״̬", "" + "�ύʧ��");
				// String i = new String(responseBody);
				// Log.i("statusCode", "" + statusCode);
				// Log.i("responseBody", "" + i);

			}
		});
	}

	// ���ڱ�����ת������ҳ��ʱ��EditView���ݵı���
	/******************************************************************/
	@Override
	public void onStop() {
		// ��ͣ��onStart()->onResume()->onPause()
		super.onStop();
		// ����activity����Ǯ������

		System.out.println("��������������������������ʼ.onPause");
		// �����ݱ��浽������Session֮��Ĵ洢��������
		// ʵ����SharedPreferences���󣨵�һ����
		SharedPreferences savedata = getSharedPreferences("data",
				Activity.MODE_PRIVATE);
		// ʵ����SharedPreferences.Editor���󣨵ڶ�����
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

		// ͬ�����ڶ�ȡSharedPreferences����ǰҪʵ������һ��SharedPreferences����
		SharedPreferences getdata = getSharedPreferences("data",
				Activity.MODE_PRIVATE);
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
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

			// ���ڴ˴���ȡ��ϵ�˵绰
			lost_telephone.setText(phone_number);

		}

		// System.out.println(itemText + "��ϵ��" + telephone1 + "�˺�" +
		// phone_number);

	};

}
