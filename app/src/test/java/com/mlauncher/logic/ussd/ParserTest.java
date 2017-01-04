package com.mlauncher.logic.ussd;

import com.mlauncher.logic.ussd.model.AccountBalance;
import com.mlauncher.logic.ussd.model.InternetBalance;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by daba on 2016-12-13.
 */
public class ParserTest {

    public static final String INTERNET_BALANCE = "[Stan promocyjnego konta z szybka transmisja danych to: 1756.30MB . Mozesz je wykorzystac do 2017-01-02 23:59:59., OK]";
    public static final String ACCOUNT_BALANCE = "[Pozostalo Ci 48,42zl do wykorzystania do 2017-03-21 23:59:59. Dodatkowo w ramach srodkow promocyjnych masz 0,00 zl do wykorzystania do -., OK]";

    @Test
    public void isAccountBalance() throws Exception {
        assertTrue(Parser.isAccountBalance(ACCOUNT_BALANCE));
        assertFalse(Parser.isAccountBalance(INTERNET_BALANCE));
    }

    @Test
    public void parseAccountBalance() throws Exception {

        AccountBalance accountBalance = Parser.parseAccountBalance(ACCOUNT_BALANCE);

        assertEquals("48.42zl", accountBalance.amount);
        assertEquals("2017-03-21", accountBalance.toDate);
    }

    @Test
    public void parseInternetBalance() throws Exception {

        InternetBalance internetBalance = Parser.parseInternetBalance(INTERNET_BALANCE);

        assertEquals("1756.30MB", internetBalance.dataAmount);
        assertEquals("2017-01-02", internetBalance.toDate);
    }
}