
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import lib.stopwords.StopWords;
import lib.porterstemmer.porterStemmer;
import lib.stopwords.WordIterator;

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
        String[] outputTXT;
        String[] outputStopwords;
        String[] outputStemmer;

        // TXT File
//        outputTXT = MyUtils.readFiles("F:\\SEMESTER 7 GANJIL 2019-2020\\Pencarian dan Temu Kembali Informasi\\YouRSearchEngine\\raw_dataset");
//        outputStopwords = Preprocessor.removeStopWords(outputTXT);
//        outputStemmer = Preprocessor.stem(outputStopwords);
//        for (int i = 0; i < outputStemmer.length; i++) {
//            System.out.println((i+1)+". "+outputStemmer[i]);
//        }


File inputDir = new File("F:/SEMESTER 7 GANJIL 2019-2020/Pencarian dan Temu Kembali Informasi/YouRSearchEngine/raw_dataset");
		File outputDir = new File("F:/SEMESTER 7 GANJIL 2019-2020/Pencarian dan Temu Kembali Informasi/YouRSearchEngine/preprocessed_dataset");
		
		Preprocessor p =new Preprocessor();
		long startTime = System.currentTimeMillis();
			
		p.preProcess(inputDir,outputDir);
			
		long endTime = System.currentTimeMillis();
		
		System.out.println();
		System.out.println("Time ellapsed: "+(endTime-startTime));
		
		File statsDir= new File("F:/SEMESTER 7 GANJIL 2019-2020/Pencarian dan Temu Kembali Informasi/YouRSearchEngine/stat.json");
		try{
			BufferedWriter bw  = new BufferedWriter(new FileWriter(statsDir));
			bw.write(MyUtils.statistics(inputDir,"word"));
			bw.write(MyUtils.statistics(outputDir,"term"));
			bw.close();
		}catch(IOException e){
			
		}
        // Stopwords
       

        // Porter Stemmer

//        System.out.println("");
//
//        //Statistik : Jumlah dokumen
//        System.out.println("Jumlah dokumen");
//        File inputFile = new File("C://Users//asus//Desktop//DataSet");
//        ArrayList<File> files = MyUtils.listFilesForFolder(inputFile);
//        System.out.println(files.size());
//        System.out.println("");
//
//        //Statistik : Jumlah words dari seluruh dokumen
//        System.out.println("Jumlah words dari seluruh dokumen");
//        int jumlahWords = 0;
//        for (int i = 1; i <= 154; i++) {
//            input = outputTXT[i - 1];
//            String[] words = input.replaceAll("\r\n", " ").replaceAll("[^a-zA-Z] ", " ").split(" ");
//            jumlahWords += words.length;
//        }
//        System.out.println(jumlahWords);
//
//        System.out.println("");
//
//        //Statistik : Jumlah rata-rata words per dokumen
//        System.out.println("Jumlah rata-rata words per dokumen");
//        int rata2 = jumlahWords / 154;
//        System.out.println(rata2);

    }
}
