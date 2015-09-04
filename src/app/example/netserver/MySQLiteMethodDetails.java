package app.example.netserver;

import java.util.List;
import java.util.Map;

import android.util.Log;
import app.example.application.MyApplication;

public class MySQLiteMethodDetails {
	/**
	 * 
	 * @param id
	 * @param what
	 * @param title
	 * @param time
	 * @param latitude
	 * @param langitude
	 * @param kind
	 * @param information
	 * @param commitperson
	 * @param contactphone
	 * @param sharenumber
	 * @param commentsnumber
	 * @param followingnumber
	 * @param location
	 * @param created
	 * @param comments
	 * @param image1
	 * @param image2
	 * @param image3
	 * @param personcreated
	 * @param personname
	 * @param personportrait
	 * @param personlocation
	 */
	public static void insertCord_Db(String id, String what, String title,
			String time, String latitude, String langitude, String kind,
			String information, String commitperson, String contactphone,
			String sharenumber, String commentsnumber, String followingnumber,
			String location, String created, String comments, String image1,
			String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			String CordKind) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, what, title, time, latitude, langitude, kind,
				information, commitperson, contactphone, sharenumber,
				commentsnumber, followingnumber, location, created, comments,
				image1, image2, image3, personcreated, personname,
				personportrait, personlocation };

		sqLiteinterface.addPersonCord(params, CordKind);
	}

	public static List<Map<String, String>> listCord_Db(String what) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listPersonCordmaps(
				null, what);

		return list;
	}

	public static void updataCord_Db(String id, String what, String title,
			String time, String latitude, String langitude, String kind,
			String information, String commitperson, String contactphone,
			String sharenumber, String commentsnumber, String followingnumber,
			String location, String created, String comments, String image1,
			String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			int position, String CordKind) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, what, title, time, latitude, langitude, kind,
				information, commitperson, contactphone, sharenumber,
				commentsnumber, followingnumber, location, created, comments,
				image1, image2, image3, personcreated, personname,
				personportrait, personlocation, position };
		sqLiteinterface.updataPersonCord(params, CordKind);
	}

	public static void deleteCord_Db(String CordKind, int app_id) {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());

		Object[] params = { app_id };

		sqLiteinterface.deletePersonCord(params, CordKind);

	}

	/**
	 * 个人主页上的数据增删查改
	 * 
	 * @param id
	 * @param what
	 * @param title
	 * @param time
	 * @param latitude
	 * @param langitude
	 * @param kind
	 * @param information
	 * @param commitperson
	 * @param contactphone
	 * @param sharenumber
	 * @param commentsnumber
	 * @param followingnumber
	 * @param location
	 * @param created
	 * @param comments
	 * @param image1
	 * @param image2
	 * @param image3
	 * @param personcreated
	 * @param personname
	 * @param personportrait
	 * @param personlocation
	 * @param CordKind
	 */

	public static void insertPersonal_Db(String id, String what, String title,
			String time, String latitude, String langitude, String kind,
			String information, String commitperson, String contactphone,
			String sharenumber, String commentsnumber, String followingnumber,
			String location, String created, String comments, String image1,
			String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			String CordKind) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, what, title, time, latitude, langitude, kind,
				information, commitperson, contactphone, sharenumber,
				commentsnumber, followingnumber, location, created, comments,
				image1, image2, image3, personcreated, personname,
				personportrait, personlocation };

		sqLiteinterface.addPersonal(params, CordKind);
	}

	public static List<Map<String, String>> listPersonal_Db(String what) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listPersonalmaps(null,
				what);

		return list;
	}

	public static void updataPersonal_Db(String id, String what, String title,
			String time, String latitude, String langitude, String kind,
			String information, String commitperson, String contactphone,
			String sharenumber, String commentsnumber, String followingnumber,
			String location, String created, String comments, String image1,
			String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			int position, String CordKind) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, what, title, time, latitude, langitude, kind,
				information, commitperson, contactphone, sharenumber,
				commentsnumber, followingnumber, location, created, comments,
				image1, image2, image3, personcreated, personname,
				personportrait, personlocation, position };
		sqLiteinterface.updataPersonal(params, CordKind);
	}

	public static void deletePersonal_Db(String CordKind) {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());

		// for (int i = 6; i <= 10; i++) {
		Object[] params = { 6 };

		sqLiteinterface.deletePersonal(params, CordKind);
		Log.i("delete", "" + 6);

		// }

	}

	/***************************************************************/
	/**
	 * 
	 * @param id
	 * @param what
	 * @param title
	 * @param time
	 * @param latitude
	 * @param langitude
	 * @param kind
	 * @param information
	 * @param commitperson
	 * @param contactphone
	 * @param sharenumber
	 * @param commentsnumber
	 * @param followingnumber
	 * @param location
	 * @param created
	 * @param comments
	 * @param image1
	 * @param image2
	 * @param image3
	 * @param personcreated
	 * @param personname
	 * @param personportrait
	 * @param personlocation
	 */
	public static void insert_Message(String id, String what, String title,
			String time, String latitude, String langitude, String kind,
			String information, String commitperson, String contactphone,
			String sharenumber, String commentsnumber, String followingnumber,
			String location, String created, String comments, String image1,
			String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, what, title, time, latitude, langitude, kind,
				information, commitperson, contactphone, sharenumber,
				commentsnumber, followingnumber, location, created, comments,
				image1, image2, image3, personcreated, personname,
				personportrait, personlocation };

		sqLiteinterface.addMessage(params);
	}

	public static List<Map<String, String>> list_Message() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listMessagemaps(null);

		return list;
	}

	public static void updata_Message(String id, String what, String title,
			String time, String latitude, String langitude, String kind,
			String information, String commitperson, String contactphone,
			String sharenumber, String commentsnumber, String followingnumber,
			String location, String created, String comments, String image1,
			String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			int position) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, what, title, time, latitude, langitude, kind,
				information, commitperson, contactphone, sharenumber,
				commentsnumber, followingnumber, location, created, comments,
				image1, image2, image3, personcreated, personname,
				personportrait, personlocation, position };
		sqLiteinterface.updataMessage(params);
	}

	public static void delete_Message() {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());

		// for (int i = 6; i <= 10; i++) {
		Object[] params = { 6 };

		sqLiteinterface.deleteMessage(params);

		// }

	}

	/*********************************************************/
	/***************************************************************/
	public static void insertDb(String id, String what, String title,
			String time, String latitude, String langitude, String kind,
			String information, String commitperson, String contactphone,
			String sharenumber, String commentsnumber, String followingnumber,
			String location, String created, String comments, String image1,
			String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, what, title, time, latitude, langitude, kind,
				information, commitperson, contactphone, sharenumber,
				commentsnumber, followingnumber, location, created, comments,
				image1, image2, image3, personcreated, personname,
				personportrait, personlocation };

		sqLiteinterface.addPerson(params, what);
	}

	public static List<Map<String, String>> listDb(String what) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listPersonmaps(null,
				what);

		return list;
	}

	public static void updataDb(String id, String what, String title,
			String time, String latitude, String langitude, String kind,
			String information, String commitperson, String contactphone,
			String sharenumber, String commentsnumber, String followingnumber,
			String location, String created, String comments, String image1,
			String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			int position) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, what, title, time, latitude, langitude, kind,
				information, commitperson, contactphone, sharenumber,
				commentsnumber, followingnumber, location, created, comments,
				image1, image2, image3, personcreated, personname,
				personportrait, personlocation, position };
