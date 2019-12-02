

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Mohamed Guendouz
 */
public class CosineSimilarity {
    
    private static TreeMap<String,ArrayList<String>> dictionary;
    private TreeMap<String,Integer> df,df_query,tf_query;
    private TreeMap<String,int[]> tf;
    private TreeMap<String,double[]> tf_idf;
    private TreeMap<String,Double> tf_idf_query,idf_query,idf,squareQuery;
    private TreeMap<String,double[]> dotProductAllDocuments,squareAllDocuments;
    private double[] squareRootAllDocuments,sumDotProductAllDocuments;
    private CosineSimilarityResult[] cosineSimilarity;
    private double squareRootQuery;
    private int sumOfDocument;


    public CosineSimilarity(int sumOfDocument,TreeMap<String,ArrayList<String>> dictionary){
        this.dictionary = dictionary;
        this.sumOfDocument = sumOfDocument;
        
        this.tf = new TreeMap<String,int[]>();
        this.idf = new TreeMap<String,Double>();
        this.df = new TreeMap<String,Integer>();
        this.tf_idf = new TreeMap<String,double[]>();
        this.dotProductAllDocuments = new TreeMap<String,double[]>();
        this.sumDotProductAllDocuments = new double[sumOfDocument];
        this.squareAllDocuments = new TreeMap<String,double[]>();
        this.squareRootAllDocuments = new double[sumOfDocument];
        
        this.tf_query = new TreeMap<String,Integer>();
        this.df_query = new TreeMap<String,Integer>();
        this.idf_query = new TreeMap<String,Double>();
        this.tf_idf_query = new TreeMap<String,Double>();
        this.squareQuery = new TreeMap<String,Double>();
        this.squareRootQuery = 0;
        this.dotProductAllDocuments = new TreeMap<String,double[]>();
        this.sumDotProductAllDocuments = new double[sumOfDocument];
        this.cosineSimilarity = new CosineSimilarityResult[sumOfDocument];
    }
    
    // When initialize document 
    public void initialize(){
        this.generateTF();
        this.generateDF();
        this.generateIDF();
        this.generateTFIDF();
        this.generateSquareAllDocuments();
        this.generateSquareRootAllDocuments();
    }
    
    public CosineSimilarityResult[] ranking(String query){
        this.generateTFQuery(query);
        this.generateDFQuery();
        this.generateIDFQuery();
        this.generateTFIDFQuery();
        this.generateSquareQuery();
        this.generateSquareRootQuery();
        this.generateDotProductAllDocuments();
        this.generateSumDotProductAllDocuments();
        this.generateCosineSimilarity();
        Arrays.sort(cosineSimilarity);
        return this.cosineSimilarity;
    }
    
    public void generateTF(){
        for(Map.Entry<String,ArrayList<String>> entry : dictionary.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            int[] arrOfDocumentNumber = new int[sumOfDocument];
            for(String document_name:value){
                int document_number = Integer.parseInt(document_name.substring(3,6));
                arrOfDocumentNumber[document_number-1] = 1;
            }
            tf.put(key, arrOfDocumentNumber);
         }
    }
    
    public void generateDF(){
        for(Map.Entry<String,int[]> entry : tf.entrySet()) {
            String key = entry.getKey();
            int[] value = entry.getValue();
            int sumOfDocumentFrequency = 0;
            for(int document_frequency:value){
                sumOfDocumentFrequency+=document_frequency;
            }
            df.put(key, sumOfDocumentFrequency);
        }
    }
    
    public void generateIDF(){
        for(Map.Entry<String,ArrayList<String>> entry : dictionary.entrySet()) {
            String key = entry.getKey();
            idf.put(key,Math.log(sumOfDocument/df.get(key)));
        }
    }
    
    public void generateTFIDF(){
        for(Map.Entry<String,int[]> entry : tf.entrySet()) {
            String key = entry.getKey();
            int[] value = entry.getValue();
            double[] arrOfTFIDF = new double[sumOfDocument];
            for (int i = 0; i < arrOfTFIDF.length; i++) {
                arrOfTFIDF[i] = value[i]*idf.get(key);
            }
            tf_idf.put(key, arrOfTFIDF);
        }
    }
    
    
    // When doing searching
    public void generateTFQuery(String query){
        String[] arrOfWord = query.split(" ");
        int i = 0 ;
        for(Map.Entry<String,ArrayList<String>> entry : dictionary.entrySet()) {
            String key = entry.getKey();
            int value = 0;
            for(String word:arrOfWord){
                if(key.equalsIgnoreCase(word)){
                    value = 1;
                    break;
                }
            }
            tf_query.put(key, value);
         }
    }
    
