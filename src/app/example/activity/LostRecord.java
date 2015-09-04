package app.example.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import m.framework.network.NetworkHelper;
import net.loonggg.utils.AddLFRecordMessage;
import net.loonggg.utils.AutoListView;
import net.loonggg.utils.AutoListView.ILoadListener;
import net.loonggg.utils.AutoListView.IReflashListener;
import net.loonggg.utils.DateUtil.DateFormatWrongException;
import net.loonggg.utils.DateTime;
import net.loonggg.utils.DateUtil;
import net.loonggg.utils.MyAlertDialog;
import net.loonggg.utils.NetManager;
import net.loonggg.utils.RecordAdapter;
import net.loonggg.utils.RecordAdapter.Commentrecord;
import net.loonggg.utils.RecordAdapter.DeleteMessagerecord;
import net.loonggg.utils.RecordAdapter.Sharerecord;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Anim.CustomProgressDialog;
import app.example.Delete.DeleteData;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;
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

public class LostRecord extends Activity implements IReflashListener,
		ILoadListener, OnItemClickListener, Commentrecord, DeleteMessagerecord,
		Sharerecord, Callback {
	private JSONArray news;
	private JSONArray messages;

	String id, what, losttime, lostlatitude, lostlangitude, kind, information,
			ccommitperson, contactphone, sharenumber, commentsnumber,
			followingnumber, location, created, title, comments, latlng,
			image1, image2, image3, time;

	boolean flagg;
	protected JSONArray followers;
	private TextView recordtext;
	private LinearLayout no_data_cord, no_net_cord;
	private AutoListView autolistview;
	private RecordAdapter adapter;
	private ImageButton backButton;

	/**************************************************/
	private boolean shareFromQQLogin = false;
	public static String TEST_IMAGE;
	public static String TEST_IMAGE_URL;
	private static final String FILE_NAME = "pic_lovely_cats.jpg";
	public static HashMap<Integer, String> TEST_TEXT;
	private String phone_number;
	private CustomProgressDialog dialog;
	private ArrayList<PersonData> apk_list = new ArrayList<PersonData>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);
		chushihua();
		if (NetManager.isOpenNetwork(LostRecord.this)) {
			no_net_cord.setVisibility(View.GONE);
			submitAsyncHttpClientGet(phone_number, true);

			dialog = new CustomProgressDialog(LostRecord.this, "正在加载中",
					R.anim.frame);
			dialog.show();

		} else {
			no_net_cord.setVisibility(View.VISIBLE);
			autolistview.setVisibility(View.GONE);
		}

	}

	public void chushihua() {

		no_data_cord = (LinearLayout) findViewById(R.id.no_data_cord);
		no_net_cord = (LinearLayout) findViewById(R.id.no_net_cord);
		autolistview = (AutoListView) findViewById(R.id.record);

		backButton = (ImageButton) findViewById(R.id.record_back);
		recordtext = (TextView) findViewById(R.id.recordtext);
		recordtext.setText("失物记录");
		SysApplication.getInstance().addActivity(this);
		SharedPreferences MyHttpPerson = getSharedPreferences("phone_number",
				Activity.MODE_PRIVATE);
		phone_number = MyHttpPerson.getString("phone_number", "");
	}

	private void initBack() {
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LostRecord.this.finish();
			}
		});
	}

	private void init(final ArrayList<PersonData> apk_list) {
		if (adapter == null) {

			autolistview.setInterface((ILoadListener) this);
			autolistview.setInterface((IReflashListener) this);
			adapter = new RecordAdapter(LostRecord.this, apk_list);

			adapter.setInterface((Commentrecord) this);
			adapter.setInterface((Sharerecord) this);
			adapter.setInterface((DeleteMessagerecord) this);
			autolistview.setOnItemClickListener(this);
			autolistview.setAdapter(adapter);

		} else {
			adapter.onDateChange(apk_list);
		}
	}

	@Override
	public void onReflash() {
		apk_list = new ArrayList<PersonData>();
		HttpRecord httpRecord = new HttpRecord(phone_number, true);
		httpRecord.submitAsyncHttpClientGet();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			public void run() {
				SharedPreferences mySharedPreferencestoken = MyApplication
						.getAppContext().getSharedPreferences("RECORD",
								Activity.MODE_PRIVATE);
				// 使用getString方法获得value，注意第2个参数是value的默认值
				int RECORD = mySharedPreferencestoken.getInt("RECORD", 0);
				if (RECORD == 1) {
					Toast.makeText(LostRecord.this, "刷新成功", Toast.LENGTH_SHORT)
							.show();
					AddLFRecordMessage.AddPersonDatas("lostrecord", apk_list,
							true);
					init(apk_list);
					// 通知listview 刷新数据完毕；
					autolistview.reflashComplete();
				} else {
					Toast.makeText(LostRecord.this, "刷新失败", Toast.LENGTH_SHORT)
							.show();
					autolistview.reflashComplete();
				}

			}
		}, 2000);

	}

	@Override
	public void onLoad() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				init(apk_list);
				// 通知listview 刷新数据完毕；
				autolistview.loadComplete();
				Toast.makeText(LostRecord.this, "已加载全部", Toast.LENGTH_SHORT)
						.show();
			}
		}, 2000);
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
		Intent intent = new Intent(LostRecord.this,
				ListViewMessageDetails.class);
		intent.putExtras(bundle);
		LostRecord.this.startActivity(intent);

	}

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

		Intent intent = new Intent(LostRecord.this,
				ListViewMessageDetails.class);
		intent.putExtras(bundle);
		LostRecord.this.startActivity(intent);

	}

	@Override
	public void onshare(int position) {
		// TODO Auto-generated method stub

		showShare(false, null, false, position);

	}

	private void initImagePath() {
		try {
			String cachePath = cn.sharesdk.framework.utils.R.getCachePath(
					LostRecord.this, null);
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
		// Log.i("TEST_IMAGE path ==>>>", TEST_IMAGE);
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
		UIHandler.sendMessage(msg, LostRecord.this);
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

		Toast.makeText(LostRecord.this, text, Toast.LENGTH_SHORT).show();
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
	private void showShare(boolean silent, String platform,
			boolean captureView, int position) {

		// System.out.println("第2点");
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
		oks.setImageUrl(LostRecord.this.TEST_IMAGE_URL);
		oks.setFilePath(LostRecord.this.TEST_IMAGE);
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

		// System.out.println("第3点");
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
		String label = getResources().getString(R.string.app_name);
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				String text = "Customer Logo -- ShareSDK "
						+ ShareSDK.getSDKVersionName();
				Toast.makeText(LostRecord.this, text, Toast.LENGTH_SHORT)
						.show();
			}
		};
		// oks.setCustomerLogo(enableLogo, disableLogo, label, listener);

		// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);
		// oks.addHiddenPlatform(TencentWeibo.NAME);

		// 为EditPage设置一个背景的View
		// oks.setEditPageBackground(getPage());
		// System.out.println("第4点");
		// 启动分享GUI
		oks.show(LostRecord.this);

	}

	@Override
	public void ondeletemessage(int position) {
		// TODO Auto-generated method stub
		ShowPickDialog(position);

	}

	private void ShowPickDialog(final int position) {
		// TODO Auto-generated method stub
		new MyAlertDialog(LostRecord.this).builder().setTitle("提示！")
				.setMsg("你是真的真的不要我了么?")
				.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(View v) {

						PersonData arraydatas = apk_list.get(position);
						String id = arraydatas.getId();
						String app_id = arraydatas.getapp_Id();

						new DeleteData(id);

						int _id = Integer.valueOf(app_id).intValue();
						Log.i("app_id", "" + _id);
						MySQLiteMethodDetails.deleteCord_Db("lostrecord", _id);

						apk_list.remove(position);
						adapter.notifyDataSetChanged();

					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				}).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			LostRecord.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

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
				Toast.makeText(LostRecord.this, "刷新失败", Toast.LENGTH_SHORT)
						.show();
				// Log.i("HttpRecord", "失败失败！！！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
				// TODO Auto-generated method stub
				// Log.i("HttpRecord", "成功成功!!!!");
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
								ccommitperson);
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
							apk_list = new ArrayList<PersonData>();
							AddLFRecordMessage.AddPersonDatas("lostrecord",
									apk_list, true);
							if (apk_list.size() == 0) {
								no_data_cord.setVisibility(View.VISIBLE);
								autolistview.setVisibility(View.GONE);
							} else {
								no_data_cord.setVisibility(View.GONE);
								autolistview.setVisibility(View.VISIBLE);
							}

							ShareSDK.initSDK(LostRecord.this);
							ShareSDK.setConnTimeout(20000);
							ShareSDK.setReadTimeout(20000);

							new Thread() {
								public void run() {

									TEST_IMAGE_URL = "http://t1.qpic.cn/mblogpic/db90a049c73e9ff8aea0/2000";
									initImagePath();
									initTestText();

									UIHandler.sendEmptyMessageDelayed(1, 100,
											LostRecord.this);
								}
							}.start();

							dialog.dismiss();
							initBack();
							init(apk_list);

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
