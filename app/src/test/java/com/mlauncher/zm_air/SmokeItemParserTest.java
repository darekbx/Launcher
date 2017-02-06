package com.mlauncher.zm_air;

import com.mlauncher.BuildConfig;
import com.mlauncher.FileUtils;
import com.mlauncher.logic.SmokeState;
import com.mlauncher.logic.zm_air.SmokeItemParser;
import com.mlauncher.model.SmokeItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by daba on 2016-09-30.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SmokeItemParserTest {

    @Test
    public void parse_json() throws Exception {

        File contentFile = FileUtils.fileFromResources("zm.json");

        assertTrue(contentFile.exists());
        assertTrue(contentFile.canRead());

        String contents = FileUtils.readFile(contentFile);
        assertNotNull(contents);

        SmokeItem[] items = new SmokeItemParser().parseJson(contents);

        assertEquals(items.length, 2);

        assertEquals(items[0].type, SmokeItem.Type.PM10);
        assertEquals(items[0].trend, SmokeItem.Trend.DOWN);
        assertEquals(items[0].value, "80.9 µg/m3");
        assertEquals(items[0].state, SmokeState.NOTBAD);
        assertEquals(items[0].time, "2016-09-30 09:00:00");
        assertEquals(items[0].probe, "Warszawa-Komunikacyjna");

        assertEquals(items[1].type, SmokeItem.Type.PM2_5);
        assertEquals(items[1].trend, SmokeItem.Trend.DOWN);
        assertEquals(items[1].value, "21.1 µg/m3");
        assertEquals(items[1].state, SmokeState.GOOD);
        assertEquals(items[1].time, "2016-09-30 09:00:00");
        assertEquals(items[1].probe, "Warszawa-Komunikacyjna");

    }
}