    public void generateDFQuery(){
        for(Map.Entry<String,Integer> entry : df.entrySet()) {
            String key = entry.getKey();
            int valueFromDF = entry.getValue();
            int valueFromDFQuery = tf_query.get(key);
            int value = valueFromDF + valueFromDFQuery;
            df_query.put(key, value);
        }
    }
    
    public void generateIDFQuery(){
        for(Map.Entry<String,ArrayList<String>> entry : dictionary.entrySet()) {
            String key = entry.getKey();
            idf_query.put(key,Math.log(sumOfDocument/df_query.get(key)));
        }
    }
    
    public void generateTFIDFQuery(){
        for(Map.Entry<String,Integer> entry : tf_query.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            tf_idf_query.put(key,value*idf_query.get(key));
        }
    }
    
    // Cosine Similarity
    public void generateDotProductAllDocuments(){
        for(Map.Entry<String,double[]> entry : tf_idf.entrySet()) {
            String key = entry.getKey();
            double[] value = entry.getValue();
            double[] result = new double[sumOfDocument];
            for (int i = 0; i < result.length; i++) {
                result[i] = value[i]*tf_idf_query.get(key);
            }
            dotProductAllDocuments.put(key,result);
        }
    }
    
    public void generateSquareAllDocuments(){
        for(Map.Entry<String,double[]> entry : tf_idf.entrySet()) {
            String key = entry.getKey();
            double[] value = entry.getValue();
            double[] result = new double[sumOfDocument];
            for (int i = 0; i < result.length; i++) {
                result[i] = Math.pow(value[i],2);
            }
            squareAllDocuments.put(key, result);
        }
    }
    
    public void generateSquareQuery(){
        for(Map.Entry<String,Double> entry : tf_idf_query.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            squareQuery.put(key, Math.pow(value, 2));
        }
    }
    
    public void generateSquareRootAllDocuments(){
        for (int i = 0; i < sumOfDocument; i++) {
            double sum = 0;
            for(Map.Entry<String,double[]> entry : squareAllDocuments.entrySet()) {
                sum+= entry.getValue()[i];
            }
            squareRootAllDocuments[i]= Math.sqrt(sum);
        }
    }
    
    public void generateSquareRootQuery(){
        double sum = 0;
        for(Map.Entry<String,Double> entry : squareQuery.entrySet()) {
            sum+= entry.getValue();
        }
        squareRootQuery = Math.sqrt(sum);
    }
    
    public void generateSumDotProductAllDocuments(){
        for (int i = 0; i < sumOfDocument; i++) {
            double sum = 0;
            for(Map.Entry<String,double[]> entry : dotProductAllDocuments.entrySet()) {
                sum += entry.getValue()[i];
            }
            sumDotProductAllDocuments[i] = sum;
        }
    }
    
    public void generateCosineSimilarity(){
        for (int i = 0; i < sumOfDocument; i++) {
            cosineSimilarity[i]= new CosineSimilarityResult(i+1,sumDotProductAllDocuments[i]/(squareRootAllDocuments[i]*squareRootQuery));
        }
    }
}

class CosineSimilarityResult implements Comparable<CosineSimilarityResult>{
    private String documentName;
    private double result;

    public CosineSimilarityResult(int documentNumber, double result) {
        if(documentNumber<=9){
            this.documentName = "Doc00"+documentNumber+".txt";
        }
        else if(documentNumber<=99){
            this.documentName = "Doc0"+documentNumber+".txt";
        }
        else{
            this.documentName = "Doc"+documentNumber+".txt";
        }
        this.result = result;
    }

    @Override
    public int compareTo(CosineSimilarityResult obj) {
        if(this.result>=obj.result){
            return -1;
        }
        else{
            return 1;
        }
    }
    
    public String getDocumentName(){
        return this.documentName;
    }
    
    
    
}

