package slackToText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportExcel
{
	/** EXCEL出力フォルダ */
	final static String EXPORT_ROOT_PATH = "C:\\Users\\メールアカウント\\Desktop\\タスクリスト";
	/**
	 * SLACKのメッセージ履歴リストをフォルダ単位でシート分けして出力する。
	 * @param SLACKのメッセージ履歴リスト
	 */
	protected void setExcelForData(ArrayList<DataBox> dataList){
	    try {
	    	String outputFilePath = EXPORT_ROOT_PATH+"/"+"Export.xlsx";
	    	FileInputStream f =null;
	    	XSSFWorkbook book = null;
			File dir = new File(outputFilePath);
	        if(!dir.exists())
	        {
	        	book = new XSSFWorkbook();
	        }
	        //正直この処理行かないと思う（未検証）
	        else
	        {
	        	f =new FileInputStream(outputFilePath);
	        	book = new XSSFWorkbook(f);
	        }

	    	XSSFRow RowTemp = null;
	    	int count=0;
	        // 内容をセットする
	        for(int i = 0; i < dataList.size(); i++)
	        {
	        	XSSFSheet sheet =null;
	            CellStyle style0 = book.createCellStyle();
	            //折り返し設定
	            style0.setWrapText(true);

	        	//チャンネルでシートを分ける。
	        	if(i > 0 && !dataList.get(i).folderName.equals(dataList.get(i-1).folderName))
	        	{
	        		System.out.println(dataList.get(i).folderName+"のExcel出力中…");
	        		sheet = book.createSheet(dataList.get(i).folderName);
	        		count = 0;
	        		setHeader(sheet);
	        	}
	        	else if(i==0)
	        	{

	        		System.out.println(dataList.get(i).folderName+"のExcel出力中…");
	        		sheet = book.createSheet(dataList.get(i).folderName);
	    	    	setHeader(sheet);
	        	}
	        	else
	        	{
	        		sheet = book.getSheet(dataList.get(i).folderName);
	        	}
	        	//シート単位で行を取得
	        	RowTemp = sheet.createRow(count+1);
	        	RowTemp.createCell(0).setCellValue(dataList.get(i).threadTS);
	        	RowTemp.createCell(1).setCellValue(dataList.get(i).ts);
	        	RowTemp.createCell(2).setCellValue(dataList.get(i).folderName);
	        	RowTemp.createCell(3).setCellValue(dataList.get(i).fileName.replace(".json", ""));
	        	RowTemp.createCell(4).setCellValue(dataList.get(i).username[0]);
	        	RowTemp.createCell(5).setCellValue(dataList.get(i).text[0]);
	        	//折り返しはTEXT（内容）のみでOK
	        	RowTemp.getCell(5).setCellStyle(style0);
	        	count++;
	        }
	        // ファイルに書き出し閉じる
	        FileOutputStream fout = new FileOutputStream(outputFilePath);
            book.write(fout);
            book.close();
	        Date date = new Date();
	        System.out.println(date.toString() + " ファイル出力完了！");

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}

	/**
	 * SLACKのピン留をシート単位にして設定する。
	 * @param SLACKのピン留履歴リスト
	 */
	protected void setExcelForPinsFlg(ArrayList<PinBox> dataList){
	    try {
	    	String outputFilePath = EXPORT_ROOT_PATH+"/"+"Export.xlsx";
	    	FileInputStream f =new FileInputStream(outputFilePath);
	    	//ExcelのZipの最小が決まっていて、それを下回ると読み込みすらできない。
	    	//下記で最小値を変更している。
	    	ZipSecureFile.setMinInflateRatio(0.001);
	    	XSSFWorkbook book = new XSSFWorkbook(f);
	        // 内容をセットする
	        for(int i = 0; i < dataList.size(); i++)
	        {
	        	XSSFSheet sheet =book.getSheet(dataList.get(i).name);
	        	String[] data =dataList.get(i).id;
	        	for(int j = 0; j < data.length; j++)
	        	{
	        		for(int k=0;k<sheet.getLastRowNum();k++)
	        		{
	        			String compData =sheet.getRow(k).getCell(1).getStringCellValue();
	        			if(compData.equals(data[j]))
	        			{
	        				sheet.getRow(k).createCell(6).setCellValue("*");
	        				System.out.println(dataList.get(i).name + " " +data[j]+ "設定完了!");
	        				break;
	        			}
	        		}
	        	}
	        }

	        // ファイルに書き出し閉じる
	        FileOutputStream fout = new FileOutputStream(outputFilePath);
            book.write(fout);
            book.close();
	        Date date = new Date();

	        System.out.println(date.toString() + " ファイル出力完了！");

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}

	/**
	 * Excelのヘッダー設定。
	 * @param シート
	 */
	protected void setHeader(XSSFSheet sheet){
        // ヘッダーを指定する
		XSSFRow row0 = sheet.createRow(0);
    	row0.createCell(0).setCellValue("ts番号(Thread_ts番号)");
    	row0.createCell(1).setCellValue("ts番号");
    	row0.createCell(2).setCellValue("チャンネル名");
    	row0.createCell(3).setCellValue("発信日");
    	row0.createCell(4).setCellValue("ユーザー名");
    	row0.createCell(5).setCellValue("内容");
    	row0.createCell(6).setCellValue("ピン留フラグ");
        sheet.setColumnWidth(0, 20*256);
        sheet.setColumnWidth(1, 20*256);
        sheet.setColumnWidth(2, 20*256);
        sheet.setColumnWidth(3, 20*256);
        sheet.setColumnWidth(4, 20*256);
        sheet.setColumnWidth(5, 35*256);
        sheet.setColumnWidth(6, 11*256);
        //ヘッダ行にオートフィルタの設定
        sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, 6));
	}
}