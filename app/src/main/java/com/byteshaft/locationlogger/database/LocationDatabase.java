package com.byteshaft.locationlogger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.byteshaft.locationlogger.utils.DatabaseConstants;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationDatabase extends SQLiteOpenHelper {

    public LocationDatabase(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseConstants.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + DatabaseConstants.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void createNewEntry(String longitude, String latitude, String timestamp, String userId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.LONGITUDE_COLUMN, longitude);
        values.put(DatabaseConstants.LATITUDE_COLUMN, latitude);
        values.put(DatabaseConstants.TIME_STAMP_COLUMN, timestamp);
        values.put(DatabaseConstants.USER_ID_COLUMN, userId);
        db.insert(DatabaseConstants.TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<HashMap> getCoordinatesForID(String ID) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "
                + DatabaseConstants.TABLE_NAME
                + " WHERE "
                + DatabaseConstants.ID_COLUMN
                + "="
                + ID;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String longitude = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.LONGITUDE_COLUMN));
            String latitude = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.LATITUDE_COLUMN));
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("longitude", longitude);
            hashMap.put("latitude", latitude);
            list.add(hashMap);
        }
        cursor.close();
        return list;
    }
}
