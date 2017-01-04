package com.mlauncher.logic.ussd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by daba on 2016-12-13.
 */
@RunWith(RobolectricTestRunner.class)
public class CallerTest {

    public static final String INTERNET_BALANCE = "Stan promocyjnego konta z szybka transmisja danych to: 1764.40MB . Mozesz je wykorzystac do 2017-01-02 23:59:59., OK";
    public static final String ACCOUNT_BALANCE = "Pozostalo Ci 48,42zl do wykorzystania do 2017-03-21 23:59:59. Dodatkowo w ramach srodkow promocyjnych masz 0,00 zl do wykorzystania do -., OK";

    @Test
    public void retrieveInfo() throws Exception {
        Caller instance =  spy(CallerMock.getMockInstance());
        Caller.setInstance(instance);

        instance.retrieveInfo();

        verify(instance, times(3)).takeFirst();
        verify(instance, times(1)).parseMessage(ACCOUNT_BALANCE);
        verify(instance, times(1)).parseMessage(INTERNET_BALANCE);
    }

    public static class CallerMock extends Caller {

        public static Caller getMockInstance() {
            return new Caller();
        }
    }
}