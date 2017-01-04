package com.mlauncher.sojp;

import com.mlauncher.BuildConfig;
import com.mlauncher.FileUtils;
import com.mlauncher.logic.sojp.Entry;
import com.mlauncher.logic.sojp.EntryParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by daba on 2016-07-22.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EntryParserTest {

    @Test
    public void parse_content() {

        File contentFile = FileUtils.fileFromResources("EV_ZP-WaKo.txt");

        assertTrue(contentFile.exists());
        assertTrue(contentFile.canRead());

        String contents = FileUtils.readFile(contentFile);
        assertNotNull(contents);

        List<Entry> entries = EntryParser.parseContent(contents);

        assertNotNull(entries);
        assertEquals(entries.size(), 15);
        assertEquals(entries.get(0).date, "2016/09/1411:00:00");
        assertEquals(entries.get(0).station, "MzWarAlNiepo");
        assertEquals(entries.get(0).parameter, "PM10");
        assertEquals(entries.get(0).unit, "µg/m3");
        assertEquals(entries.get(0).value, 43.1, 0);
        assertEquals(entries.get(14).date, "2016/09/1413:00:00");
        assertEquals(entries.get(14).station, "MzWarAlNiepo");
        assertEquals(entries.get(14).parameter, "benzen");
        assertEquals(entries.get(14).unit, "µg/m3");
        assertEquals(entries.get(14).value, 0.76, 0);

    }
}
