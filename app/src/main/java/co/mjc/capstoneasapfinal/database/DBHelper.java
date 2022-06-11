package co.mjc.capstoneasapfinal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

// 대기
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ASAPDatabase.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE schedule (lecName TEXT, dayOTW TEXT);");
        db.execSQL("CREATE TABLE filePaths (dayOTW TEXT, path TEXT, " +
                "FOREIGN KEY(dayOTW) REFERENCES schedule(dayOTW));");
        db.execSQL("CREATE TABLE pdfData(dayOTW TEXT, pdfName TEXT, pdfUri TEXT, " +
                "FOREIGN KEY(dayOTW) REFERENCES schedule(dayOTW));");
        db.execSQL("CREATE TABLE noteData(dayOTW TEXT, noteName TEXT, noteImage TEXT, " +
                "FOREIGN KEY(dayOTW) REFERENCES schedule(dayOTW))");


    }

    // 굳이 필요없음
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}