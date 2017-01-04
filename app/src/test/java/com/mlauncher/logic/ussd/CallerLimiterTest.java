package com.mlauncher.logic.ussd;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by daba on 2016-12-13.
 */
@RunWith(RobolectricTestRunner.class)
public class CallerLimiterTest {

    @Test
    public void canCall() throws Exception {
        CallerLimiter callerLimiter = new CallerLimiterMock(RuntimeEnvironment.application);

        boolean result = callerLimiter.canCall();
        assertTrue(result);

        result = callerLimiter.canCall();
        assertFalse(result);

        Thread.sleep(100);

        result = callerLimiter.canCall();
        assertTrue(result);
    }

    private class CallerLimiterMock extends CallerLimiter {

        public CallerLimiterMock(Context context) {
            super(context);
        }

        @Override
        protected long getCallInterval() {
            return TimeUnit.MILLISECONDS.toMillis(100);
        }

        @Override
        protected long getNextCallTime(long lastCallTime) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(lastCallTime));
            calendar.add(Calendar.MILLISECOND, (int)getCallInterval());
            return calendar.getTimeInMillis();
        }
    }
}