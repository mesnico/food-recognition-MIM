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
	
	public static void main(String[] args) throws Exception{
		
		File srcFolder = new File(PROBE_DIRECTORY);
		File[] folders = srcFolder.listFiles();
		
		//create a directory with the current timestamp
		File currentDir = new File(STATS_DIRECTORY +"/"+ System.currentTimeMillis());
		currentDir.mkdirs();
		
		for (File imgFolder: folders) {
			File[] imgFiles = imgFolder.listFiles();
			CsvFileWriter csvWriter = new CsvFileWriter(currentDir.getAbsolutePath() + "/" + imgFolder.getName()+".csv");
			int count=1;
			for (File imgFile: imgFiles) {
				//Collect statistics for the current probe image
				
				long starttime = System.currentTimeMillis();
				LucImageSearch l = new LucImageSearch(Parameters.PIVOTS_FILE, Parameters.TOP_K_QUERY);
	            List<ImgDescriptor> foundImages = l.recognizeImage(imgFile);
	            
	            KNNClassifier knn = new KNNClassifier();
	            String classification = knn.classify(foundImages);
	            System.out.println("["+imgFolder.getName()+"]: ("+count+"/"+imgFiles.length+") - overall search time for "+imgFile.getName()+": "+(System.currentTimeMillis() - starttime)+"ms");
	            
	            //record statistics
	            csvWriter.append(
	            		knn.isClassificationOk(), 
	            		knn.getPrecision(), 
	            		knn.getRecall()
	            	);
	            
	            count++;
			}
			csvWriter.close();
		}
	}
}
