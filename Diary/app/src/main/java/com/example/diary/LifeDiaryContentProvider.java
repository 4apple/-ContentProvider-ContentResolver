package com.example.diary;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * @author wanlijun
 * @description  提供外部访问数据库的接口，外部访问数据库用getContentResolver()
 * @time 2018/1/22 15:15
 */

public class LifeDiaryContentProvider extends ContentProvider {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "lijun";
    private static final String TABLE_NAME = "diary";
    private static final int DIARIES = 1; //多条记录
    private static final int DIARY_ID = 2; //单条记录

    private static UriMatcher uriMatcher;
    private DatabaseHelper mDatabaseHelper;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Fields.AUTHORITY,"diaries",DIARIES);
        uriMatcher.addURI(Fields.AUTHORITY,"diaries/#",DIARY_ID);
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case DIARIES:
                return Fields.DiaryColumns.CONTENT_TYPE;
            case DIARY_ID:
                return Fields.DiaryColumns.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalArgumentException("Unknown URI "+ uri);
        }
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if(uriMatcher.match(uri) != DIARIES){
            throw new IllegalArgumentException("Unknown URI "+uri);
        }
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues values;
        if(contentValues != null){
            values = new ContentValues(contentValues);
        }else{
            values = new ContentValues();
        }
        if(!values.containsKey(Fields.DiaryColumns.TIME)){
            values.put(Fields.DiaryColumns.TIME,getFormateTime());
        }
        if (!values.containsKey(Fields.DiaryColumns.TITLE)){
            values.put(Fields.DiaryColumns.TITLE, Resources.getSystem().getString(R.string.app_name));
        }
        if(!values.containsKey(Fields.DiaryColumns.CONTENT)){
            values.put(Fields.DiaryColumns.CONTENT,"");
        }
        long rowId = db.insert(TABLE_NAME, Fields.DiaryColumns.CONTENT,values);
        if(rowId > 0){
            Uri diaryUri = ContentUris.withAppendedId(Fields.DiaryColumns.CONTENT_URI,rowId);
            return diaryUri;
        }
        throw new SQLException("Failed to insert row into "+uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        if(uriMatcher.match(uri) != DIARY_ID){
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        String rowId = uri.getPathSegments().get(1);
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.delete(TABLE_NAME, Fields.DiaryColumns._ID + "=" + rowId,null);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if(uriMatcher.match(uri) != DIARY_ID){
            throw new IllegalArgumentException("Unknown URI "+uri);
        }
        ContentValues values;
        if(contentValues != null){
            values = new ContentValues(contentValues);
        }else{
            values = new ContentValues();
        }
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String rowId = uri.getPathSegments().get(1);
        return db.update(TABLE_NAME,values, Fields.DiaryColumns._ID + "=" + rowId,null);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)){
            case DIARIES:
                queryBuilder.setTables(TABLE_NAME);
                break;
            case DIARY_ID:
                queryBuilder.setTables(TABLE_NAME);
                queryBuilder.appendWhere(Fields.DiaryColumns._ID + "=" + uri.getPathSegments().get(1));
                break;
                default:
                    throw new IllegalArgumentException("Unknown URI "+uri);
        }
        String orderBy;
        if(TextUtils.isEmpty(s1)){
            orderBy = "time desc";
        }else{
            orderBy = s1;
        }
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = queryBuilder.query(db,strings,s,strings1,null,null,orderBy);
        return c;
    }
    private String getFormateTime(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        String time = calendar.get(Calendar.YEAR)+"年"
                +month+"月"
                +calendar.get(Calendar.DAY_OF_MONTH)+"日"
                +calendar.get(Calendar.HOUR_OF_DAY)+"时"
                +calendar.get(Calendar.MINUTE)+"分";
        return  time;
    }

    public class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(Context context) {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql = "create table "+ TABLE_NAME
                    +"(" + Fields.DiaryColumns._ID + " integer primary key,"
                    + Fields.DiaryColumns.TITLE + " text,"
                    + Fields.DiaryColumns.CONTENT + " text,"
                    + Fields.DiaryColumns.TIME + " text"
                    +")";
            sqLiteDatabase.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