//		Log.i("MySQLiteMethodDetails", "" + params);
		sqLiteinterface.updataPerson(params, what);
	}

	public static void deletePerson(String what) {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());

		// for (int i = 6; i <= 10; i++) {
		Object[] params = { 6 };

		sqLiteinterface.deletePerson(params, what);
		Log.i("delete", "" + 6);

		// }

	}

	/*********************************************************/
	/**
	 * 留言的具体方法
	 * 
	 * @param id
	 * @param created
	 * @param from_person
	 * @param content
	 * @param if_read
	 * @param personcreated
	 * @param personname
	 * @param personportrait
	 * @param personlocation
	 */
	public static void insertWords_Db(String id, String created,
			String from_person, String content, String if_read,
			String personcreated, String personname, String personportrait,
			String personlocation) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, created, from_person, content, if_read,
				personcreated, personname, personportrait, personlocation };

		sqLiteinterface.addWords(params);
	}

	public static List<Map<String, String>> listWords_Db() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listWordsmaps(null);

		return list;
	}

	public static void updataWords_Db(String id, String created,
			String from_person, String content, String if_read,
			String personcreated, String personname, String personportrait,
			String personlocation, int position) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, created, from_person, content, if_read,
				personcreated, personname, personportrait, personlocation,
				position };
		Log.i("MySQLiteMethodDetails", "" + params);
		sqLiteinterface.updataWords(params);
	}

	public static void deleteWords_Db() {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());

		// for (int i = 6; i <= 10; i++) {
		Object[] params = { 6 };

		sqLiteinterface.deleteWords(params);

		// }

	}

	/***********************************************************/
	/**
	 * 
	 * @param id
	 * @param title
	 * @param time
	 * @param information
	 * @param commitperson
	 * @param image1
	 * @param image2
	 * @param image3
	 * @param personcreated
	 * @param personname
	 * @param personportrait
	 * @param personlocation
	 * @return
	 */

	public static boolean insert_discover_Db(String id, String title,
			String time, String information, String commitperson,
			String image1, String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			String sharenumber, String commentsnumber, String followingnumber) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, title, time, information, commitperson, image1,
				image2, image3, personcreated, personname, personportrait,
				personlocation, sharenumber, commentsnumber, followingnumber };

		return sqLiteinterface.addDiscover(params);
	}

	public static List<Map<String, String>> list_discover_Db() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listDiscovermaps(null);

		return list;
	}

	public static void delete_discover_Db(int app_id) {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
//		Log.i("删除成功了么？", "发现信息：" + app_id);

		Object[] params = { app_id };

		sqLiteinterface.deleteDiscover(params);

	}

	public static void updata_discover_Db(String id, String title, String time,
			String information, String commitperson, String image1,
			String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			String sharenumber, String commentsnumber, String followingnumber,
			int position) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, title, time, information, commitperson, image1,
				image2, image3, personcreated, personname, personportrait,
				personlocation, sharenumber, commentsnumber, followingnumber,
				position };
		Log.i("更改", "" + params);
		sqLiteinterface.updataDiscover(params);
	}

	/**
	 * 
	 * @param id
	 * @param title
	 * @param time
	 * @param information
	 * @param commitperson
	 * @param image1
	 * @param image2
	 * @param image3
	 * @param personcreated
	 * @param personname
	 * @param personportrait
	 * @param personlocation
	 * @param sharenumber
	 * @param commentsnumber
	 * @param followingnumber
	 * @return
	 */
	public static boolean insert_discover_Cord_Db(String id, String title,
			String time, String information, String commitperson,
			String image1, String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			String sharenumber, String commentsnumber, String followingnumber) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, title, time, information, commitperson, image1,
				image2, image3, personcreated, personname, personportrait,
				personlocation, sharenumber, commentsnumber, followingnumber };

		return sqLiteinterface.addDiscoverCord(params);
	}

	public static List<Map<String, String>> list_discover_Cord_Db() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface
				.listDiscoverCordmaps(null);

		return list;
	}

	public static void delete_discover_Cord_Db(int app_id) {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
//		Log.i("删除成功了么？", "发现信息：" + app_id);

		Object[] params = { app_id };

		sqLiteinterface.deleteDiscoverCord(params);

	}

	public static void updata_discover_Cord_Db(String id, String title,
			String time, String information, String commitperson,
			String image1, String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			String sharenumber, String commentsnumber, String followingnumber,
			int position) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, title, time, information, commitperson, image1,
				image2, image3, personcreated, personname, personportrait,
				personlocation, sharenumber, commentsnumber, followingnumber,
				position };
		Log.i("更改", "" + params);
		sqLiteinterface.updataDiscoverCord(params);
	}

	/**
	 * 个人主页上的发现
	 * 
	 * @param id
	 * @param title
	 * @param time
	 * @param information
	 * @param commitperson
	 * @param image1
	 * @param image2
	 * @param image3
	 * @param personcreated
	 * @param personname
	 * @param personportrait
	 * @param personlocation
	 * @param sharenumber
	 * @param commentsnumber
	 * @param followingnumber
	 * @return
	 */
	public static boolean insert_discover_Personal_Db(String id, String title,
			String time, String information, String commitperson,
			String image1, String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			String sharenumber, String commentsnumber, String followingnumber) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, title, time, information, commitperson, image1,
				image2, image3, personcreated, personname, personportrait,
				personlocation, sharenumber, commentsnumber, followingnumber };

		return sqLiteinterface.addDiscoverPersonal(params);
	}

	public static List<Map<String, String>> list_discover_Personal_Db() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface
				.listDiscoverPersonalmaps(null);

		return list;
	}

	public static void delete_discover_Personal_Db(int app_id) {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
//		Log.i("删除成功了么？", "发现信息：" + app_id);

		Object[] params = { app_id };

		sqLiteinterface.deleteDiscoverPersonal(params);

	}

	public static void updata_discover_Personal_Db(String id, String title,
			String time, String information, String commitperson,
			String image1, String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			String sharenumber, String commentsnumber, String followingnumber,
			int position) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, title, time, information, commitperson, image1,
				image2, image3, personcreated, personname, personportrait,
				personlocation, sharenumber, commentsnumber, followingnumber,
				position };
		Log.i("更改", "" + params);
		sqLiteinterface.updataDiscoverPersonal(params);
	}

	/**
	 * 
	 * @param id
	 * @param comment_id
	 * @param comments_header
	 * @param comments_name
	 * @param comments_time
	 * @param comments_content
	 */
	public static boolean insertcomment_Db(String id, String comment_id,
			String comment_phone, String comments_header, String comments_name,
			String comments_time, String comments_content) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, comment_id, comment_phone, comments_header,
				comments_name, comments_time, comments_content };

		return sqLiteinterface.addComments(params);
	}

	public static List<Map<String, String>> listComment_Db() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listCommentsmaps(null);

		return list;
	}

	public static void deleteComment_Db(int app_id) {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());

		Object[] params = { app_id };

		boolean b = sqLiteinterface.deleteComments(params);
