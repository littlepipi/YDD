package net.loonggg.utils;

import java.util.List;
import java.util.Map;

import app.example.netserver.MySQLiteMethodDetails;

public class CommentsClicent_Clicent {

	public static void CommentsClicent() {
		List<Map<String, String>> l1 = MySQLiteMethodDetails.listComment_Db();
		List<Map<String, String>> l2 = MySQLiteMethodDetails.list_NO();

		int sss = 0;
		int ssss = l2.size();

		for (int i = 0; i < l1.size(); i++) {

			String comments_id = l1.get(i).get("comment_id");
			for (int j = 0; j < ssss; j++) {
				String NO = l2.get(j).get("NO");
				if (!(NO.equals(comments_id))) {

					sss = sss + 1;

				}

			}
			if (sss == ssss) {

				int app_id = Integer.valueOf(l1.get(i).get("app_id"))
						.intValue();
				MySQLiteMethodDetails.deleteComment_Db(app_id);
			}
			sss = 0;
		}
	}
}
