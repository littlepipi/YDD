package net.loonggg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.example.netserver.MySQLiteMethodDetails;

public class AddDiscoverMessageData {

	public static void AddiscoverMessageDatas(int num, int kind,
			ArrayList<DiscoverMessageData> apk_list) {


		final List<Map<String, String>> list = MySQLiteMethodDetails
				.list_discover_Db();
		if (kind == 0) {

			for (int i = 0; i <= num - 1; i++) {

				final DiscoverMessageData discoverMessageData = new DiscoverMessageData();

				discoverMessageData.setId(list.get(i).get("id"));
				discoverMessageData.setapp_Id(list.get(i).get("app_id"));
				discoverMessageData.setTitle(list.get(i).get("title"));
				discoverMessageData.setTime(list.get(i).get("time"));

				discoverMessageData.setInformation(list.get(i).get(
						"information"));
				discoverMessageData.setCommitPerson(list.get(i).get(
						"commitperson"));
				discoverMessageData.setCommentsNumber(list.get(i).get(
						"commentsnumber"));
				discoverMessageData.setFollowingNumber(list.get(i).get(
						"followingnumber"));
				discoverMessageData.setShareNumber(list.get(i).get(
						"sharenumber"));
				discoverMessageData.setImage1(list.get(i).get("image1"));
				discoverMessageData.setImage2(list.get(i).get("image2"));
				discoverMessageData.setImage3(list.get(i).get("image3"));

				discoverMessageData.setPersoncreated(list.get(i).get(
						"personcreated"));

				discoverMessageData
						.setPersonname(list.get(i).get("personname"));
				discoverMessageData.setPersonportrait(list.get(i).get(
						"personportrait"));
				discoverMessageData.setPersonlocation(list.get(i).get(
						"personlocation"));

				apk_list.add(discoverMessageData);
				// }
			}
		} else if (kind == 1) {

			for (int i = 5; i <= num + 4; i++) {
//				System.out.println("发现数据：" + i + "标题"
//						+ list.get(i).get("title"));
				final DiscoverMessageData discoverMessageData = new DiscoverMessageData();

				discoverMessageData.setId(list.get(i).get("id"));
				discoverMessageData.setapp_Id(list.get(i).get("app_id"));
				discoverMessageData.setTitle(list.get(i).get("title"));
				discoverMessageData.setTime(list.get(i).get("time"));
				discoverMessageData.setInformation(list.get(i).get(
						"information"));
				discoverMessageData.setCommitPerson(list.get(i).get(
						"commitperson"));
				discoverMessageData.setCommentsNumber(list.get(i).get(
						"commentsnumber"));
				discoverMessageData.setFollowingNumber(list.get(i).get(
						"followingnumber"));
				discoverMessageData.setShareNumber(list.get(i).get(
						"sharenumber"));
				discoverMessageData.setImage1(list.get(i).get("image1"));
				discoverMessageData.setImage2(list.get(i).get("image2"));
				discoverMessageData.setImage3(list.get(i).get("image3"));

				discoverMessageData.setPersoncreated(list.get(i).get(
						"personcreated"));

				discoverMessageData
						.setPersonname(list.get(i).get("personname"));
				discoverMessageData.setPersonportrait(list.get(i).get(
						"personportrait"));
				discoverMessageData.setPersonlocation(list.get(i).get(
						"personlocation"));

				apk_list.add(discoverMessageData);

			}
		}

	}

}
