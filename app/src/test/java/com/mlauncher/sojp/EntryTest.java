package com.mlauncher.sojp;

import com.mlauncher.logic.SmokeState;
import com.mlauncher.logic.sojp.Entry;
import com.mlauncher.model.SmokeItem;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by daba on 2016-09-14.
 */
public class EntryTest {

    @Test
    public void testToSmokeItem_pm10_good() throws Exception {
        Entry entry = new Entry();
        entry.date = "2016/09/1411:00:00";
        entry.station = "MzWarAlNiepo";
        entry.parameter = "PM10";
        entry.unit = "µg/m3";
        entry.value = 43.1d;

        SmokeItem smokeItem = entry.toSmokeItem();

        assertNotNull(smokeItem);
        assertEquals(smokeItem.probe, entry.station);
        assertEquals(smokeItem.value, entry.value + " " + entry.unit);
        assertEquals(smokeItem.time, Entry.smokeItemDate(entry.date));
        assertEquals(smokeItem.state, SmokeState.GOOD);
        assertEquals(smokeItem.getArrow(), "-");
    }

    @Test
    public void testToSmokeItem_pm2_5_verybad() throws Exception {
        Entry entry = new Entry();
        entry.date = "2016/09/1411:00:00";
        entry.station = "MzWarAlNiepo";
        entry.parameter = "PM2.5";
        entry.unit = "µg/m3";
        entry.value = 80.0d;

        SmokeItem smokeItem = entry.toSmokeItem();

        assertNotNull(smokeItem);
        assertEquals(smokeItem.probe, entry.station);
        assertEquals(smokeItem.value, entry.value + " " + entry.unit);
        assertEquals(smokeItem.time, Entry.smokeItemDate(entry.date));
        assertEquals(smokeItem.state, SmokeState.VERYBAD);
        assertEquals(smokeItem.getArrow(), "-");
    }

    @Test
    public void determineState_pm10() {
        Entry entry =new Entry();
        int step = 50;

        entry.value = 45;
        assertEquals(SmokeState.GOOD, entry.determineState(step));

        entry.value = 90;
        assertEquals(SmokeState.NOTBAD, entry.determineState(step));

        entry.value = 120;
        assertEquals(SmokeState.BAD, entry.determineState(step));

        entry.value = 180.5;
        assertEquals(SmokeState.VERYBAD, entry.determineState(step));

        entry.value = 200.01;
        assertEquals(SmokeState.EXTREMELYBAD, entry.determineState(step));
    }

    @Test
    public void determineState_pm2_5() {
        Entry entry =new Entry();
        int step = 25;

        entry.value = 10;
        assertEquals(SmokeState.GOOD, entry.determineState(step));

        entry.value = 35;
        assertEquals(SmokeState.NOTBAD, entry.determineState(step));

        entry.value = 51;
        assertEquals(SmokeState.BAD, entry.determineState(step));

        entry.value = 80;
        assertEquals(SmokeState.VERYBAD, entry.determineState(step));

        entry.value = 100.01;
        assertEquals(SmokeState.EXTREMELYBAD, entry.determineState(step));
    }

    @Test
    public void isPM10() throws Exception {
        Entry entry =new Entry();
        entry.parameter = "PM10";

        assertTrue(entry.isPM10());
    }
}