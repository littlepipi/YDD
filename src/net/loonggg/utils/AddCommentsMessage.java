package net.loonggg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.example.netserver.MySQLiteMethodDetails;

public class AddCommentsMessage {

	private String commentscontent, commentsheader, commentsnickname,
			commentstime, comment_id,comment_phone,app_id;

	public ArrayList<CommentsMessage> addCommentsMessages( String id) {

		ArrayList<CommentsMessage> arrayList = new ArrayList<CommentsMessage>();
		List<Map<String, String>> list = MySQLiteMethodDetails.listComment_Db();
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).get("id").equals(id)) {
				app_id=list.get(i).get("app_id");
				commentscontent = list.get(i).get("comments_content");
				comment_id = list.get(i).get("comment_id");
				comment_phone = list.get(i).get("comment_phone");
				commentstime = list.get(i).get("comments_time");
				commentsheader = list.get(i).get("comments_header");
				commentsnickname = list.get(i).get("comments_name");
				CommentsMessage commentsMessage = new CommentsMessage();
				commentsMessage.setComment_app_id(app_id);
				commentsMessage.setComment_id(comment_id);
				commentsMessage.setComment_phone(comment_phone);
				commentsMessage.setCommentsContent(commentscontent);
				commentsMessage.setCommentsHeader(commentsheader);
				commentsMessage.setCommentsNickname(commentsnickname);
				commentsMessage.setCommentsTime(commentstime);
				arrayList.add(commentsMessage);

			} else {
				
			}

		}

		return arrayList;

	}

}
