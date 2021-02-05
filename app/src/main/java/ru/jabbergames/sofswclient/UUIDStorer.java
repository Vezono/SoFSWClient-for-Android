package ru.jabbergames.sofswclient;

import android.content.ContentValues;
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
        // Cursor docs = db.rawQuery("SELECT uuid FROM uuids", null);
        Cursor docs = db.query("uuids", new String[] {"uuid"}, "uuid", null,
                null, null, null);
        if (!docs.moveToNext()){
            genUUID(db);
            db.close();
            return getUUID(context);
        }
        else {
            UUID = docs.getString(0);
        }
        db.close();
        return UUID;
    }

    public static void genUUID(SQLiteDatabase db) {
        String newUUID = UUID.randomUUID().toString();
        // db.rawQuery("INSERT INTO uuids VALUES(\"" + newUUID +"\")", null);
        ContentValues cv = new ContentValues();
        cv.put("uuid", newUUID);
        db.insert("uuids", "uuid", cv);
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