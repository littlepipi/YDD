package app.example.activity;

import java.util.ArrayList;

import net.loonggg.utils.AddCommentsToMessage;
import net.loonggg.utils.CommentsAdapter;
import net.loonggg.utils.CommentsAdapter.CommentsDelete;
import net.loonggg.utils.CommentsMessage;
import net.loonggg.utils.CommentsToMessageListView;
import net.loonggg.utils.MyAlertDialog;
import net.loonggg.utils.NetManager;
import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import app.example.Anim.CustomProgressDialog;
import app.example.Delete.DeleteMessageCommentsData;
import app.example.application.MyApplication;
import app.example.internet.picture.ImageLoader;
import app.example.netserver.HttpRecord;
import app.example.netserver.MySQLiteMethodDetails;

/**
 * @author Administrator
 */
public class MessageComments extends Activity implements CommentsDelete {

	private CommentsToMessageListView list_message;

	private String id;
	private String Comments_Kind;
	private CommentsAdapter commentsAdapter;

	public ImageLoader imageLoader;

	private ArrayList<CommentsMessage> comments_num;

	private ImageButton back;

	private LinearLayout no_data_cord;

	private LinearLayout no_net_cord;

	private CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.commentstomessage);
		ReceiveData();
		if (NetManager.isOpenNetwork(MessageComments.this)) {
			no_net_cord.setVisibility(View.GONE);
			if (comments_num.size() == 0) {
				no_data_cord.setVisibility(View.VISIBLE);
				list_message.setVisibility(View.GONE);
			} else {
				no_data_cord.setVisibility(View.GONE);
				list_message.setVisibility(View.VISIBLE);
			}
			showList(comments_num);

		} else {
			no_net_cord.setVisibility(View.VISIBLE);
			list_message.setVisibility(View.GONE);
		}

		onBottonClick();

	}

	private void showList(final ArrayList<CommentsMessage> comments_num) {
		if (commentsAdapter == null) {

			commentsAdapter = new CommentsAdapter(MessageComments.this,
					comments_num);
			commentsAdapter.setInterface(this);
			list_message.setAdapter(commentsAdapter);
			list_message.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					CommentsMessage commentsMessage = comments_num
							.get(position);
					String id = commentsMessage.get_id();
					Comments_Kind = commentsMessage.getCommentkind();
					if ("message".equals(Comments_Kind)) {

						Intent intent = new Intent(MessageComments.this,
								CommentsToMessageDetail.class);
						intent.putExtra("id", id);
						MessageComments.this.startActivity(intent);
					} else if ("news".equals(Comments_Kind)) {

						Intent intent = new Intent(MessageComments.this,
								CommentsToMessageDiscoverDetailDetails.class);
						intent.putExtra("id", id);
						MessageComments.this.startActivity(intent);
					}

				}
			});
		} else {
			commentsAdapter.onDateChange(comments_num);
		}
	}

	private void ShowPickDialog(final int position) {

		new MyAlertDialog(MessageComments.this).builder().setTitle("提示！")
				.setMsg("确定删除这条评论？表后悔哦！")
				.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(View v) {

						CommentsMessage commentsMessage = comments_num
								.get(position);
						String comment_id = commentsMessage.getComment_id();
						String app_id = commentsMessage.getComment_app_id();
						DeleteMessageCommentsData deleteCommentsData = new DeleteMessageCommentsData(
								id, comment_id);
//						Log.i("删除删除！！！", "" + deleteCommentsData);
						int _id = Integer.valueOf(app_id).intValue();
//						Log.i("app_id", "" + _id);
						MySQLiteMethodDetails.deleteComment_Db(_id);

						comments_num.remove(position);
						commentsAdapter.notifyDataSetChanged();

					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				}).show();

	}

	public void ReceiveData() {
		back = (ImageButton) findViewById(R.id.post_lost_message_back1);
		list_message = (CommentsToMessageListView) findViewById(R.id.list_message);
		no_data_cord = (LinearLayout) findViewById(R.id.no_data_cord);
		no_net_cord = (LinearLayout) findViewById(R.id.no_net_cord);
		AddCommentsToMessage addCommentsToMessage = new AddCommentsToMessage();
		comments_num = addCommentsToMessage.addCommentsToMessages();

		SharedPreferences messageComment = MyApplication.getAppContext()
				.getSharedPreferences("comment_inte", Activity.MODE_PRIVATE);
		messageComment.edit().clear().commit();

	}

	public void onBottonClick() {

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MessageComments.this.finish();
			}
		});

	}

	@Override
	public void onCommentsDelete(int position) {
		ShowPickDialog(position);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			MessageComments.this.finish();

		}
		return super.onKeyDown(keyCode, event);
	}

}
