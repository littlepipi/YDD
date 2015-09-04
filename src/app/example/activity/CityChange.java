package app.example.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;
import app.example.netserver.MyHttpPerson;

import com.example.sortlistview.CharacterParser;
import com.example.sortlistview.PinyinComparator;
import com.example.sortlistview.SideBar;
import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.example.sortlistview.SortAdapter;
import com.example.sortlistview.SortModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CityChange extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private String province = null;
	private String from_from = null;
	String location;
	private ImageButton backButton;
	// 从登陆获取过来的电话号码
	String name;
	String phone_number;
	String token;
	String password;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosecity);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		province = intent.getStringExtra("province");
		from_from = intent.getStringExtra("from_from");
		initViews();
		backmethod();
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
				location = city.toString().trim();
				if (from_from.equals("Setting")) {
					Toast.makeText(getApplication(), city, Toast.LENGTH_SHORT)
							.show();

					SharedPreferences mySharedPreferencesphone = getSharedPreferences(
							"phone_number", Activity.MODE_PRIVATE);

					phone_number = mySharedPreferencesphone
							.getString("phone_number", "").toString().trim();

					SharedPreferences mySharedPreferancePassword = getSharedPreferences(
							"phone_password", MODE_PRIVATE);

					password = mySharedPreferancePassword
							.getString("password", "").toString().trim();

					SharedPreferences MyHttpPerson = getSharedPreferences(
							"MyHttpPerson", MODE_PRIVATE);

					name = MyHttpPerson.getString("personname", "").toString()
							.trim();

					SharedPreferences mySharedPreferencestoken = getSharedPreferences(
							"token", Activity.MODE_PRIVATE);

					token = mySharedPreferencestoken.getString("token", "")
							.toString().trim();

					AsyncHttpClientPost(name, location, token, phone_number,
							password, "change_others");
				} else if (from_from.equals("Home_page")) {

					SharedPreferences mySharedPreferences = MyApplication
							.getAppContext().getSharedPreferences("Home_page",
									Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// 用putString的方法保存数据
					editor.putString("Home_page", location);
					// 提交当前数据
					editor.commit();
					Intent intent = new Intent(CityChange.this,
							MainActivity.class);
					startActivity(intent);
					CityChange.this.finish();

				}

			}

			private void AsyncHttpClientPost(String name,
					final String location, String token,
					final String phone_number, String password, String operation) {
				final Handler handler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						if (msg.what == 0) {

							Intent intent = new Intent(CityChange.this,
									AccountSettingActivity.class);

							intent.putExtra("location", location);

							intent.putExtra("flag_location", true);

							startActivity(intent);

							CityChange.this.finish();

							MyHttpPerson myHttpPerson = new MyHttpPerson(
									phone_number);
							myHttpPerson.submitAsyncHttpClientGet();
						}
					}
				};
				// 发送请求到服务器
				AsyncHttpClient client = new AsyncHttpClient();
				String url = UrlPath.patchApi;
				// 创建请求参数
				RequestParams params = new RequestParams();
				params.put("operation", "change_others");
				params.put("password", password);
				params.put("token", token);
				params.put("phone_number", phone_number);
				params.put("name", name);
				params.put("location", location);

				// 执行post方法
				client.post(url, params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {

						System.out.println("成功");// 打印
						// String i = new String(responseBody);
						System.out.print("statusCode" + statusCode);

						handler.sendEmptyMessage(0);

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						System.out.print("修改失败");

						System.out.print("statusCode" + statusCode);

					}
				});
				// TODO Auto-generated method stub

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
				CityChange.this.finish();

				CityChange.this.overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			CityChange.this.finish();

			CityChange.this.overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);
		}
		return super.onKeyDown(keyCode, event);
	}
}
