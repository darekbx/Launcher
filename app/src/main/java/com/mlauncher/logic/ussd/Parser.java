package com.mlauncher.logic.ussd;

import com.mlauncher.logic.ussd.model.AccountBalance;
import com.mlauncher.logic.ussd.model.InternetBalance;

/**
 * Created by daba on 2016-12-12.
 */

public class Parser {

    public static boolean isAccountBalance(String message) {
        return message.indexOf("[Pozostalo") == 0;
    }

    public static AccountBalance parseAccountBalance(String message) {
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.amount = message.substring(14, 21).replace(',', '.');
        accountBalance.toDate = message.substring(42, 52);
        return accountBalance;
    }

    public static InternetBalance parseInternetBalance(String message) {
        InternetBalance internetBalance = new InternetBalance();
        internetBalance.dataAmount = message.substring(56, 65);
        internetBalance.toDate = message.substring(93, 103);
        return internetBalance;
    }
}