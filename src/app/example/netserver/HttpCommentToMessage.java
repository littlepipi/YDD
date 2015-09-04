package app.example.netserver;

import net.loonggg.utils.DateUtil;
import net.loonggg.utils.DateUtil.DateFormatWrongException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import app.example.Url.UrlPath;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HttpCommentToMessage {
	private String _id, messagekind;

	String id, what, losttime, lostlatitude, lostlangitude, kind, information,
			commitperson, contactphone, sharenumber, commentsnumber, comments,
			followingnumber, location, created, title, latlng, image1, image2,
			image3;

	private int i;
	private String urlr;
	public HttpCommentToMessage(String _id, String messagekind) {
		this._id = _id;
		this.messagekind = messagekind;
	}

	public void submitHttpClientGet() {
		// 创建异步请求端对象
		AsyncHttpClient client = new AsyncHttpClient();
		if ("message".equals(messagekind)) {
		 urlr = UrlPath.singlemessageApi;
		}else if ("news".equals(messagekind)) {
		 urlr = UrlPath.singlenewsApi;
		}
		String url = urlr + "?" + "id=" + _id;
//		System.out.println("httpCommentYoMessage::::"+url);
		// 发送get请求对象
		client.get(url, new AsyncHttpResponseHandler() {
			
			
		
			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] arg2,
					Throwable arg3) {
//				System.out.println("失败"+statusCode);
				
			}
			@Override
			public void onSuccess(int statusCode, Header[] arg1, byte[] arg2) {
				final String res = new String(arg2);
//				System.out.println("成功"+statusCode);
				
				try {
					JSONObject item = new JSONObject(res);
					
					if ("message".equals(messagekind)) {
						System.out.println("成功"+statusCode);

							id = item.getString("id");
							HttpComments myHttpComments = new HttpComments(id,
									"message");
							myHttpComments.submitAsyncHttpClientGet();
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

							comments = null;

							HttpcommentToMessagePerson httpcommentToMessagePerson = new HttpcommentToMessagePerson(
									commitperson);
							httpcommentToMessagePerson
									.submitAsyncHttpClientGet(i, id, what,
											losttime, lostlatitude,
											lostlangitude, kind, information,
											commitperson, contactphone,
											sharenumber, commentsnumber,
											followingnumber, location, created,
											title, comments, image1, image2,
											image3);
							httpcommentToMessagePerson
									.submitAsyncHttpClientGet();

//						}
					} else if ("news".equals(messagekind)) {
//					
							id = item.getString("id");
							try {
								losttime = DateUtil.toSimpleDate(item
										.getString("created"));
							} catch (DateFormatWrongException e) {

								e.printStackTrace();
							}
							String lostimagestart = item.getString("image1");
							String pathpopu = UrlPath.host;
							image1 = pathpopu + lostimagestart;
							String lostimagestart1 = item.getString("image2");
							image2 = pathpopu + lostimagestart1;
							String lostimagestart3 = item.getString("image3");
							image3 = pathpopu + lostimagestart3;
							title = item.getString("title");
							information = item.getString("information");
							commitperson = item.getString("commit_person");

							sharenumber = item.getString("share_number");
							followingnumber = item
									.getString("following_number");
							commentsnumber = item.getString("comments_number");

							HttpcommentToNewsPerson httpcommentToNewsPerson = new HttpcommentToNewsPerson(
									commitperson);
							httpcommentToNewsPerson
									.submitAsyncHttpClientGet(i, id, losttime,
											information, commitperson, title,
											image1, image2, image3,
											sharenumber, commentsnumber,
											followingnumber);
							httpcommentToNewsPerson
									.submitAsyncHttpClientGet();

						}
					}

//				}

				catch (JSONException e) {

					e.printStackTrace();
				}

			}
			
		
		});
		
		return;

	}

}
