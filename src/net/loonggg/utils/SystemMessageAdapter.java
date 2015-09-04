package net.loonggg.utils;

import java.util.ArrayList;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SystemMessageAdapter extends BaseAdapter {
	ArrayList<SystemMessage> system_num;
	LayoutInflater inflater;

	private String systemcreated, systemcontent;

	public SystemMessageAdapter(Context context,
			ArrayList<SystemMessage> system_num) {
		this.system_num = system_num;
		this.inflater = LayoutInflater.from(context);

	}

	public void onDateChange(ArrayList<SystemMessage> system_num) {
		this.system_num = system_num;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return system_num.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return system_num.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void additem() {

	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final SystemMessage systemMessage = system_num.get(position);
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.system_message_item, null);
			holder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.item);

			holder.system_created = (TextView) convertView
					.findViewById(R.id.system_created);
			holder.system_content = (TextView) convertView
					.findViewById(R.id.system_content);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		systemcreated = systemMessage.getCreated();
		systemcontent = systemMessage.getContent();

		holder.system_content.setText(systemcontent);
		holder.system_created.setText(systemcreated);

		return convertView;
	}

	class ViewHolder {
		LinearLayout linearLayout;

		TextView system_content, system_created;

	}

}
