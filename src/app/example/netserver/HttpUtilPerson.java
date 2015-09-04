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

public class HttpUtilPerson {
	private String phone_number;
	private String personcreated, personname, personportrait, personlocation;

	private int position;
	private String id, what, time, latitude, langitude, kind, information,
			commitperson, contactphone, sharenumber, commentsnumber,
			followingnumber, location, created, title, image1, comments,
			image2, image3;
	private int i;



	public HttpUtilPerson(String phone_number) {
		this.phone_number = phone_number;
		
	}

	public void submitAsyncHttpClientGet(final int i, final String id,
			final String what, final String time, final String latitude,
			final String langitude, final String kind,
			final String information, final String commitperson,
			final String contactphone, final String sharenumber,
			final String commentsnumber, final String followingnumber,
			final String location, final String created, final String title,
			final String comments, final String image1, final String image2,
			final String image3) {
		this.i = i;
		this.id = id;
		this.what = what;
		this.time = time;
		this.langitude = langitude;
		this.latitude = latitude;
		this.kind = kind;
		this.information = information;
		this.commitperson = commitperson;
		this.contactphone = contactphone;
		this.sharenumber = sharenumber;
		this.commentsnumber = commentsnumber;
		this.followingnumber = followingnumber;
		this.location = location;
		this.created = created;
		this.title = title;
		this.comments = comments;
		this.image1 = image1;
		this.image2 = image2;
		this.image3 = image3;

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
				// JsonData jsonData = new JsonData();
				// jsonData.parseJSONArray(json, apk_list);

				try {
					JSONObject item = new JSONObject(json);

					personcreated = item.getString("created");
					personname = item.getString("name");
					personportrait = UrlPath.host + item.getString("portrait");
					personlocation = item.getString("location");
					List<Map<String, String>> list = MySQLiteMethodDetails
							.listDb(what);
					if (list.size() < 5) {
//						Log.i("HttpUtilPerson", "所有数据：" + id + "---" + what
//								+ "---" + title + "---" + time + "---"
//								+ latitude + "---" + langitude + "---" + kind
//								+ "---" + information + "---" + commitperson
//								+ "---" + contactphone + "---" + sharenumber
//								+ "---" + commentsnumber + "---"
//								+ followingnumber + "---" + location + "---"
//								+ created + "---" + comments + "---" + image1
//								+ "---" + image2 + "---" + image3 + "---"
//								+ personcreated + "---" + personname + "---"
//								+ personportrait + "---" + personlocation);

						MySQLiteMethodDetails.insertDb(id, what, title, time,
								latitude, langitude, kind, information,
								commitperson, contactphone, sharenumber,
								commentsnumber, followingnumber, location,
								created, comments, image1, image2, image3,
								personcreated, personname, personportrait,
								personlocation);
					} else if (list.size() >= 5) {
						position = i + 1;
//						Log.i("HttpUtilPerson", "所有数据：" + id + "---" + what
//								+ "---" + title + "---" + time + "---"
//								+ latitude + "---" + langitude + "---" + kind
//								+ "---" + information + "---" + commitperson
//								+ "---" + contactphone + "---" + sharenumber
//								+ "---" + commentsnumber + "---"
//								+ followingnumber + "---" + location + "---"
//								+ created + "---" + comments + "---" + image1
//								+ "---" + image2 + "---" + image3 + "---"
//								+ personcreated + "---" + personname + "---"
//								+ personportrait + "---" + personlocation);
						MySQLiteMethodDetails.updataDb(id, what, title, time,
								latitude, langitude, kind, information,
								commitperson, contactphone, sharenumber,
								commentsnumber, followingnumber, location,
								created, comments, image1, image2, image3,
								personcreated, personname, personportrait,
								personlocation, position);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
