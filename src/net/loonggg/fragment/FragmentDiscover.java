package net.loonggg.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import m.framework.network.NetworkHelper;
import net.loonggg.utils.AddDiscoverMessageData;
import net.loonggg.utils.AutoListView;
import net.loonggg.utils.AutoListView.ILoadListener;
import net.loonggg.utils.AutoListView.IReflashListener;
import net.loonggg.utils.CommentsClicent_Clicent;
import net.loonggg.utils.DateUtil;
import net.loonggg.utils.DateUtil.DateFormatWrongException;
import net.loonggg.utils.DiscoverMessageAdapter;
import net.loonggg.utils.DiscoverMessageAdapter.Commentdiscover;
import net.loonggg.utils.DiscoverMessageAdapter.DeleteMessagediscover;
import net.loonggg.utils.DiscoverMessageAdapter.PersonHeaderdiscover;
import net.loonggg.utils.DiscoverMessageAdapter.Sharediscover;
import net.loonggg.utils.DiscoverMessageData;
import net.loonggg.utils.MyAlertDialog;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import app.example.Anim.CustomProgressDialog;
import app.example.Delete.DeleteNewsMessageData;
import app.example.Url.UrlPath;
import app.example.activity.DiscoverMesssageDetails;
import app.example.activity.PersonalPage;
import app.example.application.MyApplication;
import app.example.netserver.HttpComments;
import app.example.netserver.HttpNewsMessage;
import app.example.netserver.HttpNewsMessageLoading;
import app.example.netserver.HttpNewsMessagePerson;
import app.example.netserver.MySQLiteMethodDetails;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class FragmentDiscover extends Fragment implements IReflashListener,
		ILoadListener, OnItemClickListener, Commentdiscover, Callback,
		DeleteMessagediscover, Sharediscover, PersonHeaderdiscover {
	String id, information, commitperson, title, latlng, image1, image2, time,
			image3, sharenumber, commentsnumber, followingnumber;
	JSONArray followers;

	private int i;
	View view1;

	private DiscoverMessageAdapter discoverMessageAdapter;
	private AutoListView listview;
	private int num = 5;
	
	boolean flag = false;
	private int HttpNewsMessage;
	
	private ArrayList<DiscoverMessageData> discover_message = new ArrayList<DiscoverMessageData>();

	// ����
	/**************************************************/
	private boolean shareFromQQLogin = false;

	private CustomProgressDialog dialog;
	public static String TEST_IMAGE;
	public static String TEST_IMAGE_URL;
	private static final String FILE_NAME = "pic_lovely_cats.jpg";
	public static HashMap<Integer, String> TEST_TEXT;

	private LinearLayout layout_net;
	private LinearLayout layout_data;
	private Button button_net;
	private Button button_data;
	Context context;

	/**************************************************/
	FragmentDiscover(Context context) {
		this.context = context;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view1 = inflater.inflate(R.layout.fragment_list, null);
		layout_net = (LinearLayout) view1.findViewById(R.id.no_net_view);
		layout_data = (LinearLayout) view1.findViewById(R.id.no_data_view);
		button_net = (Button) view1.findViewById(R.id.no_net_setting);
		button_data = (Button) view1.findViewById(R.id.no_data_setting);
		listview = (AutoListView) view1.findViewById(R.id.fragment_listview);
		if (NetManager.isOpenNetwork(getActivity())) {

			layout_net.setVisibility(View.GONE);
			layout_data.setVisibility(View.GONE);
			dialog = new CustomProgressDialog(getActivity(), "���ڼ�����",
					R.anim.frame);
			dialog.show();

			NetData();

		} else {
			listview.setVisibility(View.GONE);
			layout_data.setVisibility(View.GONE);
			layout_net.setVisibility(View.VISIBLE);
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

	private void showList(final ArrayList<DiscoverMessageData> discover_message) {
		if (discoverMessageAdapter == null) {

			listview.setInterface((ILoadListener) this);
			listview.setInterface((IReflashListener) this);

			discoverMessageAdapter = new DiscoverMessageAdapter(
					(Activity) context, discover_message);
			discoverMessageAdapter.setInterface((Commentdiscover) this);
			discoverMessageAdapter.setInterface((DeleteMessagediscover) this);
			discoverMessageAdapter.setInterface((Sharediscover) this);

			discoverMessageAdapter.setInterface((PersonHeaderdiscover) this);
			listview.setAdapter(discoverMessageAdapter);
			listview.setOnItemClickListener(this);
			
		} else {
			discoverMessageAdapter.onDateChange(discover_message);
		}
	}

	private void NetData() {
		MySQLiteMethodDetails.EmptyTable("NO");
		submitAsyncHttpClientGet(0, 5);
	}

	@Override
	public void onReflash() {
		MySQLiteMethodDetails.EmptyTable("NO");
		listview.setEnabled(false);
		discover_message = new ArrayList<DiscoverMessageData>();

		HttpNewsMessage httpNewsMessage = new HttpNewsMessage(0, 5);
		httpNewsMessage.submitAsyncHttpClientGet();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				
					
					SharedPreferences mySharedPreferencestoken = MyApplication
							.getAppContext().getSharedPreferences(
									"HttpNewsMessage", Activity.MODE_PRIVATE);
					// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
					int HttpNewsMessage = mySharedPreferencestoken.getInt(
							"HttpNewsMessage", 0);

					if (HttpNewsMessage> 0) {
						CommentsClicent_Clicent.CommentsClicent();
						Toast.makeText((Activity)context, "ˢ�³ɹ�",
								Toast.LENGTH_SHORT).show();
						AddDiscoverMessageData.AddiscoverMessageDatas(
								HttpNewsMessage, 0, discover_message);
						// ֪ͨ������ʾ
						showList(discover_message);
					} else if (HttpNewsMessage == 0) {
						Toast.makeText((Activity)context, "�Ѽ���ȫ��������",
								Toast.LENGTH_SHORT).show();
					}else if (HttpNewsMessage == -1) {
						Toast.makeText((Activity)context, "ˢ��ʧ��",
								Toast.LENGTH_SHORT).show();
					}
					
					// ֪ͨlistview ˢ��������ϣ�
					listview.reflashComplete();
//					Log.i("��ϲ", "ˢ�³ɹ�");
					flag = true;
					listview.setEnabled(true);
			
}
			
		}, 1500);

	}

	@Override
	public void onLoad() {
		MySQLiteMethodDetails.EmptyTable("NO");
		if (flag) {
			num = 5;

			flag = false;
		}
		HttpNewsMessageLoading httpNewsMessageLoading = new HttpNewsMessageLoading(
				num, num + 5);
		httpNewsMessageLoading.submitAsyncHttpClientGet();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				
				// ��ȡ��������// �ӵ�¼��ȡ����token
				SharedPreferences mySharedPreferencestoken = MyApplication
						.getAppContext()
						.getSharedPreferences("HttpNewsMessageLoading",
								Activity.MODE_PRIVATE);
				// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
				int HttpNewsMessageLoading = mySharedPreferencestoken.getInt(
						"HttpNewsMessageLoading", 0);
				System.out.println(HttpNewsMessageLoading);
				if (HttpNewsMessageLoading >0) {
					CommentsClicent_Clicent.CommentsClicent();
					
					Toast.makeText((Activity)context, "ˢ�³ɹ�",
							Toast.LENGTH_SHORT).show();
					AddDiscoverMessageData.AddiscoverMessageDatas(
							HttpNewsMessageLoading, 1, discover_message);
					
					showList(discover_message);
					
					
					num = num + 5;
				} else if (HttpNewsMessageLoading == 0) {
					Toast.makeText((Activity)context, "�Ѽ���ȫ��������",
							Toast.LENGTH_SHORT).show();
				}else if (HttpNewsMessageLoading == -1) {
					Toast.makeText((Activity)context, "����ʧ��",
							Toast.LENGTH_SHORT).show();
				}

				
				listview.loadComplete();
				

			}
		}, 1500);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		DiscoverMessageData discoverMessageData = discover_message
				.get(position - 1);
		String id = discoverMessageData.getId();
		String title = discoverMessageData.getTitle();
		String losttime = discoverMessageData.getTime();

		String lostinformation = discoverMessageData.getInformation();
		String commitperson = discoverMessageData.getCommitPerson();
		String followingnumber = discoverMessageData.getFollowingNumber();
		String looknumber = discoverMessageData.getShareNumber();
		String personname = discoverMessageData.getPersonname();
		String personportrait = discoverMessageData.getPersonportrait();
		String image1 = discoverMessageData.getImage1();
		String image2 = discoverMessageData.getImage2();
		String image3 = discoverMessageData.getImage3();
		String personlocations = discoverMessageData.getPersonlocation();
		Bundle bundle = new Bundle();
		bundle.putString("personlocations", personlocations);
		bundle.putString("id", id);
		bundle.putString("title", title);
		bundle.putString("time", losttime);
		bundle.putString("information", lostinformation);
		bundle.putString("commitperson", commitperson);
		bundle.putString("followingnumber", followingnumber);
		bundle.putString("looknumber", looknumber);
		bundle.putString("personname", personname);
		bundle.putString("personportrait", personportrait);
		bundle.putString("image1", image1);
		bundle.putString("image2", image2);
		bundle.putString("image3", image3);

		Intent intent = new Intent(getActivity(), DiscoverMesssageDetails.class);
		intent.putExtras(bundle);
		getActivity().startActivity(intent);

	}

	@Override
	public void ondeletemessage(int position) {
		// TODO Auto-generated method stub
		ShowPickDialog(position);

	}

	private void ShowPickDialog(final int position) {
		// TODO Auto-generated method stub

		new MyAlertDialog(getActivity()).builder().setTitle("��ʾ��")
				.setMsg("���������Ĳ�Ҫ����ô?")
				.setPositiveButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(View v) {
						DiscoverMessageData discoverMessageData = discover_message
								.get(position);
						String id = discoverMessageData.getId();
						DeleteNewsMessageData deleteNewsMessageData = new DeleteNewsMessageData(
								id);
//						Log.i("ɾ��ɾ��������", "" + deleteNewsMessageData);
						discover_message.remove(position);
						discoverMessageAdapter.notifyDataSetChanged();

					}
				}).setNegativeButton("ȡ��", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				}).show();

	}

	// ����

	/**
	 * ************************************************************
	 */
	@Override
	public void oncomment(int position) {
		// TODO Auto
		DiscoverMessageData discoverMessageData = discover_message
				.get(position);
		String id = discoverMessageData.getId();
		String title = discoverMessageData.getTitle();
		String losttime = discoverMessageData.getTime();

		String lostinformation = discoverMessageData.getInformation();
		String commitperson = discoverMessageData.getCommitPerson();
		String followingnumber = discoverMessageData.getFollowingNumber();
		String looknumber = discoverMessageData.getShareNumber();

		String personname = discoverMessageData.getPersonname();
		String personportrait = discoverMessageData.getPersonportrait();
		String image1 = discoverMessageData.getImage1();
		String image2 = discoverMessageData.getImage2();
		String image3 = discoverMessageData.getImage3();

		String personlocations = discoverMessageData.getPersonlocation();
		Bundle bundle = new Bundle();
		bundle.putString("personlocations", personlocations);

		bundle.putString("id", id);
		bundle.putString("title", title);
		bundle.putString("time", losttime);
		bundle.putString("information", lostinformation);
		bundle.putString("commitperson", commitperson);
		bundle.putString("followingnumber", followingnumber);
		bundle.putString("looknumber", looknumber);
		bundle.putString("personname", personname);
		bundle.putString("personportrait", personportrait);
		bundle.putString("image1", image1);
		bundle.putString("image2", image2);
		bundle.putString("image3", image3);
		Intent intent = new Intent(getActivity(), DiscoverMesssageDetails.class);
		intent.putExtras(bundle);
		getActivity().startActivity(intent);

	}

	// ���ͷ������������ҳ
	@Override
	public void onPersonHeader(int position) {
		// TODO Auto-generated method stub
		DiscoverMessageData discoverMessageData = discover_message
				.get(position);

		String personname = discoverMessageData.getPersonname();
		String personportrait = discoverMessageData.getPersonportrait();
		String personlocation = discoverMessageData.getPersonlocation();
		String commitperson = discoverMessageData.getCommitPerson();

		SharedPreferences mySharedPreferences = getActivity()
				.getSharedPreferences("Personal_Personal",
						Activity.MODE_PRIVATE);
		// ʵ����SharedPreferences.Editor���󣨵ڶ�����
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		// ��putString�ķ�����������
		editor.putString("personal_commitperson", commitperson);
		editor.putString("personal_personname", personname);
		editor.putString("personal_personportrait", personportrait);
		editor.putString("personal_personlocation", personlocation);
		// �ύ��ǰ����
		editor.commit();
		Intent intent = new Intent(getActivity(), PersonalPage.class);

		getActivity().startActivity(intent);

	}

	@Override
	public void onshare(int position) {
		// TODO Auto-generated method stub

		showShare(false, null, false, position);

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

	/** ��actionת��ΪString */
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
		UIHandler.sendMessage(msg, FragmentDiscover.this);
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
			// �ɹ�
			text = plat.getName() + " completed at " + text;
		}
			break;
		case 2: {
			// ʧ��
			text = plat.getName() + " caught error at " + text;
		}
			break;
		case 3: {
			// ȡ��
			text = plat.getName() + " canceled at " + text;
		}
			break;
		}

		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
		return false;
	}

	// ʹ�ÿ�ݷ�����ɷ����������ϸ�Ķ�λ��SDK��ѹĿ¼��Docs�ļ�����OnekeyShare���JavaDoc��
	/**
	 * ShareSDK���ɷ���������</br>
	 * 1����һ�������÷�ʽ����������onekeyshare��Ŀ��onekeyshare��Ŀ������mainlibs��</br>
	 * 2���ڶ����ǰ�onekeyshare��mainlibs���ɵ���Ŀ�У������Ӿ����õڶ��ַ�ʽ</br> �뿴��ShareSDK
	 * ʹ��˵���ĵ�����SDK����Ŀ¼�� </br> ���߿����缯���ĵ�
	 * http://wiki.mob.com/Android_%E5%BF%AB%E9%
	 * 80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
	 * 3������ʱ����sample���߱����ӵĻ�������copy��ȥ����proguard-project.txt�ļ���
	 * 
	 * 
	 * ƽ̨������Ϣ�����ַ�ʽ�� 1�������Ǻ�̨���ø���΢��ƽ̨��key
	 * 2���ڴ��������ø���΢��ƽ̨��key��http://mob.com/androidDoc
	 * /cn/sharesdk/framework/ShareSDK.html
	 * 3���������ļ������ã������������assets/ShareSDK.conf,
	 */
	private void showShare(boolean silent, String platform,
			boolean captureView, int position) {
		OnekeyShare oks = new OnekeyShare();
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name)); // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(getString(R.string.evenote_title));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		String Urlr = "http://123.56.150.30:8000/alive/86id=";
		String Urlt = Urlr + position + "/";
		oks.setTitleUrl(Urlt);
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		String share_content = "�׶�����һ�����У԰�ͳ��У���������������������ʧ��������Ϣ��APP��Ϊ�û��ṩ��Ч����ݵ�ʧ���������!"
				+ Urlt;
		oks.setText(share_content);
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl(Urlt);
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("�ҵĵ��̣������ף�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl(Urlt);
		oks.setImageUrl(FragmentDiscover.TEST_IMAGE_URL);
		oks.setFilePath(FragmentDiscover.TEST_IMAGE);
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

//		System.out.println("��3��");
		// ��༭ҳ����ʾΪDialogģʽ
		oks.setDialogMode();

		// ���Զ���Ȩʱ���Խ���SSO��ʽ
		// if(!CustomShareFieldsPage.getBoolean("enableSSO", true))
		oks.disableSSOWhenAuthorize();

		// ȥ��ע�ͣ����ݷ���Ĳ��������ͨ��OneKeyShareCallback�ص�
		// oks.setCallback(new OneKeyShareCallback());

		// ȥ�Զ��岻ͬƽ̨���ֶ�����
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());

		// ȥ��ע�ͣ���ʾ�ھŹ��������Զ����ͼ��
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

		// ȥ��ע�ͣ����ݷ���Ź����н���������΢������Ѷ΢��
		// oks.addHiddenPlatform(SinaWeibo.NAME);
		// oks.addHiddenPlatform(TencentWeibo.NAME);

		// ΪEditPage����һ��������View
		// oks.setEditPageBackground(getPage());
