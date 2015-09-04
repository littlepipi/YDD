package app.example.netserver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	private static String DATABASE_NAME = "APP.db";
	private static int DATABASE_VERSION = 1;

	public MySQLiteOpenHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 创建table
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table lost(app_id integer primary key autoincrement,id TEXT, what TEXT,title TEXT,losttime TEXT, lostlatitude TEXT,lostlangitude TEXT , lostkind TEXT,lostinformation TEXT, commitperson TEXT,contactphone TEXT,sharenumber TEXT,commentsnumber TEXT,followingnumber TEXT,location TEXT,created TEXT,comments TEXT,lostimage1 TEXT,lostimage2 TEXT,lostimage3 TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT);");
		db.execSQL("create table find(app_id integer primary key autoincrement,id TEXT, what TEXT,title TEXT,losttime TEXT, lostlatitude TEXT,lostlangitude TEXT , lostkind TEXT,lostinformation TEXT, commitperson TEXT,contactphone TEXT,sharenumber TEXT,commentsnumber TEXT,followingnumber TEXT,location TEXT,created TEXT,comments TEXT,lostimage1 TEXT,lostimage2 TEXT,lostimage3 TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT);");
		db.execSQL("create table comments(app_id integer primary key autoincrement,id TEXT, comment_id TEXT,comment_phone TEXT,comments_header TEXT,comments_name TEXT, comments_time TEXT,comments_content TEXT);");
		db.execSQL("create table words(app_id integer primary key autoincrement, id TEXT,created TEXT,from_person TEXT,content TEXT,if_read TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT);");
		db.execSQL("create table fans(app_id integer primary key autoincrement,id TEXT, NO_OFF TEXT);");
		db.execSQL("create table start(app_id integer primary key autoincrement,start TEXT);");
		db.execSQL("create table discover(app_id integer primary key autoincrement,id TEXT,title TEXT,time TEXT,information TEXT, commitperson TEXT,image1 TEXT,image2 TEXT,image3 TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT,sharenumber TEXT, commentsnumber TEXT, followingnumber TEXT);");
		db.execSQL("create table lostrecord(app_id integer primary key autoincrement,id TEXT, what TEXT,title TEXT,losttime TEXT, lostlatitude TEXT,lostlangitude TEXT , lostkind TEXT,lostinformation TEXT, commitperson TEXT,contactphone TEXT,sharenumber TEXT,commentsnumber TEXT,followingnumber TEXT,location TEXT,created TEXT,comments TEXT,lostimage1 TEXT,lostimage2 TEXT,lostimage3 TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT);");
	    db.execSQL("create table findrecord(app_id integer primary key autoincrement,id TEXT, what TEXT,title TEXT,losttime TEXT, lostlatitude TEXT,lostlangitude TEXT , lostkind TEXT,lostinformation TEXT, commitperson TEXT,contactphone TEXT,sharenumber TEXT,commentsnumber TEXT,followingnumber TEXT,location TEXT,created TEXT,comments TEXT,lostimage1 TEXT,lostimage2 TEXT,lostimage3 TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT);");
		db.execSQL("create table commentsrecord(app_id integer primary key autoincrement,id TEXT, comment_id TEXT,comment_phone TEXT,comments_header TEXT,comments_name TEXT, comments_time TEXT,comments_content TEXT);");
		db.execSQL("create table discoverrecord(app_id integer primary key autoincrement,id TEXT,title TEXT,time TEXT,information TEXT, commitperson TEXT,image1 TEXT,image2 TEXT,image3 TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT,sharenumber TEXT, commentsnumber TEXT, followingnumber TEXT);");
		db.execSQL("create table lostpersonal(app_id integer primary key autoincrement,id TEXT, what TEXT,title TEXT,losttime TEXT, lostlatitude TEXT,lostlangitude TEXT , lostkind TEXT,lostinformation TEXT, commitperson TEXT,contactphone TEXT,sharenumber TEXT,commentsnumber TEXT,followingnumber TEXT,location TEXT,created TEXT,comments TEXT,lostimage1 TEXT,lostimage2 TEXT,lostimage3 TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT);");
		db.execSQL("create table findpersonal(app_id integer primary key autoincrement,id TEXT, what TEXT,title TEXT,losttime TEXT, lostlatitude TEXT,lostlangitude TEXT , lostkind TEXT,lostinformation TEXT, commitperson TEXT,contactphone TEXT,sharenumber TEXT,commentsnumber TEXT,followingnumber TEXT,location TEXT,created TEXT,comments TEXT,lostimage1 TEXT,lostimage2 TEXT,lostimage3 TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT);");
		db.execSQL("create table discoverpersonal(app_id integer primary key autoincrement,id TEXT,title TEXT,time TEXT,information TEXT, commitperson TEXT,image1 TEXT,image2 TEXT,image3 TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT,sharenumber TEXT, commentsnumber TEXT, followingnumber TEXT);");
		db.execSQL("create table repliesmessage(app_id integer primary key autoincrement,id TEXT, comment_id TEXT,comment_phone TEXT,comments_header TEXT,comments_name TEXT, comments_time TEXT,comments_content TEXT,comments_kind TEXT);");
		db.execSQL("create table messagediscover(app_id integer primary key autoincrement,id TEXT,title TEXT,time TEXT,information TEXT, commitperson TEXT,image1 TEXT,image2 TEXT,image3 TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT,sharenumber TEXT, commentsnumber TEXT, followingnumber TEXT);");
		db.execSQL("create table messagelostandfind(app_id integer primary key autoincrement,id TEXT, what TEXT,title TEXT,losttime TEXT, lostlatitude TEXT,lostlangitude TEXT , lostkind TEXT,lostinformation TEXT, commitperson TEXT,contactphone TEXT,sharenumber TEXT,commentsnumber TEXT,followingnumber TEXT,location TEXT,created TEXT,comments TEXT,lostimage1 TEXT,lostimage2 TEXT,lostimage3 TEXT,personcreated TEXT,personname TEXT,personportrait TEXT,personlocation TEXT);");
		db.execSQL("create table system(app_id integer primary key autoincrement,id TEXT, time TEXT,content TEXT);");
		db.execSQL("create table NO(app_id integer primary key autoincrement,NO TEXT);");

	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
