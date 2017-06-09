import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Feature {

	String name ;
	ArrayList<String> fileNames = new ArrayList<String> () ;
	int fileCounter = 0;
	
	public Feature(String name){
		this.name = name;
		int counter =0;
		String tempFileName = "";
		try (BufferedReader br = new BufferedReader(
				new FileReader(new File(Main.folder.getAbsolutePath() + "/.vp-files")))) {
			for (String line; (line = br.readLine()) != null;) {
					if(counter%2 ==0){
						 tempFileName = line;
						//System.out.println("File name: "+line);
					}else{
						if(line.equals(name)){
							fileNames.add(tempFileName);
							;
						}	
					}
				
				counter++;
				}
			fileCounter = fileNames.size();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getFileSize(){
		return fileCounter;
	}
}
