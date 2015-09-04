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
	// 从登陆获取过来的电话号码
	String name;
	String phone_number;
	String token;
	String password;
	String location;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY;
	// 填写从短信SDK应用后台注册得到的APPSECRET
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
		// isEmpty判断变量是否初始化
		if (TextUtils.isEmpty(APPKEY) || TextUtils.isEmpty(APPSECRET)) {
		} else {
			initSDK();
		}

	}

	private void initSDK() {
		// 初始化短信SDK
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
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);

	}

	protected void onDestroy() {
		// 销毁回调监听接口
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
			// 短信注册成功后，返回MainActivity,然后提示新好友
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
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		switch (province) {
		case "安徽":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.anhui));
			break;
		case "福建":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.fujian));
			break;
		case "甘肃":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.gansu));
			break;
		case "广东":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.guangdong));
			break;
		case "广西":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.guangxi));
			break;
		case "贵州":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.guizhou));
			break;
		case "海南":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hainan));
			break;
		case "河南":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.henan));
			break;
		case "河北":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hebei));
			break;
		case "黑龙江":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.heilongjiang));
			break;
		case "湖北":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hubei));
			break;
		case "湖南":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.hunan));
			break;
		case "吉林":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.jilin));
			break;
		case "江苏":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.jiangsu));
			break;
		case "江西":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.jiangxi));
			break;
		case "辽宁":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.liaoning));
			break;
		case "内蒙古":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.neimenggu));
			break;
		case "宁夏":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.ningxia));
			break;
		case "青海":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.qinghai));
			break;
		case "山东":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.shandong));
			break;
		case "山西":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.yishanxi));
			break;
		case "四川":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.sichuan));
			break;
		case "台湾":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.taiwan));
			break;
		case "西藏":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.xizang));
			break;
		case "新疆":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.xinjiang));
			break;
		case "云南":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.yunnan));
			break;
		case "浙江":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.zhejiang));
			break;
		case "陕西":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.sanshanxi));
			break;
		case "北京":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.beijing));
			break;
		case "天津":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.tianjin));
			break;
		case "上海":
			SourceDateList = filledData(getResources().getStringArray(
					R.array.shanghai));
			break;
		case "重庆":
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
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				String city = ((SortModel) adapter.getItem(position)).getName();

				RegisterPage registerPage = new RegisterPage();
				registerPage.show(MyApplication.getAppContext());

				// 实例化SharedPreferences对象（第一步）
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"city", Activity.MODE_PRIVATE);
				// 实例化SharedPreferences.Editor对象（第二步）
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// 用putString的方法保存数据
				editor.putString("city", city);
				// 提交当前数据
				editor.commit();

				Toast.makeText(getApplication(), city, Toast.LENGTH_SHORT)
						.show();

			}

		});
		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);

		// 根据输入框输入值的改变来过滤搜索

	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
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
