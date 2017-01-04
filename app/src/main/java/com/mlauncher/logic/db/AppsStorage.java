package com.mlauncher.logic.db;

import java.util.ArrayList;
import java.util.List;

import com.mlauncher.model.SortableResolveInfo;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by INFOR PL on 2015-01-27.
 */
public class AppsStorage {

    private SQLiteDatabase db;
    private Context context;

    public AppsStorage(Context context) {

        this.context = context;
        db = new StorageHelper(context).getWritableDatabase();
    }

    public void addItems(List<ResolveInfo> items) {
        List<String> currentItems = getItemsPackageNames();
        for (ResolveInfo item : items) {

            currentItems.remove(item.activityInfo.applicationInfo.packageName);

            SQLiteStatement stmt = db.compileStatement("INSERT OR IGNORE INTO apps VALUES (null, ?, ?, ?, ?)");
            stmt.bindString(1, item.activityInfo.applicationInfo.packageName);
            stmt.bindString(2, item.activityInfo.name);
            stmt.bindString(3, item.loadLabel(context.getPackageManager()).toString());
            stmt.bindLong(4, 0);

            stmt.executeInsert();
        }

        for (String packageName : currentItems) {
            db.delete("apps", "package_name = ?", new String[] { packageName});
        }
    }

    public List<SortableResolveInfo> getItems() {
        Cursor cursor = db.query("apps",
                null, null, null, null, null, "clicks DESC, label ASC");
        List<SortableResolveInfo> items = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                items.add(getFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return items;
    }

    public List<String> getItemsPackageNames() {
        Cursor cursor = db.query("apps",
                new String[] { "package_name" },
                null, null, null, null, "clicks DESC, label ASC");
        List<String> items = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                items.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return items;
    }

    public void increase(String packageName) {
        db.execSQL("UPDATE apps SET clicks = clicks + 1 WHERE package_name = ?", new Object[] { packageName });
    }

    public void close() {
        db.close();
    }

    private SortableResolveInfo getFromCursor(Cursor cursor) {
        SortableResolveInfo item = new SortableResolveInfo();
        item.packageName = cursor.getString(1);
        item.name = cursor.getString(2);
        item.label = cursor.getString(3);
        item.clicks = cursor.getInt(4);
        return item;
    }
}