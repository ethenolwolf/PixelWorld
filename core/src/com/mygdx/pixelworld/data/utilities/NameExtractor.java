package com.mygdx.pixelworld.data.utilities;

import com.mygdx.pixelworld.GUI.Logger;

import java.io.*;
import java.util.Random;

public class NameExtractor {

    private final static String FILE_PATH = "core/assets/maleNames.dic";
    private final static String DEFAULT_NAME = "Alex";

    public static String extract() {
        LineNumberReader lnr;
        try {
            lnr = new LineNumberReader(new FileReader(new File(FILE_PATH)));
            lnr.skip(Long.MAX_VALUE);
            lnr.close();
        } catch (FileNotFoundException e) {
            Logger.log("NameExtractor", "Name dic not found.");
            return DEFAULT_NAME;
        } catch (IOException e) {
            Logger.log("NameExtractor", "IO Error while reading.");
            return DEFAULT_NAME;
        }

        try {
            Random rand = new Random();
            FileInputStream fs = new FileInputStream(FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            for (int i = 0; i < rand.nextInt(lnr.getLineNumber()) - 1; i++)
                br.readLine();
            String line = br.readLine();
            String names[] = line.split(" ");
            return names[0];
        } catch (FileNotFoundException e) {
            Logger.log("NameExtractor", "Name dic not found.");
            return DEFAULT_NAME;
        } catch (IOException e) {
            Logger.log("NameExtractor", "IO Error while reading.");
            return DEFAULT_NAME;
        }
    }
}
