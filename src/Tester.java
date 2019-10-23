
import lib.normalization.Normalization;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import lib.stopwords.StopWords;
import lib.porterstemmer.porterStemmer;
import lib.stopwords.WordIterator;
import lib.normalization.Normalization;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this inputlate file, choose Tools | Templates
 * and open the inputlate in the editor.
 */

/**
 *
 * @author asus
 */


public class Tester {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String input = "";
        String[] outputTXT = new String[154];
        String[] outputStopwords = new String[154];
        String[] outputNormalization = new String[154];
        
        // TXT File
        for (int i = 1; i <= 154; i++) {
            String name = "";
            if(i<10){
                name = "00"+i;
            }
            else if(i>=10 && i<100){
                name = "0"+i;
            }
            else if(i>=100 && i<=154){
                name = ""+i;
            }
            
            // initialize input
            BufferedReader br = new BufferedReader(new FileReader("C:/Users/asus/Desktop/DataSet/Doc"+name+".txt"));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                input = sb.toString();
            } 
            finally {
                br.close();
            }
            // Case Folding
            outputTXT[i-1]=input.toLowerCase();
            input = "";
        }
        
        // Stopwords
        for (int i = 1; i <= 154; i++) {
            for (final String word : new WordIterator(outputTXT[i-1])) {
                if (StopWords.English.isStopWord(word) == false) {
                    input += word+" ";
                }
            }
            outputStopwords[i-1]=input;
            input = "";
        }
        
        // Normalization
        for (int i = 1; i <= 154; i++) {
            input = outputStopwords[i-1];
            String temp = Normalization.formatString(input);
            outputNormalization[i-1] = temp;
            
            //print output
//            System.out.println(i+". "+outputNormalization[i-1]);
            input = "";
        }
        
        // Porter Stemmer
        for (int i = 1; i <= 154; i++) {
            porterStemmer stemmer = new porterStemmer(); 
            input = outputNormalization[i-1];
            String temp = "";
            String[] arrInput =  input.split(" ");
            for (int j = 0; j < arrInput.length; j++) {
                stemmer.setCurrent(arrInput[j]);
                stemmer.stem();
                temp += stemmer.getCurrent()+" ";
            }
            
            System.out.println(i+". "+temp);
            input = "";
        }
        
        System.out.println("");
        
        //Statistik : Jumlah dokumen
        System.out.println("Jumlah dokumen");
        File inputFile = new File("C://Users//asus//Desktop//DataSet");
	ArrayList<File> files = MyUtils.listFilesForFolder(inputFile);
        System.out.println(files.size());
        System.out.println("");
        
        //Statistik : Jumlah words dari seluruh dokumen
        System.out.println("Jumlah words dari seluruh dokumen");
        int jumlahWords = 0;
        for (int i = 1; i <= 154; i++) {
            input = outputTXT[i-1];
            String[] words = input.replaceAll("\r\n", " ").replaceAll("[^a-zA-Z] ", " ").split(" ");
            jumlahWords+=words.length;
        }
        System.out.println(jumlahWords);
        
        System.out.println("");
        
        //Statistik : Jumlah rata-rata words per dokumen

        System.out.println("Jumlah rata-rata words per dokumen");
        int rata2 = jumlahWords/154;
        System.out.println(rata2);
         
        
    }
}
