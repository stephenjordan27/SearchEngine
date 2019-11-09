/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javax.naming.directory.SearchResult;

/**
 *
 * @author Irvan Hardyanto
 */
public class FXMLDocumentController implements Initializable {
    
    private TreeMap<String,ArrayList<String>> dictionary = new TreeMap<String,ArrayList<String>>(new Comparator<String>() {
                public int compare(String s1, String s2) {
                    return s1.compareTo((s2));
                }
            });
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
    private void handleSearchButton(ActionEvent event){
        //Hasil boolean query : resul
        BooleanQuery bq = new BooleanQuery(dictionary, "fairest and time and not rose");
        ArrayList<String> result2 = bq.documentBooleanQuery();
        
        PrecisionRecallCalculator calculator = new PrecisionRecallCalculator("asd", 7);
        SearchResults search = new SearchResults(result2,154);
        calculator.calculate(search);
        calculator.calculateAveragePrecision();
        int sasd = calculator.getRelevantDocumentsFound();
        
        this.ListViewResult.getItems().clear();
        String text = this.TextFieldQuery.getText();
        System.out.println("you searched: "+text);
        System.out.println("Dictionary size: "+this.dictionary.size());
        
        long start = System.currentTimeMillis();
        String query = Preprocessor.preProcess(text);
        ArrayList<String> result = this.dictionary.get(query.trim());

        System.out.println("query = "+query);
        if(result==null){
            System.out.println("warning result null");
            this.LabelProcessingTime.setText("Tidak ada hasil");
            return;
        }
        long end = System.currentTimeMillis();
        
        ObservableList<String> test = FXCollections.<String>observableArrayList(result);
        this.ListViewResult.getItems().addAll(test);
        this.LabelProcessingTime.setText("Menampilkan "+result.size()+" hasil("+(end-start)/100+" detik)");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        String dir="raw_dataset";
        String outDir="preprocessed_dataset";
        
        File f =new File(dir);
        File fout=new File(outDir);
        //Preprocessor.preProcess(f, fout);
        long start = System.currentTimeMillis();
        this.dictionary = Preprocessor.createDictionary(MyUtils.listFilesForFolder(f));
        long end = System.currentTimeMillis();
        System.out.println("Creating dictionary takes: "+(end-start));
    }    
   
}
