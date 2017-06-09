
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 *
 * @author HP
 */
public class Main {

	/**
	 * @param args
	 *            the command line arguments
	 */
	static File folder = new File("C:\\Github\\Marlin\\Marlin");
	String featureName = "HAVE_TMC2130DRIVER"; // not reqiured in multiple mode
	String keyWordForIf = "#if";
	ArrayList<Feature> features = new ArrayList<Feature>();
	public static void main(String[] args) {
		// TODO code application logic here
		Main main = new Main();

	}

	public Main() {
		/**
		 * Uncomment for multiple feature analyze (Require clafer)
		 */

		AddFeaturesToList();
		for (Feature feature : features) {
			analyiseCharacteristic(feature.name,feature.fileCounter, folder, keyWordForIf);
		}

		/**
		 * Uncomment for single feaure analyze
		 */
		// analyiseCharacteristic(featureName, folder, keyWordForIf);

	}

	private void AddFeaturesToList() {
		int featureCounter = 0;
		try (BufferedReader br = new BufferedReader(
				new FileReader(new File(folder.getAbsolutePath() + "/.vp-project")))) {
			for (String line; (line = br.readLine()) != null;) {
				if (!line.contains("Marlin")) {
					Feature feature;
					String oneLine = line.trim().replace("xor ", "");
					if (oneLine.contains(" ")) {
						oneLine = oneLine.substring(0, oneLine.indexOf(" "));
					} 
					feature = new Feature(oneLine);
					features.add(feature);
					featureCounter++;
					System.out.println("Add feature: [" + oneLine + "]");
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total Number of Features: " + featureCounter);
	}

	private void analyiseCharacteristic(String featureName, int fileCounter,File folder, String keyWordForIf) {
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
						if (line.contains(beginAnnotation)) {
							// System.out.println("Current SD "
							// +(NoPerviousIf-NoPerviousEndIf)+"= Pervious #if
							// ("+NoPerviousIf+") - Pervious #endif
							// ("+NoPerviousEndIf+")"); //Uncoment to show SD
							pairBegin = counter;
							BTD = true;
							totalSD += (NoPerviousIf - NoPerviousEndIf);
							annotationCounter++;
							 System.out.println("<<<<"+listOfFiles[i].getAbsolutePath()+"find begin at "+counter); //Uncoment to show LOF
						} else if (line.contains(endAnnotation)) {
							pairEnd = counter;
							 System.out.println("-----"+listOfFiles[i].getAbsolutePath()+"find end at "+counter); //Uncoment to show LOF
							totalLOC += pairEnd - pairBegin - 1;
							pairBegin = 0;
							pairEnd = 0;
							BTD = false;
						}
						if (BTD && line.contains(keyWordForIf)) {
							// System.out.println(listOfFiles[i].getName()+"
							// find TD: "+line); //Uncoment to show TD
							TD++;
						}

						if (line.contains(keyWordForIf)) {
							NoPerviousIf++;
						} else if (line.contains("#endif")) {
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
			if(totalLOC>0){
		System.out.println("Feature Annotation: [" + featureName + "]");
		System.out.println("LOF: " + totalLOC);
		System.out.println("NOFL: " + (annotationCounter+fileCounter) + "(Annotation: "+ annotationCounter+"|file: "+fileCounter+")");
		System.out.println("TD: " + TD);
		if (annotationCounter > 0) {
			System.out.println("Avg. SD: " + totalSD / annotationCounter);
		} else {

			System.out.println("Avg. SD: " + totalSD);
		}
		System.out.println("--------------End-----------------");
			}

	}

}
