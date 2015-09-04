package app.example.activity;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import app.example.adapter.GridAdapterCategoryRight;

public class KindRight extends Activity {

	private ImageView backImageView;
	@SuppressWarnings("unused")
	private String Title[];
	private int mScreenWidth;
	private float density;
	private RelativeLayout relative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_home_page_right_kind);
		SysApplication.getInstance().addActivity(this);
		DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取屏幕分辨率

		mScreenWidth = dm.widthPixels;

		density = dm.density;

		relative = (RelativeLayout) 
				findViewById(R.id.screen_hight_kind_right);
		relative.getLayoutParams().height = mScreenWidth/3*4;
		final GridView gridview = (GridView) findViewById(R.id.fragment_home_page_tab_right_more_gridview);

		gridview.setEnabled(false);

		Handler handler1 = new Handler();

		handler1.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				gridview.setEnabled(true);

			}
		}, 2000);
		backImageView = (ImageView) this.findViewById(R.id.fragment_home_page_tab_right_more_back);
		backImageView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				backImageView
						.setImageResource(R.drawable.common_icon_arrow_left_white);

				finish();
				KindRight.this.overridePendingTransition(
						R.anim.in_from_left, R.anim.out_to_right);

			}
		});
		// 生成动态数组，并且转入数据
		final String Title[] = { "所有","钱包", "证件", "钥匙", "数码", "饰品", "衣服", "文件",
				"书籍", "宠物" ,"车辆","其他"};
		

		gridview.setAdapter(new GridAdapterCategoryRight(this.getBaseContext()));
		
	
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("foundkind_from_kindright", Title[arg2]);
				intent.putExtra("found_from_kindright", "招领");
				setResult(40, intent);
				finish();
			}
		});

	}

//

}
