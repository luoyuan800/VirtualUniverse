package luo.gavin.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadTxtToArray {

    public static void main(String[] args) throws IOException{
        File f = new File("D:\\abc.txt");
        InputStream input = new FileInputStream(f);
        BufferedReader b = new BufferedReader(new InputStreamReader(input));
        StringBuffer buffer = new StringBuffer();
        String value = b.readLine();
        List<String[]> arrayList = new ArrayList<String[]>();
        while(value != null){
            String[] temp = buffer.toString().replaceFirst(" ","").split("\\s+");
            String[] number = new String[temp.length];
            for(int i=0;i<temp.length;i++){
                number[i] = temp[i];
                System.out.print(number[i]+" ");
            }
            arrayList.add(number);
            value = b.readLine();
        }
        String[][] result = new String[arrayList.size()][];
        result = arrayList.toArray(result);
    }
}