package net.loonggg.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import m.framework.network.NetworkHelper;
import net.loonggg.utils.CheckNet;

import org.json.JSONArray;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import app.example.aboutus.AboutUsActivity;
import app.example.activity.FeedbackActivity;
import app.example.activity.LoginActvity;
import app.example.activity.SystemUpActivity;
import app.example.adapter.GridAdapterMine;
import app.example.updata.UpdateManger;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class FragmentMineUnlogin extends Fragment implements Callback {
	private Button loginButton;
	private View view;
	UpdateManger mUpdateManger;// ��������õ�

	/**************************************************/
	// ����
	private boolean shareFromQQLogin = false;
	public static String TEST_IMAGE;
	public static String TEST_IMAGE_URL;
	private static final String FILE_NAME = "pic_lovely_cats.jpg";
	public static HashMap<Integer, String> TEST_TEXT;

	/**************************************************/
	private int mScreenWidth;
	private float density;
	private RelativeLayout relative;
	private String version;// ���ص�ǰ�汾��
	private Float item_version;// �������汾��
	Context context;

	public FragmentMineUnlogin(Context context) {
		this.context = context;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = inflater.inflate(R.layout.fragment_mine_unlogin, container,
				false);

		/**********************************************/
		// ����
		ShareSDK.initSDK((Activity)context);
		ShareSDK.setConnTimeout(20000);
		ShareSDK.setReadTimeout(20000);

		new Thread() {
			public void run() {
				// http://t1.qpic.cn/mblogpic/db90a049c73e9ff8aea0/2000//�׶���logo��ַ
				TEST_IMAGE_URL = "http://t1.qpic.cn/mblogpic/db90a049c73e9ff8aea0/2000";
				initImagePath();
				initTestText();

				UIHandler.sendEmptyMessageDelayed(1, 100,
						FragmentMineUnlogin.this);
			}
		}.start();
		loginButton = (Button) view.findViewById(R.id.login_button);

		loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent loginIntent = new Intent(getActivity(),
						LoginActvity.class);
				startActivity(loginIntent);

			}
		});
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);// ��ȡ��Ļ�ֱ���

		mScreenWidth = dm.widthPixels;

		density = dm.density;

		relative = (RelativeLayout) view
				.findViewById(R.id.screen_hight_mine_unlogin);
		relative.getLayoutParams().height = mScreenWidth;
		GridView gridview = (GridView) view
				.findViewById(R.id.fragment_mine_unlogin_gridview);
		gridview.setAdapter(new GridAdapterMine(((Activity)context).getBaseContext()));
	
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// TODO Auto-generated method stub
				switch (arg2) {

				case 0:
				case 1:
				case 2:
				case 3:

					Intent loginIntent = new Intent(getActivity(),
							LoginActvity.class);
					startActivity(loginIntent);
					break;
				case 6:
					Intent feedbackIntent = new Intent(getActivity(),
							FeedbackActivity.class);

					startActivity(feedbackIntent);
					break;
				case 4:

					boolean net = CheckNet.detect(getActivity());

					if (net) {

						// ���������¼�
						showShare(false, null, false);

					} else {

						Toast.makeText(getActivity(), "���粻���ã��������磡",
								Toast.LENGTH_SHORT).show();
					}

					break;

				case 5:// ��ջ���

					Toast.makeText(getActivity(), "��������棡", Toast.LENGTH_SHORT)
							.show();

					break;
				case 7:// ϵͳ����

					// ϵͳ����
					Intent update = new Intent(getActivity(),
							SystemUpActivity.class);

					startActivity(update);

					break;
				case 8:// ��������
					Intent aboutus = new Intent(getActivity(),
							AboutUsActivity.class);
					startActivity(aboutus);

					break;

				default:
					break;
				}

			}
		});
		return view;
	}

	private void initImagePath() {
		try {
			String cachePath = cn.sharesdk.framework.utils.R.getCachePath(
					getActivity(), null);
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
		UIHandler.sendMessage(msg, FragmentMineUnlogin.this);
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
	@SuppressLint("SdCardPath")
	private void showShare(boolean silent, String platform, boolean captureView) {
		System.out.println("��2��");
		OnekeyShare oks = new OnekeyShare();
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name)); // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(getString(R.string.evenote_title));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
//		String Urlt = "http://123.56.150.30:8000/static/release/update/YiDiudiu.apk";
		String Urlt = "http://a.app.qq.com/o/simple.jsp?pkgname=tx.ydd.app";
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
		oks.setImageUrl(FragmentMineUnlogin.this.TEST_IMAGE_URL);
		oks.setFilePath(FragmentMineUnlogin.this.TEST_IMAGE);
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

	/**
	 * ��ȡ���ذ汾��
	 * 
	 * @return ��ǰӦ�õİ汾��
	 */
	public String getVersion() {

		try {
			PackageManager manager = getActivity().getPackageManager();
			PackageInfo info = manager.getPackageInfo(getActivity()
					.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}