package net.loonggg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.example.application.MyApplication;
import app.example.internet.picture.ImageLoader;
import app.example.netserver.PersonData;

public class RecordAdapter extends BaseAdapter {

	ArrayList<PersonData> apk_list;
	LayoutInflater inflater;
	String phoneno;

	private Commentrecord comment;
	private Sharerecord share;
	private DeleteMessagerecord deleteMessage;
	public ImageLoader imageLoader;
	private List<Map<String, String>> list;

	String id, title, time, langitude, latitude, kind, information,
			commitperson, commitorganization, followingperson,
			followingorganization, sharenumber, commentsnumber,
			followingnumber, location, created, comments, contactphone, image1;

	private android.view.animation.Animation animation;

	String followingNumber1;// 点赞数

	public RecordAdapter(Context context, ArrayList<PersonData> apk_list) {
		this.apk_list = apk_list;
		this.inflater = LayoutInflater.from(context);
		imageLoader = new ImageLoader(context);
		animation = AnimationUtils.loadAnimation(context, R.anim.point_zan);
	}

	public void onDateChange(ArrayList<PersonData> apk_list) {
		this.apk_list = apk_list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return apk_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return apk_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final PersonData arraydatas = apk_list.get(position);
		final ViewHolder holder;
		/***************************************************************/
		/**
		 * 获取数据
		 */
		id = arraydatas.getId();

		title = arraydatas.getTitle();
		time = arraydatas.getTime();
		langitude = arraydatas.getLangitude();
		latitude = arraydatas.getLatitude();
		kind = arraydatas.getKind();
		information = arraydatas.getInformation();
		commitperson = arraydatas.getCommitPerson();

		followingnumber = arraydatas.getFollowingNumber();
		location = arraydatas.getLocation();
		created = arraydatas.getCreated();
		comments = arraydatas.getComments();
		contactphone = arraydatas.getContactPhone();
		image1 = arraydatas.getImage1();

		/***************************************************************/
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.lost_record, null);
			holder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.item);
			holder.personheader = (ImageView) convertView
					.findViewById(R.id.head_image);
			holder.personnickname = (TextView) convertView
					.findViewById(R.id.nick);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.publictime = (TextView) convertView
					.findViewById(R.id.public_time);
			holder.label = (TextView) convertView.findViewById(R.id.label);
			holder.titletime = (TextView) convertView.findViewById(R.id.time);
			holder.deletemessage = (ImageView) convertView
					.findViewById(R.id.delete_message);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.titleplace = (TextView) convertView
					.findViewById(R.id.titleplace);
			holder.place = (TextView) convertView.findViewById(R.id.place);
			holder.textdetail = (TextView) convertView
					.findViewById(R.id.textdetail);
			holder.lostpicture = (ImageView) convertView
					.findViewById(R.id.picture);
			holder.comment = (TextView) convertView.findViewById(R.id.comment);
			holder.personlocation = (TextView) convertView.findViewById(R.id.personlocation);
			holder.share = (TextView) convertView.findViewById(R.id.share);

			holder.titleattentionno = (TextView) convertView
					.findViewById(R.id.titleattentionno);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/******************************************/

		holder.personnickname.setText(arraydatas.getPersonname());
		holder.personlocation.setText(arraydatas.getPersonlocation());
		imageLoader.DisplayImage(arraydatas.getPersonportrait(),
				holder.personheader);
		holder.title.setText(title);
		holder.publictime.setText(created);
		holder.time.setText(time);
		holder.place.setText(location);
		holder.textdetail.setText(information);
		Lable.lable(kind, holder.label);

		holder.titleattentionno.setText(followingnumber);

		imageLoader.DisplayImage(image1, holder.lostpicture);

		/*************************************/
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
			holder.deletemessage.setVisibility(View.GONE);
		} else {
			if (mycommitperson.equals(commitperson)) {
				holder.deletemessage.setVisibility(View.VISIBLE);
			} else if (!(mycommitperson.equals(commitperson))) {
				holder.deletemessage.setVisibility(View.GONE);
			}
		}

		/*************************************/

		holder.label.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation anim = AnimationUtils.loadAnimation(
						holder.label.getContext(), R.anim.shake);
				holder.label.startAnimation(anim);
			}
		});

		holder.deletemessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				deleteMessage.ondeletemessage(position);
			}
		});

		holder.comment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				comment.oncomment(position);

			}
		});

		holder.share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				share.onshare(Integer.parseInt(arraydatas.getId()));

			}
		});

		return convertView;
	}

	/***************************/

	public interface Commentrecord {

		public void oncomment(int position);

	}

	public void setInterface(Commentrecord comment) {

		this.comment = comment;
	}

	public interface Sharerecord {

		public void onshare(int position);

	}

	public void setInterface(Sharerecord share) {

		this.share = share;
	}

	/**********************************/
	public interface DeleteMessagerecord {

		public void ondeletemessage(int position);

	}

	public void setInterface(DeleteMessagerecord deleteMessage) {

		this.deleteMessage = deleteMessage;
	}

	class ViewHolder {

		LinearLayout linearLayout;

		TextView title, publictime, label, titletime, time, titleplace, place,
				textdetail, personnickname, titleattentionno,personlocation;

		ImageView lostpicture, deletemessage, personheader;

		TextView comment, share;

	}

}
