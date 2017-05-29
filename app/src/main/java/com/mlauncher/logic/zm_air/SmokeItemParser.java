package com.mlauncher.logic.zm_air;

import com.mlauncher.logic.SmokeState;
import com.mlauncher.model.SmokeItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by daba on 2016-09-30.
 */
public class SmokeItemParser {

    public static final String[] PROBE_NAMES = new String[]{
            "Warszawa-Marszałkowska",
            "Warszawa-Komunikacyjna",
            "Warszawa-Ursynów",
            "Warszawa-Targówek"};

    public SmokeItem[] parseJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONObject results = root.getJSONObject("results");
        JSONObject pm10 = results.getJSONObject("pm_10");
        JSONObject pm2_5 = results.getJSONObject("pm_25");
        String time = root.getString("time");

        for (String name : PROBE_NAMES) {
            if (checkProbe(pm10, name)) {
                return extractProbe(pm10, pm2_5, time, name);
            }
        }

        return null;
    }

    public boolean checkProbe(JSONObject pm10, String probeName) throws JSONException {
        return getChildByName(pm10, probeName).optString("current_value", null) != null;
    }

    public SmokeItem[] extractProbe(JSONObject pm10, JSONObject pm2_5, String time, String name) throws JSONException {
        JSONObject pm10Object = getChildByName(pm10, name);
        JSONObject pm2_5Object = getChildByName(pm2_5, name);
        return new SmokeItem[]{
                parseObjectToItem(SmokeItem.Type.PM10, pm10Object, time),
                parseObjectToItem(SmokeItem.Type.PM2_5, pm2_5Object, time),
        };
    }

    public JSONObject getChildByName(JSONObject parent, String name) throws JSONException {
        Iterator<String> iterator = parent.keys();
        while (iterator.hasNext()) {
            String probeName = iterator.next();
            JSONObject child = parent.getJSONObject(probeName);
            if (child.optString("name").equals(name)) {
                return child;
            }
        }
        return null;
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
