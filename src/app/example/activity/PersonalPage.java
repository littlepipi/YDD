package app.example.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import m.framework.network.NetworkHelper;
import net.loonggg.utils.AddLFRecordMessage;
import net.loonggg.utils.Constant;
import net.loonggg.utils.DateTime;
import net.loonggg.utils.DateUtil;
import net.loonggg.utils.PersonalBaseAdapter;
import net.loonggg.utils.DateUtil.DateFormatWrongException;
import net.loonggg.utils.PersonalBaseAdapter.Comment;
import net.loonggg.utils.PersonalBaseAdapter.Mappicture;
import net.loonggg.utils.PersonalBaseAdapter.Phone;
import net.loonggg.utils.PersonalBaseAdapter.Share;
import net.loonggg.utils.PersonalBaseAdapter.ShortMessage;
import net.loonggg.utils.Personal_ListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Anim.CustomProgressDialog;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;
import app.example.internet.picture.ImageLoader;
import app.example.netserver.HttpComments;
import app.example.netserver.HttpRecord;
import app.example.netserver.HttpRecordMessagePerson;
import app.example.netserver.HttpRecordNewsPerson;
import app.example.netserver.MySQLiteMethodDetails;
import app.example.netserver.PersonData;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.example.baidumaplocation.BaiduShiWu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class PersonalPage extends Activity implements Phone, ShortMessage,
		Mappicture, OnItemClickListener, Comment, Share, Callback {

	private JSONArray news;
	private JSONArray messages;

	String id, what, losttime, lostlatitude, lostlangitude, kind, information,
			ccommitperson, contactphone, sharenumber, commentsnumber,
			followingnumber, location, created, title, comments, latlng,
			image1, image2, image3, time;

	boolean flagg;
	protected JSONArray followers;

	private String personname;
	private String personportrait;
	private String personlocation;
	private String commitperson;

	private TextView personal_page_lost, personal_page_find,
			personal_page_discover;

	private ImageButton personal_page_back, personal_page_add_words;
	private ImageView personal_page_head_portrait;
	private TextView personal_page_nickname, personal_page_location;
	public ImageLoader imageLoader;
	String phoneno;
	/**************************************************/
	private boolean shareFromQQLogin = false;
	public static String TEST_IMAGE;
	public static String TEST_IMAGE_URL;
	private static final String FILE_NAME = "pic_lovely_cats.jpg";
	public static HashMap<Integer, String> TEST_TEXT;

	/**************************************************/
	private PersonalBaseAdapter adapter;
	private Personal_ListView listview;

	boolean b = false;
	boolean flag = false;

	private ArrayList<PersonData> apk_list;

	private Activity ACTIVITY = PersonalPage.this;
	CustomProgressDialog dialog;
	private String token;
	private View footerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_page);
		apk_list = new ArrayList<PersonData>();
		start();
		receiver();
		dialog = new CustomProgressDialog(PersonalPage.this, "正在加载中",
				R.anim.frame);
		dialog.show();

	}

	/**
	 * 初始化布局
	 */
	public void start() {
		imageLoader = new ImageLoader(MyApplication.getAppContext());
		personal_page_back = (ImageButton) findViewById(R.id.personal_page_back);
		personal_page_add_words = (ImageButton) findViewById(R.id.personal_page_add_words);
		personal_page_head_portrait = (ImageView) findViewById(R.id.personal_page_head_portrait);
		personal_page_nickname = (TextView) findViewById(R.id.personal_page_nickname);
		personal_page_location = (TextView) findViewById(R.id.personal_page_location);
		personal_page_lost = (TextView) findViewById(R.id.personal_page_lost);
		personal_page_find = (TextView) findViewById(R.id.personal_page_find);
		personal_page_discover = (TextView) findViewById(R.id.personal_page_discover);
		listview = (Personal_ListView) findViewById(R.id.personal_message);
		footerView = getLayoutInflater().inflate(R.layout.personal_footer_item,
				null);

		listview.addFooterView(footerView);

		footerView.addOnLayoutChangeListener(listfooter);
	}

	public void data() {
		personal_page_nickname.setText(personname);
		personal_page_location.setText(personlocation);
		imageLoader.DisplayImage(personportrait, personal_page_head_portrait);
	}

	public void receiver() {
		SharedPreferences mySharedPreferences = getSharedPreferences(
				"Personal_Personal", Activity.MODE_PRIVATE);

		commitperson = mySharedPreferences
				.getString("personal_commitperson", "").toString().trim();
		personname = mySharedPreferences.getString("personal_personname", "")
				.toString().trim();
		personportrait = mySharedPreferences
				.getString("personal_personportrait", "").toString().trim();
		personlocation = mySharedPreferences
				.getString("personal_personlocation", "").toString().trim();
		submitAsyncHttpClientGet(commitperson, false);

		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		token = mySharedPreferencestoken.getString("token", "").toString()
				.trim();

	}

	/**
	 * 发表留言
	 */
	public void words() {
		personal_page_add_words.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("".equals(token)) {
					Toast.makeText(PersonalPage.this, "请登录！",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(ACTIVITY, PublishWord.class);
					intent.putExtra("commitperson", commitperson);
					startActivityForResult(intent, Constant.PERSONAL_ADD_WORDS);
				}

			}
		});

	}

	private void Click() {

		personal_page_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				MySQLiteMethodDetails.EmptyTable("lostpersonal");
				MySQLiteMethodDetails.EmptyTable("findpersonal");
				MySQLiteMethodDetails.EmptyTable("discoverpersonal");
				finish();
				ACTIVITY.overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);
			}
		});

		personal_page_find.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalPage.this,
						Personal_Found.class);
				intent.putExtra("commitperson", commitperson);
				startActivity(intent);
				finish();

			}
		});

		personal_page_discover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(PersonalPage.this,
						Personal_Discover.class);
				intent.putExtra("commitperson", commitperson);
				startActivity(intent);
				finish();
			}
		});

	}

	private void showList(final ArrayList<PersonData> apk_list) {
		if (adapter == null) {
			adapter = new PersonalBaseAdapter(ACTIVITY, apk_list);
			adapter.setInterface((Phone) this);
			adapter.setInterface((ShortMessage) this);
			adapter.setInterface((Mappicture) this);
			adapter.setInterface((Comment) this);
			adapter.setInterface((Share) this);
			listview.setAdapter(adapter);
			if (listview.getCount() == 2) {

			} else {
				listview.setOnItemClickListener(this);
			}
		} else {
			adapter.onDateChange(apk_list);
		}
	}

	@Override
	public void onphone(final String phone) {

		Builder dialog = new AlertDialog.Builder(ACTIVITY)
				.setIcon(R.drawable.button_green)
				.setTitle("拨打电话：")
				.setMessage("您确定要拨打" + phone + "这个号码吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {

						dialog.dismiss();

						phoneno = phone;
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + phoneno));
						startActivity(intent);

					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

		dialog.show();
	}

	@Override
	public void onmessage(final String phone) {

		Builder dialog = new AlertDialog.Builder(ACTIVITY)
				.setIcon(R.drawable.ico_to_there)
				.setTitle("发送短信到：" + phone)

				.setMessage("您确定要发送短信Ta吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
						Intent intent = new Intent(ACTIVITY, SendMessage.class);
						intent.putExtra("number", phone);
						ACTIVITY.startActivity(intent);
						ACTIVITY.finish();
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

		dialog.show();
	}

	@Override
	public void onmappicture(int position) {

		PersonData arraydatas = apk_list.get(position);

		String title = arraydatas.getTitle();
		String time = arraydatas.getTime();
		String langitude = arraydatas.getLangitude();
		String latitude = arraydatas.getLatitude();
		String kind = arraydatas.getKind();
		String information = arraydatas.getInformation();
		String location = arraydatas.getLocation();
		String created = arraydatas.getCreated();
		Intent intent = new Intent(ACTIVITY, BaiduShiWu.class);
		Bundle bundle = new Bundle();
		bundle.putString("created", created);
		bundle.putString("time", time);
		bundle.putString("location", location);
		bundle.putString("label", kind);
		bundle.putString("latitude", latitude);
		bundle.putString("langitude", langitude);
		bundle.putString("textdetail", information);
		bundle.putString("title", title);
		intent.putExtras(bundle);
		ACTIVITY.startActivity(intent);
	}

	// 评论
	/****************************************************************/
	@Override
	public void oncomment(int position) {

		PersonData arraydatas = apk_list.get(position);
		String id = arraydatas.getId();

		String title = arraydatas.getTitle();
		String losttime = arraydatas.getTime();
		String lostlangitude = arraydatas.getLangitude();
		String lostlatitude = arraydatas.getLatitude();
		String lostkind = arraydatas.getKind();
		String lostinformation = arraydatas.getInformation();
		String commitperson = arraydatas.getCommitPerson();
		// String sharenumber = arraydatas.getShareNumber();
		// String commentsnumber = arraydatas.getCommentsNumber();
		String followingnumber = arraydatas.getFollowingNumber();
		String location = arraydatas.getLocation();
		String created = arraydatas.getCreated();
		String comments = arraydatas.getComments();
		String contactphone = arraydatas.getContactPhone();
		String personname = arraydatas.getPersonname();
		String personportrait = arraydatas.getPersonportrait();
		String image1 = arraydatas.getImage1();
		String image2 = arraydatas.getImage2();
		String image3 = arraydatas.getImage3();
		String personlocations = arraydatas.getPersonlocation();
		Bundle bundle = new Bundle();
		bundle.putString("personlocations", personlocations);

		bundle.putString("id", id);
		bundle.putString("title", title);
		bundle.putString("time", losttime);
		bundle.putString("langitude", lostlangitude);
		bundle.putString("latitude", lostlatitude);
		bundle.putString("kind", lostkind);
		bundle.putString("information", lostinformation);
		bundle.putString("commitperson", commitperson);
		bundle.putString("contactphone", contactphone);
		bundle.putString("followingnumber", followingnumber);
		bundle.putString("location", location);
		bundle.putString("created", created);
		bundle.putString("comments", comments);
		bundle.putString("personname", personname);
		bundle.putString("personportrait", personportrait);
		bundle.putString("image1", image1);
		bundle.putString("image2", image2);
		bundle.putString("image3", image3);
		Intent intent = new Intent(ACTIVITY, ListViewMessageDetails.class);
		intent.putExtras(bundle);
		ACTIVITY.startActivity(intent);

	}

	/****************************************************************/

	/****************************************************************/
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		PersonData arraydatas = apk_list.get(position - 1);
		String id = arraydatas.getId();
		String title = arraydatas.getTitle();
		String losttime = arraydatas.getTime();
		String lostlangitude = arraydatas.getLangitude();
		String lostlatitude = arraydatas.getLatitude();
		String lostkind = arraydatas.getKind();
		String lostinformation = arraydatas.getInformation();
		String commitperson = arraydatas.getCommitPerson();
		String followingnumber = arraydatas.getFollowingNumber();
		String location = arraydatas.getLocation();
		String created = arraydatas.getCreated();
		String comments = arraydatas.getComments();
		String contactphone = arraydatas.getContactPhone();
		String personname = arraydatas.getPersonname();
		String personportrait = arraydatas.getPersonportrait();
		String image1 = arraydatas.getImage1();
		String image2 = arraydatas.getImage2();
		String image3 = arraydatas.getImage3();

		String personlocations = arraydatas.getPersonlocation();
		Bundle bundle = new Bundle();
		bundle.putString("personlocations", personlocations);

		bundle.putString("id", id);
		bundle.putString("title", title);
		bundle.putString("time", losttime);
		bundle.putString("langitude", lostlangitude);
		bundle.putString("latitude", lostlatitude);
		bundle.putString("kind", lostkind);
		bundle.putString("information", lostinformation);
		bundle.putString("commitperson", commitperson);
		bundle.putString("followingnumber", followingnumber);
		bundle.putString("location", location);
		bundle.putString("created", created);
		bundle.putString("comments", comments);
		bundle.putString("contactphone", contactphone);
		bundle.putString("personname", personname);
		bundle.putString("personportrait", personportrait);
		bundle.putString("image1", image1);
		bundle.putString("image2", image2);
		bundle.putString("image3", image3);

		Intent intent = new Intent(ACTIVITY, ListViewMessageDetails.class);
		intent.putExtras(bundle);
		ACTIVITY.startActivity(intent);

	}

	@Override
	public void onshare(int position) {

		showShare(false, null, false, position);

	}

	private void initImagePath() {
		try {
			String cachePath = cn.sharesdk.framework.utils.R.getCachePath(
					ACTIVITY, null);
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

	/** 将action转换为String */
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

	public void onComplete(Platform plat, int action,
			HashMap<String, Object> res) {

		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, PersonalPage.this);
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

		Toast.makeText(ACTIVITY, text, Toast.LENGTH_SHORT).show();
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
	 * 
	 * 
	 * 平台配置信息有三种方式： 1、在我们后台配置各个微博平台的key
	 * 2、在代码中配置各个微博平台的key，http://mob.com/androidDoc
	 * /cn/sharesdk/framework/ShareSDK.html
	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
	 */
	@SuppressLint("DefaultLocale")
	private void showShare(boolean silent, String platform,
			boolean captureView, int position) {

		System.out.println("第2点position" + position);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name)); // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.evenote_title));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		String Urlr = "http://123.56.150.30:8000/alive/id=";
		String Urlt = Urlr + position + "/";
		oks.setTitleUrl(Urlt);
		// text是分享文本，所有平台都需要这个字段
		String share_content = "易丢丢是一款基于校园和城市，分区检索、发布及分享失物招领信息的APP，为用户提供高效、便捷的失物招领服务!"
				+ Urlt;
		oks.setText(share_content);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(Urlt);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我的地盘，我主宰！");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(Urlt);
		oks.setImageUrl(PersonalPage.TEST_IMAGE_URL);
		oks.setFilePath(PersonalPage.TEST_IMAGE);
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

		System.out.println("第3点");
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();

		// 在自动授权时可以禁用SSO方式
		// if(!CustomShareFieldsPage.getBoolean("enableSSO", true))
		oks.disableSSOWhenAuthorize();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
		// oks.setCallback(new OneKeyShareCallback());

		// 去自定义不同平台的字段内容
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());

		// 去除注释，演示在九宫格设置自定义的图标
		// Bitmap enableLogo = BitmapFactory.decodeResource(getResources(),
		// R.drawable.ic_launcher);
		// Bitmap disableLogo = BitmapFactory.decodeResource(getResources(),
		// R.drawable.sharesdk_unchecked);
		@SuppressWarnings("unused")
		String label = getResources().getString(R.string.app_name);
		@SuppressWarnings("unused")
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				String text = "Customer Logo -- ShareSDK "
						+ ShareSDK.getSDKVersionName();
				Toast.makeText(ACTIVITY, text, Toast.LENGTH_SHORT).show();
			}
		};
		// oks.setCustomerLogo(enableLogo, disableLogo, label, listener);

		// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);
		// oks.addHiddenPlatform(TencentWeibo.NAME);

		// 为EditPage设置一个背景的View
		// oks.setEditPageBackground(getPage());
		System.out.println("第4点");
		// 启动分享GUI
		oks.show(ACTIVITY);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			MySQLiteMethodDetails.EmptyTable("lostpersonal");
			MySQLiteMethodDetails.EmptyTable("findpersonal");
			MySQLiteMethodDetails.EmptyTable("discoverpersonal");
			Intent intent = new Intent(PersonalPage.this, MainActivity.class);
			PersonalPage.this.startActivity(intent);

			PersonalPage.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	OnLayoutChangeListener listfooter = new OnLayoutChangeListener() {

		@Override
		public void onLayoutChange(View v, int left, int top, int right,
				int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
			if (listview.getCount() == 2) {
				v.setVisibility(View.VISIBLE);
			} else {
				v.setVisibility(View.GONE);
			}

		}

	};

	@SuppressLint("HandlerLeak")
	public void submitAsyncHttpClientGet(final String phone_number,
			final boolean flagg) {
		// 创建异步请求端对象

		AsyncHttpClient client = new AsyncHttpClient();

		String urlr = UrlPath.rocordOperateApi;

		String url = urlr + phone_number;

		// 发送get请求对象
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

				Log.i("HttpRecord", "失败失败！！！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
				// TODO Auto-generated method stub
				Log.i("HttpRecord", "成功成功!!!!");
				String iString = new String(responseBody);

				try {
					JSONObject jsonObject = new JSONObject(iString);
					news = jsonObject.getJSONArray("news");
					messages = jsonObject.getJSONArray("messages");
					JSONObject jsonnews = new JSONObject();
					JSONObject jsonmessage = new JSONObject();
					for (int i = 0; i < news.length(); i++) {
						jsonnews = news.getJSONObject(i);
						id = jsonnews.getString("id");
						/*********************************************/

						HttpComments myHttpComments = new HttpComments(id,
								"news");
						myHttpComments.submitAsyncHttpClientGet();
						/*********************************************/
						String lostimagestart = jsonnews.getString("image1");
						String pathpopu = UrlPath.host;
						image1 = pathpopu + lostimagestart;
						String lostimagestart1 = jsonnews.getString("image2");
						image2 = pathpopu + lostimagestart1;
						String lostimagestart3 = jsonnews.getString("image3");
						image3 = pathpopu + lostimagestart3;
						title = jsonnews.getString("title");
						information = jsonnews.getString("information");
						ccommitperson = jsonnews.getString("commit_person");

						try {
							time = DateUtil.toSimpleDate(jsonnews
									.getString("created"));
						} catch (DateFormatWrongException e) {

							e.printStackTrace();
						}
						sharenumber = jsonnews.getString("reading_number");
						followingnumber = jsonnews
								.getString("following_number");
						followers = jsonnews.getJSONArray("followers");
						commentsnumber = "1";
						for (int j = 0; j < followers.length(); j++) {

							String oneperson = followers.getString(j);
							System.out.println("返回点赞人：：：" + oneperson);
							if (ccommitperson.equals(oneperson)) {
								commentsnumber = "0";
							}
						}
						HttpRecordNewsPerson httpRecordNewsPerson = new HttpRecordNewsPerson(
								phone_number);
						httpRecordNewsPerson.submitAsyncHttpClientGet(i, id,
								time, information, ccommitperson, title,
								image1, image2, image3, sharenumber,
								commentsnumber, followingnumber, flagg);
						httpRecordNewsPerson.submitAsyncHttpClientGet();

					}
					for (int i = 0; i < messages.length(); i++) {
						jsonmessage = messages.getJSONObject(i);
						id = jsonmessage.getString("id");
						/*********************************************/

						HttpComments myHttpComments = new HttpComments(id,
								"message");
						myHttpComments.submitAsyncHttpClientGet();
						/*********************************************/
						what = jsonmessage.getString("what");

						latlng = jsonmessage.getString("latlng");
						kind = jsonmessage.getString("kind");
						title = jsonmessage.getString("title");
						information = jsonmessage.getString("information");
						ccommitperson = jsonmessage.getString("commit_person");

						contactphone = jsonmessage.getString("contact_phone");
						sharenumber = jsonmessage.getString("share_number");
						followingnumber = jsonmessage
								.getString("reading_number");
						commentsnumber = jsonmessage
								.getString("comments_number");
						location = jsonmessage.getString("location");

						String lostimagestart = jsonmessage.getString("image1");
						String pathpopu = UrlPath.host;
						image1 = pathpopu + lostimagestart;
						String lostimagestart1 = jsonmessage
								.getString("image2");
						image2 = pathpopu + lostimagestart1;
						String lostimagestart3 = jsonmessage
								.getString("image3");
						image3 = pathpopu + lostimagestart3;

						try {
							losttime = DateTime.toSimpleDate(jsonmessage
									.getString("time"));
						} catch (net.loonggg.utils.DateTime.DateFormatWrongException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							created = DateUtil.toSimpleDate(jsonmessage
									.getString("created"));
						} catch (DateFormatWrongException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						String[] sp = { "titude:", "gitude:" };// 关键字数组
						for (int k = 0; k < sp.length; k++) {
							String value = sp[k];// 取出一个关键字

							String ee = "," + value + ",";// 关键字前后加逗号
							latlng = latlng.replaceAll(value, ee);// 这句很关键，replaceAll有返回值，如不把返回值给vv，vv的值不会变
						}
						String[] result = latlng.split(",");// 按照逗号分割

						lostlatitude = result[2];
						lostlangitude = result[5];

						comments = null;
						HttpRecordMessagePerson httpRecordMessagePerson = new HttpRecordMessagePerson(
								commitperson);
						httpRecordMessagePerson.submitAsyncHttpClientGet(i, id,
								what, losttime, lostlatitude, lostlangitude,
								kind, information, ccommitperson, contactphone,
								sharenumber, commentsnumber, followingnumber,
								location, created, title, comments, image1,
								image2, image3, flagg);
						httpRecordMessagePerson.submitAsyncHttpClientGet();

					}
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {

							data();
							Click();
							words();

							AddLFRecordMessage.AddPersonDatas("lostpersonal",
									apk_list, false);
							ShareSDK.initSDK(ACTIVITY);
							ShareSDK.setConnTimeout(20000);
							ShareSDK.setReadTimeout(20000);

							new Thread() {
								public void run() {
									// 分享的图片
									// http://t1.qpic.cn/mblogpic/db90a049c73e9ff8aea0/2000//易丢丢logo网址
									TEST_IMAGE_URL = "http://t1.qpic.cn/mblogpic/db90a049c73e9ff8aea0/2000";
									initImagePath();
									initTestText();

									UIHandler.sendEmptyMessageDelayed(1, 100,
											PersonalPage.this);
								}
							}.start();
							dialog.dismiss();
							showList(apk_list);

						}
					}, 1000);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		return;

	}

}