//		System.out.println("��4��");
		// ��������GUI
		oks.show(getActivity());
	}

	/****************************************************************/

	public void submitAsyncHttpClientGet(int start, int end) {
		// �����첽����˶���
		AsyncHttpClient client = new AsyncHttpClient();

		String urlr = UrlPath.newsGetApi;

		String url = urlr + "?" + "start=" + start + "&" + "end=" + end;
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				String json = msg.getData().getString("responseBody");
				try {

					JSONObject item = new JSONObject();
					JSONArray response = new JSONArray(json);

					int a = response.length();
					SharedPreferences mySharedPreferences = MyApplication
							.getAppContext().getSharedPreferences(
									"HttpNewsMessage", Activity.MODE_PRIVATE);
					// ʵ����SharedPreferences.Editor���󣨵ڶ�����
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// ��putString�ķ�����������
					editor.putInt("HttpNewsMessage", a);
					// �ύ��ǰ����
					editor.commit();

					for (i = 0; i < response.length(); i++) {
						item = response.getJSONObject(i);
						id = item.getString("id");

						try {
							time = DateUtil.toSimpleDate(item
									.getString("created"));
						} catch (DateFormatWrongException e) {
							e.printStackTrace();
						}
						HttpComments myHttpComments = new HttpComments(id,
								"news");
						myHttpComments.submitAsyncHttpClientGet();
						title = item.getString("title");
						information = item.getString("information");
						commitperson = item.getString("commit_person");
						String lostimagestart = item.getString("image1");
						sharenumber = item.getString("reading_number");
						followingnumber = item.getString("following_number");
						followers = item.getJSONArray("followers");
						commentsnumber = "1";
//						for (int i = 0; i < followers.length(); i++) {
//							String oneperson = followers.getString(i);
//							System.out.println("���ص����ˣ�����" + oneperson);
//							if (commitperson.equals(oneperson)) {
//								commentsnumber = "0";
//							}
//						}

						String pathpopu = UrlPath.host;
						image1 = pathpopu + lostimagestart;
						String lostimagestart1 = item.getString("image2");
						image2 = pathpopu + lostimagestart1;
						String lostimagestart3 = item.getString("image3");
						image3 = pathpopu + lostimagestart3;
						HttpNewsMessagePerson httpNewsMessagePerson = new HttpNewsMessagePerson(
								commitperson);
						httpNewsMessagePerson.submitAsyncHttpClientGet(i, id,
								time, information, commitperson, title, image1,
								image2, image3, sharenumber, commentsnumber,
								followingnumber);
						httpNewsMessagePerson.submitAsyncHttpClientGet();
					}
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {

							CommentsClicent_Clicent.CommentsClicent();
							SharedPreferences mySharedPreferencestoken = MyApplication
									.getAppContext().getSharedPreferences(
											"HttpNewsMessage",
											Activity.MODE_PRIVATE);
							// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ
							HttpNewsMessage = mySharedPreferencestoken.getInt(
									"HttpNewsMessage", 0);

							if (HttpNewsMessage == 0) {

								layout_data.setVisibility(View.VISIBLE);
								button_data
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {

												discover_message = new ArrayList<DiscoverMessageData>();
												HttpNewsMessage httpNewsMessage = new HttpNewsMessage(
														0, 5);
												httpNewsMessage
														.submitAsyncHttpClientGet();

												if (HttpNewsMessage != 0) {
													AddDiscoverMessageData
															.AddiscoverMessageDatas(
																	HttpNewsMessage,
																	0,
																	discover_message);
												} else if (HttpNewsMessage == 0) {
													Toast.makeText(
															getActivity(),
															"�������ݣ�",
															Toast.LENGTH_SHORT)
															.show();
												}

												showList(discover_message);
												// ֪ͨlistview ˢ��������ϣ�
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
									// �����ͼƬ

									// http://t1.qpic.cn/mblogpic/db90a049c73e9ff8aea0/2000//�׶���logo��ַ
									TEST_IMAGE_URL = "http://t1.qpic.cn/mblogpic/db90a049c73e9ff8aea0/2000";
									initImagePath();
									initTestText();
									UIHandler.sendEmptyMessageDelayed(1, 100,
											FragmentDiscover.this);
								}
							}.start();

							discover_message = new ArrayList<DiscoverMessageData>();

							if (HttpNewsMessage != 0) {

								AddDiscoverMessageData.AddiscoverMessageDatas(
										HttpNewsMessage, 0, discover_message);

							} else if (HttpNewsMessage == 0) {

								Toast.makeText(getActivity(), "�Ѽ���ȫ��������",
										Toast.LENGTH_SHORT).show();
							}
							dialog.dismiss();
							showList(discover_message);

						}
					}, 1500);
				}

				catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};

		// ����get�������
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Toast.makeText(getActivity(), "����ʧ��",
						Toast.LENGTH_SHORT).show();
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
		return;

	}

}
