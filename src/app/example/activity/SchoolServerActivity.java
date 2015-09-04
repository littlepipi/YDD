package app.example.activity;

import java.util.ArrayList;

import net.loonggg.utils.AutoListView;
import net.loonggg.utils.CheckNet;
import net.loonggg.utils.SchoolServerAdapter;
import net.loonggg.utils.SchoolServerPersonData;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import app.example.Url.UrlPath;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SchoolServerActivity extends Activity implements
		OnItemClickListener {
	private String name;
	private String school;
	private String grade;
	private String slogan;
	private String introduction;
	private String activities;
	private ListView listview;
	private ArrayList<SchoolServerPersonData> list;
	private SchoolServerAdapter adapter;
	private ImageButton back;
	private LinearLayout nodata, no_net_cord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_server);
		list = new ArrayList<SchoolServerPersonData>();
		init();// 控件初始化
		boolean network = CheckNet.detect(SchoolServerActivity.this);

		if (network) {
			
			submitAsyncHttpClientGet(list);
			
		} else {

			no_net_cord.setVisibility(View.VISIBLE);

		}

	}

	private void init() {
		// TODO Auto-generated method stub
		no_net_cord = (LinearLayout) findViewById(R.id.no_net_cord);
		nodata = (LinearLayout) findViewById(R.id.nodata);
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void init(final ArrayList<SchoolServerPersonData> list) {
		if (adapter == null) {
			listview = (ListView) findViewById(R.id.schoolserver);
			adapter = new SchoolServerAdapter(SchoolServerActivity.this, list);
			listview.setOnItemClickListener(this);
			listview.setAdapter(adapter);
		} else {
			adapter.onDateChange(list);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub

		SchoolServerPersonData persondata = list.get(position);
		String name = persondata.getName();
		String school = persondata.getSchool();
		String grade = persondata.getGrade();
		String slogan = persondata.getSlogan();
		String introduction = persondata.getIntroduction();
		String activities = persondata.getActivities();

		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		bundle.putString("school", school);
		bundle.putString("grade", grade);
		bundle.putString("slogan", slogan);
		bundle.putString("introduction", introduction);
		bundle.putString("activities", activities);

		Intent intent = new Intent(SchoolServerActivity.this,
				SchoolServerPersonActivity.class);
		intent.putExtras(bundle);
		SchoolServerActivity.this.startActivity(intent);

	}

	public boolean submitAsyncHttpClientGet(final ArrayList<SchoolServerPersonData> list) {
		// 创建异步请求端对象
		AsyncHttpClient client = new AsyncHttpClient();

		String url = UrlPath.schoolserverApi;

		// 发送get请求对象
		client.get(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				
				JSONObject item;
				for (int i = 0; i < response.length(); i++) {
					try {

						item = response.getJSONObject(i);
						name = item.getString("name");
						school = item.getString("school");
						grade = item.getString("grade");
						slogan = item.getString("slogan");
						introduction = item.getString("resume");
						activities = item.getString("experience");

						SchoolServerPersonData data = new SchoolServerPersonData();

						data.setName(name);
						data.setSchool(school);
						data.setGrade(grade);
						data.setSlogan(slogan);
						data.setIntroduction(introduction);
						data.setActivities(activities);
						list.add(data);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				if (list.size() == 0) {
					nodata.setVisibility(View.VISIBLE);
				} else {
					init(list);// 适配器初始化
				}
			}
		});
		return true;

	}
	
	
}
