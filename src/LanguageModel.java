
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Frengki Ang(Kontributor Utama), dengan beberapa penyesuaian oleh Irvan H
 */
public class LanguageModel 
{
    private String query;
    private double lamda;
    private String direktori;
    private ArrayList<String> tempListDocs;
    private ArrayList<String> listDocs;
    private TreeMap<String,ArrayList<String>> dictionary; 
    
    public LanguageModel(TreeMap<String,ArrayList<String>> dictionary)
    {
        this.dictionary = dictionary;
        this.listDocs = new ArrayList<String>();
        this.tempListDocs = new ArrayList<String>();
        this.lamda = 0.25;
        this.direktori = "cleaned_dataset";
    }
    
    public void setQuery(String q)
    {
        this.query = q;
    }
    
    public TreeMap<Double,String> calculateRankingHashMap() throws FileNotFoundException, IOException{
        TreeMap<Double,String> ranking = new TreeMap<>(Collections.reverseOrder());
        double[] rank = this.calculateRanking();
        for(int i = 0;i < rank.length;i++){
            ranking.put(rank[i],listDocs.get(i));
        }
        return ranking;
    }
    
    public double[] calculateRanking() throws FileNotFoundException, IOException
    {
        String[] wordQuery = this.query.split("\\s+");
        double[] equation = new double[wordQuery.length];
        int totalDocsWords=0;
        double[] ranking = new double[listDocs.size()];
        for(int i=0;i<ranking.length;i++)
        {
            ranking[i] = 1;
        }
        
        //hitung total kata semua doc
        for(int i=0;i<listDocs.size();i++)
        {
            String fileName = listDocs.get(i);
            File file = new File(direktori + "\\" + fileName);
            totalDocsWords += hitungKataDoc(fileName);
        }
        
        for(int j=0;j<listDocs.size();j++)
        {
            String fileName = listDocs.get(j);
            double jumlahKataQueryDiDocYangDiBanding = 0;
            double jumlahKataQueryDiDoc = 0;
            double jumlahKataQueryDiSemuaDoc = 0;
            double jumlahKataDoc = 0;
            for(int i=0;i<wordQuery.length;i++)
            {
                jumlahKataQueryDiDocYangDiBanding = hitungHitKataQueryDiDoc(fileName, wordQuery[i]);
                jumlahKataDoc = hitungKataDoc(fileName);
                jumlahKataQueryDiSemuaDoc += jumlahKataQueryDiDocYangDiBanding;
                for(int k=0;k<listDocs.size();k++)
                {
                    if(k!=j)
                    {
                        fileName = listDocs.get(k);
                        jumlahKataQueryDiDoc = hitungHitKataQueryDiDoc(fileName, wordQuery[i]);
                        jumlahKataQueryDiSemuaDoc += jumlahKataQueryDiDoc;
                    }
                }
                equation[i] = (lamda * jumlahKataQueryDiDocYangDiBanding/jumlahKataDoc) + ((1-lamda) * jumlahKataQueryDiSemuaDoc/(double)totalDocsWords);
            }  
            for(int i=0;i<equation.length;i++)
            {
                ranking[j] *= equation[i];
            }
        }
        
        return ranking;
    }
    
    public void cariDocYangMemilikiKataQuery()
    {
        String[] wordQuery = this.query.split("\\s+");
        for(int i = 0;i<wordQuery.length;i++)
        {
            this.tempListDocs = this.dictionary.get(wordQuery[i]);
            for(int j=0;j<this.tempListDocs.size();j++)
            {
                this.listDocs.add(this.tempListDocs.get(j));
            }
        }
    }
    
    public int hitungKataDoc(String namaFile) throws FileNotFoundException, IOException
    {
        int count = 0;
        BufferedReader br;
        br = new BufferedReader(new FileReader(direktori + "\\" + namaFile));
        String line = br.readLine();
        while(line!=null)
        {
            String[] word = line.split(" ");
            for(int j=0;j<word.length;j++)
            {
                count++;
            }
            line = br.readLine();
        }
        return count;
    }
    
    public int hitungHitKataQueryDiDoc(String namaFile, String kata) throws FileNotFoundException, IOException
    {
        int count = 0;
        BufferedReader br;
        br = new BufferedReader(new FileReader(direktori + "\\" + namaFile));
        String line = br.readLine();
        while(line!=null)
        {
            String[] word = line.split(" ");
            for(int j=0;j<word.length;j++)
            {
                if(word[j].equals(kata))
                {
                    count++;
                }
            }
            line = br.readLine();
        }
        return count;
    }
}
