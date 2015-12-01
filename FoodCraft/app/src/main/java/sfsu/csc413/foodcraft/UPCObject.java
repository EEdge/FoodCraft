package sfsu.csc413.foodcraft;

/**
 * Created by pklein on 11/26/15.
 */
public class UPCObject {
    public String code;
    public String product_title;
    public String original_title;
    //public String original_title;
    //UPCObject(String code, String product_title, String original_title){
    UPCObject(String code, String product_title, String original_title){
        this.code = code;
        this.product_title = product_title;
        this.original_title = original_title;
        //this.original_title = original_title;
    }
}
