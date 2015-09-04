package net.loonggg.utils;

import java.util.List;
import java.util.Map;

import tx.ydd.app.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MySimpleAdapter extends SimpleAdapter {
	LayoutInflater mInflater;
	ViewHolder holder;
	private int selectPosition = -1;
	public MySimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub

	}

	public void selectPosition(int position){
		this.selectPosition = position;
		notifyDataSetChanged();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.night_item, null);
			holder = new ViewHolder();

			holder.ItemText = (TextView) convertView
					.findViewById(R.id.ItemText);
			holder.ItemImage = (ImageView) convertView
					.findViewById(R.id.ItemImage);
			holder.ItemImageImage = (ImageView) convertView
					.findViewById(R.id.ItemImageImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}

		if (selectPosition == position)
		{
			
			//这里是选中的代码 比如 radiobutton.setChecked(true)  -- 必须写
			holder.ItemImageImage.setVisibility(View.VISIBLE);
		}else{
			//这里是未选中的代码 比如 radiobutton.setChecked(false)  -- 必须写
			holder.ItemImageImage.setVisibility(View.GONE);
		}

		return convertView;

	}

	private static class ViewHolder {

		TextView ItemText;

		ImageView ItemImage, ItemImageImage;

	}

	// class BottomListener implements OnItemClickListener {
	//
	//
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// // TODO Auto-generated method stub
	//
	// }
	// }

}