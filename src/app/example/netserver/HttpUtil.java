package app.example.netserver;

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

public class HttpUtil {
	private int start;
	private int end;
	private String address;
	private String httpkind;
	private String somekind;
	String id, what, losttime, lostlatitude, lostlangitude, kind, information,
			commitperson, contactphone, sharenumber, commentsnumber,
			followingnumber, location, created, title, comments, latlng,
			image1, image2, image3;
	
	private int i;

	public HttpUtil(int start, int end, String address, String httpkind,
			String somekind) {
		this.start = start;
		this.end = end;
		this.address = address;
		this.httpkind = httpkind;
		this.somekind = somekind;

	}

	public boolean submitAsyncHttpClientGet() {
		// 创建异步请求端对象
		AsyncHttpClient client = new AsyncHttpClient();

		String urlr = UrlPath.messagesGetApi;

		String url = urlr + "?" + "location=" + address + "&" + "start="
				+ start + "&" + "end=" + end + "&what=" + httpkind
				+ "&kind=" + somekind;
    
		// 发送get请求对象
		client.get(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				if (httpkind.equals("lost")) {
					SharedPreferences mySharedPreferences = MyApplication
							.getAppContext().getSharedPreferences(
									"HttpUtil_lost", Activity.MODE_PRIVATE);
					int a = response.length();
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// 用putString的方法保存数据
					editor.putInt("HttpUtil_lost", a);
					// 提交当前数据
					editor.commit();
				} else if (httpkind.equals("find")) {
					SharedPreferences mySharedPreferences = MyApplication
							.getAppContext().getSharedPreferences(
									"HttpUtil_find", Activity.MODE_PRIVATE);
					int a = response.length();
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// 用putString的方法保存数据
					editor.putInt("HttpUtil_find", a);
					// 提交当前数据
					editor.commit();
				}

				try {

					JSONObject item;
					for (i = 0; i < response.length(); i++) {
						item = response.getJSONObject(i);

						id = item.getString("id");
						
						HttpComments myHttpComments = new HttpComments(id,"message");
						myHttpComments.submitAsyncHttpClientGet();
						/*********************************************/
						what = item.getString("what");
						String losttimestart = item.getString("time");
						latlng = item.getString("latlng");
						kind = item.getString("kind");
						title = item.getString("title");
						information = item.getString("information");
						commitperson = item.getString("commit_person");

						contactphone = item.getString("contact_phone");
						sharenumber = item.getString("share_number");
						followingnumber = item.getString("reading_number");
						commentsnumber = item.getString("comments_number");
						location = item.getString("location");

						String lostimagestart = item.getString("image1");
						String pathpopu = UrlPath.host;
						image1 = pathpopu + lostimagestart;
						String lostimagestart1 = item.getString("image2");
						image2 = pathpopu + lostimagestart1;
						String lostimagestart3 = item.getString("image3");
						image3 = pathpopu + lostimagestart3;
						String createdstart = item.getString("created");

						String losttimemiddle = losttimestart.substring(0,
								losttimestart.length());
						losttime = losttimemiddle.replace("T", " ");

						String createdmiddle = createdstart.substring(0,
								losttimestart.length());
						created = createdmiddle.replace("T", " ");

						String[] sp = { "titude:", "gitude:" };// 关键字数组
						for (int k = 0; k < sp.length; k++) {
							String value = sp[k];// 取出一个关键字

							String ee = "," + value + ",";// 关键字前后加逗号
							latlng = latlng.replaceAll(value, ee);// 这句很关键，replaceAll有返回值，如不把返回值给vv，vv的值不会变
						}
						String[] result = latlng.split(",");// 按照逗号分割

						lostlatitude = result[2];
						lostlangitude = result[5];
						comments =null;

						HttpUtilPerson httpPersonMessage = new HttpUtilPerson(
								commitperson);
						httpPersonMessage.submitAsyncHttpClientGet(i, id, what,
								losttime, lostlatitude, lostlangitude, kind,
								information, commitperson, contactphone,
								sharenumber, commentsnumber, followingnumber,
								location, created, title, comments, image1,
								image2, image3);
						httpPersonMessage.submitAsyncHttpClientGet();

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				if (httpkind.equals("lost")) {
					SharedPreferences mySharedPreferences = MyApplication
							.getAppContext().getSharedPreferences(
									"HttpUtil_lost", Activity.MODE_PRIVATE);
					
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// 用putString的方法保存数据
					editor.putInt("HttpUtil_lost", -1);
					// 提交当前数据
					editor.commit();
				} else if (httpkind.equals("find")) {
					SharedPreferences mySharedPreferences = MyApplication
							.getAppContext().getSharedPreferences(
									"HttpUtil_find", Activity.MODE_PRIVATE);
					
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// 用putString的方法保存数据
					editor.putInt("HttpUtil_find", -1);
					// 提交当前数据
					editor.commit();
				}
			}
		});
		return true;

	}

}
