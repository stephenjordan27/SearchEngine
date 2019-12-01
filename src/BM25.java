
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
public class BM25 
{
    private TreeMap<String,ArrayList<String>> dictionary;
    private ArrayList<File> files;
    private int[][] bagOfWords;
    
    public BM25(TreeMap<String,ArrayList<String>> dictionary)
    {
        this.dictionary = dictionary;
        this.bagOfWords = new int[this.dictionary.size()][154];
    }
  
    public ArrayList<File> listFilesForFolder(File folder){
	ArrayList<File> files = new ArrayList<File>();
	//menggunakan listFiles() biar langsung dapet objek File nya,
	//kalau list() hanya string nama file nya saja.
	for(File f : folder.listFiles())
        {
            if(f.isDirectory())
            {
		listFilesForFolder(f);
            }
            else
            {
		files.add(f);
		//System.out.println(f.getName());
            }
        }
	return files;
    }
    
    public int[][] initiateBagOfWords() throws FileNotFoundException, IOException
    {
        File file = new File("cleaned_dataset");
        File[] files = file.listFiles();
        for(int i=0;i<files.length;i++)
        {
            BufferedReader br;
            br = new BufferedReader(new FileReader(files[i]));
            String line = br.readLine();
            while(line!=null)
            {
                String[] word = line.split(" ");
                for(int j=0;j<word.length;j++)
                {
                    ArrayList<String> listDoc = this.dictionary.get(word[j]);
                    if(listDoc!=null)
                    {
                        for(int k=0;k<listDoc.size();k++)
                        {
                            String temp = listDoc.get(k);
                            int idx = 3;
                            while(temp.charAt(idx)!='.')
                            {
                                idx++;
                            }
                            int docNum = Integer.parseInt(temp.substring(3,idx));
                            bagOfWords[docNum][i]++;
                        }
                    }
                }
                line = br.readLine();
            }
        }
        return bagOfWords;
    }

    public TreeMap<String, ArrayList<String>> getDictionary() {
        return dictionary;
    }
}
