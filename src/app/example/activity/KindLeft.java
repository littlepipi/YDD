package app.example.activity;

import java.util.ArrayList;
import java.util.HashMap;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import app.example.adapter.GridAdapterCategoryLeft;

public class KindLeft extends Activity {

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
		setContentView(R.layout.fragment_home_page_left_kind);
		SysApplication.getInstance().addActivity(this);
		backImageView = (ImageView) this
				.findViewById(R.id.fragment_home_page_tab_left_more_back);
		backImageView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				backImageView
						.setImageResource(R.drawable.common_icon_arrow_left_white);

				finish();

			}
		});
		
		DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取屏幕分辨率

		mScreenWidth = dm.widthPixels;

		density = dm.density;

		relative = (RelativeLayout) 
				findViewById(R.id.screen_hight_kind_left);
		relative.getLayoutParams().height = mScreenWidth/3*5;
		final GridView gridview = (GridView) findViewById(R.id.fragment_home_page_tab_left_kind_gridview);

		gridview.setEnabled(false);

		Handler handler1 = new Handler();

		handler1.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				gridview.setEnabled(true);

			}
		}, 2000);
		// 生成动态数组，并且转入数据
		final String Title[] = { "所有", "钱包", "证件", "钥匙", "数码", "饰品", "衣服",
				"文件", "书籍", "宠物", "找人", "车辆", "其他", "", "" };

		gridview.setSelector(new ColorDrawable(Color.BLUE));

		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 15; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", R.drawable.circle_pink_white);// 添加图像资源的ID
			map.put("ItemText", Title[i]);// 按序号做ItemText
			lstImageItem.add(map);
			backImageView = (ImageView) this
					.findViewById(R.id.fragment_home_page_tab_left_more_back);
			backImageView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					backImageView
							.setImageResource(R.drawable.common_icon_arrow_left_white);

					finish();
					KindLeft.this.overridePendingTransition(
							R.anim.in_from_left, R.anim.out_to_right);

				}
			});
		}

		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		SimpleAdapter saImageItems = new SimpleAdapter(this, // 没什么解释
				lstImageItem,// 数据来源
				R.layout.fragment_home_page_left_item_kind,// night_item的XML实现

				// 动态数组与ImageItem对应的子项
				new String[] { "ItemImage", "ItemText" },

				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.ItemImage, R.id.ItemText });
		// 添加并且显示
		gridview.setAdapter(saImageItems);
		// 添加消息处理

		gridview.setAdapter(new GridAdapterCategoryLeft(this.getBaseContext()));

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

//				Log.i("--------->", "" + arg2);

				if (arg2 == 13 || arg2 == 14) {

					gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

				} else {
					Intent intent = new Intent();

					intent.putExtra("lostkind_from_kindleft", Title[arg2]);
					intent.putExtra("lost_from_kindleft", "失物");
					setResult(20, intent);
					finish();

				}

			}
		});

	}
	//

}
