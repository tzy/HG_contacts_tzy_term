package com.contacts.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;


public class SqliteRawQueryLoader extends AsyncTaskLoader<Cursor> {
	final ForceLoadContentObserver mObserver;

    SQLiteDatabase mSQLiteDatabase;
    
    String mDatabaseName;
    String mSql;
    String[] mSqlArgs;

    Cursor mCursor;

    /* Runs on a worker thread */
    @Override
    public Cursor loadInBackground() {
    	Cursor cursor = mSQLiteDatabase.rawQuery(mSql, mSqlArgs);
        if (cursor != null) {
            // Ensure the cursor window is filled
            cursor.getCount();
            cursor.registerContentObserver(mObserver);
        }
        return cursor;
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    
    public SqliteRawQueryLoader(Context context, SQLiteDatabase db, String sql, String[] sqlArgs) {
        super(context);
        mObserver = new ForceLoadContentObserver();
        
        mSQLiteDatabase = db;
        mSql = sql;
        mSqlArgs = sqlArgs;
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        
        // Ensure the loader is stopped
        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }

}

