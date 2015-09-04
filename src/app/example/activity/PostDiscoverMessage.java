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
	private TextView discover1, submitButton;// ����������Ϣ
	private ImageButton discover_back;// ���ذ�ť
	private EditText discover_title;// ����
	private EditText discover_content;// ��������

	String discover, title, information, phone_number;

	// �����˳���ҳ��ʱ����������õ�
	SharedPreferences preferences_discover = null;
	SharedPreferences.Editor editor_discover = null;

	SharedPreferences savedata1 = null;
	SharedPreferences.Editor losteditor1 = null;
	SharedPreferences mySharedPreferences;
	InputStream[] iStream = new ByteArrayInputStream[3];// �ϴ�ͷ���õ���������

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
		// �����˳���ҳ��ʱ�����SHareprefences�����õ�
		preferences_discover = getSharedPreferences("found_locate_EditText",
				Activity.MODE_PRIVATE);
		editor_discover = preferences_discover.edit();

		// �����˳���ҳ��ʱ�����SHareprefences������EditView �����õ�
		savedata1 = getSharedPreferences("discover", Activity.MODE_PRIVATE);
		losteditor1 = savedata1.edit();

		Init();// ͼƬ
		submit();
		initback();

	}

	/**
	 * ���ڲ˵���ť
	 */
	/**
	 * ���ڲ˵���ť
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
//							.setTitle("�߽����^o^")
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

		new MyAlertDialog(PostDiscoverMessage.this).builder().setTitle("��ʾ��")
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

						// �˳���ҳ��ʱ�����shareprefences���������
						editor_discover.clear();
						editor_discover.commit();

						// �˳���shareprefences������EditView����
						losteditor1.clear();
						losteditor1.commit();

						discover_title.setText("");
						discover_content.setText("");

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

	/**
	 * ��Ϣ���ݵ�������
	 */
	private void submitmethod() {

		// �ӵ�¼��ȡ����token
		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
		String token = mySharedPreferencestoken.getString("token", "")
				.toString().trim();
		System.out.println("token: " + token);
		mySharedPreferences = getSharedPreferences("phone_number",
				Activity.MODE_PRIVATE);
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
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

					notnull();// �жϸ��������Ƿ�Ϊ��

				} else {
					Toast.makeText(PostDiscoverMessage.this, "���������ӣ��������磡",
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

					notnull();// �жϸ��������Ƿ�Ϊ��

				} else {
					Toast.makeText(PostDiscoverMessage.this, "���������ӣ��������磡",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	protected void notnull() {

		switch (1) {

		case 1:

			if ("".equals(discover_title.getText().toString())) {
				Toast.makeText(PostDiscoverMessage.this, "���ⲻ��Ϊ�գ�",
						Toast.LENGTH_SHORT).show();
				break;
			}

		case 2:

			if ("".equals(discover_content.getText().toString())) {
				Toast.makeText(PostDiscoverMessage.this, "�������ݲ���Ϊ�գ�",
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
				Toast.makeText(getApplicationContext(), "�ύ�ɹ���",
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

				// �ύ�ɹ�����ձ����
				// �˳���ҳ��ʱ�����shareprefences���������
				editor_discover.clear();
				editor_discover.commit();

				// �˳���shareprefences������EditView����
				losteditor1.clear();
				losteditor1.commit();

				discover_title.setText("");
				discover_content.setText("");

				finish();

			}
		};

		// �������󵽷�����
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.newsAddApi;
		// �����������
		RequestParams params = new RequestParams();
		params.put("title", title);
		params.put("token", token);
		params.put("phone_number", phone_number);
		params.put("information", information);

		// �ϴ���Ƭ
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
				// ���ص���json���ݣ���Ҫ����
				// String iString=;
				// System.out.println( new String (responseBody));

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// ��ӡ������Ϣ
//				System.out.println("������" + statusCode);
//				System.out.println("������" + new String(responseBody));
				error.printStackTrace();
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

		// System.out.println("��������������������������ʼ.onPause");
		// �����ݱ��浽������Session֮��Ĵ洢��������
		// ʵ����SharedPreferences���󣨵�һ����
		SharedPreferences savedata = getSharedPreferences("discover",
				Activity.MODE_PRIVATE);
		// ʵ����SharedPreferences.Editor���󣨵ڶ�����
		SharedPreferences.Editor losteditor = savedata.edit();

		losteditor.putString("discover", discover);
		losteditor.putString("title", discover_title.getText().toString());
		losteditor.putString("content", discover_content.getText().toString());

		losteditor.commit();

		// System.out.println("������������������������" + discover);

	}

	/******************************************************************/
	@Override
	protected void onResume() {
		super.onResume();

		// System.out.println("-----------onResume()------��ʼonResume");

		// ͬ�����ڶ�ȡSharedPreferences����ǰҪʵ������һ��SharedPreferences����
		SharedPreferences getdata = getSharedPreferences("discover",
				Activity.MODE_PRIVATE);
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
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
