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
    private boolean isAnd = false,defaultMode=true;
    @FXML
    private Label label,LabelProcessingTime;
    
    @FXML 
    private Button BtnSearch,btnAnd,btnOr,btnResetQuery;
    
    @FXML
    private TextField TextFieldQuery;
    
    @FXML
    private ListView ListViewResult;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @FXML
    private void handleORButton(ActionEvent event){
        this.isAnd =false;
        this.defaultMode = false;
        String text = this.TextFieldQuery.getText();
        text = this.addBooleanOperators(text, isAnd);
        this.TextFieldQuery.setText(text);
    }
    
    @FXML
    private void handleANDButton(ActionEvent event){
        this.isAnd = true;
        this.defaultMode =false;
        String text = this.TextFieldQuery.getText();
        text = this.addBooleanOperators(text, isAnd);
        this.TextFieldQuery.setText(text);
    }
    
    @FXML
    private void handleTextFieldQuery(ActionEvent event){
        this.LabelProcessingTime.setText("");
    }
    
    @FXML
    private void handleResetButton(ActionEvent event){
        String text = this.TextFieldQuery.getText();
        text = text.replaceAll(" and "," ");
        text = text.replaceAll(" or "," ");
        this.TextFieldQuery.setText(text);
        this.defaultMode = true;
    }
    
    //References: https://examples.javacodegeeks.com/desktop-java/javafx/listview-javafx/javafx-listview-example/
    @FXML
    private void handleSearchButton(ActionEvent event){
        //Hasil boolean query : resul        
        this.ListViewResult.getItems().clear();
        String text = this.TextFieldQuery.getText();
        if(this.defaultMode){
            text = this.addBooleanOperators(text, false);
        }
        if(text.length()==0){
            this.LabelProcessingTime.setText("Error! query tidak boleh kosong!");
            return;
        }
        ArrayList<String> result2 = bq.documentBooleanQuery(this.PreprocessQuery(text.trim()));
        long start = System.currentTimeMillis();
        String query = this.PreprocessQuery(text.trim());
        
        PrecisionRecallCalculator calculator = new PrecisionRecallCalculator("asd", 7);
        SearchResults search = new SearchResults(result2,7);
        calculator.calculate(search);
        calculator.calculateAveragePrecision();
        int sasd = calculator.getRelevantDocumentsFound();
        
        System.out.println("query = "+query);
        if(result2==null){
            System.out.println("warning result null");
            this.LabelProcessingTime.setText("Tidak ada hasil");
            return;
        }
        long end = System.currentTimeMillis();
        
        ObservableList<String> test = FXCollections.<String>observableArrayList(result2);
        this.ListViewResult.getItems().addAll(test);
        this.LabelProcessingTime.setText("Menampilkan "+result2.size()+" hasil("+(end-start)*1.0/1000*1.0+" detik)");
        this.defaultMode = true;
    }
    
    private String addBooleanOperators(String input, boolean isAnd){
        String output="";
        input = input.trim();
        if(isAnd){
            output = input.replaceAll(" ", " and ");
        }else{
            output = input.replaceAll(" ", " or ");
        }
        return output;
    }
    
    private String PreprocessQuery(String query){
        String output="";
        String[] words = query.split(" ");
        for(String word: words){
            if(word.equals("and")||word.equals("or")||word.equals("not")){
                output += " "+word;           
            }else{
                output += " "+Preprocessor.preProcess(word).trim();
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
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Creating dictionary +initialization takes: "+(end-start)*1.0/1000*1.0+" detik");
    }    
   
}
