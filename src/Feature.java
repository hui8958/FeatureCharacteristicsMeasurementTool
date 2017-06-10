import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Feature {

	String name ;
	ArrayList<String> fileNames = new ArrayList<String> () ;
	int LOF = 0;
	int NOFL = 0;
	int annotation = 0;
	int fileCounter = 0;
	int TD = 0;
	int avgSD = 0;
		
	public Feature(String name){
		this.name = name;
		int counter =0;
		String tempFileName = "";
		try (BufferedReader br = new BufferedReader(
				new FileReader(new File(FeatureAnalyze.folder.getAbsolutePath() + "/.vp-files")))) {
			for (String line; (line = br.readLine()) != null;) {
					if(counter%2 ==0){
						 tempFileName = line;
						//System.out.println("File name: "+line);
					}else{
						if(line.equalsIgnoreCase(name)){
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
	
	public void analyiseCharacteristic(String featureName, int fileCounter, File folder, String keyWordForIf) {
		File[] listOfFiles = folder.listFiles();
		String beginAnnotation = "//&begin[" + featureName + "]";
		String endAnnotation = "//&end[" + featureName + "]";
		int counter = 0;
		int pairBegin = 0;
		int pairEnd = 0;
		int totalLOC = 0;
		int totalSD = 0;
		int TD = 0;
		boolean BTD = false;
		int NoPerviousIf = 0;
		int NoPerviousEndIf = 0;
		int annotationCounter = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
			counter = 0;
			pairBegin = 0;
			pairEnd = 0;
			NoPerviousIf = 0;
			NoPerviousEndIf = 0;
			if (listOfFiles[i].isFile()) {
				try (BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i].getAbsoluteFile()))) {
					String line;
					while ((line = br.readLine()) != null) {
						if (line.toLowerCase().contains(beginAnnotation.toLowerCase())) {
							// System.out.println("Current SD "
							// +(NoPerviousIf-NoPerviousEndIf)+"= Pervious #if
							// ("+NoPerviousIf+") - Pervious #endif
							// ("+NoPerviousEndIf+")"); //Uncoment to show SD
							pairBegin = counter;
							BTD = true;
							totalSD += (NoPerviousIf - NoPerviousEndIf);
							annotationCounter++;
							// System.out.println("<<<<"+listOfFiles[i].getAbsolutePath()+"find
							// begin at "+counter); //Uncoment to show LOF
						} else if (line.toLowerCase().contains(endAnnotation.toLowerCase())) {
							pairEnd = counter;
							// System.out.println("-----"+listOfFiles[i].getAbsolutePath()+"find
							// end at "+counter); //Uncoment to show LOF
							totalLOC += pairEnd - pairBegin - 1;
							pairBegin = 0;
							pairEnd = 0;
							BTD = false;
						}
						if (BTD && line.toLowerCase().contains(keyWordForIf.toLowerCase())) {
							// System.out.println(listOfFiles[i].getName()+"
							// find TD: "+line); //Uncoment to show TD
							TD++;
						}

						if (line.toLowerCase().contains(keyWordForIf.toLowerCase())) {
							NoPerviousIf++;
						} else if (line.toLowerCase().contains("#endif")) {
							NoPerviousEndIf++;
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
			System.out.println("Feature Annotation: [" + featureName + "]");
			System.out.println("LOF: " + totalLOC);
			LOF = totalLOC;
			System.out.println("NOFL: " + (annotationCounter + fileCounter) + "(Annotation: " + annotationCounter
					+ "|file: " + fileCounter + ")");
			NOFL = (annotationCounter + fileCounter);
			annotation = annotationCounter;
			System.out.println("TD: " + TD);
			this.TD =TD; 
			if (annotationCounter > 0) {
				System.out.println("Avg. SD: " + totalSD / annotationCounter);
				this.avgSD =  totalSD / annotationCounter;
			} else {

				System.out.println("Avg. SD: " + totalSD);
				this.avgSD =  totalSD;
			}
			System.out.println("--------------End-----------------");
		}
		
	}
	
	public String getName() {
		return name;
	}

	public ArrayList<String> getFileNames() {
		return fileNames;
	}

	public int getLOF() {
		return LOF;
	}

	public int getNOFL() {
		return NOFL;
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

	public int getAvgSD() {
		return avgSD;
	}

	
}
