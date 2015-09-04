package app.example.netserver;

import net.loonggg.utils.DateUtil;
import net.loonggg.utils.DateUtil.DateFormatWrongException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HttpNewsMessage {
	private int start;
	private int end;

	String id, information, commitperson, title, latlng, image1, image2, time,
			image3, sharenumber, commentsnumber, followingnumber;
	JSONArray followers;

	private int i;

	public HttpNewsMessage(int start, int end) {
		this.start = start;
		this.end = end;

	}

	public void submitAsyncHttpClientGet() {
		// 创建异步请求端对象
		AsyncHttpClient client = new AsyncHttpClient();

		String urlr = UrlPath.newsGetApi;

		String url = urlr + "?" + "start=" + start + "&" + "end=" + end;

		// 发送get请求对象
		client.get(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				int a = response.length();
				SharedPreferences mySharedPreferences = MyApplication
						.getAppContext().getSharedPreferences(
								"HttpNewsMessage", Activity.MODE_PRIVATE);
				// 实例化SharedPreferences.Editor对象（第二步）
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// 用putString的方法保存数据
				editor.putInt("HttpNewsMessage", a);
				// 提交当前数据
				editor.commit();

				try {

					JSONObject item;

					for (i = 0; i < response.length(); i++) {
						item = response.getJSONObject(i);
						id = item.getString("id");

						try {
							time = DateUtil.toSimpleDate(item
									.getString("created"));
						} catch (DateFormatWrongException e) {

							e.printStackTrace();
						}
						HttpComments myHttpComments = new HttpComments(id,
								"news");
						myHttpComments.submitAsyncHttpClientGet();

						title = item.getString("title");
						information = item.getString("information");
						commitperson = item.getString("commit_person");
						String lostimagestart = item.getString("image1");
						sharenumber = item.getString("reading_number");
						followingnumber = item.getString("following_number");
						followers = item.getJSONArray("followers");
						commentsnumber = "1";
						

						String pathpopu = UrlPath.host;
						image1 = pathpopu + lostimagestart;
						String lostimagestart1 = item.getString("image2");
						image2 = pathpopu + lostimagestart1;
						String lostimagestart3 = item.getString("image3");
						image3 = pathpopu + lostimagestart3;

						HttpNewsMessagePerson httpNewsMessagePerson = new HttpNewsMessagePerson(
								commitperson);
						httpNewsMessagePerson.submitAsyncHttpClientGet(i, id,
								time, information, commitperson, title, image1,
								image2, image3, sharenumber, commentsnumber,
								followingnumber);
						httpNewsMessagePerson.submitAsyncHttpClientGet();

					}
				}

				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				SharedPreferences mySharedPreferences = MyApplication
						.getAppContext().getSharedPreferences(
								"HttpNewsMessage", Activity.MODE_PRIVATE);
				// 实例化SharedPreferences.Editor对象（第二步）
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// 用putString的方法保存数据
				editor.putInt("HttpNewsMessage", -1);
				// 提交当前数据
				editor.commit();
			}

		});
		return;

	}

}
