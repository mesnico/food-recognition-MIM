package it.unipi.ing.mim.stats;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.unipi.ing.mim.deep.ImgDescriptor;
import it.unipi.ing.mim.deep.Parameters;
import it.unipi.ing.mim.deep.tools.KNNClassifier;
import it.unipi.ing.mim.img.lucene.LucImageSearch;

public class CollectClassificationStatistics {
	//directory containing all the images used for testing the system
	private static final String PROBE_DIRECTORY = "data/test";
	private static final String STATS_DIRECTORY = "stats/";
	//private static final int NUM_CLASSES=101;
	
	public static void main(String[] args) throws Exception{
		
		File srcFolder = new File(PROBE_DIRECTORY);
		File[] folders = srcFolder.listFiles();
		int numClasses = folders.length;
		//Data structure to save Class and Id 
		HashMap<String,Integer> resultMap=new HashMap<String,Integer>();
		String label;
		int value=1;
		for(File folderName : folders){
			label=folderName.getName();
			resultMap.put(label,value++);
		}
		
		//create a directory with the current timestamp
		File currentDir = new File(STATS_DIRECTORY +"/"+ System.currentTimeMillis());
		currentDir.mkdirs();
		File globalDir = new File(currentDir.getAbsolutePath() + "/Classification");
        if (!globalDir.exists()){
        	globalDir.mkdir();
        }
        
    
        //initialize all the needed statistic files (2 for every K)
        int testK[] = {1,5,10,15};
        CsvFileWriter[] csvConfusionMatrixWriters = new CsvFileWriter[testK.length];
        CsvFileWriter[] csvClassificationSummaryWriters = new CsvFileWriter[testK.length];
        for (int i=0; i<testK.length; i++){
        	int k = testK[i];
        	
            csvConfusionMatrixWriters[i]=new CsvFileWriter(globalDir.getAbsolutePath() +"/ConfusionMatrixStats_"+k+"NN.csv");
            csvConfusionMatrixWriters[i].appendConfusionMatrixHeader();
            csvClassificationSummaryWriters[i] = new CsvFileWriter(globalDir.getAbsolutePath() +"/ClassificationSummaryStats_"+k+"NN.csv");
            csvClassificationSummaryWriters[i].appendClassificationSummaryHeader();
        }
        
        //extract features from every image once and compute the classification testK.length times
		for (File imgFolder: folders) {
			File[] imgFiles = imgFolder.listFiles();
			
			int count = 0;
			float[] correctlyClassified= new float[testK.length];
			for (File imgFile: imgFiles) {
				//Collect statistics for the current probe image
				long starttime = System.currentTimeMillis();
				LucImageSearch l = new LucImageSearch(Parameters.PIVOTS_FILE, Parameters.TOP_K_QUERY);
	            List<ImgDescriptor> foundImages = l.recognizeImage(imgFile);
	            
	            System.out.println("["+imgFolder.getName()+"]: ("+count+"/"+imgFiles.length+") - overall search time for "+imgFile.getName()+": "+(System.currentTimeMillis() - starttime)+"ms");
	            
	            KNNClassifier knn = new KNNClassifier();
	            
	            //performs the classification multiple times varying K
	            for(int i=0; i<testK.length; i++){
	            	int k = testK[i];
	            	
		            String classification = knn.classifyTest(foundImages,imgFile.getParentFile().getName(), k);
		            int actualClassId = resultMap.get(classification);
		            int targetClassId = resultMap.get(imgFolder.getName());
		          
		            boolean correctClassification=knn.isClassificationOk(imgFile.getParentFile().getName());
		            
		            //record statistics
		            csvConfusionMatrixWriters[i].appendConfusionMatrix(
		            		imgFile.getName(),
		            		targetClassId,
		            		actualClassId
		            	);
		            if(correctClassification)
		            	correctlyClassified[i]++;
	            }
	            count++;
			}
			
			//calculate the classification percentage for every K
			for(int i=0; i<testK.length; i++){
				float classificationPercentage=correctlyClassified[i]/(float)imgFiles.length;
			
				//outputs the overall class statistics on file
				csvClassificationSummaryWriters[i].appendClassificationSummary(imgFolder.getName(),classificationPercentage);
			}
		}
		
		//close all files
		for(int i=0; i<testK.length; i++){
			csvConfusionMatrixWriters[i].close();
			csvClassificationSummaryWriters[i].close();
		}
	}
}
