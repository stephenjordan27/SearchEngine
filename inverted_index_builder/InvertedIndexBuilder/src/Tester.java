
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Irvan Hardyanto - 2016730070
 */
public class Tester {

    public static void main(String[] args) {
        File inputDir = new File("cleaned_dataset");
        TreeMap<String, ArrayList<String>> invertedIndex = InvertedIndexBuilder.createDictionary(inputDir);
        TreeSet<String> keySet = new TreeSet(invertedIndex.keySet());
        Iterator<String> iSet = keySet.iterator();
        String currKey;
        ArrayList<String> value;
        BufferedWriter bw;
        long startTime = System.currentTimeMillis();
        try {
            File output = new File("inverted_index.dat");
            
            ObjectOutputStream os = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(output)));
            bw = new BufferedWriter(new FileWriter(new File("invertedIndex.txt")));
            
            while (iSet.hasNext()) {
                currKey = iSet.next();
                if(currKey.length()>0){
                    value = invertedIndex.get(currKey);
                    bw.write(currKey + " -> " + value.toString()+"\n");
                }
            }
            bw.close();
            os.writeObject(invertedIndex);
            os.flush();
            os.close();
        } catch (IOException ex) {
            System.out.println("IOException has been occured");
            ex.printStackTrace();
        }
        try{
            ObjectInputStream oi = new ObjectInputStream(new GZIPInputStream(new FileInputStream("inverted_index.dat")));
            
            TreeMap<String, ArrayList<String>> tmp  = ( TreeMap<String, ArrayList<String>>) oi.readObject();
            System.out.println(tmp.equals(invertedIndex));
        }catch(IOException ex){
            System.out.println("e1");
            ex.printStackTrace();
        }catch (ClassNotFoundException ex){
            System.out.println("e2");
            ex.printStackTrace();
        }
        System.out.println("Building Inverted Index takes: "+(System.currentTimeMillis()-startTime)+" milliseconds");
    }
}
