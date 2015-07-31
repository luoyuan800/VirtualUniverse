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
    private static long latest = System.currentTimeMillis();
    private File file;

    public Output(Class clazz){
        file = new File(clazz.getSimpleName() + "_" + System.currentTimeMillis() + ".opr");
    }
    public Output(String file){
        this.file = new File(file + ".opr");
    }
    public synchronized void LOG(String msg) {
        this.add(msg);
        long current = System.currentTimeMillis();
        long interval = current - latest;
        if (interval > 10000 || this.size() > 100 || msg.matches("Finished stop.*")) {
            output();
        }
    }

    private void output() {
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String msg : this) {
                bw.write("\n");
                bw.write(msg);
            }
            bw.flush();
            bw.close();
            this.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
