package com.example.beerlist.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
* Class for creating the database
*/

public class FavDB extends SQLiteOpenHelper{
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "BeerDB";

    private static String TABLE_NAME = "favoriteTable";
    public static String KEY_ID = "id";
    public static String ITEM_NAME = "itemName";
    public static String ITEM_TAGLINE = "itemTagline";
    public static String ITEM_DESCRIPTION = "itemDescription";
    public static String ITEM_IMAGEURL = "itemImageURL";
    public static String FAVORITE_STATUS = "fStatus";

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " TEXT," + ITEM_NAME+ " TEXT,"
            + ITEM_TAGLINE + " TEXT," + ITEM_DESCRIPTION+ " TEXT,"
            + ITEM_IMAGEURL + " TEXT," + FAVORITE_STATUS+" TEXT)";

    public FavDB(Context context) {
        super(context,DATABASE_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //Create empty table
    public void insertEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (int x = 1; x < 25; x++) {
            cv.put(KEY_ID, x);
            cv.put(FAVORITE_STATUS, "0");

            db.insert(TABLE_NAME,null, cv);
        }
    }

    //Insert data into database
    public void insertIntoTheDatabase(String item_name, String item_tagline,String item_description, String item_imageurl, String id, String fav_status) {

        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_NAME, item_name);
        cv.put(ITEM_TAGLINE, item_tagline);
        cv.put(ITEM_DESCRIPTION, item_description);
        cv.put(ITEM_IMAGEURL, item_imageurl);
        cv.put(KEY_ID, id);
        cv.put(FAVORITE_STATUS, fav_status);
        db.insert(TABLE_NAME,null, cv);
        Log.d("FavDB Status", ITEM_NAME + ", favstatus - "+fav_status+" - . " + cv);
    }

    //Read all data
    public Cursor read_all_data(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME + " where " + KEY_ID+"="+id+"";
        return db.rawQuery(sql,null,null);
    }

    //Remove line from database
    public void remove_fav(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET  "+ FAVORITE_STATUS+" ='0' WHERE "+KEY_ID+"="+id+"";
        db.execSQL(sql);
        Log.d("Remove", id.toString());

    }

    //Select all favorite list
    public Cursor select_all_favorite_list() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+FAVORITE_STATUS+" ='1'";
        return db.rawQuery(sql,null,null);
    }
}
