/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Irvan Hardyanto
 */
public class FXMLDocumentController implements Initializable {
    
    private TreeMap<String,ArrayList<String>> dictionary;
    private BooleanQuery bq;
    private CosineSimilarity cs;
    
    private LanguageModel lm;
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
    private void handleListViewClick(MouseEvent event){
        String name = (String)this.ListViewResult.getSelectionModel().getSelectedItem();
        //baca file nya
        
        String line="";
        String content= "";
        Label ta = new Label();
        try{
            File dir = new File("raw_dataset");
            File doc = new File(dir,name);
            BufferedReader br =  new BufferedReader(new FileReader(doc));
            while((line=br.readLine())!=null){
                content +=line+"\n";
            }
            br.close();
        }catch(FileNotFoundException  e){
            System.out.println("File not found!");
        }catch(IOException e){
            e.printStackTrace();
        }catch(NullPointerException e){
            
        }
        ta.setText(content);
        
        VBox box=  new VBox(ta);
        Stage fileContent = new Stage();
        box.setMargin(ta,new Insets(30,30,30,30));
        Scene scene=new Scene(box,370,350);
        fileContent.setTitle("YouRSearchEngine - "+name);
        fileContent.setScene(scene);
        fileContent.show();
        System.out.println(name);
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
    private void handleSearchButton(ActionEvent event) throws IOException{
        //Hasil boolean query : resul 
        long start = System.currentTimeMillis();
        
        //kosongkan list hasil pencarian, agar tidak saling tumpang tindih
        this.ListViewResult.getItems().clear();
        
        //dapatkan query user
        String text = this.TextFieldQuery.getText();
        
        //default mode or
        if(this.defaultMode){
            text = this.addBooleanOperators(text, false);
        }
        
        //periksa apakah query kosong atau tidak
        if(text.length()==0){
            this.LabelProcessingTime.setText("Error! query tidak boleh kosong!");
            return;
        }
        
        //cari dokumen yang mengandung term-term yang dicari
        ArrayList<String> result2 = bq.documentBooleanQuery(this.PreprocessQuery(text.trim()));
        
        //hitung cosine similarity pada dokumen hasil pencarian(?)
        CosineSimilarityResult[] cosineSimilarity = this.cs.ranking(text);
        
        String query = this.PreprocessQuery(text.trim());
        
        //hitung precision recall
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
        
        //language model
        this.lm.setQuery(Preprocessor.preProcess(text));
        double[] ranking = this.lm.calculateRanking(result2);
        
        TreeMap<Double,String> rank = this.lm.calculateRankingHashMap(result2);
        System.out.println("query = "+query);
        for(int i=0;i<ranking.length;i++)
        {
            System.out.println(ranking[i]);
        }
        long end = System.currentTimeMillis();
        
        //tampilkan hasil ke layar
        ObservableList<String> test = FXCollections.<String>observableArrayList(result2);
        this.ListViewResult.getItems().addAll(test);
        
        //tampilkan waktu yang dibutuhkan untuk memproses query
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
        
        //siapkan preprocessor
        Preprocessor.init();
        try{
            ObjectInputStream oi = new ObjectInputStream(new GZIPInputStream(new FileInputStream("inverted_index.dat")));
            
            //baca inverted index dari file
            this.dictionary  = ( TreeMap<String, ArrayList<String>>) oi.readObject();
            
            //siapkan boolean query
            this.bq = new BooleanQuery(this.dictionary);
            
            //siapkan cosine similarity
            this.cs = new CosineSimilarity(154, dictionary);
            this.cs.initialize();
            
            //siapkan language model
            this.lm = new LanguageModel();
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Creating dictionary +initialization takes: "+(end-start)*1.0/1000*1.0+" detik");
    }    
}
