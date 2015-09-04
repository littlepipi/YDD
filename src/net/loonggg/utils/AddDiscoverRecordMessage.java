package net.loonggg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.example.netserver.MySQLiteMethodDetails;

public class AddDiscoverRecordMessage {

	public static void AddDiscoverRecordMessageData( 
			ArrayList<DiscoverMessageData> apk_list,boolean flag) {
if (flag==true) {
	final List<Map<String, String>> list = MySQLiteMethodDetails
			.list_discover_Cord_Db();
	for (int i = 0; i < list.size(); i++) {

		final DiscoverMessageData discoverMessageData = new DiscoverMessageData();

		discoverMessageData.setId(list.get(i).get("id"));
		discoverMessageData.setapp_Id(list.get(i).get("app_id"));
		discoverMessageData.setTime(list.get(i).get("time"));
		discoverMessageData.setTitle(list.get(i).get("title"));

		discoverMessageData.setInformation(list.get(i).get("information"));
		discoverMessageData
				.setCommitPerson(list.get(i).get("commitperson"));
		discoverMessageData.setCommentsNumber(list.get(i).get(
				"commentsnumber"));
		discoverMessageData.setFollowingNumber(list.get(i).get(
				"followingnumber"));
		discoverMessageData.setShareNumber(list.get(i).get("sharenumber"));
		discoverMessageData.setImage1(list.get(i).get("image1"));
		discoverMessageData.setImage2(list.get(i).get("image2"));
		discoverMessageData.setImage3(list.get(i).get("image3"));

		discoverMessageData.setPersoncreated(list.get(i).get(
				"personcreated"));

		discoverMessageData.setPersonname(list.get(i).get("personname"));
		discoverMessageData.setPersonportrait(list.get(i).get(
				"personportrait"));
		discoverMessageData.setPersonlocation(list.get(i).get(
				"personlocation"));

		apk_list.add(discoverMessageData);

	}
}else if (flag==false) {
	final List<Map<String, String>> list = MySQLiteMethodDetails
			.list_discover_Personal_Db();
	for (int i = 0; i < list.size(); i++) {

		final DiscoverMessageData discoverMessageData = new DiscoverMessageData();

		discoverMessageData.setId(list.get(i).get("id"));
		discoverMessageData.setapp_Id(list.get(i).get("app_id"));
		discoverMessageData.setTitle(list.get(i).get("title"));
		discoverMessageData.setTime(list.get(i).get("time"));
		discoverMessageData.setInformation(list.get(i).get("information"));
		discoverMessageData
				.setCommitPerson(list.get(i).get("commitperson"));
		discoverMessageData.setCommentsNumber(list.get(i).get(
				"commentsnumber"));
		discoverMessageData.setFollowingNumber(list.get(i).get(
				"followingnumber"));
		discoverMessageData.setShareNumber(list.get(i).get("sharenumber"));
		discoverMessageData.setImage1(list.get(i).get("image1"));
		discoverMessageData.setImage2(list.get(i).get("image2"));
		discoverMessageData.setImage3(list.get(i).get("image3"));

		discoverMessageData.setPersoncreated(list.get(i).get(
				"personcreated"));

		discoverMessageData.setPersonname(list.get(i).get("personname"));
		discoverMessageData.setPersonportrait(list.get(i).get(
				"personportrait"));
		discoverMessageData.setPersonlocation(list.get(i).get(
				"personlocation"));

		apk_list.add(discoverMessageData);

	}
}
		

	}

}
