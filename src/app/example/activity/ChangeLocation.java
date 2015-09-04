package app.example.activity;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.example.application.MyApplication;

public class ChangeLocation extends Activity {
	TextView change_location_location_now, change_location_location_china;
	LinearLayout change_location_one, change_location_two;
	Button change_location_not;
	ImageButton change_location_back;
	ImageView change_location_city_image, change_location_school_image;
	private String country;

	private HorizontalScrollView mScrollView;
	private final Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_location);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		country = intent.getStringExtra("NEWCITY");
		mScrollView = (HorizontalScrollView) findViewById(R.id.change_location_scrollview);
		change_location_one = (LinearLayout) findViewById(R.id.change_location_one);
		change_location_two = (LinearLayout) findViewById(R.id.change_location_two);
		change_location_back = (ImageButton) findViewById(R.id.change_location_back);
		change_location_city_image = (ImageView) findViewById(R.id.change_location_city_image);
		change_location_school_image = (ImageView) findViewById(R.id.change_location_school_image);
		change_location_location_now = (TextView) findViewById(R.id.change_location_location_now);
		change_location_location_china = (TextView) findViewById(R.id.change_location_location_china);
		change_location_not = (Button) findViewById(R.id.change_location_not);
		change_location_location_now.setText(country);
		change_location_one.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				change_location_city_image.isClickable();
				Intent intent = new Intent(MyApplication.getAppContext(),
						Province.class);
				intent.putExtra("kind", "society");
				intent.putExtra("from", "Home_page");
				intent.putExtra("Home_page", "Home_page");
				startActivity(intent);
				finish();
			}
		});
		change_location_two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				change_location_school_image.isClickable();
				Intent intent = new Intent(MyApplication.getAppContext(),
						Province.class);
				intent.putExtra("kind", "school");
				intent.putExtra("from", "Home_page");
				intent.putExtra("Home_page", "Home_page");
				startActivity(intent);
				finish();
			}
		});
		change_location_location_china
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						SharedPreferences mySharedPreferences = MyApplication
								.getAppContext().getSharedPreferences(
										"Home_page", Activity.MODE_PRIVATE);
						// 实例化SharedPreferences.Editor对象（第二步）
						SharedPreferences.Editor editor = mySharedPreferences
								.edit();
						// 用putString的方法保存数据
						editor.putString("Home_page", "全国");
						// 提交当前数据
						editor.commit();
						Intent intent = new Intent(ChangeLocation.this,
								MainActivity.class);
						startActivity(intent);
						ChangeLocation.this.finish();

					}
				});
		change_location_not.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChangeLocation.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		change_location_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChangeLocation.this,
						MainActivity.class);
				startActivity(intent);
			}
		});
		mHandler.post(mScrollToBottom); // 关键是这里
	}

	private Runnable mScrollToBottom = new Runnable() {
		@Override
		public void run() {
			int off = change_location_location_now.getMeasuredWidth()
					- mScrollView.getWidth(); // 计算移动量
//			System.out.println("偏移量~~~" + off + "文字长度"
//					+ change_location_location_now.getHeight() + "滚动长度"
//					+ mScrollView.getHeight());
			if (off > 0) {
				mScrollView.scrollTo(0, off); // 自动移动
			}
		}
	};

}
