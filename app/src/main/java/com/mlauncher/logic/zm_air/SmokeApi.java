package com.mlauncher.logic.zm_air;

import android.content.Context;
import android.os.AsyncTask;

import com.mlauncher.logic.utils.NetworkUtils;
import com.mlauncher.model.SmokeItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by INFOR PL on 2015-08-19.
 */
public class SmokeApi extends AsyncTask<Void, Void, SmokeItem[]> {

    public interface Listener {
        void onItems(SmokeItem pm10, SmokeItem pm2_5);
    }

    private static final String ZM_URL = "http://www.zm.org.pl/powietrze/json/";

    private Context context;
    private SmokeItem[] items;
    private Listener listener;

    public SmokeApi(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected SmokeItem[] doInBackground(Void... voids) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(new URL(ZM_URL).openStream()));
                StringBuilder builder = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    builder.append(line);
                }

                try {
                    in.close();
                } catch (IOException e) {
                }

                String json = removeFunction(builder.toString());
                return new SmokeItemParser().parseJson(json);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return items;
        }
    }

    public String removeFunction(String input) {
        return input.substring(15, input.length() - 1);
    }

    @Override
    protected void onPostExecute(SmokeItem[] smokeItems) {
        if (smokeItems != null) {
            items = smokeItems;
            listener.onItems(smokeItems[0], smokeItems[1]);
        }
    }
}