package com.mlauncher.logic.ussd.model;

/**
 * Created by daba on 2016-12-12.
 */

public class AccountBalance extends BaseBalance {
    public String amount;

    @Override
    public String toString() {
        return "Amount: " + amount + "\n" + super.toString();
    }
}
