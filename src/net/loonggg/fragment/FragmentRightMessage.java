package net.loonggg.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import m.framework.network.NetworkHelper;
import net.loonggg.utils.AddPersonData;
import net.loonggg.utils.AutoListView;
import net.loonggg.utils.AutoListView.ILoadListener;
import net.loonggg.utils.AutoListView.IReflashListener;
import net.loonggg.utils.CommentsClicent_Clicent;
import net.loonggg.utils.MyAlertDialog;
import net.loonggg.utils.MyBaseAdapter;
import net.loonggg.utils.MyBaseAdapter.Comment;
import net.loonggg.utils.MyBaseAdapter.DeleteMessage;
import net.loonggg.utils.MyBaseAdapter.Mappicture;
import net.loonggg.utils.MyBaseAdapter.PersonHeader;
import net.loonggg.utils.MyBaseAdapter.Phone;
import net.loonggg.utils.MyBaseAdapter.Share;
import net.loonggg.utils.MyBaseAdapter.ShortMessage;
import net.loonggg.utils.NetManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import app.example.Delete.DeleteData;
import app.example.Url.UrlPath;
import app.example.activity.ListViewMessageDetails;
import app.example.activity.PersonalPage;
import app.example.activity.SendMessage;
import app.example.application.MyApplication;
import app.example.netserver.HttpComments;
import app.example.netserver.HttpUI;
import app.example.netserver.HttpUtil;
import app.example.netserver.HttpUtilPerson;
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

