package net.loonggg.utils;

import java.util.ArrayList;
import java.util.HashMap;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridViewAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, Object>> item;
	private Context context;
	int selectPosition = -1;
	
	public MyGridViewAdapter(Context context,
			ArrayList<HashMap<String, Object>> item) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.item = item;
		
	}

	public void setSelectPosition(int position) {
		selectPosition = position;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return item.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		if (convertView == null) {
			convertView = ((Activity) context).getLayoutInflater().inflate(
					R.layout.night_item, null);

			Holder holder = new Holder();

			holder.itemText = (TextView) convertView
					.findViewById(R.id.ItemText);
			holder.itemImage = (ImageView) convertView
					.findViewById(R.id.ItemImage);
			holder.itemImageImage = (ImageView) convertView
					.findViewById(R.id.ItemImageImage);
	
			convertView.setTag(holder);
		}

		Holder h = (Holder) convertView.getTag();// 取出ViewHolder对象

		HashMap<String, Object> a = item.get(position);

	

		int imageView = a.get("ItemImage").hashCode();

		h.itemImage.setImageResource(imageView);
		
		String iString = a.get("ItemText").toString();
		
		h.itemText.setText(iString);


		if (position == selectPosition) {
			h.itemImageImage.setVisibility(View.VISIBLE);
		} else {
			h.itemImageImage.setVisibility(View.GONE);
		}

		return convertView;
	}

	private class Holder {
		@SuppressWarnings("unused")
		TextView itemText;
		ImageView itemImage;
		ImageView itemImageImage;
	}

}
