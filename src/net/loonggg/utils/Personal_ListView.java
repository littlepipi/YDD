package net.loonggg.utils;

import tx.ydd.app.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class Personal_ListView extends ListView {
	View header;// 顶部布局文件；
	

	public Personal_ListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public Personal_ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public Personal_ListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 初始化界面，添加顶部布局文件到 listview
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.personal_page_header, null);
		
	
		this.addHeaderView(header, null, false);

	}

}
