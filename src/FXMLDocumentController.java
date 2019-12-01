/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 *
 * @author Irvan Hardyanto
 */
public class FXMLDocumentController implements Initializable {
    
    private TreeMap<String,ArrayList<String>> dictionary;
    private BooleanQuery bq;
    private BM25 bm;
    private int[][] bagOfWords;
    private LanguageModel lm;
    @FXML
    private Label label,LabelProcessingTime;
    
    @FXML 
    private Button BtnSearch;
    
    @FXML
    private TextField TextFieldQuery;
    
    @FXML
    private ListView ListViewResult;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    //References: https://examples.javacodegeeks.com/desktop-java/javafx/listview-javafx/javafx-listview-example/
    @FXML
    private void handleSearchButton(ActionEvent event) throws IOException{
        //Hasil boolean query : resul        
        this.ListViewResult.getItems().clear();
        String text = this.TextFieldQuery.getText();
        ArrayList<String> result2 = bq.documentBooleanQuery(this.PreprocessQuery(text.trim()));
        long start = System.currentTimeMillis();
        String query = this.PreprocessQuery(text.trim());
        
        PrecisionRecallCalculator calculator = new PrecisionRecallCalculator("asd", 7);
        SearchResults search = new SearchResults(result2,7);
        calculator.calculate(search);
        calculator.calculateAveragePrecision();
        int sasd = calculator.getRelevantDocumentsFound();
        
        if(result2==null){
            System.out.println("warning result null");
            this.LabelProcessingTime.setText("Tidak ada hasil");
            return;
        }
        long end = System.currentTimeMillis();
        
        this.lm.setQuery(query);
        double[] ranking = this.lm.calculateRanking();
        
        System.out.println("query = "+query);
        for(int i=0;i<ranking.length;i++)
        {
            System.out.println(ranking[i]);
        }
        this.lm.clear();
        
        ObservableList<String> test = FXCollections.<String>observableArrayList(result2);
        this.ListViewResult.getItems().addAll(test);
        this.LabelProcessingTime.setText("Menampilkan "+result2.size()+" hasil("+(end-start)*1.0/1000*1.0+" detik)");
    }
    
    private String PreprocessQuery(String query){
        String output="";
        String[] words = query.split(" ");
        for(String word: words){
            if(word.equals("and")||word.equals("or")||word.equals("not")){
                output += " "+word;           
            }else{
                output += " "+Preprocessor.preProcess(word);
            }
        }
        return output.trim();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        long start = System.currentTimeMillis();
        Preprocessor.init();
        try{
            ObjectInputStream oi = new ObjectInputStream(new GZIPInputStream(new FileInputStream("inverted_index.dat")));
            this.dictionary  = ( TreeMap<String, ArrayList<String>>) oi.readObject();
            this.bq = new BooleanQuery(this.dictionary);
            this.bm = new BM25(this.dictionary);
            this.bagOfWords = this.bm.initiateBagOfWords();
            this.lm = new LanguageModel(this.dictionary);
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Creating dictionary +initialization takes: "+(end-start)*1.0/1000*1.0+" detik");
    }    

    public TreeMap<String, ArrayList<String>> getDictionary() {
        return dictionary;
    }
}
