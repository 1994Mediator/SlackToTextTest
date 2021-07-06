package slackToText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReadFile
{

	//	final static String DIR_PATH = "D:\\slack"; //検索開始したいフォルダのPath(今回の場合なら~Folder/まで書く)
	/** JSON読み取りルートフォルダ */
	final static String DIR_PATH = "C:\\Users\\メールアカウント\\Desktop\\slack2"; //検索開始したいフォルダのPath(今回の場合なら~Folder/まで書く)
	/** EXCEL出力フォルダ */
	final static String EXPORT_ROOT_PATH = "C:\\Users\\メールアカウント\\Desktop\\タスクリスト";
	public static ArrayList<DataBox> dataList = new ArrayList<>();
	public static ArrayList<PinBox> pinList = new ArrayList<>();

	/**
	 * 共通JSONプロパティ読み取り
	 */
	public static ArrayList<DataBox> readJSONFile(String data,String fileName,String folderName) throws NullPointerException{
	    JSONArray JArray = new JSONArray(data);
	    for(int n=0; n<JArray.length(); n++) {
	        JSONObject Jobject = JArray.getJSONObject(n);
	        //ユーザーを取得
	        String[] username = {};
	        //テキスト内容を取得
	        String[] text = {};
	        //スレッド番号を取得
	        String threadTS = "";
	        String ts = "";
	        ConvertDataToDisplayData convert = new ConvertDataToDisplayData();
	        //ユーザーを取得
	        String subtype = convert.convertNULL(Jobject,"subtype");
	        //とりあえずBOTのメッセージ以外は共通処理でいい（MESSAGE以外のタイプは情報として大事なものがないから）
	        if(!subtype.equals("bot_message"))
	        {
		        //ユーザーを取得
	        	text = new String[1];
	        	username = new String[1];
		        username[0] = convert.convertNULL(Jobject,"user");
		        username[0] = convert.userConvertName(username[0]);
		        //テキスト内容を取得
		        text[0] = convert.convertNULL(Jobject,"text");
		        text[0] = convert.userConvertName(text[0]);
		        //スレッド番号を取得（スレッド単位で共通のTSを取得）
		        threadTS = convert.convertThread_TS(Jobject);
		        //ピン留フラグの判定のため必要。
		        ts = convert.convertNULL(Jobject,"ts");
	        }
	        //Twitterの情報が欲しいために作った処理。
	        else
	        {
		        //ユーザーを取得
		        username = convert.convertNULL2(Jobject,"attachments","author_name");
		        //bot_message中にattachmentsも何もない。
		        if(username.length == 1 && username[0].equals(""))
		        {
		        	username[0] = convert.convertNULL(Jobject,"user");
		        }
		        username[0] = convert.userConvertName(username[0]);
		        //テキスト内容を取得
		        text = convert.convertNULL2(Jobject,"attachments","text");
		        text[0] = convert.userConvertName(text[0]);
		        //スレッド番号を取得（スレッド単位で共通のTSを取得）
		        threadTS = convert.convertThread_TS(Jobject);
		        //ピン留フラグの判定のため必要。
		        ts = convert.convertNULL(Jobject,"ts");
	        }


	        dataList.add(new DataBox(username,text,threadTS,ts,folderName,fileName));
	    }
	    return dataList;
	}
	/**
	 * チャンネル.jsonのプロパティ読み取り
	 */
	public static ArrayList<PinBox> readJSONChannelsFile(String data,String fileName) throws NullPointerException{
	    JSONArray JArray = new JSONArray(data);
	    for(int n=0; n<JArray.length(); n++) {
	        JSONObject Jobject = JArray.getJSONObject(n);
	        //ユーザーを取得
	        ConvertDataToDisplayData convert = new ConvertDataToDisplayData();
	        String name = convert.convertNULL(Jobject,"name");

	        //スレッド番号を取得
	        String[] id = convert.convertNULL2(Jobject,"pins","id");
	        pinList.add(new PinBox(name,id));
	    }
	    return pinList;
	}


	/**
	 * 1つのファイルを読み取る処理。コピペ。
	 * @param JSONファイルのあるファイルパス
	 * @return JSONプロパティ(STRING)
	 */
	private static String readOneFile(String filepath){
	    try{
	        File file = new File(filepath);
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        String data = "";
	        String str = br.readLine();
	        while(str != null){
	            data += str;
	            str = br.readLine();
	        }
	        br.close();
	        return data;
	    }catch(FileNotFoundException e){
	        System.out.println(e);
	        return null;
	    }catch(IOException e){
	        System.out.println(e);
	        return null;
	    }
	}

	/**
	 * ファイル検索して、JSONファイルを読み取る。
	 * @param 検索するルートフォルダパス
	 */
	public void file_search(String path){
	    File dir = new File(path);
	    File files[] = dir.listFiles();
	    for(int i=0; i<files.length; i++)
	    {
	        String file_name = files[i].getName();
	        if(files[i].isDirectory())
	        {  //ディレクトリの場合、フォルダ名とそのファイルパスを引数にもう一度検索する。
	        	//構成上フォルダ→ファイルにしかならないので。
	            file_search2(path+"/"+file_name,file_name);
			}
			else
			{
				//slackのエキスポートでは、フォルダのほかにChannel,userなど3つのJSONファイルがつくられる。
				//必要なのはChannelのみ。
				if(files[i].getName().contains("channels.json"))
				{
					String jsonFile = readOneFile(path+"/"+file_name);
					//チャンネル用の専用の読み取り処理（全然プロパティが違うので）
					readJSONChannelsFile(jsonFile,file_name);
				}

	        }
	    }
	}
	/**
	 * ファイル検索して、JSONファイルを読み取る。
	 * @param 検索するフォルダパス
	 * @param 検索するフォルダ名
	 */
	public static void file_search2(String path,String folderName){
	    File dir = new File(path);
	    File files[] = dir.listFiles();
	    System.out.println(folderName+"読み取り中…");
	    for(int i=0; i<files.length; i++)
	    {
	      String file_name = files[i].getName();
	      String jsonFile = readOneFile(path+"/"+file_name);
	      readJSONFile(jsonFile,file_name,folderName);
	    }
	}

}