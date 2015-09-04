package net.loonggg.utils;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class popup extends PopupWindow {

	private Button postLostMessage, postFoundMessage, postDiscoverMessage;
	private View mMenuView;

	public popup(final Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popupwindow, null);
		postLostMessage = (Button) mMenuView
				.findViewById(R.id.lost_information);
		postFoundMessage = (Button) mMenuView
				.findViewById(R.id.found_information);
		postDiscoverMessage = (Button) mMenuView
				.findViewById(R.id.discover_information);
		// 设置按钮监听
		postLostMessage.setOnClickListener(itemsOnClick);
		postFoundMessage.setOnClickListener(itemsOnClick);
		postDiscoverMessage.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.popupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//		mMenuView.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View v, MotionEvent event) {
//
//				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//				int y = (int) event.getY();
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					if (y < height) {
//						dismiss();
//						Handler handler = new Handler();
//						handler.postDelayed(new Runnable() {
//
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								context.finish();
//							}
//						}, 50);
//
//					}
//				}
//				return true;
//			}
//		});

	}

}