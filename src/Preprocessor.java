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
import java.util.Properties;
import lib.normalization.Normalization;
import lib.porterstemmer.porterStemmer;
import lib.stopwords.StopWords;
import lib.stopwords.WordIterator;

public class Preprocessor{
    private static StanfordCoreNLP pipeline=null;
	
    public static void init(){
        Properties properties;
        String propertiesName = "tokenize, ssplit, pos, lemma";
        properties = new Properties();
        properties.setProperty("annotators", propertiesName);
        
        pipeline = new StanfordCoreNLP(properties);
    }
	
	//references: https://www.youtube.com/watch?v=nmwRpDuYeeE
	/**
	 * Melakukan lemmatization pada sebuah baris pada dokumen,dengan menggunakan stanfordCoreNLP API (https://stanfordnlp.github.io/CoreNLP/)
	 * @param inputString baris pada dokumen
	 * @return string dengan kata-kata dalam bentuk dasar.
	 **/
            public static String lemmatize(String inputString){
		StanfordCoreNLP stanfordCoreNLP = pipeline;
		boolean first=true;
		String processedLine,lemma;
		
		CoreDocument coreDocument = new CoreDocument(inputString);
		stanfordCoreNLP.annotate(coreDocument);
		List<CoreLabel> coreLabelList = coreDocument.tokens();
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
            if(!arrInput[j].substring(arrInput[j].length()-3,arrInput[j].length()).equalsIgnoreCase("ing")){
                stemmer.setCurrent(arrInput[j]);
                stemmer.stem();
                outputString += stemmer.getCurrent() + " ";
            }
            else{
                outputString += arrInput[j] + " ";
            }
            
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
    
    public static String preProcessWithoutLemmatization(String inputString) {
        //buang spasi di depan atau belakang
        inputString = inputString.trim();
        
        //stop words
        inputString = removeStopWords(inputString);

        //normalization
        inputString = Normalization.formatString(inputString);
        
        //porter stemmer
        inputString = stem(inputString);

        return inputString;
    }
}