package com.mlauncher.logic.ussd;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mlauncher.logic.ussd.model.AccountBalance;
import com.mlauncher.logic.ussd.model.InternetBalance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daba on 2016-12-12.
 */

public class Caller {

    public interface Listener {
        void onAccountBalance(AccountBalance accountBalance);

        void onInternetBalance(InternetBalance internetBalance);
    }

    public static final String ACCOUNT_BALANCE = "*101";
    public static final String INTERNET_BALANCE = "*108";

    private static final boolean DEBUG = false;

    private static Caller instance;
    private Activity activity;
    private Receiver receiver;
    private Listener listener;

    private List<String> codeQueue = new ArrayList<>(2);

    public static Caller getInstance(@Nullable Activity activity) {
        if (instance == null) {
            if (activity == null) {
                throw new NullPointerException();
            }
            instance = new Caller(activity);
        }
        return instance;
    }

    public Caller() {
    }

    public Caller(Activity activity) {
        this.activity = activity;
        receiver = new Receiver();
        activity.startService(new Intent(activity, AccessibilityService.class));
        activity.registerReceiver(receiver, new IntentFilter(Receiver.INFO_ACTION));
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static void setInstance(Caller caller) {
        instance = caller;
    }

    public void cleanUp() {
        if (activity != null && receiver != null) {
            activity.unregisterReceiver(receiver);
        }
    }

    public void retrieveInfo() {
        codeQueue.add(ACCOUNT_BALANCE);
        codeQueue.add(INTERNET_BALANCE);
        takeFirst();
    }

    public void takeFirst() {
        if (codeQueue.size() == 0) {
            return;
        }
        String code = codeQueue.get(0);
        codeQueue.remove(0);
        callService(code);
    }

    private void callService(String ussdCode) {
        if (DEBUG) {
            if (ussdCode.equals(ACCOUNT_BALANCE)) {
                parseMessage("[Pozostalo Ci 48,42zl do wykorzystania do 2017-03-21 23:59:59. Dodatkowo w ramach srodkow promocyjnych masz 0,00 zl do wykorzystania do -., OK]");
            } else if (ussdCode.equals(INTERNET_BALANCE)) {
                parseMessage("[Stan promocyjnego konta z szybka transmisja danych to: 1756.30MB . Mozesz je wykorzystac do 2017-01-02 23:59:59., OK]");
            }
        } else {
            try {
                activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode + Uri.encode("#"))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void parseMessage(String message) {
        if (message != null) {
            if (Parser.isAccountBalance(message)) {
                try {
                    AccountBalance accountBalance = Parser.parseAccountBalance(message);
                    if (listener != null) {
                        listener.onAccountBalance(accountBalance);
                    }
                } catch (Exception e) {
                    Log.v("-------", "Unable to parse account message: " + message);
                    e.printStackTrace();
                }
            } else {
                try {
                    InternetBalance internetBalance = Parser.parseInternetBalance(message);
                    if (listener != null) {
                        listener.onInternetBalance(internetBalance);
                    }
                } catch (Exception e) {
                    Log.v("-------", "Unable to parse internet message: " + message);
                    e.printStackTrace();
                }
            }
        }
        takeFirst();
    }
}