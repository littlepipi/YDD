package app.example.activity;

import java.util.ArrayList;

import net.loonggg.utils.AddSystemMessage;
import net.loonggg.utils.NetManager;
import net.loonggg.utils.SystemMessage;
import net.loonggg.utils.SystemMessageAdapter;
import tx.ydd.app.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import app.example.application.MyApplication;
import app.example.internet.picture.ImageLoader;

/**
 * @author Administrator
 */
public class MessageSystem extends Activity {

	private ListView list_message;

	private SystemMessageAdapter systemMessageAdapter;

	public ImageLoader imageLoader;

	private ArrayList<SystemMessage> system_num;

	private ImageButton back;

	private LinearLayout no_data_cord;

	private LinearLayout no_net_cord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_message);
		ReceiveData();
		if (NetManager.isOpenNetwork(MessageSystem.this)) {
			no_net_cord.setVisibility(View.GONE);
			if (system_num.size() == 0) {
				no_data_cord.setVisibility(View.VISIBLE);
				list_message.setVisibility(View.GONE);
			} else {
				no_data_cord.setVisibility(View.GONE);
				list_message.setVisibility(View.VISIBLE);
			}
			showList(system_num);
		} else {
			no_net_cord.setVisibility(View.VISIBLE);
			list_message.setVisibility(View.GONE);
		}

		onBottonClick();
	}

	private void showList(final ArrayList<SystemMessage> system_num) {
		if (systemMessageAdapter == null) {

			systemMessageAdapter = new SystemMessageAdapter(MessageSystem.this,
					system_num);

			list_message.setAdapter(systemMessageAdapter);
			list_message.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

				}
			});
		} else {
			systemMessageAdapter.onDateChange(system_num);
		}
	}

	public void ReceiveData() {
		back = (ImageButton) findViewById(R.id.post_lost_message_back1);
		list_message = (ListView) findViewById(R.id.system_listview);
		no_data_cord = (LinearLayout) findViewById(R.id.no_data_cord);
		no_net_cord = (LinearLayout) findViewById(R.id.no_net_cord);
		AddSystemMessage addSystemMessage = new AddSystemMessage();
		system_num = addSystemMessage.addSystemMessages();
		SharedPreferences messageSystem = MyApplication.getAppContext()
				.getSharedPreferences("system_inte", Activity.MODE_PRIVATE);
		messageSystem.edit().clear().commit();
	}

	public void onBottonClick() {

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MessageSystem.this.finish();

			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			MessageSystem.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
