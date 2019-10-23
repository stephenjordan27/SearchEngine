
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
        String result = ""; 
        
        System.out.println("Stopwords");
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
            BufferedReader br = new BufferedReader(new FileReader("C:/Users/asus/Desktop/DataSet/Doc"+name+".txt"));

            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                result = sb.toString();
            } 
            finally {
                br.close();
            }
            
            
            // Stopwords
            System.out.print(i+".");
            for (final String word : new WordIterator(result)) {
                if (StopWords.English.isStopWord(word) == false) {
                    System.out.print(word+" ");
                }
            }
            System.out.println("");
        }
        
        System.out.println("");
        
        System.out.println("Porter Stemmer");
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
            BufferedReader br = new BufferedReader(new FileReader("C:/Users/asus/Desktop/DataSet/Doc"+name+".txt"));

            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                result = sb.toString();
            } 
            finally {
                br.close();
            }
            
            // Porter Stemmer
            System.out.print(i+".");
            porterStemmer stemmer = new porterStemmer(); 
            String input = result.replaceAll("\r\n", "").replaceAll("[^a-zA-Z0-9]", " ");
            String[] arrInput =  input.split(" ");
            for (int j = 0; j < arrInput.length; j++) {
                stemmer.setCurrent(arrInput[j]);
                stemmer.stem();
                System.out.print(stemmer.getCurrent()+" ");
                
            }
            
            System.out.println("");
        }
    }
}
