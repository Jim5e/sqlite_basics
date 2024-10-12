package com.example.sqllite_basics;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class myDBadapter {
    myDBHelper myhelper;

    public myDBadapter(Context context) {
        myhelper = new myDBHelper(context);
    }

    public long insertData(String user, String pass) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(myDBHelper.name, user);
        contentValues.put(myDBHelper.password, pass);

        long id = db.insert(myDBHelper.TABLE_NAME, null, contentValues);

        return id;
    }

    public String getData(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDBHelper.UID, myDBHelper.name, myDBHelper.password};

        Cursor cursor = db.query (myDBHelper.TABLE_NAME, columns, null, null, null, null, null);

        StringBuffer buffer = new StringBuffer();

        while(cursor.moveToNext()){
            @SuppressLint("Range") int cid = cursor.getInt(cursor.getColumnIndex(myDBHelper.UID));
            @SuppressLint("Range") String user = cursor.getString(cursor.getColumnIndex(myDBHelper.name));
            @SuppressLint("Range") String pass = cursor.getString(cursor.getColumnIndex(myDBHelper.password));

            buffer.append(cid + "" + user + "" + pass + "\n");

        }
        return buffer.toString();
    }

    static class myDBHelper extends SQLiteOpenHelper {
        //db, table, version
        private static final String DATABASE_NAME = "mydb";
        private static final String TABLE_NAME = "users";
        private static final int DB_ver = 1;

        //Columns
        private static final String UID = "_id";
        private static final String name = "name";
        private static final String password = "password";

        //
        private static final String CREATE_TABLE =
                "CREATE TABLE "+ TABLE_NAME + " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, " + name + " VARCHAR(255), "
                + password + " VARCHAR(255));";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public myDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DB_ver);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try{
                sqLiteDatabase.execSQL(CREATE_TABLE);
            }catch(Exception e){
                Toast.makeText(context.getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL(DROP_TABLE);
                onCreate(sqLiteDatabase);
            }catch(Exception e){
                Toast.makeText(context.getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
            }
        }


    }


}
