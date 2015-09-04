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
	// �ӵ�½��ȡ�����ĵ绰����
	String name;
	String phone_number;
	String token;
	String password;

	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * ����ƴ��������ListView�����������
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
					// ʵ����SharedPreferences.Editor���󣨵ڶ�����
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// ��putString�ķ�����������
					editor.putString("Home_page", location);
					// �ύ��ǰ����
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
				// �������󵽷�����
				AsyncHttpClient client = new AsyncHttpClient();
				String url = UrlPath.patchApi;
				// �����������
				RequestParams params = new RequestParams();
				params.put("operation", "change_others");
				params.put("password", password);
				params.put("token", token);
				params.put("phone_number", phone_number);
				params.put("name", name);
				params.put("location", location);

				// ִ��post����
				client.post(url, params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {

						System.out.println("�ɹ�");// ��ӡ
						// String i = new String(responseBody);
						System.out.print("statusCode" + statusCode);

						handler.sendEmptyMessage(0);

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						System.out.print("�޸�ʧ��");

						System.out.print("statusCode" + statusCode);

					}
				});
				// TODO Auto-generated method stub

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