//		Log.i("删除成功了么？", "" + app_id + "^^^" + b);

	}

	/**
	 * 
	 * @param id
	 * @param title
	 * @param time
	 * @param information
	 * @param commitperson
	 * @param image1
	 * @param image2
	 * @param image3
	 * @param personcreated
	 * @param personname
	 * @param personportrait
	 * @param personlocation
	 * @param sharenumber
	 * @param commentsnumber
	 * @param followingnumber
	 * @return
	 */
	public static boolean insert_news_Db(String id, String title, String time,
			String information, String commitperson, String image1,
			String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			String sharenumber, String commentsnumber, String followingnumber) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, title, time, information, commitperson, image1,
				image2, image3, personcreated, personname, personportrait,
				personlocation, sharenumber, commentsnumber, followingnumber };

		return sqLiteinterface.addnews(params);
	}

	public static List<Map<String, String>> list_news_Db() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listnewsmaps(null);

		return list;
	}

	public static void delete_news_Db(int app_id) {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
//		Log.i("删除成功了么？", "发现信息：" + app_id);

		Object[] params = { app_id };

		sqLiteinterface.deletenews(params);

	}

	public static void updata_news_Db(String id, String title, String time,
			String information, String commitperson, String image1,
			String image2, String image3, String personcreated,
			String personname, String personportrait, String personlocation,
			String sharenumber, String commentsnumber, String followingnumber,
			int position) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, title, time, information, commitperson, image1,
				image2, image3, personcreated, personname, personportrait,
				personlocation, sharenumber, commentsnumber, followingnumber,
				position };
		Log.i("更改", "" + params);
		sqLiteinterface.updatanews(params);
	}

	/**
	 * 
	 * @param id
	 * @param comment_id
	 * @param comment_phone
	 * @param comments_header
	 * @param comments_name
	 * @param comments_time
	 * @param comments_content
	 * @return
	 */
	public static boolean insertcommentToMessage_Db(String id,
			String comment_id, String comment_phone, String comments_header,
			String comments_name, String comments_time,
			String comments_content, String comments_kind) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, comment_id, comment_phone, comments_header,
				comments_name, comments_time, comments_content, comments_kind };

		return sqLiteinterface.addCommentsToMessage(params);
	}

	public static List<Map<String, String>> listCommentToMessage_Db() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface
				.listCommentsToMessagemaps(null);

		return list;
	}

	public static void deleteCommentToMessage_Db(int app_id) {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());

		Object[] params = { app_id };

		boolean b = sqLiteinterface.deleteCommentsToMessage(params);
