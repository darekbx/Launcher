package com.mlauncher.logic.sojp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by daba on 2016-09-14.
 */
public class SOPJController {

    public interface Listener {
        void onData(List<Entry> entries);
    }

    public static void loadAirQuality(final Listener listener, String... parameters) {
        ContentDownloader.downloadContent(new ContentDownloader.Listener() {
            @Override
            public void onContent(String content) {
                List<Entry> entries = EntryParser.parseContent(content);
                List<Entry> filtered = entriesByParameters(entries, "PM10", "PM2.5");
                listener.onData(filtered);
            }
        });
    }

    public static List<Entry> entriesByParameters(List<Entry> entries, String... parameters) {
        List<Entry> filteredEntries = new ArrayList<>();
        List<String> filters = Arrays.asList(parameters);

        for (Entry entry : entries) {
            if (filters.contains(entry.parameter)) {
                if (checkForDuplicates(filteredEntries, parameters)) {
                    filteredEntries.clear();
                }
                filteredEntries.add(entry);
            }
        }

        return filteredEntries;
    }

    public static boolean checkForDuplicates(List<Entry> filteredEntries, String... parameters) {
        return filteredEntries.size() == parameters.length;
    }
}