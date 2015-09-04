package net.loonggg.utils;

import tx.ydd.app.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class MyDiscoverListView extends ListView {
	View header;// 顶部布局文件；
	int headerHeight;// 顶部布局文件的高度；
	int firstVisibleItem;// 当前第一个可见的item的位置；
	int scrollState;// listview 当前滚动状态；
	boolean isRemark;// 标记，当前是在listview最顶端摁下的；
	int startY;// 摁下时的Y值；

	int state;// 当前的状态；
	final int NONE = 0;// 正常状态；
	final int PULL = 1;// 提示下拉状态；
	final int RELESE = 2;// 提示释放状态；
	final int REFLASHING = 3;// 刷新状态；

	View footer;// 底部布局；
	int totalItemCount;// 总数量；
	int lastVisibleItem;// 最后一个可见的item；
	boolean isLoading;// 正在加载；

	public MyDiscoverListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public MyDiscoverListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public MyDiscoverListView(Context context, AttributeSet attrs, int defStyle) {
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
		header = inflater.inflate(R.layout.discovedetails_header, null);
//		footer = inflater.inflate(R.layout.discovedetails_footer, null);
//		this.addFooterView(footer, null, false);
//		footer.setVisibility(View.INVISIBLE);
		this.addHeaderView(header, null, false);

	}

}
