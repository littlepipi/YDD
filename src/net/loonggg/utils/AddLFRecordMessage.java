package net.loonggg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.example.netserver.MySQLiteMethodDetails;
import app.example.netserver.PersonData;

public class AddLFRecordMessage {

	public static void AddPersonDatas(String what,
			ArrayList<PersonData> apk_list, boolean flag) {
		List<Map<String, String>> list = null;
		if (flag = true) {
			list = MySQLiteMethodDetails.listCord_Db(what);
		}else if (flag = false){
			list = MySQLiteMethodDetails.listPersonal_Db(what);
		}

		for (int i = 0; i < list.size(); i++) {

			final PersonData personData = new PersonData();

			personData.setId(list.get(i).get("id"));
			personData.setapp_Id(list.get(i).get("app_id"));
			personData.setWhat(list.get(i).get("what"));

			personData.setTime(list.get(i).get("losttime"));
			personData.setLangitude(list.get(i).get("lostlangitude"));
			personData.setLatitude(list.get(i).get("lostlatitude"));
			personData.setKind(list.get(i).get("lostkind"));
			personData.setTitle(list.get(i).get("title"));

			personData.setInformation(list.get(i).get("lostinformation"));
			personData.setCommitPerson(list.get(i).get("commitperson"));

			personData.setContactPhone(list.get(i).get("contactphone"));

			personData.setShareNumber(list.get(i).get("sharenumber"));
			personData.setFollowingNumber(list.get(i).get("followingnumber"));
			personData.setCommentsNumber(list.get(i).get("commentsnumber"));
			personData.setLocation(list.get(i).get("location"));
			personData.setCreated(list.get(i).get("created"));
			personData.setComments(list.get(i).get("comments"));

			personData.setImage1(list.get(i).get("lostimage1"));
			personData.setImage2(list.get(i).get("lostimage2"));
			personData.setImage3(list.get(i).get("lostimage3"));

			personData.setPersoncreated(list.get(i).get("personcreated"));

			personData.setPersonname(list.get(i).get("personname"));
			personData.setPersonportrait(list.get(i).get("personportrait"));
			personData.setPersonlocation(list.get(i).get("personlocation"));

			apk_list.add(personData);

		}

	}

}
