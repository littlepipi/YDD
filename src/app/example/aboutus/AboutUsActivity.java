package app.example.aboutus;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.example.activity.SchoolServerActivity;
import app.example.activity.SysApplication;

public class AboutUsActivity extends Activity {

	private TextView versiontextView;
	private LinearLayout linearLayoutone, linearLayouttwo, linearLayoutthree;
	private ImageButton backButton;
	private ImageView about_us_plat_go, about_us_team_go, about_us_welfare_go;
	String version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		SysApplication.getInstance().addActivity(this);
		init();// 控件初始化
	}

	private void init() {
		// TODO Auto-generated method stub
		linearLayoutone = (LinearLayout) findViewById(R.id.about_us_plat);
		linearLayouttwo = (LinearLayout) findViewById(R.id.about_us_team);
		linearLayoutthree = (LinearLayout) findViewById(R.id.about_us_welfare);
		about_us_plat_go = (ImageView) findViewById(R.id.about_us_plat_go);
		about_us_team_go = (ImageView) findViewById(R.id.about_us_team_go);
		about_us_welfare_go = (ImageView) findViewById(R.id.about_us_welfare_go);
		backButton = (ImageButton) findViewById(R.id.about_us_back);
		versiontextView = (TextView) findViewById(R.id.about_us_version_number);

		version = getVersion();

		versiontextView.setText("版本号v" + version);

		linearLayoutone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				about_us_plat_go.isClickable();

				Intent intent = new Intent(AboutUsActivity.this,
						PlatformActivity.class);
				AboutUsActivity.this.startActivity(intent);
				AboutUsActivity.this.overridePendingTransition(
						R.anim.in_from_right, R.anim.out_to_left);

			}
		});
		linearLayouttwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				about_us_team_go.isClickable();
				Intent intent = new Intent(AboutUsActivity.this,
						TeamActivity.class);
				AboutUsActivity.this.startActivity(intent);
				AboutUsActivity.this.overridePendingTransition(
						R.anim.in_from_right, R.anim.out_to_left);
			}
		});
		linearLayoutthree.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				about_us_welfare_go.isClickable();
				Intent intent = new Intent(AboutUsActivity.this,
						SchoolServerActivity.class);
				AboutUsActivity.this.startActivity(intent);
				AboutUsActivity.this.overridePendingTransition(
						R.anim.in_from_right, R.anim.out_to_left);
			}
		});
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutUsActivity.this.finish();
			}
		});

	}

	/**
	 * 获取本地版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		try {

			PackageManager manager = AboutUsActivity.this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					AboutUsActivity.this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			AboutUsActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
