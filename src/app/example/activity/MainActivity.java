package app.example.activity;

import java.util.ArrayList;
import java.util.List;

import net.loonggg.fragment.FragmentHomePage;
import net.loonggg.fragment.FragmentMessage;
import net.loonggg.fragment.FragmentMine;
import net.loonggg.fragment.FragmentMineUnlogin;
import net.loonggg.fragment.NewFragmentDiscover;
import net.loonggg.utils.System_time_New;
import net.loonggg.utils.popup;
import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import app.example.service.Myservice;

public class MainActivity extends FragmentActivity {

	// private Object[] objects = { R.drawable.lost, R.drawable.found,
	// R.drawable.more, "失物信息", "招领信息", "发现信息" };
	private RadioGroup radiogroup;
	private FragmentManager fragmentManager;
	private RadioButton radioButtonhomepage, radioButtondiscover,
			radioButtonmessage, radioButtonmine;
	private List<RadioButton> radioButtonlist = new ArrayList<RadioButton>();
	private String lostkind_from_kindleft = null;

	private String lost_from_kindleft = null;
	private String DOORHOMEPAGE = "1";
	private String foundkind_from_kindright = null;

	private String found_from_kindright = null;
	// private int Status;

	private String settingback = null;
	private String token;

	private ImageView plus;

	private popup menuWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		init();
		setFragmentIndicator();

		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		token = mySharedPreferencestoken.getString("token", "").toString()
				.trim();

		SysApplication.getInstance().addActivity(this);
		radiogroup = (RadioGroup) findViewById(R.id.activity_main_radiogroup);

		plus = (ImageView) findViewById(R.id.plus);

