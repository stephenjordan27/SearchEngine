/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
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
        this.ListViewResult.getItems().clear();
        String text = this.TextFieldQuery.getText();
        
        System.out.println("you searched: "+text);
        System.out.println("Dictionary size: "+this.dictionary.size());
        
        long start = System.currentTimeMillis();
        String query = Preprocessor.preProcess(text);
        ArrayList<String> result = null;
        if(query.length()>0){
            result = this.dictionary.get(query.trim());
        }
        System.out.println("query = "+query);
        if(result==null){
            System.out.println("warning result null");
            this.LabelProcessingTime.setText("Tidak ada hasil");
            return;
        }
        long end = System.currentTimeMillis();
        
        ObservableList<String> test = FXCollections.<String>observableArrayList(result);
        this.ListViewResult.getItems().addAll(test);
        String queryProcTime = String.format(" %.4f",(end-start)*1.0/1000*1.0);
        this.LabelProcessingTime.setText("Menampilkan "+result.size()+" hasil("+queryProcTime+" detik)");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        long start = System.currentTimeMillis();
        Preprocessor.init();
        try{
            ObjectInputStream oi = new ObjectInputStream(new GZIPInputStream(new FileInputStream("inverted_index.dat")));
            this.dictionary  = ( TreeMap<String, ArrayList<String>>) oi.readObject();
        }catch(IOException ex){
            System.out.println("e1");
            ex.printStackTrace();
        }catch (ClassNotFoundException ex){
            System.out.println("e2");
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Reading dictionary +initialization takes: "+(end-start));
    }    
   
}
