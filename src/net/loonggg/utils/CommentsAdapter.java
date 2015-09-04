package net.loonggg.utils;

import java.util.ArrayList;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.example.activity.ListViewMessageDetails;
import app.example.application.MyApplication;
import app.example.internet.picture.ImageLoader;

public class CommentsAdapter extends BaseAdapter {
	ArrayList<CommentsMessage> comments_num;
	LayoutInflater inflater;
	public ImageLoader imageLoader;
	CommentsDelete commentsDelete;
	private ListViewMessageDetails listViewMessageDetails;
	private String comments_id, commentsheader, commentsnickname, commentstime,
			commnetscontent;

	public CommentsAdapter(Context context,
			ArrayList<CommentsMessage> comments_num) {
		this.comments_num = comments_num;
		this.inflater = LayoutInflater.from(context);
		imageLoader = new ImageLoader(context);
		listViewMessageDetails = new ListViewMessageDetails();

	}

	public void onDateChange(ArrayList<CommentsMessage> comments_num) {
		this.comments_num = comments_num;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comments_num.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return comments_num.get(position);
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
		// TODO Auto-generated method stub
		final CommentsMessage commentsMessage = comments_num.get(position);
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.comments_listview_item,
					null);
			holder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.item);

			holder.commentsheader = (ImageView) convertView
					.findViewById(R.id.comments_header);
			holder.commentsnickname = (TextView) convertView
					.findViewById(R.id.comments_nickname);

			holder.commnetscontent = (TextView) convertView
					.findViewById(R.id.comments_content);
			holder.commentstime = (TextView) convertView
					.findViewById(R.id.comments_time);
			holder.comments_delete = (TextView) convertView
					.findViewById(R.id.comments_delete);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		comments_id = commentsMessage.getComment_id();
		commentsheader = commentsMessage.getCommentsHeader();
		commentsnickname = commentsMessage.getCommentsNickname();
		commentstime = commentsMessage.getCommentsTime();
		commnetscontent = commentsMessage.getCommentsContent();

		/*************************************/
		final String string = commentsMessage.getComment_phone();
		SharedPreferences mySharedPreferencestoken = MyApplication
				.getAppContext().getSharedPreferences("token",
						Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		String token = mySharedPreferencestoken.getString("token", "")
				.toString().trim();
		SharedPreferences sharedPreferences1 = MyApplication.getAppContext()
				.getSharedPreferences("phone_number", Activity.MODE_PRIVATE);
		String mycommitperson1 = sharedPreferences1
				.getString("phone_number", "").toString().trim();
		SharedPreferences sharedPreferences2 = MyApplication.getAppContext()
				.getSharedPreferences("phone_number" + mycommitperson1,
						Activity.MODE_PRIVATE);
		String mycommitperson = sharedPreferences2
				.getString("phone_number", "").toString().trim();
		if ("".equals(token)) {
			holder.comments_delete.setVisibility(View.GONE);
		} else {
			if (mycommitperson.equals(string)) {
				holder.comments_delete.setVisibility(View.VISIBLE);
			} else if (!(mycommitperson.equals(string))) {
				holder.comments_delete.setVisibility(View.GONE);
			}
		}

		/*************************************/
		imageLoader.DisplayImage(commentsheader, holder.commentsheader);
		holder.commentsnickname.setText(commentsnickname);
		holder.commentstime.setText(commentstime);
		holder.commnetscontent.setText(commnetscontent);
		holder.comments_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				commentsDelete.onCommentsDelete(position);
			}
		});
		return convertView;
	}

	class ViewHolder {
		LinearLayout linearLayout;

		TextView commnetscontent, commentstime, commentsnickname,
				comments_reply, comments_to_person, comments_, comments_delete;

		ImageView commentsheader;

	}

	public interface CommentsDelete {

		public void onCommentsDelete(int position);

	}

	public void setInterface(CommentsDelete commentsDelete) {

		this.commentsDelete = commentsDelete;
	}

}
