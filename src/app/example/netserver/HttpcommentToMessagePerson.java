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

public class HttpcommentToMessagePerson {
	private String phone_number;
	private String personcreated, personname, personportrait, personlocation;

	private int position;
	private String id, what, time, latitude, langitude, kind, information,
			commitperson, contactphone, sharenumber, commentsnumber, comments,
			followingnumber, location, created, title, image1, image2, image3;
	private int i;
	private int num = 0;
	// private ArrayList<PersonData> apk_list;

	public HttpcommentToMessagePerson(String phone_number) {
		this.phone_number = phone_number;
		// this.apk_list = apk_list;
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
		Log.i("HttpUI", "第三站点");

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
								.list_Message();
						
						int total = list.size();

						if (total == 0) {
	                      
							MySQLiteMethodDetails.insert_Message(id, what, title,
									time, latitude, langitude, kind, information,
									commitperson, contactphone, sharenumber,
									commentsnumber, followingnumber, location,
									created, comments, image1, image2, image3,
									personcreated, personname, personportrait,
									personlocation);
							total=1;
						} else {
							for (int i = 0; i < list.size(); i++) {
								String _id = list.get(i).get("id");
								
									if (!(_id.equals(id))) {
										num = num + 1;

									}
								}

							}
							if (num == total) {
								 
								MySQLiteMethodDetails.insert_Message(id, what, title,
										time, latitude, langitude, kind, information,
										commitperson, contactphone, sharenumber,
										commentsnumber, followingnumber, location,
										created, comments, image1, image2, image3,
										personcreated, personname, personportrait,
										personlocation);
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

//				Log.i("HttpCommentToMessage", "失败失败！！！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
				// TODO Auto-generated method stub
//				Log.i("HttpCommentToMessage", "成功成功!!!!");
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
