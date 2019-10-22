class MyUtils{
	//references: https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
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
}