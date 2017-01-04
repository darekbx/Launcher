package com.mlauncher.logic.utils;

import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by INFOR PL on 2015-07-06.
 */
public class ShellUtils {

    public static boolean unlock(Context context, String path) {

        return executeStatementNoResonse(
                new String[] {
                        "su",
                        "-c",
                        "chmod 777 " + path
                });
    }

    public static List<String> listDebuggableApps() {
        try {

            String[] command = new String[]{
                "su", "-c", "grep \" 1 /\" /data/system/packages.list"
            };

            Process process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            List<String> output = new ArrayList<String>();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                output.add(line.indexOf(' ') > 0 ? line.substring(0, line.indexOf(' ')) : line);
            }

            return output;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean isDeviceRooted() {
        return checkRoot1() || checkRoot2() || checkRoot3();
    }

    private static boolean checkRoot1() {
        return Build.TAGS != null && Build.TAGS.contains("test-keys");
    }

    private static boolean checkRoot2() {

        try {
            return new File("/system/app/Superuser.apk").exists();
        }
        catch (Exception e) {
            return false;
        }
    }

    private static boolean checkRoot3() {
        return executeStatementNoResonse(
                new String[]{
                        "/system/xbin/which",
                        "su"
                });
    }

    public static boolean executeStatementNoResonse(String[] shellCmd) {

        try {

            Process process = Runtime.getRuntime().exec(shellCmd);
            process.waitFor();

            return true;
        }
        catch (Exception e) { return false; }
    }
}
