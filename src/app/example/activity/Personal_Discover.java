package app.example.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import m.framework.network.NetworkHelper;
import net.loonggg.utils.AddDiscoverRecordMessage;
import net.loonggg.utils.DiscoverMessageData;
import net.loonggg.utils.PersonalDiscoverAdapter;
import net.loonggg.utils.PersonalDiscoverAdapter.Commentdiscover;
import net.loonggg.utils.PersonalDiscoverAdapter.Sharediscover;
import net.loonggg.utils.Personal_Else_ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.Toast;
import app.example.netserver.MySQLiteMethodDetails;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class Personal_Discover extends Activity implements OnItemClickListener,
		Commentdiscover, Callback, Sharediscover {

	private Personal_Else_ListView personallistview;
	private ImageButton personal_page_back;
	private PersonalDiscoverAdapter discoverMessageAdapter;

	private ArrayList<DiscoverMessageData> discover_message = new ArrayList<DiscoverMessageData>();

	// 分享
	/**************************************************/
	private boolean shareFromQQLogin = false;
	private LinearLayout no_data_personal;
	private LinearLayout no_net_personal;

	public static String TEST_IMAGE;
	public static String TEST_IMAGE_URL;
	private static final String FILE_NAME = "pic_lovely_cats.jpg";
	public static HashMap<Integer, String> TEST_TEXT;

	/**************************************************/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.personaldiscover);
		personallistview = (Personal_Else_ListView) findViewById(R.id.personal_edit_message);

		personal_page_back = (ImageButton) findViewById(R.id.personal_page_back);
		no_data_personal = (LinearLayout) findViewById(R.id.no_data_personal);
		no_net_personal = (LinearLayout) findViewById(R.id.no_net_personal);
		Click();
		SysApplication.getInstance().addActivity(this);

		ShareSDK.initSDK(Personal_Discover.this);
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
						Personal_Discover.this);
			}
		}.start();

		discover_message = new ArrayList<DiscoverMessageData>();

		AddDiscoverRecordMessage.AddDiscoverRecordMessageData(discover_message,
				false);
		if (discover_message.size()==0) {
			no_data_personal.setVisibility(View.VISIBLE);
			personallistview.setVisibility(View.GONE);
		}else {
			no_data_personal.setVisibility(View.GONE);
			personallistview.setVisibility(View.VISIBLE);
		}
		showList(discover_message);
	}

	private void showList(final ArrayList<DiscoverMessageData> discover_message) {
		if (discoverMessageAdapter == null) {

			discoverMessageAdapter = new PersonalDiscoverAdapter(
					Personal_Discover.this, discover_message);
			discoverMessageAdapter.setInterface((Commentdiscover) this);
			discoverMessageAdapter.setInterface((Sharediscover) this);

			personallistview.setAdapter(discoverMessageAdapter);
			
				personallistview.setOnItemClickListener(this);
		

		} else {
			discoverMessageAdapter.onDateChange(discover_message);
		}
	}

	private void Click() {

		personal_page_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MySQLiteMethodDetails.EmptyTable("discoverpersonal");
				Intent intent = new Intent(Personal_Discover.this,
						PersonalPage.class);
				Personal_Discover.this.startActivity(intent);
				finish();
				Personal_Discover.this.overridePendingTransition(
						R.anim.in_from_left, R.anim.out_to_right);
			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		DiscoverMessageData discoverMessageData = discover_message
				.get(position - 1);
		String id = discoverMessageData.getId();
		String time = discoverMessageData.getTime();
		String title = discoverMessageData.getTitle();
		String information = discoverMessageData.getInformation();
		String commitperson = discoverMessageData.getCommitPerson();
		String personname = discoverMessageData.getPersonname();
		String personportrait = discoverMessageData.getPersonportrait();
		String looknumber = discoverMessageData.getShareNumber();
		String followingNumber = discoverMessageData.getFollowingNumber();
		String image1 = discoverMessageData.getImage1();
		String image2 = discoverMessageData.getImage2();
		String image3 = discoverMessageData.getImage3();

		String personlocations = discoverMessageData.getPersonlocation();
		Bundle bundle = new Bundle();
		bundle.putString("personlocations", personlocations);

		bundle.putString("id", id);
		bundle.putString("title", title);
		bundle.putString("time", time);
		bundle.putString("information", information);
		bundle.putString("commitperson", commitperson);

		bundle.putString("looknumber", looknumber);
		bundle.putString("followingnumber", followingNumber);
		bundle.putString("personname", personname);
		bundle.putString("personportrait", personportrait);
		bundle.putString("image1", image1);
		bundle.putString("image2", image2);
		bundle.putString("image3", image3);

		Intent intent = new Intent(Personal_Discover.this,
				DiscoverMesssageDetails.class);
		intent.putExtras(bundle);
		Personal_Discover.this.startActivity(intent);

	}

	@Override
	public void oncomment(int position) {
		// TODO Auto
		DiscoverMessageData discoverMessageData = discover_message
				.get(position);
		String id = discoverMessageData.getId();
		String time = discoverMessageData.getTime();
		String title = discoverMessageData.getTitle();
		String information = discoverMessageData.getInformation();
		String commitperson = discoverMessageData.getCommitPerson();
		String looknumber = discoverMessageData.getShareNumber();
		String followingnumber = discoverMessageData.getFollowingNumber();
		String commentsnumber = discoverMessageData.getCommentsNumber();
		String personname = discoverMessageData.getPersonname();
		String personportrait = discoverMessageData.getPersonportrait();
		String image1 = discoverMessageData.getImage1();
		String image2 = discoverMessageData.getImage2();
		String image3 = discoverMessageData.getImage3();
		String personlocations = discoverMessageData.getPersonlocation();
		Bundle bundle = new Bundle();
		bundle.putString("personlocations", personlocations);

		bundle.putString("id", id);
		bundle.putString("time", time);
		bundle.putString("title", title);
		bundle.putString("information", information);
		bundle.putString("commitperson", commitperson);
		bundle.putString("looknumber", looknumber);
		bundle.putString("followingnumber", followingnumber);

		bundle.putString("commentsnumber", commentsnumber);
		bundle.putString("personname", personname);
		bundle.putString("personportrait", personportrait);
		bundle.putString("image1", image1);
		bundle.putString("image2", image2);
		bundle.putString("image3", image3);
		Intent intent = new Intent(Personal_Discover.this,
				DiscoverMesssageDetails.class);
		intent.putExtras(bundle);
		Personal_Discover.this.startActivity(intent);

	}

	@Override
	public void onshare(int position) {
		// TODO Auto-generated method stub

		showShare(false, null, false, position);

	}

	private void initImagePath() {
		try {
			String cachePath = cn.sharesdk.framework.utils.R.getCachePath(
					Personal_Discover.this, null);
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
		Log.i("TEST_IMAGE path ==>>>", TEST_IMAGE);
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
		UIHandler.sendMessage(msg, Personal_Discover.this);
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

		Toast.makeText(Personal_Discover.this, text, Toast.LENGTH_SHORT).show();
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

//		System.out.println("第2点");
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name)); // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.evenote_title));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		String Urlr = "http://123.56.150.30:8000/alive/86id=";
		String Urlt = Urlr + position + "/";
		oks.setTitleUrl(Urlt);
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
		oks.setImageUrl(Personal_Discover.this.TEST_IMAGE_URL);
		oks.setFilePath(Personal_Discover.this.TEST_IMAGE);
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

//		System.out.println("第3点");
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
				Toast.makeText(Personal_Discover.this, text, Toast.LENGTH_SHORT)
						.show();
			}
		};
		// oks.setCustomerLogo(enableLogo, disableLogo, label, listener);

		// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);
		// oks.addHiddenPlatform(TencentWeibo.NAME);

		// 为EditPage设置一个背景的View
		// oks.setEditPageBackground(getPage());
//		System.out.println("第4点");
		// 启动分享GUI
		oks.show(Personal_Discover.this);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			MySQLiteMethodDetails.EmptyTable("discoverpersonal");
			Intent intent = new Intent(Personal_Discover.this,
					PersonalPage.class);
			Personal_Discover.this.startActivity(intent);
			Personal_Discover.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
