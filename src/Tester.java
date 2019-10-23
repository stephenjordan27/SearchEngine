
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lib.stopwords.StopWords;
import lib.porterstemmer.porterStemmer;
import lib.stopwords.WordIterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author asus
 */


public class Tester {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String temp = "";
        String[] outputReader = new String[154];
        String[] outputStopwords = new String[154];
        
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
                temp = sb.toString();
            } 
            finally {
                br.close();
            }
            outputReader[i-1]=temp;
            temp = "";
        }
        
        // Stopwords
        for (int i = 1; i <= 154; i++) {
            for (final String word : new WordIterator(outputReader[i-1])) {
                if (StopWords.English.isStopWord(word) == false) {
                    temp += word+" ";
                }
            }
            outputStopwords[i-1]=temp;
            temp = "";
        }
        
        // Porter Stemmer
        for (int i = 1; i <= 154; i++) {
            porterStemmer stemmer = new porterStemmer(); 
            temp = outputStopwords[i-1];
            String[] arrInput =  temp.split(" ");
            for (int j = 0; j < arrInput.length; j++) {
                stemmer.setCurrent(arrInput[j]);
                stemmer.stem();
                temp += stemmer.getCurrent()+" ";
                
            }
            
            //print output
            System.out.println(i+". "+temp);
            temp = "";
        }
    }
}