//		Log.i("删除成功了么？", "" + app_id + "^^^" + b);

	}

	/**
	 * 
	 * @param id
	 * @param NO_OFF
	 * @return
	 */
	public static boolean insert_fans_Db(String id, String NO_OFF) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, NO_OFF };

		return sqLiteinterface.addFans(params);
	}

	public static List<Map<String, String>> list_fans_Db() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listFansmaps(null);

		return list;
	}

	public static Map<String, String> map_fans_DB(String id) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());

		String[] selectionArgs = { id };
		Map<String, String> map = sqLiteinterface.viewFans(selectionArgs);

		return map;

	}

	public static void delete_fans_Db(int id) {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
//		Log.i("删除成功了么？", "发现信息：" + id);

		Object[] params = { id };

		sqLiteinterface.deleteFans(params);

	}

	public static void updata_fans_Db(String id, String NO_OFF) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, NO_OFF, id };
//		Log.i("点赞开关:", "" + params);
		sqLiteinterface.updataFans(params);
	}

	/**
	 * 
	 * @param id
	 * @param NO_OFF
	 * @return
	 */
	public static boolean insert_start(String start) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { start };

		return sqLiteinterface.addStart(params);
	}

	public static List<Map<String, String>> list_start() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listStartmaps(null);
		return list;
	}

	public static boolean EmptyTable(String table) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		boolean flag = sqLiteinterface.EmptyTable(table);
		return flag;
	}

	/**
	 * 
	 * @param id
	 * @param created
	 * @param from_person
	 * @param content
	 * @param if_read
	 * @param personcreated
	 * @param personname
	 * @param personportrait
	 * @param personlocation
	 */
	public static void insertSystem_Db(String id, String time, String content) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, time, content };

		sqLiteinterface.addSystem(params);
	}

	public static List<Map<String, String>> listSystem_Db() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listSystemmaps(null);

		return list;
	}

	public static void updataSystem_Db(String id, String time, String content) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { id, time, content };

		sqLiteinterface.updataSystem(params);
	}

	public static void deleteSystem_Db(int app_id) {

		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());

		Object[] params = { app_id };

		sqLiteinterface.deleteSystem(params);

	}

	
	
	public static void insert_NO(String NO) {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		Object[] params = { NO };

		sqLiteinterface.addNO(params);
	}

	public static List<Map<String, String>> list_NO() {
		SQLiteinterface sqLiteinterface = new MySQLiteMethod(
				MyApplication.getAppContext());
		List<Map<String, String>> list = sqLiteinterface.listNO(null);

		return list;
	}
}