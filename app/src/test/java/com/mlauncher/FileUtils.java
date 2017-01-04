package com.mlauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by daba on 2016-09-14.
 */
public class FileUtils {

    public static String readFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, Charset.forName("ISO-8859-1")));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public static File fileFromResources(String fileName) {
        String testRestDir = new File("src/test/res").getAbsolutePath();
        return new File(testRestDir, fileName);
    }
}
