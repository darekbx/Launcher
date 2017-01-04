package com.mlauncher.logic.ussd.model;

/**
 * Created by daba on 2016-12-12.
 */

public class InternetBalance extends BaseBalance {
    public String dataAmount;

    @Override
    public String toString() {
        return "Data amount: " + dataAmount + "\n" + super.toString();
    }
}
