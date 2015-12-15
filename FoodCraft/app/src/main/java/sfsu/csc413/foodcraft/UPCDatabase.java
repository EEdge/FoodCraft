package sfsu.csc413.foodcraft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class acts as a middle layer between the Database helper class and all other database actions.
 *
 * @file:UPCDatabase.java
 * @author: Paul Klein
 * @version: 1.0
 */
public class UPCDatabase {
    private DBHelper dbHelper;

    public UPCDatabase(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Inserts code and matching title into the UPC table.
     *
     * @param code
     * @param title
     */
    public void insert(String code, String title) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("upc", code);
        values.put("product", title);
        db.insert("cache", null, values);
        db.close(); // Closing database connection
    }

    /**
     * A function that makes a simple SELECT statement, based on the given UPC code.
     *
     * @param code
     * @return The cached title in the database, or NULL if no title is found.
     */
    public String getProductByCode(String code) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                "upc" + "," +
                "product" +
                " FROM " + "cache"
                + " WHERE " +
                "upc" + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(code)});
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
