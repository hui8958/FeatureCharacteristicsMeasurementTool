/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 *
 * @author HP
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//-------------------------------- 
        File folder = new File("C:\\Github\\Marlin\\Marlin");
          String featureName = "HAVE_TMC2130DRIVER";
          String keyWordForSD="#if";
           String keyWordForTD="#if";
//---------------------------------    
        File[] listOfFiles = folder.listFiles();
        String beginAnnotation = "//&begin["+featureName+"]";
          String endAnnotation = "//&end["+featureName+"]";
         int counter = 0;
         int pairBegin = 0;
         int pairEnd = 0;
         int totalLOC = 0;
         int totalSD=0;
         int TD = 0;
         boolean BTD = false;
         int NoPerviousIf = 0;
         int NoPerviousEndIf = 0;
         int annotationCounter = 0;
        for (int i = 0; i < listOfFiles.length; i++) {
            counter =0;
            pairBegin = 0;
             pairEnd = 0;
             NoPerviousIf =0;
             NoPerviousEndIf = 0;
            if (listOfFiles[i].isFile()) {
                try (BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i].getAbsoluteFile()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if(line.contains(beginAnnotation)){
                          System.out.println("Current SD " +(NoPerviousIf-NoPerviousEndIf)+" = Pervious #if ("+NoPerviousIf+") - Pervious #endif ("+NoPerviousEndIf+")"); 
                            pairBegin = counter;
                            BTD = true;
                            totalSD+=(NoPerviousIf-NoPerviousEndIf);
                            annotationCounter++;
                            System.out.println(listOfFiles[i].getAbsolutePath()+" find begin at "+counter);
                        }else if(line.contains(endAnnotation)){
                            pairEnd = counter;
                            System.out.println(listOfFiles[i].getAbsolutePath()+" find end at "+counter);
                            totalLOC += pairEnd-pairBegin-1;
                            pairBegin=0;
                            pairEnd=0;
                            BTD = false;
                        }
                        if(BTD&&line.contains(keyWordForTD)){
                            System.out.println(listOfFiles[i].getName()+" find TD: "+line);
                                TD++;
                        }
                        
                        if(line.contains(keyWordForSD)){
                        	NoPerviousIf++;
                        }else if(line.contains("#endif")){
                        	NoPerviousEndIf++;
                        }
                      counter++;
                    }
                } catch (Exception E) {
                    System.out.println("error");
                }

            } else if (listOfFiles[i].isDirectory()) {
              //  System.out.println("Directory " + listOfFiles[i].getName());
            }
            
        }
        System.out.println("------Feature characteristic statistics------");
        System.out.println("Feature Annotation: ["+featureName+"]");
        System.out.println("LOF:" + totalLOC);
        System.out.println("TD:" + TD);
        System.out.println("Avg. SD:" + totalSD/annotationCounter);
        
    }

}
