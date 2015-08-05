package luo.gavin.test;

import luo.gavin.virtual.Base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReadTxtToArray {

    public static void main(String[] args) throws IOException{
        BaseImp b1 = new BaseImp(10, 10, 10);
        BaseImp b2 = new BaseImp(100,10,500);
        System.out.println(b1.contains(b2));
    }

    static class BaseImp extends Base{

        public BaseImp(int x, int y, int radii) {
            super(x, y, radii);
        }

        @Override
        protected void doRun() {

        }

        @Override
        public Random getRandom() {
            return null;
        }
    }
}