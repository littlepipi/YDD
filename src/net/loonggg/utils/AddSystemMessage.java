package net.loonggg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.example.netserver.MySQLiteMethodDetails;

public class AddSystemMessage {

	private String content, id, created, app_id;

	public ArrayList<SystemMessage> addSystemMessages() {

		ArrayList<SystemMessage> arrayList = new ArrayList<SystemMessage>();
		List<Map<String, String>> list = MySQLiteMethodDetails.listSystem_Db();
		for (int i = 0; i < list.size(); i++) {

			app_id = list.get(i).get("app_id");
			content = list.get(i).get("content");
			id = list.get(i).get("id");
			created = list.get(i).get("time");
			SystemMessage systemMessage = new SystemMessage();
			systemMessage.setapp_id(app_id);
			systemMessage.set_id(id);
			systemMessage.setContent(content);
			systemMessage.setCreated(created);
			arrayList.add(systemMessage);

		}

		return arrayList;

	}

}
