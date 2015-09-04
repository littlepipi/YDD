package net.loonggg.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ChangeSharedList_Json {

	public void saveInfo(Context context, String key,
			List<Map<String, JSONArray>> list) {
		JSONArray mJsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Map<String, JSONArray> map = list.get(i);
			Iterator<Entry<String, JSONArray>> iterator = map.entrySet()
					.iterator();

			JSONObject object = new JSONObject();

			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Entry entry = (Entry) iterator.next();
				try {
					object.put((String) entry.getKey(), entry.getValue());
				} catch (JSONException e) {

				}
			}
			mJsonArray.put(object);
		}

		SharedPreferences sp = context.getSharedPreferences("finals",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(key, mJsonArray.toString());
		editor.commit();
	}

	public List<Map<String, JSONArray>> getInfo(Context context, String key) {
		List<Map<String, JSONArray>> list = new ArrayList<Map<String, JSONArray>>();
		SharedPreferences sp = context.getSharedPreferences("finals",
				Context.MODE_PRIVATE);
		String result = sp.getString(key, "");
		try {
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject itemObject = array.getJSONObject(i);
				Map<String, JSONArray> map = new HashMap<String, JSONArray>();
				JSONArray names = itemObject.names();
				if (names != null) {
					for (int j = 0; j < names.length(); j++) {
						String name = names.getString(j);

						JSONArray value = itemObject.getJSONArray(name);

						map.put(name, value);
					}
				}
				list.add(map);
			}
		} catch (JSONException e) {

		}

		return list;
	}

}
