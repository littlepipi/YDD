package app.example.netserver;

import net.loonggg.utils.DateUtil;
import net.loonggg.utils.DateUtil.DateFormatWrongException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.example.Url.UrlPath;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HttpComments {
	private String id;
	private String commit_person;
	private String comments_id;
	private String commentstime;
	private String commentscontent;
  private String kind;
	
  
	public HttpComments(String id,String kind) {
	
		this.id = id;
		this.kind=kind;
		
	}

	public void submitAsyncHttpClientGet() {
		// 创建异步请求端对象

		AsyncHttpClient client = new AsyncHttpClient();

		String urlr = UrlPath.commentGetApi;
		String url = urlr + "?" + "id=" + id+"&kind="+kind;

		
		// 发送get请求对象
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// System.out.println("评论数据：：：："+arg0+new String(arg2));
                 
               
			}

			
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				try {
					JSONObject item = new JSONObject();
					JSONArray jsonArray = new JSONArray(new String(arg2));

					for (int i = 0; i < jsonArray.length(); i++) {
						item = jsonArray.getJSONObject(i);
						commit_person = item.getString("commit_person");
						comments_id = item.getString("id");
						
						 try {
						 commentstime = DateUtil.toSimpleDate(item
						 .getString("created"));
						 } catch (DateFormatWrongException e) {
						 e.printStackTrace();
						 }
					
						commentscontent = item.getString("content");
						HttpCommentsPerson httpComments = new HttpCommentsPerson(
								commit_person);

						httpComments.submitAsyncHttpClientGet(id, commentstime,
								commentscontent, comments_id);
						httpComments.submitAsyncHttpClientGet();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

		});
		

		return;

		
	}

}
