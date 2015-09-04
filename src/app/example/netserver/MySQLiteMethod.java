package app.example.netserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MySQLiteMethod implements SQLiteinterface {

	private MySQLiteOpenHelper mySQLiteOpenHelper = null;

	public MySQLiteMethod(Context context) {
		mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
	}

	@Override
	public boolean addMessage(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into "
					+ "messagelostandfind"
					+ "( id, what,title, losttime, lostlatitude,lostlangitude, lostkind, lostinformation,"
					+ "commitperson, contactphone, sharenumber, commentsnumber,"
					+ "followingnumber, location, created, "
					+ "  comments,lostimage1,lostimage2,lostimage3,personcreated,personname,personportrait,personlocation) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deleteMessage(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + "messagelostandfind" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataMessage(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update "
					+ "messagelostandfind"
					+ " set id=?,what=?, title=?, losttime=?, lostlatitude=?,lostlangitude=?, lostkind=?, lostinformation=?,"
					+ "commitperson=?, contactphone=?, sharenumber=?, commentsnumber=?,"
					+ "followingnumber=?, location=?, created=?, "
					+ " comments=?,lostimage1=?,lostimage2=?,lostimage3=? ,personcreated =? ,personname=? ,personportrait=? ,personlocation =? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewMessage(String[] selectionArgs) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + "messagelostandfind"
					+ " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listMessagemaps(String[] selectionArgs) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "messagelostandfind"
				+ " order by losttime desc ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/**
	 * 
	 */
	@Override
	public boolean addnews(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into "
					+ "messagediscover"
					+ "( id ,title ,time,information , commitperson ,image1,image2 ,image3 ,personcreated ,personname ,personportrait ,personlocation ,sharenumber, commentsnumber, followingnumber) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deletenews(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + "messagediscover" + " where app_id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updatanews(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update messagediscover set id=?, title=?,time=?, information=?,commitperson=?,"
					+ "image1=?,image2=?,image3=? ,personcreated =? ,personname=? ,personportrait=? ,personlocation =? ,sharenumber =?, commentsnumber =?, followingnumber =? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewnews(String[] selectionArgs) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + "messagediscover" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listnewsmaps(String[] selectionArgs) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "messagediscover"
				+ " order by time desc ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/***********************************************************************/
	/**
	 * 信息的增删查改
	 */
	@Override
	public boolean addPerson(Object[] params, String table) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into "
					+ table
					+ "( id, what,title, losttime, lostlatitude,lostlangitude, lostkind, lostinformation,"
					+ "commitperson, contactphone, sharenumber, commentsnumber,"
					+ "followingnumber, location, created, "
					+ "  comments,lostimage1,lostimage2,lostimage3,personcreated,personname,personportrait,personlocation) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deletePerson(Object[] params, String table) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + table + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataPerson(Object[] params, String table) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update "
					+ table
					+ " set id=?,what=?, title=?, losttime=?, lostlatitude=?,lostlangitude=?, lostkind=?, lostinformation=?,"
					+ "commitperson=?, contactphone=?, sharenumber=?, commentsnumber=?,"
					+ "followingnumber=?, location=?, created=?, "
					+ " comments=?,lostimage1=?,lostimage2=?,lostimage3=? ,personcreated =? ,personname=? ,personportrait=? ,personlocation =? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewPerson(String[] selectionArgs, String table) {
		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + table + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listPersonmaps(String[] selectionArgs,
			String table) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + table + " order by app_id ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/**************************************************************************/
	/**
	 * 信息记录的增删查改
	 */
	@Override
	public boolean addPersonCord(Object[] params, String table) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into "
					+ table
					+ "( id, what,title, losttime, lostlatitude,lostlangitude, lostkind, lostinformation,"
					+ "commitperson, contactphone, sharenumber, commentsnumber,"
					+ "followingnumber, location, created, "
					+ "  comments,lostimage1,lostimage2,lostimage3,personcreated,personname,personportrait,personlocation) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deletePersonCord(Object[] params, String table) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + table + " where app_id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataPersonCord(Object[] params, String table) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update "
					+ table
					+ " set id=?,what=?, title=?, losttime=?, lostlatitude=?,lostlangitude=?, lostkind=?, lostinformation=?,"
					+ "commitperson=?, contactphone=?, sharenumber=?, commentsnumber=?,"
					+ "followingnumber=?, location=?, created=?, "
					+ " comments=?,lostimage1=?,lostimage2=?,lostimage3=? ,personcreated =? ,personname=? ,personportrait=? ,personlocation =? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewPersonCord(String[] selectionArgs,
			String table) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + table + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listPersonCordmaps(String[] selectionArgs,
			String table) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + table + " order by losttime desc ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/*****************************************************************************/
	/**
	 * 个人主页上的增删查改
	 */
	@Override
	public boolean addPersonal(Object[] params, String table) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into "
					+ table
					+ "( id, what,title, losttime, lostlatitude,lostlangitude, lostkind, lostinformation,"
					+ "commitperson, contactphone, sharenumber, commentsnumber,"
					+ "followingnumber, location, created, "
					+ "  comments,lostimage1,lostimage2,lostimage3,personcreated,personname,personportrait,personlocation) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deletePersonal(Object[] params, String table) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + table + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataPersonal(Object[] params, String table) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update "
					+ table
					+ " set id=?,what=?, title=?, losttime=?, lostlatitude=?,lostlangitude=?, lostkind=?, lostinformation=?,"
					+ "commitperson=?, contactphone=?, sharenumber=?, commentsnumber=?,"
					+ "followingnumber=?, location=?, created=?, "
					+ " comments=?,lostimage1=?,lostimage2=?,lostimage3=? ,personcreated =? ,personname=? ,personportrait=? ,personlocation =? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewPersonal(String[] selectionArgs, String table) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + table + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listPersonalmaps(String[] selectionArgs,
			String table) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + table + " order by losttime desc ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/**
	 * 回复评论时加载入数据库单条imessage或者news信息的增删查改
	 */

	@Override
	public boolean addCommentsToMessage(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into "
					+ "repliesmessage"
					+ "( id, comment_id,comment_phone ,comments_header, comments_name, comments_time,comments_content,comments_kind) values(?,?,?,?,?,?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deleteCommentsToMessage(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + "repliesmessage"
					+ " where comment_id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataCommentsToMessage(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update "
					+ "repliesmessage"
					+ " set id=?,what=?, title=?, losttime=?, lostlatitude=?,lostlangitude=?, lostkind=?, lostinformation=?,"
					+ "commitperson=?, contactphone=?, sharenumber=?, commentsnumber=?,"
					+ "followingnumber=?, location=?, created=?, "
					+ " comments=?,lostimage1=?,lostimage2=?,lostimage3=? ,personcreated =? ,personname=? ,personportrait=? ,personlocation =? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewCommentsToMessage(String[] selectionArgs) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + "repliesmessage" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listCommentsToMessagemaps(
			String[] selectionArgs) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "repliesmessage"
				+ " order by comments_time desc;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/***********************************************************************/
	/**
	 * 评论的增删查改
	 */

	@Override
	public boolean addComments(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into "
					+ "comments"
					+ "( id, comment_id,comment_phone ,comments_header, comments_name, comments_time,comments_content) values(?,?,?,?,?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deleteComments(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + "comments" + " where app_id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataComments(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update "
					+ "comments"
					+ " set id=?,what=?, title=?, losttime=?, lostlatitude=?,lostlangitude=?, lostkind=?, lostinformation=?,"
					+ "commitperson=?, contactphone=?, sharenumber=?, commentsnumber=?,"
					+ "followingnumber=?, location=?, created=?, "
					+ " comments=?,lostimage1=?,lostimage2=?,lostimage3=? ,personcreated =? ,personname=? ,personportrait=? ,personlocation =? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewComments(String[] selectionArgs) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + "comments" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listCommentsmaps(String[] selectionArgs) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "comments"
				+ " order by comments_time ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/**
	 * 个人主页上的发现的增删查改
	 */

	@Override
	public boolean addDiscoverPersonal(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into "
					+ "discoverpersonal"
					+ "( id ,title ,time,information , commitperson ,image1,image2 ,image3 ,personcreated ,personname ,personportrait ,personlocation ,sharenumber, commentsnumber, followingnumber) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deleteDiscoverPersonal(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + "discoverpersonal"
					+ " where app_id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataDiscoverPersonal(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update discoverpersonal set id=?, title=?,time=?, information=?,commitperson=?,"
					+ "image1=?,image2=?,image3=? ,personcreated =? ,personname=? ,personportrait=? ,personlocation =? ,sharenumber =?, commentsnumber =?, followingnumber =? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewDiscoverPersonal(String[] selectionArgs) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + "discoverpersonal" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listDiscoverPersonalmaps(
			String[] selectionArgs) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "discoverpersonal"
				+ " order by time desc;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/**
	 * 发现记录的增删查改
	 */

	@Override
	public boolean addDiscoverCord(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into "
					+ "discoverrecord"
					+ "( id ,title ,time,information , commitperson ,image1,image2 ,image3 ,personcreated ,personname ,personportrait ,personlocation ,sharenumber, commentsnumber, followingnumber) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deleteDiscoverCord(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + "discoverrecord" + " where app_id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataDiscoverCord(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update discoverrecord set id=?, title=?,time=?, information=?,commitperson=?,"
					+ "image1=?,image2=?,image3=? ,personcreated =? ,personname=? ,personportrait=? ,personlocation =? ,sharenumber =?, commentsnumber =?, followingnumber =? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewDiscoverCord(String[] selectionArgs) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + "discoverrecord" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listDiscoverCordmaps(String[] selectionArgs) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "discoverrecord"
				+ " order by time desc;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/**
	 * 发现的增删查改
	 */

	@Override
	public boolean addDiscover(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into "
					+ "discover"
					+ "( id ,title ,time,information , commitperson ,image1,image2 ,image3 ,personcreated ,personname ,personportrait ,personlocation ,sharenumber, commentsnumber, followingnumber) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deleteDiscover(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + "discover" + " where app_id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataDiscover(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update discover set id=?, title=?,time=?, information=?,commitperson=?,"
					+ "image1=?,image2=?,image3=? ,personcreated =? ,personname=? ,personportrait=? ,personlocation =? ,sharenumber =?, commentsnumber =?, followingnumber =? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewDiscover(String[] selectionArgs) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + "discover" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listDiscovermaps(String[] selectionArgs) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "discover" + " order by app_id ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/**
	 * 点赞的判断
	 */

	@Override
	public boolean addFans(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into " + "fans" + "( id ,NO_OFF) values(?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deleteFans(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + "fans" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataFans(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update fans set id=?, NO_OFF=? where id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewFans(String[] selectionArgs) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + "fans" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listFansmaps(String[] selectionArgs) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "fans" + " order by id ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/**
 * 
 */
	@Override
	public boolean addStart(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into " + "start" + "( start ) values(?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public List<Map<String, String>> listStartmaps(String[] selectionArgs) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "start" + " order by app_id ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	/***********************************************************************/
	/**
	 * 留言的增删查改
	 */
	@Override
	public boolean addWords(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into "
					+ "words"
					+ "( id, created, from_person, content, if_read,personcreated,personname,personportrait,personlocation) values(?,?,?,?,?,?,?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deleteWords(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + "words" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataWords(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update "
					+ "words"
					+ " set id=?,created=?, from_person=?, content=?, if_read=?,personcreated =? ,personname=? ,personportrait=? ,personlocation =? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewWords(String[] selectionArgs) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + "words" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listWordsmaps(String[] selectionArgs) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "words" + " order by app_id ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}

	@Override
	public boolean EmptyTable(String table) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "DELETE FROM " + table;

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean addSystem(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into " + "system"
					+ "(id,time,content) values(?,?,?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean deleteSystem(Object[] params) {
		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "delete from " + "system" + " where app_id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public boolean updataSystem(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "update " + "system"
					+ " set  id=?,time=?, content=? where app_id=?  ";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}

	@Override
	public Map<String, String> viewSystem(String[] selectionArgs) {

		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "select * from " + "system" + " where id=?";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}

			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, String>> listSystemmaps(String[] selectionArgs) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "system" + " order by time desc ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}
/********************************************************/
	@Override
	public boolean addNO(Object[] params) {

		boolean flag = false;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			String sql = "insert into " + "NO"
					+ "(NO) values(?)";
			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			sqLiteDatabase.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}
		return flag;
	}
	@Override
	public List<Map<String, String>> listNO(String[] selectionArgs) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String sql = "select * from " + "NO" + " order by app_id ;";
		SQLiteDatabase sqLiteDatabase = null;
		try {

			sqLiteDatabase = mySQLiteOpenHelper.getWritableDatabase();
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				list.add(map);
			}
		} catch (Exception e) {

		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}

		}

		return list;
	}
}