		plus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this,
						MainActivityBackground.class);
				Bundle bundle = new Bundle();
				bundle.putString("token", token);
				intent.putExtras(bundle);
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.rotate, R.anim.rotateout);
			}
		});

		Intent intent = getIntent();
		lostkind_from_kindleft = intent
				.getStringExtra("lostkind_from_result_mainactivity");
		lost_from_kindleft = intent
				.getStringExtra("lost_from_result_mainactivity");

		foundkind_from_kindright = intent
				.getStringExtra("foundkind_from_result_mainactivity");
		found_from_kindright = intent
				.getStringExtra("found_from_result_mainactivity");
		String cityString = intent.getStringExtra("city2");
		fragmentManager = getSupportFragmentManager();
		FragmentHomePage fragmentHomePage = new FragmentHomePage();
		Bundle bundle = new Bundle();
		bundle.putString("lostkind_from_mainactivity", lostkind_from_kindleft);

		bundle.putString("lost_from_mainactivity", lost_from_kindleft);
		bundle.putString("foundkind_from_mainactivity",
				foundkind_from_kindright);
		bundle.putString("found_from_mainactivity", found_from_kindright);
		bundle.putString("city3", cityString);
		fragmentHomePage.setArguments(bundle);

		fragmentManager.beginTransaction()
				.replace(R.id.activity_main_page, fragmentHomePage).commit();
		// setCheck(0);
		Intent settingIntent = getIntent();
		settingback = settingIntent.getStringExtra("settingback");

		if ("settingback".equals(settingback)) {

			fragmentManager
					.beginTransaction()
					.replace(R.id.activity_main_page,
							new FragmentMine(MainActivity.this)).commit();
			setCheck(3);
		} else {
			fragmentHomePage.setArguments(bundle);
			fragmentManager.beginTransaction()
					.replace(R.id.activity_main_page, fragmentHomePage)
					.commit();

		}

	}

	private void init() {
		radiogroup = (RadioGroup) findViewById(R.id.activity_main_radiogroup);
		radioButtonhomepage = (RadioButton) findViewById(R.id.activity_main_home_page);
		radioButtondiscover = (RadioButton) findViewById(R.id.activity_main_discover);
		radioButtonmessage = (RadioButton) findViewById(R.id.activity_main_message);
		radioButtonmine = (RadioButton) findViewById(R.id.activity_main_mine);
		radioButtonlist.add(radioButtonhomepage);
		radioButtonlist.add(radioButtondiscover);
		radioButtonlist.add(radioButtonmessage);
		radioButtonlist.add(radioButtonmine);
	}

	private void setFragmentIndicator() {

		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				FragmentHomePage fragmentHomePage = new FragmentHomePage();
				switch (checkedId) {
				case R.id.activity_main_home_page:

					fragmentManager.beginTransaction()
							.replace(R.id.activity_main_page, fragmentHomePage)
							.commit();
					Bundle bundle = new Bundle();
					bundle.putString("lostkind_from_mainactivity",
							lostkind_from_kindleft);
					bundle.putString("DOORHOMEPAGE", DOORHOMEPAGE);
					fragmentHomePage.setArguments(bundle);
					break;
				case R.id.activity_main_discover:
					setCheck(1);
					fragmentManager
							.beginTransaction()
							.replace(R.id.activity_main_page,
									new NewFragmentDiscover()).commit();
					break;

				case R.id.activity_main_message:
					setCheck(2);
					fragmentManager
							.beginTransaction()
							.replace(R.id.activity_main_page,
									new FragmentMessage()).commit();
					break;
				case R.id.activity_main_mine:
					setCheck(3);
					if ("".equals(token)) {

						fragmentManager
								.beginTransaction()
								.replace(
										R.id.activity_main_page,
										new FragmentMineUnlogin(
												MainActivity.this)).commit();

					} else {
						fragmentManager
								.beginTransaction()
								.replace(R.id.activity_main_page,
										new FragmentMine(MainActivity.this))
								.commit();

					}

					break;
				default:
					break;
				}
			}
		});
	}

	private void setCheck(int index) {

		for (int i = 0; i < radioButtonlist.size(); i++) {

			if (i == index) {
				radioButtonlist.get(index).setChecked(true);
			} else {
				radioButtonlist.get(i).setChecked(false);
			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { // resultCode为回传的标记，
		case 20:

			if (data == null || "".equals(data)) {
				return;
			} else {
				Bundle bundle = data.getExtras(); // data为B中回传的Intent

				lostkind_from_kindleft = bundle
						.getString("lostkind_from_kindleft");
				lost_from_kindleft = bundle.getString("lost_from_kindleft");
				Intent intent = new Intent(MainActivity.this,
						MainActivity.class);
				intent.putExtra("lostkind_from_result_mainactivity",
						lostkind_from_kindleft);
				intent.putExtra("lost_from_result_mainactivity",
						lost_from_kindleft);
				startActivity(intent);
				finish();

				break;

			}
		case 40:

			if (data == null || "".equals(data)) {
				return;
			} else {
				Bundle bundle = data.getExtras(); // data为B中回传的Intent

				foundkind_from_kindright = bundle
						.getString("foundkind_from_kindright");
				found_from_kindright = bundle.getString("found_from_kindright");
				Intent intent = new Intent(MainActivity.this,
						MainActivity.class);

				intent.putExtra("foundkind_from_result_mainactivity",
						foundkind_from_kindright);
				intent.putExtra("found_from_result_mainactivity",
						found_from_kindright);

				startActivity(intent);
				finish();

				break;
			}

		default:
			break;
		}

	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				
				SysApplication.getInstance().exit();// 可直接关闭所有的Acitivity并退出应用程序。
			}
			return true;
		}

		return true;

	}

	@Override
	protected void onDestroy() {

		SharedPreferences staytime = getSharedPreferences("STAYTIME",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editorcomment = staytime.edit();
		// 用putString的方法保存数据
		editorcomment.putString("STAYTIME", System_time_New.system_time(true));
		// 提交当前数据
		editorcomment.commit();
		SharedPreferences staySystemtime = getSharedPreferences("SYSTEMTIME",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editorsystem = staySystemtime.edit();
		// 用putString的方法保存数据
		editorsystem.putString("SYSTEMTIME", System_time_New.system_time(false));
		// 提交当前数据
		editorsystem.commit();
		Intent service = new Intent(MainActivity.this, Myservice.class);
		MainActivity.this.stopService(service);
		super.onDestroy();
	}
}
