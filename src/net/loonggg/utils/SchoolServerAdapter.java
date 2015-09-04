package net.loonggg.utils;

import java.util.ArrayList;

import tx.ydd.app.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.example.netserver.PersonData;

public class SchoolServerAdapter extends BaseAdapter {

	ArrayList<SchoolServerPersonData> list;
	LayoutInflater inflater;
	private String name;
	private String school;
	private String grade;
	private String slogan;
	private String introduction;
	private String activities;

	public SchoolServerAdapter(Context context,
			ArrayList<SchoolServerPersonData> list) {
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	public void onDateChange(ArrayList<SchoolServerPersonData> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return 5;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
		// return 5;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SchoolServerPersonData persondata = list.get(position);
		ViewHolder holder;
		/***************************************************************/
		/**
		 * 获取数据
		 */
		name = persondata.getName();
		school = persondata.getSchool();
		grade = persondata.getGrade();
		slogan = persondata.getSlogan();
		introduction = persondata.getIntroduction();
		activities = persondata.getActivities();

		/***************************************************************/
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.activity_school_server_list, null);

			holder.peronnickname = (TextView) convertView
					.findViewById(R.id.peronnickname);
			holder.schoolname = (TextView) convertView
					.findViewById(R.id.schoolname);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/******************************************/

		holder.peronnickname.setText(name);
		holder.schoolname.setText(school);
		return convertView;
	}

	public class ViewHolder {

		public TextView peronnickname;
		public TextView schoolname;

	}

}
