package app.example.analytic_data;

import net.loonggg.utils.DateUtil;
import net.loonggg.utils.DateUtil.DateFormatWrongException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.example.netserver.HttpCommentToMessage;
import app.example.netserver.OtherHttpPerson;

public class Analytic_Single_String_Data {
	static String id, commit_person, created, content, message, news;
   private static int num=0;
	public static int JSONArray(String json) {

		// 向数据库中插入数据

		try {
			JSONObject item = new JSONObject();
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {

				item = jsonArray.getJSONObject(i);
                   num=num+1;
				id = item.getString("id");
				commit_person = item.getString("commit_person");
				try {
					created = DateUtil.toSimpleDate(item.getString("created"));
				} catch (DateFormatWrongException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				content = item.getString("content");
				message = item.getString("message");
				news = item.getString("news");

				if ("null".equals(message)) {
					if ("null".equals(news)) {

					} else {
						
						HttpCommentToMessage httpCommentToMessage = new HttpCommentToMessage(
								news, "news");
						httpCommentToMessage.submitHttpClientGet();
						OtherHttpPerson otherHttpPerson = new OtherHttpPerson(
								commit_person, id, created, content, news,
								"news");
						otherHttpPerson.submitAsyncHttpClientGet();

					}
				} else {

					
					HttpCommentToMessage httpCommentToMessage = new HttpCommentToMessage(
							message, "message");
					httpCommentToMessage.submitHttpClientGet();
					OtherHttpPerson otherHttpPerson = new OtherHttpPerson(
							commit_person, id, created, content, message,
							"message");
					otherHttpPerson.submitAsyncHttpClientGet();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;

	}

}
