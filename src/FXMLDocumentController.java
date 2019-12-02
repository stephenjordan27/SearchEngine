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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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

    private TreeMap<String, ArrayList<String>> dictionary;
    private BooleanQuery bq;
    private CosineSimilarity cs;
    private CosineSimilarityResult[] cosineSimilarity;
    private ArrayList<String> resultLM;
    private long startCS = 0, endCS = 0, startLM = 0, endLM = 0;
    private BM25 bm25;
    private final int sumOfDocument = 154;

    private LanguageModel lm;
    private boolean isAnd = false, defaultMode = true, isTop5 = true;

    private ToggleGroup rankingMethod;
    @FXML
    private Label label, LabelProcessingTime, retrievedRelevant, retrievedNonRelevant, NotRetrievedRelevant, NotRetrievedNonRelevant, Precision, Recall;

    @FXML
    private RadioButton RadioBtnTop5, RadioBtnTop10, radioButtonCS, radioButtonLM;

    @FXML
    private Button BtnSearch, BtnSubmit, btnAnd, btnOr, btnResetQuery;

    @FXML
    private TextField TextFieldQuery, TextFieldGoldenAnswer, TextThreshold;

    @FXML
    private ListView ListViewResult;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    @FXML
    private void handleCSRadioButton(MouseEvent event) {
        this.showCS();
    }

    private void showCS() {
        this.ListViewResult.getItems().clear();

        System.out.println("Metode yang dipilih: Cosine Similarity");
        ArrayList<String> resultCS = new ArrayList();
        
        double thresholdValue = 0;
        if(!TextThreshold.getText().isEmpty()){
             thresholdValue = Double.parseDouble(TextThreshold.getText());
        }   
        for (CosineSimilarityResult res : this.cosineSimilarity) {
            if (TextThreshold.getText() != null && res.getResult() >= thresholdValue) {
                resultCS.add(String.format("%.3f", res.getResult()) + "\t" + res.getDocumentName());
            } else if (TextThreshold.getText() == null) {
                resultCS.add(String.format("%.3f", res.getResult()) + "\t" + res.getDocumentName());
            }
        }
        ObservableList<String> test;
        if (this.isTop5) {
            if (resultCS.size() >= 5) {
                test = FXCollections.<String>observableArrayList(resultCS.subList(0, 5));
            } else {
                test = FXCollections.<String>observableArrayList(resultCS);
            }
        } else {
            test = FXCollections.<String>observableArrayList(resultCS.subList(0, 5));
        }
        this.ListViewResult.getItems().addAll(test);
        this.LabelProcessingTime.setText("Menampilkan " + resultCS.size() + " hasil dengan ranking Cosine Similarity (" + (this.endCS - this.startCS) * 1.0 / 1000 * 1.0 + " detik)");
        this.defaultMode = true;
    }

    @FXML
    private void handleLMRadioButton(MouseEvent event) {
        //kosongkan list hasil pencarian, agar tidak saling tumpang tindih
        this.ListViewResult.getItems().clear();

        System.out.println("Metode yang dipilih: Language Model");

        ObservableList<String> test;
        if (this.isTop5) {
            if (this.resultLM.size() >= 5) {
                test = FXCollections.<String>observableArrayList(this.resultLM.subList(0, 5));
            } else {
                test = FXCollections.<String>observableArrayList(this.resultLM);
            }
        } else {
            test = FXCollections.<String>observableArrayList(this.resultLM.subList(0, 10));
        }
        for (int i = 0; i < test.size(); i++) {
            String[] arrOfTest = test.get(i).split("\t");
            double thresholdValue = 0;
            double rankingValue = Double.parseDouble(arrOfTest[0].replace(",", "."));
            if(!TextThreshold.getText().isEmpty()){
                 thresholdValue = Double.parseDouble(TextThreshold.getText());
            }   
            if (TextThreshold.getText() != null && rankingValue >= thresholdValue) {
                this.ListViewResult.getItems().add(test.get(i));
            } else if (TextThreshold.getText() == null) {
                this.ListViewResult.getItems().addAll(test);
                break;
            }

        }
//        this.ListViewResult.getItems().addAll(test);
        this.LabelProcessingTime.setText("Menampilkan " + this.resultLM.size() + " hasil dengan ranking Language Model (" + (endLM - startLM) * 1.0 / 1000 * 1.0 + " detik)");
        this.defaultMode = true;
    }

    @FXML
    private void handleTop5RadioButton(MouseEvent event) {
        this.isTop5 = true;
        this.ListViewResult.getItems().clear();
        if (this.rankingMethod.getSelectedToggle().equals(this.radioButtonCS)) {
            ArrayList<String> resultCS = new ArrayList();
            for (CosineSimilarityResult res : this.cosineSimilarity) {
                if (res.getResult() != 0.0) {
                    resultCS.add(String.format("%.3f", res.getResult()) + "\t" + res.getDocumentName());
                }
            }
            ObservableList<String> test;
            if (resultCS.size() >= 5) {
                test = FXCollections.<String>observableArrayList(resultCS.subList(0, 5));
            } else {
                test = FXCollections.<String>observableArrayList(resultCS);
            }
            this.ListViewResult.getItems().addAll(test);
        } else if (this.rankingMethod.getSelectedToggle().equals(this.radioButtonLM)) {
            ObservableList<String> test;
            if (this.resultLM.size() >= 5) {
                test = FXCollections.<String>observableArrayList(this.resultLM.subList(0, 5));
            } else {
                test = FXCollections.<String>observableArrayList(this.resultLM);
            }
            this.ListViewResult.getItems().addAll(test);
        }
    }

    @FXML
    private void handleTop10RadioButton(MouseEvent event) {
        this.isTop5 = false;
        this.ListViewResult.getItems().clear();
        if (this.rankingMethod.getSelectedToggle().equals(this.radioButtonCS)) {
            ArrayList<String> resultCS = new ArrayList();
            for (CosineSimilarityResult res : this.cosineSimilarity) {
                if (res.getResult() != 0.0) {
                    resultCS.add(String.format("%.3f", res.getResult()) + "\t" + res.getDocumentName());
                }
            }
            ObservableList<String> test;
            if (resultCS.size() >= 10) {
                test = FXCollections.<String>observableArrayList(resultCS.subList(0, 10));
            } else {
                test = FXCollections.<String>observableArrayList(resultCS);
            }
            this.ListViewResult.getItems().addAll(test);
        } else if (this.rankingMethod.getSelectedToggle().equals(this.radioButtonLM)) {
            ObservableList<String> test;
            if (this.resultLM.size() >= 10) {
                test = FXCollections.<String>observableArrayList(this.resultLM.subList(0, 10));
            } else {
                test = FXCollections.<String>observableArrayList(this.resultLM);
            }
            this.ListViewResult.getItems().addAll(test);
        }
    }

    @FXML
    private void handleListViewClick(MouseEvent event) {
        String[] row = ((String) this.ListViewResult.getSelectionModel().getSelectedItem()).split("\t");
        String name = row[1];
        //baca file nya

        String line = "";
        String content = "";
        Label ta = new Label();
        try {
            File dir = new File("raw_dataset");
            File doc = new File(dir, name);
            BufferedReader br = new BufferedReader(new FileReader(doc));
            while ((line = br.readLine()) != null) {
                content += line + "\n";
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
        ta.setText(content);

        VBox box = new VBox(ta);
        Stage fileContent = new Stage();
        box.setMargin(ta, new Insets(30, 30, 30, 30));
        Scene scene = new Scene(box, 370, 350);
        fileContent.setTitle("YouRSearchEngine - " + name);
        fileContent.setScene(scene);
        fileContent.show();
        System.out.println(name);
    }

    @FXML
    private void handleORButton(ActionEvent event) {
        this.isAnd = false;
        this.defaultMode = false;
        String text = this.TextFieldQuery.getText();
        text = this.addBooleanOperators(text, isAnd);
        this.TextFieldQuery.setText(text);
    }

    @FXML
    private void handleANDButton(ActionEvent event) {
        this.isAnd = true;
        this.defaultMode = false;
        String text = this.TextFieldQuery.getText();
        text = this.addBooleanOperators(text, isAnd);
        this.TextFieldQuery.setText(text);
    }

    @FXML
    private void handleTextFieldQuery(ActionEvent event) {
        this.LabelProcessingTime.setText("");
    }

    @FXML
    private void handleResetButton(ActionEvent event) {
        String text = this.TextFieldQuery.getText();
        text = text.replaceAll(" and ", " ");
        text = text.replaceAll(" or ", " ");
        this.TextFieldQuery.setText(text);
        this.defaultMode = true;
    }

    @FXML
    private void handleSubmitButton(ActionEvent event) {
        String text = this.TextFieldGoldenAnswer.getText();
        String[] goldenAnswer = text.split(" ");
        ArrayList<String> displayResult = new ArrayList<String>();
        ObservableList obj = this.ListViewResult.getItems();
        for (int i = 0; i < obj.size(); i++) {
            String[] queryResult = ((String) obj.get(i)).split("\t");
            displayResult.add(queryResult[1]);

        }
        PrecisionRecall pr = new PrecisionRecall(sumOfDocument, goldenAnswer, displayResult);
        pr.generatePrecisionRecallVariable();
        retrievedRelevant.setText(pr.getTp() + "");
        retrievedNonRelevant.setText(pr.getFp() + "");
        NotRetrievedRelevant.setText(pr.getFn() + "");
        NotRetrievedNonRelevant.setText(pr.getTn() + "");
        Precision.setText(String.format("%.2f", pr.precision()) + "");
        Recall.setText(String.format("%.2f", pr.recall()) + "");
    }

    //References: https://examples.javacodegeeks.com/desktop-java/javafx/listview-javafx/javafx-listview-example/
    @FXML
    private void handleSearchButton(ActionEvent event) throws IOException {
        //Hasil boolean query : resul 
        long start = System.currentTimeMillis();

        //dapatkan query user
        String text = this.TextFieldQuery.getText();

        //default mode or
        if (this.defaultMode) {
            text = this.addBooleanOperators(text, false);
        }

        //periksa apakah query kosong atau tidak
        if (text.length() == 0) {
            this.LabelProcessingTime.setText("Error! query tidak boleh kosong!");
            return;
        }

        String cleanQuery = Preprocessor.preProcess(text);
        //cari dokumen yang mengandung term-term yang dicari
        ArrayList<String> result2 = bq.documentBooleanQuery(this.PreprocessQuery(text.trim()));

        //hitung cosine similarity pada dokumen hasil pencarian(?)
        this.startCS = System.currentTimeMillis();
        CosineSimilarityResult[] cosineSimilarity = this.cs.ranking(cleanQuery);
        this.endCS = System.currentTimeMillis();
        Arrays.sort(cosineSimilarity);
        String query = this.PreprocessQuery(text.trim());

        if (result2 == null) {
            System.out.println("warning result null");
            this.LabelProcessingTime.setText("Tidak ada hasil");
            return;
        }

        //language model
        this.lm.setQuery(cleanQuery);
        double[] ranking = this.lm.calculateRanking(result2);

        this.startLM = System.currentTimeMillis();
        TreeMap<Double, String> rank = this.lm.calculateRankingHashMap(result2);
        this.endLM = System.currentTimeMillis();

        System.out.println("query = " + query);
//        for(int i=0;i<ranking.length;i++)
//        {
//            System.out.println(ranking[i]);
//        }
//        long end = System.currentTimeMillis();

        //BM-25
        long startBM25 = System.currentTimeMillis();

        long endBM25 = System.currentTimeMillis();

        this.cosineSimilarity = cosineSimilarity;

        this.resultLM = new ArrayList();
        ArrayList<String> resultLM = new ArrayList();

        Iterator it = rank.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            resultLM.add(String.format("%.7f", pair.getKey()) + "\t" + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        this.resultLM = resultLM;
        this.showCS();
        this.defaultMode = true;
    }

    private String addBooleanOperators(String input, boolean isAnd) {
        String output = "";
        input = input.trim();
        if (isAnd) {
            output = input.replaceAll(" ", " and ");
        } else {
            output = input.replaceAll(" ", " or ");
        }
        return output;
    }

    private String PreprocessQuery(String query) {
        String output = "";
        String[] words = query.split(" ");
        for (String word : words) {
            if (word.equals("and") || word.equals("or") || word.equals("not")) {
                output += " " + word;
            } else {
                output += " " + Preprocessor.preProcess(word).trim();
            }
        }
        return output.trim();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup tg = new ToggleGroup();
//        this.RadioBtnTop10.setToggleGroup(tg);
//        this.RadioBtnTop5.setToggleGroup(tg);
//        this.RadioBtnTop5.setSelected(true);

        long start = System.currentTimeMillis();
        System.out.println(this.radioButtonCS == null);
        this.rankingMethod = new ToggleGroup();
        this.radioButtonCS.setToggleGroup(this.rankingMethod);
        this.radioButtonLM.setToggleGroup(this.rankingMethod);
//        this.radioButtonBM25.setToggleGroup(this.rankingMethod);

        this.radioButtonCS.setSelected(true);
        //siapkan preprocessor
        Preprocessor.init();
        try {
            ObjectInputStream oi = new ObjectInputStream(new GZIPInputStream(new FileInputStream("inverted_index.dat")));

            //baca inverted index dari file
            this.dictionary = (TreeMap<String, ArrayList<String>>) oi.readObject();

            //siapkan boolean query
            this.bq = new BooleanQuery(this.dictionary);

            //siapkan cosine similarity
            this.cs = new CosineSimilarity(154, dictionary);
            this.cs.initialize();

            //siapkan language model
            this.lm = new LanguageModel();

            this.bm25 = new BM25(dictionary);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Creating dictionary +initialization takes: " + (end - start) * 1.0 / 1000 * 1.0 + " detik");
    }
}
