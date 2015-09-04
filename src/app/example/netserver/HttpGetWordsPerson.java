package app.example.netserver;

import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import app.example.Url.UrlPath;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HttpGetWordsPerson {
	private String phone_number;
	private String personcreated, personname, personportrait, personlocation;

	private int position;
	private String id, created, from_person, content, if_read;
	private int i;

	// private ArrayList<PersonData> apk_list;

	public HttpGetWordsPerson(String phone_number) {
		this.phone_number = phone_number;
		// this.apk_list = apk_list;
	}

	// id, created, from_person, content, if_read
	public void submitAsyncHttpClientGet(final int i, final String id,
			final String created, final String from_person,
			final String content, final String if_read) {
		this.i = i;
		this.id = id;
		this.created = created;
		this.from_person = from_person;
		this.content = content;
		this.if_read = if_read;

	}

	@SuppressLint("HandlerLeak")
	public void submitAsyncHttpClientGet() {
		// 创建异步请求端对象

		AsyncHttpClient client = new AsyncHttpClient();

		String urlr = UrlPath.getMemberApi;

		String url = urlr + "?" + "phone_number=" + phone_number;

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				String json = msg.getData().getString("responseBody");
				

				try {
					JSONObject item = new JSONObject(json);

					personcreated = item.getString("created");
					personname = item.getString("name");
					personportrait = UrlPath.host + item.getString("portrait");
					personlocation = item.getString("location");
					List<Map<String, String>> list = MySQLiteMethodDetails
							.listWords_Db();
					if (list.size() < 10) {
//						Log.i("HttpUtilPerson", "所有数据：" + id + "---" + created
//								+ "---" + from_person + "---" + content + "---"
//								+ if_read + "---" + personcreated + "---"
//								+ personname + "---" + personportrait + "---"
//								+ personlocation);

						MySQLiteMethodDetails.insertWords_Db(id, created,
								from_person, content, if_read, personcreated,
								personname, personportrait, personlocation);
					} else if (list.size() >= 10) {
						position = i + 1;
//						Log.i("HttpUtilPerson", "所有数据：" + id + "---" + created
//								+ "---" + from_person + "---" + content + "---"
//								+ if_read + "---" + personcreated + "---"
//								+ personname + "---" + personportrait + "---"
//								+ personlocation);
						MySQLiteMethodDetails.updataWords_Db(id, created,
								from_person, content, if_read, personcreated,
								personname, personportrait, personlocation,
								position);
					}

				} catch (JSONException e) {
					
					e.printStackTrace();
				}

			}
		};

		// 发送get请求对象
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

//				Log.i("HttpUtilPerson", "失败失败！！！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
				// TODO Auto-generated method stub
//				Log.i("HttpUtilPerson", "成功成功!!!!");
				String iString = new String(responseBody);
				Bundle bundle = new Bundle();
				bundle.putString("responseBody", iString);
				Message message = new Message();
				message.setData(bundle);
				handler.sendMessage(message);

			}
		});
		return;

	}

}
