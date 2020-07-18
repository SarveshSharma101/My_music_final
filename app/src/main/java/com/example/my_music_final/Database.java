package com.example.my_music_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    private static final String database_name = "fav.db";
    private static final String table = "Favourite_Songs";
    private static final String songID = "songid";
    public Database(@Nullable Context context) {
        super(context, database_name, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table '"+table+"'(songid long primary key)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+table);
        onCreate(db);
    }

    public boolean insertsong(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(songID,id);
        long r = db.insert(table,null,cv);
        if(r ==-1){
            return false;
        }
        else{ return true; }
    }

    public Cursor get_favSongs(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c= db.rawQuery("select * from "+table,null);
        return c;
    }

    public int deletefavsong(long id){
        SQLiteDatabase db= this.getWritableDatabase();

        return db.delete(table,"songid=?",new String[]{String.valueOf(id)});
    }
}
