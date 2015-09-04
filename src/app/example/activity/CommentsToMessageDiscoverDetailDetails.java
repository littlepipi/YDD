package app.example.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import m.framework.network.NetworkHelper;
import net.loonggg.utils.AddCommentsMessage;
import net.loonggg.utils.CommentsAdapter;
import net.loonggg.utils.CommentsAdapter.CommentsDelete;
import net.loonggg.utils.CommentsMessage;
import net.loonggg.utils.CommentsToMessageDiscoverData;
import net.loonggg.utils.DiscoverMessageData;
import net.loonggg.utils.MyAlertDialog;
import net.loonggg.utils.MyDiscoverListView;
import net.loonggg.utils.NetManager;
import net.loonggg.utils.ReceiverNewTime;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Anim.CustomProgressDialog;
import app.example.Delete.DeleteMessageCommentsData;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;
import app.example.internet.picture.ImageLoader;
import app.example.netserver.HttpComments;
import app.example.netserver.MySQLiteMethodDetails;
import cn.sharesdk.demo.CustomShareFieldsPage;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author Administrator
 */
public class CommentsToMessageDiscoverDetailDetails extends Activity implements
		Callback, OnClickListener, CommentsDelete {
	private ImageView imageview_header, imageview_image1, imageview_image2,
			imageview_image3;
	private TextView textview_name, textview_time, textview_title,
			textview_details;
	private TextView textview_comment, textview_share;

	private TextView looknum, titlelikenno;
	private MyDiscoverListView list_message;
	private EditText input_message;
	private Button send_message;
	private ImageButton backButton;
	/**
	 * **********************************************
	 */
	// 分享
	private boolean shareFromQQLogin = false;
	public static String TEST_IMAGE;
	public static String TEST_IMAGE_URL;
	private static final String FILE_NAME = "pic_lovely_cats.jpg";
	public static HashMap<Integer, String> TEST_TEXT;

	/**
	 * **********************************************
	 */

	private String id;
	private String title;
	private String time;
	private String information;
	private String commitperson;
	private String followingnumber, looknumber;
	private String created;
	private String personname, personportrait;
	private CommentsAdapter commentsAdapter;
	private String image1;
	private String image2;
	private String image3;
	public ImageLoader imageLoader;
	private String token, phone_number;// 令牌
	private String commentsnickname;
	private String comment;
	private ArrayList<CommentsMessage> comments_num;
	private Boolean relay = false;
	private String relay_relay;
	private LinearLayout linearLayout;
	private DiscoverMessageData discoverMessageData = new DiscoverMessageData();
	private int width;
	protected String comments_phone;
	private View footerView;
	private int mScreenWidth;
	private float density;
	private CustomProgressDialog dialog;
	private TextView textview_location;
	private String personlocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.discovedetails);
		init();
		if (NetManager
				.isOpenNetwork(CommentsToMessageDiscoverDetailDetails.this)) {

			dialog = new CustomProgressDialog(
					CommentsToMessageDiscoverDetailDetails.this, "正在加载中",
					R.anim.frame);
			dialog.show();

			Netdata();

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					ReceiveData();
					/**********************************************/
					// 分享
					ShareSDK.initSDK(CommentsToMessageDiscoverDetailDetails.this);
					ShareSDK.setConnTimeout(20000);
					ShareSDK.setReadTimeout(20000);

					new Thread() {
						public void run() {
							// 分享的图片
							TEST_IMAGE_URL = "http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg";
							initImagePath();
							initTestText();

							UIHandler
									.sendEmptyMessageDelayed(
											1,
											100,
											CommentsToMessageDiscoverDetailDetails.this);
						}
					}.start();
					/**********************************************/

					/*****************************************/
					// 从登录获取令牌token
					SharedPreferences mySharedPreferencestoken = getSharedPreferences(
							"token", Activity.MODE_PRIVATE);
					// 使用getString方法获得value，注意第2个参数是value的默认值
					token = mySharedPreferencestoken.getString("token", "");
					SharedPreferences MyHttpPerson = getSharedPreferences(
							"phone_number", MODE_PRIVATE);

					phone_number = MyHttpPerson.getString("phone_number", "");
					/*****************************************/

					/*****************************************/
					dialog.dismiss();
					showList(comments_num);

					onBottonClick();

				}
			}, 1000);

		}

	}

	public void init() {

		imageLoader = new ImageLoader(MyApplication.getAppContext());

		backButton = (ImageButton) findViewById(R.id.discovedetails_back);
		imageview_header = (ImageView) findViewById(R.id.head_image);
		imageview_image1 = (ImageView) findViewById(R.id.picture);
		imageview_image2 = (ImageView) findViewById(R.id.picture2);
		imageview_image3 = (ImageView) findViewById(R.id.picture3);
		DisplayMetrics dm = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取屏幕分辨率

		mScreenWidth = dm.widthPixels;

		density = dm.density;

		imageview_image1.getLayoutParams().width = mScreenWidth / 3 - 3;

		imageview_image2.getLayoutParams().width = mScreenWidth / 3 - 3;

		imageview_image3.getLayoutParams().width = mScreenWidth / 3 - 3;
		imageview_image1.setOnClickListener(this);
		imageview_image2.setOnClickListener(this);
		imageview_image3.setOnClickListener(this);

		linearLayout = (LinearLayout) findViewById(R.id.comments_linearLayout);

		list_message = (MyDiscoverListView) findViewById(R.id.discovedetails_detalis);
		footerView = getLayoutInflater().inflate(
				R.layout.footer_layout_no_comments, null);

		list_message.addFooterView(footerView);

		footerView.addOnLayoutChangeListener(listfooter);
		textview_name = (TextView) findViewById(R.id.nick);
		textview_time = (TextView) findViewById(R.id.time);

		textview_title = (TextView) findViewById(R.id.title);
		textview_details = (TextView) findViewById(R.id.textdetail);
		input_message = (EditText) findViewById(R.id.comments_input_message);
		send_message = (Button) findViewById(R.id.comments_send_message);

		looknum = (TextView) findViewById(R.id.titleattentionno);

		width = send_message.getLayoutParams().width;
		textview_comment = (TextView) findViewById(R.id.discover_comment);
		textview_share = (TextView) findViewById(R.id.discover_share);
		textview_location = (TextView) findViewById(R.id.personlocation);

	}

	private void showList(final ArrayList<CommentsMessage> comments_num) {
		if (commentsAdapter == null) {

			commentsAdapter = new CommentsAdapter(
					CommentsToMessageDiscoverDetailDetails.this, comments_num);
			commentsAdapter.setInterface(this);
			list_message.setAdapter(commentsAdapter);
			if (list_message.getCount() == 2) {

			} else {
				list_message.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						if (NetManager
								.isOpenNetwork(CommentsToMessageDiscoverDetailDetails.this)) {
							if ("".equals(token)) {
								
								Toast.makeText(CommentsToMessageDiscoverDetailDetails.this, "请登录", Toast.LENGTH_SHORT).show();
							}else {
							relay = false;
							CommentsMessage commentsMessage = comments_num
									.get(position - 1);
							comments_phone = commentsMessage.getComment_phone();
							commentsnickname = commentsMessage
									.getCommentsNickname();
							relay_relay = "回复" + commentsnickname + ":";
							input_message.setHint(relay_relay);
							linearLayout.setVisibility(View.VISIBLE);

							InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							inputMethodManager.toggleSoftInput(0,
									InputMethodManager.HIDE_NOT_ALWAYS);
							input_message.setEnabled(true);
							input_message.setFocusable(true);
							input_message.setFocusableInTouchMode(true);
							input_message.requestFocus();
							}	} else {
							Toast.makeText(
									CommentsToMessageDiscoverDetailDetails.this,
									"暂无网络连接", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		} else {
			commentsAdapter.onDateChange(comments_num);
		}
	}

	private void ShowPickDialog(final int position) {

		new MyAlertDialog(CommentsToMessageDiscoverDetailDetails.this)
				.builder().setTitle("提示！").setMsg("确定删除这条评论？表后悔哦！")
				.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(View v) {

						CommentsMessage commentsMessage = comments_num
								.get(position);

						String comment_id = commentsMessage.getComment_id();
						DeleteMessageCommentsData deleteCommentsData = new DeleteMessageCommentsData(
								id, comment_id);
//						Log.i("删除删除！！！", "" + deleteCommentsData);
						comments_num.remove(position);
						commentsAdapter.notifyDataSetChanged();

					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				}).show();

	}

	public void Netdata() {
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		HttpComments httpComments = new HttpComments(id, "news");
		httpComments.submitAsyncHttpClientGet();

	}

	public void ReceiveData() {
		AddCommentsMessage addCommentsMessage = new AddCommentsMessage();
		comments_num = addCommentsMessage.addCommentsMessages(id);
		new CommentsToMessageDiscoverData(discoverMessageData, id);
		id = discoverMessageData.getId();
		time = discoverMessageData.getTime();
		title = discoverMessageData.getTitle();
		information = discoverMessageData.getInformation();
		personname = discoverMessageData.getPersonname();
		personportrait = discoverMessageData.getPersonportrait();
		commitperson = discoverMessageData.getCommitPerson();
		personlocation = discoverMessageData.getPersonlocation();

		looknumber = discoverMessageData.getShareNumber();
		followingnumber = discoverMessageData.getFollowingNumber();
		image1 = discoverMessageData.getImage1();
		image2 = discoverMessageData.getImage2();
		image3 = discoverMessageData.getImage3();
		textview_time.setText(created);
		textview_title.setText(title);

		textview_details.setText(information);

		looknum.setText(looknumber);
		textview_location.setText(personlocation);
		textview_name.setText(personname);
		imageLoader.DisplayImage(personportrait, imageview_header);

		if ((image1.length()) > ((UrlPath.host + "null").length())) {
			imageview_image1.setVisibility(View.VISIBLE);
			imageLoader.DisplayImage(image1, imageview_image1);
		} else if ((image2.length()) <= ((UrlPath.host + "null").length())) {
			imageview_image2.setVisibility(View.GONE);
		}
		if ((image2.length()) > ((UrlPath.host + "null").length())) {
			imageview_image2.setVisibility(View.VISIBLE);
			imageLoader.DisplayImage(image2, imageview_image2);
		} else if ((image2.length()) <= ((UrlPath.host + "null").length())) {
			imageview_image2.setVisibility(View.GONE);
		}
		if ((image3.length()) > ((UrlPath.host + "null").length())) {
			imageview_image3.setVisibility(View.VISIBLE);
			imageLoader.DisplayImage(image3, imageview_image3);
		} else if ((image3.length()) <= ((UrlPath.host + "null").length())) {
			imageview_image3.setVisibility(View.GONE);
		}
	}

	public void onBottonClick() {

		// 评论
		textview_comment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (NetManager
						.isOpenNetwork(CommentsToMessageDiscoverDetailDetails.this)) {
					if ("".equals(token)) {
						Toast.makeText(CommentsToMessageDiscoverDetailDetails.this, "请登录", Toast.LENGTH_SHORT).show();
						
					}else {
					relay = true;
					input_message.setHint("请输入评论内容");
					linearLayout.setVisibility(View.VISIBLE);
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.toggleSoftInput(0,
							InputMethodManager.HIDE_NOT_ALWAYS);
					input_message.setEnabled(true);
					input_message.setFocusable(true);
					input_message.setFocusableInTouchMode(true);
					input_message.requestFocus();
					}} else {
					Toast.makeText(CommentsToMessageDiscoverDetailDetails.this,
							"暂无网络连接", Toast.LENGTH_SHORT).show();
				}

			}
		});

		/*****************************************/

		/*****************************************/
		// 分享
		textview_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// 点击分享的事件
				showShare(true, null, false, id);

			}
		});
		/*****************************************/
		// 返回按钮
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});
		send_message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (relay == true) {
					comment = input_message.getText().toString();
					/**
					 * 直接导入到数据库
					 */
					String comments_time = ReceiverNewTime.NewTime();
					SharedPreferences sharedPreferences = MyApplication
							.getAppContext().getSharedPreferences(
									"MyHttpPerson", Activity.MODE_PRIVATE);
					String comments_header = sharedPreferences.getString(
							"personportrait", "");
					String comments_name = sharedPreferences.getString(
							"personname", "");

					boolean flag = MySQLiteMethodDetails.insertcomment_Db(id,
							null, commitperson, comments_header, comments_name,
							comments_time, comment);
					CommentsMessage commentsMessage = new CommentsMessage();
					commentsMessage.setCommentsContent(comment);
					commentsMessage.setCommentsHeader(comments_header);
					commentsMessage.setCommentsNickname(comments_name);
					commentsMessage.setCommentsTime(comments_time);
					comments_num.add(commentsMessage);
					commentsAdapter.notifyDataSetChanged();
					list_message.setSelection(list_message.getCount() - 1);

					submitAsyncHttpClientPostSendComent(token, comment,
							phone_number, id, "COMMENT", commitperson);

					input_message.setText("");
					linearLayout.setVisibility(View.GONE);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(input_message.getWindowToken(),
							0);
				} else {
					comment = relay_relay + input_message.getText().toString();
					/**
					 * 直接导入到数据库
					 */
					String comments_time = ReceiverNewTime.NewTime();
					SharedPreferences sharedPreferences = MyApplication
							.getAppContext().getSharedPreferences(
									"MyHttpPerson", Activity.MODE_PRIVATE);
					String comments_header = sharedPreferences.getString(
							"personportrait", "");
					String comments_name = sharedPreferences.getString(
							"personname", "");

					boolean flag = MySQLiteMethodDetails.insertcomment_Db(id,
							null, commitperson, comments_header, comments_name,
							comments_time, comment);
					CommentsMessage commentsMessage = new CommentsMessage();
					commentsMessage.setCommentsContent(comment);
					commentsMessage.setCommentsHeader(comments_header);
					commentsMessage.setCommentsNickname(comments_name);
					commentsMessage.setCommentsTime(comments_time);
					comments_num.add(commentsMessage);
					commentsAdapter.notifyDataSetChanged();
					list_message.setSelection(list_message.getCount() - 1);

					submitAsyncHttpClientPostSendComent(token, comment,
							phone_number, id, "COMMENT", comments_phone);

					input_message.setText("");
					linearLayout.setVisibility(View.GONE);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(input_message.getWindowToken(),
							0);
				}

			}
		});
	}

	/**
	 * 点击删除
	 */
	@Override
	public void onCommentsDelete(int position) {
		ShowPickDialog(position);

	}

	protected void submitAsyncHttpClientPostSendComent(String token,
			String comment, String phone_number, String id, String operation,
			String toperson) {

		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.newsOperateApi;
		// 创建请求参数
		RequestParams param = new RequestParams();
		param.put("phone_number", phone_number);
		param.put("to_person", toperson);
		param.put("token", token);
		param.put("id", id);
		param.put("content", comment);
		param.put("operation", "COMMENT");
//		Log.i("----", "" + phone_number + "" + toperson + "" + token + "" + id
//				+ "" + comment);
		// 执行post方法
		client.post(url, param, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
//				Log.i("------", arg0 + "====" + new String(arg2));

				Toast.makeText(CommentsToMessageDiscoverDetailDetails.this,
						"评论失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Toast.makeText(CommentsToMessageDiscoverDetailDetails.this,
						"评论成功", Toast.LENGTH_SHORT).show();

			}

		});

	}

	
	
	private void initImagePath() {
		try {
			String cachePath = cn.sharesdk.framework.utils.R.getCachePath(
					CommentsToMessageDiscoverDetailDetails.this, null);
			TEST_IMAGE = cachePath + FILE_NAME;
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				// Bitmap pic = BitmapFactory.decodeResource(getResources(),
				// R.drawable.pic);
				FileOutputStream fos = new FileOutputStream(file);
				// pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
//		Log.i("TEST_IMAGE path ==>>>", TEST_IMAGE);
	}

	@SuppressLint("UseSparseArrays")
	private void initTestText() {
		TEST_TEXT = new HashMap<Integer, String>();
		try {
			NetworkHelper network = new NetworkHelper();
			String resp = network.httpGet("http://mob.com/Assets/snsplat.json",
					null, null);
			JSONObject json = new JSONObject(resp);
			int status = json.optInt("status");
			if (status == 200) {
				JSONArray democont = json.optJSONArray("democont");
				if (democont != null && democont.length() > 0) {
					for (int i = 0, size = democont.length(); i < size; i++) {
						JSONObject plat = democont.optJSONObject(i);
						if (plat != null) {
							int snsplat = plat.optInt("snsplat", -1);
							String cont = plat.optString("cont");
							TEST_TEXT.put(snsplat, cont);
						}
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 1: {
			// menu.triggerItem(MainAdapter.GROUP_DEMO, MainAdapter.ITEM_DEMO);
		}
			break;
		case 2: {
			// String text = getString(R.string.receive_rewards, msg.arg);
			// Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		}
			break;
		}
		return false;
	}

	/**
	 * 将action转换为String
	 */
	public static String actionToString(int action) {
		switch (action) {
		case Platform.ACTION_AUTHORIZING:
			return "ACTION_AUTHORIZING";
		case Platform.ACTION_GETTING_FRIEND_LIST:
			return "ACTION_GETTING_FRIEND_LIST";
		case Platform.ACTION_FOLLOWING_USER:
			return "ACTION_FOLLOWING_USER";
		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
			return "ACTION_SENDING_DIRECT_MESSAGE";
		case Platform.ACTION_TIMELINE:
			return "ACTION_TIMELINE";
		case Platform.ACTION_USER_INFOR:
			return "ACTION_USER_INFOR";
		case Platform.ACTION_SHARE:
			return "ACTION_SHARE";
		default: {
			return "UNKNOWN";
		}
		}
	}

	/**
	 * **************************************************
	 */
	// 以下是分享用到
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> res) {

		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, CommentsToMessageDiscoverDetailDetails.this);
	}

	public void onCancel(Platform palt, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = palt;
		UIHandler.sendMessage(msg, this);
	}

	public void onError(Platform palt, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = palt;
		UIHandler.sendMessage(msg, this);
	}

	public boolean handleMessage2(Message msg) {
		Platform plat = (Platform) msg.obj;
		String text = actionToString(msg.arg2);
		switch (msg.arg1) {
		case 1: {
			// 成功
			text = plat.getName() + " completed at " + text;
		}
			break;
		case 2: {
			// 失败
			text = plat.getName() + " caught error at " + text;
		}
			break;
		case 3: {
			// 取消
			text = plat.getName() + " canceled at " + text;
		}
			break;
		}

		Toast.makeText(CommentsToMessageDiscoverDetailDetails.this, text,
				Toast.LENGTH_SHORT).show();
		return false;
	}

	// 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）

	/**
	 * ShareSDK集成方法有两种</br>
	 * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
	 * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br> 请看“ShareSDK
	 * 使用说明文档”，SDK下载目录中 </br> 或者看网络集成文档
	 * http://wiki.mob.com/Android_%E5%BF%AB%E9%
	 * 80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
	 * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
	 * <p/>
	 * <p/>
	 * 平台配置信息有三种方式： 1、在我们后台配置各个微博平台的key
	 * 2、在代码中配置各个微博平台的key，http://mob.com/androidDoc
	 * /cn/sharesdk/framework/ShareSDK.html
	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
	 */
	private void showShare(boolean silent, String platform,
			boolean captureView, String id2) {

		System.out.println("第2点");
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name)); // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.evenote_title));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// String Url = "http://123.56.150.30:8000/alive/id=57";
		String Urlr = "http://123.56.150.30:8000/alive/id=";
		String Url = Urlr + id + "/";
		oks.setTitleUrl(Url);
		// text是分享文本，所有平台都需要这个字段
		String share_content = "易丢丢，不仅是一个失物招领，发表说说的软件，在里面，还可以找回丢失的女朋友哦，"
				+ "还等什么，赶紧下载吧，嘿嘿！详情见" + Url + " @易丢丢开发者服务平台";
		oks.setText(share_content);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(Url);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我的地盘，我主宰！");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(Url);
		oks.setVenueName(CustomShareFieldsPage.getString("venueName",
				"ShareSDK"));
		// 表示分享位置的名称
		oks.setVenueDescription(CustomShareFieldsPage.getString(
				"venueDescription", "This is a beautiful place!"));

		oks.setFilePath(CommentsToMessageDiscoverDetailDetails.this.TEST_IMAGE);
		oks.setLatitude(23.056081f);
		oks.setLongitude(113.385708f);
		oks.setSilent(silent);
		oks.setShareFromQQAuthSupport(shareFromQQLogin);
		String theme = "classic";
		if (OnekeyShareTheme.SKYBLUE.toString().toLowerCase().equals(theme)) {
			oks.setTheme(OnekeyShareTheme.SKYBLUE);
		} else {
			oks.setTheme(OnekeyShareTheme.CLASSIC);
		}

		if (platform != null) {
			oks.setPlatform(platform);
		}

	
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();

		oks.disableSSOWhenAuthorize();

		oks.show(CommentsToMessageDiscoverDetailDetails.this);

	}

	// 点击详情的图片进行浏览
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.picture:

			Intent intent1 = new Intent(
					CommentsToMessageDiscoverDetailDetails.this,
					ImageActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putString("image", image1);
			intent1.putExtras(bundle1);
			CommentsToMessageDiscoverDetailDetails.this.startActivity(intent1);

			break;

		case R.id.picture2:
			Intent intent2 = new Intent(
					CommentsToMessageDiscoverDetailDetails.this,
					ImageActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putString("image", image2);
			intent2.putExtras(bundle2);
			CommentsToMessageDiscoverDetailDetails.this.startActivity(intent2);

			break;
		case R.id.picture3:
			Intent intent3 = new Intent(
					CommentsToMessageDiscoverDetailDetails.this,
					ImageActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putString("image", image3);
			intent3.putExtras(bundle3);
			CommentsToMessageDiscoverDetailDetails.this.startActivity(intent3);
			break;

		}

	}

	private void backmethod() {
		backButton = (ImageButton) findViewById(R.id.post_lost_message_back1);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				CommentsToMessageDiscoverDetailDetails.this.finish();

				CommentsToMessageDiscoverDetailDetails.this
						.overridePendingTransition(R.anim.in_from_left,
								R.anim.out_to_right);

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			CommentsToMessageDiscoverDetailDetails.this.finish();

			CommentsToMessageDiscoverDetailDetails.this
					.overridePendingTransition(R.anim.in_from_left,
							R.anim.out_to_right);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {

		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth() + width;
			;
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {

				if (!(NetManager
						.isOpenNetwork(CommentsToMessageDiscoverDetailDetails.this))) {
					send_message.setEnabled(false);
					Toast.makeText(CommentsToMessageDiscoverDetailDetails.this,
							"无网络连接", Toast.LENGTH_SHORT).show();

				} else {
					if ("".equals(token)) {
						send_message.setEnabled(false);
						Toast.makeText(
								CommentsToMessageDiscoverDetailDetails.this,
								"请登录", Toast.LENGTH_SHORT).show();
					} else {
						send_message.setEnabled(true);
					}

				}

				return false;
			} else {
				linearLayout.setVisibility(View.GONE);
				input_message.setText("");
				return true;
			}
		}
		return false;
	}

	OnLayoutChangeListener listfooter = new OnLayoutChangeListener() {

		@Override
		public void onLayoutChange(View v, int left, int top, int right,
				int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
			if (list_message.getCount() == 2) {
				v.setVisibility(View.VISIBLE);
			} else {
				v.setVisibility(View.GONE);
			}

		}

	};
}
