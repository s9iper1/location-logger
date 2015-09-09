package com.byteshaft.locationlogger.utils;

public class DatabaseConstants {

    public static final String DATABASE_NAME = "LocationDatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "LocationDatabase";
    public static final String SSID_COLUMN = "SSID";
    public static final String LONGITUDE_COLUMN = "LONGITUDE";
    public static final String LATITUDE_COLUMN = "LATITUDE";
    public static final String TIME_STAMP_COLUMN = "TIME";
    public static final String USER_ID_COLUMN = "USER_ID";
    public static final String ID_COLUMN = "ID";

    private static final String OPENING_BRACE = "(";
    private static final String CLOSING_BRACE = ")";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + OPENING_BRACE
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SSID_COLUMN+ " TEXT UNIQUE,"
            + LONGITUDE_COLUMN + " TEXT,"
            + LATITUDE_COLUMN + " TEXT,"
            + TIME_STAMP_COLUMN + " TEXT,"
            + USER_ID_COLUMN + " TEXT"
            + CLOSING_BRACE;
}
