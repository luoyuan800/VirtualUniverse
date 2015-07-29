/*
 * Output.java
 * Date: 7/27/2015
 * Time: 4:02 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package luo.gavin.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Output extends ArrayList<String> {
    private static final Output output = new Output();
    private static long latest = System.currentTimeMillis();
    private static File file = new File(""  + System.currentTimeMillis());

    public synchronized static void LOG(String msg) {
        output.add(msg);
        long current = System.currentTimeMillis();
        long interval = current - latest;
        if (interval > 10000 || output.size() > 100 || msg.matches("Finished stop the Virtual")) {
            output();
        }
    }

    private static void output() {
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String msg : output) {
                bw.write("\n");
                bw.write(msg);
            }
            bw.flush();
            bw.close();
            output.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
