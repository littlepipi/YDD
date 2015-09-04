package app.example.activity;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import app.example.layout.VerticalSlidingView;
import app.example.layout.VerticalSlidingView.OnPageScrollListener;

public class WelcomeActivity extends Activity {
	private VerticalSlidingView mSlideView;
	private int[] LAYOUTS = new int[] { R.layout.guide_num_one,
			R.layout.guide_num_two, R.layout.guide_num_three,
			R.layout.guide_num_four, R.layout.guide_num_five, };
	private View[] mViews = new View[5];

	private boolean mIsFirst = true;

	private ImageView guide_one_one, guide_one_two, guide_one_next;
	private ImageView guide_two_one, guide_two_two, guide_two_next;
	private ImageView guide_three_one, guide_three_two, guide_three_next;
	private ImageView guide_four_one, guide_four_two, guide_four_next;
	private ImageView guide_five_one, guide_five_two;
	private Button guide_five_begin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		mSlideView = (VerticalSlidingView) findViewById(R.id.mojitutourial_sliding_view);
		mSlideView.setOnPageScrollListener(new MyPageScrollListener());
		initViews();

	}

	class MyPageScrollListener implements OnPageScrollListener {

		@Override
		public void onPageChanged(int position) {
			switch (position) {
			case 0:
				layout1AnimStart();
				break;
			case 1:
				layout2AnimStart();
				break;
			case 2:
				layout3AnimStart();
				break;
			case 3:
				layout4AnimStart();
				break;
			case 4:
				layout5AnimStart();
				break;
			}
		}

	}

	private void layout1AnimStart() {

		guide_one_one.setVisibility(View.VISIBLE);
		guide_one_two.setVisibility(View.VISIBLE);
		guide_one_next.setVisibility(View.VISIBLE);
		guide_one_one.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.in_from_top));
		guide_one_two.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.title_scale_anim));

		guide_one_next.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.next_anim));

		clearLayout2Anim();

	}

	private void layout2AnimStart() {

		guide_two_one.setVisibility(View.VISIBLE);
		guide_two_two.setVisibility(View.VISIBLE);
		guide_two_one.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.in_from_top));
		guide_two_two.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.title_scale_anim));

		guide_two_next.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.next_anim));

		clearLayout1Anim();
		clearLayout3Anim();
	}

	private void layout3AnimStart() {
		guide_three_two.setVisibility(View.VISIBLE);
		guide_three_one.setVisibility(View.VISIBLE);
		guide_three_next.setVisibility(View.VISIBLE);
		guide_three_one.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.in_from_top));
		guide_three_two.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.title_scale_anim));

		guide_three_next.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.next_anim));

		clearLayout2Anim();
		clearLayout4Anim();

	}

	private void layout4AnimStart() {
		guide_four_one.setVisibility(View.VISIBLE);
		guide_four_two.setVisibility(View.VISIBLE);
		guide_four_one.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.in_from_top));
		guide_four_two.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.title_scale_anim));
		guide_four_next.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.next_anim));
		clearLayout3Anim();
		clearLayout5Anim();
	}

	private void layout5AnimStart() {
		guide_five_one.setVisibility(View.VISIBLE);
		guide_five_two.setVisibility(View.VISIBLE);
		guide_five_one.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.in_from_top));
		guide_five_two.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.title_scale_anim));

		clearLayout4Anim();
	}

	private void clearLayout1Anim() {
		guide_one_two.clearAnimation();
		guide_one_two.setVisibility(View.INVISIBLE);
		guide_one_one.clearAnimation();
		guide_one_one.setVisibility(View.INVISIBLE);

	}

	private void clearLayout2Anim() {
		guide_two_one.clearAnimation();
		guide_two_one.setVisibility(View.INVISIBLE);
		guide_two_two.clearAnimation();
		guide_two_two.setVisibility(View.INVISIBLE);
	}

	private void clearLayout3Anim() {
		guide_three_one.clearAnimation();
		guide_three_one.setVisibility(View.INVISIBLE);
		guide_three_two.clearAnimation();
		guide_three_two.setVisibility(View.INVISIBLE);

	}

	private void clearLayout4Anim() {
		guide_four_one.clearAnimation();
		guide_four_one.setVisibility(View.INVISIBLE);
		guide_four_two.clearAnimation();
		guide_four_two.setVisibility(View.INVISIBLE);
	}

	private void clearLayout5Anim() {
		guide_five_one.clearAnimation();
		guide_five_one.setVisibility(View.INVISIBLE);
		guide_five_two.clearAnimation();
		guide_five_two.setVisibility(View.INVISIBLE);
	}

	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout1 = inflater.inflate(R.layout.guide_num_one, null);
		mViews[0] = layout1;
		guide_one_one = (ImageView) layout1
				.findViewById(R.id.guide_one_back_one);
		guide_one_two = (ImageView) layout1
				.findViewById(R.id.guide_one_back_two);
		guide_one_next = (ImageView) layout1.findViewById(R.id.t1_next);

		View layout2 = inflater.inflate(R.layout.guide_num_two, null);
		mViews[1] = layout2;
		guide_two_one = (ImageView) layout2
				.findViewById(R.id.guide_two_back_one);
		guide_two_two = (ImageView) layout2
				.findViewById(R.id.guide_two_back_two);
		guide_two_next = (ImageView) layout2.findViewById(R.id.t2_next);

		View layout3 = inflater.inflate(R.layout.guide_num_three, null);
		mViews[2] = layout3;
		guide_three_one = (ImageView) layout3
				.findViewById(R.id.guide_three_back_one);
	
		guide_three_two = (ImageView) layout3
				.findViewById(R.id.guide_three_back_two);

		
		guide_three_next = (ImageView) layout3.findViewById(R.id.t3_next);

		View layout4 = inflater.inflate(R.layout.guide_num_four, null);
		mViews[3] = layout4;
		guide_four_one = (ImageView) layout4
				.findViewById(R.id.guide_four_back_one);
		
		
		guide_four_two = (ImageView) layout4
				.findViewById(R.id.guide_four_back_two);
	
		guide_four_next = (ImageView) layout4.findViewById(R.id.t4_next);

		View layout5 = inflater.inflate(R.layout.guide_num_five, null);
		mViews[4] = layout5;
		guide_five_one = (ImageView) layout5
				.findViewById(R.id.guide_five_back_one);
		
		guide_five_two = (ImageView) layout5
				.findViewById(R.id.guide_five_back_two);
		
		guide_five_begin = (Button) layout5.findViewById(R.id.begin);
		guide_five_begin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WelcomeActivity.this,
						MainActivity.class);
				WelcomeActivity.this.startActivity(intent);
				finish();
			}
		});
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		mSlideView.addView(mViews[0], lp);
		mSlideView.addView(mViews[1], lp);
		mSlideView.addView(mViews[2], lp);
		mSlideView.addView(mViews[3], lp);
		mSlideView.addView(mViews[4], lp);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (mIsFirst) {
			layout1AnimStart();
			mIsFirst = false;
		}
	}

}
