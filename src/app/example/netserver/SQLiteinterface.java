package app.example.netserver;

import java.util.List;
import java.util.Map;

public interface SQLiteinterface {
	/**
	 * 判断是否是第一次进入app
	 * 
	 * @param params
	 * @return
	 */
	public boolean addStart(Object[] params);

	public List<Map<String, String>> listStartmaps(String[] selectionArgs);

	/**
	 * 信息的添删查改
	 * 
	 * @param params
	 * @param table
	 * @return
	 */
	public boolean addPerson(Object[] params, String table);

	public boolean deletePerson(Object[] params, String table);

	public boolean updataPerson(Object[] params, String table);

	public Map<String, String> viewPerson(String[] selectionArgs, String table);

	public List<Map<String, String>> listPersonmaps(String[] selectionArgs,
			String table);
	/**
	 * 回复评论时加载入数据库单条message信息
	 * 
	 * @param params
	 * @param table
	 * @return
	 */
	public boolean addMessage(Object[] params);

	public boolean deleteMessage(Object[] params);

	public boolean updataMessage(Object[] params);

	public Map<String, String> viewMessage(String[] selectionArgs);

	public List<Map<String, String>> listMessagemaps(String[] selectionArgs);
	
	
	/**
	 * 回复评论时加载入数据库单条news信息
	 * 
	 * @param params
	 * @return
	 */
	public boolean addnews(Object[] params);

	public boolean deletenews(Object[] params);

	public boolean updatanews(Object[] params);

	public Map<String, String> viewnews(String[] selectionArgs);

	public List<Map<String, String>> listnewsmaps(String[] selectionArgs);
	
	
	/**
	 * 评论的添删查改
	 * 
	 * @param params
	 * @return
	 */
	public boolean addCommentsToMessage(Object[] params);

	public boolean deleteCommentsToMessage(Object[] params);

	public boolean updataCommentsToMessage(Object[] params);

	public Map<String, String> viewCommentsToMessage(String[] selectionArgs);

	public List<Map<String, String>> listCommentsToMessagemaps(String[] selectionArgs);
	/**
	 * 信息记录的添删查改
	 * 
	 * @param params
	 * @param table
	 * @return
	 */
	public boolean addPersonCord(Object[] params, String table);

	public boolean deletePersonCord(Object[] params, String table);

	public boolean updataPersonCord(Object[] params, String table);

	public Map<String, String> viewPersonCord(String[] selectionArgs, String table);

	public List<Map<String, String>> listPersonCordmaps(String[] selectionArgs,
			String table);
/**
 * 个人主页上的数据增删查改
 * @param params
 * @param table
 * @return
 */
	
	public boolean addPersonal(Object[] params, String table);

	public boolean deletePersonal(Object[] params, String table);

	public boolean updataPersonal(Object[] params, String table);

	public Map<String, String> viewPersonal(String[] selectionArgs, String table);

	public List<Map<String, String>> listPersonalmaps(String[] selectionArgs,
			String table);
	/**
	 * 评论的添删查改
	 * 
	 * @param params
	 * @return
	 */
	public boolean addComments(Object[] params);

	public boolean deleteComments(Object[] params);

	public boolean updataComments(Object[] params);

	public Map<String, String> viewComments(String[] selectionArgs);

	public List<Map<String, String>> listCommentsmaps(String[] selectionArgs);

	/**
	 * 发现的添删查改
	 * 
	 * @param params
	 * @return
	 */
	public boolean addDiscover(Object[] params);

	public boolean deleteDiscover(Object[] params);

	public boolean updataDiscover(Object[] params);

	public Map<String, String> viewDiscover(String[] selectionArgs);

	public List<Map<String, String>> listDiscovermaps(String[] selectionArgs);
	/**
	 * 发现记录的添删查改
	 * 
	 * @param params
	 * @return
	 */
	public boolean addDiscoverCord(Object[] params);

	public boolean deleteDiscoverCord(Object[] params);

	public boolean updataDiscoverCord(Object[] params);

	public Map<String, String> viewDiscoverCord(String[] selectionArgs);

	public List<Map<String, String>> listDiscoverCordmaps(String[] selectionArgs);
	/**
	 * 个人主页发现的添删查改
	 * 
	 * @param params
	 * @return
	 */
	public boolean addDiscoverPersonal(Object[] params);

	public boolean deleteDiscoverPersonal(Object[] params);

	public boolean updataDiscoverPersonal(Object[] params);

	public Map<String, String> viewDiscoverPersonal(String[] selectionArgs);

	public List<Map<String, String>> listDiscoverPersonalmaps(String[] selectionArgs);

	/**
	 * 点赞的判断
	 * 
	 * @param params
	 * @return
	 */
	public boolean addFans(Object[] params);

	public boolean deleteFans(Object[] params);

	public boolean updataFans(Object[] params);

	public Map<String, String> viewFans(String[] selectionArgs);

	public List<Map<String, String>> listFansmaps(String[] selectionArgs);

	/**
	 * 留言的增删查改
	 * 
	 * @param params
	 * @return
	 */
	public boolean addWords(Object[] params);

	public boolean deleteWords(Object[] params);

	public boolean updataWords(Object[] params);

	public Map<String, String> viewWords(String[] selectionArgs);

	public List<Map<String, String>> listWordsmaps(String[] selectionArgs);

	/**
	 * 系统消息的增删查改
	 * 
	 * @param params
	 * @return
	 */
	public boolean addSystem(Object[] params);

	public boolean deleteSystem(Object[] params);

	public boolean updataSystem(Object[] params);

	public Map<String, String> viewSystem(String[] selectionArgs);

	public List<Map<String, String>> listSystemmaps(String[] selectionArgs);
	
	
	public boolean addNO(Object[] params);
	public List<Map<String, String>> listNO(String[] selectionArgs);
	/**
	 * 删除表
	 */
	public boolean EmptyTable(String table);
	
}
