
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author asus a455l
 */
public class LanguageModel 
{
    private TreeMap<String,ArrayList<String>> dictionary; 
    private String query;
    private ArrayList<String> tempListDocs;
    private ArrayList<String> listDocs;
    private double lamda;
    private int totalDocsWords;
    private String direktori;
    
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
    
    public double[] calculateRanking() throws FileNotFoundException, IOException
    {
        String[] wordQuery = this.query.split("\\s+");
        double[] equation = new double[wordQuery.length];
        
        cariDocYangMemilikiKataQuery();
        double[] ranking = new double[this.listDocs.size()];
        for(int i=0;i<ranking.length;i++)
        {
            ranking[i] = 1;
        }
        
        //hitung total kata semua doc
        for(int i=0;i<this.listDocs.size();i++)
        {
            String fileName = this.listDocs.get(i);
            File file = new File(direktori + "\\" + fileName);
            this.totalDocsWords += hitungKataDoc(fileName);
        }
        
        for(int j=0;j<this.listDocs.size();j++)
        {
            String fileName = this.listDocs.get(j);
            double jumlahKataQueryDiDocYangDiBanding = 0;
            double jumlahKataQueryDiDoc = 0;
            double jumlahKataQueryDiSemuaDoc = 0;
            double jumlahKataDoc = 0;
            for(int i=0;i<wordQuery.length;i++)
            {
                jumlahKataQueryDiDocYangDiBanding = hitungHitKataQueryDiDoc(fileName, wordQuery[i]);
                jumlahKataDoc = hitungKataDoc(fileName);
                jumlahKataQueryDiSemuaDoc += jumlahKataQueryDiDocYangDiBanding;
                for(int k=0;k<this.listDocs.size();k++)
                {
                    if(k!=j)
                    {
                        jumlahKataQueryDiDoc = hitungHitKataQueryDiDoc(fileName, wordQuery[i]);
                        jumlahKataQueryDiSemuaDoc += jumlahKataQueryDiDoc;
                    }
                }
                equation[i] = (lamda * jumlahKataQueryDiDocYangDiBanding/jumlahKataDoc) + ((1-lamda) * jumlahKataQueryDiSemuaDoc/(double)this.totalDocsWords);
            }  
            for(int i=0;i<equation.length;i++)
            {
                ranking[j] *= equation[i];
            }
        }
        
        return ranking;
    }
    
    public void clear()
    {
        this.listDocs.clear();
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
