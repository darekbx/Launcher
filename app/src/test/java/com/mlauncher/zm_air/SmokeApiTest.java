package com.mlauncher.zm_air;

import com.mlauncher.BuildConfig;
import com.mlauncher.logic.zm_air.SmokeApi;
import com.mlauncher.model.SmokeItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by daba on 2016-09-30.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SmokeApiTest {

    @Test
    public void do_in_background() {

        SmokeApi.Listener listener = mock(SmokeApi.Listener.class);

        new SmokeApi(RuntimeEnvironment.application, listener).execute();

        ArgumentCaptor<SmokeItem> pm10Captor = new ArgumentCaptor<SmokeItem>();
        ArgumentCaptor<SmokeItem> pm25Captor = new ArgumentCaptor<SmokeItem>();
        verify(listener, times(1)).onItems(pm10Captor.capture(), pm25Captor.capture());

        assertNotNull(pm10Captor.getValue());
        assertNotNull(pm25Captor.getValue());

        assertEquals(pm10Captor.getValue().type, SmokeItem.Type.PM10);
        assertEquals(pm25Captor.getValue().type, SmokeItem.Type.PM2_5);
    }

    @Test
    public void remove_function() {
        String input = "jsonpZMWklejka({})";
        String output = new SmokeApi(null, null).removeFunction(input);

        assertEquals(output, "{}");
    }
}
