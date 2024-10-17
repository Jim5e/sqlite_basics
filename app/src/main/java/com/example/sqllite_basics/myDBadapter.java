package com.example.sqllite_basics;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
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

        Log.d("InsertData", "Inserted ID: " + id); // Log the inserted ID
        return id;
    }


    public String getData() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDBHelper.UID, myDBHelper.name, myDBHelper.password};

        Cursor cursor = db.query(myDBHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int cid = cursor.getInt(cursor.getColumnIndex(myDBHelper.UID));
            @SuppressLint("Range") String user = cursor.getString(cursor.getColumnIndex(myDBHelper.name));
            @SuppressLint("Range") String pass = cursor.getString(cursor.getColumnIndex(myDBHelper.password));

            buffer.append(cid).append(" ").append(user).append(" ").append(pass).append("\n"); // Proper formatting
        }
        return buffer.toString();
    }

    public long deleteData(String userId) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String whereClause = myDBHelper.UID + " = ?";
        String[] whereArgs = {userId};
        return db.delete(myDBHelper.TABLE_NAME, whereClause, whereArgs);
    }

    public user_model locateUser(String userId) {
        SQLiteDatabase db = myhelper.getReadableDatabase(); // Use readable database for queries
        user_model returned_user = null;

        // Define the columns to retrieve
        String[] columns = {myDBHelper.UID, myDBHelper.name, myDBHelper.password};

        // Define the selection criteria
        String selection = myDBHelper.UID + " = ?";
        String[] selectionArgs = {userId};

        // Use try-with-resources to ensure cursor is closed
        try (Cursor cursor = db.query(myDBHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String user = cursor.getString(cursor.getColumnIndex(myDBHelper.name));
                @SuppressLint("Range") String pass = cursor.getString(cursor.getColumnIndex(myDBHelper.password));

                returned_user = new user_model(userId, user, pass);
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error locating user", e);
        } finally {
            db.close();
        }

        return returned_user;
    }

    public long updateData(String userId, String updatedName, String updatedPassword) {
        SQLiteDatabase db = myhelper.getWritableDatabase(); // Use writable database for updates

        // Create a ContentValues object to hold the updated values
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDBHelper.name, updatedName);
        contentValues.put(myDBHelper.password, updatedPassword);

        // Define the selection criteria for the update
        String selection = myDBHelper.UID + " = ?";
        String[] selectionArgs = {userId};
        long result = -1;

        try {
            // Perform the update operation
            result = db.update(myDBHelper.TABLE_NAME, contentValues, selection, selectionArgs);
        } catch (Exception e) {
            // Handle any exceptions (e.g., log them)
            Log.e("DatabaseError", "Error updating user data", e);
        }

        return result;
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
