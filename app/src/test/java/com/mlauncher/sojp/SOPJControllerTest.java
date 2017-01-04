package com.mlauncher.sojp;

import com.mlauncher.BuildConfig;
import com.mlauncher.FileUtils;
import com.mlauncher.logic.sojp.Entry;
import com.mlauncher.logic.sojp.EntryParser;
import com.mlauncher.logic.sojp.SOPJController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by daba on 2016-09-14.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SOPJControllerTest {

    @Test
    public void testLoadAirQuality() throws Exception {
        SOPJController.Listener listener = mock(SOPJController.Listener.class);

        SOPJController.loadAirQuality(listener, "PM10", "PM2.5");

        ArgumentCaptor<List> argument = new ArgumentCaptor<List>();
        verify(listener, times(1)).onData(argument.capture());

        assertNotNull(argument.getValue());
        assertEquals(argument.getValue().size(), 2);
        assertEquals(((Entry)argument.getValue().get(0)).parameter, "PM10");
        assertEquals(((Entry)argument.getValue().get(1)).parameter, "PM2.5");
    }

    @Test
    public void testEntriesByParameters() throws Exception {
        File contentFile = FileUtils.fileFromResources("EV_ZP-WaKo.txt");

        assertTrue(contentFile.exists());
        assertTrue(contentFile.canRead());

        String contents = FileUtils.readFile(contentFile);
        assertNotNull(contents);

        List<Entry> entries = EntryParser.parseContent(contents);

        List<Entry> filteredEntries = SOPJController.entriesByParameters(entries, "PM10", "PM2.5");

        assertNotNull(filteredEntries);
        assertEquals(filteredEntries.size(), 2);
        assertEquals(filteredEntries.get(0).parameter, "PM10");
        assertEquals(filteredEntries.get(1).parameter, "PM2.5");
    }
}