package com.example.lostandfound.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.lostandfound.model.LostFoundItem;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "lost_found.db";
    private static final int    DB_VERSION = 1;

    public static final String TABLE_NAME    = "items";
    public static final String COL_ID        = "id";
    public static final String COL_POST_TYPE = "post_type";
    public static final String COL_NAME      = "name";
    public static final String COL_PHONE     = "phone";
    public static final String COL_DESC      = "description";
    public static final String COL_DATE      = "date";
    public static final String COL_LOCATION  = "location";
    public static final String COL_CATEGORY  = "category";
    public static final String COL_IMAGE_URI = "image_uri";
    public static final String COL_TIMESTAMP = "timestamp";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_POST_TYPE + " TEXT NOT NULL, " +
                    COL_NAME      + " TEXT NOT NULL, " +
                    COL_PHONE     + " TEXT NOT NULL, " +
                    COL_DESC      + " TEXT NOT NULL, " +
                    COL_DATE      + " TEXT NOT NULL, " +
                    COL_LOCATION  + " TEXT NOT NULL, " +
                    COL_CATEGORY  + " TEXT NOT NULL, " +
                    COL_IMAGE_URI + " TEXT, " +
                    COL_TIMESTAMP + " TEXT NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_TABLE); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertItem(LostFoundItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_POST_TYPE, item.getPostType());
        cv.put(COL_NAME,      item.getName());
        cv.put(COL_PHONE,     item.getPhone());
        cv.put(COL_DESC,      item.getDescription());
        cv.put(COL_DATE,      item.getDate());
        cv.put(COL_LOCATION,  item.getLocation());
        cv.put(COL_CATEGORY,  item.getCategory());
        cv.put(COL_IMAGE_URI, item.getImageUri());
        cv.put(COL_TIMESTAMP, item.getTimestamp());
        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result;
    }

    public List<LostFoundItem> getAllItems() {
        List<LostFoundItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COL_ID + " DESC");
        if (cursor.moveToFirst()) {
            do { list.add(cursorToItem(cursor)); } while (cursor.moveToNext());
        }
        cursor.close(); db.close();
        return list;
    }

    public List<LostFoundItem> getItemsByCategory(String category) {
        List<LostFoundItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_CATEGORY + "=?",
                new String[]{category}, null, null, COL_ID + " DESC");
        if (cursor.moveToFirst()) {
            do { list.add(cursorToItem(cursor)); } while (cursor.moveToNext());
        }
        cursor.close(); db.close();
        return list;
    }

    public LostFoundItem getItemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        LostFoundItem item = null;
        if (cursor.moveToFirst()) { item = cursorToItem(cursor); }
        cursor.close(); db.close();
        return item;
    }

    public boolean deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    private LostFoundItem cursorToItem(Cursor c) {
        LostFoundItem item = new LostFoundItem();
        item.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
        item.setPostType(c.getString(c.getColumnIndexOrThrow(COL_POST_TYPE)));
        item.setName(c.getString(c.getColumnIndexOrThrow(COL_NAME)));
        item.setPhone(c.getString(c.getColumnIndexOrThrow(COL_PHONE)));
        item.setDescription(c.getString(c.getColumnIndexOrThrow(COL_DESC)));
        item.setDate(c.getString(c.getColumnIndexOrThrow(COL_DATE)));
        item.setLocation(c.getString(c.getColumnIndexOrThrow(COL_LOCATION)));
        item.setCategory(c.getString(c.getColumnIndexOrThrow(COL_CATEGORY)));
        item.setImageUri(c.getString(c.getColumnIndexOrThrow(COL_IMAGE_URI)));
        item.setTimestamp(c.getString(c.getColumnIndexOrThrow(COL_TIMESTAMP)));
        return item;
    }
}