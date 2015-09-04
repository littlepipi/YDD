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
import app.example.Url.UrlPath;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HttpcommentToNewsPerson {
	private String phone_number;
	private String personcreated, personname, personportrait, personlocation;

	private String id, time, information, commitperson, title, image1, image2,
			image3, sharenumber, commentsnumber, followingnumber;

	private int num = 0;
	@SuppressWarnings("unused")
	private int i;

	public HttpcommentToNewsPerson(String phone_number) {
		this.phone_number = phone_number;

	}

	public void submitAsyncHttpClientGet(final int i, final String id,
			final String time, final String information,
			final String commitperson, final String title, final String image1,
			final String image2, final String image3, final String sharenumber,
			final String commentsnumber, final String followingnumber) {
		this.i = i;
		this.id = id;
		this.time = time;
		this.information = information;
		this.commitperson = commitperson;
		this.title = title;
		this.image1 = image1;
		this.image2 = image2;
		this.image3 = image3;
		this.sharenumber = sharenumber;
		this.commentsnumber = commentsnumber;
		this.followingnumber = followingnumber;

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
							.list_news_Db();

//					Log.i("HttpNewsPerson", "所有数据：" + id + "---" + title
//							+ "---" + information + "---" + commitperson
//							+ "---" + image1 + "---" + image2 + "---" + image3
//							+ "---" + personcreated + "---" + personname
//							+ "---" + personportrait + "---" + personlocation
//							+ "---" + sharenumber + "---" + commentsnumber
//							+ "---" + followingnumber);
					int total = list.size();

					if (total == 0) {

						MySQLiteMethodDetails.insert_news_Db(id, title, time,
								information, commitperson, image1, image2,
								image3, personcreated, personname,
								personportrait, personlocation, sharenumber,
								commentsnumber, followingnumber);
						total = 1;
					} else {
						for (int i = 0; i < list.size(); i++) {
							String _id = list.get(i).get("id");

							if (!(id.equals(_id))) {
								num = num + 1;

							}

						}
						if (num == total) {

							MySQLiteMethodDetails.insert_news_Db(id, title,
									time, information, commitperson, image1,
									image2, image3, personcreated, personname,
									personportrait, personlocation,
									sharenumber, commentsnumber,
									followingnumber);
						}
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

//				Log.i("HttpNewsMessagePerson", "失败失败！！！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
				// TODO Auto-generated method stub
//				Log.i("HttpNewsMessagePerson", "成功成功!!!!");
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
