package net.loonggg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.example.netserver.MySQLiteMethodDetails;

public class AddCommentsToMessage {

	private String id ,commentscontent, commentsheader, commentsnickname,
			commentstime, comment_id,comment_phone,app_id,comment_kind;

	public ArrayList<CommentsMessage> addCommentsToMessages() {

		ArrayList<CommentsMessage> arrayList = new ArrayList<CommentsMessage>();
		List<Map<String, String>> list = MySQLiteMethodDetails.listCommentToMessage_Db();
		for (int i = 0; i < list.size(); i++) {

		    	id=list.get(i).get("id");
				app_id=list.get(i).get("app_id");
				commentscontent = list.get(i).get("comments_content");
				comment_id = list.get(i).get("comment_id");
				comment_phone = list.get(i).get("comment_phone");
				commentstime = list.get(i).get("comments_time");
				commentsheader = list.get(i).get("comments_header");
				commentsnickname = list.get(i).get("comments_name");
				comment_kind = list.get(i).get("comments_kind");
				
				CommentsMessage commentsMessage = new CommentsMessage();
				commentsMessage.setComment_app_id(app_id);
				commentsMessage.set_id(id);
				commentsMessage.setComment_id(comment_id);
				commentsMessage.setComment_phone(comment_phone);
				commentsMessage.setCommentsContent(commentscontent);
				commentsMessage.setCommentsHeader(commentsheader);
				commentsMessage.setCommentsNickname(commentsnickname);
				commentsMessage.setCommentsTime(commentstime);
				commentsMessage.setCommentskind(comment_kind);
				
				arrayList.add(commentsMessage);

			}

		

		return arrayList;

	}

}
