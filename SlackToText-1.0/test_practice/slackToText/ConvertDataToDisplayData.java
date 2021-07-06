package slackToText;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConvertDataToDisplayData
{

	//修正したい。
	/** JSON読み取りルートフォルダ */
	final static String DIR_PATH = "C:\\Users\\メールアカウント\\Desktop\\slack2"; //検索開始したいフォルダのPath(今回の場合なら~Folder/まで書く)
	/** EXCEL出力フォルダ */
	final static String EXPORT_ROOT_PATH = "C:\\Users\\メールアカウント\\Desktop\\タスクリスト";
	public static ArrayList<DataBox> dataList = new ArrayList<>();
	public static ArrayList<PinBox> pinList = new ArrayList<>();

	/**
	 * NULLチェック＆NULLの場合空白文字列を設定する。
	 * @param 判定するJSONオブジェクト
	 * @param 判定するキー値
	 * @return キー値がない場合は空白。ある場合は、プロパティの値を返却
	 */
	protected String convertNULL(JSONObject Jobject,String key){
	    String keyValue = "";
	    //バグ。thread_tsの存在有無はget == nullじゃ無理
	    if(!Jobject.isNull(key))
	    {
	    	keyValue = (String)Jobject.get(key);
	    }
	    return keyValue;
	}

	/**
	 * プロパティ中にプロパティが存在するモノの対応。
	 * @param 判定するJSONオブジェクト
	 * @param 判定するキー値
	 * @param 判定するキー値(プロパティの中にあるキー）
	 * @return キー値がない場合は空白。ある場合は、プロパティの値を返却
	 */
	protected String[] convertNULL2(JSONObject Jobject,String key,String key2){
	    String[] keyValue = {};
	    //バグ。thread_tsの存在有無はget == nullじゃ無理
	    if(!Jobject.isNull(key))
	    {
	    	if(Jobject.get(key).getClass().toString().equals("class java.lang.String"))
	    	{
	    		keyValue = new String[1];
	    		keyValue[0] = convertNULL(Jobject,key);
	    	}
	    	else
	    	{
	    		JSONArray data = (JSONArray)Jobject.get(key);
	    		keyValue = new String[data.length()];
	    		for(int i=0;i<data.length();i++)
	    		{
	    			JSONObject Jobject2 = data.getJSONObject(i);
	    			keyValue[i] = convertNULL(Jobject2,key2);
	    		}
	    	}

	    }
	    else
	    {
	    	keyValue = new String[1];
	    	keyValue[0] ="";
	    }
	    return keyValue;
	}

	/**
	 * プロパティ中にプロパティが存在するモノの対応。
	 * @param 判定するJSONオブジェクト
	 * @return thread_tsプロパティ値がない場合はTSのプロパティ値を設定。<BR>ある場合は、thread_tsプロパティの値を返却
	 */
	protected String convertThread_TS(JSONObject Jobject){
	    String ts = "";

	    //バグ。thread_tsの存在有無はget == nullじゃ無理
	    if(Jobject.isNull("thread_ts"))
	    {
	    	ts = (String)Jobject.get("ts");
	    }
	    else
	    {
	    	ts = (String)Jobject.get("thread_ts");
	    }
	    return ts;
	}
	/**
	 * ユーザーID→ユーザー名に変更する。
	 * BOTのユーザーはほぼ対応していない。
	 * @param ユーザーID
	 * @return TEXTのユーザーID→ユーザー名に置換した値
	 */
	protected String userConvertName(String propertyUser){
	    String user = "";
	    if(propertyUser.contains("UF8RN8X3Q"))
	    {
	    	user =propertyUser.replace("UF8RN8X3Q", "お茶");
	    }
	    else if(propertyUser.contains("UFPE3K7TQ"))
	    {
	    	user =propertyUser.replace("UFPE3K7TQ", "水");
	    }
	    else if(propertyUser.contains("UGYJNCKU0"))
	    {
	    	user =propertyUser.replace("UGYJNCKU0", "糸目");
	    }
	    else if(propertyUser.contains("UF78X7VJ8"))
	    {
	    	user =propertyUser.replace("UF78X7VJ8", "八橋");
	    }
	    else if(propertyUser.contains("UF6ML1800"))
	    {
	    	user =propertyUser.replace("UF6ML1800", "由宇");
	    }
	    else if(propertyUser.contains("UF9AU34GM"))
	    {
	    	user =propertyUser.replace("UF9AU34GM", "彩萌");
	    }
	    //ここからBOT
	    else if(propertyUser.contains("UR5QEE3U1"))
	    {
	    	user =propertyUser.replace("UR5QEE3U1", "github");
	    }
	    else if(propertyUser.contains("UQDPHC7ND"))
	    {
	    	user =propertyUser.replace("UQDPHC7ND", "毎日担当変更");
	    }
	    else
	    {
	    	user = propertyUser;
	    }
	    return user;
	}
}