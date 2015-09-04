package net.loonggg.fragment;

import java.util.ArrayList;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import app.example.activity.ChangeLocation;
import app.example.activity.KindLeft;
import app.example.activity.KindRight;
import app.example.application.MyApplication;

public class FragmentHomePage extends Fragment implements OnClickListener {

	private HorizontalScrollView mHorizontalScrollView;
	private LinearLayout mLinearLayout;
	private ViewPager pager;
	private ImageView mImageView;
	private int mScreenWidth;
	private int item_width;
	private float density;
	private int endPosition;
	private int beginPosition;
	private int currentFragmentIndex;
	private boolean isEnd;

	private ArrayList<Fragment> fragments;
	private String lostkind_from_mainactivity = null;

	private String lost_from_mainactivity = null;
	private String foundkind_from_mainactivity = null;
	private String found_from_mainactivity = null;
	private ImageView kindImageView;
	// private boolean speed = false;
	private TextView cityImageView;
	View view;
	String text, text2;
	private String Kind = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home_page, null);
		SharedPreferences mySharedPreferencestoken = MyApplication
				.getAppContext().getSharedPreferences("Home_page",
						Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值

		text2 = mySharedPreferencestoken.getString("Home_page", "全国");

		if (text2 == null) {

			text = "全国";

		} else {

			text = text2;

		}
		if (text.indexOf("学") != -1) {
			Kind = "school";
		} else {
			Kind = "society";
		}
		Bundle bundle = getArguments();
		lostkind_from_mainactivity = bundle
				.getString("lostkind_from_mainactivity");
		lost_from_mainactivity = bundle.getString("lost_from_mainactivity");
		foundkind_from_mainactivity = bundle
				.getString("foundkind_from_mainactivity");

		found_from_mainactivity = bundle.getString("found_from_mainactivity");
		/**
		 * 类别
		 */

		kindImageView = (ImageView) view
				.findViewById(R.id.fragment_home_page_tab_kind);

		/**
		 * 下一行切换标题的类别图片
		 */
		// kindImageView.setImageResource(2);
		kindImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (currentFragmentIndex == 0) {
					Intent intent = new Intent(getActivity(), KindLeft.class);
					getActivity().startActivityForResult(intent, 200);
					getActivity().overridePendingTransition(
							R.anim.in_from_right, R.anim.out_to_left);
				} else if (currentFragmentIndex == 1) {
					Intent intent = new Intent(getActivity(), KindRight.class);
					getActivity().startActivityForResult(intent, 300);
					getActivity().overridePendingTransition(
							R.anim.in_from_right, R.anim.out_to_left);
				} else {
					Toast.makeText(getActivity(), "跳转出错！！！", Toast.LENGTH_LONG)
							.show();
				}

			}
		});
		/**
		 * 城市
		 */

		cityImageView = (TextView) view
				.findViewById(R.id.fragment_home_page_tab_city_school);
		cityImageView.setText(text);
		cityImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ChangeLocation.class);
				intent.putExtra("NEWCITY", text);
				startActivity(intent);
				getActivity().finish();

			}
		});

		/**
		 * 设置滑动页卡，获取屏幕分辨率等
		 */
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取屏幕分辨率
		mScreenWidth = dm.widthPixels;
		density = dm.density;
		mHorizontalScrollView = (HorizontalScrollView) view
				.findViewById(R.id.fragment_home_page_scrollview);
		mLinearLayout = (LinearLayout) view
				.findViewById(R.id.fragment_home_page_linearlayout);
		mImageView = (ImageView) view
				.findViewById(R.id.fragment_home_page_imageview);

		// item_width = (int) ((mScreenWidth *density + 0.5f));
		item_width = (int) (mScreenWidth / 4);
		mImageView.getLayoutParams().width = item_width;// 初始化图片位置
		mLinearLayout.getLayoutParams().width = 2 * item_width + 4;
		// 初始化布局
		pager = (ViewPager) view
				.findViewById(R.id.fragment_home_page_tab_viewpager);

		// 初始化导航
		initNav();
		// 初始化viewPager
		initViewPager();
		return view;
	}

	private void initViewPager() {
		fragments = new ArrayList<Fragment>();

		MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(
				getChildFragmentManager(), fragments);

		FragmentHomePageLeft fragmentHomePageLeft = new FragmentHomePageLeft();
		fragments.add(fragmentHomePageLeft);
		Bundle bundle_left = new Bundle();
		bundle_left.putString("lostkind_from_fragmenthomepage",
				lostkind_from_mainactivity);
		fragmentHomePageLeft.setArguments(bundle_left);
		// 视频标签页面
		FragmentHomePageRight fragmentHomePageRight = new FragmentHomePageRight();
		fragments.add(fragmentHomePageRight);
		Bundle bundle_right = new Bundle();
		bundle_right.putString("foundkind_from_fragmenthomepage",
				foundkind_from_mainactivity);
		fragmentHomePageRight.setArguments(bundle_right);
		pager.setAdapter(fragmentPagerAdapter);
		Log.i("--------------", "++++++++++++++++++++++++>>>>>>>>");
		fragmentPagerAdapter.setFragments(fragments);
		Log.i("-----", "++++++++++++++++++++++++");
		pager.setOnPageChangeListener(new MyOnPageChangeListener());

		if (lost_from_mainactivity == null && found_from_mainactivity == null) {

			pager.setCurrentItem(0);// 设置默认页卡

		}

		else

		{
			if ("失物".equals(lost_from_mainactivity)) {

				pager.setCurrentItem(0);// 设置默认页卡

			} else if ("招领".equals(found_from_mainactivity)) {

				pager.setCurrentItem(1);// 设置默认页卡

			}
		}

	}

	private void initNav() {
		for (int i = 0; i < 2; i++) {
			RelativeLayout layout = new RelativeLayout(getActivity());
			TextView view = new TextView(getActivity());

			if (i == 0) {
				view.setText("失物");

			} else if (i == 1) {
				view.setText("招领");

			}

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			layout.addView(view, params);
			mLinearLayout.addView(layout, (int) (mScreenWidth / 4 + 0.5f), 50);
			layout.setOnClickListener(this);
			layout.setTag(i);
		}
	}

	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		private ArrayList<Fragment> fragments;
		private FragmentManager fm;

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
		}

		public MyFragmentPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> fragments) {
			super(fm);
			this.fm = fm;
			this.fragments = fragments;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void setFragments(ArrayList<Fragment> fragments) {
			Log.i("------23ee------", "++++++++++++++++++++++++>>>>>>>>");
			if (this.fragments != null) {
				Log.i("---------767-----", "++++++++++++++++++++++++>>>>>>>>");
				FragmentTransaction ft = fm.beginTransaction();
				for (Fragment f : this.fragments) {
					Log.i("--------488------", "++++++++++++++++++++++++>>>>>>>>");
					ft.remove(f);
				}
				ft.commit();
				ft = null;
				fm.executePendingTransactions();
			}
			this.fragments = fragments;
			notifyDataSetChanged();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			Object obj = super.instantiateItem(container, position);
			return obj;
		}

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		/**
		 * 此方法是页面跳转完后得到调用， arg0是你当前选中的页面的Position（位置编号）。
		 */

		@Override
		public void onPageSelected(final int position) {
			Animation animation = new TranslateAnimation(endPosition, position
					* item_width, 0, 0);

			beginPosition = position * item_width;
			currentFragmentIndex = position;
			if (animation != null) {
				animation.setFillAfter(true);// 停留最后效果

				animation.setDuration(200);// 设置动画持续时间
				mImageView.startAnimation(animation);// 开始
			}

		}

		/**
		 * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到
		 * 
		 * 调用。其中三个参数的含义分别为：
		 * 
		 * arg0 :当前页面，及你点击滑动的页面
		 * 
		 * arg1:当前页面偏移的百分比
		 * 
		 * arg2:当前页面偏移的像素位置
		 */
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			if (!isEnd) {
				if (currentFragmentIndex == position) {
					endPosition = item_width * currentFragmentIndex
							+ (int) (item_width * positionOffset);
				}
				if (currentFragmentIndex == position + 1) {
					endPosition = item_width * currentFragmentIndex
							- (int) (item_width * (1 - positionOffset));
				}

				Animation mAnimation = new TranslateAnimation(beginPosition,
						endPosition, 0, 0);
				mAnimation.setFillAfter(true);

				mAnimation.setDuration(200);

				mImageView.startAnimation(mAnimation);
				mHorizontalScrollView.invalidate();
				beginPosition = endPosition;
			}

		}

		/**
		 * 此方法是在状态改变的时候调用，其中arg0这个参数
		 * 
		 * 有三种状态（0，1，2）。arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
		 * 
		 * 当页面开始滑动的时候，三种状态的变化顺序为（1，2，0）
		 */

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_DRAGGING) {
				isEnd = false;
			} else if (state == ViewPager.SCROLL_STATE_SETTLING) {
				isEnd = true;
				beginPosition = currentFragmentIndex * item_width;
				if (pager.getCurrentItem() == currentFragmentIndex) {
					// 未跳入下一个页面
					mImageView.clearAnimation();
					Animation animation = null;
					// 恢复位置
					animation = new TranslateAnimation(endPosition,
							currentFragmentIndex * item_width, 0, 0);
					animation.setFillAfter(true);

					animation.setDuration(250);

					mImageView.startAnimation(animation);
					mHorizontalScrollView.invalidate();
					endPosition = currentFragmentIndex * item_width;
				}

			}
		}

	}

	@Override
	public void onClick(View v) {
		pager.setCurrentItem((Integer) v.getTag());
	}
}
