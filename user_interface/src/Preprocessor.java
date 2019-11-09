import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.TreeMap;
import lib.normalization.Normalization;
import lib.porterstemmer.porterStemmer;
import lib.stopwords.StopWords;
import lib.stopwords.WordIterator;

public class Preprocessor{
    static String[] preprocessedDocs;
	/**
	 * Melakukan preprocessing pada seluruh file pada folder tertentu, kemudian
	 * menuliskannya ke folder baru
	 * @param inputDir direktori (folder) tempat file-file input berada
	 * @param outputDir direktori (folder) tempat file-file hasil preprocessing
	 **/
	public static void preProcess(File inputDir,File outputDir){
		BufferedReader br;
		BufferedWriter bw;
		String line,processedLine;
		boolean first = true;
		try{
			ArrayList<File> files = MyUtils.listFilesForFolder(inputDir);
			Iterator<File> iter = files.iterator();
			
			outputDir.mkdir();
			
			while(iter.hasNext()){	
				File currentFile = iter.next();
				File outputFile = new File(outputDir,currentFile.getName());
				
				br = new BufferedReader(new FileReader(currentFile));
				bw = new BufferedWriter(new FileWriter(outputFile));
				
				while((line=br.readLine())!=null){
					first = true;
					processedLine = "";
					
					//lemmatization
					processedLine = lemmatize(line);
					
					
					bw.write(processedLine+"\n");
				}
				bw.close();
			}		
		}catch(IOException e){
			System.out.println("exception occured");
		}
	}
	
	//references: https://www.youtube.com/watch?v=nmwRpDuYeeE
	/**
	 * Melakukan lemmatization pada sebuah baris pada dokumen,dengan menggunakan stanfordCoreNLP API (https://stanfordnlp.github.io/CoreNLP/)
	 * @param inputString baris pada dokumen
	 * @return string dengan kata-kata dalam bentuk dasar.
	 **/
	public static String lemmatize(String inputString){
		StanfordCoreNLP stanfordCoreNLP = OurPipeline.getPipeline();
		boolean first=true;
		String processedLine,lemma;
		
		CoreDocument coreDocument = new CoreDocument(inputString);
		stanfordCoreNLP.annotate(coreDocument);
		List<CoreLabel>coreLabelList = coreDocument.tokens();
		processedLine="";
		
		for(CoreLabel coreLabel : coreLabelList){
			lemma = coreLabel.lemma();
			if(first){
				first = false;
			}else{
				processedLine+=" ";
			}
			processedLine += lemma;
		}
		return processedLine;
	}
        
        public static String[] lemmatize(String[] inputDocs) {
        String[] outputDocs = new String[inputDocs.length];
        for (int i = 0; i < inputDocs.length; i++) {
            outputDocs[i] = lemmatize(inputDocs[i]);
        }
        return outputDocs;
    }

    public static String removeStopWords(String inputString) {
        String outputString = "";
        for (final String word : new WordIterator(inputString)) {
            if (StopWords.English.isStopWord(word) == false) {
                outputString += word + " ";
            }
        }
        return outputString;
    }

    public static String[] removeStopWords(String[] inputDocs) {
        String[] outputDocs = new String[inputDocs.length];
        for (int i = 0; i < inputDocs.length; i++) {
            outputDocs[i] = removeStopWords(inputDocs[i]);
        }
        return outputDocs;
    }

    public static String stem(String inputString) {
        String outputString = "";
        porterStemmer stemmer = new porterStemmer();
        String[] arrInput = inputString.split(" ");
        for (int j = 0; j < arrInput.length; j++) {
            stemmer.setCurrent(arrInput[j]);
            stemmer.stem();
            outputString += stemmer.getCurrent() + " ";
        }
        return outputString;
    }

    public static String[] stem(String[] inputDocs) {
        String[] outputDocs = new String[inputDocs.length];
        for (int i = 0; i < inputDocs.length; i++) {
            outputDocs[i] = stem(inputDocs[i]);
        }
        return outputDocs;
    }

    public static String preProcess(String inputString) {
        //buang spasi di depan atau belakang
        inputString = inputString.trim();

        //stop words
        inputString = removeStopWords(inputString);

        //normalization
        inputString = Normalization.formatString(inputString);

        //lematization
        inputString = lemmatize(inputString);

        //porter stemmer
        inputString = stem(inputString);

        return inputString;
    }
}