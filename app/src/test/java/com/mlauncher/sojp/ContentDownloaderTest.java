package com.mlauncher.sojp;

import com.mlauncher.BuildConfig;
import com.mlauncher.logic.sojp.ContentDownloader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by daba on 2016-09-14.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ContentDownloaderTest {

    @Test
    public void testDownloadContent() throws Exception {
        ContentDownloader.Listener listener = mock(ContentDownloader.Listener.class);

        ContentDownloader.downloadContent(listener);

        ArgumentCaptor<String> argument = new ArgumentCaptor<String>();
        verify(listener, times(1)).onContent(argument.capture());

        assertNotNull(argument.getValue());
        assertTrue(argument.getValue().length() > 10);
        assertEquals(argument.getValue().charAt(0), '2');
    }
}