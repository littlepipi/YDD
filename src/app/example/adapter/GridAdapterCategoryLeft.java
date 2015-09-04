package app.example.adapter;

import java.util.ArrayList;
import java.util.List;

import tx.ydd.app.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridAdapterCategoryLeft extends BaseAdapter {
	public static class Item {

		public String text_category_left;
		public int resId_category_left;
		LinearLayout linearLayout;

	}

	private List<Item> mItems = new ArrayList<GridAdapterCategoryLeft.Item>();
	private Context mContext;
	 String text[] = { "全部", "钱包", "证件", "钥匙", "数码", "饰品", "衣服",
			"文件", "书籍","宠物","找人","车辆","其他","","" };
	 int resId[] = { R.drawable.all, R.drawable.purse, R.drawable.card,
				R.drawable.keys, R.drawable.digital, R.drawable.ring,
				R.drawable.cloth, R.drawable.file, R.drawable.book, R.drawable.pat,
				R.drawable.people, R.drawable.car, R.drawable.others,
				R.drawable.transparent, R.drawable.transparent, };
	public GridAdapterCategoryLeft(Context context) {
		// 测试数据
		for (int i = 0; i < 15; i++) {
			Item object = new Item();

			
			object.text_category_left = text[i];
			object.resId_category_left = resId[i];
			mItems.add(object);
		}
		mContext = context;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.fragment_home_page_left_item_kind, null);
			
		}
		RelativeLayout relativeLayout = (RelativeLayout)convertView.findViewById(R.id.yanse);
		if (position==13||position==14) {
			relativeLayout.setBackgroundResource(R.drawable.fragment_mine_never_change);
			
		}else {
			relativeLayout.setBackgroundResource(R.drawable.fragment_mine_grey_blue);
		}
		ImageView image = (ImageView) convertView.findViewById(R.id.ItemImage);
		TextView text = (TextView) convertView.findViewById(R.id.ItemText);
		Item item = (Item) getItem(position);
		image.setImageResource(item.resId_category_left);
		text.setText(item.text_category_left);
		
	
		
		return convertView;
	}
}
