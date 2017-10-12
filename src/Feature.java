import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Feature {

	String name ;
	ArrayList<String> fileNames = new ArrayList<String> () ;
	int LoFC = 0;
	int SD = 0;
	int annotation = 0;
	int fileCounter = 0;
	int TD = 0;
		
	public Feature(String name){
		this.name = name;
		int counter =0;
		String tempFileName = "";
		try (BufferedReader br = new BufferedReader(
				new FileReader(new File(FeatureAnalyze.folder.getAbsolutePath() + "/.vp-files")))) {
			for (String line; (line = br.readLine()) != null;) {
					if(counter%2 ==0){
						 tempFileName = line;
					}else{
						if(line.equalsIgnoreCase(name)){
							fileNames.add(tempFileName);
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
	
	public void analyiseCharacteristic(String AnnotationName, int fileCounter, File folder, String keyWordForIf) {
		File[] listOfFiles = folder.listFiles();
		String beginAnnotation = "//&begin[" + AnnotationName + "]";
		String endAnnotation = "//&end[" + AnnotationName + "]";
		int counter = 0;
		int pairBegin = 0;
		int pairEnd = 0;
		int totalLOC = 0;
		int TD = 0;
		boolean BTD = false;
		int annotationCounter = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
			counter = 0;
			pairBegin = 0;
			pairEnd = 0;

			if (listOfFiles[i].isFile()) {
				try (BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i].getAbsoluteFile()))) {
					String line;
					while ((line = br.readLine()) != null) {
						if (line.toLowerCase().contains(beginAnnotation.toLowerCase())) {
							pairBegin = counter;
							BTD = true;
							annotationCounter++;
						} else if (line.toLowerCase().contains(endAnnotation.toLowerCase())) {
							pairEnd = counter;
							totalLOC += pairEnd - pairBegin - 1;
							pairBegin = 0;
							pairEnd = 0;
							BTD = false;
						}else if (BTD && (line.toLowerCase().contains("#if enable")||line.toLowerCase().contains("#ifdef"))&&!line.toLowerCase().contains(AnnotationName.toLowerCase())) {
							 System.out.println(listOfFiles[i].getName()+" find TD: "+line); //Uncoment to show TD
							TD++;
						}
						counter++;
					}
				} catch (Exception E) {
					System.out.println("error");
				}

			} else if (listOfFiles[i].isDirectory()) {
				// System.out.println("Directory " + listOfFiles[i].getName());
			}

		}
		if (totalLOC > 0) {
			System.out.println("Feature Annotation: [" + AnnotationName + "]");
			System.out.println("LOF: " + totalLOC);
			LoFC = totalLOC;
			System.out.println("SD: " + (annotationCounter + fileCounter) + "(Annotation: " + annotationCounter
					+ "|file: " + fileCounter + ")");
			SD = (annotationCounter + fileCounter);
			annotation = annotationCounter;
			System.out.println("TD: " + TD);
			this.TD =TD; 

			System.out.println("--------------End-----------------");
		}
		
	}
	
	public String getName() {
		return name;
	}

	public ArrayList<String> getFileNames() {
		return fileNames;
	}

	public int getLoFC() {
		return LoFC;
	}

	public int getSD() {
		return SD;
	}

	public int getAnnotation() {
		return annotation;
	}

	public int getFileCounter() {
		return fileCounter;
	}

	public int getTD() {
		return TD;
	}
}
