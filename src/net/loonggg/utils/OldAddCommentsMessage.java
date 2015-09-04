package net.loonggg.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OldAddCommentsMessage {

	Map<String, JSONArray> map;
	JSONArray jsonArray = new JSONArray();
	private String commentscontent, commentsheader, commentsnickname,
			commentstime,comment_id;

	public ArrayList<CommentsMessage> addCommentsMessages(String id,
			List<Map<String, JSONArray>> list) {
		map = new HashMap<String, JSONArray>();
		ArrayList<CommentsMessage> arrayList = new ArrayList<CommentsMessage>();
/**
 * list集合格式[map,map,map]
 * map<String,JSONArray>格式   id=JsonArray,
 * JsonArray格式     jsonObject
 * 
 * 
 */
		for (int i = 0; i < list.size(); i++) {
			map = list.get(i);
			@SuppressWarnings("rawtypes")
			Set set = map.keySet();

			for (Object key : set) {
				if (id.equals(key)) {
					jsonArray = map.get(key);

					try {
						for (int j = 0; j < jsonArray.length(); j++) {
							JSONObject jsonObject = jsonArray.getJSONObject(j);

							commentscontent = jsonObject
									.getString("commentscontent");
							commentsheader = jsonObject
									.getString("commentshead");
							commentsnickname = jsonObject
									.getString("commentsnickname");
							commentstime = jsonObject.getString("commentstime");
							comment_id=jsonObject.getString("comment_id");
							
							
							CommentsMessage commentsMessage = new CommentsMessage();
							commentsMessage.setCommentsContent(commentscontent);
							commentsMessage.setCommentsHeader(commentsheader);
							commentsMessage
									.setCommentsNickname(commentsnickname);
							commentsMessage.setCommentsTime(commentstime);
							commentsMessage.setComment_id(comment_id);
							
							arrayList.add(commentsMessage);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

		return arrayList;

	}

}