public class FragmentRightMessage extends Fragment implements IReflashListener,
		ILoadListener, Phone, ShortMessage, Mappicture, OnItemClickListener,
		Comment, DeleteMessage, Share, Callback, PersonHeader {
	String id, what, losttime, lostlatitude, lostlangitude, kkind, information,
			commitperson, contactphone, sharenumber, commentsnumber,
			followingnumber, location, created, title, comments, latlng,
			image1, image2, image3;
	View view1;
	String phoneno;

	/**************************************************/
	private boolean shareFromQQLogin = false;
	public static String TEST_IMAGE;
	public static String TEST_IMAGE_URL;
	private static final String FILE_NAME = "pic_lovely_cats.jpg";
	public static HashMap<Integer, String> TEST_TEXT;
	/**************************************************/

	private MyBaseAdapter adapter;
	private AutoListView listview;
	private int num = 5;
	boolean b = false;
	boolean flag = false;
	private String city = null;
	private boolean first = false;
	private ArrayList<PersonData> apk_list = new ArrayList<PersonData>();
	private int HttpUtil;
	private LinearLayout layout_net;
	private LinearLayout layout_data;
	private Button button_net;
	private Button button_data;
	Context context;
	private String kind;
	private String newkind;

	public FragmentRightMessage(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view1 = inflater.inflate(R.layout.fragment_list, null, false);
		layout_net = (LinearLayout) view1.findViewById(R.id.no_net_view);
		layout_data = (LinearLayout) view1.findViewById(R.id.no_data_view);
		button_net = (Button) view1.findViewById(R.id.no_net_setting);
		button_data = (Button) view1.findViewById(R.id.no_data_setting);
		listview = (AutoListView) view1.findViewById(R.id.fragment_listview);

		if (NetManager.isOpenNetwork(getActivity())) {
			layout_net.setVisibility(View.GONE);
			layout_data.setVisibility(View.GONE);

			NetData();

		} else {
			layout_net.setVisibility(View.VISIBLE);
			layout_data.setVisibility(View.GONE);
			button_net.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = null;
					try {
						String sdkVersion = android.os.Build.VERSION.SDK;
						if (Integer.valueOf(sdkVersion) > 10) {
							intent = new Intent(
									android.provider.Settings.ACTION_SETTINGS);
						} else {
							intent = new Intent();
							ComponentName comp = new ComponentName(
									"com.android.settings",
									"com.android.settings.WirelessSettings");
							intent.setComponent(comp);
							intent.setAction("android.intent.action.VIEW");
						}
						getActivity().startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		}

		return view1;
	}

	private void showList(final ArrayList<PersonData> apk_list) {
		if (adapter == null) {
			listview.setInterface((ILoadListener) this);
			listview.setInterface((IReflashListener) this);
			adapter = new MyBaseAdapter((Activity) context, apk_list);
			adapter.setInterface((Phone) this);
			adapter.setInterface((DeleteMessage) this);
			adapter.setInterface((ShortMessage) this);
			adapter.setInterface((PersonHeader) this);
			adapter.setInterface((Mappicture) this);

			adapter.setInterface((Comment) this);
			adapter.setInterface((Share) this);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(this);
			first = true;
		} else {
			adapter.onDateChange(apk_list);
		}
	}

	private void NetData() {
		Bundle bundle = getArguments();
		newkind = bundle.getString("lostkind_from_fragmenthomepageright");
		if (newkind == null) {
			kind = "所有";
		} else {
			kind = newkind;
		}
		MySQLiteMethodDetails.EmptyTable("NO");
		SharedPreferences mySharedPreferencestoken = MyApplication
				.getAppContext().getSharedPreferences("Home_page",
						Activity.MODE_PRIVATE);
		city = mySharedPreferencestoken.getString("Home_page", "全国");
		submitAsyncHttpClientGet(0, 5, city, "find", kind);

	}

	@Override
	public void onReflash() {
		MySQLiteMethodDetails.EmptyTable("NO");
		listview.setEnabled(false);
		apk_list = new ArrayList<PersonData>();
		// Log.i("刷新", "刷新中");
		HttpUtil httpUtil = new HttpUtil(0, 5, city, "find", kind);
		 httpUtil.submitAsyncHttpClientGet();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				
					
					
					SharedPreferences mySharedPreferencestoken = MyApplication
							.getAppContext().getSharedPreferences(
									"HttpUtil_find", Activity.MODE_PRIVATE);
					// 使用getString方法获得value，注意第2个参数是value的默认值
					int Httpflash = mySharedPreferencestoken.getInt(
							"HttpUtil_find", 0);
					if (Httpflash > 0) {
						CommentsClicent_Clicent.CommentsClicent();
						Toast.makeText((Activity)context, "刷新成功", Toast.LENGTH_SHORT)
						.show();
						AddPersonData.AddPersonDatas("find", Httpflash, 0,
								apk_list, true);
						// 通知界面显示
						showList(apk_list);
						// 通知listview 刷新数据完毕；
					} else if (Httpflash == 0) {
						Toast.makeText((Activity)context, "已加载全部！！！",
								Toast.LENGTH_SHORT).show();

					}else if (Httpflash ==-1){
						Toast.makeText((Activity)context, "刷新失败", Toast.LENGTH_SHORT)
						.show();
					}
					
					listview.reflashComplete();

					flag = true;
					listview.setEnabled(true);
				
			}
		}, 1500);

	}

	@Override
	public void onLoad() {
		if (flag) {
			num = 5;

			flag = false;
		}

		HttpUI httpUI = new HttpUI(num, num + 5, city, "find", kind);

		httpUI.submitHttpClientGet();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				
				
				// 获取更多数据// 从登录获取令牌token
				SharedPreferences mySharedPreferencestoken = MyApplication
						.getAppContext().getSharedPreferences("HttpUI_find",
								Activity.MODE_PRIVATE);
				// 使用getString方法获得value，注意第2个参数是value的默认值
				int HttpUI = mySharedPreferencestoken.getInt("HttpUI_find", 0);
				// Log.i("HttpUI", "HttpUI:" + HttpUI);
				// getLoadData();
				if (HttpUI > 0) {
					CommentsClicent_Clicent.CommentsClicent();
					Toast.makeText((Activity)context, "加载成功",
							Toast.LENGTH_SHORT).show();
					AddPersonData.AddPersonDatas("find", HttpUI, 1, apk_list,
							true);
					// 更新listview显示；
					showList(apk_list);
					// 通知listview加载完毕
					
					num = num + 5;
					// 用于退出该页面时，清空SHareprefences保留的EditView 内容用到

				} else if (HttpUI == 0) {
					Toast.makeText((Activity)context, "已加载全部！！！",
							Toast.LENGTH_SHORT).show();
				}else if (HttpUI == 0) {
					Toast.makeText((Activity)context, "加载失败！",
							Toast.LENGTH_SHORT).show();
				}

				
				listview.loadComplete();
			

			}
		}, 1500);
	}

	@Override
	public void onphone(final String phone) {
		// TODO Auto-generated method stub

		new MyAlertDialog(getActivity()).builder().setTitle("提示！")
				.setMsg("确定要打电话给Ta:" + phone)
				.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + phone));
						startActivity(intent);

					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				}).show();

	}

	@Override
	public void onmessage(final String phone) {
		// TODO Auto-generated method stub
		new MyAlertDialog(getActivity()).builder().setTitle("提示！")
				.setMsg("确定要发短信给Ta:" + phone)
				.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								SendMessage.class);
						intent.putExtra("number", phone);
						getActivity().startActivity(intent);
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				}).show();

	}

	@Override
	public void ondeletemessage(int position) {
		// TODO Auto-generated method stub
		ShowPickDialog(position);

	}

	private void ShowPickDialog(final int position) {
		// TODO Auto-generated method stub
		new MyAlertDialog(getActivity()).builder().setTitle("提示！")
				.setMsg("你是真的真的不要我了么?")
				.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(View v) {

						PersonData arraydatas = apk_list.get(position);
						String id = arraydatas.getId();
						DeleteData deleteData = new DeleteData(id);
						// Log.i("删除删除！！！", "" + deleteData);
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
	public void onmappicture(int position) {
		// TODO Auto-generated method stub
		PersonData arraydatas = apk_list.get(position);

		String title = arraydatas.getTitle();
		String time = arraydatas.getTime();
		String langitude = arraydatas.getLangitude();
		String latitude = arraydatas.getLatitude();
		String kind = arraydatas.getKind();
		String information = arraydatas.getInformation();
		String location = arraydatas.getLocation();
		String created = arraydatas.getCreated();

		Intent intent = new Intent(getActivity(), BaiduShiWu.class);
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
		getActivity().startActivity(intent);

	}

	// 评论
	/****************************************************************/
	@Override
	public void oncomment(int position) {
		// TODO Auto
		PersonData arraydatas = apk_list.get(position);
		String id = arraydatas.getId();

		String title = arraydatas.getTitle();
		String losttime = arraydatas.getTime();
		String lostlangitude = arraydatas.getLangitude();
		String lostlatitude = arraydatas.getLatitude();
		String lostkind = arraydatas.getKind();
		String lostinformation = arraydatas.getInformation();
		String commitperson = arraydatas.getCommitPerson();
		String sharenumber = arraydatas.getShareNumber();
		String commentsnumber = arraydatas.getCommentsNumber();
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
		bundle.putString("sharenumber", sharenumber);
		bundle.putString("commentsnumber", commentsnumber);
		bundle.putString("followingnumber", followingnumber);
		bundle.putString("location", location);
		bundle.putString("created", created);
		bundle.putString("comments", comments);
		bundle.putString("personname", personname);
		bundle.putString("personportrait", personportrait);
		bundle.putString("image1", image1);
		bundle.putString("image2", image2);
		bundle.putString("image3", image3);
		Intent intent = new Intent(getActivity(), ListViewMessageDetails.class);
		intent.putExtras(bundle);
		getActivity().startActivity(intent);

	}

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
		String sharenumber = arraydatas.getShareNumber();
		String commentsnumber = arraydatas.getCommentsNumber();
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

		bundle.putString("sharenumber", sharenumber);
		bundle.putString("commentsnumber", commentsnumber);
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

		Intent intent = new Intent(getActivity(), ListViewMessageDetails.class);
		intent.putExtras(bundle);
		getActivity().startActivity(intent);

	}

	@Override
	public void onshare(int id) {
		// TODO Auto-generated method stub

		showShare(false, null, false, id);

	}

	private void initImagePath() {
		try {
			String cachePath = cn.sharesdk.framework.utils.R.getCachePath(
					MyApplication.getAppContext(), null);
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
		UIHandler.sendMessage(msg, FragmentRightMessage.this);
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

		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
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

		System.out.println("第3点position" + position);
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
		oks.setImageUrl(FragmentRightMessage.TEST_IMAGE_URL);
		oks.setFilePath(FragmentRightMessage.TEST_IMAGE);
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
				Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
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
		oks.show(getActivity());

	}

	@Override
	public void onPersonHeader(int position) {
		// TODO Auto-generated method stub
		PersonData arraydatas = apk_list.get(position);

		String personname = arraydatas.getPersonname();
		String personportrait = arraydatas.getPersonportrait();
		String personlocation = arraydatas.getPersonlocation();
		String commitperson = arraydatas.getCommitPerson();

		SharedPreferences mySharedPreferences = getActivity()
				.getSharedPreferences("Personal_Personal",
						Activity.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		// 用putString的方法保存数据
		editor.putString("personal_commitperson", commitperson);
		editor.putString("personal_personname", personname);
		editor.putString("personal_personportrait", personportrait);
		editor.putString("personal_personlocation", personlocation);
		// 提交当前数据
		editor.commit();
		Intent intent = new Intent(getActivity(), PersonalPage.class);

		getActivity().startActivity(intent);

	}

	public void submitAsyncHttpClientGet(int start, int end, String address,
			final String httpkind, String somekind) {
		// 创建异步请求端对象

		AsyncHttpClient client = new AsyncHttpClient();

		String urlr = UrlPath.messagesGetApi;

		String url = urlr + "?" + "location=" + address + "&" + "start="
				+ start + "&" + "end=" + end + "&what=" + httpkind + "&kind="
				+ somekind;
		// System.out.println("获取数据开始");
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				String json = msg.getData().getString("responseBody");
				try {

					JSONObject item = new JSONObject();
					JSONArray response = new JSONArray(json);

					if (httpkind.equals("lost")) {
						SharedPreferences mySharedPreferences = MyApplication
								.getAppContext().getSharedPreferences(
										"HttpUtil_lost", Activity.MODE_PRIVATE);
						int a = response.length();
						SharedPreferences.Editor editor = mySharedPreferences
								.edit();

						editor.putInt("HttpUtil_lost", a);

						editor.commit();
					} else if (httpkind.equals("find")) {
						SharedPreferences mySharedPreferences = MyApplication
								.getAppContext().getSharedPreferences(
										"HttpUtil_find", Activity.MODE_PRIVATE);
						int a = response.length();

						SharedPreferences.Editor editor = mySharedPreferences
								.edit();
						editor.putInt("HttpUtil_find", a);
						editor.commit();
					}

					for (int i = 0; i < response.length(); i++) {
						item = response.getJSONObject(i);

						id = item.getString("id");

						HttpComments myHttpComments = new HttpComments(id,
								"message");
						myHttpComments.submitAsyncHttpClientGet();
						/*********************************************/
						what = item.getString("what");
						String losttimestart = item.getString("time");
						latlng = item.getString("latlng");
						kkind = item.getString("kind");
						title = item.getString("title");
						information = item.getString("information");
						commitperson = item.getString("commit_person");

						contactphone = item.getString("contact_phone");
						sharenumber = item.getString("share_number");
						followingnumber = item.getString("reading_number");
						commentsnumber = item.getString("comments_number");
						location = item.getString("location");

						String lostimagestart = item.getString("image1");
						String pathpopu = UrlPath.host;
						image1 = pathpopu + lostimagestart;
						String lostimagestart1 = item.getString("image2");
						image2 = pathpopu + lostimagestart1;
						String lostimagestart3 = item.getString("image3");
						image3 = pathpopu + lostimagestart3;
						String createdstart = item.getString("created");

						String losttimemiddle = losttimestart.substring(0,
								losttimestart.length());
						losttime = losttimemiddle.replace("T", " ");

						String createdmiddle = createdstart.substring(0,
								losttimestart.length());
						created = createdmiddle.replace("T", " ");

						String[] sp = { "titude:", "gitude:" };// 关键字数组
						for (int k = 0; k < sp.length; k++) {
							String value = sp[k];// 取出一个关键字

							String ee = "," + value + ",";// 关键字前后加逗号
							latlng = latlng.replaceAll(value, ee);//
							// 这句很关键，replaceAll有返回值，如不把返回值给vv，vv的值不会变
						}
						String[] result = latlng.split(",");// 按照逗号分割

						lostlatitude = result[2];
						lostlangitude = result[5];
						comments = null;

						HttpUtilPerson httpPersonMessage = new HttpUtilPerson(
								commitperson);
						httpPersonMessage.submitAsyncHttpClientGet(i, id, what,
								losttime, lostlatitude, lostlangitude, kkind,
								information, commitperson, contactphone,
								sharenumber, commentsnumber, followingnumber,
								location, created, title, comments, image1,
								image2, image3);
						httpPersonMessage.submitAsyncHttpClientGet();

					}
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							CommentsClicent_Clicent.CommentsClicent();
							SharedPreferences mySharedPreferencestoken = MyApplication
									.getAppContext().getSharedPreferences(
											"HttpUtil_find",
											Activity.MODE_PRIVATE);
							// 使用getString方法获得value，注意第2个参数是value的默认值
							HttpUtil = mySharedPreferencestoken.getInt(
									"HttpUtil_find", 0);
							if (HttpUtil == 0) {

								layout_data.setVisibility(View.VISIBLE);

								button_data
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {

												apk_list = new ArrayList<PersonData>();
												HttpUtil httpUtil = new HttpUtil(
														0, 5, city, "find",
														kind);
												httpUtil.submitAsyncHttpClientGet();

												if (HttpUtil != 0) {
													AddPersonData
															.AddPersonDatas(
																	"find",
																	HttpUtil,
																	0,
																	apk_list,
																	true);
												} else if (HttpUtil == 0) {
													Toast.makeText(
															getActivity(),
															"暂无数据！",
															Toast.LENGTH_SHORT)
															.show();
												}

												showList(apk_list);
												// 通知listview 刷新数据完毕；
												listview.reflashComplete();

												flag = true;

											}
										});
							}
							ShareSDK.initSDK((Activity) context);
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
											FragmentRightMessage.this);
								}
							}.start();

							apk_list = new ArrayList<PersonData>();
							if (HttpUtil> 0) {
								AddPersonData.AddPersonDatas("find", HttpUtil,
										0, apk_list, true);
							} else if (HttpUtil == 0) {
								Toast.makeText(getActivity(), "已加载全部！！！",
										Toast.LENGTH_SHORT).show();
							}

							showList(apk_list);
						}
					}, 1500);
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}
		};

		// 发送get请求对象
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				String iString = new String(arg2);
				Bundle bundle = new Bundle();
				bundle.putString("responseBody", iString);
				Message message = new Message();
				message.setData(bundle);
				handler.sendMessage(message);

			}

		});

	}
}
