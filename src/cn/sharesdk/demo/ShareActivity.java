//package cn.sharesdk.demo;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.HashMap;
//
//import m.framework.network.NetworkHelper;
//import net.loonggg.fragment.R;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.framework.utils.UIHandler;
//import cn.sharesdk.onekeyshare.OnekeyShare;
//import cn.sharesdk.onekeyshare.OnekeyShareTheme;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Message;
//import android.os.Handler.Callback;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.Toast;
//
//public class ShareActivity extends Activity implements Callback,
//		OnClickListener {
//
//	private boolean shareFromQQLogin = false;
//	public static String TEST_IMAGE;
//	public static String TEST_IMAGE_URL;
//	private static final String FILE_NAME = "pic_lovely_cats.jpg";
//	public static HashMap<Integer, String> TEST_TEXT;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
////		setContentView(R.layout.share);
//		ShareSDK.initSDK(this);
//		ShareSDK.setConnTimeout(20000);
//		ShareSDK.setReadTimeout(20000);
//		// 控件初始化
//		share = (Button) findViewById(R.id.share);
//		share.setOnClickListener(this);
//
//		new Thread() {
//			public void run() {
//				// 分享的图片
//
//				TEST_IMAGE_URL = "http://f1.sharesdk.cn/imgs/2014/05/21/oESpJ78_533x800.jpg";
//				initImagePath();
//				initTestText();
//				UIHandler.sendEmptyMessageDelayed(1, 100, ShareActivity.this);
//			}
//		}.start();
//
//	}
//
//	private void initImagePath() {
//		try {
//			String cachePath = cn.sharesdk.framework.utils.R.getCachePath(this,
//					null);
//			TEST_IMAGE = cachePath + FILE_NAME;
//			File file = new File(TEST_IMAGE);
//			if (!file.exists()) {
//				file.createNewFile();
//				// Bitmap pic = BitmapFactory.decodeResource(getResources(),
//				// R.drawable.pic);
//				FileOutputStream fos = new FileOutputStream(file);
//				// pic.compress(CompressFormat.JPEG, 100, fos);
//				fos.flush();
//				fos.close();
//			}
//		} catch (Throwable t) {
//			t.printStackTrace();
//			TEST_IMAGE = null;
//		}
//		Log.i("TEST_IMAGE path ==>>>", TEST_IMAGE);
//	}
//
//	@SuppressLint("UseSparseArrays")
//	private void initTestText() {
//		TEST_TEXT = new HashMap<Integer, String>();
//		try {
//			NetworkHelper network = new NetworkHelper();
//			String resp = network.httpGet("http://mob.com/Assets/snsplat.json",
//					null, null);
//			JSONObject json = new JSONObject(resp);
//			int status = json.optInt("status");
//			if (status == 200) {
//				JSONArray democont = json.optJSONArray("democont");
//				if (democont != null && democont.length() > 0) {
//					for (int i = 0, size = democont.length(); i < size; i++) {
//						JSONObject plat = democont.optJSONObject(i);
//						if (plat != null) {
//							int snsplat = plat.optInt("snsplat", -1);
//							String cont = plat.optString("cont");
//							TEST_TEXT.put(snsplat, cont);
//						}
//					}
//				}
//			}
//		} catch (Throwable t) {
//			t.printStackTrace();
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.share: {
//			// 直接分享
//
//			System.out.println("第一点");
//			showShare(true, null, false);
//		}
//			break;
//		}
//
//	}
//
//	public boolean handleMessage(Message msg) {
//		switch (msg.what) {
//		case 1: {
//			// menu.triggerItem(MainAdapter.GROUP_DEMO, MainAdapter.ITEM_DEMO);
//		}
//			break;
//		case 2: {
//			// String text = getString(R.string.receive_rewards, msg.arg);
//			// Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//		}
//			break;
//		}
//		return false;
//	}
//
//	/** 将action转换为String */
//	public static String actionToString(int action) {
//		switch (action) {
//		case Platform.ACTION_AUTHORIZING:
//			return "ACTION_AUTHORIZING";
//		case Platform.ACTION_GETTING_FRIEND_LIST:
//			return "ACTION_GETTING_FRIEND_LIST";
//		case Platform.ACTION_FOLLOWING_USER:
//			return "ACTION_FOLLOWING_USER";
//		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
//			return "ACTION_SENDING_DIRECT_MESSAGE";
//		case Platform.ACTION_TIMELINE:
//			return "ACTION_TIMELINE";
//		case Platform.ACTION_USER_INFOR:
//			return "ACTION_USER_INFOR";
//		case Platform.ACTION_SHARE:
//			return "ACTION_SHARE";
//		default: {
//			return "UNKNOWN";
//		}
//		}
//	}
//
//	public void onComplete(Platform plat, int action,
//			HashMap<String, Object> res) {
//
//		Message msg = new Message();
//		msg.arg1 = 1;
//		msg.arg2 = action;
//		msg.obj = plat;
//		UIHandler.sendMessage(msg, this);
//	}
//
//	public void onCancel(Platform palt, int action) {
//		Message msg = new Message();
//		msg.arg1 = 3;
//		msg.arg2 = action;
//		msg.obj = palt;
//		UIHandler.sendMessage(msg, this);
//	}
//
//	public void onError(Platform palt, int action, Throwable t) {
//		t.printStackTrace();
//
//		Message msg = new Message();
//		msg.arg1 = 2;
//		msg.arg2 = action;
//		msg.obj = palt;
//		UIHandler.sendMessage(msg, this);
//	}
//
//	public boolean handleMessage2(Message msg) {
//		Platform plat = (Platform) msg.obj;
//		String text = actionToString(msg.arg2);
//		switch (msg.arg1) {
//		case 1: {
//			// 成功
//			text = plat.getName() + " completed at " + text;
//		}
//			break;
//		case 2: {
//			// 失败
//			text = plat.getName() + " caught error at " + text;
//		}
//			break;
//		case 3: {
//			// 取消
//			text = plat.getName() + " canceled at " + text;
//		}
//			break;
//		}
//
//		Toast.makeText(ShareActivity.this, text, Toast.LENGTH_SHORT).show();
//		return false;
//	}
//
//	// 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）
//	/**
//	 * ShareSDK集成方法有两种</br>
//	 * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
//	 * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br> 请看“ShareSDK
//	 * 使用说明文档”，SDK下载目录中 </br> 或者看网络集成文档
//	 * http://wiki.mob.com/Android_%E5%BF%AB%E9%
//	 * 80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
//	 * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
//	 *
//	 *
//	 * 平台配置信息有三种方式： 1、在我们后台配置各个微博平台的key
//	 * 2、在代码中配置各个微博平台的key，http://mob.com/androidDoc
//	 * /cn/sharesdk/framework/ShareSDK.html
//	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
//	 */
//	private void showShare(boolean silent, String platform, boolean captureView) {
//
//	
//		System.out.println("第2点");
//		OnekeyShare oks = new OnekeyShare();
//		// 关闭sso授权
//		oks.disableSSOWhenAuthorize();
//
//		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
//		// oks.setNotification(R.drawable.ic_launcher,
//		// getString(R.string.app_name)); // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		oks.setTitle(getString(R.string.evenote_title));
//		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		oks.setTitleUrl("http://123.56.150.30:8000/alive/id=57/");
//		// text是分享文本，所有平台都需要这个字段
//		oks.setText(getString(R.string.share_content));
//		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
//		// url仅在微信（包括好友和朋友圈）中使用
//		oks.setUrl("http://123.56.150.30:8000/alive/id=57/");
//		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		oks.setComment("我的地盘，我主宰！");
//		// site是分享此内容的网站名称，仅在QQ空间使用
//		oks.setSite(getString(R.string.app_name));
//		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		oks.setSiteUrl("http://http://123.56.150.30:8000/alive/id=57/");
//		oks.setImageUrl(ShareActivity.TEST_IMAGE_URL);
//		oks.setFilePath(ShareActivity.TEST_IMAGE);
//		oks.setLatitude(23.056081f);
//		oks.setLongitude(113.385708f);
//		oks.setSilent(silent);
//		oks.setShareFromQQAuthSupport(shareFromQQLogin);
//		String theme = "classic";
//		if (OnekeyShareTheme.SKYBLUE.toString().toLowerCase().equals(theme)) {
//			oks.setTheme(OnekeyShareTheme.SKYBLUE);
//		} else {
//			oks.setTheme(OnekeyShareTheme.CLASSIC);
//		}
//
//		if (platform != null) {
//			oks.setPlatform(platform);
//		}
//
//		System.out.println("第3点");
//		// 令编辑页面显示为Dialog模式
//		oks.setDialogMode();
//
//		// 在自动授权时可以禁用SSO方式
//		// if(!CustomShareFieldsPage.getBoolean("enableSSO", true))
//		oks.disableSSOWhenAuthorize();
//
//		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
//		// oks.setCallback(new OneKeyShareCallback());
//
//		// 去自定义不同平台的字段内容
//		// oks.setShareContentCustomizeCallback(new
//		// ShareContentCustomizeDemo());
//
//		// 去除注释，演示在九宫格设置自定义的图标
//		// Bitmap enableLogo = BitmapFactory.decodeResource(getResources(),
//		// R.drawable.ic_launcher);
//		// Bitmap disableLogo = BitmapFactory.decodeResource(getResources(),
//		// R.drawable.sharesdk_unchecked);
//		String label = getResources().getString(R.string.app_name);
//		OnClickListener listener = new OnClickListener() {
//			public void onClick(View v) {
//				String text = "Customer Logo -- ShareSDK "
//						+ ShareSDK.getSDKVersionName();
//				Toast.makeText(ShareActivity.this, text,
//						Toast.LENGTH_SHORT).show();
//			}
//		};
//		// oks.setCustomerLogo(enableLogo, disableLogo, label, listener);
//
//		// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
//		// oks.addHiddenPlatform(SinaWeibo.NAME);
//		// oks.addHiddenPlatform(TencentWeibo.NAME);
//
//		// 为EditPage设置一个背景的View
//		// oks.setEditPageBackground(getPage());
//		System.out.println("第4点");
//		// 启动分享GUI
//		oks.show(this);
//
//	
//	}
//
//}
