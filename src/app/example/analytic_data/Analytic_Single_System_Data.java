package app.example.analytic_data;

import java.util.List;
import java.util.Map;

import net.loonggg.utils.DateUtil;
import net.loonggg.utils.DateUtil.DateFormatWrongException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.example.netserver.MySQLiteMethodDetails;

public class Analytic_Single_System_Data {
	static String id, created, content;
	static int num;

	public static String JSONArray(String json) {

		try {
			JSONObject item = new JSONObject();
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {

				item = jsonArray.getJSONObject(i);
				id = item.getString("id");
				try {
					created = DateUtil.toSimpleDate(item.getString("created"));
				} catch (DateFormatWrongException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				content = item.getString("content");

				List<Map<String, String>> list = MySQLiteMethodDetails
						.listSystem_Db();

				int total = list.size();

				if (total == 0) {
					MySQLiteMethodDetails.insertSystem_Db(id, created, content);
					total = 1;
				} else {
					for (int j = 0; j < list.size(); j++) {
						String comments_id = list.get(j).get("id");

						if (!(comments_id.equals(id))) {
							num = num + 1;

						}
					}

				}
				if (num == total) {

					MySQLiteMethodDetails.insertSystem_Db(id, created, content);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return content;

	}

}
