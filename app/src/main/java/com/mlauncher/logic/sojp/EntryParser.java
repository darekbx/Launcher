package com.mlauncher.logic.sojp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daba on 2016-09-14.
 */
public class EntryParser {

    public static List<Entry> parseContent(String content) {
        String[] lines = content.split("\n");
        int count = lines.length;
        List<Entry> entries = new ArrayList<>(count);
        for (int i = 0; i < count - 1; i++) {
            String[] chunks = lines[i].split("\\|");
            Entry entry = new Entry();
            entry.date = chunks[0];
            entry.station = chunks[1];
            entry.parameter = chunks[2];
            entry.unit = chunks[3];
            entry.value = toDouble(chunks[4]);
            entries.add(entry);
        }
        return entries;
    }

    public static double toDouble(String value) {
        if (value.indexOf(',') != -1) {
            value = value.replace(',', '.');
        }
        return Double.parseDouble(value);
    }
}
