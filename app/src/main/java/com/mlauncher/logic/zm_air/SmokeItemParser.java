package com.mlauncher.logic.zm_air;

import com.mlauncher.logic.SmokeState;
import com.mlauncher.model.SmokeItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by daba on 2016-09-30.
 */
public class SmokeItemParser {

    public static final String[] PROBE_NAMES = new String[]{"sonda1", "sonda2", "sonda3", "sonda4"};

    public SmokeItem[] parseJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONObject results = root.getJSONObject("results");
        JSONObject pm10 = results.getJSONObject("pm_10");
        JSONObject pm2_5 = results.getJSONObject("pm_25");
        String time = root.getString("time");

        if (checkProbe(pm10, PROBE_NAMES[1])) {
            return extractProbe(pm10, pm2_5, time, PROBE_NAMES[1]);
        } else if (checkProbe(pm10, PROBE_NAMES[2])) {
            return extractProbe(pm10, pm2_5, time, PROBE_NAMES[2]);
        } else if (checkProbe(pm10, PROBE_NAMES[0])) {
            return extractProbe(pm10, pm2_5, time, PROBE_NAMES[0]);
        } else if (checkProbe(pm10, PROBE_NAMES[3])) {
            return extractProbe(pm10, pm2_5, time, PROBE_NAMES[3]);
        }

        return null;
    }

    public boolean checkProbe(JSONObject pm10, String probeName) throws JSONException {
        return pm10.getJSONObject(probeName).optString("current_value", null) != null;
    }

    public SmokeItem[] extractProbe(JSONObject pm10, JSONObject pm2_5, String time, String probeName) throws JSONException {
        return new SmokeItem[]{
                parseObjectToItem(SmokeItem.Type.PM10, pm10.getJSONObject(probeName), time),
                parseObjectToItem(SmokeItem.Type.PM2_5, pm2_5.getJSONObject(probeName), time),
        };
    }

    public SmokeItem parseObjectToItem(SmokeItem.Type type, JSONObject object, String time) throws JSONException {

        String trend = object.getString("trend");

        SmokeItem item = new SmokeItem();
        item.type = type;
        item.value = object.getString("current_value");
        item.state = SmokeState.fromString(object.getString("state"));
        item.probe = object.getString("name");
        item.time = time;

        if (trend.equalsIgnoreCase("up")) {
            item.trend = SmokeItem.Trend.UP;
        } else if (trend.equalsIgnoreCase("down")) {
            item.trend = SmokeItem.Trend.DOWN;
        } else {
            item.trend = SmokeItem.Trend.STABLE;
        }

        return item;
    }
}
