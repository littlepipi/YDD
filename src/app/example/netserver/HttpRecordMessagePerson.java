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

public class HttpRecordMessagePerson {
	private String phone_number;
	private String personcreated, personname, personportrait, personlocation;

	private int position;
	private String CordKind;
	private String id, what, time, latitude, langitude, kind, information,
			commitperson, contactphone, sharenumber, commentsnumber,
			followingnumber, location, created, title, image1, comments,
			image2, image3;
	private boolean flag;
	private int i;
	private int num = 0;

	public HttpRecordMessagePerson(String phone_number) {
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
			final String image3, final boolean flag) {
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
		this.flag = flag;

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

					if (flag == true) {
						CordKind = what + "record";
						List<Map<String, String>> list = MySQLiteMethodDetails
								.listCord_Db(CordKind);

//						Log.i("HttpRercordMessage", "所有数据：" + id + "---" + what
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

						int total = list.size();

						if (total == 0) {

							MySQLiteMethodDetails.insertCord_Db(id, what,
									title, time, latitude, langitude, kind,
									information, commitperson, contactphone,
									sharenumber, commentsnumber,
									followingnumber, location, created,
									comments, image1, image2, image3,
									personcreated, personname, personportrait,
									personlocation, CordKind);
						} else {
							for (int i = 0; i < list.size(); i++) {
								String _id = list.get(i).get("id");
								String _followingnumber = list.get(i).get(
										"followingnumber");
								int app_id = Integer.valueOf(
										list.get(i).get("app_id")).intValue();

								if (!(id.equals(_id))) {
									num = num + 1;

								} else {
									if (_followingnumber
											.equals(followingnumber)) {

									} else {
										MySQLiteMethodDetails.deleteCord_Db(
												CordKind, app_id);

										
										MySQLiteMethodDetails.insertCord_Db(id,
												what, title, time, latitude,
												langitude, kind, information,
												commitperson, contactphone,
												sharenumber, commentsnumber,
												followingnumber, location,
												created, comments, image1,
												image2, image3, personcreated,
												personname, personportrait,
												personlocation, CordKind);
									}
								}

							}
							if (num == total) {

								MySQLiteMethodDetails.insertCord_Db(id, what,
										title, time, latitude, langitude, kind,
										information, commitperson,
										contactphone, sharenumber,
										commentsnumber, followingnumber,
										location, created, comments, image1,
										image2, image3, personcreated,
										personname, personportrait,
										personlocation, CordKind);
							}
						}
					} else if (flag == false) {
						CordKind = what + "personal";
						List<Map<String, String>> list = MySQLiteMethodDetails
								.listPersonal_Db(CordKind);
//
//						Log.i("HttppersonalMessage", "所有数据：" + id + "---"
//								+ what + "---" + title + "---" + time + "---"
//								+ latitude + "---" + langitude + "---" + kind
//								+ "---" + information + "---" + commitperson
//								+ "---" + contactphone + "---" + sharenumber
//								+ "---" + commentsnumber + "---"
//								+ followingnumber + "---" + location + "---"
//								+ created + "---" + comments + "---" + image1
//								+ "---" + image2 + "---" + image3 + "---"
//								+ personcreated + "---" + personname + "---"
//								+ personportrait + "---" + personlocation);

						int total = list.size();

						if (total == 0) {

							MySQLiteMethodDetails.insertPersonal_Db(id, what,
									title, time, latitude, langitude, kind,
									information, commitperson, contactphone,
									sharenumber, commentsnumber,
									followingnumber, location, created,
									comments, image1, image2, image3,
									personcreated, personname, personportrait,
									personlocation, CordKind);
						} else {
							for (int i = 0; i < list.size(); i++) {
								String _id = list.get(i).get("id");

								if (!(id.equals(_id))) {
									num = num + 1;

								}

							}
							if (num == total) {

								MySQLiteMethodDetails.insertPersonal_Db(id,
										what, title, time, latitude, langitude,
										kind, information, commitperson,
										contactphone, sharenumber,
										commentsnumber, followingnumber,
										location, created, comments, image1,
										image2, image3, personcreated,
										personname, personportrait,
										personlocation, CordKind);
							}
						}
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

//				Log.i("HttpRecordMessage", "失败失败！！！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
//				Log.i("HttpRecordMessage", "成功成功!!!!");
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
