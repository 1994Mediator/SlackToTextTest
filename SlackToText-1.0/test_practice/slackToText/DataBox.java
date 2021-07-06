package slackToText;

/**
 * データを入れるためのクラス
 * @author お茶
 *
 */
public class DataBox
{
    String ts ="";
    String threadTS ="";
    String folderName ="";
    String fileName ="";
    String[] text = {};
    String[] username = {};

    DataBox(String[] username,String[] text,String threadTS,String ts,String folderName,String fileName)
    {
        this.username = username;
        this.text = text;
        this.ts = ts;
        this.threadTS = threadTS;
        this.folderName = folderName;
        this.fileName = fileName;

    }
}