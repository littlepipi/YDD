package net.loonggg.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class CommentsToMessageListView extends ListView {
	View header;// ���������ļ���
	View footer;// �ײ����֣�

	public CommentsToMessageListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public CommentsToMessageListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public CommentsToMessageListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * ��ʼ�����棬��Ӷ��������ļ��� listview
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		
	}

}
