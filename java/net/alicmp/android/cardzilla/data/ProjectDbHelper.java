package net.alicmp.android.cardzilla.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ali on 9/1/15.
 */
public class ProjectDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;

    static final String DATABASE_NAME = "weather.db";


    public ProjectDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PACKET_TABLE =
                "CREATE TABLE " + ProjectContract.PacketEntry.TABLE_NAME + " (" +
                        ProjectContract.PacketEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ProjectContract.PacketEntry.COLUMN_PACKET_TITLE + " TEXT NOT NULL, " +
                        ProjectContract.PacketEntry.COLUMN_NEXT_READ_TIME + " INTEGER, " +
                        ProjectContract.PacketEntry.COLUMN_LAST_READ_TIME + " INTEGER " +
                        " );";

        final String SQL_CREATE_CARD_TABLE =
                "CREATE TABLE " + ProjectContract.CardEntry.TABLE_NAME + " (" +
                        ProjectContract.CardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ProjectContract.CardEntry.COLUMN_PACKET_ID + " INTEGER NOT NULL, " +
                        ProjectContract.CardEntry.COLUMN_FRONT_TEXT + " TEXT NOT NULL, " +
                        ProjectContract.CardEntry.COLUMN_BACK_TEXT + " TEXT NOT NULL, " +
                        ProjectContract.CardEntry.COLUMN_FRONT_IMAGE_PATH + " TEXT, " +
                        ProjectContract.CardEntry.COLUMN_BACK_IMAGE_PATH + " TEXT, " +
                        ProjectContract.CardEntry.COLUMN_CARD_LEVEL + " INTEGER NOT NULL, " +
                        ProjectContract.CardEntry.COLUMN_READ_TIME + " INTEGER, " +
                        " FOREIGN KEY (" + ProjectContract.CardEntry.COLUMN_PACKET_ID + ") REFERENCES " +
                        ProjectContract.PacketEntry.TABLE_NAME + " (" + ProjectContract.PacketEntry._ID + ") " +
                        " );";

        db.execSQL(SQL_CREATE_PACKET_TABLE);
        db.execSQL(SQL_CREATE_CARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProjectContract.PacketEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProjectContract.CardEntry.TABLE_NAME);
        onCreate(db);
    }
}
