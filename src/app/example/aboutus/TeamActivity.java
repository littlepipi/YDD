package app.example.aboutus;

import java.util.ArrayList;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

//第一次运行的引导页代码
public class TeamActivity extends Activity implements OnPageChangeListener {
	private Context context;
	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;

	private LinearLayout indicatorLayout;
	private ArrayList<View> views;
	private ImageView[] indicators = null;
	private int[] images;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myteam);
		context = this;
		// 创建桌面快捷方式

		// 设置引导图片
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 仅需在这设置图片 指示器和page自动添加
		images = new int[] { R.drawable.one, R.drawable.two, R.drawable.three,
				R.drawable.four, R.drawable.five, R.drawable.sit,
				R.drawable.seven, R.drawable.eight, R.drawable.nine };

		initView();
	}

	// 初始化视图
	private void initView() {
		// 实例化视图控件
		viewPager = (ViewPager) findViewById(R.id.viewpage);

		indicatorLayout = (LinearLayout) findViewById(R.id.indicator);
		views = new ArrayList<View>();
		indicators = new ImageView[images.length]; // 定义指示器数组大小

		for (int i = 0; i < images.length; i++) {
			// 循环加入图片
			ImageView imageView = new ImageView(context);
			imageView.setBackgroundResource(images[i]);
			views.add(imageView);
			// 循环加入指示器
			indicators[i] = new ImageView(context);
			indicators[i].setBackgroundResource(R.drawable.indicators_default);
			if (i == 0) {
				indicators[i].setBackgroundResource(R.drawable.indicators_now);
			}
			indicatorLayout.addView(indicators[i]);
		}
		pagerAdapter = new BasePagerAdapter(views);
		viewPager.setAdapter(pagerAdapter); // 设置适配器
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	// 监听viewpage
	@Override
	public void onPageSelected(int arg0) {

		// 更改指示器图片
		for (int i = 0; i < indicators.length; i++) {
			indicators[arg0].setBackgroundResource(R.drawable.indicators_now);
			if (arg0 != i) {
				indicators[i]
						.setBackgroundResource(R.drawable.indicators_default);
			}
		}
	}
}
