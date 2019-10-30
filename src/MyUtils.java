import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException; 

class MyUtils{
	//references: https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
	
	//CONTOH PENGUNAAN: Bila folder dataset mentah terdapat pada direktori C:/xampp/htdocs/TugasPTKI/raw_dataset,
	//maka untuk mendapatkan seluruh file caranya:
	//File input = new File("C:/xampp/htdocs/TugasPTKI/raw_dataset")
	//ArrayList<File> files = MyUtils.listFilesForFolder(input)
	
	/**
	* Membaca seluruh File pada sebuah folder
	* @param folder Objek File yang menyatakan direktori folder berisi file-file yang ingin dibaca
	* @return ArrayList Objek File
	**/
	public static ArrayList<File> listFilesForFolder(File folder){
		ArrayList<File> files = new ArrayList<File>();
		//menggunakan listFiles() biar langsung dapet objek File nya,
		//kalau list() hanya string nama file nya saja.
		for(File f : folder.listFiles()){
			if(f.isDirectory()){
				listFilesForFolder(f);
			}else{
				files.add(f);
				//System.out.println(f.getName());
			}
		}
		return files;
	}
	
        /**
     * Code By Stephen Jordan 2016730018
     *
     */
    public static String[] readFiles(String dir) {
        String[] output = new String[154];
        String input = "";
        try {
            for (int i = 1; i <= 154; i++) {
                String name = "";
                if (i < 10) {
                    name = "00" + i;
                } else if (i >= 10 && i < 100) {
                    name = "0" + i;
                } else if (i >= 100 && i <= 154) {
                    name = "" + i;
                }

                // initialize input
                BufferedReader br = new BufferedReader(new FileReader(dir + "/Doc" + name + ".txt"));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                input = sb.toString();

                br.close();

                output[i - 1] = input;
                input = "";
            }
        } catch (IOException e) {

        }
        return output;
    }
        
	public static void readAllVoid(File f){
        try{
            BufferedReader br = new BufferedReader(new FileReader(f));
			String line="";
			while((line = br.readLine())!= null ){
				System.out.println(line);
			}
        }catch(FileNotFoundException e){
            System.out.println("Error: File: "+f.getName()+" not found!");
        }catch(IOException e){
            System.out.println("Error reading file : "+f.getName());
        }
	}
	
	/**
	* Mengembalkan sebuah ArrayList yang berisi tiap baris pada dokumen
	* @param f File yang ingin dibaca
	* @return ArrayList yang bersi tiap baris pada dokumen
	**/
	public static ArrayList<String> readAll(File f){
            ArrayList<String> lines = new ArrayList<String>();
            try{
                BufferedReader br = new BufferedReader(new FileReader(f));
		String line = "";
		
		while((line = br.readLine())!= null ){
			lines.add(line);
		}
            }catch(FileNotFoundException e){
                System.out.println("Error: File: "+f.getName()+" not found!");
            }catch(IOException e){
                System.out.println("Error reading file : "+f.getName());
            }	
            return lines;
	}
        
         /**
     * Query Result adalah list dokumen dokumen yang ada
     */
    public static void getBagOfWords(ArrayList<String> queryResults){
        //queryResults hanya berisi nama file dalam string biasa
        //sementara bag of words perlu list seluruh kata di dokumen
        //kalau baca dari disk lama lagi... 
    }
}