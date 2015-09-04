package app.example.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tx.ydd.app.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.example.application.MyApplication;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import com.example.sortlistview.CharacterParser;
import com.example.sortlistview.PinyinComparator;
import com.example.sortlistview.SideBar;
import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.example.sortlistview.SortAdapter;
import com.example.sortlistview.SortModel;

public class City extends Activity implements Callback {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private String province = null;
	private ImageButton backButton;
	// �ӵ�½��ȡ�����ĵ绰����
	String name;
	String phone_number;
	String token;
	String password;
	String location;
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * ����ƴ��������ListView�����������
	 */
	private PinyinComparator pinyinComparator;

	// ��д�Ӷ���SDKӦ�ú�̨ע��õ���APPKEY
	private static String APPKEY;
	// ��д�Ӷ���SDKӦ�ú�̨ע��õ���APPSECRET
	private static String APPSECRET;
	String a = "5aec12823819";
	String b = "b42dafb1c491a7655cdf907b2c608f05 ";

	private Dialog pd;
	private String choose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosecity);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		province = intent.getStringExtra("province");
		initViews();
		backmethod();

		APPKEY = a.toString().trim();
		APPSECRET = b.toString().trim();
		// isEmpty�жϱ����Ƿ��ʼ��
		if (TextUtils.isEmpty(APPKEY) || TextUtils.isEmpty(APPSECRET)) {
		} else {
			initSDK();
		}

	}

	private void initSDK() {
		// ��ʼ������SDK
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		final Handler handler = new Handler(this);
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// ע��ص������ӿ�
		SMSSDK.registerEventHandler(eventHandler);

	}

	protected void onDestroy() {
		// ���ٻص������ӿ�
		SMSSDK.unregisterAllEventHandler();

		super.onDestroy();
	}

	@Override
	protected void onPause() {

		super.onPause();

	}

	public boolean handleMessage(Message msg) {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}

		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
			// ����ע��ɹ��󣬷���MainActivity,Ȼ����ʾ�º���
			if (result == SMSSDK.RESULT_COMPLETE) {
				Toast.makeText(this, R.string.smssdk_user_info_submited,
						Toast.LENGTH_SHORT).show();

			} else {
				((Throwable) data).printStackTrace();
			}
		} else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT) {
			if (result == SMSSDK.RESULT_COMPLETE) {

			} else {
				((Throwable) data).printStackTrace();
			}
		}
		return false;
	}

	private void initViews() {
		// ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// �����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		switch (province) {
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.anhui));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.fujian));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.gansu));
			break;
		case "�㶫":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.guangdong));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.guangxi));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.guizhou));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hainan));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.henan));
			break;
		case "�ӱ�":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hebei));
			break;
		case "������":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.heilongjiang));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hubei));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hunan));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.jilin));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.jiangsu));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.jiangxi));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.liaoning));
			break;
		case "���ɹ�":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.neimenggu));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.ningxia));
			break;
		case "�ຣ":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.qinghai));
			break;
		case "ɽ��":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.shandong));
			break;
		case "ɽ��":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.yishanxi));
			break;
		case "�Ĵ�":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.sichuan));
			break;
		case "̨��":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.taiwan));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.xizang));
			break;
		case "�½�":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.xinjiang));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.yunnan));
			break;
		case "�㽭":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.zhejiang));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.sanshanxi));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.beijing));
			break;
		case "���":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.tianjin));
			break;
		case "�Ϻ�":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.shanghai));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.chongqing));
			break;
		default:
			break;
		}

		sortListView = (ListView) findViewById(R.id.listView3);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
				String city = ((SortModel) adapter.getItem(position)).getName();

				RegisterPage registerPage = new RegisterPage();
				registerPage.show(MyApplication.getAppContext());

				// ʵ����SharedPreferences���󣨵�һ����
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"city", Activity.MODE_PRIVATE);
				// ʵ����SharedPreferences.Editor���󣨵ڶ�����
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// ��putString�ķ�����������
				editor.putString("city", city);
				// �ύ��ǰ����
				editor.commit();

				Toast.makeText(getApplication(), city, Toast.LENGTH_SHORT)
						.show();

			}

		});
		// ����a-z��������Դ����
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);

		// �������������ֵ�ĸı�����������

	}

	/**
	 * ΪListView�������
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// ����ת����ƴ��
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}
	private void backmethod() {
		backButton = (ImageButton) findViewById(R.id.choosecity_back);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				City.this.finish();

				City.this.overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			City.this.finish();

			City.this.overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);
		}
		return super.onKeyDown(keyCode, event);
	}


}
