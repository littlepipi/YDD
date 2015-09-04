package app.example.activity;

import net.loonggg.utils.popup;
import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;
import app.example.application.MyApplication;

public class MainActivityBackground extends Activity {
	private popup menuWindow;
	String token;
	private ImageView cancelImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutInflater inflate = LayoutInflater.from(this);
		View view = inflate.inflate(R.layout.activity_main_background, null);

		setContentView(view);
		cancelImageView = (ImageView) findViewById(R.id.cancel);
		Animation animation1 = AnimationUtils
				.loadAnimation(this, R.anim.rotate);
		final Animation animation2 = AnimationUtils.loadAnimation(this,
				R.anim.rotate);
		cancelImageView.startAnimation(animation1);
		Bundle bundle = getIntent().getExtras();
		token = bundle.getString("token");

		if (view.isClickable()) {
			view.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.VISIBLE);
		}
		SysApplication.getInstance().addActivity(this);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				// 实例化SelectPicPopupWindow
				menuWindow = new popup(MainActivityBackground.this,
						itemsOnClick);
				// 显示窗口
				menuWindow.showAtLocation(
						MainActivityBackground.this.findViewById(R.id.main),
						Gravity.CENTER_VERTICAL, 0, 0);
				// 设置layout在PopupWindow中显示的位置
				menuWindow.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						// TODO Auto-generated method stub
						cancelImageView.startAnimation(animation2);
						MainActivityBackground.this.finish();
						overridePendingTransition(R.anim.rotate,
								R.anim.rotateout);

					}
				});
			}
		}, 50);
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.lost_information:
				Animation scaleAnimation1 = new ScaleAnimation(1.2f, 1.0f,
						1.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);

				scaleAnimation1.setDuration(500);
				v.startAnimation(scaleAnimation1);
				if ("".equals(token)) {
					Toast.makeText(MyApplication.getAppContext(), "您还未登录哦",
							Toast.LENGTH_SHORT).show();

				} else {
					Handler handler1 = new Handler();

					handler1.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							menuWindow.dismiss();
						}
					}, 200);
					Intent intent1 = new Intent();
					intent1.setClass(v.getContext(), PostLostMessage.class);
					v.getContext().startActivity(intent1);
					finish();
					overridePendingTransition(0, 0);

				}
				break;
			case R.id.found_information:
				Animation scaleAnimation2 = new ScaleAnimation(1.2f, 1.0f,
						1.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);

				scaleAnimation2.setDuration(500);
				v.startAnimation(scaleAnimation2);

				if ("".equals(token)) {
					Toast.makeText(MyApplication.getAppContext(), "您还未登录哦",
							Toast.LENGTH_SHORT).show();

				} else {
					Handler handler2 = new Handler();

					handler2.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							menuWindow.dismiss();
						}
					}, 200);

					Intent intent2 = new Intent();

					intent2.setClass(v.getContext(), PostFoundMessage.class);
					v.getContext().startActivity(intent2);
					finish();
					overridePendingTransition(0, 0);

				}
				break;
			case R.id.discover_information:
				Animation scaleAnimation3 = new ScaleAnimation(1.2f, 1.0f,
						1.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);

				scaleAnimation3.setDuration(500);
				v.startAnimation(scaleAnimation3);
				if ("".equals(token)) {
					Toast.makeText(MyApplication.getAppContext(), "您还未登录哦",
							Toast.LENGTH_SHORT).show();
				} else {

					Handler handler3 = new Handler();

					handler3.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							menuWindow.dismiss();
						}
					}, 200);
					Intent intent3 = new Intent();

					intent3.setClass(v.getContext(), PostDiscoverMessage.class);
					v.getContext().startActivity(intent3);
					finish();
					overridePendingTransition(0, 0);

				}
				break;
			default:
				break;
			}

		}

	};
}
