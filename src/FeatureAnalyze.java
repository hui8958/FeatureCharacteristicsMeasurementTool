
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
public class FeatureAnalyze {

	/**
	 * @param args
	 *            the command line arguments
	 */
	static File folder;
	String keyWordForIf;
	ArrayList<Feature> features = new ArrayList<Feature>();

	public FeatureAnalyze(String projectLocation, String keyWordForIf) {
		/**
		 * Uncomment for multiple feature analyze (Require clafer)
		 */

		folder = new File(projectLocation);
		this.keyWordForIf = keyWordForIf;
		System.out.println("keyWordForIf: "+this.keyWordForIf);
		AddFeaturesToList();
		for (Feature feature : features) {
			feature.analyiseCharacteristic(feature.name, feature.fileCounter, folder, this.keyWordForIf);
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
				if (!line.toLowerCase().contains("Marlin".toLowerCase())) {
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

	
}
