package com.example.bookmanager;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class MyContentProvider extends ContentProvider {
    static final String AUTHORITY = "com.example.bookmanager";
    static final String CONTENT_PATH ="bookdata";
    static final String URL = "content://"+AUTHORITY + "/" + CONTENT_PATH;
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final String TABLE_NAME = "Books";
    private SQLiteDatabase db;

    private static HashMap<String, String> BOOKS_PROJECTION_MAP;

    static final int ALLITEMS = 1;
    static final int ONEITEM = 2;
    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,CONTENT_PATH,ALLITEMS);
        uriMatcher.addURI(AUTHORITY,CONTENT_PATH+ "/#", ONEITEM);
    }

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Context context = getContext();
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        if(db==null)
            return false;
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)){
            case ALLITEMS:
                return "vnd.android.cursor.dir/vnd.example.bookdata";
            case ONEITEM:
                return "vnd.android.cursor.item/vnd.example.bookdata";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long number_row = db.insert(TABLE_NAME,"",values);
        if(number_row>0){
            Uri uri1 = ContentUris.withAppendedId(CONTENT_URI,number_row);
            getContext().getContentResolver().notifyChange(uri1,null);
            return uri1;
        }
        throw new SQLException("Failed to add a record into "+uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(TABLE_NAME);
        switch (uriMatcher.match(uri)){
            case ALLITEMS:
                sqLiteQueryBuilder.setProjectionMap(BOOKS_PROJECTION_MAP);
                break;
            case ONEITEM:
                sqLiteQueryBuilder.appendWhere("id_book" + "=" + uri.getPathSegments().get(1));
                break;
        }
        if(sortOrder == null || sortOrder =="")
            sortOrder = "title";

        Cursor cursor = sqLiteQueryBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int count = 0;
        switch (uriMatcher.match(uri)){
            case ALLITEMS:
                count = db.update(TABLE_NAME,values,selection,selectionArgs);
                break;
            case ONEITEM:
                count = db.update(TABLE_NAME,values,"id_book" + "=" + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? "AND(" +selection + ')' : ""),selectionArgs);
                        break;
                        default: throw new IllegalArgumentException("Unknowm URI" +uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int count=0;
        switch (uriMatcher.match(uri)){
            case ALLITEMS:
                count = db.delete(TABLE_NAME, selection,selectionArgs);
                break;
            case ONEITEM:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TABLE_NAME,"id_book" + "=" + id + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""),selectionArgs);
                break;
            default: throw new IllegalArgumentException("Unknowm URI" +uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

}
