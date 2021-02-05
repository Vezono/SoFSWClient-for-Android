package ru.jabbergames.sofswclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;

public class UUIDStorer {
    public static String getUUID(Context context) {
        String UUID;
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor docs = db.rawQuery("SELECT uuid FROM uuids", null);
        if (!docs.moveToNext()){
            UUID = genUUID(db);
        }
        else {
            UUID = docs.getString(0);
        }
        db.close();
        return UUID;
    }

    public static String genUUID(SQLiteDatabase db) {
        String newUUID = UUID.randomUUID().toString();
        db.rawQuery("INSERT INTO uuids VALUES(" + newUUID +")", null);
        return newUUID;
    }
}

class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "uuids", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS uuids(uuid varchar(255));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }
}