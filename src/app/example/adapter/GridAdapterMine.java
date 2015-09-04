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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridAdapterMine extends BaseAdapter {
	public static class Item {

		public String text1;
		public int resId1;

	}

	// ArrayList<HashMap<String, Object>> lstImageItem = new
	// ArrayList<HashMap<String, Object>>();
	// for (int i = 0; i < 9; i++) {
	// HashMap<String, Object> map = new HashMap<String, Object>();
	// map.put("ItemImage", srcs[i]);// 添加图像资源的ID
	// map.put("ItemText", table[i]);// 按序号做ItemText
	// lstImageItem.add(map);
	// }
	private List<Item> mItems = new ArrayList<GridAdapterMine.Item>();
	private Context mContext;
	String text[] = { "失物记录", "招领记录", "发现记录", "账户设置", "软件分享", "清空缓存", "意见反馈",
			"系统更新", "关于我们" };;
	int resId[] = {  R.drawable.lost_record, R.drawable.found_recodr,
			R.drawable.discover_record, R.drawable.setting,
			R.drawable.share, R.drawable.blush,
			R.drawable.feedback, R.drawable.update,
			R.drawable.about_us };

	public GridAdapterMine(Context context) {
		// 测试数据
		for (int i = 0; i < 9; i++) {
			Item object = new Item();

			object.text1 = text[i];
			object.resId1 = resId[i];
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
					R.layout.fragment_mine_item, null);
		}
		RelativeLayout relativeLayout = (RelativeLayout)convertView.findViewById(R.id.yanse);
		relativeLayout.setBackgroundResource(R.drawable.fragment_mine_grey_blue);
		ImageView image = (ImageView) convertView
				.findViewById(R.id.fragment_mine_ItemImage);
		TextView text = (TextView) convertView
				.findViewById(R.id.fragment_mine_ItemText);
		Item item = (Item) getItem(position);
		image.setImageResource(item.resId1);
		text.setText(item.text1);
		return convertView;
	}
}
