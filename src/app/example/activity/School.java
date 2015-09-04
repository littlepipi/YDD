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

public class School extends Activity implements Callback {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private String province = null;
	private ImageButton backButton;


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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseschool);
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
					R.array.anhuischool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.fujianschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.gansuschool));
			break;
		case "�㶫":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.guangdongschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.guangxischool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.guizhouschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hainanschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.henanschool));
			break;
		case "�ӱ�":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hebeischool));
			break;
		case "������":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.heilongjiangschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hubeischool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hunanschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.jilinschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.jiangsuschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.jiangxischool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.liaoningschool));
			break;
		case "���ɹ�":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.neimengguschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.ningxiaschool));
			break;
		case "�ຣ":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.qinghaischool));
			break;
		case "ɽ��":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.shandongschool));
			break;
		case "ɽ��":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.yishanxischool));
			break;
		case "�Ĵ�":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.sichuanschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.xizangschool));
			break;
		case "�½�":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.xinjiangschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.yunnanschool));
			break;
		case "�㽭":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.zhejiangschool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.sanshanxischool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.beijingschool));
			break;
		case "���":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.tianjinschool));
			break;
		case "�Ϻ�":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.shanghaischool));
			break;
		case "����":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.chongqingschool));
			break;
		default:
			break;
		}

		sortListView = (ListView) findViewById(R.id.listView2);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
				String school = ((SortModel) adapter.getItem(position))
						.getName();
				// Intent intent = new Intent(School.this, Result.class);
				// intent.putExtra("city", city);
				// startActivity(intent);

				RegisterPage registerPage = new RegisterPage();
				registerPage.show(MyApplication.getAppContext());

				// ����绰���봫�ݸ�ʧ������
				// ʵ����SharedPreferences���󣨵�һ����
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"school", Activity.MODE_PRIVATE);
				// ʵ����SharedPreferences.Editor���󣨵ڶ�����
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// ��putString�ķ�����������
				editor.putString("school", school);
				// �ύ��ǰ����
				editor.commit();

				Toast.makeText(getApplication(), school, Toast.LENGTH_SHORT)
						.show();

				{

				}

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
		backButton = (ImageButton) findViewById(R.id.chooseschool_back);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				School.this.finish();

				School.this.overridePendingTransition(
						R.anim.in_from_left, R.anim.out_to_right);

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			School.this.finish();

			School.this.overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}
