package it.unipi.ing.mim.stats;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.unipi.ing.mim.deep.ImgDescriptor;
import it.unipi.ing.mim.deep.Parameters;
import it.unipi.ing.mim.deep.tools.KNNClassifier;
import it.unipi.ing.mim.img.lucene.LucImageSearch;

public class CollectStatistics {
	//directory containing all the images used for testing the system
	private static final String PROBE_DIRECTORY = "data/img_full";
	private static final String STATS_DIRECTORY = "stats/";
	//private static final int NUM_CLASSES=101;
	
	public static void main(String[] args) throws Exception{
		
		File srcFolder = new File(PROBE_DIRECTORY);
		File[] folders = srcFolder.listFiles();
		int numClasses = folders.length;
		
		//create a directory with the current timestamp
		File currentDir = new File(STATS_DIRECTORY +"/"+ System.currentTimeMillis());
		currentDir.mkdirs();
		float totalClassificationPercentageSum=0;
		float totalPrecisionSum=0;
		File globalDir = new File(currentDir.getAbsolutePath() + "/Global");
        if (!globalDir.exists()){
        	globalDir.mkdir();
        }
        
        //initialize the writer needed to write the overall classes statistics
        CsvFileWriter csvAllClassesWriter=new CsvFileWriter(globalDir.getAbsolutePath() +"/overallClassesStats.csv");
        csvAllClassesWriter.appendFinalHeader();
        
		for (File imgFolder: folders) {
			File[] imgFiles = imgFolder.listFiles();
			/*File dir = new File(currentDir.getAbsolutePath() + "/"+ imgFolder.getName());
		        if (!dir.exists()){
		        	dir.mkdir();
		        }*/
			//initialize the writer to write the measurements for the single class
			CsvFileWriter csvPerClassWriter = new CsvFileWriter(currentDir.getAbsolutePath() +"/"+imgFolder.getName()+".csv");
			csvPerClassWriter.appendHeader();
			int count=1;
			float correctlyClassified=0;
			float precisionSum=0;
			for (File imgFile: imgFiles) {
				//Collect statistics for the current probe image
				long starttime = System.currentTimeMillis();
				LucImageSearch l = new LucImageSearch(Parameters.PIVOTS_FILE, Parameters.TOP_K_QUERY);
	            List<ImgDescriptor> foundImages = l.recognizeImage(imgFile);
	            
	            KNNClassifier knn = new KNNClassifier();
	            String classification = knn.classifyTest(foundImages,imgFile.getParentFile().getName());
	            System.out.println("["+imgFolder.getName()+"]: ("+count+"/"+imgFiles.length+") - overall search time for "+imgFile.getName()+": "+(System.currentTimeMillis() - starttime)+"ms");
	            boolean correctClassification=knn.isClassificationOk(imgFile.getParentFile().getName());
	            //record statistics
	            csvPerClassWriter.append(
	            		imgFile.getName(),
	            		correctClassification, 
	            		knn.getPrecision(), 
	            		knn.getRecall(),
	            		knn.getAvgPrecision()
	            	);
	            precisionSum+=knn.getPrecision();
	            if(correctClassification)
	            	correctlyClassified++;
	            count++;
			}
			csvPerClassWriter.close();
			
			//calculates the overall statistics for the current class
			float classificationPercentage=correctlyClassified/(float)imgFiles.length;
			float meanPrecision=precisionSum/(float)imgFiles.length;
			
			//outputs the overall class statistics on file
			csvAllClassesWriter.appendFinalStats(imgFolder.getName(),classificationPercentage, meanPrecision);
			
			totalClassificationPercentageSum+=classificationPercentage;
			totalPrecisionSum+=meanPrecision;
	
		}
		csvAllClassesWriter.close();
		
		//writes the global stats
		CsvFileWriter csvWriterGlobal=new CsvFileWriter(globalDir.getAbsolutePath() +"/globalStats.csv");
		float totalClassificationPercentage=totalClassificationPercentageSum/numClasses;
		float totalMeanPrecision=totalPrecisionSum/numClasses;
		csvWriterGlobal.appendFinalStats("global",totalClassificationPercentage, totalMeanPrecision);
		csvWriterGlobal.close();
	}
}
