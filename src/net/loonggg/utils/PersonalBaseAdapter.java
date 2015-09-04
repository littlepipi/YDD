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
import android.widget.ToggleButton;
import app.example.application.MyApplication;
import app.example.internet.picture.ImageLoader;
import app.example.netserver.MySQLiteMethodDetails;
import app.example.netserver.PersonData;

public class PersonalBaseAdapter extends BaseAdapter {
	ArrayList<PersonData> apk_list;
	LayoutInflater inflater;
	String phoneno;

	private Phone phone;
	private ShortMessage message;
	private Mappicture mappicture;

	private Comment comment;
	private Share share;

	public ImageLoader imageLoader;
	private List<Map<String, String>> list;
	@SuppressWarnings("unused")
	private Map<String, String> map;
	String id, title, time, langitude, latitude, kind, information,
			commitperson, sharenumber, commentsnumber, followingnumber,
			location, created, comments, contactphone, image1;

	private android.view.animation.Animation animation;

	String followingNumber1;// 点赞数

	public PersonalBaseAdapter(Context context, ArrayList<PersonData> apk_list) {
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

		return apk_list.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final PersonData arraydatas = apk_list.get(position);
		final ViewHolder holder;
		/***************************************************************/
		/**
		 * 获取数据
		 */
		id = arraydatas.getId();
		map = MySQLiteMethodDetails.map_fans_DB(id);

		list = MySQLiteMethodDetails.list_fans_Db();

		int _list = 0;
		String _id;
		if (list.isEmpty()) {
			MySQLiteMethodDetails.insert_fans_Db(id, Constant.NO);
		} else {
			for (int i = 0; i < list.size(); i++) {
				_id = list.get(i).get("id");
				if (!id.equals(_id)) {
					_list = _list + 1;
				}
			}
			if (_list == list.size()) {
				MySQLiteMethodDetails.insert_fans_Db(id, Constant.NO);

			}
		}

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
			convertView = inflater.inflate(R.layout.fragment_home_page_layout,
					null);
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
			holder.titletime = (TextView) convertView
					.findViewById(R.id.titletime);
			holder.deletemessage = (ImageView) convertView
					.findViewById(R.id.delete_message);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.titleplace = (TextView) convertView
					.findViewById(R.id.titleplace);
			holder.place = (TextView) convertView.findViewById(R.id.place);
			holder.textdetail = (TextView) convertView
					.findViewById(R.id.textdetail);
			holder.personlocation = (TextView) convertView
					.findViewById(R.id.personlocation);

			holder.titleattention = (TextView) convertView
					.findViewById(R.id.titleattention);
			holder.titleattentionno = (TextView) convertView
					.findViewById(R.id.titleattentionno);

			holder.lostpicture = (ImageView) convertView
					.findViewById(R.id.picture);
			holder.mappictureImageView = (TextView) convertView
					.findViewById(R.id.mappicture);
			holder.phone = (TextView) convertView.findViewById(R.id.phone_number);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.comment = (TextView) convertView.findViewById(R.id.comment);

			holder.share = (TextView) convertView.findViewById(R.id.share);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.personlocation.setText(arraydatas.getPersonlocation());
		holder.personnickname.setText(arraydatas.getPersonname());

		imageLoader.DisplayImage(arraydatas.getPersonportrait(),
				holder.personheader);

		holder.title.setText(title);
		holder.publictime.setText(created);
		holder.time.setText(time);
		holder.place.setText(location);
		holder.textdetail.setText(information);
		Lable.lable(kind, holder.label);
		imageLoader.DisplayImage(image1, holder.lostpicture);

		holder.titleattentionno.setText(followingnumber);
		/*************************************/
		final String string = arraydatas.getContactPhone();

		SharedPreferences mySharedPreferencestoken = MyApplication
				.getAppContext().getSharedPreferences("token",
						Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值

		holder.deletemessage.setVisibility(View.GONE);

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

		holder.phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				phone.onphone(string);
			}

		});
		holder.message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				message.onmessage(string);
			}
		});

		holder.mappictureImageView
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						mappicture.onmappicture(position);

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
	public void setInterface(Phone phone) {
		this.phone = phone;
	}

	public interface Phone {
		public void onphone(String string);
	}

	public void setInterface(ShortMessage message) {
		this.message = message;
	}

	public interface ShortMessage {
		public void onmessage(String string);
	}

	public void setInterface(Mappicture mappicture) {

		this.mappicture = mappicture;
	}

	public interface Mappicture {

		public void onmappicture(int position);
	}

	public interface Comment {

		public void oncomment(int position);

	}

	public void setInterface(Comment comment) {

		this.comment = comment;
	}

	public interface Share {

		public void onshare(int id);

	}

	public void setInterface(Share share) {

		this.share = share;
	}

	class ViewHolder {
		LinearLayout linearLayout;

		TextView title, publictime, label, titletime, time, titleplace, place,
				textdetail, titlecomment, titleattention, titleattentionno,
				titleshare, personnickname,personlocation;

		ImageView lostpicture, deletemessage, personheader;

		TextView phone, message, comment, mappictureImageView, share;
		ToggleButton fans;
	}
}
