package com.mlauncher.logic.ussd;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by daba on 2016-12-12.
 */
public class AccessibilityService extends android.accessibilityservice.AccessibilityService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getClassName().equals("android.app.AlertDialog")) {
            performGlobalAction(GLOBAL_ACTION_BACK);
            String text = accessibilityEvent.getText().toString();
            Intent intent = new Intent(Receiver.INFO_ACTION);
            intent.putExtra(Receiver.MESSAGE_KEY, text);
            sendBroadcast(intent);
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
    }
}