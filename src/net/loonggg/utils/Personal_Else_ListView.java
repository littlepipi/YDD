package net.loonggg.utils;

import tx.ydd.app.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class Personal_Else_ListView extends ListView {
	View header;// ���������ļ���
	View footer;// �ײ����֣�

	public Personal_Else_ListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public Personal_Else_ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public Personal_Else_ListView(Context context, AttributeSet attrs,
			int defStyle) {
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
		header = inflater.inflate(R.layout.personalelse_header, null);
		// footer = inflater.inflate(R.layout.details_lost_footer, null);
		// this.addFooterView(footer, null, false);
		this.addHeaderView(header, null, false);

	}

}
