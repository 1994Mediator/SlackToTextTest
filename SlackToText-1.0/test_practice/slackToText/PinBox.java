package slackToText;

/**
 * ピン留フラグを入れるためのクラス
 * @author お茶
 *
 */
public class PinBox{
    String name;
    int day;
    int period;
    String[] id ={};
    String pins ="";
    PinBox(String name,String[] pins){
        this.name = name;
        this.id = pins;
    }
}