package net.loonggg.utils;

import java.util.ArrayList;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.example.application.MyApplication;
import app.example.internet.picture.ImageLoader;

public class PersonalDiscoverAdapter extends BaseAdapter {
	ArrayList<DiscoverMessageData> discover_message;
	LayoutInflater inflater;
	String id, title, time, information, commitperson, nickname,
			personportrait, commentsno, sharesno,followingnumber,looknumber;
	private DeleteMessagediscover deleteMessage;
	private Commentdiscover comment;
	private Sharediscover share;
	private Care care;
	public ImageLoader imageLoader;
	int followingNumbers;

	public PersonalDiscoverAdapter(Context context,
			ArrayList<DiscoverMessageData> discover_message) {
		this.discover_message = discover_message;
		this.inflater = LayoutInflater.from(context);
		imageLoader = new ImageLoader(context);

	}

	public void onDateChange(ArrayList<DiscoverMessageData> discover_message) {
		this.discover_message = discover_message;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return discover_message.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return discover_message.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final DiscoverMessageData discoverMessageData = discover_message
				.get(position);
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.discoverrecord, null);
			holder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.item);

			holder.personheader = (ImageView) convertView
					.findViewById(R.id.head_image);
			holder.personnickname = (TextView) convertView
					.findViewById(R.id.nick);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.deletemessage = (ImageView) convertView
					.findViewById(R.id.delete_message);
			holder.textdetail = (TextView) convertView
					.findViewById(R.id.textdetail);
			holder.discoverpicture = (ImageView) convertView
					.findViewById(R.id.picture);
			holder.comment = (TextView) convertView
					.findViewById(R.id.discover_comment);
			holder.share = (TextView) convertView
					.findViewById(R.id.discover_share);
			holder.personlocation = (TextView) convertView
					.findViewById(R.id.personlocation);
		
					
			holder.looknum = (TextView) convertView
					.findViewById(R.id.titleattentionno);
		
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		id = discoverMessageData.getId();
		time = discoverMessageData.getTime();
		title = discoverMessageData.getTitle();
		information = discoverMessageData.getInformation();
		nickname = discoverMessageData.getPersonname();
		personportrait = discoverMessageData.getPersonportrait();
		commitperson = discoverMessageData.getCommitPerson();
		followingnumber = discoverMessageData.getFollowingNumber();
		looknumber = discoverMessageData.getShareNumber();
		holder.personnickname.setText(nickname);

		imageLoader.DisplayImage(discoverMessageData.getPersonportrait(),
				holder.personheader);
		holder.time.setText(time);
		holder.title.setText(title);
		holder.textdetail.setText(information);

		imageLoader.DisplayImage(discoverMessageData.getImage1(),
				holder.discoverpicture);

		holder.looknum.setText(looknumber);
		holder.personlocation.setText(discoverMessageData.getPersonlocation());

		/*************************************/
		SharedPreferences mySharedPreferencestoken = MyApplication
				.getAppContext().getSharedPreferences("token",
						Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值

		holder.deletemessage.setVisibility(View.GONE);

		/*************************************/

		holder.comment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				comment.oncomment(position);

			}
		});

		holder.share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				share.onshare(Integer.parseInt(discoverMessageData.getId()));

			}
		});

		

		holder.deletemessage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				deleteMessage.ondeletemessage(position);
			}
		});
		return convertView;

	}

	/**********************************/
	public interface DeleteMessagediscover {

		public void ondeletemessage(int position);

	}

	public void setInterface(DeleteMessagediscover deleteMessage) {

		this.deleteMessage = deleteMessage;
	}

	public interface Commentdiscover {

		public void oncomment(int position);

	}

	public void setInterface(Commentdiscover comment) {

		this.comment = comment;
	}

	public interface Sharediscover {

		public void onshare(int id);

	}

	public void setInterface(Sharediscover share) {

		this.share = share;
	}

	public void setInterface(Care care) {
		this.care = care;

	}

	public interface Care {
		public void oncare(boolean lean, String id);
	}

	class ViewHolder {
		LinearLayout linearLayout;

		TextView title, time, textdetail, personnickname, titlecommentno,
				titleshareno, titlelikenno, looknum,personlocation;

		ImageView discoverpicture, deletemessage, personheader;

		TextView comment, share;
		

	}
}
