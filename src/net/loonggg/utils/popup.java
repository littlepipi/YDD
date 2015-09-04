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
		// ���ð�ť����
		postLostMessage.setOnClickListener(itemsOnClick);
		postFoundMessage.setOnClickListener(itemsOnClick);
		postDiscoverMessage.setOnClickListener(itemsOnClick);
		// ����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		// ����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.MATCH_PARENT);
		// ����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		// ����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.popupAnimation);
		// ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// ����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		// mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
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