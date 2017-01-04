package com.mlauncher.logic.sojp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by daba on 2016-09-14.
 */
public class ContentDownloader {

    private static final String ADDRESS = "http://sojp.wios.warszawa.pl/wyniki/EV_ZP-WaKo.txt";

    public interface Listener {
        void onContent(String content);
    }

    public static void downloadContent(final Listener listener) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    InputStream inputStream = new URL(ADDRESS).openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line + "\n");
                    }

                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                    return builder.toString();
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    listener.onContent(result);
                }
            }
        }.execute();
    }
}