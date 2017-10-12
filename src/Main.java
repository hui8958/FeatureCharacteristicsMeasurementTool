import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

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
		String location = "D:/Github/Marlin/Marlin";
		String excelName = "Features.xls";
		FeatureAnalyze featureAnalyze = new FeatureAnalyze(location, "#if");
		try {

			WritableWorkbook book = Workbook.createWorkbook(new File(excelName));
			WritableSheet sheet = book.createSheet("Sheet1", 0);
			Label label1 = new Label(0, 0, "Annotation Name");
			Label label2 = new Label(1, 0, "LoFC");
			Label label3 = new Label(2, 0, "SD");
			Label label4 = new Label(3, 0, "TD");
			sheet.addCell(label1);
			sheet.addCell(label2);
			sheet.addCell(label3);
			sheet.addCell(label4);
			for (int i = 0; i < featureAnalyze.features.size(); i++) {
				Feature tempFeature = featureAnalyze.features.get(i);
				if (tempFeature.getLoFC() > 0) {
					Label label6 = new Label(0, counter, tempFeature.name);
					jxl.write.Number label7 = new jxl.write.Number(1, counter, tempFeature.getLoFC());
					jxl.write.Number label8 = new jxl.write.Number(2, counter, tempFeature.getSD());
					jxl.write.Number label9 = new jxl.write.Number(3, counter, tempFeature.getTD());

					sheet.addCell(label6);
					sheet.addCell(label7);
					sheet.addCell(label8);
					sheet.addCell(label9);
					counter++;
				}
			}
			book.write();
			book.close();
			Desktop.getDesktop().open(new File(excelName));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
}
