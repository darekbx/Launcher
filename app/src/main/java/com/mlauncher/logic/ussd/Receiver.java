package com.mlauncher.logic.ussd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by daba on 2016-12-12.
 */

public class Receiver extends BroadcastReceiver {

    public static final String INFO_ACTION = "ussd.INFO_ACTION";
    public static final String MESSAGE_KEY = "ussd.MESSAGE_KEY";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(INFO_ACTION)) {
            Caller.getInstance(null).parseMessage(intent.getStringExtra(MESSAGE_KEY));
        }
    }
}
