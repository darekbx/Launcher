package com.mlauncher.dotpad;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

public class DotManager {

    public static int countDots() {
        List<Dot> dots = dots();
        int count = 0;
        for (Dot dot : dots) {
            if (dot.isSticked) {
                continue;
            }
            count++;
        }
        return count;
    }

	public static List<Dot> dots() {
		SQLiteDatabase db = openDataBase();
    	Cursor cursor = db.query("dots", null,
				"isArchival = ?", new String[] { "0" },
				null, null, null, null);
        List<Dot> items = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
            	Dot dot = getFromCursor(cursor);
        		items.add(dot);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
		db.close();
        return items;
	}

	public static SQLiteDatabase openDataBase() {
		StringBuilder builder = new StringBuilder();
		builder.append(Environment.getExternalStorageDirectory());
		builder.append("/dotpad.sqlite");
		return SQLiteDatabase.openDatabase(builder.toString(), null,
				SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}

    private static Dot getFromCursor(Cursor cursor) {
        final Dot item = new Dot();
        item.id = cursor.getInt(0);
        item.color = cursor.getInt(2);
        item.size = cursor.getInt(3);
        item.position = new Point(cursor.getInt(4), cursor.getInt(5));
        item.isSticked = cursor.getInt(11) == 1;
        return item;
    }
}