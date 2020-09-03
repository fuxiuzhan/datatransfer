package com.fxz.channelswitcher.datatransferserver.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiuzhan.fu
 */
public class FileUtils {

    public static List<String> readFile(String filePath) {
        List<String> result = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return result;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filePath)));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (line!=null && line.length()>0) {
                    result.add(line);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void appendFile(String filePath, String text) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter ous = new BufferedWriter(new FileWriter(file, true));
            ous.newLine();
            ous.write(text);
            ous.flush();
            ous.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(readFile("d:\\test.txt"));
        appendFile("d:\\test.txt", "127.0.0.5");
        System.out.println(readFile("d:\\test.txt"));
    }
}
