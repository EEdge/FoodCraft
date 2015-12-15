package sfsu.csc413.foodcraft;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This programs acts as an abstraction layer between the database and our UPCDatabase class.
 * It creates the DBHelper object, which enables the UPCDatabase class to manipulate the database.
 * @file:DBHelper.java
 * @author: Paul Klein
 * @version: 1.0
 */
public class DBHelper  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "UPC.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_CACHE = "CREATE TABLE " + "cache"  + "("
                + "upc"  + " INTEGER PRIMARY KEY,"
                + "product" + " TEXT )";

        db.execSQL(CREATE_TABLE_CACHE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "cache");
        onCreate(db);

    }

}