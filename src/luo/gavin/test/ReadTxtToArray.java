package luo.gavin.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ReadTxtToArray {

    public static void main(String[] args) throws IOException{
        InetAddress address = InetAddress.getByName("rd-aix02");
        System.out.println(address.getHostName() + ", " + address.getHostAddress());
        InetAddress address1 = InetAddress.getByName("10.4.114.96");
        System.out.println(address1.getHostName() + ", " + address1.getHostAddress());
    }
}