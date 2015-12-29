/*
 * Copyright (C) 2015 Federico Paolinelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/**********************************************************************************************************************************************************************
****** AUTO GENERATED FILE BY ANDROID SQLITE HELPER SCRIPT BY FEDERICO PAOLINELLI. ANY CHANGE WILL BE WIPED OUT IF THE SCRIPT IS PROCESSED AGAIN. *******
**********************************************************************************************************************************************************************/
package com.whiterabbit.rxrestsample.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class RepoDbHelper {
    private static final String TAG = "DbHelper";

    private static final String DATABASE_NAME = "DbHelper.db";
    private static final int DATABASE_VERSION = 1;


    // Variable to hold the database instance
    protected SQLiteDatabase mDb;
    // Context of the application using the database.
    private final Context mContext;
    // Database open/upgrade helper
    private DbHelper mDbHelper;
    
    public RepoDbHelper(Context context) {
        mContext = context;
        mDbHelper = new DbHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public RepoDbHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
                                                     
    public void close() {
        mDb.close();
    }

    public static final String ROW_ID = "_id";

    
    // -------------- REPO DEFINITIONS ------------
    public static final String REPO_TABLE = "Repo";
    
    public static final String REPO_ID_COLUMN = "Id";
    public static final int REPO_ID_COLUMN_POSITION = 1;
    
    public static final String REPO_NAME_COLUMN = "Name";
    public static final int REPO_NAME_COLUMN_POSITION = 2;
    
    public static final String REPO_FULLNAME_COLUMN = "FullName";
    public static final int REPO_FULLNAME_COLUMN_POSITION = 3;
    
    public static final String REPO_OWNER_COLUMN = "Owner";
    public static final int REPO_OWNER_COLUMN_POSITION = 4;
    
    


    // -------- TABLES CREATION ----------

    
    // Repo CREATION 
    private static final String DATABASE_REPO_CREATE = "create table " + REPO_TABLE + " (" +
                                "_id integer primary key autoincrement, " +
                                REPO_ID_COLUMN + " text, " +
                                REPO_NAME_COLUMN + " text, " +
                                REPO_FULLNAME_COLUMN + " text, " +
                                REPO_OWNER_COLUMN + " text" +
                                ")";
    

    
    // ----------------Repo HELPERS -------------------- 
    public long addRepo (String Id, String Name, String FullName, String Owner) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(REPO_ID_COLUMN, Id);
        contentValues.put(REPO_NAME_COLUMN, Name);
        contentValues.put(REPO_FULLNAME_COLUMN, FullName);
        contentValues.put(REPO_OWNER_COLUMN, Owner);
        return mDb.insert(REPO_TABLE, null, contentValues);
    }

    public long updateRepo (long rowIndex,String Id, String Name, String FullName, String Owner) {
        String where = ROW_ID + " = " + rowIndex;
        ContentValues contentValues = new ContentValues();
        contentValues.put(REPO_ID_COLUMN, Id);
        contentValues.put(REPO_NAME_COLUMN, Name);
        contentValues.put(REPO_FULLNAME_COLUMN, FullName);
        contentValues.put(REPO_OWNER_COLUMN, Owner);
        return mDb.update(REPO_TABLE, contentValues, where, null);
    }

    public boolean removeRepo(long rowIndex){
        return mDb.delete(REPO_TABLE, ROW_ID + " = " + rowIndex, null) > 0;
    }

    public boolean removeAllRepo(){
        return mDb.delete(REPO_TABLE, null, null) > 0;
    }

    public Cursor getAllRepo(){
    	return mDb.query(REPO_TABLE, new String[] {
                         ROW_ID,
                         REPO_ID_COLUMN,
                         REPO_NAME_COLUMN,
                         REPO_FULLNAME_COLUMN,
                         REPO_OWNER_COLUMN
                         }, null, null, null, null, null);
    }

    public Cursor getRepo(long rowIndex) {
        Cursor res = mDb.query(REPO_TABLE, new String[] {
                                ROW_ID,
                                REPO_ID_COLUMN,
                                REPO_NAME_COLUMN,
                                REPO_FULLNAME_COLUMN,
                                REPO_OWNER_COLUMN
                                }, ROW_ID + " = " + rowIndex, null, null, null, null);

        if(res != null){
            res.moveToFirst();
        }
        return res;
    }
    

    private static class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // Called when no database exists in disk and the helper class needs
        // to create a new one. 
        @Override
        public void onCreate(SQLiteDatabase db) {      
            db.execSQL(DATABASE_REPO_CREATE);
            
        }

        // Called when there is a database version mismatch meaning that the version
        // of the database on disk needs to be upgraded to the current version.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Log the version upgrade.
            Log.w(TAG, "Upgrading from version " + 
                        oldVersion + " to " +
                        newVersion + ", which will destroy all old data");
            
            // Upgrade the existing database to conform to the new version. Multiple 
            // previous versions can be handled by comparing _oldVersion and _newVersion
            // values.

            // The simplest case is to drop the old table and create a new one.
            db.execSQL("DROP TABLE IF EXISTS " + REPO_TABLE + ";");
            
            // Create a new one.
            onCreate(db);
        }
    }
}

