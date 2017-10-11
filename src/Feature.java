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
	int SD = 0;
	int annotation = 0;
	int fileCounter = 0;
	int TD = 0;
	int avgND = 0;
		
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
		int totalND = 0;
		int TD = 0;
		boolean BTD = false;
		int NoPerviousIf = 0;
		int NoPerviousIfdef = 0;
		int NoPerviousEndIf = 0;
		int annotationCounter = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
			counter = 0;
			pairBegin = 0;
			pairEnd = 0;
			NoPerviousIf = 0;
			NoPerviousIfdef= 0;
			NoPerviousEndIf = 0;
			if (listOfFiles[i].isFile()) {
				try (BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i].getAbsoluteFile()))) {
					String line;
					while ((line = br.readLine()) != null) {
						if (line.toLowerCase().contains(beginAnnotation.toLowerCase())) {
							// System.out.println("Current ND "
							// +(NoPerviousIf-NoPerviousEndIf)+"= Pervious #if
							// ("+NoPerviousIf+") - Pervious #endif
							// ("+NoPerviousEndIf+")"); //Uncoment to show ND

							pairBegin = counter;
							BTD = true;
							System.out.println("Start Searching for TD");
							/*if(keyWordForIf.toLowerCase().equals("#if")){
								totalND += (NoPerviousIf+NoPerviousIfdef - NoPerviousEndIf);
							}else{
								totalND += NoPerviousIf+NoPerviousIfdef - 2*NoPerviousEndIf;
							}*/
							
							annotationCounter++;
						} else if (line.toLowerCase().contains(endAnnotation.toLowerCase())) {
							pairEnd = counter;
							totalLOC += pairEnd - pairBegin - 1;
							pairBegin = 0;
							pairEnd = 0;
							BTD = false;
							System.out.println("End Searching for TD");
						}else if (BTD && (line.toLowerCase().contains("#if enable")||line.toLowerCase().contains("#ifdef"))&&!line.toLowerCase().contains(featureName.toLowerCase())) {
							 System.out.println(listOfFiles[i].getName()+" find TD: "+line); //Uncoment to show TD
							TD++;
						}

						/*
						if (line.toLowerCase().contains("#if ")) {
							NoPerviousIf++;
						} else if (line.toLowerCase().contains("#ifdef ")) {
							NoPerviousIfdef++;
						}else if (line.toLowerCase().contains("#endif")) {
							NoPerviousEndIf++;
						}
						
						*/
						/*
						if (line.toLowerCase().contains(keyWordForIf.toLowerCase()+" ")) {
							NoPerviousIf++;
						} else if (line.toLowerCase().contains("#endif")) {
							NoPerviousEndIf++;
						}
						*/
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
			System.out.println("SD: " + (annotationCounter + fileCounter) + "(Annotation: " + annotationCounter
					+ "|file: " + fileCounter + ")");
			SD = (annotationCounter + fileCounter);
			annotation = annotationCounter;
			System.out.println("TD: " + TD);
			this.TD =TD; 
			
			if(keyWordForIf.toLowerCase().equals("#if")){
				if (annotationCounter > 0) {
					System.out.println("Avg. ND: " + totalND / annotationCounter);
					this.avgND =  totalND / annotationCounter;
				} else {

					System.out.println("Avg. ND: " + totalND);
					this.avgND =  totalND;
				}
			}else if(keyWordForIf.toLowerCase().equals("#ifdef")){
				if (annotationCounter > 0) {
					System.out.println("Avg. ND: " + totalND / annotationCounter);
					this.avgND =  totalND / annotationCounter;
				} else {

					System.out.println("Avg. ND: " + totalND);
					this.avgND =  totalND;
				}
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

	public int getAvgND() {
		return avgND;
	}

	
}
