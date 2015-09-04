package app.example.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sortlistview.CharacterParser;
import com.example.sortlistview.PinyinComparator;
import com.example.sortlistview.SideBar;
import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.example.sortlistview.SortAdapter;
import com.example.sortlistview.SortModel;

public class Province extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private String kind = null;
	private String change = null;
	private String from = null;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseprovince);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		kind = intent.getStringExtra("kind");
		from = intent.getStringExtra("from");

		if (from.equals("register")) {
			change = intent.getStringExtra("register");
		} else if (from.equals("Setting")) {
			change = intent.getStringExtra("Setting");
		} else if (from.equals("Home_page")) {
			change = intent.getStringExtra("Home_page");
		}
		
		initViews();
		backmethod();


	}

	private void backmethod() {
		backButton = (ImageButton) findViewById(R.id.chooseprovince_back);

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				back();
			}
		});

	}

	private void back() {
		if (change.equals("register")) {
			Intent intent = new Intent(Province.this, SchoolOrCity.class);

			Province.this.startActivity(intent);

			Province.this.finish();
			Province.this.overridePendingTransition(
					R.anim.in_from_left, R.anim.out_to_right);
		} else if (change.equals("Setting")) {
			Intent intent = new Intent(Province.this,
					AccountSettingActivity.class);

			Province.this.startActivity(intent);

			Province.this.finish();
			Province.this.overridePendingTransition(
					R.anim.in_from_left, R.anim.out_to_right);
		} else if (change.equals("Home_page")) {
			Intent intent = new Intent(Province.this, MainActivity.class);

			Province.this.startActivity(intent);

			Province.this.finish();
			Province.this.overridePendingTransition(
					R.anim.in_from_left, R.anim.out_to_right);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			back();
		}

		return super.onKeyDown(keyCode, event);
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

		sortListView = (ListView) findViewById(R.id.listView1);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String province = ((SortModel) adapter.getItem(position))
						.getName();
				// ����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
				if (change.equals("register")) {
					if ("school".equals(kind)) {
						Intent intent = new Intent(Province.this, School.class);
						intent.putExtra("province", province);
						startActivity(intent);
					} else if ("society".equals(kind)) {
						Intent intent = new Intent(Province.this, City.class);
						intent.putExtra("province", province);
						startActivity(intent);
						Toast.makeText(getApplication(), province,
								Toast.LENGTH_SHORT).show();
					}
				} else if (change.equals("Setting")) {
					if ("school".equals(kind)) {
						Intent intent = new Intent(Province.this,
								SchoolChange.class);
						intent.putExtra("province", province);

						intent.putExtra("from_from", change);

						startActivity(intent);
					} else if ("society".equals(kind)) {
						Intent intent = new Intent(Province.this,
								CityChange.class);
						intent.putExtra("province", province);

						intent.putExtra("from_from", change);

						startActivity(intent);
						Toast.makeText(getApplication(), province,
								Toast.LENGTH_SHORT).show();
					}
				} else if (change.equals("Home_page")) {
					if ("school".equals(kind)) {
						Intent intent = new Intent(Province.this,
								SchoolChange.class);
						intent.putExtra("province", province);

						intent.putExtra("from_from", change);

						startActivity(intent);
					} else if ("society".equals(kind)) {
						Intent intent = new Intent(Province.this,
								CityChange.class);
						intent.putExtra("province", province);

						intent.putExtra("from_from", change);

						startActivity(intent);
						Toast.makeText(getApplication(), province,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(Province.this, "��ת��������", Toast.LENGTH_LONG)
							.show();
				}

			}

		});

		SourceDateList = filledData(getResources().getStringArray(
				R.array.province));

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

}
