package sfsu.csc413.foodcraft;

/**
 * Created by pklein on 11/26/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UPCDatabase {
    private DBHelper dbHelper;

    public UPCDatabase(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void insert(String code, String title) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("upc", code);
        values.put("product",title);
        db.insert("cache", null, values);
        db.close(); // Closing database connection
    }

    public void delete(String code) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete("cache", "upc" + "= ?", new String[]{String.valueOf(code)});
        db.close(); // Closing database connection
    }

    public String getProductByCode(String code){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                "upc" + "," +
                "product" +
                " FROM " + "cache"
                + " WHERE " +
                "upc" + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(code) } );
        String title = null;
        if (cursor.moveToFirst()) {
            do {
                title = cursor.getString(cursor.getColumnIndex("product"));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return title;
    }

}
