package slackToText;

import java.io.File;
import java.util.ArrayList;

public class JsonTest
{

	//	final static String DIR_PATH = "D:\\slack"; //検索開始したいフォルダのPath(今回の場合なら~Folder/まで書く)
	/** JSON読み取りルートフォルダ */
	final static String DIR_PATH = "C:\\Users\\メールアカウント\\Desktop\\slack2"; //検索開始したいフォルダのPath(今回の場合なら~Folder/まで書く)
	/** EXCEL出力フォルダ */
	final static String EXPORT_ROOT_PATH = "C:\\Users\\メールアカウント\\Desktop\\タスクリスト";
	public static ArrayList<DataBox> dataList = new ArrayList<>();
	public static ArrayList<PinBox> pinList = new ArrayList<>();

	/**
	 * SLACK:JSONファイル読み取り→Excel出力メイン処理
	 */
	public static void main(String[] args) throws NullPointerException
	{
		File dir = new File(EXPORT_ROOT_PATH+"/"+"Export.xlsx");
		//既に存在するファイルは削除する。（同じシート名の有無判断が非常に面倒なため）
        if(dir.exists())
        {
        	dir.delete();

        }
		//フォルダを読み取る
        ReadFile read = new ReadFile();
        read.file_search(DIR_PATH);
		//ログの出力(本当ならログファイルつくって出すべき)
	    System.out.println("Excel出力処理開始…");
	    ExportExcel excel = new ExportExcel();
	    excel.setExcelForData(dataList);
	    System.out.println("Excel出力処理終了！");
	    //Channels.json→ピン留フラグをシートに設定する。
	    excel.setExcelForPinsFlg(pinList);
	}
}