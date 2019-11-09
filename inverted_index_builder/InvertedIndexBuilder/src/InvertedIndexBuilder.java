
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Irvan Hardyanto - 2016730070
 */
public class InvertedIndexBuilder {

    public static TreeMap<String, ArrayList<String>> createDictionary(File cleanedDocsDir) {
        TreeMap<String, ArrayList<String>> output = new TreeMap<>();
        //lakukan preprocess data
        ArrayList<File> files = new ArrayList<File>();
        Iterator<String> iString;
        Iterator<File> iFile;

        for (File f : cleanedDocsDir.listFiles()) {
            if (f.isDirectory()) {
                System.out.println("Error reading, this is not a file");
            } else {
                //System.out.println(f.getName());
                try {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    String line = "";

                    while ((line = br.readLine()) != null) {
                        line= line.trim();
                        String[] terms = line.split(" ");
                        for (int i = 0; i < terms.length; i++) {
                            if (output.containsKey(terms[i])) {
                                ArrayList<String> tmp = output.get(terms[i]);
                                if(!tmp.contains(f.getName()))
                                    tmp.add(f.getName());
                            } else {
                                ArrayList<String> postingList = new ArrayList<>();
                                postingList.add(f.getName());
                                output.put(terms[i], postingList);
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error: File: " + f.getName() + " not found!");
                } catch (IOException e) {
                    System.out.println("Error reading file : " + f.getName());
                }
            }
        }
        //uncomment untuk melihat dictionary yang dihasilkan
        //NB: pastikan java anda minimal jdk 1.8!!
//        File outDir = new File("dictionary2.txt");
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter(outDir));
//            output.forEach((k, v) -> {
//                    try{
//                        
//                    bw.write(k + " " + v.toString()+"\n");
//                    }catch(IOException e){
//                    }}
//            );
//            bw.close();
//        } catch (IOException e) {
//            System.out.println("cannot write txt dictionary");
//        }
//        //System.out.println("Mapping: "+output);
        return output;
    }
}
