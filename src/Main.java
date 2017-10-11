import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

	public Main(){
		int counter =1;
		String location = "/Users/hui/Documents/Github/Marlin/Marlin";
		FeatureAnalyze featureAnalyze = new FeatureAnalyze(location, "#if");
		try {

			WritableWorkbook book = Workbook.createWorkbook(new File("Features.xls"));
			WritableSheet sheet = book.createSheet("Sheet1", 0);
			Label label1 = new Label(0, 0, "Feature Name");
			Label label2 = new Label(1, 0, "LOF");
			Label label3 = new Label(2, 0, "NOFL");
			Label label4 = new Label(3, 0, "TD");
			Label label5 = new Label(4, 0, "Avg.SD");
			sheet.addCell(label1);
			sheet.addCell(label2);
			sheet.addCell(label3);
			sheet.addCell(label4);
			sheet.addCell(label5);
			for (int i = 0; i < featureAnalyze.features.size(); i++) {
				Feature tempFeature = featureAnalyze.features.get(i);
				if (tempFeature.getLOF() > 0) {
					Label label6 = new Label(0, counter, tempFeature.name);
					jxl.write.Number label7 = new jxl.write.Number(1, counter, tempFeature.getLOF());
					jxl.write.Number label8 = new jxl.write.Number(2, counter, tempFeature.getNOFL());
					jxl.write.Number label9 = new jxl.write.Number(3, counter, tempFeature.getTD());
					jxl.write.Number label0 = new jxl.write.Number(4, counter, tempFeature.getAvgSD());
					sheet.addCell(label6);
					sheet.addCell(label7);
					sheet.addCell(label8);
					sheet.addCell(label9);
					sheet.addCell(label0);
					counter++;
				}
			}
			book.write();
			book.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
}
