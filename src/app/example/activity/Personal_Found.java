package app.example.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import m.framework.network.NetworkHelper;
import net.loonggg.utils.AddLFRecordMessage;
import net.loonggg.utils.PersonalBaseAdapter;
import net.loonggg.utils.PersonalBaseAdapter.Comment;
import net.loonggg.utils.PersonalBaseAdapter.Mappicture;
import net.loonggg.utils.PersonalBaseAdapter.Phone;
import net.loonggg.utils.PersonalBaseAdapter.Share;
import net.loonggg.utils.PersonalBaseAdapter.ShortMessage;
import net.loonggg.utils.Personal_Else_ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import app.example.netserver.MySQLiteMethodDetails;
import app.example.netserver.PersonData;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.example.baidumaplocation.BaiduShiWu;

public class Personal_Found extends Activity implements Phone, ShortMessage,
		Mappicture, OnItemClickListener, Comment, Share, Callback {

	private String commitperson;

	private ImageButton personal_page_back;

	String phoneno;
	/**************************************************/
	private boolean shareFromQQLogin = false;
	public static String TEST_IMAGE;
	public static String TEST_IMAGE_URL;
	private static final String FILE_NAME = "pic_lovely_cats.jpg";
	public static HashMap<Integer, String> TEST_TEXT;

	/**************************************************/
	private PersonalBaseAdapter adapter;
	private Personal_Else_ListView listview;

	boolean b = false;
	boolean flag = false;

	private ArrayList<PersonData> apk_list = new ArrayList<PersonData>();

	private Activity ACTIVITY = Personal_Found.this;

	private LinearLayout no_net_personal;

	private LinearLayout no_data_personal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.personalfound);
		apk_list = new ArrayList<PersonData>();
		start();
		receiver();
		Click();

		AddLFRecordMessage.AddPersonDatas("findpersonal", apk_list, false);
		if (apk_list.size()==0) {
			no_data_personal.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
		}else {
			no_data_personal.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		}
		ShareSDK.initSDK(ACTIVITY);
		ShareSDK.setConnTimeout(20000);
		ShareSDK.setReadTimeout(20000);

		new Thread() {
			public void run() {
				// �����ͼƬ
				// http://t1.qpic.cn/mblogpic/db90a049c73e9ff8aea0/2000//�׶���logo��ַ
				TEST_IMAGE_URL = "http://t1.qpic.cn/mblogpic/db90a049c73e9ff8aea0/2000";
				initImagePath();
				initTestText();

				UIHandler.sendEmptyMessageDelayed(1, 100, Personal_Found.this);
			}
		}.start();

		showList(apk_list);

	}

	/**
	 * ��ʼ������
	 */
	public void start() {
		personal_page_back = (ImageButton) findViewById(R.id.personal_page_back);
		listview = (Personal_Else_ListView) findViewById(R.id.personal_edit_message);
		no_data_personal = (LinearLayout) findViewById(R.id.no_data_personal);
		no_net_personal = (LinearLayout) findViewById(R.id.no_net_personal);
	}

	public void receiver() {

		Intent intent = getIntent();
		commitperson = intent.getStringExtra("commitperson");

	}

	private void Click() {

		personal_page_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MySQLiteMethodDetails.EmptyTable("findpersonal");
				Intent intent = new Intent(Personal_Found.this,
						PersonalPage.class);
				Personal_Found.this.startActivity(intent);
				finish();
				ACTIVITY.overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);
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

			listview.setOnItemClickListener(this);

		} else {
			adapter.onDateChange(apk_list);
		}
	}

	@Override
	public void onphone(final String phone) {

		Builder dialog = new AlertDialog.Builder(ACTIVITY)
				.setIcon(R.drawable.button_green)
				.setTitle("����绰��")
				.setMessage("��ȷ��Ҫ����" + phone + "���������")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {

						dialog.dismiss();

						phoneno = phone;
						Intent intent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + phoneno));
						startActivity(intent);

					}
				})
				.setNegativeButton("����", new DialogInterface.OnClickListener() {
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
				.setTitle("���Ͷ��ŵ���" + phone)

				.setMessage("��ȷ��Ҫ���Ͷ���Ta��")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
						Intent intent = new Intent(ACTIVITY, SendMessage.class);
						intent.putExtra("number", phone);
						ACTIVITY.startActivity(intent);
						ACTIVITY.finish();
					}
				})
				.setNegativeButton("����", new DialogInterface.OnClickListener() {
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

	// ����
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
		UIHandler.sendMessage(msg, Personal_Found.this);
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

		Toast.makeText(ACTIVITY, text, Toast.LENGTH_SHORT).show();
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
	@SuppressLint("DefaultLocale")
	private void showShare(boolean silent, String platform,
			boolean captureView, int position) {

		System.out.println("��2��position" + position);
		OnekeyShare oks = new OnekeyShare();
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name)); // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(getString(R.string.evenote_title));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		String Urlr = "http://123.56.150.30:8000/alive/id=";
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
		oks.setImageUrl(Personal_Found.TEST_IMAGE_URL);
		oks.setFilePath(Personal_Found.TEST_IMAGE);
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

		System.out.println("��3��");
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

		// ȥ��ע�ͣ����ݷ���Ź����н���������΢������Ѷ΢��
		// oks.addHiddenPlatform(SinaWeibo.NAME);
		// oks.addHiddenPlatform(TencentWeibo.NAME);

		// ΪEditPage����һ��������View
		// oks.setEditPageBackground(getPage());
		System.out.println("��4��");
		// ��������GUI
		oks.show(ACTIVITY);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// MySQLiteMethodDetails.EmptyTable("findpersonal");
			Intent intent = new Intent(Personal_Found.this, PersonalPage.class);
			Personal_Found.this.startActivity(intent);
			Personal_Found.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
